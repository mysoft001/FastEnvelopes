package go.fast.fastenvelopes.info;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 获取详情里的推荐列表
 * @author hanwei
 *
 */

public class AttentionMeListInfo  extends BaseInfo  implements Serializable{
    
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public ArrayList<AttentionObj>  attentionMeList;//关注我的用户列表

    public ArrayList<AttentionObj> getAttentionMeList() {
        return attentionMeList;
    }

    public void setAttentionMeList(ArrayList<AttentionObj> attentionMeList) {
        this.attentionMeList = attentionMeList;
    }


    

}
