package go.fast.fastenvelopes.http;

import android.util.Log;

public class LoggerHelper {
	private static final String TAG = "owl_debug";
	private static boolean on = true;
	
	public static boolean getLogAble() {
        return on;
    }

    public static void setLogAble(boolean on) {
        LoggerHelper.on = on;
    }

    public static void d(String msg) {
		if (on) {
			Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (on) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable e) {
		if (on) {
			Log.d(tag, msg, e);
		}
	}
	
	public static void i(String msg) {
		if (on) {
			Log.i(TAG, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (on) {
			Log.i(tag, msg);
		}
	}

	public static void e(String msg) {
		if (on) {
			Log.e(TAG, msg);
		}
	}
	
	public static void e(String msg, Throwable e) {
		if (on) {
			Log.e(TAG, msg , e);
		}
	}
	
	public static void e(Throwable e) {
		if (on) {
			Log.e(TAG, "" , e);
		}
	}

	public static void e(String tag, String msg) {
		if (on) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable e) {
		if (on) {
			Log.e(tag, msg, e);
		}
	}

	public static void w(String msg) {
		if (on) {
			Log.w(TAG, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (on) {
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable e) {
		if (on) {
			Log.w(tag, msg, e);
		}
	}

	public static void i(String tag, String msg, Throwable e) {
		if (on) {
			Log.i(tag, msg, e);
		}
	}

}
