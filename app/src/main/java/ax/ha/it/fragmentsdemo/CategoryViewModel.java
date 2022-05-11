package ax.ha.it.fragmentsdemo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;

    private final LiveData<List<Category>> allCategories;

    public CategoryViewModel (Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
    }

    LiveData<List<Category>> getAllCategories() { return allCategories; }

    public void insert(Category category) { categoryRepository.insert(category); }
}
