//
//  UIButton+EnlargeEdge.h
//  QianFangGuJie
//
//  Created by chenyi on 15/6/30.
//  Copyright (c) 2015年 JYZD. All rights reserved.
//

#import <objc/runtime.h>

@interface UIButton (EnlargeEdge)
- (void)setEnlargeEdge:(CGFloat) size;
- (void)setEnlargeEdgeWithTop:(CGFloat) top right:(CGFloat) right bottom:(CGFloat) bottom left:(CGFloat) left;
@end
