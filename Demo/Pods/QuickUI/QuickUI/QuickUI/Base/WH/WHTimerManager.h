//
//  CPBTimerManager.h
//  CaoPanBao
//
//  Created by zhuojian on 14-5-7.
//  Copyright (c) 2014年 Mark. All rights reserved.
//

#import <Foundation/Foundation.h>

#define WH_GLOBAL_USERR_NAME @"WH_GLOBAL_USERR_NAME" // 名字
#define WH_SERALIZE_CLASS_NAME @"serialize_class_name" // 序列化类名的键值名
#define WH_GLOBAL_EXPIRE_SESSION @"WH_GLOBAL_EXPIRE_SESSION"

/** 定时通知管理类 */
@interface WHTimerManager : NSObject
{
    
}
+(WHTimerManager*)shareTimerManager;

/**
 添加通知监听
 @param target 添加的目标对象
 @param selector 目标选择器
 @param notifyName 通知名称
 */
-(void)addTarget:(id)target selector:(SEL)selector notifyName:(NSString*)notifyName;

/**
 移除通知监听
 @param target 移除的目标对象
 @param notifyName 移除的通知名称
 */
-(void)removeTarget:(id)target notifyName:(NSString*)notifyName;

/**
 是否监听了此通知
 @param notifyName 通知名称
 @return YES 有监听，NO 无监听
 */
-(BOOL)hasNotifyName:(NSString*)notifyName;

/** 
 禁用通知监听
 @param 通知名称
 */
-(void)disabledNotify:(NSString*)notify;


/** 
 启用通知监听
 @param 通知名称
 */
-(void)enabledNotify:(NSString*)notify;


/** 删除所有通知监听 */
-(void)removeAll;


@property(nonatomic,strong)NSMutableDictionary* pDiabledDict;

@property(nonatomic,strong)NSMutableDictionary* pNotifyDict;

@end
