package go.fast.fastenvelopes.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import go.fast.fastenvelopes.rewardanim.utils.FlakeView;

public class UMShareCallbackListener implements UMShareListener {

    Activity context;




    private View rewardRootView;

    private FlakeView flakeView;

    // public UMShareCallbackListener(Context context,LivingPageInfo
    // livingPageInfo)
    // {
    // this.context=context;
    //
    // this.livingPageInfo=livingPageInfo;
    // }

    public UMShareCallbackListener(Context context, View rewardRootView,
	    FlakeView flakeView) {
	this.context = (Activity) context;
	this.rewardRootView = rewardRootView;
	this.flakeView = flakeView;

    }
    


    public UMShareCallbackListener(Context context) {

	this.context = (Activity) context;

    }

    @Override
    public void onResult(SHARE_MEDIA platform) {
	Log.d("plat", "platform" + platform);


	if (flakeView != null && rewardRootView != null
		//&& !TimeUtils.isCurrentDay(context, "share_reward_lastday")
		) {//通知服务器增加金币
	    shareReward();
	} else {
	    if (platform.name().equals("WEIXIN_FAVORITE")) {
		Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
	    } else {

		Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
	    }
	}

    }

    private void shareReward() {


    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
	Toast.makeText(context, platform + " 分享失败", Toast.LENGTH_SHORT).show();
	if (t != null) {
	    Log.d("throw", "throw:" + t.getMessage());
	}
    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {
	// Toast.makeText(context, platform + " 分享取消了",
	// Toast.LENGTH_SHORT).show();
    }

}
