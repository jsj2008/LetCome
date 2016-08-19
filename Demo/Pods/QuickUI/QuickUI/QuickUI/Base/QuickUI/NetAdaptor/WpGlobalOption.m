//
//  WpSdkLibGlobalOption.m
//  WeiboPaySdkLib
//
//  Created by Mark on 13-5-29.
//  Copyright (c) 2013年 WeiboPay. All rights reserved.
//

#import "WpCommonFunction.h"
#import "WpGlobalOption.h"
#import "WpBaseAdapter.h"
//#import "WpSaveDeviceInfoAdapter.h"

#import "WHGlobalHelper.h"
#import "WHStringHelper.h"
#import "ViewControllerManager.h"

#import "WpAlertViewCommon.h"

#import "QUNetResponse.h"
#import "QUNetAdaptor.h"

@interface WpGlobalOption ()
{
    NSOperationQueue* _imageOperationQueue;
    NSOperationQueue* _sdkOperationQueue;
    
    QUServiceState _preServiceCallStatus;
    
    // 设备Hash值
    NSString* _deviceHashValue;

}

- (void)createGlobalObject;

// 设备网络环境变化回调
- (void)reachabilityChanged:(NSNotification*)note;
- (void)saveDeviceInfoAdapterCallback:(WpResponse*)response;

@end


@implementation WpGlobalOption

// 获得全局的GlobalOption
+ (WpGlobalOption*)sharedOption
{
    //
    static dispatch_once_t wpSdkLibGlobalOptionOnceToken;
    static WpGlobalOption* globalWpSdkLibGlobalOption = nil;
    
    dispatch_once(&wpSdkLibGlobalOptionOnceToken, ^{
        globalWpSdkLibGlobalOption = [[WpGlobalOption alloc] init];
        [globalWpSdkLibGlobalOption createGlobalObject];
    });
    
    return globalWpSdkLibGlobalOption;
}

- (void)createGlobalObject
{
    _imageOperationQueue = [[NSOperationQueue alloc] init];
    [_imageOperationQueue setMaxConcurrentOperationCount:16];
    
    _sdkOperationQueue = [[NSOperationQueue alloc] init];
    [_sdkOperationQueue setMaxConcurrentOperationCount:8];
    
    _deviceHashValue = @"";
    
}

// 调用SDK服务端请求
- (void)executeUrlOperation:(NSOperation*)operation
{
    [_sdkOperationQueue addOperation:operation];
}

// 调用服务端其他次要请求（后台加载请求或图片请求）
- (void)executeImageOperation:(NSOperation*)operation
{
    [_imageOperationQueue addOperation:operation];
}

// 得到设备的HashValue
- (NSString*)getDeviceHashValue
{
    return _deviceHashValue;
}


- (void)saveDeviceInfoAdapterCallback:(QUNetResponse*)response
{
    if (QU_SERVICE_BACK_OK == [self serviceCallBackFromApp:response andShowMessage:NO])
    {
        _deviceHashValue = response.pData;
    }
    
//    _saveDeviceInfoAdapter = nil;
}


// 服务端请求返回状态判断
//
// 方式三：提示，回首页，登录页
// 方式四：提示，回首页，回商户app
- (NSInteger)serviceCallBack:(QUNetResponse*)response andShowMessage:(BOOL)bShow andFollow:(NSInteger)followCode andViewController:(UIViewController*)vControl andTest:(BOOL)bTest
{

//    if (response.pRetCode == 73001) {
//        [WpCommonFunction messageBoxWithMessage:@"密码错误"];
//        
//    }
//    
//    if (response.pRetCode == 73003)
//    {
//        [WpCommonFunction messageBoxWithMessage:@"失败次数过多，锁定一小时"];
//    }
//    if(response.pRetCode == 73004){
//        [WpCommonFunction messageBoxWithMessage:@"建议升级"];
//}
//
//    if (response.pRetCode == 79008) {
//        [WpCommonFunction messageBoxWithMessage:@"密码错误"];
//    }
    
//    if (response.pRetCode != 0) {
//        [WpCommonFunction messageBoxWithMessage:response.pRetString];
//    }
    
    /** 余额不足 */
//    if([response.pRetCode isEqualToString:@"20052"])
//    {
//        [WpCommonFunction messageBoxTwoButtonWithMessage:NSLocalizedString(@"CPBOrderOptionController_alert_buy_msg", nil) andTitle:nil andLeftButton:NSLocalizedString(@"common_alert_btn_cancel", nil) andRightButton:NSLocalizedString(@"CPBOrderOptionController_alert_buy_ok", nil) andTag:ALERT_MONEY_NOT_ENOUGH_20052 andDelegate:self];
//        _preServiceCallStatus = QU_SERVICE_BACK_FAIL;
//        return _preServiceCallStatus;
        
//        _preServiceCallStatus = QU_SERVICE_BACK_OK;
//        return _preServiceCallStatus;
//    }
    
    //剥离底层错误判断 放入AppMock中
    _preServiceCallStatus = QU_SERVICE_BACK_OK;
    return _preServiceCallStatus;
    
}



- (QUServiceState)serviceCallBackFromApp:(QUNetResponse*)response andShowMessage:(BOOL)bShow
{
    return [self serviceCallBack:response andShowMessage:bShow andFollow:-1 andViewController:nil andTest:NO];
}




@end
