package ax.ha.it.fragmentsdemo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AdviceService {

    @GET("getadvice")
    Call<List<Advice>> getAdvices();

    @POST("addadvice")
    @FormUrlEncoded
    Call<String> addAdvice(
            @Field("advice") String content,
            @Field("author") String author,
            @Field("category") String category);

}
