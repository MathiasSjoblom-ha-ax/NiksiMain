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
import java.util.List;

import ax.ha.it.fragmentsdemo.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private myViewModel viewModel;
    Advice advice = new Advice("","","");
    String[] categories = {"Lifestyle", "Technology", "Miscellaneous"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(myViewModel.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        PreferenceManager.setDefaultValues(getContext(), R.xml.root_preferences, false);
        binding.enterAuthor.setText(sharedPreferences.getString("Author", ""));
        binding.categorySpinner.setSelection(sharedPreferences.getInt("Reply", 0));


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
