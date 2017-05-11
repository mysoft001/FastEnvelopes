package go.fast.fastenvelopes.rewardanim.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import go.fast.fastenvelopes.R;


public class RewardAnimUtils {

    
    
    /**
     * 显示奖励后的动画
     * @param context
     * @param rootView
     * 
     * 
     * 绑定的view
     * 
     * 
     * @param flakeView
     * 
     * 

     * 
     * 金币数
     * 
     * 
     * @param show
     * 
     * 是否一直显示金币下落动画
     * 
     * @return
     */
    public static  PopupWindow showPopWindows(final Activity context, View rootView,
	    FlakeView flakeView,String titleText, String moneyNum, boolean show, final boolean isAutoDismiss) {

	View view = context.getLayoutInflater().inflate(
		R.layout.view_login_reward, null);
	final PopupWindow pop = new PopupWindow(view,
		FrameLayout.LayoutParams.MATCH_PARENT,
		FrameLayout.LayoutParams.MATCH_PARENT);
	TextView tvTips = (TextView) view.findViewById(R.id.tv_tip);
	TextView money = (TextView) view.findViewById(R.id.tv_money);
	tvTips.setText(titleText);
	money.setText(moneyNum);
	final LinearLayout container = (LinearLayout) view
		.findViewById(R.id.container);
	// 将flakeView 添加到布局中
	container.addView(flakeView);
	// 设置背景
	context.getWindow().setBackgroundDrawable(
		new ColorDrawable(Color.BLACK));
	// 设置同时出现在屏幕上的金币数量 建议64以内 过多会引起卡顿
	flakeView.addFlakes(8);
	/**
	 * 绘制的类型
	 * 
	 * @see View.LAYER_TYPE_HARDWARE
	 * @see View.LAYER_TYPE_SOFTWARE
	 * @see View.LAYER_TYPE_NONE
	 */
	flakeView.setLayerType(View.LAYER_TYPE_NONE, null);
	view.findViewById(R.id.btn_ikow).setOnClickListener(
		new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			if (container != null) {
			    container.removeAllViews();
			}
			pop.dismiss();
		    }
		});
	if (pop == null) {

	    // pop = new PopupWindow(view,
	    // FrameLayout.LayoutParams.MATCH_PARENT,
	    // FrameLayout.LayoutParams.MATCH_PARENT);
	}
	ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
		R.color.half_color));
	pop.setBackgroundDrawable(dw);
	pop.setOutsideTouchable(true);
	pop.setFocusable(true);
	pop.showAtLocation(rootView, Gravity.CENTER, 0, 0);

	/**
	 * 移除动画
	 */
	final Thread thread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		try {
		    // 设置2秒后
		    Thread.sleep(2000);
		    context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
			    if(isAutoDismiss)
			    {
				pop.dismiss();
			    }
			    container.removeAllViews();
			}
		    });
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}

	    }
	});
	
	 if(isAutoDismiss)
	    {
	     new Thread(new Runnable() {
		    @Override
		    public void run() {
			try {
			    // 设置2秒后
			    Thread.sleep(2000);
			    context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
				   
					pop.dismiss();
				}
			    });
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}

		    }
		}).start();
	    }
	
	if (!show)
	    thread.start();
	MediaPlayer player = MediaPlayer.create(context, R.raw.shake);
	player.start();
	return pop;
    }
}
