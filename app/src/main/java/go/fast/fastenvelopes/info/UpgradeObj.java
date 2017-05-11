package go.fast.fastenvelopes.info;


public class UpgradeObj  extends BaseInfo{
    public boolean isEnd;// 是否已经完结
    public String content;// 更新的内容
    public int version;// 服务器上最新的版本号  如果小于那么提示更新
    public int mustStartVersion;// 强制更新的开始版本
}
