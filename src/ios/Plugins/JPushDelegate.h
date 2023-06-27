//
//  JPushDelegate.h
//  delegateExtention
//
//  Created by 万思 on 2020/2/15.
//  Copyright (c) 2015年 JPush. All rights reserved.
//

#import <UserNotifications/UserNotifications.h>
#import "JPUSHService.h"
// 定义全局变量
extern NSString * _Nonnull deviceTokenAPNS;
// 声明全局变量的setter方法
void setDeviceTokenAPNS(NSString * _Nonnull value);
@interface JPushDelegate: NSObject <JPUSHRegisterDelegate>

+(JPushDelegate*)getInstance;
+(void)initJPush;

-(void)registerForRemoteNotification;
-(void)startJPushSDK;

@end
