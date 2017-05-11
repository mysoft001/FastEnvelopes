package go.fast.fastenvelopes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.info.RoomInfo;
import go.fast.fastenvelopes.utils.ToolUtils;


/**
 * 
 * 
 */
public class NotificationDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "notificationDB";

    public static final int BOOKSHELF_TYPE_MYWATCH = 1;
    public static final int BOOKSHELF_TYPE_MYCOLLECTION = 2;
    public static final int BOOKSHELF_TYPE_MYWORKS = 3;

    public static final String TABLE_LOCAL_NOTIFICATION = "cache_notification_table";// 消息通知表

	public static final String TABLE_ATTENTION_ROOM= "attention_room_table";// 关注的房间表

	public static final String TABLE_CHAT_SESSION= "attention_chat_session";// 会话表

    // （一部作品只会对应一个书签）

    public static final String createTime = "createTime";// 创建的时间

    public static final String type = "type";
    // 消息类型（
    // 1、收藏提示（xxx 收藏了你的作品 《XXX》）
    // 2、关注提示（XXX在 两小时前关注了你）
    // 3、收藏的作品更新提示（《XXX》更新了新的内容）
    // 4、共享作品提交审核提示（XXX 写了你的作品《XXX》 请审核）
    // 5、点赞提示（XXX赞了你）
    // 6、评论提示（XXX评论了你的作品《XXX》）
    // 7、分享提示（XXX 分享了你的作品《XXX》）
    // 8、回复提示（XXX在《XXX》回复了你））

    public static final String articleUrl = "articleUrl";// 作品封面
    public static final String articleName = "articleName";// 作品名称

    public static final String nickName = "nickName";// 用户昵称
    public static final String headurl = "headurl";// 用户头像
    public static final String account = "account";// 用户账号

  //  public static final String content = "content";// 内容


    private final String notifyTable = "create table "
	    + TABLE_LOCAL_NOTIFICATION
	    + " (id integer primary key autoincrement," + type
	    + " integer," + roomId + " text," + roomName + " text,"
	     + nickName + " text," + headurl + " text,"
	    + account + " text," + content + " text," + createTime + " text)";


	public static final String roomName="roomName";// 房间名称

	public static final String roomType="roomType";// 房间类型（红包的玩法）

	public static final String roomId="roomId";// 房间的id

	public static final String lastContent="lastContent";//最后发言的内容

	public static final String lastAccount="lastAccount";//最后发言的人的账号

	public static final String lastNickname="lastNickname";//最后发言的人的昵称

	public static final String hasPassWord="hasPassWord";//是否有密码（0 没有 1有）

	public static final String maxCount="maxCount";//最大加入人数

	private final String roomTable = "create table "
			+ TABLE_ATTENTION_ROOM
			+ " (id integer primary key autoincrement," + roomType
			+ " integer," + roomName + " text," + lastContent + " text,"
			+ lastAccount + " text," + lastNickname + " text," + hasPassWord + " integer,"
			+ maxCount + " integer,"  + createTime + " text)";


	public static final String content="content";// 聊天的内容（与answer二者只有其中一个有值）
	public static final String answer="answer";//用户本次给出的答案
	public static final String secretCode="secretCode";// 本次红包设定的结果值
	public static final String compare="compare";//与结果的比较值（0 猜中与结果相，1 比结果大，-1 比结果小）

	//public int type;// 红包的玩法

	//public static final String roomId="";// 房间的id
	public static final String  publishTime="publishTime";//该聊天发布的时间

	private final String chatTable = "create table "
			+ TABLE_CHAT_SESSION
			+ " (id integer primary key autoincrement," + roomId
			+ " integer," + content + " text," + answer + " text,"
			+ secretCode + " text," + compare + " integer," + publishTime + " text)";

    private Context context;

    private static NotificationDB notificationDB;

    public static NotificationDB getInstance(Context context) {
	if (notificationDB == null) {
	    notificationDB = new NotificationDB(context, DB_NAME, null, 1);
	}
	return notificationDB;
    }

    public NotificationDB(Context context, String name, CursorFactory factory,
	    int version) {
	super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

	System.out.println("create a database");
	db.execSQL(notifyTable);// 1

    }

    // 定义升级函数
    private void upgradeDatabaseToVersion2(SQLiteDatabase db) {
	String deleteChapter = "DROP TABLE IF EXISTS "
		+ TABLE_LOCAL_NOTIFICATION;
	db.execSQL(deleteChapter);
    }

    // 重写onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {

	System.out.println("  onUpgrade     " + oldVersion
		+ "    currentVersion   " + currentVersion);
	switch (oldVersion) {
	case 1:
	    //
	    if (currentVersion <= 1) {
		return;
	    }

	    db.beginTransaction();
	    try {
		upgradeDatabaseToVersion2(db);
		System.out.println("  onUpgrade     " + oldVersion
			+ "    currentVersion   " + currentVersion);
		db.setTransactionSuccessful();
	    } catch (Throwable ex) {

		System.out.println("   onUpgrade     Throwable    " + ex);
		break;
	    } finally {
		db.endTransaction();
	    }

	    return;
	}
    }

    public void closeTable() {
	close();
    }

    public boolean insertNotify(PushMsgInfo pushMsgInfo) {

	Cursor cursor = getWritableDatabase().rawQuery(
		"select * from " + TABLE_LOCAL_NOTIFICATION
			+ " order by createTime desc " , null);
	
	if(cursor.getCount()>50)//记录大于一定条数删除最老的数据
	{
	    cursor.moveToLast();
	    
	    getWritableDatabase().delete(TABLE_LOCAL_NOTIFICATION,
			"createTime = ? ",
			new String[] { cursor.getString(cursor.getColumnIndex("createTime")) });
	}
	ContentValues values = new ContentValues();

	values.put("account", pushMsgInfo.account);
	values.put("nickName", pushMsgInfo.nickname);
		values.put("content", pushMsgInfo.content);
	values.put("createTime", pushMsgInfo.createdAt);
	values.put("type", pushMsgInfo.type);
	boolean isInsert = getWritableDatabase().insert(
		TABLE_LOCAL_NOTIFICATION, null, values) != -1;
	cursor.close();
	close();
	return isInsert;

    }

    /**
     * 获取指定位置开始的 一定条数的数据
     * 
     * @param startPos
     * @param count
     * @return
     */
    public List<PushMsgInfo> getPushMsgListByPos(int startPos, int count) {
	ArrayList<PushMsgInfo> pushMsgList = new ArrayList<>();

	Cursor cursor = getWritableDatabase().rawQuery(
		"select * from " + TABLE_LOCAL_NOTIFICATION
			+ " order by createTime desc  limit " +count 
			+ " offset " + startPos, null);
	
	while (cursor.moveToNext()) {

	    PushMsgInfo pushMsgInfo = new PushMsgInfo();



	    pushMsgInfo.account = cursor.getString(cursor
		    .getColumnIndex(account));
	    pushMsgInfo.nickname = cursor.getString(cursor
		    .getColumnIndex(nickName));

		if(ToolUtils.isNullOrEmpty(createTime)||createTime.equalsIgnoreCase("null"))
		{
			pushMsgInfo.createdAt = System.currentTimeMillis();
		}
		else
		{
			try {
				pushMsgInfo.createdAt = Long.valueOf(cursor.getString(cursor
						.getColumnIndex(createTime)));
			}
			catch (Exception e)
			{

			}

		}

	    pushMsgInfo.content = cursor.getString(cursor
		    .getColumnIndex(content));
	    pushMsgInfo.type = cursor.getInt(cursor
		    .getColumnIndex(type));

	    pushMsgList.add(pushMsgInfo);
	}
	cursor.close();
	close();
	return pushMsgList;
    }

    public void deleteANotifyByCreateTime(PushMsgInfo info) {
	getWritableDatabase().delete(TABLE_LOCAL_NOTIFICATION,
		"createTime = ? ",
		new String[] { String.valueOf(info.createTime) });
	close();
    }

    public void deleteNotifyList(List<PushMsgInfo> listNotify) {
	if (listNotify == null) {
	    return;
	}

	for (int i = 0; i < listNotify.size(); i++) {
	    deleteANotifyByCreateTime(listNotify.get(i));

	}
	close();

    }

    public void deleteAllNotify() {
	getWritableDatabase().execSQL("delete from cache_notification_table");

	getWritableDatabase()
		.execSQL(
			"DELETE FROM sqlite_sequence WHERE name = 'cache_notification_table'");
	close();
    }

    // -----------------------------------------------房间表操作---------------------------------------------


	public boolean insertRoom(RoomInfo roomInfo) {

		Cursor cursor = getWritableDatabase().rawQuery(
				"select * from " + TABLE_ATTENTION_ROOM
						+ " order by createTime desc " , null);

		if(cursor.getCount()>50)//记录大于一定条数删除最老的数据
		{
			cursor.moveToLast();

			getWritableDatabase().delete(TABLE_ATTENTION_ROOM,
					"createTime = ? ",
					new String[] { cursor.getString(cursor.getColumnIndex("createTime")) });
		}
		ContentValues values = new ContentValues();

		values.put("createTime", roomInfo.createTime);
		values.put("hasPassWord", roomInfo.hasPassWord);
		values.put("lastAccount", roomInfo.lastAccount);
		values.put("lastContent", roomInfo.lastContent);
		values.put("lastNickname", roomInfo.lastNickname);
		values.put("maxCount", roomInfo.maxCount);
		values.put("roomId", roomInfo.roomId);
		values.put("roomName", roomInfo.roomName);
		boolean isInsert = getWritableDatabase().insert(
				TABLE_ATTENTION_ROOM, null, values) != -1;
		cursor.close();
		close();
		return isInsert;
	}

	/**
	 * 获取指定位置开始的 一定条数的数据
	 *
	 * @param startPos
	 * @param count
	 * @return
	 */
	public List<RoomInfo> getRoomListByPos(int startPos, int count) {
		ArrayList<RoomInfo> roomInfoList = new ArrayList<>();
		Cursor cursor = getWritableDatabase().rawQuery(
				"select * from " + TABLE_LOCAL_NOTIFICATION
						+ " order by createTime desc  limit " +count
						+ " offset " + startPos, null);

		while (cursor.moveToNext()) {

			RoomInfo roomInfo = new RoomInfo();

			roomInfo.createTime = Long.valueOf(cursor.getString(cursor
					.getColumnIndex(createTime)));
			roomInfo.hasPassWord = cursor.getInt(cursor
					.getColumnIndex(hasPassWord));
			roomInfo.lastAccount = cursor.getString(cursor
					.getColumnIndex(lastAccount));
			roomInfo.lastContent = cursor.getString(cursor
					.getColumnIndex(lastContent));
			roomInfo.lastNickname = cursor.getString(cursor
					.getColumnIndex(lastNickname));
			roomInfo.lastAccount = cursor.getString(cursor
					.getColumnIndex(lastAccount));
			roomInfo.maxCount = cursor.getInt(cursor
					.getColumnIndex(maxCount));
			roomInfo.roomId = cursor.getString(cursor
					.getColumnIndex(roomId));
			roomInfo.roomName = cursor.getString(cursor
					.getColumnIndex(roomName));

			roomInfoList.add(roomInfo);
		}
		cursor.close();
		close();
		return roomInfoList;
	}

	public void deleteRoomById(PushMsgInfo info) {
		getWritableDatabase().delete(TABLE_ATTENTION_ROOM,
				"createTime = ? ",
				new String[] { String.valueOf(info.createTime) });
		close();
	}




}
