package ax.ha.it.fragmentsdemo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Advice.class}, version = 2, exportSchema = false)
public abstract class AdviceDataBase extends RoomDatabase {

    public abstract AdviceDAO AdvDao();

    private static volatile AdviceDataBase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AdviceDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AdviceDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AdviceDataBase.class, "advice_table")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }



}
