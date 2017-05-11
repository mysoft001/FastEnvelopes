///**
// * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package go.fast.fastenvelopes.adapters;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import java.util.List;
//
//import go.fast.fastenvelopes.R;
//import go.fast.fastenvelopes.activity.UserInfoActivity;
//import go.fast.fastenvelopes.http.ImageOptions;
//import go.fast.fastenvelopes.info.MessageInfo;
//import go.fast.fastenvelopes.utils.PixelUtil;
//import go.fast.fastenvelopes.utils.ToolUtils;
//
///**
// * adpater
// *
// */
//public class RoomSpecialListAdapter extends ArrayAdapter<MessageInfo>
//	implements OnClickListener {
//
//    private Context context;
//
//	private long lastShowTime;
//
//    public RoomSpecialListAdapter(Context context, int resource,
//	    List<MessageInfo> objects) {
//	super(context, resource, objects);
//	inflater = LayoutInflater.from(context);
//	this.context = context;
//    }
//
//    private LayoutInflater inflater;
//
//    @SuppressLint("ResourceAsColor")
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//	if (convertView == null) {
//	    convertView = inflater.inflate(R.layout.chat_list_item, parent,
//		    false);
//	}
//	ViewHolder holder = (ViewHolder) convertView.getTag();
//	if (holder == null) {
//	    holder = new ViewHolder();
//	    holder.compareText = (TextView) convertView.findViewById(R.id.compare_tv);
//
//	    holder.content = (TextView) convertView
//		    .findViewById(R.id.content_tv);
//	    holder.resultLayout = (RelativeLayout) convertView
//		    .findViewById(R.id.result_rl);
//	    holder.publishTime = (TextView) convertView
//		    .findViewById(R.id.publishtime_tv);
//	    holder.headImg = (ImageView) convertView
//		    .findViewById(R.id.headimg_iv);
//	    convertView.setTag(holder);
//	}
//
//	final MessageInfo chatItemInfo = getItem(position);
//
//		if(ToolUtils.isNullOrEmpty(chatItemInfo.content))
//		{
//			holder.content.setText(chatItemInfo.getAnswer());
//		}
//		else
//		{
//			holder.content.setText(chatItemInfo.content);
//		}
//
//		if(chatItemInfo.compare==0)
//		{
//			holder.resultLayout.setVisibility(View.VISIBLE);
//
//		}
//		else
//		{
//			holder.resultLayout.setVisibility(View.INVISIBLE);
//			if(chatItemInfo.compare==1)
//			{
//				holder.compareText.setText(context.getResources().getText(R.string.compare_large));
//			}
//			else
//			{
//				holder.compareText.setText(context.getResources().getText(R.string.compare_little));
//			}
//
//
//		}
//		if (ToolUtils.isNullOrEmpty(chatItemInfo.headurl)) {
//		} else {
//			ImageLoader.getInstance().displayImage(
//					chatItemInfo.headurl,
//					holder.headImg,
//					ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(20,
//							context)));
//		}
//
//		long publishTime=chatItemInfo
//				.getPublishTime();
//if(publishTime-lastShowTime>1000*60*2)//间隔大于两分钟
//{
//	holder.publishTime.setVisibility(View.VISIBLE);
//	holder.publishTime.setText(ToolUtils.showTimeDuration(publishTime));
//	lastShowTime=publishTime;
//}
//		else
//{
//	holder.publishTime.setVisibility(View.GONE);
//}
//
//	holder.headImg.setOnClickListener(this);
//	holder.headImg.setTag(position);
//
//	return convertView;
//    }
//
//    private static class ViewHolder {
//	public RelativeLayout msgLayout;
//	public TextView compareText;// 与结果比较的结果
//	TextView content;
//	TextView publishTime;
//	ImageView headImg;
//	RelativeLayout resultLayout;
//
//    }
//
//    String getStrng(Context context, int resId) {
//	return context.getResources().getString(resId);
//    }
//
//    @Override
//    public void onClick(View v) {
//	int position = (Integer) v.getTag();
//	switch (v.getId()) {
//
//	case R.id.headimg_iv:// 用户头像
//	    int userPos = (Integer) v.getTag();
//	    Intent userInfoIntent = new Intent(context, UserInfoActivity.class);
//	    userInfoIntent.putExtra("account", getItem(userPos).account);
//	    userInfoIntent.putExtra("headurl", getItem(userPos).headurl);
//	    userInfoIntent.putExtra("nickname", getItem(userPos).nickName);
//	    context.startActivity(userInfoIntent);
//	    ((Activity) context).overridePendingTransition(
//		    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
//	    break;
//
//	}
//
//    }
//
//}
