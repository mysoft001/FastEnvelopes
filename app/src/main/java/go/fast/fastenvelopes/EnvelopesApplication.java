package go.fast.fastenvelopes;

import android.app.Application;

import com.igexin.sdk.PushManager;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import go.fast.fastenvelopes.utils.CommentValues;

/**
 * Created by hanwei on 2017/1/6.
 */

public class EnvelopesApplication extends Application {

    private static EnvelopesApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);

        Config.REDIRECT_URL = "您新浪后台的回调地址";
        PushManager.getInstance().initialize(this.getApplicationContext(), go.fast.fastenvelopes.service.PushInitService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), go.fast.fastenvelopes.service.PushIntentService.class);
        instance = this;
    }

    // 各个平台的配置，建议放在全局Application或者程序入口vv
    {

        PlatformConfig.setWeixin(CommentValues.WXAPP_ID, CommentValues.WXAPP_SECRET);
        PlatformConfig.setSinaWeibo(CommentValues.WeiBoAPP_KEY, CommentValues.WeiBoAPP_SECRET);
        PlatformConfig.setQQZone(CommentValues.QQShare_APP_ID, CommentValues.QQShare_APP_SECRET);
       Config.DEBUG=true;

    }


    public static EnvelopesApplication getInstance(){
        // 这里不用判断instance是否为空
        return instance;
    }
}
