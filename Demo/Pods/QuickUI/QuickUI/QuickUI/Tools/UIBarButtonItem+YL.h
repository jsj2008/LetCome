//
//  UIBarButtonItem+YL.h
//  QianFangGuJie
//
//  Created by 余龙 on 15/1/24.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIBarButtonItem (YL)


- (id)initWithIcon:(NSString *)icon highlightedIcon:(NSString *)highlighted target:(id)target action:(SEL)action;
+ (id)buttonItemWithIcon:(NSString *)icon highlightedIcon:(NSString *)highlighted target:(id)target action:(SEL)action;

@end
