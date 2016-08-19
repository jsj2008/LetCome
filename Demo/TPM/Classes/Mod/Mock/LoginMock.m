//
//  LoginMock.m
//  LetCome
//
//  Created by  rjt on 16/8/12.
//  Copyright © 2016年 JYZD. All rights reserved.
//

#import "LoginMock.h"
#import "LoginEntity.h"

@implementation LoginParam

-(NSString *)sendMethod
{
    
    return @"POST";
}

@end

@implementation LoginMock

-(NSString *)getOperatorType
{
    
    return @"/user/login";
}

-(Class)getEntityClass
{
    
    return [LoginEntity class];
}

@end
