//
//  NSTimer+YL.m
//  QianFangGuJie
//
//  Created by 余龙 on 15/3/30.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import "NSTimer+YL.h"

@implementation NSTimer (YL)

-(void)pauseTimer{
    
    if (![self isValid]) {
        return ;
    }
    
    [self setFireDate:[NSDate distantFuture]];
    
    
}


-(void)resumeTimer{
    
    if (![self isValid]) {
        return ;
    }
    
    //[self setFireDate:[NSDate dateWithTimeIntervalSinceNow:0]];
    [self setFireDate:[NSDate date]];
    
}

@end
