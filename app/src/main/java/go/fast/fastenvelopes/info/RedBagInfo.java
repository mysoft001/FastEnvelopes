package go.fast.fastenvelopes.info;

import java.util.ArrayList;

/**
 * 红包信息表
 */
public class RedBagInfo extends BaseInfo{

    public String envelopeName;// 红包类型名称

    public String envelopeId;// 红包Id

    public int envelopeType;// 红包type（1猜金额红包 2 自由猜红包 3 近者得红包）

    public int permission;//红包权限（指定谁可以参与抢红包游戏，猜金额红包需要指定0 参与者可以猜 1所有人可以抢）

    public String roomId;//发红包的房间id

    public int playerCount;//参与的玩家数

    public ArrayList<String> palyerList;//玩家列表

    public ArrayList<AnswerInfo> answerList;//回答列表

}
