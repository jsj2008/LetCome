//
//  CPBOnceTask.h
//  CaoPanBao
//
//  Created by zhuojian on 14-5-7.
//  Copyright (c) 2014年 Mark. All rights reserved.
//

#import <Foundation/Foundation.h>

/**
 WHOnceTask 间隔调用类
 
 例子：
 
 WHOnceTask.h
 #define CPB_ONCETASK_HQ_KEY @"CPB_ONCETASK_HQ_KEY"   // 刷新行情
 #define CPB_ONCETASK_HQ_VAL 1                        // 刷新时间一秒钟

 xxx.m
 
 if([[WHOnceTask shareOnceTask]expired:CPB_ONCETASK_HQ_KEY validTime:CPB_ONCETASK_HQ_VAL]) // 如果过期执行下面代码
 {
 
 }
 
 
 */
@interface WHOnceTask : NSObject
{
    NSMutableDictionary* taskDict;
}
+(WHOnceTask*)shareOnceTask;
/**
 任务过期
 @param 任务名称
 @validTime 间隔时间（秒）
 @return YES 过期，触发任务 NO 不过期，不触发任务
 */
-(BOOL)expired:(NSString *)key validTime:(double)validTime;

/**
 任务过期
 @param 任务名称
 @validTime 间隔时间（秒）
 @return 离任务结束剩余时间
 */
-(double)expiredInterval:(NSString *)key validTime:(double)validTime;

/**
 移除任务(达到任务立刻过期的效果)
 @param key 任务名称
 */
-(void)removeTask:(NSString*)key;

/** 清空任务(所有任务全部过期) */
-(void)removeAll;

@end
