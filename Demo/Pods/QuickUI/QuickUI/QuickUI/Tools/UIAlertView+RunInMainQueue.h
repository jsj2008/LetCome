//
//  UIAlertView+RunInMainQueue.h
//  JuApp
//
//  Created by bizmatch on 15-2-24.
//  Copyright (c) 2015年 Shanghai-electric. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIAlertView (RunInMainQueue)
-(void)showInMainQueue;
+(void)simplePromptWithMessage:(NSString*)msg;//创建一个简单的提示框
@end
