package go.fast.fastenvelopes.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import dialogplus.ViewHolder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.RoomListAdapter;
import go.fast.fastenvelopes.db.AttentionUserDB;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.http.ImageOptions;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.RoomItemInfo;
import go.fast.fastenvelopes.info.RoomListInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import me.maxwin.view.XListView;

import static go.fast.fastenvelopes.R.id.empty_layout;

public class UserInfoActivity extends BaseActivity implements XListView.IXListViewListener, OnClickListener {
    private static final String FRAGMENT_TAG = "main_fragment";

    protected TextView userNick;// 用户昵称
    protected TextView signature;// 签名
    protected TextView account;// 账号
    protected TextView personalLetterText;// 私信


    private List<RoomItemInfo> roomList;
    RoomListAdapter roomListAdapter;

    //protected RelativeLayout envelopeAnnal;// 红包记录

    private XListView envelopeHistoryL;
    //  protected RelativeLayout myCollectLayout;// 我的收藏


    protected TextView attentionText;// 关注


    protected ImageView userIcon;

    //protected ImageView keepIcon;//收藏的图标


    private String headUrl;
    String userAccount;
    String nickname;

    private UserInfo userInfo;

    private View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_layout);
        initView();
    }

    private void initView() {
        userInfo=new UserInfo();
        roomList = new ArrayList<>();
        userNick = (TextView) findViewById(R.id.user_nick_tv);
        signature = (TextView) findViewById(R.id.user_signature_tv);
        account = (TextView) findViewById(R.id.user_account_tv);
        envelopeHistoryL = (XListView) findViewById(R.id.list_lv);
        attentionText = (TextView) findViewById(R.id.attention_tv);
        personalLetterText = (TextView) findViewById(R.id.personal_letter_tv);

        emptyView = findViewById(R.id.empty_layout);
        envelopeHistoryL.setPullLoadEnable(true);
        envelopeHistoryL.setPullRefreshEnable(false);
        envelopeHistoryL.setXListViewListener(this);
        roomListAdapter = new RoomListAdapter(this, R.layout.room_list_item, roomList);
        envelopeHistoryL.setAdapter(roomListAdapter);
        userIcon = (ImageView) findViewById(R.id.iv_user_photo);
        userIcon.setOnClickListener(this);
        //envelopeAnnal.setOnClickListener(this);
        attentionText.setOnClickListener(this);
        personalLetterText.setOnClickListener(this);
        userAccount = getIntent().getStringExtra("account");
        headUrl = getIntent().getStringExtra("headurl");
        nickname = getIntent().getStringExtra("nickname");

        userInfo.account=userAccount;
        userInfo.headurl=headUrl;
        userInfo.nickname=nickname;

        emptyView = findViewById(empty_layout);
        List<String> attentionedL = AttentionUserDB.getInstance(
                this).getAttentionList();

        System.out.println("关注的列表    " + attentionedL.size());
        if (attentionedL.contains(userAccount)) {
            attentionText.setText("已关注");
        }
        if (!ToolUtils.isNullOrEmpty(headUrl)) {
            ImageLoader.getInstance().displayImage(
                    headUrl,
                    userIcon,
                    ImageOptions
                            .getOptionsTouxiangByRound(PixelUtil.dp2Pixel(40,
                                    this)));
        }
        if (!ToolUtils.isNullOrEmpty(nickname)) {
            userNick.setText(nickname);
        }
        if (!ToolUtils.isNullOrEmpty(userAccount)) {
            account.setText(userAccount);
        }
        sendRequest(userAccount);

        envelopeHistoryL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    InRoom(roomList.get(position - 1));
                }
            }
        });

        ((TextView) emptyView.findViewById(R.id.empty_tv)).setText("正在加载中...");
        envelopeHistoryL.setEmptyView(emptyView);
        emptyView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((TextView) emptyView.findViewById(R.id.empty_tv))
                        .setText("加载数据中...");

                requestRoomList();
            }
        });
        if (CommonUtils.isNetWorkConnected(this)) {
            requestRoomList();
        } else {

            ((TextView) emptyView.findViewById(R.id.empty_tv))
                    .setText("好像没有联上网哦，点此刷新~~~~");

        }

    }

    private void InRoom(RoomItemInfo roomInfo) {
        if (roomInfo.roomIsPass == 1) {
            if (PreferenceUtils.getInstance(this).getSettingUserAccount().equals(roomInfo.roomCreatedAccount))//是创建者自己不需要输入密码
            {
                goChatActivity(roomInfo);
            } else {
                showPsdDialog(roomInfo);
            }
        } else {
            goChatActivity(roomInfo);
        }


    }

    DialogPlus dialog;

    private void showPsdDialog(final RoomItemInfo roomInfo) {
        DialogPlusBuilder dialogPlusB = DialogPlus.newDialog(UserInfoActivity.this);

        dialogPlusB.setContentHolder(new ViewHolder(R.layout.show_input_psd_dialog));
        dialogPlusB.setContentBackgroundResource(android.R.color.transparent);
        dialogPlusB.setGravity(Gravity.CENTER);
        //dialogPlusB.setCancelable(isCancelable);
        dialog = dialogPlusB.create();
        final EditText psdEdit = ((EditText) dialog.getHolderView().findViewById(R.id.input_psd_et));
        TextView postiveText = ((TextView) dialog.getHolderView().findViewById(R.id.postive_tv));
        TextView negtiveText = ((TextView) dialog.getHolderView().findViewById(R.id.negtive_tv));
        postiveText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ToolUtils.isNullOrEmpty(psdEdit.getText().toString())) {
                    ToolUtils.showToast(UserInfoActivity.this, "请输入房间密码");
                    return;
                }
                checkPsd(psdEdit.getText().toString(), roomInfo);
                //dialog.dismiss();
            }
        });
        negtiveText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void checkPsd(String psd, final RoomItemInfo roomInfo) {
        HttpRequest.checkRoomPsd(UserInfoActivity.this, roomInfo.roomId, psd, new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {
                if (HttpResponseUtil.isResponseOk(response))//密码正确
                {
                    goChatActivity(roomInfo);
                    dialog.dismiss();
                } else {
                    if (response.getCode().equals("2008")) {
                        ToolUtils.showToast(UserInfoActivity.this, "密码不正确，请检查后重试");
                    } else if (response.getCode().equals("2007")) {
                        dialog.dismiss();
                        ToolUtils.showToast(UserInfoActivity.this, "房间不存在");
                    }

                }
            }

            @Override
            public void onFailure(String rawJsonData) {
                dialog.dismiss();
                ToolUtils.showToast(UserInfoActivity.this, "验证密码出错，请稍后再试或联系我们");
            }
        });
    }


    private void goChatActivity(RoomItemInfo roomInfo) {
        Intent chatIntent = new Intent(this, ChatEnvelopeActivity.class);
        chatIntent.putExtra("roomInfo", roomInfo);
        startActivity(chatIntent);
    }

    private void requestRoomList() {
        HttpRequest.getUserHistoryRoomList(this,userAccount, new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {
                RoomListInfo roomListInfo = (RoomListInfo) response;
                if (HttpResponseUtil.isResponseOk(response)) {
                    if (roomListInfo.list != null && roomListInfo.list.size() > 0) {

                        roomList.clear();
                        roomList.addAll(roomListInfo.list);
                        roomListAdapter.notifyDataSetChanged();
                    } else {
                        ToolUtils.showToast(UserInfoActivity.this, "获取房间记录失败");
                    }
                }
                else
                {
                    if (response.getCode().equals("1999"))// 在其他地方登录
                    {
                        // 有缓存账号不跳登录界面

                        HttpRequest.reLogin(UserInfoActivity.this);
                    } else {
                        ToolUtils.showToast(UserInfoActivity.this, response.msg);
                    }
                }

                onLoad();
            }

            @Override
            public void onFailure(String rawJsonData) {
                onLoad();
            }
        });
    }

    private void onLoad() {// 防止多个线程同时调用
        // TODO Auto-generated method stub
        envelopeHistoryL.stopRefresh();
        envelopeHistoryL.stopLoadMore();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        envelopeHistoryL.setRefreshTime(str);

        if (roomList == null || roomList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            if (CommonUtils.isNetWorkConnected(this)) {
                ((TextView) emptyView.findViewById(R.id.empty_tv))
                        .setText("还没有进行过红包游戏");
            } else {
                ((TextView) emptyView.findViewById(R.id.empty_tv))
                        .setText("好像没有联上网哦，点此刷新");
            }

        } else {

            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void initMiddle(TextView middleView) {
        // TODO Auto-generated method stub
        super.initMiddle(middleView);
        middleView.setText("用户详情");
    }

    private void setInitView(UserInfo userInfo) {

        if(userInfo==null)
        {
            return;
        }
        this.userInfo=userInfo;
        userNick.setText(userInfo.nickName);
        signature.setText(userInfo.signature);
        account.setText(userInfo.account);
        // int
        // sexType=Integer.valueOf(PreferenceUtils.getInstance(getActivity()).getSettingUserSex());

        System.out.println("   headurl     " + userInfo.headurl);
        System.out.println("   headurlBig     " + userInfo.headurlBig);
        if (ToolUtils.isNullOrEmpty(userInfo.headurlBig)) {
            headUrl = userInfo.headurl;
        } else {
            headUrl = userInfo.headurlBig;
        }

        ImageLoader.getInstance().displayImage(
                userInfo.headurl,
                userIcon,
                ImageOptions
                        .getOptionsByRound(PixelUtil.dp2Pixel(40, this)));

    }

    public void sendPost(View v) {

        // sendRequest();
        return;
    }

    private void sendRequest(String account) {

        if (!CommonUtils.isNetWorkConnected(this)) {
            ToolUtils.showToast(this, "网络出问题了，请检查后再试");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("account", PreferenceUtils.getInstance(this)
                .getSettingUserAccount());
        params.put("user_account", account);
        DeviceUuidFactory uuid = new DeviceUuidFactory(this);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        // String Street =
        HttpRestClient.post(Constant.GET_USERINFO, params,
                new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, UserInfo response) {

                        System.out.println("  GET_USERINFO    "+rawJsonResponse);
                        if (response != null) {
                            if (response.getStatus().equals("ok")) {
                                setInitView(response);
                            } else {
                                if (response.getCode().equals("1999"))// 在其他地方登录
                                {
                                    HttpRequest.reLogin(UserInfoActivity.this);
                                } else {
                                    ToolUtils.showToastByStr(UserInfoActivity.this,
                                            response.msg);
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

//    private void updateAttentionState() {
//
//	List<String> attentionedL = ArticleChapterDB.getInstance(this)
//		.getAttentionList();
//	System.out.println(" updateAttentionList      " + attentionedL.size());
//	if (attentionedL.contains(userAccount)) {
//	    keepIcon.setSelected(true);
//	}
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//	if (id == R.id.action_settings) {
//	    return true;
//	}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_user_photo:
                ArrayList<String> imgList = new ArrayList<String>();
                imgList.add(headUrl);
//	    Intent fullPhotoAc = new Intent(this, PhotoActivity.class);
//	    fullPhotoAc.putStringArrayListExtra("imgurl", imgList);
//	    startActivity(fullPhotoAc);
                break;

            case R.id.attention_tv:// 点击关注

                if (attentionText.getText().toString().equals("已关注")) {
                    cancelAttentionUser();
                } else {
                    attentionUser();
                }
                break;

            case R.id.personal_letter_tv:// 私信

                Intent personalIntent = new Intent(this, PersonalLetterActivity.class);
System.out.println("打印信息---"+userInfo.account+"   "+userInfo.nickname);
                personalIntent.putExtra("userInfo", userInfo);

                startActivity(personalIntent);
                break;
        }


    }

    private void attentionUser() {
        HttpRequest.addUserAttention(this, userAccount,
                new HttpRequest.onRequestCallback() {

                    @Override
                    public void onSuccess(BaseInfo response) {

                        if (HttpResponseUtil.isResponseOk(response)) {

                            attentionText.setText("已关注");
                            // 更新到本地数据库
                            AttentionUserDB.getInstance(UserInfoActivity.this)
                                    .insertAttentionInfo(userAccount);

                        } else {

                            if (response.getCode().equals("1999"))// 在其他地方登录
                            {
                                HttpRequest.reLogin(UserInfoActivity.this);
                            } else {
                                ToolUtils.showToastByStr(UserInfoActivity.this,
                                        response.msg);
                            }
                        }
                    }

                    public void onFailure(String rawJsonData) {

                    }
                });
    }

    private void cancelAttentionUser() {
        HttpRequest.cancelUserAttention(this, userAccount,
                new HttpRequest.onRequestCallback() {

                    @Override
                    public void onSuccess(BaseInfo response) {

                        if (HttpResponseUtil.isResponseOk(response)) {
                            attentionText.setText("关注");
                            // 更新到本地数据库
                            AttentionUserDB.getInstance(UserInfoActivity.this)
                                    .deleteAttentionInfo(userAccount);

                        } else {
                            if (response.getCode().equals("1999"))// 在其他地方登录
                            {
                                HttpRequest.reLogin(UserInfoActivity.this);
                            } else {
                                ToolUtils.showToastByStr(UserInfoActivity.this,
                                        response.msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String rawJsonData) {

                    }
                });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
