# JPush SDK OPPO通道集成指南


## 概述

在国内 Android 生态中，推送通道都是由终端与云端之间的长链接来维持，严重依赖于应用进程的存活状态。如今一些手机厂家会在自家 rom 中做系统级别的推送通道，再由系统分发给各个 app，以此提高在自家 rom 上的推送送达率。

JPush SDK 为了尽可能提高开发者在各类 rom 上的推送送达率，对使用 ColorOS 的设备推送，自动切换到 OPPO 通道。同时，为了保证 SDK 的易用性，原本 JPush 的所有接口调用逻辑都不用修改，JPush 会对自身支持的功能做兼容。只需在 manifest 中配置上 OPPO SDK 必须的配置组件即可。

## 功能描述

+ JPush 初始化的时候可选择是否初始化 OPush 通道。

+ 在 ColorOS 设备上 JPush 通道与 OPush 通道共存。

+ OPush 通道初始化后支持 stopPush/resumePush 与 tag/alias这些 JPush 原有的功能，其它的 JPush 未支持的功能目前暂时还不可用。

+ 通知效果：

	* OPPO 通道不支持 ACTION\_NOTIFICATION\_RECEIVED 和 ACTION\_NOTIFICATION\_OPENED事件。

	* 点击通知，如果有指定跳转页面，则跳转到用户配置的activity，未配置则跳转到主页。


***注：*** OPPO 推送目前支持 ColorOS3.1 以上的系统，ColorOS3.0 目前正在随着 OTA 计划集成并支持。R11 及之后的新上市机型全部支持 OPUSH，R11 之前主要机型，正在通过 OTA 升级方式进行更新。JPush 通过 PushManager.isSupportPush(Context context) 接口进行判断，对支持的机型，通知走 OPPO 通道，不支持则走极光通道。


## 手动配置集成步骤

主要步骤为：

* [1. 增加OPPO插件包及OPPO推送包](#1)

* [2. 配置OPPO推送sdk所需要的权限](#2)

* [3. 配置OPPO必须的组件](#3)

* [4. 将OPPO_APPKEY、OPPO_APPID、OPPO_APPSECRET 替换为在OPPO后台注册对应该应用的AppKey/AppID/APPSecret](#4)


#### <h3 id="1">1. 增加OPPO插件包及OPPO推送包</h3>
+ 将third-push目录下找到oppo目录，从libs中拷贝其中的jar包至工程的libs目录下。
+ jar包说明：
	* jpush-android-plugin-oppo-v3.x.x.jar : JPush 插件包
	* com.coloros.mcssdk.jar : OPPO 推送包


***注1***：极光集成 OPPO 通道在 JPush Android SDK 3.1.5 添加。

#### <h3 id="2">2. 配置OPPO推送sdk所需要的权限</h3>

```
 	<uses-permission android:name="com.coloros.mcs.permission.RECIEVE_MCS_MESSAGE"/>

```


#### <h3 id="3">3. 配置OPPO必须的组件</h3>

```

	 <service android:name="cn.jpush.android.service.PluginOppoPushService">
            <intent-filter>
                <action android:name="com.coloros.mcs.action.RECEIVE_MCS_MESSAGE" />
            </intent-filter>
     </service>

```


#### <h3 id="4">4. 将OPPO应用的 appkey 、appid 、appsecret 加上前缀“OP-”，填入meta-data 标签中</h3>


```

	 <meta-data android:name="OPPO_APPKEY"
	       android:value="OP-您的应用对应的OPPO的APPKEY" />

     <meta-data android:name="OPPO_APPID"
            android:value="OP-您的应用对应的OPPO的APPID" />

     <meta-data android:name="OPPO_APPSECRET"
            android:value="OP-您的应用对应的OPPO的APPSECRET" />


```

## 使用 JCenter 自动化集成步骤

+ 确认android studio的 Project 根目录的主 gradle 中配置了jcenter支持。（新建project默认配置就支持）

```
	buildscript {
		repositories {
			jcenter()
		}
            ......
	}

	allprojets {
		repositories {
			jcenter()
		}
	}

```

+ 在应用 module 的 gradle 中 dependencies 节点添加如下代码:


```
    dependencies {
        compile 'cn.jiguang.sdk.plugin:oppo:3.1.5'
    }

```

+ 在应用 module 的 gradle 中 defaultConfig 节点添加如下代码:


```
    manifestPlaceholders = [

        // 设置manifest.xml中的变量
        OPPO_APPKEY : "OP-您的应用对应的OPPO的APPKEY", // OPPO平台注册的appkey
        OPPO_APPID : "OP-您的应用对应的OPPO的APPID", // OPPO平台注册的appid
        OPPO_APPSECRET: "OP-您的应用对应的OPPO的APPSECRET"//OPPO平台注册的appsecret

    ]


```

## OPush SDK的编译混淆问题

如果使用了 proguard，需要在配置文件中加入，可以防止一个误报的 warning 导致无法成功编译。

	-dontwarn com.coloros.mcsdk.**
	-keep class com.coloros.mcsdk.** { *; }


## 通知内容长度兼容
### 功能说明
由于 OPPO 官方的通知内容长度限制为200个字数以内（中英文都算一个），当通知内容（极光的“alert”字段的值）长度超过200字，OPPO 通道会推送失败。此时调用极光api推送通知，请在payload 中的 "notification" 的 "android" 节点的"extras"节点添加以下字段：

### 使用方式

#### Push API 推送说明
<div class="table-d" align="center" >
	<table border="1" width = "100%">
		<tr  bgcolor="#D3D3D3" >
			<th >关键字</th>
			<th >类型</th>
			<th >示例</th>
			<th >说明</th>
		</tr>
		<tr >
			<td>oppns_content_forshort</td>
			<td>string</td>
			<td>"short content"</td>
			<td>通知内容（极光的“alert”字段）长度超过200个字时，请在此字段的值传入不超过200个字的通知内容。</td>
		</tr>
	</table>
</div>


***请求json示例：***

```
{
    "platform": [
        "android"
    ],
    "audience": "all",
    "notification": {
        "android": {
            "alert": "在国内 Android 生态中，推送通道都是由终端与云端之间的长链接来维持，严重依赖于应用进程的存活状态。如今一些手机厂家会在自家 rom 中做系统级别的推送通道，再由系统分发给各个 app，以此提高在自家 rom 上的推送送达率。JPush SDK 为了尽可能提高开发者在各类 rom 上的推送送达率，对使用 ColorOS 的设备推送，自动切换到OPPO通道。同时，为了保证 SDK 的易用性，原本 JPush 的所有接口调用逻辑都不用修改,JPush 会对自身支持的功能做兼容.只需在manifest中配置上 OPPO SDK 必须的配置组件即可.",
            "title": "概述",
            "uri_action": "com.example.jpushdemo.OpenClickActivity",
            "extras": {
                "oppns_content_forshort": "在国内 Android 生态中，推送通道都是由终端与云端之间的长链接来维持，严重依赖于应用进程的存活状态。"
            }
        }

    },
    "message": {
        "msg_content": "自定义消息内容003"
    }
}

```




## 点击通知跳转 Activity
### 功能说明

OPPO 允许开发者在推送通知的时候传入自定义的 intent action 字符串，当用户点击了该通知，系统会根据 action 的值过滤出匹配的 Activity ，并打开 Activity，获取推送内容。若不配置，点击通知则直接打开应用主页。

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
			<td>"com.example.jpushdemo.OpenClickActivity"</td>
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
            "uri_action": "com.example.jpushdemo.OpenClickActivity"
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
rom_type| byte|通过此key获取到下发通知的平台。得到值说明：byte类型的整数， 0为极光，1为小米，2为华为，3为魅族，4为 OPPO，8为FCM。

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

        //获取fcm、oppo平台附带的jpush信息
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
            	name= "oppo";
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



