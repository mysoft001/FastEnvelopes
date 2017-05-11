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

public class FeedbackItemObj extends UserInfo implements Serializable {

    public static final long serialVersionUID = 1L;
    
    public String questionId;
    public String content = "看了很多大家写的东西，真是太有才了~~~";// 反馈的内容

    public long publishTime = System.currentTimeMillis();// 发布的时间（发布时的时间戳）

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    
    
    
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public long getPublishTime() {
	if (publishTime < 10000000000L)// php时间戳是10位
	{
	    String newTime = String.valueOf(publishTime) + "000";

	    publishTime = Long.valueOf(newTime);

	}

	return publishTime;
    }

    public void setPublishTime(long publishTime) {
	this.publishTime = publishTime;
    }

}
