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

public class UserInfo extends BaseInfo implements Serializable {

    public static final long serialVersionUID = 1L;
    // public String param;//状态
    // public String info;//状态
    // public String userId;// 用户的token 机器码

    public String nickName = "英雄不问出处";// 用户昵称

    public String nickname = "英雄不问出处";// 用户昵称

    public int sex;// 用户性别（0女1男）

    public int age = 20;// 用户年龄

    public int isEnforce = 1;// 是否修改过密码(1 已经修改)

    public int attentionMeCounts = 0;// 用户关注我的数量(我的粉丝数)

    public int attentionOthersCounts = 0;// 我关注的用户数量

    public int zanMeCounts = 0;// 用户点赞的数量

    public String signature = "清风徐来，白雪飘飘";// 用户签名

    public String headurl;// 用户头像

    public String headurlBig;// 用户头像大图

    public String level = "1";// 用户等级

    public int loginTimes;// 登录次数

    public String account;// 用户账号

    public String phone;// 用户的手机号

    public String psd;// 用户的密码

    public String uid;// 用户的第三方账号Id

    public int signTimes;// 签到的次数

    public int signCoin;// 签到获得的总金币数
    
    public int  goldCoin;//账号的总金币数
    public int  lastCounts;//用户剩余可玩次数

    public int  envelopeCounts;//获得的红包总个数
    // public String nickname ;// 用户昵称

    // public List<LivingPageInfo> myWorks;//我的作品
    // public List<LivingPageInfo> myCollect;//我的收藏
    // public List<BookmarksInfo> bookmarksInfo;//我的书签
    // public String getNickname() {
    // return nickname;
    // }
    //
    // public void setNickname(String nickname) {
    // this.nickname = nickname;
    // }
    //
    // public String getUserId() {
    // return userId;
    // }
    //
    // public void setUserId(String userId) {
    // this.userId = userId;
    // }

    public String getNickName() {
	return nickName;
    }

    public void setNickName(String nickName) {

	this.nickName = nickName;
    }

    public String getSex() {
	return sex == 0 ? "女" : "男";
    }

    public void getShowSex(int sex) {
	this.sex = sex;
    }

    public void setSex(int sex) {
	this.sex = sex;
    }

    public String getAge() {
	return age + "";
    }

    // public String getShowAge() {
    // return age+"";
    // }

    public void setAge(int age) {
	this.age = age;
    }

    public String getLevel() {
	return level;
    }

    public void setLevel(String level) {
	this.level = level;
    }

    public String getSignature() {
	return signature;
    }

    public void setSignature(String signature) {
	this.signature = signature;
    }

    public String getHeadUrl() {
	return headurl;
    }

    public void setHeadUrl(String headUrl) {
	this.headurl = headUrl;
    }

    // public int getAttentionMeCounts() {
    // return attentionMeCounts;
    // }
    //
    // public void setAttentionMeCounts(int attentionMeCounts) {
    // this.attentionMeCounts = attentionMeCounts;
    // }
    //
    // public int getZanMeCounts() {
    // return zanMeCounts;
    // }
    //
    // public void setZanMeCounts(int zanMeCounts) {
    // this.zanMeCounts = zanMeCounts;
    // }

    public int getLoginTimes() {
	return loginTimes;
    }

    public void setLoginTimes(int loginTimes) {
	this.loginTimes = loginTimes;
    }

    public String getHeadurl() {
	return headurl;
    }

    public void setHeadurl(String headurl) {
	this.headurl = headurl;
    }

    public String getAccount() {
	return account;
    }

    public void setAccount(String account) {
	this.account = account;
    }

}
