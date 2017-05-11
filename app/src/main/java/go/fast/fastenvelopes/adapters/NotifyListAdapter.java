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
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.PersonalLetterActivity;
import go.fast.fastenvelopes.activity.UserInfoActivity;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.ToolUtils;


/**
 * adpater
 * 
 */
public class NotifyListAdapter extends ArrayAdapter<PushMsgInfo> implements
	OnClickListener {

    private Context context;

    public NotifyListAdapter(Context context, int resource,
	    List<PushMsgInfo> objects) {
	super(context, resource, objects);
	inflater = LayoutInflater.from(context);
	this.context = context;
    }

    private LayoutInflater inflater;

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.notifyinfo_item, parent,
		    false);
	}
	ViewHolder holder = (ViewHolder) convertView.getTag();
	if (holder == null) {
	    holder = new ViewHolder();
	    holder.notifyContent = (TextView) convertView
		    .findViewById(R.id.content_tv);
	    holder.notifyTypeImg = (ImageView) convertView
		    .findViewById(R.id.zaina_avatar);
	    holder.notifyTitle = (TextView) convertView
		    .findViewById(R.id.title_tv);
	    holder.timeText = (TextView) convertView.findViewById(R.id.time_tv);
	    convertView.setTag(holder);
	}

	final PushMsgInfo pushMsgInfo = getItem(position);
	holder.timeText.setText(ToolUtils.showTimeDuration(pushMsgInfo
		.getCreatedAt()));
	SpannableString spanableInfo = null;
	int nickLength = 0;
	switch (pushMsgInfo.type) {

	case CommonUtils.PUSH_MSG_TYPE_USER_ATTENTION:// 关注提醒
	    holder.notifyTypeImg
		    .setImageResource(R.drawable.notify_type_attention);
	    holder.notifyTitle.setText("关注提醒");

	    spanableInfo = new SpannableString(pushMsgInfo.nickname + "关注了你");
	    if (!ToolUtils.isNullOrEmpty(pushMsgInfo.nickname)) {

		nickLength = pushMsgInfo.nickname.length();
	    }
	    spanableInfo.setSpan(new Clickable(new OnClickListener() {

		@Override
		public void onClick(View v) {

		    jumpUserActivity(pushMsgInfo);
		}
	    }), 0, nickLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
	    break;
	case CommonUtils.PUSH_MSG_TYPE_LETTER://收到了一条私信

	    nickLength = 0;
	    holder.notifyTypeImg
		    .setImageResource(R.drawable.notify_type_comment);
	    holder.notifyTitle.setText("消息提醒");

	    spanableInfo = new SpannableString(pushMsgInfo.nickname + "："
		    + pushMsgInfo.content);

	    if (!ToolUtils.isNullOrEmpty(pushMsgInfo.nickname)) {

		nickLength = pushMsgInfo.nickname.length();
	    }

	    spanableInfo.setSpan(new Clickable(new OnClickListener() {

		@Override
		public void onClick(View v) {

		    jumpUserActivity(pushMsgInfo);
		}
	    }), 0, nickLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

	    spanableInfo.setSpan(new Clickable(new OnClickListener() {

		@Override
		public void onClick(View v) {

			jumpPersonalLetterActivity(pushMsgInfo);
		}
	    }), nickLength + 1, nickLength + (pushMsgInfo.content==null?0:pushMsgInfo.content.length())
		    + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
	    break;
//	case PushMsgOperating.USER_ZAN_TAG:
//
//	    holder.notifyTypeImg
//		    .setImageResource(R.drawable.notify_type_zan);
//	    holder.notifyTitle.setText("点赞提示");
//
//	    spanableInfo = new SpannableString(pushMsgInfo.nickname + "赞了你");
//	    if (!ToolUtils.isNullOrEmpty(pushMsgInfo.nickname)) {
//
//		nickLength = pushMsgInfo.nickname.length();
//	    }
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpUserActivity(pushMsgInfo);
//		}
//	    }), 0, nickLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//	    break;
//
//	case PushMsgOperating.USER_CALLBACK_TAG:// 用户回复提醒
//
//	    nickLength = 0;
//	    holder.notifyTypeImg
//		    .setImageResource(R.drawable.notify_type_replay);
//	    holder.notifyTitle.setText("回复提醒");
//
//	    spanableInfo = new SpannableString(pushMsgInfo.nickname + "在作品"
//		    + "《" + pushMsgInfo.articleName + "》中回复了你");
//
//	    if (!ToolUtils.isNullOrEmpty(pushMsgInfo.nickname)) {
//
//		nickLength = pushMsgInfo.nickname.length();
//	    }
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpUserActivity(pushMsgInfo);
//		}
//	    }), 0, nickLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpDetailArticle(pushMsgInfo);
//		}
//	    }), nickLength + 3, nickLength + pushMsgInfo.articleName.length()
//		    + 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//	    break;
//	case PushMsgOperating.ARTICLE_COLLECTED_UPDATE:
//
//	    holder.notifyTypeImg
//		    .setImageResource(R.drawable.notify_type_collect);
//	    holder.notifyTitle.setText("作品更新提示");
//
//	    spanableInfo = new SpannableString("作品" + "《"
//		    + pushMsgInfo.articleName + "》更新了新的章节，快去看吧");
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpDetailArticle(pushMsgInfo);
//		}
//	    }), 2, 4 + pushMsgInfo.articleName.length(),
//		    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//	    break;
//	case PushMsgOperating.ARTICLE_CHECK:
//
//	    holder.notifyTypeImg
//		    .setImageResource(R.drawable.notify_type_check);
//	    holder.notifyTitle.setText("共享作品提交审核提示");
//
//	    spanableInfo = new SpannableString("作品" + "《"
//		    + pushMsgInfo.articleName + "》续写了新内容，需要你审核");
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpDetailArticle(pushMsgInfo);
//		}
//	    }), 2, 4 + pushMsgInfo.articleName.length(),
//		    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//	    break;
//	case PushMsgOperating.ARTICLE_SHARE:
//
//	    nickLength = 0;
//	    holder.notifyTypeImg
//		    .setImageResource(R.drawable.notify_type_share);
//	    holder.notifyTitle.setText("分享提示");
//
//	    spanableInfo = new SpannableString(pushMsgInfo.nickname + "分享了你的作品"
//		    + "《" + pushMsgInfo.articleName + "》");
//
//	    if (!ToolUtils.isNullOrEmpty(pushMsgInfo.nickname)) {
//
//		nickLength = pushMsgInfo.nickname.length();
//	    }
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpUserActivity(pushMsgInfo);
//		}
//	    }), 0, nickLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpDetailArticle(pushMsgInfo);
//		}
//	    }), nickLength + 7, nickLength + pushMsgInfo.articleName.length()
//		    + 9, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//	    break;
//
//	case PushMsgOperating.ARTICLE_COLLECTED:
//
//	    nickLength = 0;
//	    holder.notifyTypeImg
//		    .setImageResource(R.drawable.notify_type_shoucang);
//	    holder.notifyTitle.setText("收藏提示");
//
//	    spanableInfo = new SpannableString(pushMsgInfo.nickname + "收藏了你的作品"
//		    + "《" + pushMsgInfo.articleName + "》");
//
//	    if (!ToolUtils.isNullOrEmpty(pushMsgInfo.nickname)) {
//
//		nickLength = pushMsgInfo.nickname.length();
//	    }
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpUserActivity(pushMsgInfo);
//		}
//	    }), 0, nickLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//	    spanableInfo.setSpan(new Clickable(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		    jumpDetailArticle(pushMsgInfo);
//		}
//	    }), nickLength + 7, nickLength + pushMsgInfo.articleName.length()
//		    + 9, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//	    break;
	}

	holder.notifyContent.setText(spanableInfo);
	holder.notifyContent
		.setMovementMethod(LinkMovementMethod.getInstance());

	return convertView;
    }

    private static class ViewHolder {
	TextView notifyContent;// 消息的内容
	TextView notifyTitle;// 消息的标题
	TextView timeText;// 消息的时间
	ImageView notifyTypeImg;// 消息类型的img

    }

    String getStrng(Context context, int resId) {
	return context.getResources().getString(resId);
    }

    private void jumpUserActivity(PushMsgInfo pushMsgInfo) {
	Intent userInfoIntent = new Intent(context, UserInfoActivity.class);
	userInfoIntent.putExtra("account", pushMsgInfo.account);
	userInfoIntent.putExtra("headurl", pushMsgInfo.headurl);
	userInfoIntent.putExtra("nickname", pushMsgInfo.nickname);
	context.startActivity(userInfoIntent);
	((Activity) context).overridePendingTransition(
		R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

	private void jumpPersonalLetterActivity(PushMsgInfo pushMsgInfo) {

		Intent personalIntent = new Intent(context, PersonalLetterActivity.class);
		UserInfo userInfo=new UserInfo();
		userInfo.account=pushMsgInfo.account;
		userInfo.nickName=pushMsgInfo.nickname;
		userInfo.headurl=pushMsgInfo.headurl;
		personalIntent.putExtra("userInfo", userInfo);

		context.startActivity(personalIntent);

	}


    @Override
    public void onClick(View v) {
	int position = (Integer) v.getTag();
	switch (v.getId()) {

	case R.id.zaina_avatar:// 用户头像

	    break;

	case R.id.attention_tv:// 点击用户关注

	    break;

	}

    }

    /**
     * 内部类，用于截获点击富文本后的事件
     */
    public class Clickable extends ClickableSpan implements
	    OnClickListener {
	private final OnClickListener mListener;

	public Clickable(OnClickListener mListener) {
	    this.mListener = mListener;
	}

	@Override
	public void onClick(View v) {
	    mListener.onClick(v);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
	    ds.setColor(Color.parseColor("#12B7F5"));
	    ds.setUnderlineText(false); // 去除超链接的下划线
	}
    }

    /**
     * 内部类，用于截获点击富文本后的事件
     */
    public class ContentClickable extends ClickableSpan implements
	    OnClickListener {
	private final OnClickListener mListener;

	public ContentClickable(OnClickListener mListener) {
	    this.mListener = mListener;
	}

	@Override
	public void onClick(View v) {
	    mListener.onClick(v);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
	    ds.setColor(Color.parseColor("#999999"));
	    ds.setUnderlineText(false); // 去除超链接的下划线
	}
    }

}
