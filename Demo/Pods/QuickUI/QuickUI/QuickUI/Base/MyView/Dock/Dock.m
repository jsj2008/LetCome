//
//  Dock.m
//  MI新浪微博
//
//  Created by 余龙 on 14/12/28.
//  Copyright (c) 2014年 余龙. All rights reserved.
//

#import "Dock.h"
#include "DockItem.h"

#define kItemTag 600  //增加tag值，精确获取item

@interface Dock ()
{
    DockItem* _selectedItem;
}

@end
@implementation Dock

-(void)addItemWithIcon:(NSString *)imageName selectedIcon:(NSString *)selected title:(NSString *)title{
    [self addItemWithIcon:imageName selectedIcon:selected title:title titleColor:Color_Dock_Selected];
}

- (void)addItemWithIcon:(NSString *)imageName selectedIcon:(NSString *)selected title:(NSString *)title titleColor:(UIColor*)color
{
    DockItem* dockItem = [[DockItem alloc]init];
    [dockItem setTitle:title forState:UIControlStateNormal];
    [dockItem setTitleColor:Color_Bg_RGB(146, 146, 146) forState:UIControlStateNormal];
    [dockItem setTitleColor:color forState:UIControlStateSelected];
    [dockItem setImage:[UIImage imageNamed:imageName] forState:UIControlStateNormal];
    [dockItem setImage:[UIImage imageNamed:selected] forState:UIControlStateSelected];
    [dockItem addTarget:self action:@selector(itemClick:) forControlEvents:UIControlEventTouchDown];
    
    [self addSubview:dockItem];
    
    
    NSInteger count = self.subviews.count;
    CGFloat height = self.frame.size.height;
    CGFloat weith = self.frame.size.width/count;
    for (int i = 0; i <count; i++) {
        DockItem *item = self.subviews[i];  // 数组的下标是从0开始的
        
        if ( count == 1) {
            [self itemClick:dockItem]; // 模拟点击
        }
        item.tag = i+kItemTag; //绑定一个tag标记
        item.frame = CGRectMake(weith*i, 0, weith, height);
    }
}




-(void)changeIconAtIndex:(int)index icon:(NSString *)imageName selectedIcon:(NSString *)selected;
{
    DockItem *item = self.subviews[index];
    [item setImage:[UIImage imageNamed:imageName] forState:UIControlStateNormal];
    [item setImage:[UIImage imageNamed:selected] forState:UIControlStateSelected];
}



- (void)selectDockItem:(NSInteger)index {

    [self itemClick:self.subviews[index]];
    
//    DockItem *item = (DockItem *)[self viewWithTag:index + kItemTag];
//    
//    if (_selectedItem.tag == item.tag) return;
//    [self itemClick:item];
}


#pragma mark - itemClick
- (void)itemClick:(DockItem *)item
{
    NSInteger from = _selectedItem.tag - kItemTag < 0 ? 0 : _selectedItem.tag - kItemTag;
    NSInteger to = item.tag - kItemTag < 0 ? 0 : item.tag - kItemTag;
    
    BOOL flg = YES;
    if ([_delegate respondsToSelector:@selector(dock:canSelectedFrom:to:)]) {
        flg = [_delegate dock:self canSelectedFrom:from to:to];
    }
    if (flg) {
        //通知代理，
        if ([_delegate respondsToSelector:@selector(dock:itemSelectedFrom:to:)]) {
            
            [_delegate dock:self itemSelectedFrom:from to:to];
        }
        _selectedItem.selected = NO;
        item.selected = YES;
        _selectedItem = item;
        
        _selectedIndex = to; // 选中按钮的tag标记
    }
   
}



@end
