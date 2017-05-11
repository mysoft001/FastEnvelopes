package go.fast.fastenvelopes.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class JumpUtils {

    public static Intent getIntent(Context paramContext) {
	StringBuilder localStringBuilder = new StringBuilder()
		.append("market://details?id=");
	String str = paramContext.getPackageName();
	localStringBuilder.append(str);
	Uri localUri = Uri.parse(localStringBuilder.toString());
	return new Intent("android.intent.action.VIEW", localUri);
    }

    // 直接跳转不判断是否存在市场应用
    public static void start(Context paramContext, String paramString) {
	Uri localUri = Uri.parse(paramString);
	Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
	localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	paramContext.startActivity(localIntent);
    }

    public static boolean judge(Context paramContext, Intent paramIntent) {
	List<ResolveInfo> localList = paramContext.getPackageManager()
		.queryIntentActivities(paramIntent,
			PackageManager.GET_INTENT_FILTERS);
	if ((localList != null) && (localList.size() > 0)) {
	    return false;
	} else {
	    return true;
	}
    }
    
    
    public static void jumpToMarket(Context paramContext)
    {
	Intent i = getIntent(paramContext);  
        boolean b = judge(paramContext, i);  
        if(!b)  
        {  
            paramContext.startActivity(i);  
        }  
        else
        {
            ToolUtils.showToast(paramContext, "手机上没检测到应用市场，请自行到应用市场搜索本软件下载更新");
        }
    }

}
