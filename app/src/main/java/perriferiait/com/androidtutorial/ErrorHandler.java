package perriferiait.com.androidtutorial;

import android.util.Log;

import io.reactivex.functions.Consumer;

/**
 * Created by admin on 26/09/17.
 */

public class ErrorHandler implements Consumer<Throwable>{

    public static final String TAG = ErrorHandler.class.getSimpleName();
    public static final ErrorHandler INSTANCE = new ErrorHandler();

    private ErrorHandler(){

    }

    public static ErrorHandler get(){
        return INSTANCE;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        Log.e(TAG,"Error en ".concat(Thread.currentThread().getName()).concat(":"),throwable);
    }
}
