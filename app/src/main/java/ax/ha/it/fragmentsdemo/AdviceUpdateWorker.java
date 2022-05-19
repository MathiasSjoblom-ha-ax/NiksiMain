package ax.ha.it.fragmentsdemo;

import static ax.ha.it.fragmentsdemo.FirstFragment.getRetrofitCategoryCall2;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdviceUpdateWorker extends Worker {

    public AdviceUpdateWorker(Context context, WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        final int[] tempResult = {0};
        CategoryDao Catdb = CategoryDatabase.getDatabase(getApplicationContext()).CategoryDao();
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
                        Toast.makeText(getApplicationContext(), "Error fetching categories from API * " + error, Toast.LENGTH_SHORT).show();
                        tempResult[0] = 1;
                    } catch (IOException e) {
                        Log.e("SecondFragment", "Bad response: "+e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, @NonNull Throwable t) {
                Log.e("NiksiProject", "Failure: "+t);
                tempResult[0] = 1;
            }
        });
        if(tempResult[0] == 1) {
            System.out.println("Failed to updated advice ");
            return Result.failure();
        } else {
            System.out.println("Sucessfully updated advice ");
            return Result.success();
        }
    }
}
