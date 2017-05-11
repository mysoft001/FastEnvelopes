package go.fast.fastenvelopes.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.interfaces.ITitleLayout;
import go.fast.fastenvelopes.receiver.PushMsgOperating;
import go.fast.fastenvelopes.service.PushIntentService;


/**
 * 游戏排行页面Fragment
 */
public class BaseFragment extends android.support.v4.app.Fragment implements
        ITitleLayout {

    public static BaseFragment newInstance() {
	return new BaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	IntentFilter filter = new IntentFilter();
	filter.addAction(PushIntentService.SEND_ATTENTION_ACTION);
	getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void initMiddle(TextView middleView) {
	middleView.setText("快玩红包");
    }

    @Override
    public void initLeft(ImageView imageView, TextView textView, View parent) {
	parent.setVisibility(View.INVISIBLE);
	imageView.setVisibility(View.GONE);
    }

    @Override
    public void initRight(ImageView imageView, TextView textView, View parent) {
	parent.setVisibility(View.INVISIBLE);
	imageView.setVisibility(View.GONE);
    }

    @Override
    public void initRefushImageView(ImageView imageView) {

    }

    @Override
    public void initRightFlag(ImageView imageView) {
	imageView.setVisibility(View.GONE);
    }

    @Override
    public void initLeftFlag(ImageView imageView) {
	imageView.setVisibility(View.GONE);
    }

    @Override
    public void initCenterImv(ImageView imageView) {
	imageView.setVisibility(View.GONE);

    }
    
    
    public void firstShowPage()
    {
	
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent) {
	    PushMsgInfo pushMsgInfo=(PushMsgInfo) intent.getSerializableExtra("content");
	   PushMsgOperating. pushMsgDoing(getActivity(),pushMsgInfo);
	}
    };

}
