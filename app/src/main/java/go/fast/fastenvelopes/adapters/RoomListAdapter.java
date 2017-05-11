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
import android.content.Context;
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
import go.fast.fastenvelopes.groupimageview.NineGridImageView;
import go.fast.fastenvelopes.groupimageview.NineGridImageViewAdapter;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.http.ImageOptions;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.RoomItemInfo;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.ToolUtils;
import me.maxwin.view.XListView;

/**
 * adpater
 * 
 */
public class RoomListAdapter extends ArrayAdapter<RoomItemInfo> implements
	OnClickListener {

    private Context context;
    private XListView targetListV;


    public RoomListAdapter(Context context, int resource,
						   List<RoomItemInfo> objects) {
	super(context, resource, objects);
	inflater = LayoutInflater.from(context);
	this.context = context;
    }

    public RoomListAdapter(Context context, int resource,
						   List<RoomItemInfo> objects, XListView targetListV) {
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
	    convertView = inflater.inflate(R.layout.room_list_item, parent,
		    false);
	}
	ViewHolder holder = (ViewHolder) convertView.getTag();
	if (holder == null) {
	    holder = new ViewHolder();
	    holder.isLockImg = (ImageView) convertView
		    .findViewById(R.id.islock_iv);
	    holder.roomIdText = (TextView) convertView
		    .findViewById(R.id.roomid_tv);
		holder.headImgGrid = (NineGridImageView) convertView
				.findViewById(R.id.gridImg_ngv);
		holder.roomNameText = (TextView) convertView
				.findViewById(R.id.name_tv);
		holder.roomTypeText = (TextView) convertView
				.findViewById(R.id.envelope_type_tv);
		holder.roomIdText = (TextView) convertView
				.findViewById(R.id.roomid_tv);
	    holder.crateTimeText = (TextView) convertView.findViewById(R.id.time_tv);
	    holder.personSizeText = (TextView) convertView
		    .findViewById(R.id.personsize_tv);
	    convertView.setTag(holder);
	}



	// Log.d("log","position========"+position);
	final RoomItemInfo roomInfo = getItem(position);
	if (roomInfo == null) {
	    return convertView;
	}

		if(roomInfo.roomIsPass==1)
		{
			holder.isLockImg.setImageResource(R.drawable.room_lock_icon);
		}
		else
		{
			holder.isLockImg.setImageResource(R.drawable.room_unlock_icon);
		}
		holder.roomIdText.setText("房间号："+roomInfo.roomId);

		NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
			@Override
			protected void onDisplayImage(Context context, ImageView imageView, String s) {

				System.out.print("  onDisplayImage        "+s);
				ImageLoader.getInstance().displayImage(
						s,
						imageView,
						ImageOptions.getOptionsTouxiangByRound(PixelUtil.dp2Pixel(
								2, context)));

				//Picasso.with(context).load(s).placeholder(R.mipmap.ic_holding).error(R.mipmap.ic_error).into(imageView);
			}

			@Override
			protected ImageView generateImageView(Context context) {
				return super.generateImageView(context);
			}
		};
		holder.headImgGrid.setAdapter(mAdapter);
		holder.headImgGrid.setImagesData(roomInfo.getMemberHeadurl());
		holder.roomNameText.setText(roomInfo.roomName);
		if(roomInfo.roomType== CommonUtils.ENVELOPE_TYPE_GUESS)
		{
			holder.roomTypeText.setText("猜金额红包");
		}
	else if(roomInfo.roomType== CommonUtils.ENVELOPE_TYPE_FREE)
		{
			holder.roomTypeText.setText("自由猜红包");
		}
		else if(roomInfo.roomType== CommonUtils.ENVELOPE_TYPE_NEAR)
		{
			holder.roomTypeText.setText("近者得红包");
		}

		holder.crateTimeText.setText(ToolUtils.showTimeDuration(roomInfo.getRoomCreatedAt()));
		holder.personSizeText.setText(roomInfo.roomCounts+"人");

	return convertView;
    }

    private static class ViewHolder {
	public ImageView isLockImg;//是否加锁
	public TextView roomIdText;//房间号
	public NineGridImageView headImgGrid;//头像
	//public TextView name;// 文章的名称
	TextView roomNameText;//房间名
	TextView roomTypeText;// 房间类型
		TextView crateTimeText;// 房间创建的时间
		TextView personSizeText;// 房间人数

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
