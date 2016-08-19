//
//  BaseConfigureManager.h
//  QuickUI
//
//  Created by chenyi on 15/12/16.
//  Copyright © 2015年 JYZD. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BaseConfigureManager : NSObject
@property (strong,nonatomic) UIColor *navBgColor;//导航栏背景颜色

// 获得全局的BaseConfigureManager
+ (BaseConfigureManager*)sharedManager;
////for navBgImg
//-(UIImage *)imageWithR:(float)red G:(float)green B:(float)bule alpha:(float)alpha;
@end
