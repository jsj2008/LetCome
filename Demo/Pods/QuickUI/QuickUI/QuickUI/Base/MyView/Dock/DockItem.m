//
//  DockItem.m
//  MI新浪微博
//
//  Created by 余龙 on 14/12/28.
//  Copyright (c) 2014年 余龙. All rights reserved.
// 

#import "DockItem.h"


@implementation DockItem

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // 1.文字居中
        self.titleLabel.textAlignment = NSTextAlignmentCenter;
        //2.调整文字大小
        self.titleLabel.font = [UIFont systemFontOfSize:10];
        
        //3.图片内容模式
        self.imageView.contentMode = UIViewContentModeCenter;
        //4.选中button背景
        [self setBackgroundImage:[UIImage imageNamed:nil] forState:UIControlStateSelected];
    }
    return self;
}

#pragma mark - 覆盖父类在highted状态时的所有操作
- (void)setHighlighted:(BOOL)highlighted
{
//    [super setHighlighted:highlighted];
//    此方法会默认执行让按钮点击的时候变灰
//    覆盖此方法，使得按钮一按下就变色，没有灰色状态
}

#pragma mark - 调整内部Lable的frame
- (CGRect)titleRectForContentRect:(CGRect)contentRect
{
    //contentRect等于button.view的大小
    CGFloat titleX = 0;
    CGFloat titleH = contentRect.size.height*kTitleRatio;
    CGFloat tilteY = contentRect.size.height - titleH - 3;
    CGFloat titleW = contentRect.size.width;
    
    return CGRectMake(titleX, tilteY, titleW, titleH);
}

#pragma mark - 调整内部imageView的frame
- (CGRect)imageRectForContentRect:(CGRect)contentRect
{
    CGFloat imageX = 0;
    CGFloat imageY = 0;
    CGFloat imageW = contentRect.size.width;
    CGFloat imageH = contentRect.size.height*(1 - kTitleRatio);
    
    return CGRectMake(imageX, imageY, imageW, imageH);
}

@end
