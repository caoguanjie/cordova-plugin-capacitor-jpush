# 安卓自定义 API 说明

- [获取厂商ID](#获取厂商ID)
- [获取厂商别名Alias](#获取厂商别名Alias)
- [设置厂商别名Alias](#设置厂商别名Alias)
- [注销别名](#注销别名)
- [怪癖](#怪癖)

##  获取厂商ID

### API - getVendorID

获取当前设备厂商注册id，其中华为是token;
oppo需要先注册才能通过sdk拿到RegisterID;

#### 代码示例

```js
window.JPush.getVendorID(function(id:string){
    console.log("JPushPlugin:VendorID is " + id)
})
```

##  获取厂商别名Alias

### API - getVendorAlias

获取当前设备的别名；
注意：


#### 代码示例

```js
window.JPush.getVendorAlias(function(alias:string){
    console.log("JPushPlugin:Alias is " + alias)
})
```

##  设置厂商别名Alias

### API - setVendorAlias

设置当前设备厂商别名，四大厂商中小米和vivo都是设置别名，华为和oppo除外;

华为：在获取token后，调用注销方法可以重置当前设备token;

oppo：oppo与华为类似，区别是OPPO用的RegisterID，华为是token；通过注册和注销RegisterID用于推送；


#### 代码示例

```js
const params = {alias:"123"};
window.JPush.setVendorAlias(params,function(isSuccess:boolean){
    console.log("JPushPlugin:setVendorAlias is " + isSuccess)
})
```

#### 参数说明

- alias:string
  需要设置为别名的字符串


##  注销别名

### API - delVendorAlias

注销当前设备厂商别名；
四大厂商中小米和vivo都是注销或解绑别名,oppo和华为是注销id或token；

#### 接口定义

```js
 window.JPush.delVendorAlias({ alias: "" }, (isSuccess: boolean) => {
        console.log("JPushPlugin:delVendorAlias is " + isSuccess);
    });
```

#### 参数说明

- alias:string
  别名字符串

##  怪癖


华为和OPPO无法设置别名，替代方法如下：
1.华为推送设置别名只能通过注册和注销token实现，建议需要通过后端接口把华为token的信息上传用于推送；
2.oppo与华为类似，区别是OPPO用的RegisterID，华为是token；通过注册和注销RegisterID用于推送；
其中oppo有提供修改RegisterID方法api，但是无法用于推送，通过厂商平台推送设置好的id会提示未注册；
