package perriferiait.com.androidtutorial.yahoo;

import io.reactivex.Single;
import perriferiait.com.androidtutorial.yahoo.json.YahooStockResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YahooService {
    @GET("yql?format=json")
    Single<YahooStockResult> yglQuery(@Query("q") String query, @Query("env") String env);
}

/*
* https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys
* */