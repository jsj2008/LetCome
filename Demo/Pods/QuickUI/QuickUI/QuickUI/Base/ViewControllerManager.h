//
//  ViewControllerManager.h
//  CaoPanBao
//
//  Created by QDS on 14-4-15.
//  Copyright (c) 2014年 Mark. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MBProgressHUD.h"
#import "WHNotifyManager.h"

@interface ViewControllerManager : NSObject<MBProgressHUDDelegate>
{
    MBProgressHUD *HUD;
}

// 获得全局的ViewControllerManager
+ (ViewControllerManager*)sharedManager;

-(UIViewController*)currentController1;
-(UIViewController*)currentController:(UIWindow*)window;

/** 
 显示等待框（浮层文字）
 @param parentView 父视图
 @param text 显示文本
 @param delay 显示停留时间
 */
-(void)showText:(UIView *)parentView text:(NSString*)text delay:(float)delay;

-(void)showText:(NSString *)text controller:(UIViewController*)controller delay:(float)delay;

-(void)showText:(NSString *)text controller:(UIViewController *)controller delay:(float)delay block:(dispatch_block_t)block;

/** 
 显示等待框
 @param parentView 父视图
 */
- (void)showWaitView:(UIView*)parentView;

/**
 显示等待框
 @param parentView 父视图
 @param point 坐标
 */
-(void)showWaitView:(UIView *)parentView withPoint:(CGPoint)point;

// 隐藏等待框
- (void)hideWaitView;


// 页面回跳至指定页面
- (void) fromTheViewController:(UIViewController*)parentViewController jumptoSpecifyViewController:(NSString *)specifyViewController;

// 页面回调至指定页面
- (void) fromTheViewController:(UIViewController*)parentViewController backtoSpecifyViewController:(Class)specifyViewController;

//退出所有presented的页面
-(void)dismissAllController:(UIViewController*)vc;

@end
