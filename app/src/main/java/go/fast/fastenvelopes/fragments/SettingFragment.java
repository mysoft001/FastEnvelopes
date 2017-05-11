package go.fast.fastenvelopes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import go.fast.fastenvelopes.EnvelopesApplication;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.NotifyMsgActivity;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.http.ImageOptions;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.TimeUtils;
import go.fast.fastenvelopes.utils.ToolUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 我的设置Fragment
 */
public class SettingFragment extends SettingBaseFragment implements
        OnClickListener {
    private static final String TAG = "SettingFragment";

     static SettingFragment settingFragment;

    public static SettingFragment newInstance() {

    //    return new SettingFragment();
//	if (settingFragment != null) {
//	    return settingFragment;
//	}
	settingFragment = new SettingFragment();
	return settingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

    }

    public void setCustomEdit(EditText customEdit) {
    }

    public void refushUserInfo() {

        PushManager.getInstance().initialize(getActivity().getApplicationContext(), go.fast.fastenvelopes.service.PushInitService.class);
        PushManager.getInstance().registerPushIntentService(getActivity().getApplicationContext(), go.fast.fastenvelopes.service.PushIntentService.class);


        userNick.setText(PreferenceUtils.getInstance(getActivity())
                .getSettingUserNickName());
        signature.setText(PreferenceUtils.getInstance(getActivity())
                .getSettingUserQianming());
        ImageLoader.getInstance().displayImage(
                PreferenceUtils.getInstance(getActivity()).getSettingUserPic(),
                userIcon,
                ImageOptions
                        .getOptionsTouxiangByRound(PixelUtil.dp2Pixel(40,
                                getActivity())));
        if (PreferenceUtils.getInstance(getActivity()).getSettingUserAccount()
                .equals("999")) {
            showLoginLayout.setVisibility(View.VISIBLE);
            showLoginLayout.setOnClickListener(this);
        } else {
            if (TimeUtils.isCurrentDay(getActivity(), "rewardDay")) {
            } else {
            }
            showLoginLayout.setVisibility(View.GONE);
        }
        sendRequest(PreferenceUtils.getInstance(getActivity())
                .getSettingUserAccount());
    }

    private void setInitView(UserInfo userInfo) {

        if (userInfo == null) {
            return;
        }


        PreferenceUtils.getInstance(getActivity()).setSettingUserAccount(
                userInfo.account);
        if (PreferenceUtils.getInstance(getActivity()).getSettingUserAccount()
                .equals("999")) {
            showLoginLayout.setVisibility(View.VISIBLE);
            showLoginLayout.setOnClickListener(this);
        } else {

            showLoginLayout.setVisibility(View.GONE);
        }
        userNick.setText(userInfo.nickName);
        signature.setText(userInfo.signature);
        account.setText(userInfo.account);
        goldSizeText.setText(userInfo.goldCoin + "");
        // int
        // sexType=Integer.valueOf(PreferenceUtils.getInstance(getActivity()).getSettingUserSex());
        int attentionCount = userInfo.attentionMeCounts;


        int totalCoins = userInfo.goldCoin;


            PreferenceUtils.getInstance(getActivity()).setSettingTotalGoldenSize(totalCoins);


        if (Integer.valueOf(userInfo.level) > 1) {
            level.setText(userInfo.level);
            PreferenceUtils.getInstance(getActivity()).setSettingUserLevel(
                    userInfo.level);
        }

        int lasCounts=userInfo.lastCounts;
        PreferenceUtils.getInstance(getActivity()).setSettingUserLastPlayCounts(
                lasCounts);
        lastPlayCountsTV.setText(lasCounts+"");

        if (userInfo.attentionMeCounts > 0) {
            fansText.setText(attentionCount + "");
            PreferenceUtils.getInstance(getActivity()).setSettingUserFans(
                    userInfo.attentionMeCounts);
        } else {
            fansText.setText("0");
        }

        if (userInfo.attentionOthersCounts > 0) {
            attentionText.setText(userInfo.attentionOthersCounts + "");
            PreferenceUtils.getInstance(getActivity()).setSettingUserAttention(
                    userInfo.attentionOthersCounts);
            ;
        } else {
            attentionText.setText("0");
        }
    }

    @Override
    public void initMiddle(TextView middleView) {
        super.initMiddle(middleView);
        ViewGroup.LayoutParams lp = middleView.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        middleView.setText("我");
        middleView.setBackgroundResource(R.color.transparent_color);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container,
                savedInstanceState);
        setInitViews();
        return rootView;
    }

    protected void setInitViews(View rootView) {
        // mylevel_rl
        super.setInitViews();


    }

    // private ImageView tixingImg;

    @Override
    public void initRight(ImageView imageView, TextView textView, View parent) {
        super.initRight(imageView, textView, parent);
        parent.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.tixing_w);
        parent.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.layout_title_right:


                Intent notifyIntent = new Intent(EnvelopesApplication.getInstance(),
                        NotifyMsgActivity.class);
                notifyIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                System.out.println("   initRight   " + getActivity()+"   "+notifyIntent);
                EnvelopesApplication.getInstance().startActivity(notifyIntent);
                break;


        }

    }

    @Override
    public void onResume() {
        super.onResume();

        refushUserInfo();


    }

    // --------------------------------------------------------------------------------------------------------------------
    private void sendRequest(String account) {

        if (!CommonUtils.isNetWorkConnected(getActivity())) {
            ToolUtils.showToast(getActivity(), "网络出问题了，请检查后再试");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("account", PreferenceUtils.getInstance(getActivity())
                .getSettingUserAccount());
        params.put("user_account", account);
        DeviceUuidFactory uuid = new DeviceUuidFactory(getActivity());
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        // String Street =
        HttpRestClient.post(Constant.GET_USERINFO, params,
                new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, UserInfo response) {

                        System.out.println("获取用户信息  " + rawJsonResponse);
                        if (response != null) {
                            if (response.getStatus().equals("ok")) {
                                setInitView(response);
                            } else {
                                if (response.getCode().equals("1999"))// 在其他地方登录
                                {
                                    HttpRequest.reLogin(getActivity());
                                } else if (response.getCode().equals("2004")) {
                                    if(PreferenceUtils.getInstance(getActivity())
                                            .getSettingUserAccount().equals("999"))
                                    {

                                    }
                                    else
                                    {
                                        ToolUtils.showToastByStr(getActivity(),
                                                response.msg);
                                    }
                                }
                            }

                        }

                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          UserInfo errorResponse) {
                    }

                    @Override
                    protected UserInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {

                        return null;
                    }
                });

    }
}
