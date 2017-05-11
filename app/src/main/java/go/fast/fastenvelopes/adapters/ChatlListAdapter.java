/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.activity.UserInfoActivity;
import go.fast.fastenvelopes.http.ImageOptions;
import go.fast.fastenvelopes.info.MessageInfo;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DataUtils;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.ToolUtils;

/**
 * adpater
 *
 */
public class ChatlListAdapter extends ArrayAdapter<MessageInfo>
        implements OnClickListener {

    private Context context;

    private long lastShowTime;

    public ChatlListAdapter(Context context, int resource,
                            List<MessageInfo> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    private LayoutInflater inflater;
    private ViewHolder holder;

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_list_item, parent,
                    false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final MessageInfo chatItemInfo = getItem(position);


        if (chatItemInfo.chatType == CommonUtils.MESSAGE_TYPE_MSG_CONTENT)//普通聊天
        {
            holder.rightCompareImg.setVisibility(View.INVISIBLE);
            holder.leftCompareImg.setVisibility(View.INVISIBLE);
            holder.resultLayout.setVisibility(View.GONE);
            if (DataUtils.isMe(context, chatItemInfo.account))//是自己的聊天信息
            {
                holder.leftMsgLayout.setVisibility(View.INVISIBLE);
                holder.rightMsgLayout.setVisibility(View.VISIBLE);
                holder.rightContent.setBackgroundResource(R.drawable.right_msg_bg);
                holder.rightContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                holder.rightContent.setTextColor(context.getResources().getColor(R.color.gray_deeptext));
                if (ToolUtils.isNullOrEmpty(chatItemInfo.content)) {
                } else {
                    holder.rightContent.setText(chatItemInfo.content);
                }


                if (ToolUtils.isNullOrEmpty(chatItemInfo.headurl)) {
                } else {
                    ImageLoader.getInstance().displayImage(
                            chatItemInfo.headurl,
                            holder.rightHeadImg,
                            ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(20,
                                    context)));
                }


            } else {

                holder.leftMsgLayout.setVisibility(View.VISIBLE);
                holder.rightMsgLayout.setVisibility(View.INVISIBLE);
                holder.leftContent.setBackgroundResource(R.drawable.left_msg_bg);
                holder.leftContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                holder.leftContent.setTextColor(context.getResources().getColor(R.color.gray_deeptext));
                if (ToolUtils.isNullOrEmpty(chatItemInfo.content)) {
                } else {
                    holder.leftContent.setText(chatItemInfo.content);
                }


                if (ToolUtils.isNullOrEmpty(chatItemInfo.headurl)) {
                } else {
                    ImageLoader.getInstance().displayImage(
                            chatItemInfo.headurl,
                            holder.leftHeadImg,
                            ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(20,
                                    context)));
                }

            }
        } else if (chatItemInfo.chatType == CommonUtils.MESSAGE_TYPE_JOIN_ENVELOPE)//有人加入红包
        {


            holder.resultLayout.setVisibility(View.VISIBLE);
            holder.leftMsgLayout.setVisibility(View.GONE);
            holder.rightMsgLayout.setVisibility(View.GONE);
            if (ToolUtils.isNullOrEmpty(chatItemInfo.headurl)) {
            } else {
                ImageLoader.getInstance().displayImage(
                        chatItemInfo.headurl,
                        holder.resultImg,
                        ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(10,
                                context)));
            }
            holder.resultText.setText("参与了抢红包游戏");


        } else if (chatItemInfo.chatType == CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE)//正在猜
        {
            holder.resultLayout.setVisibility(View.GONE);



            if (DataUtils.isMe(context, chatItemInfo.account))//是自己的聊天信息
            {
                holder.leftMsgLayout.setVisibility(View.INVISIBLE);
                holder.rightMsgLayout.setVisibility(View.VISIBLE);
                holder.rightCompareImg.setVisibility(View.VISIBLE);
                holder.rightContent.setBackgroundResource(R.drawable.left_red_message_bg);
                holder.rightContent.setTextColor(context.getResources().getColor(R.color.living_danmu_right_color));
                holder.rightContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                if (ToolUtils.isNullOrEmpty(chatItemInfo.content)) {
                    holder.rightContent.setText(chatItemInfo.getAnswer());
                } else {
                    holder.rightContent.setText(chatItemInfo.content);
                }

                if (chatItemInfo.isPreLoading) {//预加载的数据的话不显示
                    holder.rightCompareImg.setVisibility(View.INVISIBLE);
                } else {
                    holder.rightCompareImg.setVisibility(View.VISIBLE);
                    if(chatItemInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)
                    {
                        holder.rightCompareImg.setVisibility(View.INVISIBLE);

                    }
                    else
                    {
                        if (chatItemInfo.envelopeResult == 1) {
                            holder.rightCompareImg.setImageResource(R.drawable.answer_big);
                        } else if (chatItemInfo.envelopeResult == -1) {
                            holder.rightCompareImg.setImageResource(R.drawable.answer_little);
                        } else//相等
                        {
                            holder.rightCompareImg.setImageResource(R.drawable.answer_equil);
                        }
                    }


                }


                if (ToolUtils.isNullOrEmpty(chatItemInfo.headurl)) {
                } else {
                    ImageLoader.getInstance().displayImage(
                            chatItemInfo.headurl,
                            holder.rightHeadImg,
                            ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(20,
                                    context)));
                }


            } else {

                holder.leftMsgLayout.setVisibility(View.VISIBLE);
                holder.rightMsgLayout.setVisibility(View.INVISIBLE);
                holder.leftCompareImg.setVisibility(View.VISIBLE);

                holder.leftContent.setBackgroundResource(R.drawable.right_red_message_bg);
                holder.leftContent.setTextColor(context.getResources().getColor(R.color.living_danmu_right_color));
                holder.leftContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

                if (ToolUtils.isNullOrEmpty(chatItemInfo.content)) {
                    holder.leftContent.setText(chatItemInfo.getAnswer());
                } else {
                    holder.leftContent.setText(chatItemInfo.content);
                }


                if (chatItemInfo.isPreLoading) {
                    holder.leftCompareImg.setVisibility(View.INVISIBLE);
                } else {
                    holder.leftCompareImg.setVisibility(View.VISIBLE);
                    if(chatItemInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)
                    {
                        holder.leftCompareImg.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        if (chatItemInfo.envelopeResult == 1) {
                            holder.leftCompareImg.setImageResource(R.drawable.answer_big);
                        } else if (chatItemInfo.envelopeResult == -1) {
                            holder.leftCompareImg.setImageResource(R.drawable.answer_little);
                        } else {
                            holder.leftCompareImg.setImageResource(R.drawable.answer_equil);
                        }
                    }

                }


                if (ToolUtils.isNullOrEmpty(chatItemInfo.headurl)) {
                } else {
                    ImageLoader.getInstance().displayImage(
                            chatItemInfo.headurl,
                            holder.leftHeadImg,
                            ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(20,
                                    context)));
                }

            }
        }

        else if (chatItemInfo.chatType == CommonUtils.MESSAGE_TYPE_MSG_RESULT_ENVELOPE)//红包结果展示
        {


            holder.resultLayout.setVisibility(View.VISIBLE);
            holder.leftMsgLayout.setVisibility(View.GONE);
            holder.rightMsgLayout.setVisibility(View.GONE);
            if (ToolUtils.isNullOrEmpty(chatItemInfo.headurl)) {
            } else {
                ImageLoader.getInstance().displayImage(
                        chatItemInfo.headurl,
                        holder.resultImg,
                        ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(10,
                                context)));
            }
            if(chatItemInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)
            {
                holder.resultText.setText(//如果goldsize大于0说明是本地拼接的数字。如果不是那么是聊天历史记录的取envelopeMoneySize
                        chatItemInfo.nickname+" 恭喜！领取了金额为"+((int)(chatItemInfo.goldSize>0?chatItemInfo.goldSize:chatItemInfo.envelopeMoneySize))+"金币的红包");
            }
            else
            {
                holder.resultText.setText(
                        chatItemInfo.nickname+" 猜中啦！获得了"+chatItemInfo.envelopeMoneySize+"金币的红包");
            }



        }
        else if (chatItemInfo.chatType == CommonUtils.MESSAGE_TYPE_MSG_START_ENVELOPE)//发起红包的消息展示
        {
            holder.resultLayout.setVisibility(View.VISIBLE);
            holder.leftMsgLayout.setVisibility(View.GONE);
            holder.rightMsgLayout.setVisibility(View.GONE);
            if (ToolUtils.isNullOrEmpty(chatItemInfo.headurl)) {
            } else {
                ImageLoader.getInstance().displayImage(
                        chatItemInfo.headurl,
                        holder.resultImg,
                        ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(10,
                                context)));
            }
            if(chatItemInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)
            {
                holder.resultText.setText(
                        chatItemInfo.nickname+"发了一个"+chatItemInfo.envelopeMoneySize+"金币的自由猜红包");
            }
            else
            if(chatItemInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_GUESS)
            {
                holder.resultText.setText(
                        chatItemInfo.nickname+"发了一个"+chatItemInfo.envelopeMoneySize+"金币的猜金额红包");
            }

        }

        long publishTime = chatItemInfo
                .getCreatedAt();
        long prePublishTime = 0;
        if (position > 0) {
            prePublishTime = getItem(position - 1).createdAt;
        }

        if (publishTime - prePublishTime > 1000 * 60)//间隔大于1分钟
        {
            getItem(position).isNeedShowTime = true;
        } else {

            getItem(position).isNeedShowTime = false;
        }
        if (chatItemInfo.isNeedShowTime) {
            holder.publishTime.setVisibility(View.VISIBLE);
            holder.publishTime.setText(ToolUtils.showTimeDuration(publishTime));
        } else {

            holder.publishTime.setVisibility(View.GONE);
        }

        if (chatItemInfo.isPreLoading) {
            holder.leftMsgLoadingProgress.setVisibility(View.VISIBLE);
            holder.rightMsgLoadingProgress.setVisibility(View.VISIBLE);
        } else {
            holder.leftMsgLoadingProgress.setVisibility(View.GONE);
            holder.rightMsgLoadingProgress.setVisibility(View.GONE);
        }

        holder.leftHeadImg.setOnClickListener(this);
        holder.leftHeadImg.setTag(position);
        holder.rightHeadImg.setOnClickListener(this);
        holder.rightHeadImg.setTag(position);

        return convertView;
    }

    private static class ViewHolder {

        TextView publishTime;
        public View rootView;

        RelativeLayout resultLayout;
        RelativeLayout leftMsgLayout;
        TextView leftContent;
        ImageView leftHeadImg;
        ImageView leftCompareImg;//比较结果图片
        TextView leftCompareText;// 与结果比较的结果
        ProgressBar leftMsgLoadingProgress;

        RelativeLayout rightMsgLayout;
        TextView rightContent;
        ImageView rightHeadImg;
        ImageView rightCompareImg;//比较结果图片
        TextView rightCompareText;// 与结果比较的结果
        ProgressBar rightMsgLoadingProgress;

        ImageView resultImg;
        TextView resultText;


        public ViewHolder(View rootView) {
            this.rootView = rootView;
            resultLayout = (RelativeLayout) rootView.findViewById(R.id.result_rl);
            publishTime = (TextView) rootView.findViewById(R.id.publishtime_tv);
            this.leftContent = (TextView) rootView.findViewById(R.id.left_msg_content_tv);
            this.leftMsgLayout = (RelativeLayout) rootView.findViewById(R.id.left_msg_root_rl);
            this.leftCompareText = (TextView) rootView.findViewById(R.id.left_compare_tv);
            this.leftCompareImg = (ImageView) rootView.findViewById(R.id.left_compare_iv);
            this.leftHeadImg = (ImageView) rootView.findViewById(R.id.left_msg_iv);
            this.leftMsgLoadingProgress = (ProgressBar) rootView.findViewById(R.id.left_progress_pb);

            this.rightContent = (TextView) rootView.findViewById(R.id.right_msg_content_tv);
            this.rightMsgLayout = (RelativeLayout) rootView.findViewById(R.id.right_msg_root_rl);
            this.rightCompareText = (TextView) rootView.findViewById(R.id.right_compare_tv);
            this.rightCompareImg = (ImageView) rootView.findViewById(R.id.right_compare_iv);
            this.rightHeadImg = (ImageView) rootView.findViewById(R.id.right_msg_iv);
            this.rightMsgLoadingProgress = (ProgressBar) rootView.findViewById(R.id.right_progress_pb);
            this.resultText = (TextView) rootView.findViewById(R.id.result_tv);
            this.resultImg = (ImageView) rootView.findViewById(R.id.result_iv);

        }

    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.right_msg_iv:
            case R.id.left_msg_iv:// 用户头像
                int userPos = (Integer) v.getTag();
                Intent userInfoIntent = new Intent(context, UserInfoActivity.class);
                userInfoIntent.putExtra("account", getItem(userPos).account);
                userInfoIntent.putExtra("headurl", getItem(userPos).headurl);
                userInfoIntent.putExtra("nickname", getItem(userPos).nickName);
                context.startActivity(userInfoIntent);
                ((Activity) context).overridePendingTransition(
                        R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                break;

        }

    }

}
