package ax.ha.it.fragmentsdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ax.ha.it.fragmentsdemo.databinding.FragmentSecondBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private myViewModel viewModel;
    Advice advice = new Advice("","","");
    String[] categories = {"Lifestyle", "Technology", "Miscellaneous"};
    List<Category> list = new LinkedList<Category>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(myViewModel.class);

        //add initial categories
        list.add(new Category(categories[0]));
        list.add(new Category(categories[1]));
        list.add(new Category(categories[2]));
        CategoryDao Catdb = CategoryDatabase.getDatabase(this.getContext()).CategoryDao();
        new Thread( () -> {
            //Catdb.deleteAll(); //Delete category tests
            Catdb.insert(list.get(0));
            Catdb.insert(list.get(1));
            Catdb.insert(list.get(2));
        }).start();

        Catdb.getAlphabetizedCategories().observe(getViewLifecycleOwner(), allCategories -> {
            ArrayList<String> spinnerList = new ArrayList<>();
            for(int i = 0; i < allCategories.size(); i++) {
                spinnerList.add(allCategories.get(i).getCategory());
            }
            ArrayAdapter adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerList);
            binding.categorySpinner.setAdapter(adapter);
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        PreferenceManager.setDefaultValues(getContext(), R.xml.root_preferences, false);
        binding.enterAuthor.setText(sharedPreferences.getString("Author", ""));

        for(int i = 0; i < categories.length; i++) {
            if(sharedPreferences.getString("Reply", "").equals(categories[i])) {
                binding.categorySpinner.setSelection(i);
            }
        }

        if(getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            binding.enterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    advice.setContent(binding.enterContent.getText().toString());
                    advice.setAuthor("Author: " + binding.enterAuthor.getText().toString());
                    advice.setCategory(binding.categorySpinner.getSelectedItem().toString());
                    viewModel.addAdvice(advice);
                    AdviceDAO dao = AdviceDataBase.getDatabase(getContext()).AdvDao();
                    new Thread( () -> {
                        dao.insert(advice);

                        AdviceService adviceService = RetrofitClient.getRetrofit().create(AdviceService.class);
                        Call<String> call = adviceService.addAdvice(advice.advice, advice.author, advice.category);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.e("SecondFragment", "onResponse: " + response.code());
                                Log.e("SecondFragment", "onResponse: " + call.toString());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("SecondFragment", "onFailure " + t.getMessage());
                            }
                        });

                        //dao.deleteAll(); //Delete all test advices
                        System.out.println("Added advice to db ");
                    }).start();

                    Navigation.findNavController(view).navigate(
                            R.id.action_secondFragment_to_firstFragment);
                }
            });
            binding.settingsButton.setOnClickListener(
                    view -> Navigation.findNavController(view).navigate(
                            R.id.action_secondFragment_to_settingsActivity));
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork =
                    connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) &&
                    (activeNetwork.isConnected());
        }
        return isConnected;
    }

}
