package go.fast.fastenvelopes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import go.fast.fastenvelopes.info.MessageInfo;
import go.fast.fastenvelopes.info.SessionItemInfo;
import go.fast.fastenvelopes.utils.PreferenceUtils;

/**
 *
 *
 */
public class AttentionUserDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "userInfoDB";

    public static final String TABLE_ATTENTION = "attentionT";// 书签表

    public static final String TABLE_PERSONAL_LETTER = "personalletterT";// 私信聊天记录表

    public static final String TABLE_SESSION_LIST = "sessionListT";// 会话列表

    public static final String account = "account";// 账号


    public static final String content = "content";// 聊天内容
    public static final String headurl = "headurl";// 用户头像
    public static final String nickname = "nickname";// 用户昵称

    public static final String sex = "sex";// 性别

    public static final String level= "level";// 等级

    public static final String letterId = "letterId";// 私信的对话id（内容为被对话用户的account）

    public static final String publishTime = "publishTime";// 发布时间

    public static final String lastPublishTime = "lastPublishTime";// 最后一条会话发送的时间

    public static final String lastContent = "lastContent";// 最后一条会话发送的内容

    public static final String sessionId = "sessionId";// 会话列表Id（用户的account 防止切换账号显示出现错误）

    private final String createAttentionTable = "create table "
            + TABLE_ATTENTION + " (id integer primary key autoincrement,"
            + account + " text)";

    private final String createPersonalLetterTable = "create table "
            + TABLE_PERSONAL_LETTER
            + " (id integer primary key autoincrement,"+ letterId + " text," + account + " text,"
            + headurl + " text," + nickname + " text," + content + " text," +
            publishTime
            + " text)";
    private final String createSessionListTable = "create table "
            + TABLE_SESSION_LIST
            + " (id integer primary key autoincrement,"+sessionId+ " text,"+ account + " text,"
            + headurl + " text," + nickname + " text," + sex + " integer," + level + " text," + lastContent + " text," +
            lastPublishTime
            + " text)";

    private Context context;

    private static AttentionUserDB articleChapterDB;

    public AttentionUserDB(Context context, String name,
                           CursorFactory factory, int version) {

        super(context, name, factory, version);
        this.context = context;
    }

    public static AttentionUserDB getInstance(Context context) {
        if (articleChapterDB == null) {
            articleChapterDB = new AttentionUserDB(context, DB_NAME, null, 1);
        }
        return articleChapterDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("create a database");
        db.execSQL(createAttentionTable);
        db.execSQL(createPersonalLetterTable);
        db.execSQL(createSessionListTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // public void CreateTable() {
    // getWritableDatabase().execSQL(sql);
    // getWritableDatabase().execSQL(createMarketsTable);
    // }

    public void closeTable() {
        close();
    }

    public void DelectTable() {
        String sql = "DROP TABLE IF EXISTS " + TABLE_ATTENTION;
        getWritableDatabase().execSQL(sql);
    }

    public List<String> getAttentionList() {

        List<String> attentionList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "select * from " + TABLE_ATTENTION, null);
        while (cursor.moveToNext()) {

            attentionList.add(cursor.getString(cursor.getColumnIndex(account)));
        }
        cursor.close();
        close();
        return attentionList;

    }

    public boolean insertAttentionInfo(String account) {
        ContentValues values = new ContentValues();
        values.put("account", account);
        return getWritableDatabase().insert(TABLE_ATTENTION, null, values) != -1;
    }

    public boolean deleteAttentionInfo(String account) {
        int deleteCount = getWritableDatabase().delete(TABLE_ATTENTION,
                "account = ? ", new String[]{account});
        close();
        return deleteCount > 0;
    }

    public boolean refushAttentionList(List<String> attentionList) {

        if (attentionList != null && attentionList.size() > 0) {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("delete from attentionT");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'attentionT'");

            for (int i = 0; i < attentionList.size(); i++) {

                ContentValues values = new ContentValues();
                values.put("account", attentionList.get(i));
                db.insert(TABLE_ATTENTION, null, values);
            }
            db.close();
            return true;

        }

        return false;

    }

    //----------------------------------------------------私信表操作开始-------------------------------------------------------

    public boolean insertMessage(String letterId,MessageInfo messageInfo) {

        letterId= PreferenceUtils.getInstance(context).getSettingUserAccount()+letterId;
        Cursor cursor = getWritableDatabase().rawQuery(
                "select * from " + TABLE_PERSONAL_LETTER
                        + " order by publishTime desc " , null);

        if(cursor.getCount()>1000)//记录大于一定条数删除最老的数据
        {
            cursor.moveToLast();

            getWritableDatabase().delete(TABLE_PERSONAL_LETTER,
                    "publishTime = ? ",
                    new String[] { cursor.getString(cursor.getColumnIndex("publishTime")) });
        }
        ContentValues values = new ContentValues();

        values.put("publishTime", messageInfo.getCreatedAt());
        values.put("letterId", letterId);
        values.put("account", messageInfo.account);
        values.put("headurl", messageInfo.headurl);
        values.put("nickname", messageInfo.nickname);
        values.put("content", messageInfo.content);
        boolean isInsert = getWritableDatabase().insert(
                TABLE_PERSONAL_LETTER, null, values) != -1;
        cursor.close();
        close();
        return isInsert;
    }

    public int deleteAllMsgById(String letterId) {
        letterId= PreferenceUtils.getInstance(context).getSettingUserAccount()+letterId;
        int deleteCount = getWritableDatabase().delete(TABLE_PERSONAL_LETTER,
                "letterId = ? ", new String[] {letterId });
        close();
        return deleteCount;
    }

    /**
     * 根据letterId获取消息列表 并根据发送时间排序
     * @param letterId
     * @param startPos
     * @param count
     * @return
     */
    public List<MessageInfo> getMessageListByPos(String letterId,int startPos, int count) {
        letterId= PreferenceUtils.getInstance(context).getSettingUserAccount()+letterId;
        ArrayList<MessageInfo> messageInfoList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "select * from " + TABLE_PERSONAL_LETTER +" where letterId = "+letterId
                        + " order by publishTime desc  limit " +count
                        + " offset " + startPos, null);


        cursor.moveToLast();
        while (cursor.moveToPrevious()) {

            MessageInfo messageInfo = new MessageInfo();

            messageInfo.createdAt = Long.valueOf(cursor.getString(cursor
                    .getColumnIndex(publishTime)));
            messageInfo.content = cursor.getString(cursor
                    .getColumnIndex(content));
            messageInfo.nickname = cursor.getString(cursor
                    .getColumnIndex(nickname));
            messageInfo.headurl = cursor.getString(cursor
                    .getColumnIndex(headurl));
            messageInfo.account = cursor.getString(cursor
                    .getColumnIndex(account));


            messageInfoList.add(messageInfo);
        }
        cursor.close();
        close();
        return messageInfoList;
    }


    public List<MessageInfo> getMessageListByPublishTime(String letterId,long publishTime, int count) {
        letterId= PreferenceUtils.getInstance(context).getSettingUserAccount()+letterId;
        ArrayList<MessageInfo> messageInfoList = new ArrayList<>();
        Cursor cursor;
        if(publishTime>0)//非进入界面第一次获取记录
        {
             cursor = getWritableDatabase().rawQuery(
                    "select * from " + TABLE_PERSONAL_LETTER +" where letterId = "+letterId+" and publishTime < "+publishTime
                            + " order by publishTime desc  limit " +count
                    , null);
        }
        else//第一次获取记录
        {
            cursor = getWritableDatabase().rawQuery(
                    "select * from " + TABLE_PERSONAL_LETTER +" where letterId = "+letterId + " order by publishTime desc  limit " +count
                    , null);
        }
        cursor.moveToLast();
        if(cursor.getCount()>0)
        {
            MessageInfo lastMessageInfo = new MessageInfo();
            lastMessageInfo.createdAt = Long.valueOf(cursor.getString(cursor
                    .getColumnIndex("publishTime")));
            lastMessageInfo.content = cursor.getString(cursor
                    .getColumnIndex(content));
            lastMessageInfo.nickname = cursor.getString(cursor
                    .getColumnIndex(nickname));
            lastMessageInfo.headurl = cursor.getString(cursor
                    .getColumnIndex(headurl));
            lastMessageInfo.account = cursor.getString(cursor
                    .getColumnIndex(account));
            messageInfoList.add(lastMessageInfo);
        }
        while (cursor.moveToPrevious()) {

            MessageInfo messageInfo = new MessageInfo();

            messageInfo.createdAt = Long.valueOf(cursor.getString(cursor
                    .getColumnIndex("publishTime")));
            messageInfo.content = cursor.getString(cursor
                    .getColumnIndex(content));
            messageInfo.nickname = cursor.getString(cursor
                    .getColumnIndex(nickname));
            messageInfo.headurl = cursor.getString(cursor
                    .getColumnIndex(headurl));
            messageInfo.account = cursor.getString(cursor
                    .getColumnIndex(account));
            messageInfoList.add(messageInfo);
        }
        cursor.close();
        close();
        return messageInfoList;
    }

    //----------------------------------------------------私信表操作结束-------------------------------------------------------


    public List<SessionItemInfo> getSessionList(int count) {
        ArrayList<SessionItemInfo> sessionList = new ArrayList<>();
        Cursor cursor;

            cursor = getWritableDatabase().rawQuery(
                    "select * from " + TABLE_SESSION_LIST+" where sessionId = "+PreferenceUtils.getInstance(context).getSettingUserAccount() + " order by lastPublishTime desc  limit " +count
                    , null);


        while (cursor.moveToNext()) {

            SessionItemInfo sessionItemInfo = new SessionItemInfo();

            sessionItemInfo.lastPublishTime = Long.valueOf(cursor.getString(cursor
                    .getColumnIndex(lastPublishTime)));
            sessionItemInfo.lastContent = cursor.getString(cursor
                    .getColumnIndex(lastContent));
            sessionItemInfo.nickname = cursor.getString(cursor
                    .getColumnIndex(nickname));
            sessionItemInfo.headurl = cursor.getString(cursor
                    .getColumnIndex(headurl));
            sessionItemInfo.level = cursor.getString(cursor
                    .getColumnIndex(level));
            sessionItemInfo.sex = cursor.getInt(cursor
                    .getColumnIndex(sex));
            sessionItemInfo.account = cursor.getString(cursor
                    .getColumnIndex(account));
            sessionList.add(sessionItemInfo);
        }
        cursor.close();
        close();
        return sessionList;
    }


    //----------------------------------------------------会话表操作开始-------------------------------------------------------
    public void insertOrUpdateSessionListByAccount(SessionItemInfo sessionItemInfo) {
        if (isExitSessionByAccount(PreferenceUtils.getInstance(context).getSettingUserAccount(),sessionItemInfo.account)) {
            if (UpdateSessionByAccount(sessionItemInfo)) {

            } else {

            }
        } else {

            if (insertASession(sessionItemInfo)) {

            }

            else {

            }
        }
    }

    public boolean insertASession(SessionItemInfo sessionItemInfo) {

        Cursor cursor = getWritableDatabase().rawQuery(
                "select * from " + TABLE_SESSION_LIST
                        + " order by lastPublishTime desc " , null);

        if(cursor.getCount()>1000)//记录大于一定条数删除最老的数据
        {
            cursor.moveToLast();

            getWritableDatabase().delete(TABLE_SESSION_LIST,
                    "lastPublishTime = ? ",
                    new String[] { cursor.getString(cursor.getColumnIndex("lastPublishTime")) });
        }
        ContentValues values = new ContentValues();
        values.put("lastPublishTime", sessionItemInfo.lastPublishTime);
        values.put("sex", sessionItemInfo.sex);
        values.put("level", sessionItemInfo.level);
        values.put("sessionId",PreferenceUtils.getInstance(context).getSettingUserAccount());
        values.put("account", sessionItemInfo.account);
        values.put("headurl", sessionItemInfo.headurl);
        values.put("nickname", sessionItemInfo.nickname);
        values.put("lastContent", sessionItemInfo.lastContent);
        boolean isInsert = getWritableDatabase().insert(
                TABLE_SESSION_LIST, null, values) != -1;
        cursor.close();
        close();
        return isInsert;
    }


    public boolean UpdateSessionByAccount(SessionItemInfo sessionItemInfo) {
        ContentValues values = new ContentValues();
        values.put("lastPublishTime", sessionItemInfo.lastPublishTime);
        values.put("sex", sessionItemInfo.sex);
        values.put("level", sessionItemInfo.level);
        values.put("headurl", sessionItemInfo.headurl);
        values.put("nickname", sessionItemInfo.nickname);
        values.put("lastContent", sessionItemInfo.lastContent);

        boolean isUpdate = getWritableDatabase().update(TABLE_SESSION_LIST,
                values, "account = ? and sessionId = ? ",
                new String[] { sessionItemInfo.account,PreferenceUtils.getInstance(context).getSettingUserAccount() }) == 1;
        close();
        return isUpdate;
    }

    public boolean isExitSessionByAccount(String sessionId,String sessionAccount)

    {
        Cursor cursor = getWritableDatabase().rawQuery(
                "select * from " + TABLE_SESSION_LIST+ " where account = "
                        + sessionAccount+" and sessionId = "+sessionId, null);

        if (cursor.getCount() > 0) {
            return true;
        }

        return false;
    }

    public int deleteASession(SessionItemInfo sessionItemInfo) {
        int deleteCount = getWritableDatabase().delete(TABLE_SESSION_LIST,
                "account = ? and sessionId = ?", new String[] { sessionItemInfo.account,PreferenceUtils.getInstance(context).getSettingUserAccount()});
        close();
        return deleteCount;
    }

    public int deleteASessionByAccount(String account) {
        int deleteCount = getWritableDatabase().delete(TABLE_SESSION_LIST,
                "account = ? and sessionId = ?", new String[] { account,PreferenceUtils.getInstance(context).getSettingUserAccount() });
        close();
        return deleteCount;
    }

    //----------------------------------------------------会话表操作结束-------------------------------------------------------
}
