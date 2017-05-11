package go.fast.fastenvelopes.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.NotifyListAdapter;
import go.fast.fastenvelopes.db.CoolDBHelper;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;



/**
 * 消息通知页面
 */
public class NotifyMsgActivity extends BaseActivity implements
	IXListViewListener, OnClickListener {
    private static final String TAG = "NotifyMsgActivity";

    private static final int countInPage = 15;

    private XListView pushListV;

    public static final int FRIST_GET_DATE = 111;// 首次加载
    public static final int REFRESH_GET_DATE = 112;// 刷新数据
    public static final int LOADMORE_GET_DATE = 113;// 获取更多数据cl
    NotifyListAdapter pushListAdapter;
    private View emptyView;
    private List<PushMsgInfo> pushMsgList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.listview_driver_fragment);
	initView();
	setInitView();
    }

    public void setCustomEdit(EditText customEdit) {
    }

    private void initView() {
	emptyView = findViewById(R.id.empty_layout);
	pushListV = (XListView) findViewById(R.id.living_lv);
	PreferenceUtils.getInstance(this).setSettingUnreadMsg(false);//将未读消息置为已读
    }

    private void setInitView() {

	pushListV.setPullLoadEnable(true);
	pushListV.setPullRefreshEnable(false);
	pushListV.setXListViewListener(this);
	pushMsgList = new ArrayList<PushMsgInfo>();
	((TextView) emptyView.findViewById(R.id.empty_tv))
		.setText("没有任何消息提醒或通知");
	pushListV.setEmptyView(emptyView);
	pushListAdapter = new NotifyListAdapter(this,
		R.layout.notifyinfo_item, pushMsgList);
	pushListV.setAdapter(pushListAdapter);
		sendRequest(FRIST_GET_DATE);// 请求数据
	emptyView.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		sendRequest(FRIST_GET_DATE);// 请求数据
	    }
	});
	if (CommonUtils.isNetWorkConnected(this)) {

	} else {

	    ((TextView) emptyView.findViewById(R.id.empty_tv))
		    .setText("好像没有联上网哦，点此刷新~~~~");

	}

	pushListV.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
	    }
	});
    }

    private TextView textView;

    // private View rightParent;

    @Override
    public void initMiddle(TextView middleView) {
	// TODO Auto-generated method stub
	super.initMiddle(middleView);
	middleView.setText("通知与消息");
    }

    public void sendRequest(final int ACTION) {
	switch (ACTION) {
	case FRIST_GET_DATE:// 第一次加载数据
	    getNotifyList();
	    break;
	case REFRESH_GET_DATE:// 刷新数据
	    getNotifyList();
	    break;
	case LOADMORE_GET_DATE:// 获取更多数据
	    getNotifyList();
	    break;
	}

    }

    @Override
    public void onDestroy() {
	super.onDestroy();
    }

    @Override
    public void onRefresh() {
	sendRequest(REFRESH_GET_DATE);
    }

    @Override
    public void onLoadMore() {
	sendRequest(LOADMORE_GET_DATE);
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {

	}
    }

    // --------------------------------------------------------------------------------------------------------------
    private void getNotifyList() {

	List<PushMsgInfo> pushListInfo = CoolDBHelper.getPushMsgListByPos(this,
		pushMsgList.size(), 15);

	if (pushListInfo.size() > 0) {//如果有拉取到新的数据那么刷新
	    pushMsgList.addAll(pushListInfo);
	    pushListAdapter.notifyDataSetChanged();
	    if (pushMsgList != null) {
		pushListV.notifyDataSetChanged(pushMsgList.size(), 15);
	    }
	} else {
	    if(pushMsgList.size()>0)
	    {
		ToolUtils.showToast(this, "没有更多的消息了");
	    }
	}
	
	pushListV.stopRefresh();
	pushListV.stopLoadMore();

	if(pushMsgList.size()==0)
	{
	    emptyView.setVisibility(View.VISIBLE);
	}
	else
	{
	    emptyView.setVisibility(View.GONE);
	}
    }
    

}
