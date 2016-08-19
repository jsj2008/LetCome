//
//  BaseManager.h
//  QianFangGuJie
//
//  Created by  rjt on 15/5/18.
//  Copyright (c) 2015年 JYZD. All rights reserved.
//

#import <Foundation/Foundation.h>

@class ReturnValue;

typedef void (^ManagerReturnBlock)(ReturnValue *val,QUMock* mock ,QUEntity* entity);
typedef void (^SManagerReturnBlock)(ReturnValue *val,id mock ,QUEntity* entity);//兼容Swift

@interface BaseManager : NSObject

+(ReturnValue*)convertResult:(QUNetAdaptor *)adaptor response:(QUNetResponse *)response ;

@end

@interface ReturnValue : NSObject
@property BOOL result;
@property (nonatomic) NSInteger resultCode;
@property (nonatomic,strong) NSString* resultString;

@end
