//
//  DockViewController.h
//  MI新浪微博
//
//  Created by 余龙 on 14/12/28.
//  Copyright (c) 2014年 余龙. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Dock.h"

@interface DockViewController : MyViewController<DockDelegate>
{
    Dock *_dock;

}

@property(readonly,nonatomic)UIViewController *selectedController;

- (void)dock:(Dock *)dock itemSelectedFrom:(NSInteger)from to:(NSInteger)to;

- (BOOL)dock:(Dock *)dock canSelectedFrom:(NSInteger)from to:(NSInteger)to;
/**
 *  仅使用与本APP，用于刷新定时器的管理
 *
 *  @param index 对应点击tabbar的位置
 */
//- (void)removeNotifyByTab:(NSInteger)index;


@end
