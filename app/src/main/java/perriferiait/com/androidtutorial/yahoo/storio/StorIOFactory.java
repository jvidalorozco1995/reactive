package perriferiait.com.androidtutorial.yahoo.storio;

import android.content.Context;
import android.database.Cursor;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import io.reactivex.annotations.NonNull;
import perriferiait.com.androidtutorial.StockUpdate;

/**
 * Created by admin on 20/09/17.
 */

public class StorIOFactory {


    private static StorIOSQLite INSTANCE;

    public synchronized static StorIOSQLite get(Context context) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = DefaultStorIOSQLite.builder().sqliteOpenHelper(new StorIODbHelper(context))
                .addTypeMapping(StockUpdate.class, SQLiteTypeMapping.<StockUpdate>builder()
                        .putResolver(new StockUpdatePutResolver())
                        .getResolver(new StockUpdateGetResolver())
                        .deleteResolver(createDeleteResolver())
                        .build()).build();
        return INSTANCE;
    }




    private static DeleteResolver<StockUpdate> createDeleteResolver() {
        return new DefaultDeleteResolver<StockUpdate>() {
            @NonNull
            @Override
            protected DeleteQuery mapToDeleteQuery(@NonNull StockUpdate object) {
                return null;
            }
        };
    }
}
