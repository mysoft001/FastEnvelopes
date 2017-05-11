package go.fast.fastenvelopes.json;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import go.fast.fastenvelopes.activity.LoginActivity;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.EnvelopeTotalInfo;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.info.RewardInfo;
import go.fast.fastenvelopes.info.RoomItemInfo;
import go.fast.fastenvelopes.info.RoomListInfo;
import go.fast.fastenvelopes.info.SearchUserInfo;
import go.fast.fastenvelopes.info.UpgradeObj;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.FileUtils;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PayUtils;
import go.fast.fastenvelopes.utils.PreferenceUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.utils.WXPayInfo;

/**
 * Created by hanwei on 2017/1/6.
 */

public class HttpRequest {
    public static void upgradeApp(final Context context,
                                  final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.postAbsoluteUrl(Constant.NEW_ABSLOUT_BASE_URL
                        + Constant.UPDATE_APP, params,
                new BaseJsonHttpResponseHandler<UpgradeObj>(UpgradeObj.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, UpgradeObj response) {

                        response.content = response.content.replace("\r\n",
                                "\n");
                        System.out.println("  UPDATE_APP      "
                                + response.content);
                        if (context == null) {
                            return;
                        }

                        if (HttpResponseUtil.isResponseOk(response)) {

                            onRequestCallback.onSuccess(response);

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseBytes, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          UpgradeObj errorResponse) {

                        System.out.println("  UPDATE_APP    onFailure     "
                                + rawJsonData);
                        onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected UpgradeObj parseResponse(String rawJsonData,
                                                       boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }

    public static void reLogin(Context context) {
        if (context != null) {

            // 本地没有账号 提示用户登录
            if (PreferenceUtils.getInstance(context).getSettingUserAccount()
                    .equals("999"))

            {
                ToolUtils.showToast(context, "您尚未登录，请先登录吧~");
            } else {
                ToolUtils.showToast(context, "该账号已在其他设备登录，请重新登录");
            }

            Intent loginIntent = new Intent(context, LoginActivity.class);
            loginIntent.putExtra("relogin", true);
            context.startActivity(loginIntent);
        }

    }


    public static void thirdLoginGo(final Context context, UserInfo user,
                                    final int type, final onRequestCallback onRequestCallback,
                                    final Dialog pd) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("uid", user.uid);
        params.put("nickName", user.nickName);
        params.put("sex", user.sex);
        File cacheHeadFile = new File(FileUtils.getCacheHeadUrlPath());
        if (cacheHeadFile.exists()) {

            try {
                params.put("headurl", cacheHeadFile, "image/jpeg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        params.put("type", type);
        HttpRestClient.post(Constant.LOGIN, params,
                new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, UserInfo response) {

                        System.out.println("  onSuccess     " + type
                                + "         " + rawJsonResponse);
                        if (pd != null) {
                            pd.dismiss();
                        }

                        if(HttpResponseUtil.isResponseOk(response))
                        {
                            onRequestCallback.onSuccess(response);
                        }
                        else
                        {
                            if (response.getCode().equals("1999")) {
                                //JumpUtils.
                                reLogin(context);
                            }
                            else
                            {
                                ToolUtils.showToast(context,response.msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          UserInfo errorResponse) {
                        onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected UserInfo parseResponse(String rawJsonData,
                                                 boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }


    /**
     * 问题反馈
     */
    public static void userFeedback(final Context context, String feedback,
                                    final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        params.put("content", feedback);
        HttpRestClient.post(Constant.FEEDBACK, params,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {

                        if(HttpResponseUtil.isResponseOk(response))
                        {
                            onRequestCallback.onSuccess(response);
                        }
                        else
                        {
                            if (response.getCode().equals("1999")) {
                                //JumpUtils.
                                reLogin(context);
                            }
                            else
                            {
                                ToolUtils.showToast(context,response.msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }


    public static void payResultConfirm(final Context context,
                                        final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        params.put(
                "orderId",
                PreferenceUtil.getInstance(context).getString(
                        PayUtils.LASTPAYID, "null"));

        HttpRestClient.post(Constant.PAY_CONFORM_REQUEST, params,
                new BaseJsonHttpResponseHandler<WXPayInfo>(WXPayInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, WXPayInfo response) {

                        System.out.println("  onSuccess     " + "         "
                                + rawJsonResponse);
                        if (HttpResponseUtil.isResponseOk(response)) {

                            onRequestCallback.onSuccess(response);
                        }

                        else if (response.getStatus().equals("login"))// 在其他地方登录
                        {
                            // 有缓存账号不跳登录界面
                            if (ToolUtils.isNullOrEmpty(PreferenceUtils
                                    .getInstance(context)
                                    .getSettingUserAccount())) {
                                reLogin(context);
                            }
                        } else {
                            ToolUtils.showToast(context, "支付失败");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          WXPayInfo errorResponse) {
                        onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected WXPayInfo parseResponse(String rawJsonData,
                                                      boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }




    public static void prePay(final Context context, float money,
                              final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        // params.put("headurl", user.headurl);
        params.put("money", money);

        HttpRestClient.post(Constant.PREPAY_REQUEST, params,
                new BaseJsonHttpResponseHandler<WXPayInfo>(WXPayInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, WXPayInfo response) {

                        System.out.println("  onSuccess     " + "         "
                                + rawJsonResponse);
                        if (HttpResponseUtil.isResponseOk(response)) {

                            onRequestCallback.onSuccess(response);
                        }
                        else
                        {

                            if (response.getCode().equals("1999"))// 在其他地方登录
                            {
                                // 有缓存账号不跳登录界面

                                    reLogin(context);
                            } else {
                                ToolUtils.showToast(context, "支付失败");
                            }
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          WXPayInfo errorResponse) {

                        System.out.println("  onFailure     " + "         "
                                + rawJsonData);
                        onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected WXPayInfo parseResponse(String rawJsonData,
                                                      boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }

    /**
     * 奖励 打赏 签到奖励等操作
     *
     * @param context
     * @param type
     * @param onRequestCallback
     */

    public static void rewardSomething(final Context context, int type,
                                       final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        params.put("type", type);
        HttpRestClient.post(Constant.REWARD_USER, params,
                new BaseJsonHttpResponseHandler<RewardInfo>(RewardInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, RewardInfo response) {

                        System.out.println(" REWARD_USER     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }
                        if (onRequestCallback != null) {
                            if (response.getStatus().equals("enough")) {
                                ToolUtils.showToast(context, "金币奖励已打上限");
                                return;
                            }
                            onRequestCallback.onSuccess(response);

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          RewardInfo errorResponse) {
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected RewardInfo parseResponse(String rawJsonData,
                                                       boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }

    /**
     * 获取房间列表
     *
     * @param context
     * @param onRequestCallback
     */

    public static void getRoomList(final Context context,
                                       final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.ROOM_LIST, params,
                new BaseJsonHttpResponseHandler<RoomListInfo>(RoomListInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, RoomListInfo response) {

                        System.out.println(" RoomListInfo     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }

                            onRequestCallback.onSuccess(response);


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          RoomListInfo errorResponse) {
                        System.out.println(" RoomListInfo   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected RoomListInfo parseResponse(String rawJsonData,
                                                       boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }


    public static void getRoomListByType(final Context context,int type,
                                   final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("type", type);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.JOIN_HAVEN_ROOM, params,
                new BaseJsonHttpResponseHandler<RoomListInfo>(RoomListInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, RoomListInfo response) {

                        System.out.println(" RoomListInfo     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }

                        onRequestCallback.onSuccess(response);


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          RoomListInfo errorResponse) {
                        System.out.println(" RoomListInfo   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected RoomListInfo parseResponse(String rawJsonData,
                                                         boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }

    public static void getSearchRoomList(final Context context,String searchContent,
                                   final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);

        params.put("content", searchContent);

        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());

        HttpRestClient.post(Constant.SEARCH_ROOM_LIST, params,
                new BaseJsonHttpResponseHandler<RoomListInfo>(RoomListInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, RoomListInfo response) {

                        System.out.println(" RoomListInfo     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }

                        if (HttpResponseUtil.isResponseOk(response)) {

                            onRequestCallback.onSuccess(response);
                        }
                        else
                        {

                            if (response.getCode().equals("1999"))// 在其他地方登录
                            {
                                // 有缓存账号不跳登录界面

                                reLogin(context);
                            } else {
                                ToolUtils.showToast(context, response.msg);
                            }
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          RoomListInfo errorResponse) {
                        System.out.println(" RoomListInfo   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected RoomListInfo parseResponse(String rawJsonData,
                                                         boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }


    /**
     * 搜索用户列表
     * @param context
     * @param searchContent
     * @param onRequestCallback
     */
    public static void getSearchUserList(final Context context,String searchContent,
                                         final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);

        params.put("content", searchContent);

        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());

        HttpRestClient.post(Constant.SEARCH_USER_LIST, params,
                new BaseJsonHttpResponseHandler<SearchUserInfo>(SearchUserInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, SearchUserInfo response) {

                        System.out.println(" SEARCH_USER_LIST     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }

                        onRequestCallback.onSuccess(response);


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          SearchUserInfo errorResponse) {
                        System.out.println(" SEARCH_USER_LIST   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected SearchUserInfo parseResponse(String rawJsonData,
                                                         boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }



    /**
     * 获取房间列表
     *
     * @param context
     * @param onRequestCallback
     */

    public static void getUserHistoryRoomList(final Context context,String account,
                                   final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account",account);
        HttpRestClient.post(Constant.USER_ROOM_HISTORY_LIST, params,
                new BaseJsonHttpResponseHandler<RoomListInfo>(RoomListInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, RoomListInfo response) {

                        System.out.println(" RoomListInfo     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }

                        onRequestCallback.onSuccess(response);


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          RoomListInfo errorResponse) {
                        System.out.println(" RoomListInfo   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected RoomListInfo parseResponse(String rawJsonData,
                                                         boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }


    public static void rechargePlayCounts(final Context context,
                                    final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.RECHARGE_PLAYCOUNTS, params,
                new BaseJsonHttpResponseHandler<UserInfo>(UserInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, UserInfo response) {

                        System.out.println(" RECHARGE_PLAYCOUNTS     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }

                        onRequestCallback.onSuccess(response);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          UserInfo errorResponse) {
                        System.out.println(" RECHARGE_PLAYCOUNTS   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected UserInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }

    public static void checkRoomPsd(final Context context,String roomId,String password,
                                   final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("id", roomId);
        params.put("password", password);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.CHECK_ROOM_PSD, params,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {

                        System.out.println(" checkRoomPsd     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }

                        onRequestCallback.onSuccess(response);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        System.out.println(" checkRoomPsd   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                         boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }


    /**
     * 获取指定房间类型的房间数量和总人数
     * @param context
     * @param onRequestCallback
     */

    public static void getEnvelopeRoomSize(final Context context,
                                    final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.GET_ENVELOPE_TYPE_SIZE, params,
                new BaseJsonHttpResponseHandler<EnvelopeTotalInfo>(EnvelopeTotalInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, EnvelopeTotalInfo response) {

                        System.out.println(" GET_ENVELOPE_TYPE_SIZE     "
                                + rawJsonResponse);
                        if (context == null||response==null) {
                            return;
                        }

                        onRequestCallback.onSuccess(response);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          EnvelopeTotalInfo errorResponse) {
                        System.out.println(" GET_ENVELOPE_TYPE_SIZE   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected EnvelopeTotalInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }

    public static void In0rExitRoom(final Context context,String roomId,int type,//进入1 退出离开2
                                    final onRequestCallback onRequestCallback) {

        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("id", roomId);
        params.put("type", type);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.IN_OR_EXIT_ROOM, params,
                new BaseJsonHttpResponseHandler<RoomItemInfo>(RoomItemInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, RoomItemInfo response) {

                        System.out.println(" IN_OR_EXIT_ROOM     "
                                + rawJsonResponse);
                        if (context == null) {
                            return;
                        }

                        onRequestCallback.onSuccess(response);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          RoomItemInfo errorResponse) {
                        System.out.println(" IN_OR_EXIT_ROOM   onFailure     "
                                + rawJsonData);
                        if (onRequestCallback != null)
                            onRequestCallback.onFailure(rawJsonData);
                    }

                    @Override
                    protected RoomItemInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });
    }
    /**
     * 绑定个推的cid用于推送
     *
     * @param context
     * @param onRequestCallback
     */
    public static void bindClicentId(final Context context, String clientId,
                                     final onRequestCallback onRequestCallback) {

        if (!CommonUtils.isNetWorkConnected(context)) {
            return;
        }
        RequestParams params = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        params.put("access_token", uid);
        params.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        params.put("clientId", clientId);
        params.put("model", "android");
        // params.put("page", page);// 当前页
        HttpRestClient.post(Constant.BINDING_CLIENTID, params,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {

                        if (HttpResponseUtil.isResponseOk(response)) {

                            onRequestCallback.onSuccess(response);

                        }

                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {

                    }

                });

    }




    /**
     * 用户对其他用户关注
     *
     * @param context
     * @param onRequestCallback
     */
    public static void addUserAttention(final Context context,
                                        String attentionAccount, final onRequestCallback onRequestCallback) {

        if (!CommonUtils.isNetWorkConnected(context)) {
            ToolUtils.showToast(context, "网络出问题了，请检查后再试");
            return;
        }
        RequestParams requestParams = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        requestParams.put("access_token", uid);
        requestParams.put("user_account", attentionAccount);
        requestParams.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.ATTENTION_USER, requestParams,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {

                        if(HttpResponseUtil.isResponseOk(response))
                        {
                            onRequestCallback.onSuccess(response);
                        }
                        else
                        {
                            if (response.getCode().equals("1999")) {
                                //JumpUtils.
                                reLogin(context);
                            }
                            else
                            {
                                ToolUtils.showToast(context,response.msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {

                        System.out.println("   onFailure   "+rawJsonData);
                        onRequestCallback.onFailure(rawJsonData);
                        ToolUtils.showToast(context, "关注失败，请稍后再试");

                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });

    }

    /**
     * 用户取消对其他用户关注
     *
     * @param context
     * @param onRequestCallback
     */
    public static void cancelUserAttention(final Context context,
                                           String attentionAccount, final onRequestCallback onRequestCallback) {

        if (!CommonUtils.isNetWorkConnected(context)) {
            ToolUtils.showToast(context, "网络出问题了，请检查后再试");
            return;
        }
        RequestParams requestParams = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        requestParams.put("access_token", uid);
        requestParams.put("user_account", attentionAccount);
        requestParams.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.CANCLE_ATTENTION_USER, requestParams,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {
                        if(HttpResponseUtil.isResponseOk(response))
                        {
                            onRequestCallback.onSuccess(response);
                        }
                        else
                        {
                            if (response.getCode().equals("1999")) {
                                //JumpUtils.
                                reLogin(context);
                            }
                            else
                            {
                                ToolUtils.showToast(context,response.msg);
                            }
                        }

                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {

                        System.out.println("取消关注-onFailure----"+rawJsonData);
                        onRequestCallback.onFailure(rawJsonData);
                        ToolUtils.showToast(context, "取消关注失败，请稍后再试");
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });

    }

    public static void sendMsg(final Context context,String roomId,int messageType,
                                           String content, final onRequestCallback onRequestCallback) {

        if (!CommonUtils.isNetWorkConnected(context)) {
            ToolUtils.showToast(context, "网络出问题了，请检查后再试");
            return;
        }
        RequestParams requestParams = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        requestParams.put("access_token", uid);
        requestParams.put("content", content);
        requestParams.put("roomId", roomId);
        requestParams.put("type", messageType);
        requestParams.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.SEND_A_MESSAGE, requestParams,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {
                        if(HttpResponseUtil.isResponseOk(response))
                        {
                            onRequestCallback.onSuccess(response);
                        }
                        else
                        {
                            if (response.getCode().equals("1999")) {
                                //JumpUtils.
                                reLogin(context);
                            }
                            else
                            {
                                ToolUtils.showToast(context,response.msg);
                            }
                        }

                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        System.out.println("   发送自由猜失败---   "+rawJsonData+"      "+errorResponse );
                        onRequestCallback.onFailure(rawJsonData);
                        ToolUtils.showToast(context, "发送消息失败了");
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });

    }


    public static void sendPersonalLetter(final Context context,String userAccount,
                               String content, final onRequestCallback onRequestCallback) {

        if (!CommonUtils.isNetWorkConnected(context)) {
            ToolUtils.showToast(context, "网络出问题了，请检查后再试");
            return;
        }
        RequestParams requestParams = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        requestParams.put("access_token", uid);
        requestParams.put("content", content);
        requestParams.put("user_account", userAccount);
        requestParams.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.SEND_A_PERSONAL_LETTER, requestParams,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {
                        System.out.println("     SEND_A_PERSONAL_LETTER   "+rawJsonResponse);

                        if(context==null)
                        {
                            return;
                        }

                        if(HttpResponseUtil.isResponseOk(response))
                        {
                            onRequestCallback.onSuccess(response);
                        }
                        else
                        {
                            if (response.getCode().equals("1999")) {
                                //JumpUtils.
                              reLogin(context);
                            }
                            else
                            {
                                ToolUtils.showToast(context,response.msg);
                            }
                        }


                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        System.out.println("   onFailure---   "+rawJsonData+"      "+errorResponse );
                        onRequestCallback.onFailure(rawJsonData);
                        ToolUtils.showToast(context, "发送消息失败了");
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });

    }

    /**
     * 关注某个房间
     * @param context
     * @param type
     * 关注为 1
     *
     * 取消关注为0
     *
     *
     * @param roomId
     * @param onRequestCallback
     */

    public static void attentionRoom(final Context context,int type,
                                           String roomId, final onRequestCallback onRequestCallback) {

        if (!CommonUtils.isNetWorkConnected(context)) {
            ToolUtils.showToast(context, "网络出问题了，请检查后再试");
            return;
        }
        RequestParams requestParams = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        requestParams.put("access_token", uid);
        requestParams.put("type", type);
        requestParams.put("id", roomId);
        requestParams.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.ATTENTION_ROOM, requestParams,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {

                        onRequestCallback.onSuccess(response);

                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        onRequestCallback.onFailure(rawJsonData);
                        ToolUtils.showToast(context, "关注房间失败，请稍后再试");
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });

    }

    public static void joinEnvelope(final Context context, PushMsgInfo pushMsgInfo, final onRequestCallback onRequestCallback) {

        if (!CommonUtils.isNetWorkConnected(context)) {
            ToolUtils.showToast(context, "网络出问题了，请检查后再试");
            return;
        }
        System.out.println("   joinEnvelope     "+pushMsgInfo.envelopeId+"     "+pushMsgInfo.roomId+"    "+pushMsgInfo.envelopeMoney);
        RequestParams requestParams = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        requestParams.put("access_token", uid);
        requestParams.put("envelopeId", pushMsgInfo.envelopeId);
        requestParams.put("roomId", pushMsgInfo.roomId);
        requestParams.put("money", pushMsgInfo.envelopeMoney);
        requestParams.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.JOIN_ENVELOPE, requestParams,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {

                        System.out.println(" JOIN_ENVELOPE    "+rawJsonResponse);
                        onRequestCallback.onSuccess(response);

                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        onRequestCallback.onFailure(rawJsonData);
                        ToolUtils.showToast(context, "加入抢红包失败，请稍后再试");
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });

    }

    /**
     * 开始红包
     * @param context
     * @param pushMsgInfo
     * @param onRequestCallback
     */
    public static void goingEnvelope(final Context context, PushMsgInfo pushMsgInfo, final onRequestCallback onRequestCallback) {

        if (!CommonUtils.isNetWorkConnected(context)) {
            ToolUtils.showToast(context, "网络出问题了，请检查后再试");
            return;
        }
        RequestParams requestParams = new RequestParams();
        DeviceUuidFactory uuid = new DeviceUuidFactory(context);
        String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
        requestParams.put("access_token", uid);
        requestParams.put("envelopeId", pushMsgInfo.envelopeId);
        requestParams.put("roomId", pushMsgInfo.roomId);
        requestParams.put("money", pushMsgInfo.envelopeMoney);
        requestParams.put("account", PreferenceUtils.getInstance(context)
                .getSettingUserAccount());
        HttpRestClient.post(Constant.START_ENVELOPE, requestParams,
                new BaseJsonHttpResponseHandler<BaseInfo>(BaseInfo.class) {

                    @Override
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          String rawJsonResponse, BaseInfo response) {

                        System.out.println(" JOIN_ENVELOPE    "+rawJsonResponse);
                        onRequestCallback.onSuccess(response);

                    }

                    @Override
                    public void onFailure(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          Throwable throwable, String rawJsonData,
                                          BaseInfo errorResponse) {
                        onRequestCallback.onFailure(rawJsonData);
                        ToolUtils.showToast(context, "开始红包失败，请稍后再试");
                    }

                    @Override
                    protected BaseInfo parseResponse(String rawJsonData,
                                                     boolean isFailure) throws Throwable {
                        return null;
                    }
                });

    }


    public interface onRequestCallback {
        public void onSuccess(BaseInfo response);

        public void onFailure(String rawJsonData);
    }

    public interface AutoRequestCallback<T> {
        public void onSuccess(T response);

        public void onFailure(String rawJsonData);
    }

}
