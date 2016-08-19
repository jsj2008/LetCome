//
//  QUNetWorkAFAdaptor.m
//  CaoPanBao
//
//  Created by zhuojian on 14-5-27.
//  Copyright (c) 2014年 weihui. All rights reserved.
//

#import "QUNetAFAdaptor.h"
#import "WpAFAdapter.h"
@implementation QUNetAFAdaptor
-(WpBaseAdapter *)createWpAdaptor{
    return [[WpAFAdapter alloc] initWithTarget:self selector:@selector(responseCallback:)];
}



@end
