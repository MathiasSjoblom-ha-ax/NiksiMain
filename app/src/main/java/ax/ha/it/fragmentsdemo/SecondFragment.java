package ax.ha.it.fragmentsdemo;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ax.ha.it.fragmentsdemo.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private myViewModel viewmodel;
    Advice advice = new Advice("","","");
    //private Advice advice;
    //int index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        viewmodel = new ViewModelProvider(requireActivity()).get(myViewModel.class);

        if(getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            binding.enterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    advice.setContent(binding.enterContent.getText().toString());
                    advice.setAuthor("Author: " + binding.enterAuthor.getText().toString());
                    advice.setCategory(binding.categorySpinner.getSelectedItem().toString());
                    viewmodel.addAdvice(advice);
                    Navigation.findNavController(view).navigate(
                            R.id.action_secondFragment_to_firstFragment);
                }
            });
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
