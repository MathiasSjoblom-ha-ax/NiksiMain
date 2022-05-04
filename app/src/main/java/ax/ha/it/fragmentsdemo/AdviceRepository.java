package ax.ha.it.fragmentsdemo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AdviceRepository {

    private AdviceDAO adviceDao;
    private LiveData<List<Advice>> allAdvice;

    AdviceRepository(Application application) {
        AdviceDataBase db = AdviceDataBase.getDatabase(application);
        adviceDao = db.AdvDao();
        allAdvice = adviceDao.getAdviceInOrder();
    }

    LiveData<List<Advice>> getAllAdvice() {
        return allAdvice;
    }

    void insert(Advice advice) {
        AdviceDataBase.databaseWriteExecutor.execute(() -> {
            adviceDao.insert(advice);
        });
    }

}
