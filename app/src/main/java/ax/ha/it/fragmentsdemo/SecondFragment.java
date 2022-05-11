package ax.ha.it.fragmentsdemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ax.ha.it.fragmentsdemo.databinding.FragmentSecondBinding;

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

        /*
        LiveData<List<Category>> list22 = db.CategoryDao().getAlphabetizedCategories();
        ArrayList<String> spinnerList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            spinnerList.add(list22.getValue().get(i).getCategory());
        }
        ArrayAdapter<String> adapter22 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerList);
        binding.categorySpinner.setAdapter(adapter22);

         */

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

}
