package perriferiait.com.androidtutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import perriferiait.com.androidtutorial.yahoo.RetrofitYahooServiceFactory;
import perriferiait.com.androidtutorial.yahoo.YahooService;
import perriferiait.com.androidtutorial.yahoo.json.YahooStockResult;
import perriferiait.com.androidtutorial.yahoo.storio.StockUpdateTable;
import perriferiait.com.androidtutorial.yahoo.storio.StorIOFactory;


import static hu.akarnokd.rxjava.interop.RxJavaInterop.toV2Observable;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.hello_world_salute)
    TextView helloText;


    @BindView(R.id.stock_updates_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id. no_data_available)
    TextView noDataAvailableView;

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
                 .flatMap(i -> Observable.<YahooStockResult>error(new RuntimeException("Oops")))
               // .flatMap(i -> yahooService.yglQuery(env,query).toObservable())
                .subscribeOn(Schedulers.io())
                .map(r -> r.getQuery().getResults().getQuote())
                .flatMap(Observable::fromIterable)
                .map(StockUpdate::create)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> {
                    log("doOnError", "error");
                    Toast.makeText(this, "We couldn't reach internet - falling back to local data",
                            Toast.LENGTH_SHORT)
                            .show();
                })
                //.doOnError(ErrorHandler.get())
                .doOnNext(this::saveStockUpdate)
                .onExceptionResumeNext(
                        v2(StorIOFactory.get(this)
                                .get()
                                .listOfObjects(StockUpdate.class)
                                .withQuery(Query.builder()
                                        .table(StockUpdateTable.TABLE)
                                        .orderBy("date DESC")
                                        .limit(50)
                                        .build())
                                .prepare()
                                .asRxObservable())
                                .take(1)
                                .flatMap(Observable::fromIterable)
                )

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockUpdate -> {
                    Log.d("APP", "New Update" + stockUpdate.getStockSymbol());
                    noDataAvailableView.setVisibility(View.GONE);
                    stockDataAdapter.add(stockUpdate);
                }, error -> {
                    if (stockDataAdapter.getItemCount() == 0) {
                        noDataAvailableView.setVisibility(View.VISIBLE);
                    }
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

    public static <T> Observable<T> v2(rx.Observable<T> source) {
        return toV2Observable(source);
    }

    private void saveStockUpdate(StockUpdate stockUpdate) {
        log("saveStockUpdate", stockUpdate.getStockSymbol());
        StorIOFactory.get(this).put().object(stockUpdate).prepare().asRxSingle().subscribe();
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
