# JPush SDK FCM通道集成指南


## 概述

Firebase 云消息传递 (FCM) 是由 Google 提供的推送服务，可以在服务器和用户设备之间建立可靠而且省电的连接，提高推送送达率。

JPush SDK 为了尽可能提高开发者在国外设备的推送送达率，对集成 FCM 的设备推送，自动切换到 FCM 通道。同时，为了保证 SDK 的易用性，原本 JPush 的所有接口调用逻辑都不用修改,JPush 会对自身支持的功能做兼容。

## 功能描述
+ FCM 集成完成后，在支持的设备上自动进行初始化。

+ FCM 可以与 JPUSH 和 其他 三方通道 共存。

+ FCM 通道初始化后支持 tag/alias 这些 JPush 原有的功能,其它的 JPush 未支持的功能目前暂时还不可用。

+ 通知效果：

	* 应用在前台时，极光收到FCM的广播消息，由极光弹出通知并触ACTION\_NOTIFICATION\_RECEIVED事件。

	* 应用在后台时，通知由FCM弹出，无法触发ACTION\_NOTIFICATION\_RECEIVED事件；

	* 无论前后台，点击的通知，如果有指定跳转页面，则跳转到用户配置的activity，未配置则跳转到主页。

***注1：***  使用 FCM 通道需要 Google Play 服务为系统服务且版本不低于17.3.4。
***注2：***  当设备网络环境为非中国时才会通过 FCM 通道进行推送。


## 手动配置集成步骤
主要步骤为：

* [1. 添加 FCM SDK 到项目中](#1)
* [2. 修改 minSdkVersion 的值](#2)
* [3. 配置 JPush 接收 FCM SDK 消息的服务类](#3)
* [4. 设置通知图标](#4)

#### <h3 id="1">1.  添加FCM SDK到项目</h3>

+ 拷贝third-push/fcm/libs中的插件包(jpush-android-plugin-fcm-v3.x.x.jar)到工程libs目录下

+ 在 [Firebase](https://firebase.google.com) 上创建和 JPush 上同包名的待发布应用,创建完成后下载该应用的 google-services.json 配置文件并添加到应用的 module 目录下。

+ 在根级 build.gradle 中添加规则，以纳入 Google 服务插件 和 Google 的 Maven 代码库,可根据 Firebase 发布的版本更新选择最新版本：

```
    buildscript {
    	dependencies {
        	classpath 'com.google.gms:google-services:4.2.0'
        }
  	}
	allprojects {
       repositories {
        	maven {
            	url "https://maven.google.com"
        	}
    	}
	}

```

+ 在应用 module 的 build.gradle 文件底部添加 apply plugin 代码行，以启用 gradle 插件：

```
	// ADD THIS AT THE BOTTOM
    apply plugin: 'com.google.gms.google-services'

```
+ 在应用 module 的 gradle 中 dependencies 节点添加如下代码，可根据 Firebase 发布的版本更新选择最新版本:

```
	 dependencies {
        compile 'com.google.firebase:firebase-messaging:17.3.4'
    }

```
**注1：**  极光集成 FCM 通道在 JPush Android SDK 3.1.0 添加。
**注2：** [添加 FCM SDK 的官方文档](https://firebase.google.com/docs/android/setup?authuser=0)

**注3：** 编译时如果遇到类似如下错误,拉取FCM依赖失败,请在 Android Studio->SDK Manager->SDK Tools 中将 Google Play Services 和 Google Repository 更新到最新版本后再试。

```
Failed to resolve: com.google.firebase:firebase-core:x.x.x
Error:(36, 13) Failed to resolve: com.google.firebase:firebase-messaging:x.x.x
```

#### <h3 id="2">2. 修改 minSdkVersion 的值</h3>

***注:*** FCM 推送会强制将 minSdkVersion 修改为 14。如果当前 app 使用的 minSdkVersion 的值小于 14,则需要使用 tools 避免被强制覆盖。

```
	<manifest
		xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:tools="http://schemas.android.com/tools"
	... >
	<uses-sdk
    		android:minSdkVersion="9"
    		android:targetSdkVersion="21"
    		tools:overrideLibrary=" com.google.android.gms.common.license,
            com.google.android.gms.tasks.license,
            com.google.firebase.firebase.common.license,
            com.google.firebase.firebase.iid.license,
            com.google.firebase.firebase.messaging.license,
            com.google.firebase.measurement.impl.license,
            com.google.firebase.measurement.license,
            com.google.firebase.firebase_core,
            com.google.firebase.measurement,
            com.google.firebase.firebase_common,
            com.google.firebase.messaging,
            com.google.firebase.iid,
            com.google.android.gms,
            com.google.android.gms.tasks,
            com.google.firebase.iid.internal,
            com.google.firebase.analytics.connector,
            com.google.android.gms.stats,
            com.google.android.gms.common,
            com.google.android.gms.measurement.api,
            com.google.android.gms.ads_identifier,
            com.google.android.gms.measurement_base,
            com.google.firebase.analytics.connector.impl,
            android.support.v4,
            android.support.compat,
            android.arch.lifecycle,
            android.support.mediacompat,
            android.support.coreutils,
            android.support.coreui,
            android.support.fragment,
            com.google.firebase.measurement_impl"" />

```

#### <h3 id="3">3. 配置JPush接收的FCM SDK的消息服务类</h3>

```
<service android:name="cn.jpush.android.service.PluginFCMMessagingService">
     <intent-filter>
           <action android:name="com.google.firebase.MESSAGING_EVENT"/>
     </intent-filter>
</service>

<service android:name="cn.jpush.android.service.PluginFCMInstanceIdService">
     <intent-filter>
           <action android:name="com.google.firebase.INSTANCE_ID_EVENT"/>
     </intent-filter>
</service>

```
#### <h3 id="4">4. 设置通知栏图标</h3>
在 AndroidManifest.xml 中增加如下配置来设置 FCM 通知图标。

```
 <meta-data
      android:name="com.google.firebase.messaging.default_notification_icon"
      android:resource="@drawable/您要配置的通知图标" />
```


## 点击通知跳转 Activity
### 功能说明

FCM 允许开发者在推送通知的时候传入自定义的 intent action 字符串，当用户点击了该通知，系统会根据 action 的值过滤出匹配的 Activity ，并打开 Activity，获取推送内容。

### 使用方式

#### Push API 推送说明

在 push api 的 payload 中的 "notification" 的 "android" 节点下添加以下字段：

<div class="table-d" align="center" >
	<table border="1" width = "100%">
		<tr  bgcolor="#D3D3D3" >
			<th >关键字</th>
			<th >类型</th>
			<th >示例</th>
			<th >说明</th>
		</tr>
		<tr >
			<td>uri_action</td>
			<td>string</td>
			<td>"cn.jpush.android.ui.OpenClickActivity"</td>
			<td>该字段用于指定开发者想要打开的 activity。值为该activity下您配置的特殊action name</td>
		</tr>
	</table>
</div>


***示例：***

```
demo manifest 配置为：
 <!-- 点击通知时，要打开的 activity -->
        <activity android:name="com.example.jpushdemo.OpenClickActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="com.example.jpushdemo.OpenClickActivity"/>
            	<category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>

请求json如下：
{
    "platform": [
        "android"
    ],
    "audience": "all",
    "notification": {
        "android": {
            "alert": "在线alert003",
            "title": "在线title003",
            "uri_action": "com.example.jpushdemo.OpenClickActivity",
        }
    },
    "message": {
        "msg_content": "自定义消息内容003"
    }
}
```


####SDK 端配置

##### 1. 在 AndroidManifest.xml 中配置点击通知要打开的 activity
```
 <activity android:name="您配置的activity"
           android:exported="true">
      <intent-filter>
            <action android:name="您配置的特殊action"/>
            <category android:name="android.intent.category.DEFAULT" />
       </intent-filter>
 </activity>
```
##### 2.获取通知相关信息
在您配置的 activity 中的onCreate方法中进行处理，获取通知信息。

通过 getIntent().getExtras().getString("JMessageExtra") 获取到数据。获取到的数据是 JSON 字符串，通过解析可以获得通知相关内容。

JSON 示例如下：

```
{
	"msg_id":"123456",
	"n_content":"this is content",
	"n_extras":{"key1":"value1","key2":"value2"},
	"n_title":"this is title",
	"rom_type":0
}
```

JSON 内容字段说明：

字段|取值类型|描述
---|---|---
msg_id|String|通过此key获取到通知的msgid
n_title|String|通过此key获取到通知标题
n_content|String|通过此key获取到通知内容
n_extras|String|通过此key获取到通知附加字段
rom_type| byte|通过此key获取到下发通知的平台。得到值说明：byte类型的整数， 0为极光，1为小米，2为华为，3为魅族，4为oppo，5为vivo，8为FCM。

***注：*** rom_type 用于点击事件的上报，一般情况下开发者只需要取到该字段的值用于上报，不需要关心具体取值。


##### 3.通知点击上报
解析通知内容后，需主动调用接口来进行通知点击上报，上报接口如下：

```
/**
* context 上下文
* msgId 消息ID
* whichPushSDK 收到推送的平台，即 rom_type 字段的取值。
**/
JPushInterface.reportNotificationOpened(Context context, String msgId, byte whichPushSDK);
```
***注：*** 点击上报必须传入正确的 whichPushSDK 参数，否则会造成统计数据错误。

##### 4.富媒体调整
+ 为 PushActivity 增加 ```<action android:name="cn.jpush.android.ui.PushActivity" />```。


+ 为 PopWinActivity 增加 ```<action android:name="cn.jpush.android.ui.PopWinActivity" />```。


```
<activity
     android:name="cn.jpush.android.ui.PushActivity"
     android:configChanges="orientation|keyboardHidden"
     android:theme="@android:style/Theme.NoTitleBar">
        <intent-filter>
            <action android:name="cn.jpush.android.ui.PushActivity" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="您的应用的包名" />
        </intent-filter>
</activity>

<activity
     android:name="cn.jpush.android.ui.PopWinActivity"
     android:configChanges="orientation|keyboardHidden"
     android:theme="@style/MyDialogStyle">
        <intent-filter>
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="您的应用的包名" />
            <action android:name="cn.jpush.android.ui.PopWinActivity"/>
        </intent-filter>
</activity>
```

##### 5.Activity 示例代码
```
package com.example.jpushdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by jiguang on 17/7/5.
 */

public class OpenClickActivity extends Activity {
    private static final String TAG = "OpenClickActivity";
    /**消息Id**/
    private static final String KEY_MSGID = "msg_id";
    /**该通知的下发通道**/
    private static final String KEY_WHICH_PUSH_SDK = "rom_type";
    /**通知标题**/
    private static final String KEY_TITLE = "n_title";
    /**通知内容**/
    private static final String KEY_CONTENT = "n_content";
    /**通知附加字段**/
    private static final String KEY_EXTRAS = "n_extras";
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView = new TextView(this);
        setContentView(mTextView);
        handleOpenClick();
    }


    /**
     * 处理点击事件，当前启动配置的Activity都是使用
     * Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
     * 方式启动，只需要在onCreat中调用此方法进行处理
     */
    private void handleOpenClick() {
        Log.d(TAG, "用户点击打开了通知");
        String data = null;
        //获取华为平台附带的jpush信息
        if (getIntent().getData() != null) {
             data = getIntent().getData().toString();
        }

        //获取fcm或oppo平台附带的jpush信息
        if(TextUtils.isEmpty(data) && getIntent().getExtras() != null){
            data = getIntent().getExtras().getString("JMessageExtra");
        }

        Log.w(TAG, "msg content is " + String.valueOf(data));
        if (TextUtils.isEmpty(data)) return;
        try {
            JSONObject jsonObject = new JSONObject(data);
            String msgId = jsonObject.optString(KEY_MSGID);
            byte whichPushSDK = (byte) jsonObject.optInt(KEY_WHICH_PUSH_SDK);
            String title = jsonObject.optString(KEY_TITLE);
            String content = jsonObject.optString(KEY_CONTENT);
            String extras = jsonObject.optString(KEY_EXTRAS);
            StringBuilder sb = new StringBuilder();
            sb.append("msgId:");
            sb.append(String.valueOf(msgId));
            sb.append("\n");
            sb.append("title:");
            sb.append(String.valueOf(title));
            sb.append("\n");
            sb.append("content:");
            sb.append(String.valueOf(content));
            sb.append("\n");
            sb.append("extras:");
            sb.append(String.valueOf(extras));
            sb.append("\n");
            sb.append("platform:");
            sb.append(getPushSDKName(whichPushSDK));
            mTextView.setText(sb.toString());

            //上报点击事件
            JPushInterface.reportNotificationOpened(this, msgId, whichPushSDK);
        } catch (JSONException e) {
            Log.w(TAG, "parse notification error");
        }


    }

    private String getPushSDKName(byte whichPushSDK) {
        String name;
        switch (whichPushSDK){
            case 0:
                name = "jpush";
                break;
            case 1:
                name = "xiaomi";
                break;
            case 2:
                name = "huawei";
                break;
            case 3:
                name = "meizu";
                break;
            case 4:
                name = "oppo";
                break;
            case 5:
                name = "vivo";
                break;
            case 8:
                name = "fcm";
                break;
            default:
                name = "jpush";
        }
        return name;
    }
}
```

