package perriferiait.com.androidtutorial;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 31/08/17.
 */

public class StockDataAdapter extends RecyclerView.Adapter<StockDataAdapter.StockUpdateViewHolder> {


        private final List<StockUpdate> data = new ArrayList<>();

    @Override
    public StockUpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_update_item, parent, false);
        StockUpdateViewHolder vh = new StockUpdateViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(StockUpdateViewHolder holder, int position) {

        holder.setStocksymbol(data.get(position).getStockSymbol());
        holder.setPrice(data.get(position).getPrice());
        holder.setDate(data.get(position).getDate());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(StockUpdate stockSymbol) {
        this.data.add(stockSymbol);
        notifyItemInserted(data.size() - 1);
    }

    protected static class StockUpdateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.stock_item_symbol)
        TextView stocksymbol;

        @BindView(R.id.stock_item_date)
        TextView date;

        @BindView(R.id.stock_item_price)
        TextView price;

        private static  final NumberFormat PRICE_FORMAT = new DecimalFormat("#0.00");

        public void setStocksymbol(String stocksymbol){
            this.stocksymbol.setText(stocksymbol);
        }
        public void setPrice(BigDecimal price){
            this.price.setText(PRICE_FORMAT.format(price.floatValue()));
        }

        public void setDate(Date date){
            this.date.setText(DateFormat.getInstance().format(date));
        }

        public StockUpdateViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}

