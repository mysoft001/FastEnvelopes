package go.fast.fastenvelopes.http;

import android.util.Log;

public class LogHelper {

    
    public static void showLog(Object className,String value)
    {
	Log.e(className.toString(), value);
    }
}
