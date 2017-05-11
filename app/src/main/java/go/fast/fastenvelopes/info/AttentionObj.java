/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package go.fast.fastenvelopes.info;

import java.io.Serializable;

public class AttentionObj extends UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public int myArticleCounts;// 创建的作品数量
    
    public boolean isAttention;//是否已经关注了

    public int getMyArticleCounts() {
	return myArticleCounts;
    }

    public void setMyArticleCounts(int myArticleCounts) {
	this.myArticleCounts = myArticleCounts;
    }

}
