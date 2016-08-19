//
//  QUHLine.m
//  A50
//
//  Created by  rjt on 15/10/11.
//  Copyright © 2015年 JYZD. All rights reserved.
//

#import "QUVSplitLine.h"

@implementation QUVSplitLine

-(void)awakeFromNib{
    UIColor *color = self.backgroundColor;
    self.backgroundColor = [UIColor clearColor];
    self.lineLayer = [CAShapeLayer layer];
    self.lineLayer.backgroundColor = color.CGColor;
    [self.layer addSublayer:self.lineLayer];
}

-(void)layoutSubviews{
    self.lineLayer.frame = CGRectMake(self.frame.size.width-0.5, 0, 0.5,self.frame.size.height);
}
@end
