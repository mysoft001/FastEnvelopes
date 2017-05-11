package go.fast.fastenvelopes.info;

import java.util.List;

/**
 * 房间信息
 */
public class RoomInfo extends BaseInfo{

    public String roomName;// 房间名称

    public int roomType;// 房间类型（红包的玩法）

    public String roomId;// 房间的id

    public String lastContent;//最后发言的内容

    public String lastAccount;//最后发言的人的账号

    public String lastNickname;//最后发言的人的昵称

    public int hasPassWord;//是否有密码（0 没有 1有）

    public int maxCount;//最大加入人数
    public int currentSize;//当前人数

    public  List<String> headUrlList;

    public long  createTime;//房间被创建的时间

    public long getCreateTime()
    {
        if(createTime<10000000000L)
        {
            createTime=Long.valueOf(createTime+"000");
        }
        return createTime;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }





}
