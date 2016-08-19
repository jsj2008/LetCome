//
//  NSString+YL.m
//  QianFangGuJie
//
//  Created by 余龙 on 15/2/12.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import "NSString+YL.h"

#define redColorText [UIColor colorWithRed:252.0/255 green:70.0/255 blue:83.0/255 alpha:1.0];
#define greenColorText Color_Down_Green //跌绿
#define grayColorText Color_Bg_6a6a6a
@implementation NSString (YL)


+ (NSString *)stockStringStyleBystring:(NSString *)str {
    
    if (!str) return nil;
    if ([str floatValue] == 0) return @"0";
    
    NSString *backStr = nil;
    if ([str hasPrefix:@"-"]) {
        backStr = str;
    } else {
        
        if ([str hasPrefix:@"+"]) {
            backStr = str;
        } else {
            
            backStr = [NSString stringWithFormat:@"+%@",str];
        }
    }
    
    return [NSString stringWithFormat:@"%.2f",[backStr floatValue]];
}

+ (void)stockStringColorStyleWithString:(NSString *)str lable:(UILabel *)lable {

    NSString *showString = [self stockStringStyleBystring:str];
    
    if (!showString) {
    
        return;
    } else {
        if ([showString floatValue] == 0) {
            lable.textColor = grayColorText;
        } else if ([showString hasPrefix:@"-"]) {
            lable.textColor = greenColorText;
        } else {
            lable.textColor = redColorText;
        }
    }
    
    lable.text = showString;
}


@end
