package go.fast.fastenvelopes.info;


public class MessageInfo extends UserInfo{

    public String content;// 聊天的内容（与answer二者只有其中一个有值）

    public double answer;//用户本次给出的答案

    public int envelopeType;//红包类型
    public int envelopeMoneySize;//红包大小

    public double goldSize;//获得的红包大小（自由猜 每人获得的结果值）

    public int envelopeResult;//与结果的比较值（0 猜中与结果相，1 比结果大，-1 比结果小）

    public int chatType;// 聊天元素类型（0 普通对话，1 猜红包对话，2 抢红包结果对话 3 发起红包 ）

    public String roomId;// 房间的id
    //public long  publishTime;//该聊天发布的时间

    public long  createdAt;//该聊天发布的时间


    //-------------------------------------------界面需要-----------------------------------------------------------

    public boolean isNeedShowTime;//是否需要显示发送的时间

    public boolean isPreLoading;//是否是预加载信息

    public long getCreatedAt() {
        if(createdAt<10000000000L)
        {
            createdAt=Long.valueOf(createdAt+"000");
        }
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getAnswer()
    {
        return answer+"";
    }
    public void setAnswer(double answer)
    {
        this.answer=answer;
    }



}
