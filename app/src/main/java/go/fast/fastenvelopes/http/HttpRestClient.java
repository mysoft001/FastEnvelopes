package go.fast.fastenvelopes.http;

import android.content.Context;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class HttpRestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static SyncHttpClient syncClient = new SyncHttpClient();

    public static void get(String url, RequestParams params,
	    AsyncHttpResponseHandler responseHandler) {
	client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params,
	    AsyncHttpResponseHandler responseHandler) {
	
	client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    
    public static void postAbsoluteUrl(String url, RequestParams params,
	    AsyncHttpResponseHandler responseHandler) {
	
	client.post(url, params, responseHandler);
    }
    
    
    public static void cancelRequest(Context context)
    {
	client.cancelRequests(context, false);
    }

    public static void syncPost(String url, RequestParams params,
	    AsyncHttpResponseHandler responseHandler) {
	syncClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
	return Constant.NEW_BASE_URL + relativeUrl;// 基本的url
    }

    //请求多个字符串参数
    public static void post(String url, String[][] params,
	    AsyncHttpResponseHandler responseHandler) {

	RequestParams requestParams = new RequestParams();

	for (int i = 0; i < params.length; i++) {
	    requestParams.add(params[i][0], params[i][1]);
	}

	client.post(getAbsoluteUrl(url), requestParams, responseHandler);
    }
    

//    //请求多个字符串参数
//    public static void postPro(Activity context,String url, String[][] params,
//	    AsyncHttpResponseHandler responseHandler) {
//	
//	
//
//	RequestParams requestParams = new RequestParams();
//
//	for (int i = 0; i < params.length; i++) {
//	    requestParams.add(params[i][0], params[i][1]);
//	}
//
//	client.post(getAbsoluteUrl(url), requestParams, new AsyncHttpResponseHandler() {
//	    
//	    @Override
//	    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//		
//	    }
//	    
//	    @Override
//	    public void onFailure(int statusCode, Header[] headers,
//		    byte[] responseBody, Throwable error) {
//		
//	    }
//	});
//    }

}
