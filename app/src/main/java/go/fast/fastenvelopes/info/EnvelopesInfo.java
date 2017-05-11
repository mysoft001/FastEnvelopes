package go.fast.fastenvelopes.info;

/**
 * 红包类型info
 */
public class EnvelopesInfo extends BaseInfo{

    public String envelopeName;// 红包类型名称

    public int envelopeType;// 红包type ( 0 猜金额红包 1 近者得红包 2 自由猜红包 )

    public int totalRooms;//已有房间数

    public int totalPeoples;//已有玩家数

}
