package perriferiait.com.androidtutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


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


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        stockDataAdapter = new StockDataAdapter();
        recyclerView.setAdapter(stockDataAdapter);
//        Observable.just("APPLE","GOOGLE","TWRITER")
//                .subscribe(stockSymbol ->
//                        stockDataAdapter.fgadd(stockSymbol));
        Observable.just(
                new StockUpdate("GOOGLE", 12.43, new Date()),
                new StockUpdate("APPLE", 645.1, new Date()),
                new StockUpdate("TWRITTER", 1.43, new Date())
        ).subscribe(stockUpdate -> {
            Log.d(HomeActivity.class.getSimpleName(),"New Update ".concat(stockUpdate.getStockSymbol()));
            stockDataAdapter.add(stockUpdate);
        });

    }
}
