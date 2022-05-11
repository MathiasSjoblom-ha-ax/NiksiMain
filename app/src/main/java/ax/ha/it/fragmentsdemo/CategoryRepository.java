package ax.ha.it.fragmentsdemo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepository {

    private CategoryDao mCategoryDao;
    private LiveData<List<Category>> allCategories;

    CategoryRepository(Application application) {
        CategoryDatabase db = CategoryDatabase.getDatabase(application);
        mCategoryDao = db.CategoryDao();
        allCategories = mCategoryDao.getAlphabetizedCategories();
    }

    LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    void insert(Category category) {
        CategoryDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.insert(category);
        });
    }
}
