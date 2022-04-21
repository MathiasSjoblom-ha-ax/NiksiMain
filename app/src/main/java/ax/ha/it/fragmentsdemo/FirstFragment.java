package ax.ha.it.fragmentsdemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ax.ha.it.fragmentsdemo.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private myViewModel viewmodel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        if (getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            binding.addButtonMain.setOnClickListener(
                    view -> Navigation.findNavController(view).navigate(
                            R.id.action_firstFragment_to_secondFragment));
        }

        RecyclerView recyclerView = binding.contentRecyclerView;
        viewmodel = new ViewModelProvider(getActivity()).get(myViewModel.class);
        //Will not work later, use observe instead of get value
        CustomAdapter recyclerAdapter = new CustomAdapter(viewmodel.getAdvice().getValue().toArray(new Advice[0]));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
