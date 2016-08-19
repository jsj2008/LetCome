//
//  Dock.h
//  MI新浪微博
//
//  Created by 余龙 on 14/12/28.
//  Copyright (c) 2014年 余龙. All rights reserved.
// 底部的tabbar

#import <UIKit/UIKit.h>

@class Dock;
@protocol DockDelegate <NSObject>

- (void)dock:(Dock *)dock itemSelectedFrom:(NSInteger)from to:(NSInteger)to;
- (BOOL)dock:(Dock *)dock canSelectedFrom:(NSInteger)from to:(NSInteger)to;
@end

@interface Dock : UIView

@property (nonatomic,assign) NSInteger selectedIndex;
@property (nonatomic,weak) id <DockDelegate>delegate;
//添加一个item
- (void)addItemWithIcon:(NSString *)imageName selectedIcon:(NSString *)selected title:(NSString *)title;
- (void)addItemWithIcon:(NSString *)imageName selectedIcon:(NSString *)selected title:(NSString *)title titleColor:(UIColor*)color;
//更改item的图片
-(void)changeIconAtIndex:(int)index icon:(NSString *)imageName selectedIcon:(NSString *)selected;

/**此方法仅，只针对于本app，用于dock的item外部选择*/
- (void)selectDockItem:(NSInteger)index;

@end
