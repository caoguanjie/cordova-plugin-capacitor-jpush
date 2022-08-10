package cn.jiguang.cordova.push;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.mode.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

import cn.fits.plugins.jpush.FitsPushPlugin;
import cn.jpush.android.service.OPushCallback;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.data.JPushLocalNotification;

public class JPushPlugin extends CordovaPlugin {

    private static final String TAG = JPushPlugin.class.getSimpleName();

    private Context mContext;

    private static JPushPlugin instance;
    private static Activity cordovaActivity;

    static String notificationTitle;
    static String notificationAlert;
    static Map<String, Object> notificationExtras = new HashMap<String, Object>();

    static String openNotificationTitle;
    static String openNotificationAlert;
    static Map<String, Object> openNotificationExtras = new HashMap<String, Object>();

    static Map<Integer, CallbackContext> eventCallbackMap = new HashMap<Integer, CallbackContext>();

    String vendorID = "";

    public JPushPlugin() {
        instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mContext = cordova.getActivity().getApplicationContext();
        Log.d("HJT", "initialize plugin" + FitsPushPlugin.jpushIntent.toString());// 离线拿到intent

        // JPushInterface.init(mContext);

        cordovaActivity = cordova.getActivity();

        // 用于APP离线时获取厂商通知栏消息数据
        if (FitsPushPlugin.extras != null && !FitsPushPlugin.extras.isEmpty()) {
            JPushPlugin.openNotificationTitle = FitsPushPlugin.title;
            JPushPlugin.openNotificationAlert = FitsPushPlugin.alert;
            JPushPlugin.openNotificationExtras = getNotificationExtras(FitsPushPlugin.jpushIntent);
        }

        // 如果同时缓存了打开事件 openNotificationAlert 和 消息事件 notificationAlert，只向 UI 发打开事件。
        // 这样做是为了和 iOS 统一。
        if (openNotificationAlert != null) {
            notificationAlert = null;
            transmitNotificationOpen(openNotificationTitle, openNotificationAlert, openNotificationExtras);
        }
        if (notificationAlert != null) {
            transmitNotificationReceive(notificationTitle, notificationAlert, notificationExtras);
        }
    }

    public void onResume(boolean multitasking) {
        if (openNotificationAlert != null) {
            notificationAlert = null;
            transmitNotificationOpen(openNotificationTitle, openNotificationAlert, openNotificationExtras);
        }
        if (notificationAlert != null) {
            transmitNotificationReceive(notificationTitle, notificationAlert, notificationExtras);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 监听接收到的意图，用于app运行时获取通知栏消息
        if (intent != null) {
            JPushPlugin.openNotificationTitle = "title";
            JPushPlugin.openNotificationAlert = "alert";
            JPushPlugin.openNotificationExtras = getNotificationExtras(intent);
            Log.d("HJT", "意图改变" + getNotificationExtras(intent).toString());
        }
    }

    // 获取通知栏消息额外参数方法转换指定数据结构
    public static Map getNotificationExtras(Intent intent) {

        Map extrasMap = new HashMap<String, Object>();
        try {
            if (Build.MANUFACTURER.toUpperCase().equals("XIAOMI")) {
                MiPushMessage message = (MiPushMessage) intent.getSerializableExtra(PushMessageHelper.KEY_MESSAGE);
                if (message != null) {
                    extrasMap = message.getExtra();
                } else {
                    Log.d("HJT", "后台重新打开收到的intent");
                }
            } else {
                for (String key : intent.getExtras().keySet()) {
                    Log.d("HJT", key);
                    extrasMap.put(key, intent.getStringExtra(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extrasMap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cordovaActivity = null;
        instance = null;
    }

    private static JSONObject getMessageObject(String message, Map<String, Object> extras) {
        JSONObject data = new JSONObject();
        try {
            data.put("message", message);
            JSONObject jExtras = new JSONObject();
            for (Entry<String, Object> entry : extras.entrySet()) {
                if (entry.getKey().equals("cn.jpush.android.EXTRA")) {
                    JSONObject jo;
                    if (TextUtils.isEmpty((String) entry.getValue())) {
                        jo = new JSONObject();
                    } else {
                        jo = new JSONObject((String) entry.getValue());
                        String key;
                        Iterator keys = jo.keys();
                        while (keys.hasNext()) {
                            key = keys.next().toString();
                            jExtras.put(key, jo.getString(key));
                        }
                    }
                    jExtras.put("cn.jpush.android.EXTRA", jo);
                } else {
                    jExtras.put(entry.getKey(), entry.getValue());
                }
            }
            if (jExtras.length() > 0) {
                data.put("extras", jExtras);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static JSONObject getNotificationObject(String title, String alert, Map<String, Object> extras) {
        JSONObject data = new JSONObject();
        try {
            data.put("title", title);
            data.put("alert", alert);
            JSONObject jExtras = new JSONObject();
            for (Entry<String, Object> entry : extras.entrySet()) {
                if (entry.getKey().equals("cn.jpush.android.EXTRA")) {
                    JSONObject jo;
                    if (TextUtils.isEmpty((String) entry.getValue())) {
                        jo = new JSONObject();
                    } else {
                        jo = new JSONObject((String) entry.getValue());
                        String key;
                        Iterator keys = jo.keys();
                        while (keys.hasNext()) {
                            key = keys.next().toString();
                            jExtras.put(key, jo.getString(key));
                        }
                    }
                    jExtras.put("cn.jpush.android.EXTRA", jo);
                } else {
                    jExtras.put(entry.getKey(), entry.getValue());
                }
            }
            if (jExtras.length() > 0) {
                data.put("extras", jExtras);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    static void transmitMessageReceive(String message, Map<String, Object> extras) {
        if (instance == null) {
            return;
        }
        JSONObject data = getMessageObject(message, extras);
        String format = "window.plugins.jPushPlugin.receiveMessageInAndroidCallback(%s);";
        final String js = String.format(format, data.toString());
        cordovaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
    }

    static void transmitNotificationOpen(String title, String alert, Map<String, Object> extras) {
        if (instance == null) {
            return;
        }
        JSONObject data = getNotificationObject(title, alert, extras);
        String format = "window.plugins.jPushPlugin.openNotificationInAndroidCallback(%s);";
        final String js = String.format(format, data.toString());
        cordovaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
        JPushPlugin.openNotificationTitle = null;
        JPushPlugin.openNotificationAlert = null;
    }

    static void transmitNotificationReceive(String title, String alert, Map<String, Object> extras) {
        if (instance == null) {
            return;
        }
        JSONObject data = getNotificationObject(title, alert, extras);
        String format = "window.plugins.jPushPlugin.receiveNotificationInAndroidCallback(%s);";
        final String js = String.format(format, data.toString());
        cordovaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
        JPushPlugin.notificationTitle = null;
        JPushPlugin.notificationAlert = null;
    }

    static void transmitReceiveRegistrationId(String rId) {
        if (instance == null) {
            return;
        }
        JSONObject data = new JSONObject();
        try {
            data.put("registrationId", rId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String format = "window.plugins.jPushPlugin.receiveRegistrationIdInAndroidCallback(%s);";
        final String js = String.format(format, data.toString());
        cordovaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
    }

    @Override
    public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext)
            throws JSONException {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Method method = JPushPlugin.class.getDeclaredMethod(action, JSONArray.class, CallbackContext.class);
                    method.invoke(JPushPlugin.this, data, callbackContext);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        return true;
    }

    void init(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.init(mContext);
    }

    void setDebugMode(JSONArray data, CallbackContext callbackContext) {
        boolean mode;
        try {
            mode = data.getBoolean(0);
            JPushInterface.setDebugMode(mode);
            JLogger.setLoggerEnable(mode);
            callbackContext.success();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void stopPush(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.stopPush(mContext);
        callbackContext.success();
    }

    void resumePush(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.resumePush(mContext);
        callbackContext.success();
    }

    void isPushStopped(JSONArray data, CallbackContext callbackContext) {
        boolean isStopped = JPushInterface.isPushStopped(mContext);
        if (isStopped) {
            callbackContext.success(1);
        } else {
            callbackContext.success(0);
        }
    }

    void areNotificationEnabled(JSONArray data, final CallbackContext callback) {
        int isEnabled;
        if (hasPermission("OP_POST_NOTIFICATION")) {
            isEnabled = 1;
        } else {
            isEnabled = 0;
        }
        callback.success(isEnabled);
    }

    void setLatestNotificationNum(JSONArray data, CallbackContext callbackContext) {
        int num = -1;
        try {
            num = data.getInt(0);
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("error reading num json");
            return;
        }
        if (num != -1) {
            JPushInterface.setLatestNotificationNumber(mContext, num);
        } else {
            callbackContext.error("error num");
        }
    }

    void setPushTime(JSONArray data, CallbackContext callbackContext) {
        Set<Integer> days = new HashSet<Integer>();
        JSONArray dayArray;
        int startHour = -1;
        int endHour = -1;
        try {
            dayArray = data.getJSONArray(0);
            for (int i = 0; i < dayArray.length(); i++) {
                days.add(dayArray.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("error reading days json");
        }
        try {
            startHour = data.getInt(1);
            endHour = data.getInt(2);
        } catch (JSONException e) {
            callbackContext.error("error reading hour json");
        }
        Context context = mContext;
        JPushInterface.setPushTime(context, days, startHour, endHour);
        callbackContext.success();
    }

    void getRegistrationID(JSONArray data, CallbackContext callbackContext) {
        Context context = mContext;
        String regID = JPushInterface.getRegistrationID(context);
        callbackContext.success(regID);
    }

    // 获取厂商类型
    void getVendorType(JSONArray data, CallbackContext callbackContext) {
        callbackContext.success(Build.MANUFACTURER);
    }

    // 获取厂商别名
    void getVendorAlias(JSONArray data, CallbackContext callbackContext) {
        List<String> aliasList = new ArrayList<>();
        switch (Build.MANUFACTURER.toUpperCase()) {
            case "OPPO":
                // ApplicationInfo info = null;
                // try {
                // info =
                // mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
                // PackageManager.GET_META_DATA);
                // } catch (PackageManager.NameNotFoundException e) {
                // e.printStackTrace();
                // }
                // String OPPO_APPID = info.metaData.getString("OPPO_APPID");
                // String OPPO_APPKEY = info.metaData.getString("OPPO_APPKEY");
                // Log.d("TAG", "欧派OPPO_APPKEY=" + OPPO_APPKEY);
                // Log.d("TAG", "欧派OPPO_APPID=" + OPPO_APPID);
                // HeytapPushManager.register(mContext, OPPO_APPID,
                // OPPO_APPKEY,new OPushCallback() {
                // @Override
                // public void onRegister(int i, String s) {
                // super.onRegister(i, s);
                // if (i == ErrorCode.SUCCESS) {
                // // vendorID = s;
                // Log.d("TAG", "欧派id=" + s);
                // // 註冊成功
                // Log.e("NPL", "註冊成功，registerId=" + s);
                // } else {
                // // 註冊失敗
                // Log.e("NPL", "註冊失敗");
                // }
                // }
                // });
                // try {
                // Thread.sleep(1000L);
                // } catch (InterruptedException e) {
                // e.printStackTrace();
                // }
                HeytapPushManager.getRegister();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                aliasList.add(HeytapPushManager.getRegisterID());
                Log.d("HJT", "欧派别名=" + HeytapPushManager.getRegisterID());
                break;
            case "VIVO":
                aliasList.add(PushClient.getInstance(mContext).getAlias());
                Log.d("HJT", "维沃别名=" + PushClient.getInstance(mContext).getAlias());
                break;
            case "XIAOMI":
                aliasList = MiPushClient.getAllAlias(mContext);
                Log.d("HJT", "小米别名数组=" + MiPushClient.getAllAlias(mContext).toString());
                break;
            case "HUAWEI":
                aliasList.add(HmsInstanceId.getInstance(mContext).getToken());
                Log.d("HJT", "华为别名token=" + HmsInstanceId.getInstance(mContext).getToken());
                break;
        }
        callbackContext.success(String.valueOf(aliasList));
    }

    // 获取厂商推送注册ID
    void getVendorID(JSONArray data, CallbackContext callbackContext) {
        Log.d("TAG", "当前MANUFACTURER厂商" + Build.MANUFACTURER);
        switch (Build.MANUFACTURER.toUpperCase()) {
            case "OPPO":
                try {
                    // 获取manifest中欧派厂商ID和key值配置
                    // ApplicationInfo info =
                    // mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
                    // PackageManager.GET_META_DATA);
                    // String OPPO_APPID = info.metaData.getString("OPPO_APPID");
                    // String OPPO_APPKEY = info.metaData.getString("OPPO_APPKEY");
                    // Log.d("TAG", "欧派OPPO_APPKEY=" + OPPO_APPKEY);
                    // Log.d("TAG", "欧派OPPO_APPID=" + OPPO_APPID);
                    // HeytapPushManager.register(mContext, OPPO_APPID,
                    // OPPO_APPKEY, new OPushCallback() {
                    // @Override
                    // public void onRegister(int i, String s) {
                    // super.onRegister(i, s);
                    // if (i == ErrorCode.SUCCESS) {
                    // // vendorID = s;
                    // Log.d("TAG", "欧派id=" + s);
                    // // 註冊成功
                    // Log.e("NPL", "註冊成功，registerId=" + s);
                    // } else {
                    // // 註冊失敗
                    // Log.e("NPL", "註冊失敗");
                    // }
                    // }
                    // });
                    HeytapPushManager.getRegister();
                    Thread.sleep(1000L);
                    vendorID = HeytapPushManager.getRegisterID();
                    Log.d("TAG", "oppo=" + vendorID);
                    // } catch (PackageManager.NameNotFoundException e) {
                    // e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "VIVO":
                vendorID = PushClient.getInstance(mContext).getRegId();
                Log.d("TAG", "维沃id=" + PushClient.getInstance(mContext).getRegId());
                break;
            case "XIAOMI":
                vendorID = MiPushClient.getRegId(mContext);
                Log.d("Tag", "当前小米id=" + MiPushClient.getRegId(mContext));
                break;
            case "HUAWEI":
                vendorID = HmsInstanceId.getInstance(mContext).getToken();
                Log.d("Tag", "当前华为or荣耀id=" + HmsInstanceId.getInstance(mContext).getToken());
                break;
        }
        callbackContext.success(vendorID);
    }

    void setVendorAlias(JSONArray data, CallbackContext callbackContext) {
        // 设置厂商别名
        Log.d("HJT", "前端参数传入" + data.toString());
        try {
            JSONObject jsonObject = data.getJSONObject(0);
            String alias = jsonObject.getString("alias");
            Boolean isSuccess = false;
            // 设置厂商别名
            switch (Build.MANUFACTURER.toUpperCase()) {
                case "OPPO":
                    // 不存在别名用于测试，oppo提供方法能直接设置id
                    HeytapPushManager.setRegisterID("OPPO_CN_fb75492f0274175b84f2c0017819a123");
                    // HeytapPushManager.register();
                    if (HeytapPushManager.getRegisterID().equals(alias)) {
                        isSuccess = true;
                    }
                    break;
                case "VIVO":
                    /**
                     * alias：别名；
                     * listener: 状态监听。30001 设置别名失败：请打开push开关；30002 设置别名失败：订阅别名为空；30003
                     * 设置别名失败：别名设置超长，字符长度超过70；
                     *
                     * 补充：绑定别名，同一个别名仅能绑定一个regId。当regId已绑定了别名A，若调用此接口绑定别名B，则与别名A的绑定关系会自动解除。
                     * 此接口与 unBindAlias 一天内最多调用100次，两次调用的间隔需大于2s。
                     *
                     */
                    PushClient.getInstance(mContext).bindAlias(alias, new IPushActionListener() {
                        @Override
                        public void onStateChanged(int code) {
                            switch (code) {
                                case 30001:
                                    callbackContext.error("设置别名失败：请打开push开关；");
                                    break;
                                case 30002:
                                    callbackContext.error("设置别名失败：订阅别名为空；");
                                    break;
                                case 30003:
                                    callbackContext.error("设置别名失败：别名设置超长，字符长度超过70；");
                                    break;
                                case 0:
                                    isSuccess = true;
                                    break;
                            }
                        }
                    });
                    if (PushClient.getInstance(mContext).getRegId().equals(alias)) {
                        isSuccess = true;
                    }
                    break;
                case "XIAOMI":
                    /**
                     * context Android平台上app的上下文，建议传入当前app的application context
                     * alias 为指定用户设置别名
                     * category 扩展参数，暂时没有用途，直接填null
                     */
                    Log.d("HJT", "小米json：" + alias);
                    MiPushClient.setAlias(mContext, alias, null);
                    Thread.sleep(1000L);
                    Log.d("HJT", "设置别名后：" + MiPushClient.getAllAlias(mContext));
                    if (MiPushClient.getAllAlias(mContext).contains(alias)) {
                        isSuccess = true;
                    }
                    break;
                case "HUAWEI":
                    Log.d("HJT", "华为无法设置别名，跳过");
                    break;
            }

            callbackContext.success(String.valueOf(isSuccess));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void delVendorAlias(JSONArray data, CallbackContext callbackContext) {
        // 删除厂商别名
        try {
            JSONObject jsonObject = data.getJSONObject(0);
            String alias = jsonObject.getString("alias");
            Boolean isSuccess = false;
            // 删除别名
            switch (Build.MANUFACTURER.toUpperCase()) {
                case "OPPO":
                    // 不存在别名用于测试，oppo提供方法能直接设置id
                    // HeytapPushManager.setRegisterID("null");
                    HeytapPushManager.unRegister();
                    isSuccess = true;
                    break;
                case "VIVO":
                    /**
                     * alias：别名；
                     * listener: 状态监听。30001 设置别名失败：请打开push开关；30002 设置别名失败：订阅别名为空；30003
                     * 设置别名失败：别名设置超长，字符长度超过70；
                     *
                     * 补充：绑定别名，同一个别名仅能绑定一个regId。当regId已绑定了别名A，若调用此接口绑定别名B，则与别名A的绑定关系会自动解除。
                     * 此接口与 unBindAlias 一天内最多调用100次，两次调用的间隔需大于2s。
                     *
                     */
                    PushClient.getInstance(mContext).unBindAlias(alias, new IPushActionListener() {
                        @Override
                        public void onStateChanged(int code) {
                            switch (code) {
                                case 30001:
                                    callbackContext.error("删除别名失败：请打开push开关；");
                                    break;
                                case 30002:
                                    callbackContext.error("删除别名失败：订阅别名为空；");
                                    break;
                                case 30003:
                                    callbackContext.error("删除别名失败：别名设置超长，字符长度超过70；");
                                    break;
                                case 0:
                                    isSuccess = true;
                                    break;
                            }
                        }
                    });
                    break;
                case "XIAOMI":
                    /**
                     * context Android平台上app的上下文，建议传入当前app的application context
                     * alias 为指定用户设置别名
                     * category 扩展参数，暂时没有用途，直接填null
                     */
                    Log.d("HJT", "小米取消别名：" + alias);
                    MiPushClient.unsetAlias(mContext, alias, null);
                    Thread.sleep(1000L);
                    if (!MiPushClient.getAllAlias(mContext).contains(alias)) {
                        isSuccess = true;
                    }
                    Log.d("HJT", "小米取消之后的所有别名：" + MiPushClient.getAllAlias(mContext));
                    break;
                case "HUAWEI":
                    isSuccess = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(
                                        mContext.getPackageName(),
                                        PackageManager.GET_META_DATA);
                                String HuaWeiSubjectID = info.metaData.getString("com.huawei.hms.client.appid");
                                HmsInstanceId.getInstance(mContext).deleteToken(HuaWeiSubjectID, "HCM");

                            } catch (PackageManager.NameNotFoundException | ApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
            }
            callbackContext.success(String.valueOf(isSuccess));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void onResume(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.onResume(this.cordova.getActivity());
    }

    void onPause(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.onPause(this.cordova.getActivity());
    }

    void reportNotificationOpened(JSONArray data, CallbackContext callbackContext) {
        try {
            String msgID;
            msgID = data.getString(0);
            JPushInterface.reportNotificationOpened(this.cordova.getActivity(), msgID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setAlias(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;
        String alias = null;

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");
            alias = params.getString("alias");
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.setAlias(mContext, sequence, alias);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void deleteAlias(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.deleteAlias(mContext, sequence);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void getAlias(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.getAlias(mContext, sequence);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void setTags(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;
        Set<String> tags = new HashSet<String>();

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");

            JSONArray tagsArr = params.getJSONArray("tags");
            for (int i = 0; i < tagsArr.length(); i++) {
                tags.add(tagsArr.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.setTags(mContext, sequence, tags);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void addTags(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;
        Set<String> tags = new HashSet<String>();

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");

            JSONArray tagsArr = params.getJSONArray("tags");
            for (int i = 0; i < tagsArr.length(); i++) {
                tags.add(tagsArr.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.addTags(mContext, sequence, tags);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void deleteTags(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;
        Set<String> tags = new HashSet<String>();

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");

            JSONArray tagsArr = params.getJSONArray("tags");
            for (int i = 0; i < tagsArr.length(); i++) {
                tags.add(tagsArr.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.deleteTags(mContext, sequence, tags);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void cleanTags(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.cleanTags(mContext, sequence);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void getAllTags(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.getAllTags(mContext, sequence);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void checkTagBindState(JSONArray data, CallbackContext callbackContext) {
        int sequence = -1;
        String tag = null;

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");
            tag = params.getString("tag");

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        JPushInterface.checkTagBindState(mContext, sequence, tag);
        eventCallbackMap.put(sequence, callbackContext);
    }

    void getConnectionState(JSONArray data, CallbackContext callback) {
        boolean isConnected = JPushInterface.getConnectionState(cordovaActivity.getApplicationContext());
        if (isConnected) {
            callback.success(1);
        } else {
            callback.success(0);
        }
    }

    /**
     * 自定义通知行为，声音、震动、呼吸灯等。
     */
    void setBasicPushNotificationBuilder(JSONArray data, CallbackContext callbackContext) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this.cordova.getActivity());
        builder.developerArg0 = "Basic builder 1";
        JPushInterface.setPushNotificationBuilder(1, builder);
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义推送通知栏样式，需要自己实现具体代码。 http://docs.jiguang.cn/client/android_tutorials/#_11
     */
    void setCustomPushNotificationBuilder(JSONArray data, CallbackContext callbackContext) {
        // CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(
        // this.cordova.getActivity(), R.layout.test_notification_layout,
        // R.id.icon, R.id.title, R.id.text);
        // JPushInterface.setPushNotificationBuilder(2, builder);
        // JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

    void clearAllNotification(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.clearAllNotifications(this.cordova.getActivity());
    }

    void clearNotificationById(JSONArray data, CallbackContext callbackContext) {
        int notificationId = -1;
        try {
            notificationId = data.getInt(0);
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("error reading id json");
            return;
        }
        if (notificationId != -1) {
            JPushInterface.clearNotificationById(this.cordova.getActivity(), notificationId);
        } else {
            callbackContext.error("error id");
        }
    }

    void addLocalNotification(JSONArray data, CallbackContext callbackContext) throws JSONException {
        int builderId = data.getInt(0);
        String content = data.getString(1);
        String title = data.getString(2);
        int notificationID = data.getInt(3);
        int broadcastTime = data.getInt(4);
        String extrasStr = data.isNull(5) ? "" : data.getString(5);
        JSONObject extras = new JSONObject();
        if (!extrasStr.isEmpty()) {
            extras = new JSONObject(extrasStr);
        }

        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(builderId);
        ln.setContent(content);
        ln.setTitle(title);
        ln.setNotificationId(notificationID);
        ln.setBroadcastTime(System.currentTimeMillis() + broadcastTime);
        ln.setExtras(extras.toString());

        JPushInterface.addLocalNotification(this.cordova.getActivity(), ln);
    }

    void removeLocalNotification(JSONArray data, CallbackContext callbackContext) throws JSONException {
        int notificationID = data.getInt(0);
        JPushInterface.removeLocalNotification(this.cordova.getActivity(), notificationID);
    }

    void clearLocalNotifications(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.clearLocalNotifications(this.cordova.getActivity());
    }

    /**
     * 设置通知静默时间 http://docs.jpush.io/client/android_api/#api_5
     */
    void setSilenceTime(JSONArray data, CallbackContext callbackContext) {
        try {
            int startHour = data.getInt(0);
            int startMinute = data.getInt(1);
            int endHour = data.getInt(2);
            int endMinute = data.getInt(3);
            if (!isValidHour(startHour) || !isValidMinute(startMinute)) {
                callbackContext.error("开始时间数值错误");
                return;
            }
            if (!isValidHour(endHour) || !isValidMinute(endMinute)) {
                callbackContext.error("结束时间数值错误");
                return;
            }
            JPushInterface.setSilenceTime(this.cordova.getActivity(), startHour, startMinute, endHour, endMinute);
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("error: reading json data.");
        }
    }

    void setGeofenceInterval(JSONArray data, CallbackContext callbackContext) throws JSONException {
        long interval = data.getLong(0);
        JPushInterface.setGeofenceInterval(this.cordova.getActivity(), interval);
    }

    void setMaxGeofenceNumber(JSONArray data, CallbackContext callbackContext) throws JSONException {
        int maxNumber = data.getInt(0);
        JPushInterface.setMaxGeofenceNumber(mContext, maxNumber);
    }

    void setBadgeNumber(JSONArray data, CallbackContext callbackContext) throws JSONException {
        int badgeNumb = data.getInt(0);
        JPushInterface.setBadgeNumber(mContext, badgeNumb);
    }

    void setMobileNumber(JSONArray data, CallbackContext callbackContext) throws JSONException {
        int sequence = -1;
        String number = null;

        try {
            JSONObject params = data.getJSONObject(0);
            sequence = params.getInt("sequence");
            number = params.getString("mobileNumber");

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
            return;
        }

        eventCallbackMap.put(sequence, callbackContext);
        JPushInterface.setMobileNumber(mContext, sequence, number);
    }

    private boolean isValidHour(int hour) {
        return !(hour < 0 || hour > 23);
    }

    private boolean isValidMinute(int minute) {
        return !(minute < 0 || minute > 59);
    }

    /**
     * 用于 Android 6.0 以上系统申请权限，具体可参考：
     * http://docs.Push.io/client/android_api/#android-60
     */
    void requestPermission(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.requestPermission(this.cordova.getActivity());
    }

    private final TagAliasCallback mTagWithAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            if (instance == null) {
                return;
            }
            JSONObject data = new JSONObject();
            try {
                data.put("resultCode", code);
                data.put("tags", tags);
                data.put("alias", alias);
                final String jsEvent = String.format("cordova.fireDocumentEvent('jpush.setTagsWithAlias',%s)",
                        data.toString());
                cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        instance.webView.loadUrl("javascript:" + jsEvent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private boolean hasPermission(String appOpsServiceId) {

        Context context = cordova.getActivity().getApplicationContext();
        if (Build.VERSION.SDK_INT >= 24) {
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            return mNotificationManager.areNotificationsEnabled();
        } else {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();

            String pkg = context.getPackageName();
            int uid = appInfo.uid;
            Class appOpsClazz;

            try {
                appOpsClazz = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClazz.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opValue = appOpsClazz.getDeclaredField(appOpsServiceId);
                int value = opValue.getInt(Integer.class);
                Object result = checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg);
                return Integer.parseInt(result.toString()) == AppOpsManager.MODE_ALLOWED;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}