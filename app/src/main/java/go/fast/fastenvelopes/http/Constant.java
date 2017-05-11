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
package go.fast.fastenvelopes.http;


public class Constant {
	public static final String CACHE_DIR = "/fastEnvelope/";
	
	public static final String CACHE_LOCALCHAPTER_DIR = "/fastEnvelope/cache/";
	public static final String CACHE_DIR_IMAGE = "/fastEnvelope/image";
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String KEFU = "kefu";
	
	//http连接
	public static final String BASE_URL = "http://im.fujinde.com:600/zaina/";
	
	//--------------------------------------新的接口----------------------------------------------------------------------------
	
	
	
//	public static final String NEW_BASE_URL = "http://env.qmchuangzuo.com/v1/";
//	 public static final String NEW_ABSLOUT_BASE_URL = "http://env.qmchuangzuo.com/";

	public static final String NEW_BASE_URL = "http://envelope.qmchuangzuo.com/v1/";
	public static final String NEW_ABSLOUT_BASE_URL = "http://envelope.qmchuangzuo.com/";



	public static final String CREATE_ROOM="room/create";//创建红包房间

	public static final String CHANGE_ARTICLE="article/change";//修改已经创建作品接口
	public static final String CREATE_ARTICLE="article/add";//创建作品接口
	public static final String PUBLISH_TALKINGMSG="talk/add";//发布讨论话题接口
	public static final String GETTALKING_LIST="talk/list";//发布讨论评论接口

	public static final String ADDZAN_TALKING="talk/zan";//讨论列表点赞接口


	public static final String GETRANKING_LIST="article/ranking";//获取排行榜接口

	public static final String GETLIVING_LIST="article/living";//获取直播列表
	public static final String REFRUSH_LIVINGLIST="article/reload";//刷新直播列表接口
	public static final String RECOMMEND_PAGELIST="article/recommend";//获取推荐页面列表接口


	public static final String GETMYWORK_LIST="work/my";//获取我的作品列表

	public static final String ARTICLE_DETAIL="article/detail";//获取作品详情接口

	public static final String ARTICLE_RECOMMEND="article/hot";//作品详情页面推荐列表

	public static final String SEARCH_ARTICLES="article/search";//根据作品类型或者名称搜索作品
	public static final String GET_COLLECT_LIST="collection/list";//获取收藏列表


	public static final String GET_CREATEARTICLE_LIST="article/create-list";//获取创建的作品列表
	public static final String ARTICLE_COLLECT="collection/add";//收藏作品
	public static final String GETTALKING_COMMENT_LIST="talk/user-talk-list";//获取讨论item的评论列表

	public static final String GETARTICLE_COMMENT_LIST="comment/list";//获取作品的评论列表



	public static final String SEND_ARTICLE_REPLAY_COMMENT="comment/reply";//发布对某个人的评论
	public static final String SEND_ARTICLE_COMMENT="comment/add";//发布作品评论
	public static final String SEND_TALKING_COMMENT="talk/user-talk";//发布讨论列表评论

	public static final String SEND_ARTICLE_REPLAY_TALKING="talk/reply";//发布对讨论评论的回复
//	public static final String GETSHUOSHUO_MSG="detail";//获取说说接口
//	public static final String GETMOVING_MSG="detail";//获取说说接口
	//public static final String GET_USERINFO_URL = "info";//获取用户信息

	//public static final String AUTO_LOGIN = "autologin?";//自动登陆根据token获取用户资料
	public static final String SETTING_USERINFO = "user/update";//设置个人信息接口

	public static final String AUTO_REGISTER = "user/create";//自动注册接口
	public static final String GET_USERINFO = "user/info";//获取用户信息



	public static final String GETATTENTION_ME_LIST="user/attention-me";//获取关注我的人列表

	public static final String GETATTENTION_LIST="user/attention-others";//获取我关注的用户列表
	public static final String ZAN_USER="user/zan";//点赞某个用户
	public static final String ATTENTION_USER="user/attention";//关注某个用户
	public static final String CANCLE_ATTENTION_USER="user/cancel-attention";//关注某个用户
	public static final String ADDZAN_RANKING="article/zan";//直播排行作品的赞
	public static final String ADDLOW_RANKING="article/low";//直播排行作品的差评
	public static final String REMOVE_COLLECTIONLIST="collection/delete";//删除收藏列表
	public static final String REMOVE_BOOKMARKLIST="mark/delete";//删除书签列表

	public static final String GET_BOOKMARK_LIST="mark/list";//获取书签列表
	public static final String GET_BOOKMARK_POS="mark/position";//获取书签位置


	public static final String GET_SEARCHITEMS="article/keywords";//获取推荐的搜索请求列表

	public static final String ADD_BOOKMARKER="mark/add";//添加书签接口

	public static final String CHANGE_PSD="user/reset-password";//修改密码接口

	public static final String LOGIN="user/login";//登录接口


	public static final String THIRD_LOGIN="user/qq-wx-login";//第三方登录接口

	public static final String FEEDBACK="question/add";//问题反馈

	public static final String GET_FEEDBACKLIST="question/list";//获取问题反馈列表


	public static final String GETCATALOG_LIST="article/chapter-list";//获取目录列表

	public static final String BINDING_CLIENTID="user/client";//绑定用户参数

	public static final String DELETE_ARTICLE="article/delete";//绑定用户参数

	public static final String GET_BOOKSHELF="article/bookshelf";//获取书架的内容

	public static final String ADD_ARTICLEWATCH="article/watch";//添加最近阅读的作品

	public static final String ADD_READCOUNT="article/add-visit";//添加作品阅读量


	public static final String SEND_LIIVNG_COMMENT="comment/add-living";//发布作品直播的评论


	//public static final String SHUOSHUO_COMMENT="comment";//说说评论接口

	public static final String START_WRITING="article/writing";//进入写作界面通知服务器的接口

	public static final String UPDATE_WRITING="article/save-content";//进入写作界面通知服务器的接口

	public static final String GET_WRITING_CONTENT="article/show-content";//获取已有的作品的内容接口


	public static final String GET_CHAPTER_PUBLISHTIME="article/chapter-publish";//获取具体章节的最后更新的时间


	public static final String STOP_WRITING="article/submit";//进入写作界面通知服务器的接口

	public static final String STOP_PERSONAL_WRITING="article/personal-submit";//个人写作发布与释放接口

	//public static final String TOKEN_BY_NIKENAME = "search";//根据昵称获取token接口

	public static final String ADD_FRIEND = "friend";//添加好友接口

	public static final String GET_CHAPTER_CONTENT="chapter/content";//获取指定章节的内容

	public static final String ARTICLE_RANK_MORE="article/rank-more";//获取排行榜 指定模块的更多列表


	public static final String ARTICLE_RECOMMEND_MODE_MORE="article/more";//获取推荐模块 更多


	public static final String ARTICLE_RANK_NEWARTICLE="article/new-articles";//获取排行榜 最新作品模块

	public static final String ARTICLE_SORT_DETAILLIST="article/search-type";//获取分类类型的详细列表

	public static final String ARTICLE_CHECK="article/check";//作品审核接口


	public static final String UPDATE_APP="system/version";//提示更新版本

	public static final String CHAPTER_USER_ICON="article/chapter-user-info";//每个章节参与的作者列表

	public static final String ARTICLE_SHARED="article/share";//作品被分享

	public static final String SEE_ARTICLE_LIVING="article/enter-living";//作品被分享

	public static final String PREPAY_REQUEST="order/add";//预支付接口

	public static final String PAY_CONFORM_REQUEST="order/check";//支付返回后向服务器确认的接口


	public static final String REWARD_USER="trade/add";//打赏、激励接口

	public static final String REWARD_GLODEN_USER="trade/reward";//打赏接口


	public static final String GET_MY_LETTER_LIST="user/my-letters";//获取我的私信列表

	public static final String ADD_LETTER_MSG="user/add-letter";//添加一条私信

	public static final String USER_GETMONEY="trade/withdraw";//提现

	public static final String ROOM_LIST="room/list";//获取房间列表
	public static final String USER_ROOM_HISTORY_LIST="room/my-enter";//获取用户房间历史记录


	public static final String CHECK_ROOM_PSD="room/validate-pass";//验证房间密码
	public static final String IN_OR_EXIT_ROOM="room/enter";//进入或者退出房间调的接口

	public static final String SEND_A_MESSAGE="chat/add";//发送消息接口

	public static final String ATTENTION_ROOM="room/attention";//关注房间

	public static final String CREATE_ENVELOPE="packet/add";//创建红包

	public static final String JOIN_ENVELOPE="packet/join";//加入红包

	public static final String  START_ENVELOPE="packet/start";//开始红包游戏
	public static final String  GET_MSG_LIST="chat/list";//获取消息类型

	public static final String CREATE_FREE_ENVELOPE="packet/free-add";//创建自由猜红包

	public static final String SEND_A_PERSONAL_LETTER="message/send";//发送私信

	public static final String SEARCH_ROOM_LIST="room/search";//获取搜索房间列表

	public static final String SEARCH_USER_LIST="user/search";//获取搜索用户列表
	public static final String JOIN_HAVEN_ROOM="room/join";//加入已有的指定类型的房间列表

	public static final String GET_ENVELOPE_TYPE_SIZE="room/total";//获取不同类型红包类型总的房间数 人数

	public static final String RECHARGE_PLAYCOUNTS="user/buy-counts";//充值可以玩的次数

	//---------------------------------------------------------------------------------------------------------------




}
