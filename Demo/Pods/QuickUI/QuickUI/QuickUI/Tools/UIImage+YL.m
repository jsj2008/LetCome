//
//  UIImage+YL.m
//  QianFangGuJie
//
//  Created by 余龙 on 15/1/4.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import "UIImage+YL.h"

@implementation UIImage (YL)

#pragma mark - 可以自由拉伸的图片，中间
+ (UIImage *)resizeImage:(NSString *)imageName
{
    //    return [image resizableImageWithCapInsets:UIEdgeInsetsMake(<#CGFloat top#>, <#CGFloat left#>, <#CGFloat bottom#>, <#CGFloat right#>)]
    //    return [image resizableImageWithCapInsets:UIEdgeInsetsMake(<#CGFloat top#>, <#CGFloat left#>, <#CGFloat bottom#>, <#CGFloat right#>) resizingMode:<#(UIImageResizingMode)#>]
    
    
    //    return [image stretchableImageWithLeftCapWidth:image.size.width*0.5 topCapHeight:image.size.height*0.5];
    return [self resizeImage:imageName xPos:0.5 yPos:0.5];
}

#pragma mark - 根据点来拉伸图片
+ (UIImage *)resizeImage:(NSString *)imageName xPos:(CGFloat)xPos yPos:(CGFloat)yPos
{
    UIImage *image = [UIImage imageNamed:imageName];
    return  [image stretchableImageWithLeftCapWidth:image.size.width*xPos topCapHeight:image.size.height*yPos];
}

@end
