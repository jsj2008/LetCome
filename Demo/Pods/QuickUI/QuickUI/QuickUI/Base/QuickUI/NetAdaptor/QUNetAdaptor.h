//
//  QUNetowrkAdaptor.h
//  CaoPanBao
//
//  Created by zhuojian on 14-5-27.
//  Copyright (c) 2014年 weihui. All rights reserved.
//

#import <Foundation/Foundation.h>
@class QUMockParam;
@class QUMock;
@class QUNetAdaptor;
@class QUNetResponse;
@class WpBaseAdapter;
@class WpResponse;

@protocol QUNetAdaptorDelegate <NSObject>
@optional
-(void)QUNetAdaptor:(QUNetAdaptor*)adaptor response:(QUNetResponse*)response;
@end

@interface QUNetAdaptor : NSObject
-(WpBaseAdapter *)createWpAdaptor;
-(void)request:(QUMockParam*)params;
//-(void)syncRequest:(QUMockParam*)params;
-(QUNetResponse *)responseSync:(WpResponse *)wpResponse;
-(void)responseCallback:(WpResponse *)wpResponse;
@property(nonatomic,weak)id<QUNetAdaptorDelegate> delegate;
@property(nonatomic,strong)NSString* operationType;
@property(nonatomic,assign)double delayTimeOut;

@end
