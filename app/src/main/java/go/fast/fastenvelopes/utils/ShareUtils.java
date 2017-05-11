package go.fast.fastenvelopes.utils;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.Map;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.info.RoomInfo;
import go.fast.fastenvelopes.info.RoomItemInfo;
import go.fast.fastenvelopes.info.UserInfo;

public class ShareUtils {





	public static void shareRoom(Context context,
								  RoomInfo roomInfo) {
		ShareAction shareAction = new ShareAction((Activity) context)
				.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
						SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
						SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
						SHARE_MEDIA.MORE)

				.withTargetUrl(
						"http://a.app.qq.com/o/simple.jsp?pkgname=go.fast.fastenvelopes")
				.setCallback(new UMShareCallbackListener(context));
		shareAction.withMedia(new UMImage(context, R.drawable.logo_512));
//
//		if(!PreferenceUtils.getInstance(context).getSettingUserAccount().equals(livingPageInfo.createAuchor))
//		{
//			HttpRequest.shareArticle(context, livingPageInfo.articleId, null);//通知服务器
//		}


		shareAction.withTitle( "一款让你上瘾的抢现金红包软件");
		shareAction.withText("我正在<快玩红包>进行抢红包游戏"
				+ "房间号为："+roomInfo.roomId+"房间名为：" + roomInfo.roomName + "快来和我一起抢红包吧" );

		shareAction.open();

	}
	public static void shareRoom(Context context,
								 RoomItemInfo roomInfo) {
		ShareAction shareAction = new ShareAction((Activity) context)
				.setDisplayList(SHARE_MEDIA.QQ,
						SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
						SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
						SHARE_MEDIA.MORE)

				.withTargetUrl(
						"http://a.app.qq.com/o/simple.jsp?pkgname=go.fast.fastenvelopes")
				.setCallback(new UMShareCallbackListener(context));
		shareAction.withMedia(new UMImage(context, R.drawable.logo_512));
		shareAction.withTitle( "一款让你上瘾的抢现金红包软件");
		shareAction.withText("我正在<快玩红包>进行抢红包游戏"
				+ "房间号为："+roomInfo.roomId+"房间名为：" + roomInfo.roomName + "快来和我一起抢红包吧" );

		shareAction.open();

	}

	public static void shareApp(Context context
								) {
		ShareAction shareAction = new ShareAction((Activity) context)
				.setDisplayList( SHARE_MEDIA.QQ,
						SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
						SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
						SHARE_MEDIA.MORE)

				.withTargetUrl(
						"http://a.app.qq.com/o/simple.jsp?pkgname=go.fast.fastenvelopes")
				.setCallback(new UMShareCallbackListener(context));
		shareAction.withMedia(new UMImage(context, R.drawable.logo_512));
//
//		if(!PreferenceUtils.getInstance(context).getSettingUserAccount().equals(livingPageInfo.createAuchor))
//		{
//			HttpRequest.shareArticle(context, livingPageInfo.articleId, null);//通知服务器
//		}


		shareAction.withTitle( "一款让你上瘾的抢现金红包软件");
		shareAction.withText("我正在<快玩红包>进行抢现金红包游戏。玩法好有趣~"
				+ "快来和我一起抢现金红包吧" );

		shareAction.open();

	}


    public static UserInfo getUserInfoByOther(final Context context,
	    final SHARE_MEDIA platform, final UserInfoListener userInfoListener) {

	UMShareAPI mShareAPI = UMShareAPI.get(context);

	mShareAPI.getPlatformInfo((Activity) context, platform,
		new UMAuthListener() {

		    @Override
		    public void onError(SHARE_MEDIA arg0, int arg1,
			    Throwable arg2) {
			if(userInfoListener!=null)
			{
			    userInfoListener.onError(arg0, arg1);
			}
		    }

		    @Override
		    public void onComplete(SHARE_MEDIA arg0, int arg1,
			    Map<String, String> arg2) {
				UserInfo user = new UserInfo();
			user.nickName = arg2.get("screen_name");
			user.headurl = arg2.get("profile_image_url");
				System.out.println("微信的 昵称-------"+user.nickName);
				System.out.println("微信的 头像------"+user.headurl );
			   System.out.println("登录方式-------"+platform);
			if (platform == SHARE_MEDIA.WEIXIN) {
			    System.out.println("微信的 uid-------"+arg2.get("unionid"));
				System.out.println("微信的 性别------"+arg2.get("gender"));

			    user.uid = arg2.get("unionid");
				if(arg2.get("gender").equals("男"))
				{
					user.sex=1;
				}
				else
				{
					user.sex=0;
				}
			  //  user.sex=Integer.valueOf(arg2.get("gender")) ;
			    
			}
			else if(platform == SHARE_MEDIA.QQ)
			{
			    user.uid = arg2.get("uid"); 
			    if(arg2.get("gender").equals("男"))
			    {
				user.sex=1;
			    }
			    else
			    {
				user.sex=0;
			    }
			    
			}
			
			if(userInfoListener!=null)
			{
			    userInfoListener.onComplete(arg0, arg1, user);
			}
			// user.
		    }

		    @Override
		    public void onCancel(SHARE_MEDIA arg0, int arg1) {
			System.out.println("   onCancel     " + arg0);
			if(userInfoListener!=null)
			{
			    userInfoListener.onCancel(arg0, arg1);
			}
		    }
		});

	return null;
    }

    public interface UserInfoListener {
	public void onComplete(SHARE_MEDIA arg0, int arg1, UserInfo user);

	public void onError(SHARE_MEDIA arg0, int arg1);
	public void onCancel(SHARE_MEDIA arg0, int arg1);
    }

}
