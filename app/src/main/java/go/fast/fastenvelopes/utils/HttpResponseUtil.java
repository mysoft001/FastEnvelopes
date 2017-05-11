package go.fast.fastenvelopes.utils;

import android.content.Context;

import go.fast.fastenvelopes.info.BaseInfo;


public class HttpResponseUtil {
    
    
    
    public static boolean isResponseOk(BaseInfo response)
    {
	if(response!=null&&response.getStatus().equals("ok"))
	{
	    return true;
	}
	return false;
    }
    public static boolean isResponseOk(Context context,BaseInfo response)
    {
	if(response!=null&&response.getStatus().equals("ok"))
	{
	    return true;
	}
	else if(response!=null&&response.getStatus().equals("login"))
	{
	    ToolUtils.showToast(context, "该账号已在其他地方登录，请重新登录");
	   // context.startActivity(new Intent(context,LoginActivity.class));
	    return true;
	}
	
	return false;
    }
}
