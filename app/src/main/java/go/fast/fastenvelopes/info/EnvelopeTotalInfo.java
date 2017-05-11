package go.fast.fastenvelopes.info;

/**
 * 红包类型info
 */
public class EnvelopeTotalInfo extends BaseInfo{

    public String envelopeName;// 红包类型名称

    public int envelopeType;// 红包type ( 0 猜金额红包 1 近者得红包 2 自由猜红包 )

    public EnvelopesInfo guess;//猜金额

    public EnvelopesInfo free;//自由猜

}
