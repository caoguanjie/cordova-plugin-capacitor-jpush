# JPush SDK 华为通道集成指南


## 概述

在国内 Android 生态中，推送通道都是由终端与云端之间的长链接来维持，严重依赖于应用进程的存活状态。如今一些手机厂家会在自家 rom 中做系统级别的推送通道，再由系统分发给各个 app，以此提高在自家 rom 上的推送送达率。

JPush SDK 为了尽可能提高开发者在各类 rom 上的推送送达率，对使用 EMUI 的设备推送，自动切换到华为通道。同时，为了保证 SDK 的易用性，原本 JPush 的所有接口调用逻辑都不用修改,JPush 会对自身支持的功能做兼容.只需在manifest中配置上华为HMS SDK 必须的配置组件即可.

## 功能描述

+ JPush 初始化的时候可选择是否初始化 HMS Push 通道。

+ 在 EMUI 设备上 JPush 通道与 华为 通道共存.

+ 华为 通道初始化后支持 tag/alias这些 JPush 原有的功能,其它的 JPush 未支持的功能目前暂时还不可用.

+ 增加兼容华为HMS SDK初始化失败处理错误的接口.

***注***：极光根据 EMUI 系统版本间差异，现采取的方案是 华为推送 SDK V6.1.0.300 及以上版本

## 手动配置集成步骤
> [官方集成文档](https://docs.jiguang.cn/jpush/client/Android/android_3rd_guide#%E5%8D%8E%E4%B8%BA%E9%80%9A%E9%81%93%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97)

这里有特别的注意事项要说明一下，官方的集成文档都是推荐用mavenCentral进行自动化集成的，我们为什么使用半自动化，半手动集成呢，很大一部分是，官方的文档集成是建立在原生android项目底下进行自动化集成的，而我们是利用插件的集成的，关键是产商的AppKey等签名都是放在js层进行维护的，不像原生项目那样，只能放在gradle文件里面进行。因此我们采用了半自动半手动的集成方式。


### 第一步，新建huawei.gradle文件，配置 mavenCentral 支持
在 `huawei.gradle`文件中 配置 `mavenCentral` 支持和 `agconnect` 依赖。
```gradle
buildscript {
    repositories {
        mavenCentral()
        // 配置HMS Core SDK的Maven仓地址。
        maven {url 'https://developer.huawei.com/repo/'}
    }
    dependencies {
        // 增加agcp插件配置，推荐您使用最新版本的agcp插件。
        // classpath 'com.huawei.agconnect:agcp:1.6.0.300'
    }
} 
repositories {
    mavenCentral()
    // maven { url 'https://jitpack.io' }
    // 配置HMS Core SDK的Maven仓地址。
    maven {url 'https://developer.huawei.com/repo/'}
    flatDir{
        dirs 'lib'
    }
}
// apply plugin: 'com.huawei.agconnect'
dependencies {
    implementation 'com.huawei.hms:push:6.3.0.304'
    //厂商版本和 JPush SDK 版本保持一致
    implementation 'cn.jiguang.sdk.plugin:huawei:4.6.3'
}
```

这里要特别说明一下，代码：
```
# 这里是集成jpush+huawei相关的SDK
implementation 'cn.jiguang.sdk.plugin:huawei:4.6.3'
```
这句代码等于是在`plugin.xml`文件设置以下内容：
```xml
 <!-- huawei推送 start -->
       <lib-file src="src/android/third-push/huawei/libs/jpush-android-plugin-huawei-v4.6.3.jar" /> 
<!-- huawei推送 end -->
 <!--huawei_config_start-->
<service
    android:name="cn.jpush.android.service.PluginHuaweiPlatformsService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.huawei.push.action.MESSAGING_EVENT" />
    </intent-filter>
</service>
<!--huawei_config_end-->

```
这两部分的代码起到的作用都是一样的，一个是自动集成，一个是手动集成，两者只需要有一个就行了。

#### 第二步，添加华为角标
在`plugin.xml`文件中加入华为角标的权限代码
```xml
 <!--华为角标-->
<uses-permission android:name="com.huawei.android.launcher.permission.CHANGE_BADGE"/>
```

#### 第三步，配置 agconnect-services.json 文件
> 记得把agconnect-services.json文件放在capacitor项目的根目录底下。

在`plugin.xml`文件中配置agconnect-services.json添加路径
```xml
 <source-file src="../../agconnect-services.json" target-dir="../../"/>
```

#### 第四步，配置 签名证书

在`plugin.xml`文件中配置`HUAWEI_APPID`华为的应用的签名证书，这里为什么是`HUAWEI_APPID`这个单词呢？这里了原理是占位符，后期通过nodejs的方式，替换动态`HUAWEI_APPID`的值，以此达到换华为appid的效果
```xml
    <!-- 华为huawei推送 start-->
    <meta-data android:name="com.huawei.hms.client.appid" android:value="appid=HUAWEI_APPID"/>
    <!-- 华为huawei推送 end-->
```


#### 第五步，引入huawei.gadle文件

在`plugin.xml`文件中引用huawei.gadle文件，

```xml
 <framework src="src/android/third-push/huawei/huawei.gradle" custom="true" type="gradleReference"/>
```

### 注意事项
1. 目前`src/android/third-push/huawei/assets`和`src/android/third-push/huawei/res`这里两个目录里面的文件都是没有任何作用的，这是因为上面用了gradle的自动集成，这些文件在自动集成的过程中就会下载到android项目里面，所以暂时没用，以后如果需要切换手动集成的时候，这些文件都能用上。