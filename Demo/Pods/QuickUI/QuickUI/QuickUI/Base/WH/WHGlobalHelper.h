//
//  CPBGlobalHelper.h
//  CaoPanBao
//
//  Created by zhuojian on 14-5-8.
//  Copyright (c) 2014年 Mark. All rights reserved.
//

#import "QUEntity.h"

/**
 离线缓存接口
 在实体类头文件增加此接口，使实体类具有离线磁盘缓存的特性
 */
@protocol WHGlobalHelperDelegate <NSObject>
/**缓存*/
-(void)store:(NSString*)key;

/** 从磁盘恢复缓存 */
+(instancetype)restore:(NSString*)key content:(NSDictionary*)content;
@end

/** 全局变量存取类
 例子:
 
 [[WHGlobalHelper]shareGlobalHelper put:@"zhuojian" key:@"name"];
 NSString* name=[[WHGlobalHelper]shareGlobalHelper get:@"name"];
 
 */
@interface WHGlobalHelper : QUEntity
{
    NSMutableDictionary* dict;
}

/**
 保存键值
 @param obj 值
 @param key 键
 */
-(void)put:(id)obj key:(NSString*)key;

/**
 获取值
 @param key 键
 @return 返回值(如果值不存在，返回nil)
 */
-(id)get:(NSString*)key;

/**
 @param key 键
 @param defValue 默认获取失败返回的值
 @return 返回值(如果获取失败，返回defValue)
 */
-(id)get:(NSString*)key defValue:(id)defValue;

+(WHGlobalHelper*)shareGlobalHelper;

+(NSString*)key:(NSString*)key prefix:(NSString*)prefix;

-(void)removeAll;
-(void)removeByKey:(NSString*)key;
@end
