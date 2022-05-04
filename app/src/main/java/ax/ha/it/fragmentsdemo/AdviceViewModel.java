package ax.ha.it.fragmentsdemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AdviceViewModel extends AndroidViewModel {
    private AdviceRepository adviceRepository;
    private final LiveData<List<Advice>> allAdvice;

    public AdviceViewModel(@NonNull Application application) {
        super(application);
        adviceRepository = new AdviceRepository(application);
        allAdvice = adviceRepository.getAllAdvice();
    }

    LiveData<List<Advice>> getAllAdvice() { return allAdvice;}

    public void insert(Advice advice) {adviceRepository.insert(advice);}

}