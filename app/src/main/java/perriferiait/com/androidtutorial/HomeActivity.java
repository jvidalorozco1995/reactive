package perriferiait.com.androidtutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import perriferiait.com.androidtutorial.yahoo.RetrofitYahooServiceFactory;
import perriferiait.com.androidtutorial.yahoo.YahooService;


public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.hello_world_salute)
    TextView helloText;


    @BindView(R.id.stock_updates_recyclerview)
    RecyclerView recyclerView;


    private LinearLayoutManager layoutManager;
    private StockDataAdapter stockDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        //Dos maneras de hacer lo mismo
        Observable.just("Hello ! please use this app responsibly")
                .subscribe(s -> helloText.setText(s));

        Observable.just("Hello ! please use this app responsibly")
                .subscribe(helloText::setText);

        YahooService yahooService = new RetrofitYahooServiceFactory().create();

        //Parametros del query
        String query = "select * from yahoo.finance.quote where symbol in ('YHOO','AAPL','GOOG','MSFT')";
        String env = "store://datatables.org/alltableswithkeys";


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        stockDataAdapter = new StockDataAdapter();
        recyclerView.setAdapter(stockDataAdapter);

        //Subscripcion al servicio
        yahooService.yglQuery(query, env)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> log(
                        data.getQuery().getResults().getQuote().get(0).getSymbol()
                ));

        yahooService.yglQuery(query, env)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .map(r -> r.getQuery().getResults().getQuote());

       /* yahooService.yglQuery(query, env)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .map(r -> r.getQuery().getResults().getQuote())
                .flatMap(Observable::fromIterable)
                .map(StockUpdate::create)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockUpdate -> {
                    Log.d("APP", "New Update" + stockUpdate.getStockSymbol());
                    stockDataAdapter.add(stockUpdate);
                });*/

        //Cada 5 segundos refresca
        Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap(i -> yahooService.yglQuery(query, env)
                        .toObservable()
                )
                .subscribeOn(Schedulers.io())
                .map(r -> r.getQuery().getResults().getQuote())
                .flatMap(Observable::fromIterable)
                .map(StockUpdate::create)
                //  .doOnNext(this::saveStockUpdate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockUpdate -> {
                    Log.d("APP", "New Update" + stockUpdate.getStockSymbol());
                    stockDataAdapter.add(stockUpdate);
                });

//        Observable.just("APPLE","GOOGLE","TWRITER")
//                .subscribe(stockSymbol ->
//                        stockDataAdapter.fgadfdfd(stockSymbol));


      /*  Observable.just(
                new StockUpdate("GOOGLE", 12.43, new Date()),
                new StockUpdate("APPLE", 645.1, new Date()),
                new StockUpdate("TWRITTER", 1.43, new Date())
        ).subscribe(stockUpdate -> {
            Log.d(HomeActivity.class.getSimpleName(),"New Update ".concat(stockUpdate.getStockSymbol()));
            stockDataAdapter.add(stockUpdate);
        });*/

    }


    private void log(Throwable throwable) {
        Log.e("APP", "Error", throwable);
    }

    private void log(String stage, String item) {
        Log.d("APP", stage + ":" + Thread.currentThread().getName() + ":" + item);
    }

    private void log(String stage) {
        Log.d("APP", stage + ":" + Thread.currentThread().getName());
    }

}
