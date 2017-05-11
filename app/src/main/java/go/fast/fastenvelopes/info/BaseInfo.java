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

public class BaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String status;// 状态
    private String result;// 请求结果
    private String code="";// 请求结果

    public String msg;// 请求结果
    private String messageInstruct;// 返回的信息

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getResult() {
	return result;
    }

    public void setResult(String result) {
	this.result = result;
    }

    public String getMessageInstruct() {
	return messageInstruct;
    }

    public void setMessageInstruct(String messageInstruct) {
	this.messageInstruct = messageInstruct;



    }
    public String getCode(){
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}