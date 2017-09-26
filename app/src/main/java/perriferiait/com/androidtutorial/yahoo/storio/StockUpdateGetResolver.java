package perriferiait.com.androidtutorial.yahoo.storio;

import android.database.Cursor;

import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import io.reactivex.annotations.NonNull;
import perriferiait.com.androidtutorial.StockUpdate;

/**
 * Created by admin on 26/09/17.
 */

public class StockUpdateGetResolver extends DefaultGetResolver<StockUpdate> {


    @Override
    public StockUpdate mapFromCursor(Cursor cursor) {
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(StockUpdateTable.Columns.ID));
        final long dateLong = cursor.getLong(cursor.getColumnIndexOrThrow(StockUpdateTable.Columns.DATE));
        final long priceLong = cursor.getLong(cursor.getColumnIndexOrThrow(StockUpdateTable.Columns.PRICE));
        final String stockSymbol = cursor.getString(cursor.getColumnIndexOrThrow(StockUpdateTable.Columns.STOCK_SYMBOL));

        Date date = getDate(dateLong);
        BigDecimal price = getPrice(priceLong);
        final StockUpdate stockUpdate = new StockUpdate(stockSymbol, price, date);
        stockUpdate.setId(id);
        return stockUpdate;
    }

    private BigDecimal getPrice(long priceLong) {
        if (priceLong != -1) {
            return new BigDecimal(priceLong).scaleByPowerOfTen(-4);
        }
        return new BigDecimal(0);
    }

    private Date getDate(long dateLong) {

        return new Date(dateLong);
    }
}
