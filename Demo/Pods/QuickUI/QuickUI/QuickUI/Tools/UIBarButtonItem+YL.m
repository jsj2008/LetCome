//
//  UIBarButtonItem+YL.m
//  QianFangGuJie
//
//  Created by 余龙 on 15/1/24.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import "UIBarButtonItem+YL.h"

@implementation UIBarButtonItem (YL)

- (id)initWithIcon:(NSString *)icon highlightedIcon:(NSString *)highlighted target:(id)target action:(SEL)action
{
    //分类调用的是本身，不需要调用父类方法，if ( self = [super init])，例如该父类是UIBarItem
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage *image = [UIImage imageNamed:icon];
    [button setBackgroundImage:image forState:UIControlStateNormal];
    [button setBackgroundImage:[UIImage imageNamed:highlighted] forState:UIControlStateHighlighted];//btn背景图片填充
    button.bounds = (CGRect){CGPointZero,image.size};
    [button addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    
    return [self initWithCustomView:button];
}

+ (id)buttonItemWithIcon:(NSString *)icon highlightedIcon:(NSString *)highlighted target:(id)target action:(SEL)action
{
    return [[self alloc]initWithIcon:icon highlightedIcon:highlighted target:target action:action];
}

@end
