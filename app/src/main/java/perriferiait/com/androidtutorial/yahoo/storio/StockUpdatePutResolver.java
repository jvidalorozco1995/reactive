package perriferiait.com.androidtutorial.yahoo.storio;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.math.BigDecimal;
import java.util.Date;

import perriferiait.com.androidtutorial.StockUpdate;

/**
 * Created by admin on 13/09/17.
 */

public class StockUpdatePutResolver extends DefaultPutResolver<StockUpdate> {
    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(@NonNull StockUpdate object) {
        return InsertQuery.builder().table(StockUpdateTable.TABLE).build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull StockUpdate object) {
        return UpdateQuery.builder().table(StockUpdateTable.TABLE)
                .where(StockUpdateTable.Columns.ID + " = ?")
                .whereArgs(object.getId())
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull StockUpdate object) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(StockUpdateTable.Columns.ID, object.getId());
        contentValues.put(StockUpdateTable.Columns.STOCK_SYMBOL, object.getStockSymbol());
        contentValues.put(StockUpdateTable.Columns.PRICE, getPrice(object.getPrice()));
        contentValues.put(StockUpdateTable.Columns.DATE, getDate(object.getDate()));

        return contentValues;
    }

    private long getPrice(@NonNull BigDecimal value) {
        if (value != null) {
            return value.scaleByPowerOfTen(4).longValue();
        } else {
            return 0;
        }
    }

    private long getDate(@NonNull Date date) {
        return date.getTime();
    }
}
