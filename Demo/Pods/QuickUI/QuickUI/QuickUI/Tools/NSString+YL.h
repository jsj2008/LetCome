//
//  NSString+YL.h
//  QianFangGuJie
//
//  Created by 余龙 on 15/2/12.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (YL)

/**
 *  出入股票数值字符串，返回+，-，0样式
 *
 *  @param str 需要转换的字符串
 *
 *  @return 拼接好得字符串
 */
+ (NSString *)stockStringStyleBystring:(NSString *)str;

/**
 *  股票字符串，用于设置不同+，-，0样式字符串，及显示时对应的颜色
 *
 *  @param str   需要展示的字符串
 *  @param lable 显示的lable
 */
+ (void)stockStringColorStyleWithString:(NSString *)str lable:(UILabel *)lable;

@end
