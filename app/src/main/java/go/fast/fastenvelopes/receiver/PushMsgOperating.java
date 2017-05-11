package go.fast.fastenvelopes.receiver;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import go.fast.fastenvelopes.activity.PersonalLetterActivity;
import go.fast.fastenvelopes.activity.ShowAttentionActivity;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.utils.ToolUtils;

import static go.fast.fastenvelopes.utils.CommonUtils.PUSH_MSG_TYPE_LETTER;
import static go.fast.fastenvelopes.utils.CommonUtils.PUSH_MSG_TYPE_USER_ATTENTION;


public class PushMsgOperating {


    public static void pushMsgDoing(final Context context,
                                    PushMsgInfo pushMsgInfo) {
        if (pushMsgInfo == null) {
            return;
        }
        switch (pushMsgInfo.type) {

            case PUSH_MSG_TYPE_USER_ATTENTION://获取用户收到的消息
                userAttentionYou(context, pushMsgInfo);
                break;
            case PUSH_MSG_TYPE_LETTER:
                receiverLetter(context, pushMsgInfo);
                break;


        }

    }

    private static void receiveMessage(final Context context,
                                       final PushMsgInfo pushMsgInfo) {

        ToolUtils.showPopNotify(context, new String[]{pushMsgInfo.nickname
                + " 关注了你"}, new OnClickListener() {

            @Override
            public void onClick(View v) {

//		Intent attentionIntent = new Intent(context,
//			ShowAttentionActivity.class);
//		attentionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		attentionIntent
//			.setAction(".wholewriter.showAttentionAction");
//		context.startActivity(attentionIntent);
            }
        });
    }

    private static void userAttentionYou(final Context context,
                                         final PushMsgInfo pushMsgInfo) {

        ToolUtils.showPopNotify(context, new String[]{pushMsgInfo.nickname
                + " 关注了你"}, new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent attentionIntent = new Intent(context,
                        ShowAttentionActivity.class);
                attentionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                attentionIntent.setAction("go.fast.fastenvelopes.showAttentionAction");
                context.startActivity(attentionIntent);
            }
        });


    }

    /**
     * 有用户评论了你的作品
     *
     * @param context
     * @param pushMsgInfo
     */
    private static void receiverLetter(final Context context,
                                       final PushMsgInfo pushMsgInfo) {

        ToolUtils.showPopNotify(context, new String[]{pushMsgInfo.nickname
                        + " 发送了一条私信"},
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        UserInfo userInfo = new UserInfo();
                        userInfo.account = pushMsgInfo.account;
                        userInfo.nickName = pushMsgInfo.nickname;
                        userInfo.headurl = pushMsgInfo.headurl;
                        Intent letterIntent = new Intent(context,
                                PersonalLetterActivity.class);
                        letterIntent.putExtra("fromNotify", true);
                        letterIntent.putExtra("userInfo", userInfo);
                        letterIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(letterIntent);

                    }
                });
    }

    private static void userZan(final Context context,
                                final PushMsgInfo pushMsgInfo) {

        ToolUtils.showPopNotify(context, new String[]{pushMsgInfo.nickname
                        + " 赞了你"},
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // Intent intent = new Intent(context,
                        // DetailArticleInfoActivity.class);
                        // intent.putExtra("pageInfo", pushMsgInfo);
                        // context.startActivity(intent);;
                    }
                });

//	ToolUtils.showPopNotify(context, new String[] { pushMsgInfo.nickname
//	+ "赞了你的作品" + "《" + pushMsgInfo.articleName + "》" },
//	new OnClickListener() {
//
//	    @Override
//	    public void onClick(View v) {
//
//		// Intent intent = new Intent(context,
//		// DetailArticleInfoActivity.class);
//		// intent.putExtra("pageInfo", pushMsgInfo);
//		// context.startActivity(intent);;
//	    }
//	});
    }

    private static void userCallback(final Context context,
                                     final PushMsgInfo pushMsgInfo) {

//
    }

    private static void collectedArticleUpdate(final Context context,
                                               final PushMsgInfo pushMsgInfo) {

//	ToolUtils.showPopNotify(context, new String[] { "作品" + "《"
//		+ pushMsgInfo.articleName + "》更新了新的章节，快去看吧" },
//		new OnClickListener() {
//
//		    @Override
//		    public void onClick(View v) {
//
//			Intent fullScreenIntent = new Intent(context,
//				BookPlayActivity.class);
//			fullScreenIntent.putExtra("pageInfo", pushMsgInfo);
//			context.startActivity(fullScreenIntent);
//		    }
//		});
    }

    private static void articleNeedCheck(final Context context,
                                         final PushMsgInfo pushMsgInfo) {

//	ToolUtils.showPopNotify(context, new String[] { "作品" + "《"
//		+ pushMsgInfo.articleName + "》续写了新内容，需要你审核" },
//		new OnClickListener() {
//
//		    @Override
//		    public void onClick(View v) {
//
//			Intent intent = new Intent(context,
//				DetailArticleInfoActivity.class);
//			intent.putExtra("pageInfo", pushMsgInfo);
//			context.startActivity(intent);
//		    }
//		});
    }

    private static void shareYourArticle(final Context context,
                                         final PushMsgInfo pushMsgInfo) {

//	ToolUtils.showPopNotify(context, new String[] { pushMsgInfo.nickname
//		+ " 分享了你的作品" + "《" + pushMsgInfo.articleName + "》" },
//		new OnClickListener() {
//
//		    @Override
//		    public void onClick(View v) {
//
//		    }
//		});
    }

    private static void coolectYourArticle(final Context context,
                                           final PushMsgInfo pushMsgInfo) {

//	ToolUtils.showPopNotify(context, new String[] { pushMsgInfo.nickname
//		+ " 收藏了你的作品" + "《" + pushMsgInfo.articleName + "》" },
//		new OnClickListener() {
//
//		    @Override
//		    public void onClick(View v) {
//			Intent intent = new Intent(context,
//				DetailArticleInfoActivity.class);
//			intent.putExtra("pageInfo", pushMsgInfo);
//			context.startActivity(intent);
//		    }
//		});
    }

    private static void rewardArticle(final Context context,
                                      final PushMsgInfo pushMsgInfo) {

//	ToolUtils.showPopNotify(context, new String[] { pushMsgInfo.nickname
//		+ " 打赏了你的作品 " + "《" + pushMsgInfo.articleName + "》"+pushMsgInfo.content+"  个金币" },
//		new OnClickListener() {
//
//		    @Override
//		    public void onClick(View v) {
//			Intent intent = new Intent(context,
//				DetailArticleInfoActivity.class);
//			intent.putExtra("pageInfo", pushMsgInfo);
//			context.startActivity(intent);
        //}
        //	});
    }
}
