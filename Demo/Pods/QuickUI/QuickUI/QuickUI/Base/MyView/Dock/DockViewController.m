//
//  DockViewController.m
//  MI新浪微博
//
//  Created by 余龙 on 14/12/28.
//  Copyright (c) 2014年 余龙. All rights reserved.
//

#import "DockViewController.h"

@interface DockViewController (){
    BOOL isSecond;
}

@end

@implementation DockViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self addDock];
}

#pragma mark - 添加dock
- (void)addDock
{
    //1.创建tabbar
    Dock *dock = [[Dock alloc]init];
    dock.frame = CGRectMake(0, self.view.bounds.size.height - kDockHight, self.view.bounds.size.width, kDockHight);
    dock.delegate = self;
    [self.view addSubview:dock];
    _dock = dock;
}


#pragma mark - dockDelegate
- (void)dock:(Dock *)dock itemSelectedFrom:(NSInteger)from to:(NSInteger)to
{
    
    [[NSNotificationCenter defaultCenter]postNotificationName:kDockItemChanged object:nil];

    if (to < 0 || to >= self.childViewControllers.count || (to == from && isSecond)) return;
    
    isSecond = YES;
    //0.移除旧的控制器
    UIViewController *oldVc = [self.childViewControllers objectAtIndex:from];
    [oldVc.view removeFromSuperview];
    
    //1.添加即将显示的新控制的
    UIViewController *newVc = [self.childViewControllers objectAtIndex:to];
    [newVc.view removeFromSuperview];
    CGFloat width = self.view.frame.size.width;
    CGFloat height = self.view.frame.size.height - kDockHight;
    newVc.view.frame = CGRectMake(0, 0, width, height);

    _selectedController = newVc;
    @try {
        [self.view addSubview:newVc.view];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception);
    }
    @finally {
        
    }
    
}

-(BOOL)dock:(Dock *)dock canSelectedFrom:(NSInteger)from to:(NSInteger)to{
    return YES;
}

@end
