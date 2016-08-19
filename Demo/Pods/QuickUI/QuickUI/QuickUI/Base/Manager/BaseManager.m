//
//  BaseManager.m
//  QianFangGuJie
//
//  Created by  rjt on 15/5/18.
//  Copyright (c) 2015年 JYZD. All rights reserved.
//

#import "BaseManager.h"

@implementation BaseManager

+(ReturnValue*)convertResult:(QUNetAdaptor *)adaptor response:(QUNetResponse *)response{
    ReturnValue* ret = [[ReturnValue alloc] init];
    if (response.pReason == QU_SERVICE_BACK_OK && response.pRetCode == 0 && response.pEntity){
        ret.result = YES;
    }else{
        ret.result = NO;
        ret.resultCode = response.pRetCode;
        ret.resultString = response.pRetString;
    }
    return  ret;
}

@end

@implementation ReturnValue

@end
