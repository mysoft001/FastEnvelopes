package go.fast.fastenvelopes.info;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 获取详情里的推荐列表
 * @author hanwei
 *
 */

public class AttentionListInfo  extends BaseInfo  implements Serializable{
    
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public ArrayList<AttentionObj>  attentionOthersList;//我关注的用户列表

    public ArrayList<AttentionObj> getAttentionOthersList() {
        return attentionOthersList;
    }

    public void setAttentionOthersList(ArrayList<AttentionObj> attentionOthersList) {
        this.attentionOthersList = attentionOthersList;
    }

    

}
