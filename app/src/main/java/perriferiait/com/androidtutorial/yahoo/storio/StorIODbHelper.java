package perriferiait.com.androidtutorial.yahoo.storio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 20/09/17.
 */

public class StorIODbHelper extends SQLiteOpenHelper{

    public StorIODbHelper(Context context){
        super(context,"reactivestocks.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(StockUpdateTable.createTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
