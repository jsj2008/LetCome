//
//  UIAlertView+RunInMainQueue.m
//  JuApp
//
//  Created by bizmatch on 15-2-24.
//  Copyright (c) 2015年 Shanghai-electric. All rights reserved.
//

#import "UIAlertView+RunInMainQueue.h"

@implementation UIAlertView (RunInMainQueue)
-(void)showInMainQueue{
    dispatch_async(dispatch_get_main_queue(), ^(void){
        [self show];
    });
}

+(void)simplePromptWithMessage:(NSString*)msg{//创建一个简单的提示框
    UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"提示" message:msg delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil];
    [alert showInMainQueue];
}

@end
