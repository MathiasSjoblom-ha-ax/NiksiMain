package ax.ha.it.fragmentsdemo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoryService {

    @GET("getcategories")
    Call<List<Category>> getCategories();

    @POST("addcategory")
    @FormUrlEncoded
    Call<Category> addCategory(@Field("name") String category);

}
