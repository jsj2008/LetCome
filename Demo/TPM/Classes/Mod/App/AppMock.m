//
//  AppMock.m
//  QianFangGuJie
//
//  Created by  rjt on 15/5/8.
//  Copyright (c) 2015年 JYZD. All rights reserved.
//

#import "AppMock.h"
#import "QUNetAFAdaptor.h"
#import "WpCommonFunction.h"
#import "NSString+MD5.h"

@implementation AppParam

@end

@implementation AppMock


-(void)run:(QUMockParam *)param isAlert:(BOOL)isalert{
    isAlert = isalert;
    [super run:param];
}

-(void)run:(QUMockParam *)param ignoreError:(BOOL)ignoreError{
    self.filterErrorCodes = @[@IGNORE_ERROR_CODE];
    [super run:param];
}


-(QUNetAdaptor *)getAdaptor{
    return [QUNetManager createAdaptor:[QUNetAFAdaptor class]];
}

-(NSDictionary *)getHeadersWithParam:(NSDictionary*)param{
    NSMutableDictionary *headDict = [[NSMutableDictionary alloc] init];
//    NSString* did = [WpCommonFunction getUniqueDeviceIdentifier];
//    NSString* sseionid = [getUserInfoManager getSessionID];
//    NSString* uid = [getUserInfoManager getUserID];
//    NSString* encryptedKey = [getUserInfoManager getEncryptedKey];
//    NSString *rtime = [NSString stringWithFormat:@"%.f",[NSDate date].timeIntervalSince1970];
//    //设置响应头字符串
//    [headDict setObject:did?did:@"" forKey:@"x-qfgj-did"];
//    [headDict setObject:sseionid?sseionid:@"" forKey:@"x-qfgj-sid"];
//    [headDict setObject:uid?uid:@"" forKey:@"x-qfgj-uid"];
//    [headDict setObject:rtime forKey:@"x-qfgj-rtime"];
//    
//    
//    NSString *userAgent = [NSString stringWithFormat:@"%@/v_%@ (%@; iOS %@; Scale/%0.2f)", [[NSBundle mainBundle] infoDictionary][(__bridge NSString *)kCFBundleExecutableKey] ?: [[NSBundle mainBundle] infoDictionary][(__bridge NSString *)kCFBundleIdentifierKey], [[NSBundle mainBundle] infoDictionary][@"CFBundleShortVersionString"] ?: [[NSBundle mainBundle] infoDictionary][(__bridge NSString *)kCFBundleVersionKey], [[UIDevice currentDevice] model], [[UIDevice currentDevice] systemVersion], [[UIScreen mainScreen] scale]];
//    [headDict setObject:userAgent forKey:@"x-qfgj-ua"];
//    //加签
//    NSMutableDictionary *tmpDict = [NSMutableDictionary dictionaryWithDictionary:param];
//    if ([tmpDict objectForKey:@"ID"]) {
//        [tmpDict setObject:[tmpDict objectForKey:@"ID"] forKey:@"id"];
//        [tmpDict removeObjectForKey:@"ID"];
//    }
//    NSArray* keyArray = [[tmpDict allKeys] sortedArrayUsingComparator:^NSComparisonResult(id  _Nonnull obj1, id  _Nonnull obj2) {
//        //        NSNumber *number1 = [[obj1 allKeys] objectAtIndex:0];
//        //        NSNumber *number2 = [[obj2 allKeys] objectAtIndex:0];
//        NSComparisonResult result = [obj1 compare:obj2];
//        
//        return result == NSOrderedDescending; // 升序
//        //        return result == NSOrderedAscending;  // 降序
//    }];
//    NSMutableString *mStr = [NSMutableString string];
//    for (NSString *key in keyArray) {
//        if (mStr.length>0) {
//            [mStr appendFormat:@"&"];
//        }
//        [mStr appendFormat:@"%@=%@",key,[tmpDict objectForKey:key]];
//    }
//    [mStr appendFormat:@"_%@_%@",encryptedKey?encryptedKey:@"",rtime];
//    //    NSLog(@"%@",mStr);
//    [headDict setObject:[mStr md5For16] forKey:@"X-QFGJ-SIGN"];
    return headDict;
}

//兼容老版本翻译网络连接加上前缀
-(NSString *)getOperatorTypeTranslate:(QUMockParam *)param{
    
    return [NSString stringWithFormat:@"%@%@",SERVER_URL_USER,[self getOperatorType]];
}

-(void)QUNetAdaptor:(QUNetAdaptor *)adaptor response:(QUNetResponse *)response{
    //[self filterError:response];
    if (self.returnBlock!=nil) {
        self.returnBlock(adaptor,response,self);
    }else{
        [self.delegate QUMock:self entity:response.pEntity];
    }
}


-(BOOL) filterError:(QUNetResponse *)response{//过滤错误，如果有错误则返回NO
//    if (![response.pSessionId isEqualToString:@""]) {
//        NSUserDefaults* sessionId = [NSUserDefaults standardUserDefaults];
//        [sessionId setObject:response.sessionId forKey:JYZD_SESSION_ID];
//    }
    //异地设备请求拦截
//    if ([self.operationType isEqualToString:@"/user/login_repeat"]) {
//        NSString *result = [[NSString alloc] initWithData:[request responseData]  encoding:NSUTF8StringEncoding];
//        if ([result rangeOfString:@"Y"].length) {
//            //                    exitParam* param = [exitParam param];
//            //                    exitMock* mock = [exitMock mock];
//            //                    [mock run:param];    //发出退出请求
//            
//            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//                [WpCommonFunction messageBoxWithMessage:@"系统检测到您已使用其他移动设备登录账号，请重新登录"];
//                [[ViewControllerManager sharedManager]launchLogin2ViewController:[ViewControllerManager sharedManager].currentController1];       //跳转登陆界面2
//            });
//            
//            
//        }
//    }
    
    BOOL flg = YES;
    
    BOOL filterFlg = NO;
    for (int i = 0; i < self.filterErrorCodes.count; i++) {
        NSNumber *code = self.filterErrorCodes[i];
        if (response.pRetCode == [code integerValue] || [code  isEqual: @IGNORE_ERROR_CODE]) {
            filterFlg = YES;
            break;
        }
    }
    if (response.pRetCode != NET_SUCCESS_CODE && !filterFlg && response.pRetCode != NET_ERROR_CODE_GET_CODE_FAIL) {
        if (response.pRetCode == NET_ERROR_CODE_SESSION_TIME_OUT ) {//session timeout,please re-login
            if (![[self getOperatorType] isEqualToString:@"/user/logoff"]){//防止退出时重复弹出登陆框
                if ([[self getOperatorType] isEqualToString:@"/user/login"]) {
                    [[NSNotificationCenter defaultCenter] postNotificationName:kToLogin object:nil];
                }
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//                    [AppMock launchSessionTimeoutViewController:[ViewControllerManager sharedManager].currentController1];       //跳转登陆界面2
                });
                flg =  NO;
            }
        }else{
            // 防止弹出多个alert
            if (response.pRetCode == AFNETWORKING_ERROR_CODE) {
                if (isAlert==YES) {
                    [WpCommonFunction messageBoxWithMessage:@"网络异常，请重试！"];
                }else{
                    [WpCommonFunction showNotifyHUDAtViewBottom:[[ViewControllerManager sharedManager] currentController1].view withErrorMessage:@"网络异常，请重试！"];
                }
            }else{
                if (isAlert==YES) {
                    [WpCommonFunction messageBoxWithMessage:response.pRetString];
                }else{
                    [WpCommonFunction showNotifyHUDAtViewBottom:[[ViewControllerManager sharedManager] currentController1].view withErrorMessage:response.pRetString];
                }
               
            }
            

            flg = NO;
        }

    }
    
    return flg;
}





@end
