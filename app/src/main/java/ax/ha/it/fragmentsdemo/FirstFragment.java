package ax.ha.it.fragmentsdemo;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ax.ha.it.fragmentsdemo.databinding.FragmentFirstBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private myViewModel viewmodel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WorkManager workManager = WorkManager.getInstance(this.getContext());
        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(AdviceUpdateWorker.class, 15, TimeUnit.MINUTES)
                                .build();

        workManager.enqueueUniquePeriodicWork(
                "AdviceUpdate",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest);

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        RecyclerView recyclerView = binding.contentRecyclerView;
        viewmodel = new ViewModelProvider(getActivity()).get(myViewModel.class);

        if (getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            binding.addButtonMain.setOnClickListener(
                    view -> Navigation.findNavController(view).navigate(
                            R.id.action_firstFragment_to_secondFragment));
        }

        if (!isConnectedToNetwork(this.getContext())) {
            Toast.makeText(this.getContext(), "No network!", Toast.LENGTH_SHORT).show();
        } else {
            CategoryDao Catdb = CategoryDatabase.getDatabase(this.getContext()).CategoryDao();
            Call<List<Category>> call = getRetrofitCategoryCall2();
            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                    if (response.isSuccessful()) {
                        List<Category> AllCategories = response.body();
                        new Thread( () -> {
                            for(int i = 0; i < AllCategories.size(); i++) {
                                Catdb.insert(new Category(AllCategories.get(i).getCategory()));
                            }
                        }).start();
                    } else {
                        try {
                            String error = response.errorBody().string();
                            Toast.makeText(getContext(), "Error fetching categories from API * " + error, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.e("SecondFragment", "Bad response: "+e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, @NonNull Throwable t) {
                    Log.e("NiksiProject", "Failure: "+t);
                }
            });
        }

        binding.getAdviceButton.setOnClickListener(view -> {
            if (!isConnectedToNetwork(this.getContext())) {
                Toast.makeText(this.getContext(), "No network!", Toast.LENGTH_SHORT).show();
            }

            Call<List<Advice>> call = getRetrofitAdviceCall();
            call.enqueue(new Callback<List<Advice>>() {
                @Override
                public void onResponse(@NonNull Call<List<Advice>> call, @NonNull Response<List<Advice>> response) {
                    if (response.isSuccessful()) {
                        List<Advice> AllAdvises = response.body();
                        CustomAdapter recyclerAdapter = new CustomAdapter(viewmodel.getAdvice().getValue().toArray(new Advice[0]));
                        Advice[] tempAdvices = new Advice[AllAdvises.size()];
                        for(int i = 0; i < AllAdvises.size(); i++) {
                            tempAdvices[i] = AllAdvises.get(i);
                        }
                        recyclerAdapter = new CustomAdapter(tempAdvices);
                        recyclerView.setAdapter(recyclerAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    } else {
                        try {
                            String error = response.errorBody().string();
                            Toast.makeText(getContext(), "Error fetching data from API * " + error, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.e("FirstFragment", "Bad response: "+e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Advice>> call, @NonNull Throwable t) {
                    Log.e("NiksiProject", "Failure: "+t);
                }
            });

        });

        AdviceDAO Advdb = AdviceDataBase.getDatabase(this.getContext()).AdvDao();
        Advdb.getAdviceInOrder().observe(getViewLifecycleOwner(), AllAdvises -> {
            CustomAdapter recyclerAdapter = new CustomAdapter(viewmodel.getAdvice().getValue().toArray(new Advice[0]));
            if(AllAdvises.size() != 0) {
                Advice[] tempAdvices = new Advice[AllAdvises.size()];
                for(int i = 0; i < AllAdvises.size(); i++) {
                    tempAdvices[i] = AllAdvises.get(i);
                }
                recyclerAdapter = new CustomAdapter(tempAdvices);
            }
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        });
        /*
        CustomAdapter recyclerAdapter = new CustomAdapter(viewmodel.getAdvice().getValue().toArray(new Advice[0]));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
         */

        return binding.getRoot();
    }

    static Call<List<Category>> getRetrofitCategoryCall2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://niksipirkka.cloud-ha.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryService service = retrofit.create(CategoryService.class);
        return service.getCategories();
    }

    static Call<List<Advice>> getRetrofitAdviceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://niksipirkka.cloud-ha.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AdviceService service = retrofit.create(AdviceService.class);
        return service.getAdvices();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
