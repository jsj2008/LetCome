//
//  BaseConfigureManager.m
//  QuickUI
//
//  Created by chenyi on 15/12/16.
//  Copyright © 2015年 JYZD. All rights reserved.
//

#import "BaseConfigureManager.h"

static BaseConfigureManager* _baseConfigureManager = nil;

@implementation BaseConfigureManager

// 获得全局的BaseConfigureManager
+ (BaseConfigureManager*)sharedManager{
    if (!_baseConfigureManager) {
        _baseConfigureManager = [[BaseConfigureManager alloc]init];
    }
    
    return _baseConfigureManager;
}

//默认值在此处添加
-(instancetype)init{
    if (self =  [super init]) {
        self.navBgColor = Color_Bg_RGB(32.0f,42.0f,57.0f);
    }
    return self;
}

//-(UIImage *)imageWithR:(float)red G:(float)green B:(float)bule alpha:(float)alpha{
//    CGSize imageSize = CGSizeMake(50, 50);
//    UIGraphicsBeginImageContextWithOptions(imageSize, 0, [UIScreen mainScreen].scale);
//    [[UIColor colorWithRed:red/255.0 green:green/255.0 blue:bule/255.0 alpha:alpha] set];
//    UIRectFill(CGRectMake(0, 0, imageSize.width, imageSize.height));
//    UIImage *pressedColorImg = UIGraphicsGetImageFromCurrentImageContext();
//    UIGraphicsEndImageContext();
//    return pressedColorImg;
//}
@end
