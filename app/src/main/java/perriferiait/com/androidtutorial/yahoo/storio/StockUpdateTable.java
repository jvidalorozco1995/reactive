package perriferiait.com.androidtutorial.yahoo.storio;

import perriferiait.com.androidtutorial.StockUpdate;

/**
 * Created by admin on 13/09/17.
 */

public class StockUpdateTable {


    private StockUpdateTable(){

    }

    public static final String TABLE="stock_updates";

    public static class Columns{
        static final String ID ="_id";
        static final String STOCK_SYMBOL="stock_symbol";
        static final String PRICE ="price";
        static final String DATE ="date";

    }

    static String createTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Columns.STOCK_SYMBOL + " TEXT NOT NULL, "
                + Columns.DATE + " LONG NOT NULL, "
                + Columns.PRICE + " LONG NOT NULL"
                + ");";
    }

}
