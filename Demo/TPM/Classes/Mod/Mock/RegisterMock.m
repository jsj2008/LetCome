//
//  RegisterMock.m
//  LetCome
//
//  Created by  rjt on 16/8/12.
//  Copyright © 2016年 JYZD. All rights reserved.
//

#import "RegisterMock.h"
#import "LoginEntity.h"

@implementation RegisterParam

-(NSString *)sendMethod
{
    
    return @"POST";
}

@end

@implementation RegisterMock

-(NSString *)getOperatorType
{
    
    return @"/user/register";
}

-(Class)getEntityClass
{
    
    return [LoginEntity class];
}

@end
