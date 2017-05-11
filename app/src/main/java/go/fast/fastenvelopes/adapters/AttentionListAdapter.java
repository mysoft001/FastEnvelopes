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

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.UserInfoActivity;
import go.fast.fastenvelopes.db.AttentionUserDB;
import go.fast.fastenvelopes.http.ImageOptions;
import go.fast.fastenvelopes.info.AttentionObj;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.ToolUtils;

/**
 * adpater
 * 
 */
public class AttentionListAdapter extends ArrayAdapter<AttentionObj> implements
	OnClickListener {

    private Context context;
    boolean isMyAttention;// 是否是我关注的人列表

    public AttentionListAdapter(Context context, int resource,
	    List<AttentionObj> objects, boolean isMyAttention) {
	super(context, resource, objects);
	inflater = LayoutInflater.from(context);
	this.context = context;
	this.isMyAttention = isMyAttention;
    }

    private LayoutInflater inflater;

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.attention_list_item,
		    parent, false);
	}
	ViewHolder holder = (ViewHolder) convertView.getTag();
	if (holder == null) {
	    holder = new ViewHolder();
	    holder.nickName = (TextView) convertView.findViewById(R.id.name_tv);

	    holder.createCount = (TextView) convertView
		    .findViewById(R.id.createcount_tv);
	    holder.sexImg = (ImageView) convertView.findViewById(R.id.sex_iv);
	    holder.headImg = (ImageView) convertView
		    .findViewById(R.id.zaina_avatar);
	    holder.attentionText = (TextView) convertView
		    .findViewById(R.id.attention_tv);
	    convertView.setTag(holder);
	}

	final AttentionObj squareObj = getItem(position);

	holder.nickName.setText(squareObj.nickname);

	holder.createCount.setText("" + squareObj.envelopeCounts);

	if (ToolUtils.isNullOrEmpty(squareObj.headurl)) {
	} else {
	    ImageLoader.getInstance().displayImage(
		    squareObj.headurl,
		    holder.headImg,
		    ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(20,
			    context)));
	}

	if (squareObj.sex == 1) {
	    holder.sexImg.setBackgroundResource(R.drawable.mail);
	} else {
	    holder.sexImg.setBackgroundResource(R.drawable.femail);
	}

	if (squareObj.isAttention) {
	    holder.attentionText.setSelected(true);
	    holder.attentionText.setText("取消关注");

	} else {
	    holder.attentionText.setSelected(false);
	    holder.attentionText.setText("+关注");
	}

	holder.attentionText.setOnClickListener(this);
	holder.attentionText.setTag(position);

	holder.headImg.setOnClickListener(this);
	holder.headImg.setTag(position);

	return convertView;
    }

    private static class ViewHolder {
	public TextView nickName;// 文章的名称
	TextView createCount;
	TextView attentionText;
	ImageView sexImg;
	ImageView headImg;

    }

    String getStrng(Context context, int resId) {
	return context.getResources().getString(resId);
    }

    @Override
    public void onClick(View v) {
	int position = (Integer) v.getTag();
	switch (v.getId()) {

	case R.id.zaina_avatar:// 用户头像
	    int userPos = (Integer) v.getTag();
	    Intent userInfoIntent = new Intent(context, UserInfoActivity.class);
	    userInfoIntent.putExtra("account", getItem(userPos).account);
	    userInfoIntent.putExtra("headurl", getItem(userPos).headurl);
	    userInfoIntent.putExtra("nickname", getItem(userPos).nickName);
	    context.startActivity(userInfoIntent);
	    ((Activity) context).overridePendingTransition(
		    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	    break;

	case R.id.attention_tv:// 点击用户关注
	    int attentionPos = (Integer) v.getTag();
	    if (getItem(attentionPos).isAttention) {
		cancelAttentionUser(getItem(attentionPos).account, attentionPos);
	    } else {
		attentionUser(getItem(attentionPos).account, attentionPos);
	    }
	    break;

	}

    }

    private void attentionUser(final String userAccount, final int postion) {
	HttpRequest.addUserAttention(context, userAccount,
		new HttpRequest.onRequestCallback() {

		    @Override
		    public void onSuccess(BaseInfo response) {

				if (HttpResponseUtil.isResponseOk(response)) {

					getItem(postion).isAttention = true;
					notifyDataSetChanged();
					// 更新到本地数据库
					AttentionUserDB.getInstance(context)
							.insertAttentionInfo(userAccount);

				} else {

					ToolUtils.showToast(context,response.msg);
				}
			}

		    @Override
		    public void onFailure(String rawJsonData) {

		    }
		});
    }

    private void cancelAttentionUser(final String userAccount, final int postion) {
	HttpRequest.cancelUserAttention(context, userAccount,
		new HttpRequest.onRequestCallback() {

		    @Override
		    public void onSuccess(BaseInfo response) {

			if (HttpResponseUtil.isResponseOk(response)) {

			    getItem(postion).isAttention = false;
			    notifyDataSetChanged();
			    // 更新到本地数据库
			    AttentionUserDB.getInstance(context)
				    .deleteAttentionInfo(userAccount);

			} else if (response != null
				&& response.getStatus().equals("attentioned")) {
			    ToolUtils.showToast(context, "你已经关注过了，不要重复关注");
			    // getItem(postion).isAttention = true;
			    // notifyDataSetChanged();
			} else if (response.getStatus().equals("login"))// 在其他地方登录
			{
			    HttpRequest.reLogin(context);
			} else {
			    ToolUtils.showToast(context, "取消关注未成功，请稍后再试");
			}

		    }

		    @Override
		    public void onFailure(String rawJsonData) {

		    }
		});
    }

}
