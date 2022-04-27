# 极光官方支持的 cordova 推送插件
>注意本插件是从官方的渠道fork下来的代码，然后集成了产商推送，解决了在capacitor平台上，ios报错等问题
>
>注意：插件从 v3.4.0 开始支持 cordova-android 7.0.0，因 cordova-android 7.0.0 修改了 Android 项目结构，因此不兼容之前的版本，升级前请务必注意。
>
>如果需要在cordova-android 7.0.0之前版本集成最新插件，参照[这篇文章](https://www.jianshu.com/p/23b117ca27a6)
>
>如果需要安装之前版本的插件，请先安装 v1.2.0 以下版本（建议安装 v1.1.12）的 [cordova-plugin-jcore](https://github.com/jpush/cordova-plugin-jcore)，再安装旧版本插件（比如 v3.3.2），否则运行会报错。
>
>[Cordova Android版本与原生版本对应表](http://cordova.apache.org/docs/en/latest/guide/platforms/android/index.html#requirements-and-support)

# 集成厂商推送
修改`plugin.xml`#394行附近参数为自己应用后台的信息即可。

具体可参见`src/android/third-push`目录下的`xx厂商集成指南.md`

## Capacitor Install

```shell
npm install cordova-plugin-capacitor-jpush
ionic cap sync android 
# npx cap sync android
```

## Cordova Install

> 注意：
> - 应用的包名一定要和 APP_KEY 对应应用的包名一致，否则极光推送服务无法注册成功。
> - 在使用 8 或以上版本的 Xcode 调试 iOS 项目时，需要先在项目配置界面的 Capabilities 中打开 Push Notifications 开关。

- 通过 Cordova Plugins 安装，要求 Cordova CLI 5.0+：

  ```shell
  cordova plugin add cordova-plugin-capacitor-jpush.git --variable APP_KEY=your_jpush_appkey
  ```

- 或直接通过 url 安装：

  ```shell
  cordova plugin add https://github.com/caoguanjie/cordova-plugin-capacitor-jpush.git --variable APP_KEY=your_jpush_appkey
  ```

- 或下载到本地安装：

  ```shell
  cordova plugin add Your_Plugin_Path --variable APP_KEY=your_jpush_appkey
  ```
  
  
### 参数

  - APP_KEY: 必须设置，JPush 上注册的包名对应的 Appkey

    ```shell
    --variable APP_KEY=your_jpush_appkey
    ```

  - CHANNEL: 可以不设置，v3.6.0+ 版本开始支持(Android Only)，方便开发者统计 APK 分发渠道，默认为 developer-default.

    ```shell
    --variable CHANNEL=your_channel
    ```

  - 同时动态配置 APP_KEY 和 CHANNEL 示例

    ```shell
    cordova plugin add cordova-plugin-capacitor-jpush.git --variable APP_KEY=your_jpush_appkey --variable CHANNEL=your_channel
    ```

### Ionic

如果使用了 Ionic，可以再安装 @jiguang-ionic/jpush 包，适配 ionic-native：

```shell
# 使用ionic4+，vue的项目
npm install --save @jiguang-ionic/jpush@2.0.0 


# npm install --save @jiguang-ionic/jpush@1.0.2 for  ionic2 or ionic3
```

然后在 *Vue的项目* 中增加：

```js
// 使用vue、react的项目
import { JPush } from '@jiguang-ionic/jpush';
JPush.init();

// @jiguang-ionic/jpush@2.0.0+，适合angular的项目
import { JPush } from '@jiguang-ionic/jpush/ngx';
...
  providers: [
    ...
    JPush,
    ...
  ]
```



## Usage

### API

- [Common](/doc/Common_detail_api.md)
- [iOS](/doc/iOS_API.md)
- [Android](/doc/Android_detail_api.md)


## FAQ

> 如果遇到了疑问，请优先参考 Demo 和 API 文档。若还无法解决，可到 [Issues](https://github.com/caoguanjie/cordova-plugin-capacitor-jpush.git/issues) 提问。

### Android

#### 在 Eclipse 中 import 工程之后出现：*Type CallbackContext cannot be resolved to a type*

右键单击工程名 -> Build Path -> Config Build Path -> Projects -> 选中工程名称 -> CordovaLib -> 点击 add。

#### 无法获取到 Registration Id

检查 AppKey 和应用包名是否对应、是否调用了 `init` 方法。

#### 如何自定义通知声音？

Android 因为各 Rom 的高度定制化，不像 iOS 一样能有一个统一的管理，如果在 Android 中想自定义通知铃声，推荐通过 JPush 推送自定义
消息，之后在 `jpush.receiveMessage` 事件监听中通过 [Cordova Local-Notification Plugin](https://github.com/katzer/cordova-plugin-local-notifications) 触发通知，再配合 [Cordova Background Plugin](https://github.com/katzer/cordova-plugin-background-mode) 插件保证应用的后台运行。

#### 如何让通知内容像 iOS 一样自动换行展示？

[#267](https://github.com/caoguanjie/cordova-plugin-capacitor-jpush.git/issues/267)

#### 关闭 App 后收不到通知

Android 的推送通过长连接的方式实现，只有在保持连接的情况下才能收到通知。而有的第三方 ROM 会限制一般应用服务的自启动，也就是在退出应用后，应用的所有服务均被杀死，且无法自启动，所以就会收不到通知。

目前 JPush 是做了应用互相拉起机制的，也就是当用户打开其他集成了 JPush 的应用时，你的应用也能同时收到推送消息。

如果你的应用希望随时都能收到推送，官方推荐是通过文案的方式引导用户在设置中允许你的应用能够自启动，常见机型的设置方法可以参考[这里](https://docs.jiguang.cn/jpush/client/Android/android_faq/#_2)。

或者自己实现应用保活，网上有很多相关文章（不推荐）。

> 为什么 QQ、微信之类的应用退出后还能够收到通知？因为这些大厂应用，手机厂商默认都会加入自启动白名单中，也不会在应用退出后杀死它们的相关服务。
> 如果你多加留意，就会发现非大厂的应用如果你一段时间不用都是收不到推送的。

### iOS

#### XCode 10 收不到推送怎么办？

打开 xcode -> file -> WorkSpace Settings… -> Build System 改成 Legacy Build System 然后卸载 App 重新运行。

#### 打包时遇到 i386 打包失败怎么办？

```shell
cordova platform update ios
```

#### PushConfig.plist 文件中的字段都是什么意思？

- Appkey：应用标识。
- Channel：渠道标识。
- IsProduction：是否生产环境。
- IsIDFA：是否使用 IDFA 启动 SDK。

#### 刚集成完插件收不到推送怎么办？

请首先按照正确方式再次配置证书、描述文件，具体可参考 [iOS 证书设置指南](https://docs.jiguang.cn/jpush/client/iOS/ios_cer_guide/)。

#### iOS 集成插件白屏、或无法启动插件、或打包报错无法找到需要引入的文件怎么办？

按照以下步骤逐个尝试：

- 升级至最新版本 Xcode
- 删除插件、再重装插件（先使用 `cordova platform add ios`，后使用 `cordova plugin add`）

## Support

- QQ 群：413602425 / 524248013
- [JPush 官网文档](https://docs.jiguang.cn/jpush/guideline/intro/)
- [极光社区](http://community.jiguang.cn/)

## Contribute

Please contribute! [Look at the issues](https://github.com/caoguanjie/cordova-plugin-capacitor-jpush.git/issues).

## License

MIT © [JiGuang](/license)
