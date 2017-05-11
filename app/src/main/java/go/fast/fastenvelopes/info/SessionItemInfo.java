package go.fast.fastenvelopes.info;



/**
 * 会话信息
 */
public class SessionItemInfo extends UserInfo{

    public String lastContent="还没有聊过呢";// 最后会话内容

    public String sessionId;

    public long  lastPublishTime=System.currentTimeMillis();//房间被创建的时间

    public long getLastPublishTime()
    {
        if(lastPublishTime<10000000000L)
        {
            lastPublishTime=Long.valueOf(lastPublishTime+"000");
        }
        return lastPublishTime;
    }

    public void setLastPublishTime(long lastPublishTime)
    {
        this.lastPublishTime = lastPublishTime;
    }





}
