//
//  KChartView.h
//  QianFangGuJie
//
//  Created by  rjt on 15/5/28.
//  Copyright (c) 2015年 JYZD. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface KChartView : UIView
@property(strong,nonatomic)NSString* jingdu;            //精度

/*!
 *  @brief  绘制K线图
 *
 *  @param records 包含K线图数据的数组
 */
-(void)drawChart:(NSArray*)records;

/*!
 *  @brief  组装分时图
 *
 *  @param resize    是否调整大小
 *  @param isMin   是否分钟K线
 */
-(void)installChart:(BOOL)resize stock:(BOOL)isStock minutes:(BOOL)isMin isNeedVol:(BOOL)needVol;

@end
