package go.fast.fastenvelopes.info;

import java.util.ArrayList;
import java.util.List;

/**
 * 房间信息
 */
public class RoomItemInfo extends BaseInfo{




   // public String roomName;// 房间名称

public String roomName;// 房间名称

    public int roomType;// 房间类型（红包的玩法）

    public String roomId;//房间的id


    public String roomCreatedAccount;//创建者的账号

    public String lastContent;//最后发言的内容

    public String lastAccount;//最后发言的人的账号

    public String lastNickname;//最后发言的人的昵称

    public int roomIsPass;//是否有密码（0 没有 1有）

    public int roomCounts;//加入人数

    public int roomMaxCounts;//最大加入人数

    public int isAttention;//是否已经关注 (1已经关注)

    public String currentCreateEnvelopeAccount;//当前发布红包的人的账号
    public String currentCreateEnvelopeNickname;//当前发布红包的人的昵称

    public int isEnvelopeing;//是否有红包正在进行(1 有 0 没有)


    public  List<UserInfo> members;//成员列表

   // public long  createTime;//房间被创建的时间
   // public long  createdAt;//房间被创建的时间

public long  roomCreatedAt;//房间被创建的时间

    public long getRoomCreatedAt()
    {
        if(roomCreatedAt<10000000000L)
        {
            roomCreatedAt=Long.valueOf(roomCreatedAt+"000");
        }
        return roomCreatedAt;
    }

    public List<String> getMemberHeadurl()
    {
        List<String> headList=new ArrayList<>();
        if(members!=null)
        {
for(int i=0;i<members.size();i++)
{
    headList.add(members.get(i).headurl);
}
        }
            return headList;
    }

    public void setRoomCreatedAt(long roomCreatedAt)
    {
        this.roomCreatedAt = roomCreatedAt;
    }





}
