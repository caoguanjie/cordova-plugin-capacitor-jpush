# JPush SDK 华为通道集成指南


## 概述

在国内 Android 生态中，推送通道都是由终端与云端之间的长链接来维持，严重依赖于应用进程的存活状态。如今一些手机厂家会在自家 rom 中做系统级别的推送通道，再由系统分发给各个 app，以此提高在自家 rom 上的推送送达率。

JPush SDK 为了尽可能提高开发者在各类 rom 上的推送送达率，对使用 EMUI 的设备推送，自动切换到华为通道。同时，为了保证 SDK 的易用性，原本 JPush 的所有接口调用逻辑都不用修改,JPush 会对自身支持的功能做兼容.只需在manifest中配置上华为HMS SDK 必须的配置组件即可.

## 功能描述

+ JPush 初始化的时候可选择是否初始化 HMS Push 通道。

+ 在 EMUI 设备上 JPush 通道与 华为 通道共存.

+ 华为 通道初始化后支持 tag/alias这些 JPush 原有的功能,其它的 JPush 未支持的功能目前暂时还不可用.

+ 增加兼容华为HMS SDK初始化失败处理错误的接口.

***注***：极光根据 EMUI 系统版本间差异，现采取的方案是 EMUI 4.1 及以上版本，使用 HMS 服务，通知走 HMS 通道；对于 EMUI 4.1 以下版本还是走极光通道。

## 手动配置集成步骤

主要步骤为：

* [1. 增加华为HMS SDK的推送包及资源文件](#1)

* [2. 增加support v4包](#2)

* [3. 修改 minSdkVersion 的值](#3)

* [4. 配置HMS SDK Push必须的组件](#4)

* [5. 配置接收 HMS 消息的广播接收器](#5)

* [6. 替换 HMS 的 appid](#6)

* [7. 在build.gradle中配置在华为后台添加的指纹证书对应的签名](#7)


#### <h3 id="1">1. 增加华为HMS SDK的推送包及资源文件</h3>

* 将third-push/huawei/res里面的资源复制到工程/res中对应的目录
* 将third-push/huawei/assets里面的资源复制到工程/assets中对应的目录
* 将third-push/huawei/libs中的包添加到工程/libs目录下。
* jar包说明：
* HMS\_SDK\_x.x.x.xxx.jar：华为推送包
* jpush-android-plugin-huawei-v3.x.x：华为插件包

* 将third-push/huawei/res中的包添加到工程/res目录下。

***注1***：极光集成华为通道在 JPush Android SDK 3.0.5 添加，对应测试的华为HMS SDK 版本为：HMS-SDK-2.4.0.300.aar

***注2***：JPush Android SDK 3.0.9 适配HMS SDK的升级,对应测试的华为HMS SDK 版本为:HMSSdk-base-2.5.2.300.aar,HMSSdk-push-2.5.2.300.aar

***注3***：JPush Android SDK 3.1.2 适配HMS SDK的升级,对应测试的华为HMS SDK 版本为:HMSSdk-base-2.5.3.302.aar,HMSSdk-push-2.5.3.302.aar

***注4***：从HMS_SDK_2.6.0.301版本开始支持jar包+res资源集成，JPush Android SDK 3.1.5对应测试的华为HMS SDK 版本为:HMS_SDK_2.6.0.301.jar

***注5***：JPush Android SDK 3.2.0 更新华为HMS SDK版本为:hmssdk_2.6.3.301.jar

***注6***：JPush Android SDK 3.3.6 更新华为HMS SDK版本为:hmssdk_2.6.3.306.jar



#### <h3 id="2">2. 增加support v4包</h3>
将android-support-v4.jar添加到工程/libs目录下,如果app已经添加了support v4包可忽略这一步.

***注***： JPush 3.0.9 版本更新了华为HMS SDK版本到2.5.2.300,HMS SDK 2.5.2.300 版本的Push相关功能不用依赖V4包.

#### <h3 id="3">3. 修改 minSdkVersion 的值</h3>

***注***： HMS arr 会强制将 minSdkVersion 修改为 14。如果当前 app 使用 minSdkVersion 的值小于 14，则需要使用 tools 避免被强制覆盖。

```
	<manifest xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:tools="http://schemas.android.com/tools"
		...
	>
		<uses-sdk
			android:minSdkVersion="9"
			android:targetSdkVersion="21"
			tools:overrideLibrary="com.huawei.android.hms.base,com.huawei.android.hms.push" />

```


#### <h3 id="4">4. 配置HMS SDK Push必须的组件</h3>

```
	<provider
		android:name="com.huawei.hms.update.provider.UpdateProvider"
		android:authorities="您应用的包名.hms.update.provider"
		android:exported="false"
		android:grantUriPermissions="true">
	</provider>
```

#### <h3 id="5">5. 配置接收 HMS 消息的广播接收器</h3>


```
	<receiver android:name="cn.jpush.android.service.PluginHuaweiPlatformsReceiver">
		<intent-filter>
			<!-- 必须,用于接收token -->
			<action android:name="com.huawei.android.push.intent.REGISTRATION" /> 			<!-- 必须,用于接收消息 -->
			<action android:name="com.huawei.android.push.intent.RECEIVE" />
			<!-- 可选,用于点击通知栏或通知栏上的按钮后触发onEvent回调 -->
			<action android:name="com.huawei.android.push.intent.CLICK" />
			<!-- 可选,查看push通道是否连接,不查看则不需要 -->
			<action android:name="com.huawei.intent.action.PUSH_STATE" />
		</intent-filter>

		<meta-data
			android:name="CS_cloud_ablitity"
			android:value="successRateAnalytics" />

	</receiver>

```


#### <h3 id="6">6. 替换 HMS 的 appid </h3>
在华为控制台上获取注册应用的 appid，并填充在 manifest 如下所示的位置。

```
	<meta-data
		android:name="com.huawei.hms.client.appid"
		android:value="appid=您的应用对应华为的appID">
	</meta-data>

```

#### <h3 id="7">7. 在build.gradle中配置在华为后台添加的指纹证书对应的签名</h3>
***注***：HMS 服务必须要求 app 签名才能注册成功。指纹证书是在终端采用keytool -list -v -keystore keystorefileName 获取偶对应的指纹证书.


```
    signingConfigs {
        release {
            storeFile file("release.keystore")//签名文件的path
            storePassword "123456"
            keyAlias "android.keystore"
            keyPassword "123456"
        }
    }

	buildTypes {
        release {
            minifyEnabled true
            proguardFiles 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        debug{
        	minifyEnabled false
            signingConfig signingConfigs.release
        }
    }

```

## 使用 JCenter 自动化集成步骤

+ 因为华为本身不支持jcenter集成方式，所以需要在 android studio的 Project 根目录的主 gradle 中配置其提供的库引用地址

```
allprojects {
	repositories {
        jcenter()
        maven {url 'http://developer.huawei.com/repo/'}
    }
}
```

+ 在应用 module 的 gradle 中 dependencies 节点添加如下代码:


```
    dependencies {
         compile 'cn.jiguang.sdk.plugin:huawei:3.x.x'//和对应JPush版本相同
    }

```

+ 在应用 module 的 gradle 中 defaultConfig 节点添加如下代码:

```
	manifestPlaceholders = [

        // 设置manifest.xml中的变量
        HUAWEI_APPID : "您的应用对应华为的appID", // 华为平台注册的appid
    ]


```

+ 因为华为需要依赖v4包,所以需要引入v4包,如果您的工程本来就已经引入v4包,可以忽略此步骤

```
    dependencies {
         compile 'com.android.support:support-v4:+'
    }


```
***注***： JPush 3.0.9 版本更新了华为HMS SDK版本到2.5.2.300,HMS SDK 2.5.2.300 版本的Push相关功能不用依赖V4包.


## HMS SDK的编译混淆问题

如果使用了 proguard，需要在配置文件中加入,可以防止一个误报的 warning 导致无法成功编译，

```
	–keep class com.huawei.hms.**{*;}

```

## 新增兼容华为HMS SDK接口
1. HMS connect失败时，需要根据错误码来判断是否能通过用户行为来解决该错误,所以增加新的接口来适配此种行为.

2. 接口所在类名为:cn.jpush.android.api.JPluginPlatformInterface,以下所有接口需要在Activity中调用,并且Activity如果调用相关接口就必须全部调用.漏掉一个接口则会导致内存泄漏或功能异常.

3. 如果在Activity中没有调用相关接口,在HMS SDK初始化失败并且可以通过用户行为解决的错误则会没有入口解决(比如华为手机上的华为移动服务框架版本太低导致的初始化失败,此时HMS SDK则会弹出对话框引导用户升级用户华为移动服务框架等).

***注***：以上内容中 “用户行为” 指 app 使用者的操作行为，一般情况下就是点击 HMS 插件升级提示。

### 1. public void onStart(Activity activity)
   在Activity中的onStart方法中调用.该接口会引用当前Activity,并且如果HMS SDK没有初始化则会初始化HMS SDK.


### 2. public void onStop(Activity activity)
   在Activity中的onStop方法中调用.该接口会清空对当前Activity的引用.



### 3. public void onActivityResult(Activity activity,int requestCode, int resultCode, Intent data)
该接口是经过用户操作解决完错误之后,返回让HMS重新初始化.

### 代码示例：

```
    public class MainActivity extends Activity{

        private JPluginPlatformInterface pHuaweiPushInterface;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            initView();
            registerMessageReceiver();  // used for receive msg
            pHuaweiPushInterface = new JPluginPlatformInterface(this.getApplicationContext());
        }

        @Override
        public void onStart() {
            super.onStart();
            pHuaweiPushInterface.onStart(this);
        }

        @Override
        public void onStop() {
            super.onStop();
            pHuaweiPushInterface.onStop(this);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            //JPush中调用HMS SDK解决错误的接口传入的requestCode为10001,开发者调用是请注意不要同样使用10001
            if(requestCode == JPluginPlatformInterface.JPLUGIN_REQUEST_CODE) {
                pHuaweiPushInterface.onActivityResult(this, requestCode, resultCode, data);
            }
        }
    }

```

## 点击通知跳转 Activity


### 功能说明

#### 支持的版本

此功能从 JPush Android SDK 3.0.9 开始支持

#### 华为手机通知跳转的定义

华为 push 允许开发者在推送通知的时候传入自定义的 intent uri 字符串，当用户点击了该通知，系统会根据 uri 的值过滤出匹配的 Activity ，并打开 Activity，达到跳转的目的。

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
			<td>uri_activity</td>
			<td>string</td>
			<td>"com.example.jpushdemo.OpenClickActivity"</td>
			<td>该字段用于指定开发者想要打开的 activity，值为activity 节点的 “android:name ” 属性值。</td>
		</tr>
	</table>
</div>


***示例：***

```
demo manifest配置：
<activity android:name="com.example.jpushdemo.OpenClickActivity"
      android:exported="true">
      <intent-filter>
           <action android:name="android.intent.action.VIEW"/>
           <category android:name="android.intent.category.DEFAULT"/>
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
            "uri_activity": "com.example.jpushdemo.OpenClickActivity",
        }
    },
    "message": {
        "msg_content": "自定义消息内容003"
    }
}
```


####SDK 端配置

##### 1.AndroidManifest.xml中配置点击通知要打开的 activity
```
<activity android:name="您配置的activity"
      android:exported="true">
      <intent-filter>
           <action android:name="android.intent.action.VIEW"/>
           <category android:name="android.intent.category.DEFAULT"/>
      </intent-filter>
</activity>
```
***注：*** android:exported 属性必须设置为 true，并增加示例中的 intent-filter，否则会导致无法收到通知。

##### 2.获取通知相关信息
目前启动配置的 activity 都是使用 Intent.FLAG\_ACTIVITY\_CLEAR\_TOP | Intent.FLAG\_ACTIVITY\_NEW\_TASK 方式启动，只需要在您配置的 activity 中的onCreate方法中进行处理，获取通知信息。

通过 getIntent().getData() 获取到Intent 数据。获取到的数据是 JSON 字符串，通过解析可以获得通知相关内容。

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
在 AndroidManifest.xml 中将 PushActivity、PopWinActivity 的 android:exported="false" 属性修改为 true，否则会导致收不到富媒体推送。

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


## 华为检测缺少HMS agent的问题临时解决方法：
  1. 将同级目录下的HMSSdk-agent.aar 文件复制到工程的libs目录下
  2. 在build.gradle文件中增加如下：
```
  repositories {
      flatDir{
          dirs 'libs'
      }
  }
```
  3. 在build.gradle文件中将HMSSdk-agent.aar加入dependencies

```
  dependencies {
      compile(name: 'HMSSdk-agent', ext: 'aar')
  }

```

  4.增加混淆配置

```
  -dontwarn com.huawei.**
  -keep public class * extends android.app.Activity
  -keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
  -keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}

```
  * 注：当前此方法仅仅适用于使用极光集成华为推送华为检测不通过的临时解决方案，当有接入华为的支付，sns等其它sdk时当前方法时不适用的，请知悉.
