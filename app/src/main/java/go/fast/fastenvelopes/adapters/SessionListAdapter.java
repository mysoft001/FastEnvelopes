/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package go.fast.fastenvelopes.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.UserInfoActivity;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.http.ImageOptions;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.SessionItemInfo;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.ToolUtils;
import me.maxwin.view.XListView;

/**
 * adpater
 * 
 */
public class SessionListAdapter extends ArrayAdapter<SessionItemInfo> implements
	OnClickListener {

    private Context context;
    private XListView targetListV;


    public SessionListAdapter(Context context, int resource,
							  List<SessionItemInfo> objects) {
	super(context, resource, objects);
	inflater = LayoutInflater.from(context);
	this.context = context;
    }

    public SessionListAdapter(Context context, int resource,
							  List<SessionItemInfo> objects, XListView targetListV) {
	super(context, resource, objects);
	inflater = LayoutInflater.from(context);
	this.context = context;
	this.targetListV = targetListV;
    }



    private LayoutInflater inflater;

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.session_list_item, parent,
		    false);
	}
	ViewHolder holder = (ViewHolder) convertView.getTag();
	if (holder == null) {
	    holder = new ViewHolder();

	    holder.levelImg = (ImageView) convertView
		    .findViewById(R.id.level_iv);
	    holder.lastContentText = (TextView) convertView
		    .findViewById(R.id.lastcontent_tv);
	    holder.sexImg = (ImageView) convertView.findViewById(R.id.sex_iv);
	    holder.headImg = (ImageView) convertView
		    .findViewById(R.id.headimg_iv);
		holder.nickName = (TextView) convertView
				.findViewById(R.id.name_tv);

	    holder.publishTime = (TextView) convertView
		    .findViewById(R.id.publishtime_tv);
	    convertView.setTag(holder);
	}

	/*
	 * if(position%2==0) {
	 * holder.list_item_layout.setBackgroundResource(R.drawable
	 * .mm_listitem); }else{
	 * holder.list_item_layout.setBackgroundResource(R.
	 * drawable.mm_listitem_grey); }
	 */

	// Log.d("log","position========"+position);
	final SessionItemInfo sessionItemInfo = getItem(position);
	if (sessionItemInfo == null) {
	    return convertView;
	}
		int level=Integer.valueOf(sessionItemInfo.level);
		if(level==1)
		{
			holder.levelImg.setImageResource(R.drawable.level1_icon);
		}
		else if(level==2)
		{
			holder.levelImg.setImageResource(R.drawable.level2_icon);
		}
		else if(level==3)
		{
			holder.levelImg.setImageResource(R.drawable.level3_icon);
		}
		else if(level==4)
		{
			holder.levelImg.setImageResource(R.drawable.level4_icon);
		}
		else if(level==5)
		{
			holder.levelImg.setImageResource(R.drawable.level5_icon);
		}

		holder.lastContentText.setText(sessionItemInfo.lastContent);
		holder.publishTime.setText(ToolUtils.showTimeDuration(sessionItemInfo.getLastPublishTime()));

		if(sessionItemInfo.sex==1)
		{
			holder.sexImg.setImageResource(R.drawable.sex);
		}
		else
		{
			holder.sexImg.setImageResource(R.drawable.woman);
		}

	holder.headImg.setTag(Integer.valueOf(position));
	holder.headImg.setOnClickListener(this);
		holder.nickName.setText(sessionItemInfo.nickname);

	if (ToolUtils.isNullOrEmpty(sessionItemInfo.headurl)) {
	} else {
	    ImageLoader.getInstance().displayImage(
				sessionItemInfo.headurl,
		    holder.headImg,
		    ImageOptions.getOptionsTouxiangByRound(PixelUtil.dp2Pixel(
			    30, context)));
	}
	return convertView;
    }

    private static class ViewHolder {
	public ImageView levelImg;
		public TextView nickName;
	public TextView lastContentText;
	public ImageView sexImg;
	TextView publishTime;// 发布的时间
	ImageView headImg;

    }

    String getStrng(Context context, int resId) {
	return context.getResources().getString(resId);
    }

    @Override
    public void notifyDataSetChanged() {
	super.notifyDataSetChanged();
	if (targetListV != null) {
	    targetListV.notifyDataSetChanged(getCount(), 5);// 设置如果评论条数少于5条的时候不显示显示更多按钮
	}

    }

    public void notifyDataSetChanged(boolean isShowMore) {
	super.notifyDataSetChanged();
	if (!isShowMore && targetListV != null) {
	    targetListV.notifyDataSetChanged(getCount(), 100000);
	}

    }

    @Override
    public void onClick(View v) {
	int position = (Integer) v.getTag();
	switch (v.getId()) {

	case R.id.headimg_iv:
	case R.id.name_tv:
	    Intent userInfoIntent = new Intent(context, UserInfoActivity.class);
	    userInfoIntent.putExtra("account", getItem(position).account);
	    userInfoIntent.putExtra("headurl", getItem(position).headurl);
	    userInfoIntent.putExtra("nickname", getItem(position).nickName);
	    context.startActivity(userInfoIntent);
	    ((Activity) context).overridePendingTransition(
		    R.anim.slide_in_from_left, R.anim.slide_out_to_right);

	    break;


	}

    }

    // <!-------------------------------------------请求数据---------------------------------!>

    private void sendInfo(String url, String[][] params) {
	HttpRestClient.post(url, params,
		new BaseJsonHttpResponseHandler<BaseInfo>() {

		    @Override
		    public void onSuccess(int statusCode,
			    cz.msebera.android.httpclient.Header[] headers,
			    String rawJsonResponse, BaseInfo response) {

		    }

		    @Override
		    public void onFailure(int statusCode,
			    cz.msebera.android.httpclient.Header[] headers,
			    Throwable throwable, String rawJsonData,
			    BaseInfo errorResponse) {

		    }

		    @Override
		    protected BaseInfo parseResponse(String rawJsonData,
			    boolean isFailure) throws Throwable {
			return null;
		    }
		});

    }

}
