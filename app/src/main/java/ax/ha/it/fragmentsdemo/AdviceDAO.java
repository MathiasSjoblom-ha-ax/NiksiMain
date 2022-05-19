package ax.ha.it.fragmentsdemo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AdviceDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Advice advice);

    @Delete
    void delete(Advice... advice);

    @Query("DELETE FROM advice_table")
    void deleteAll();

    @Update
    void update(Advice... advice);

    @Query("SELECT * FROM advice_table ORDER BY author ASC")
    LiveData<List<Advice>> getAdviceInOrder();
}
