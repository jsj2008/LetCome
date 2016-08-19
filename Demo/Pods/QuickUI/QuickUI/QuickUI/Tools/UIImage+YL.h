//
//  UIImage+YL.h
//  QianFangGuJie
//
//  Created by 余龙 on 15/1/4.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (YL)

//可以自由拉伸的图片，中间
+ (UIImage *)resizeImage:(NSString *)imageName;
//根据点来拉伸图片
+ (UIImage *)resizeImage:(NSString *)imageName xPos:(CGFloat)xPos yPos:(CGFloat)yPos ;

@end
