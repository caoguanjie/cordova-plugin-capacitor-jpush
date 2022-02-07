//
//  JPushDelegate.h
//  delegateExtention
//
//  Created by 万思 on 2020/2/15.
//  Copyright (c) 2015年 JPush. All rights reserved.
//

#import <UserNotifications/UserNotifications.h>
#import "JPUSHService.h"

@interface JPushDelegate: NSObject <JPUSHRegisterDelegate>

+(JPushDelegate*)getInstance;
+(void)initJPush;

-(void)registerForRemoteNotification;
-(void)startJPushSDK;

@end
