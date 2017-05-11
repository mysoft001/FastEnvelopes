package go.fast.fastenvelopes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import dialogplus.ViewHolder;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.RoomListAdapter;
import go.fast.fastenvelopes.fragments.EnvelopeRoomFragment;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.RoomItemInfo;
import go.fast.fastenvelopes.info.RoomListInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import me.maxwin.view.XListView;


/**
 * 不同红包类型的activity
 */
public class EnvelopeRoomAcitvity extends BaseActivity implements XListView.IXListViewListener {

    private XListView sessionListv;
    private View emptyView;
    private List<RoomItemInfo> roomList;
    RoomListAdapter roomListAdapter;
   // View headView;
    int envelopeRoomType = CommonUtils.ENVELOPE_TYPE_GUESS;

    public static EnvelopeRoomFragment newInstance() {
        return new EnvelopeRoomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        emptyView = findViewById(R.id.empty_layout);
        sessionListv = (XListView) findViewById(R.id.list_lv);
        sessionListv.setPullRefreshEnable(true);
        sessionListv.setPullLoadEnable(false);
        sessionListv.setXListViewListener(this);
        setInitView();
    }


    private void setInitView() {
        roomList = new ArrayList<>();
        envelopeRoomType = getIntent().getIntExtra("envelopeType", CommonUtils.ENVELOPE_TYPE_GUESS);
        roomListAdapter = new RoomListAdapter(this, R.layout.room_list_item, roomList);
        sessionListv.setAdapter(roomListAdapter);

        if (envelopeRoomType == CommonUtils.ENVELOPE_TYPE_GUESS) {
            middleView.setText("猜金额红包房间列表");
        } else if (envelopeRoomType == CommonUtils.ENVELOPE_TYPE_FREE) {
            middleView.setText("自由猜红包房间列表");
        }


        sessionListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    InRoom(roomList.get(position - 1));
                }
            }
        });

        ((TextView) emptyView.findViewById(R.id.empty_tv)).setText("正在加载中...");
        sessionListv.setEmptyView(emptyView);
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


    private void requestRoomList() {
        HttpRequest.getRoomListByType(this, envelopeRoomType, new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {
                RoomListInfo roomListInfo = (RoomListInfo) response;

                if (HttpResponseUtil.isResponseOk(response)) {
                    if (roomListInfo.list != null && roomListInfo.list.size() > 0) {

                        roomList.clear();
                        roomList.addAll(roomListInfo.list);
                        roomListAdapter.notifyDataSetChanged();
                    } else {
                        ToolUtils.showToast(EnvelopeRoomAcitvity.this, "获取房间列表失败");
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
        sessionListv.stopRefresh();
        sessionListv.stopLoadMore();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        sessionListv.setRefreshTime(str);

        if (roomList == null || roomList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            if (CommonUtils.isNetWorkConnected(this)) {
                ((TextView) emptyView.findViewById(R.id.empty_tv))
                        .setText("还没有房间");
            } else {
                ((TextView) emptyView.findViewById(R.id.empty_tv))
                        .setText("好像没有联上网哦，点此刷新");
            }

        } else {

            emptyView.setVisibility(View.GONE);
        }
    }

    DialogPlus dialog;

    private void showPsdDialog(final RoomItemInfo roomInfo) {
        DialogPlusBuilder dialogPlusB = DialogPlus.newDialog(this);

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
                    ToolUtils.showToast(EnvelopeRoomAcitvity.this, "请输入房间密码");
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
        HttpRequest.checkRoomPsd(this, roomInfo.roomId, psd, new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {
                if (HttpResponseUtil.isResponseOk(response))//密码正确
                {
                    goChatActivity(roomInfo);
                    dialog.dismiss();
                } else {
                    if (response.getCode().equals("2008")) {
                        ToolUtils.showToast(EnvelopeRoomAcitvity.this, "密码不正确，请检查后重试");
                    } else if (response.getCode().equals("2007")) {
                        dialog.dismiss();
                        ToolUtils.showToast(EnvelopeRoomAcitvity.this, "房间不存在");
                    }

                }
            }

            @Override
            public void onFailure(String rawJsonData) {
                dialog.dismiss();
                ToolUtils.showToast(EnvelopeRoomAcitvity.this, "验证密码出错，请稍后再试或联系我们");
            }
        });
    }


    private void goChatActivity(RoomItemInfo roomInfo) {
        Intent chatIntent = new Intent(EnvelopeRoomAcitvity.this, ChatEnvelopeActivity.class);
        chatIntent.putExtra("roomInfo", roomInfo);
        startActivity(chatIntent);
    }

    TextView middleView;

    @Override
    public void initMiddle(TextView middleView) {
        super.initMiddle(middleView);
        this.middleView = middleView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        requestRoomList();
    }

    @Override
    public void onLoadMore() {

    }
}
