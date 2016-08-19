//
//  CPBNotifyManager.h
//  CaoPanBao
//
//  Created by zhuojian on 14-6-25.
//  Copyright (c) 2014年 Mark. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface WHNotifyManager : NSObject
+(WHNotifyManager*)shareNotifyManager;

/**
 添加通知
 @param target 通知目标对象
 @param selector 回调选择器
 @param notifyName 通知别名
 */
-(void)addNotificationTarget:(id)target selector:(SEL)selector notifyName:(NSString*)notifyName;

/**
 移除通知
 @param target 移除目标对象通知
 @param notifyName 移除的通知别名
 */
-(void)removeNotificationTarget:(id)target notifyName:(NSString*)notifyName;

/**
 是否有通知
 @param notifyName 通知别名
 */
-(BOOL)hasNotifyName:(NSString*)notifyName;

/**
 发送通知
 @param notifyName 通知别名
 */
-(void)postNotify:(NSString*)notifyName;


@property(nonatomic,strong)NSMutableDictionary* pNotifyDict;
@end
