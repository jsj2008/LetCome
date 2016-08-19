////
////  ViewControllerManager.m
////  CaoPanBao
////
////  Created by QDS on 14-4-15.
////  Copyright (c) 2014年 Mark. All rights reserved.
////

#import "WpWaitView2.h"
#import "WpWaitViewController.h"

static ViewControllerManager* _controllerManager = nil;

@interface ViewControllerManager ()
{
    WpWaitView2* cWaitView2;
}



@end


@implementation ViewControllerManager


// 获得全局的ViewControllerManager
+ (ViewControllerManager*)sharedManager
{
    if (!_controllerManager) {
        _controllerManager = [[ViewControllerManager alloc] init];
    }
    return _controllerManager;
}
//
//
-(void)showText:(NSString *)text controller:(UIViewController*)controller delay:(float)delay
{
    [self showText:controller.navigationController.view text:text delay:delay];
}
//
-(void)showText:(NSString *)text controller:(UIViewController *)controller delay:(float)delay block:(dispatch_block_t)block
{
    const double delayInSeconds = delay;
    
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delayInSeconds * NSEC_PER_SEC)); // 1
    
    [self showText:NSLocalizedString(text, nil) controller:controller delay:delayInSeconds];
    
    dispatch_after(popTime, dispatch_get_main_queue(),block);
}
-(void)showText:(UIView *)parentView text:(NSString*)text delay:(float)delay
{
    if (HUD)
    {
        HUD=nil;
    }
    HUD = [[MBProgressHUD alloc] initWithView:parentView];
	[parentView addSubview:HUD];
    
    WpWaitViewController* waitViewController = [[WpWaitViewController alloc] initWithNibName:@"WpWaitViewController" bundle:nil tips:text WaitType:AlertWaitType];
    HUD.customView = waitViewController.view;
	
	// Set custom view mode
	HUD.mode = MBProgressHUDModeCustomView;
	
	HUD.delegate = self;
    
//    HUD.backgroundColor=[UIColor colorWithRed:20.f/255.f green:20.f/255.f blue:20.f/255.f alpha:0.8];
    HUD.backgroundColor=[UIColor clearColor];
	[HUD show:YES];
    
    [HUD hide:YES afterDelay:delay];
}

// 显示、隐藏等待框
- (void)showWaitView:(UIView*)parentView
{
//    if (cWaitView2)
//        return;
//    
//    cWaitView2 = [[WpWaitView2 alloc] initWithFrame:CGRectMake(0, 0, 0, 0)];
//    [parentView addSubview:cWaitView2];
    
    [self showWaitView:parentView withPoint:CGPointMake(0, 0)];
}

-(void)showWaitView:(UIView *)parentView withPoint:(CGPoint)point
{
    if (cWaitView2)
        return;
    
    cWaitView2 = [[WpWaitView2 alloc] initWithFrame:CGRectMake(0, 0, 0, 0)];
    cWaitView2.frame=CGRectMake(point.x, point.y, cWaitView2.frame.size.width, cWaitView2.frame.size.height);
    [parentView addSubview:cWaitView2];
}

- (void)hideWaitView
{
    if (cWaitView2 == nil)
        return;
    
    [cWaitView2 closeView];
    [cWaitView2 removeFromSuperview];
    cWaitView2 = nil;
}

// 页面回跳至指定页面
- (void) fromTheViewController:(UIViewController*)parentViewController jumptoSpecifyViewController:(NSString *)specifyViewController
{
    for (UIViewController *controller in parentViewController.navigationController.viewControllers)
    {
        if ([controller class] == NSClassFromString(specifyViewController))
        {
            [parentViewController.navigationController popToViewController:controller animated:YES];
             
             break;
        }
    }
}

// 页面回跳至指定页面
- (void) fromTheViewController:(UIViewController*)parentViewController backtoSpecifyViewController:(Class)specifyViewController
{
    for (UIViewController *controller in parentViewController.navigationController.viewControllers)
    {
        if ([controller isMemberOfClass:specifyViewController])
        {
            [parentViewController.navigationController popToViewController:controller animated:YES];
            
            break;
        }
    }
}

// 获取当前的controller
-(UIViewController*)currentController1{
    UIWindow * window = [[UIApplication sharedApplication] keyWindow];
    NSString *windowDescription = NSStringFromClass([window class]);
    while ([windowDescription isEqualToString:@"_UIAlertControllerShimPresenterWindow"]){
        window.hidden = YES;
        window = [[UIApplication sharedApplication] keyWindow];
        windowDescription = NSStringFromClass([window class]);
    }
    return [self currentController:window];
}

// 获取当前的controller
-(UIViewController*)currentController:(UIWindow*)window{
    
//    UIViewController *result = kShareAppDelegate.topController;
    UIViewController *result = nil;
    // 需要判断其他的controller
    
    //return result;
    
    if (window.windowLevel < UIWindowLevelNormal)
    {
        NSArray *windows = [[UIApplication sharedApplication] windows];
        for(UIWindow * tmpWin in windows)
        {
            if (tmpWin.windowLevel == UIWindowLevelNormal)
            {
                window = tmpWin;
                break;
            }
            else{
                
            }
        }
    }
    
    
    UIView *frontView = [[window subviews] objectAtIndex:0];
    
    id nextResponder = [frontView nextResponder];
    
    
    if ([nextResponder isKindOfClass:[UIViewController class]])
    {
        // 当前不处于nav.viewcontrollers中返回的当前controller
        result = nextResponder;
        
        //        if ([result isMemberOfClass:[WHDrawPswViewController class]])
        //        {
        //            return result;
        //        }
    }
    else
        result = window.rootViewController;
    UIViewController *resultCtrl ;
    if ([result isKindOfClass:[UINavigationController class]]) {
        resultCtrl= [((UINavigationController *)result).viewControllers lastObject];
        
    }else{
        resultCtrl = result;
    }
    
    while (resultCtrl.presentedViewController) {
        resultCtrl = resultCtrl.presentedViewController;
    }
    return resultCtrl;
    
}

-(void)dismissAllController:(UIViewController*)vc{
    if (vc.presentedViewController != nil) {
        [self dismissAllController:vc.presentedViewController];
    }
    [vc dismissViewControllerAnimated:NO completion:nil];
}


@end
