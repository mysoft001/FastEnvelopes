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
package go.fast.fastenvelopes.utils;


import go.fast.fastenvelopes.info.BaseInfo;

public class WXPayInfo extends BaseInfo {
    private static final long serialVersionUID = 1L;
    public String appid;// 
    public String partnerid;// 

    public String prepayid;// 
    public String extension="WXPay";// 
    public String noncestr;// 
    public String timestamp;// 
    
    public String result_code;//返回的码
    

    
    public String sign;// 
    public String orderId;// 服务器生成的订单号。用于后续确认支付用










    
}
