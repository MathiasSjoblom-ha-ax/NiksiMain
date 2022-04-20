package ax.ha.it.fragmentsdemo;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ax.ha.it.fragmentsdemo.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    int counter = 0;
    String[] content;
    String[] author;
    String[] category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        if(getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            binding.enterButton.setOnClickListener(
                    view -> Navigation.findNavController(view).navigate(
                                        R.id.action_secondFragment_to_firstFragment));
            content[counter] = binding.enterContent.getText().toString();
            author[counter] = binding.enterAuthor.getText().toString();
            category[counter] = binding.categorySpinner.getSelectedItem().toString();
            counter++;
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
