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
import java.util.List;

public class PushMsgInfo extends BaseInfo implements Serializable {

    public static final long serialVersionUID = 1L;











    public String roomName;// 房间名称

    public String roomId;// 房间id

    public String roomType;// 房间type

    public String envelopeId;// 红包id


    public int chatType;// 消息内容的类型

    public int envelopeType;// 红包类型
    public int envelopePower;// 红包权限
    public int envelopeJoinSize;//红包参与人数

    public int envelopeFullResult;//红包结果是否是满员状态下产生的结果（1 是满员）

    public int envelopeCounts;//红包的个数
    public int envelopeMoneySize;//红包的总金额
    public int envelopeResult;//红包猜的结果
    public int envelopeStatus;//红包的状态(1 发起 2 正在猜 3结束)
    // public static final int ;
    // public String param;//状态
    // public String info;//状态
    public String userId;// 用户的token 机器码

    public String nickName = "";// 用户昵称

    public String nickname = "英雄不问出处";// 用户昵称

    public List<FreeAnswerInfo> freeAnswerList;//自由猜的答案列表

    public List<String> joinList;//参与抢红包的人员列表

    public int sex;// 用户性别（0女1男）

    public int age = 20;// 用户年龄

    public long createTime;

    public long createdAt;

    public String headurl;// 用户头像

    public int envelopeMoney = 5;// 参与红包需要的金币数

   // public int goldCoin = 0;// 打赏的金币数

    public int attentionMeCounts = 0;// 用户关注我的数量(我的粉丝数)

    public String commentContent = "太棒了";// 评论的内容

    public int attentionOthersCounts = 0;// 我关注的用户数量

    public int zanMeCounts = 0;// 用户点赞的数量

    public String signature = "清风徐来，白雪飘飘";// 用户签名

    public int type=0;// 消息类型

    // 消息类型（
    // 1、收藏提示（xxx 收藏了你的作品 《XXX》）
    // 2、关注提示（XXX在 两小时前关注了你）
    // 3、收藏的作品更新提示（《XXX》更新了新的内容）
    // 4、共享作品提交审核提示（XXX 写了你的作品《XXX》 请审核）
    // 5、点赞提示（XXX赞了你）
    // 6、评论提示（XXX评论了你的作品《XXX》）
    // 7、分享提示（XXX 分享了你的作品《XXX》）

    public String level = "1";// 用户等级

    public int loginTimes;// 登录次数

    public String account="1000";// 用户账号

    // 8、回复提示（XXX在《XXX》回复了你））

    public String content="";// 通知的内容


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

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
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



    public String getAccount() {
	return account;
    }

    public void setAccount(String account) {
	this.account = account;
    }


    public long getCreatedAt() {
        if(createdAt<10000000000L)
        {
            createdAt=Long.valueOf(createdAt+"000");
        }
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreateTime() {
        if(createTime<10000000000L)
        {
            createTime=Long.valueOf(createTime+"000");
        }
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
