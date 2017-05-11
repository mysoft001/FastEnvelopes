package go.fast.fastenvelopes.db;

import android.content.Context;

import java.util.List;

import go.fast.fastenvelopes.info.MessageInfo;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.info.SessionItemInfo;


public class CoolDBHelper {

//    public static boolean isEndChapterByChapterId(Context context,
//	    String articleId, int chapterId, long lastPublishTime) {
//
//	ArticleChapterDB db = ArticleChapterDB.getInstance(context);
//
//	return db
//		.isEndChapterByChapterId(articleId, chapterId, lastPublishTime);
//    }


    // --------------------------------------------------消息通知表相关操作---------------------------------------------------------------------

    public static boolean insertNotify(Context context, PushMsgInfo pushMsgInfo) {

	NotificationDB db = NotificationDB.getInstance(context);

	return db.insertNotify(pushMsgInfo);

    }

    /**
     * 获取指定位置开始的 一定条数的数据
     * 
     * @param startPos
     * @param count
     * @return
     */
    public static List<PushMsgInfo> getPushMsgListByPos(Context context,
	    int startPos, int count) {
	NotificationDB db = NotificationDB.getInstance(context);

	return db.getPushMsgListByPos(startPos, count);
    }

    public static void deleteANotifyByCreateTime(Context context,
	    PushMsgInfo info) {
	NotificationDB db = NotificationDB.getInstance(context);

	db.deleteANotifyByCreateTime(info);
    }

    public static void deleteNotifyList(Context context,
	    List<PushMsgInfo> listNotify) {
	NotificationDB db = NotificationDB.getInstance(context);
	db.deleteNotifyList(listNotify);
    }

    public static void deleteAllNotify(Context context) {

	NotificationDB db = NotificationDB.getInstance(context);
	db.deleteAllNotify();

    }

	//-----------------------------私信------------------------------

	public static boolean insertMessage(Context context,String letterId,MessageInfo messageInfo) {
		AttentionUserDB db=AttentionUserDB.getInstance(context);

		return db.insertMessage(letterId,messageInfo);
	}

	public static int deleteAllMsgById(Context context,String letterId) {
		AttentionUserDB db=AttentionUserDB.getInstance(context);

		return db.deleteAllMsgById(letterId);
	}

	/**
	 * 根据letterId获取消息列表 并根据发送时间排序
	 * @param letterId
	 * @param startPos
	 * @param count
	 * @return
	 */
	public static List<MessageInfo> getMessageListByPos(Context context,String letterId,int startPos, int count) {
		AttentionUserDB db=AttentionUserDB.getInstance(context);
		return db.getMessageListByPos(letterId,startPos,count);
	}

	public static List<MessageInfo> getMessageListByPublishTime(Context context,String letterId,long publishTime, int count) {
		AttentionUserDB db=AttentionUserDB.getInstance(context);
		return db.getMessageListByPublishTime(letterId,publishTime,count);
	}


	//------------------------------------会话----------------------------------


	public static List<SessionItemInfo> getSessionList(Context context,int count) {

		AttentionUserDB db=AttentionUserDB.getInstance(context);
		return db.getSessionList(count);

	}
	public  static void insertOrUpdateSessionListByAccount(Context context,SessionItemInfo sessionItemInfo) {

		AttentionUserDB db=AttentionUserDB.getInstance(context);
		db.insertOrUpdateSessionListByAccount(sessionItemInfo);

	}

	public static int deleteASession(Context context,SessionItemInfo sessionItemInfo) {
		AttentionUserDB db=AttentionUserDB.getInstance(context);


		return db.deleteASession(sessionItemInfo);
	}

	public static int deleteASessionByAccount(Context context,String account) {
		AttentionUserDB db=AttentionUserDB.getInstance(context);
		return db.deleteASessionByAccount(account);
	}
}
