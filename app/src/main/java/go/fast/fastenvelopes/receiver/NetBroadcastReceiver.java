package go.fast.fastenvelopes.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import go.fast.fastenvelopes.activity.BaseActivity;
import go.fast.fastenvelopes.utils.NetUtlis;


public class NetBroadcastReceiver extends BroadcastReceiver {  
    
    public NetEvevt evevt = BaseActivity.evevt;
  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        // TODO Auto-generated method stub  
        // 如果相等的话就说明网络状态发生了变化  
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {  
            
            if(!ImageLoader.getInstance().isInited())
    	{
    	    ImageLoader.getInstance().init(
    			ImageLoaderConfiguration.createDefault(context)); 
    	}
            int netWorkState = NetUtlis.getNetWorkState(context);
            // 接口回调传过去状态的类型  
            evevt.onNetChange(netWorkState);  
        }  
    }  
  
    // 自定义接口  
    public interface NetEvevt {  
        public void onNetChange(int netMobile);  
    }  
}  