//
//  WpAFAdapter.m
//  QianFangGuJie
//
//  Created by kinghy on 15/6/20.
//  Copyright (c) 2015年 JYZD. All rights reserved.
//

#import "WpAFAdapter.h"
#import "Constant.h"
#import "WpCommonFunction.h"
#import "WpGlobalOption.h"
#import "WHStringHelper.h"
#include "AFNetworking.h"

#pragma mark - WpBaseAdapter

@interface WpAFAdapter ()
{
    id target;
    SEL selector;
    BOOL isSync;
    
}
@property(nonatomic,strong)NSString* body;
- (void)toCallback;
- (void)getRequestFinish:(id)responseObj;
- (void)getRequestError:(NSError*)error;

@end

@implementation WpAFAdapter



- (id)initWithTarget:(id)_target selector:(SEL)_selector
{
    self = [super init];
    if (self)
    {
        target = _target;
        selector = _selector;
        
        response = [[WpResponse alloc] init];
        response.adapter = self;
        
        paramDict = [[NSMutableDictionary alloc] init];
        
        needHttpScheme=[[NSMutableDictionary alloc] initWithCapacity:10];
        
        /** 以下接口，需采用http */
        //        needHttpScheme=@{
        //                         @"HHQ":@"1",
        //                         @"HHQCHART":@"1",
        //                         @"HSTATE":@"1",
        //                         @"HTradeState":@"1"
        //                         };
        
    }
    return self;
}

- (id)initWithResponse:(ResponseBlock)_responseBlock
{
    self = [super init];
    if (self)
    {
        responseBlock =_responseBlock;
        
        response = [[WpResponse alloc] init];
        response.adapter = self;
        
        paramDict = [[NSMutableDictionary alloc] init];
    }
    return self;
}

- (void)dealloc
{
    [self removeTarget];
}

- (void)removeTarget
{
    target = nil;
    responseBlock = nil;
}

//- (NSString*)getOperationType
//{
//    return @"";
//}

//- (void)getPostBody
//{
//
//}

//- (void)getBigPostBody
//{
//
//}

- (void)parseResponseRoot:(NSDictionary*)root
{
    response.data=root;
}

- (void)parseErrorResponseRoot:(NSDictionary*)root
{
    response.errorData=root;
    //    id codeError=
}

//- (double)getTimeOutSeconds
//{
//    return 30.0;
//}

/**
 *  产品域名：openpro.jyzd.sina.com   对应96 8115
 用户域名：openuser.jyzd.sina.com    对应96 8116
 http://hqn.jyzd.sina.com/    晚上行情，黄金白银
 http://hqd.jyzd.sina.com/    早上行情，股票期指
 
 */

- (BOOL)isShowContent
{
    return YES;
}


//
//- (void)main
//{
//    isSync = NO;
//    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
//    requestSerializer.timeoutInterval = self.timeOutRequest;
////    requestSerializer.timeoutInterval = kTimeoutSecondes;;
//    NSDictionary *dt = self.headers;
//    if (dt) {
//        NSArray *keys = [dt allKeys];
//        for (int i=0; i<keys.count; ++i) {
//            [requestSerializer setValue:dt[keys[i]] forHTTPHeaderField:keys[i]];
//        }
//    }
//    NSString *strUrl = self.operationType;
//    strUrl = [strUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//    //讲大写ID转换为小写id
//    NSMutableDictionary *tmpdict = [NSMutableDictionary dictionaryWithDictionary:self.params];
//    if ([tmpdict objectForKey:@"ID"]) {
//        [tmpdict setObject:[tmpdict objectForKey:@"ID"] forKey:@"id"];
//        [tmpdict removeObjectForKey:@"ID"];
//    }
//    NSMutableURLRequest *request = [requestSerializer requestWithMethod:(self.sendMethod?[self.sendMethod uppercaseString]:@"POST") URLString:strUrl parameters:tmpdict error:nil];
//    
//    AFHTTPRequestOperation *requestOperation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
//    AFHTTPResponseSerializer *responseSerializer = [AFJSONResponseSerializer serializer];
//    responseSerializer.acceptableContentTypes = [NSSet setWithObject:@"text/html"];
//    
//    [requestOperation setResponseSerializer:responseSerializer];
//    [requestOperation start];
//    [requestOperation waitUntilFinished];
//    
//    //    [request setShouldContinueWhenAppEntersBackground:NO];
//    
//    // 保存cookie，恢复登录状态
//    //    [request setUseCookiePersistence:NO];
//    //    [request setRequestCookies:[NSMutableArray arrayWithArray:[WCFUser sharedUser].requestCookies]];
//    //
//    //    [request setRequestHeaders:headDict];
//    //    [request startSynchronous];
//    [self getRequestError:requestOperation];
//}

- (void)toCallback
{
    if (isSync == NO) {
        if(responseBlock)
        {
            dispatch_sync(dispatch_get_main_queue(), ^{
                responseBlock(response, self);
            });
        }
        else
        {
            if ([target respondsToSelector:selector])
            {
                [target performSelectorOnMainThread:selector withObject:response waitUntilDone:YES];
            }
        }
    }
}

- (void)getRequestFinish:(id)responseObj
{
    id content = responseObj;
    
    // 更新请求的cookies值
    //    [WCFUser sharedUser].requestCookies = request.responseCookies;
    
    if ([self isShowContent])
    {
        if (!([self.operationType isEqualToString:@"/v1/tpo/prices"] || [self.operationType isEqualToString:@"/v1/tpo/price"] || [self.operationType isEqualToString:@"/user/login_repeat"])) {
            

//            WPNSLOG(@"############\ncontent: %@\nsdk operationType: %@\ncookiesResponse: %@\ncookiesRequest: %@", content, self.operationType, request.responseCookies, request.requestCookies);

        }
    }
    
    @try
    {
        
        NSDictionary* rootDict = content;
        
        NSString *result = [content JSONString];
//        NSString *result = [[NSString alloc] initWithData:content  encoding:NSUTF8StringEncoding];
        if ([result rangeOfString:@"\"id\" :"].length) {
            result = [result stringByReplacingOccurrencesOfString:@"\"id\" :" withString:@"\"ID\" :"];   //将id转为ID
            NSData *newData = [result dataUsingEncoding:NSUTF8StringEncoding];
            rootDict = [newData objectFromJSONData];
        }
        
        if ([result rangeOfString:@"\"init_fund\" :"].length) {
            NSString *newString = [result stringByReplacingOccurrencesOfString:@"\"init_fund\" :" withString:@"\"Init_fund\" :"];   //将id转为ID
            NSData *newData = [newString dataUsingEncoding:NSUTF8StringEncoding];
            rootDict = [newData objectFromJSONData];
        }
        
        response.retCode = [[WpCommonFunction JsonStringFromDict:rootDict andKey:@"error_code"] intValue];
        response.retString = [WpCommonFunction JsonStringFromDict:rootDict andKey:@"error_msg"];
        response.sessionId = [WpCommonFunction JsonStringFromDict:rootDict andKey:@"session_id"];
        
        if (response.retCode){
            if([WHStringHelper isNilByValue:rootDict]==NO){
                response.jsonBody= [rootDict JSONString];
            }
            
            [self parseResponseRoot:rootDict];
        }
        else
        {
            
            if([WHStringHelper isNilByValue:rootDict]==NO){
                response.jsonBody= [rootDict JSONString];
            }
            
            [self parseErrorResponseRoot:rootDict];
        }
    }
    @catch (NSException* e){
        response.retCode = AFNETWORKING_ERROR_CODE;
        response.retString = NSLocalizedString(@"服务请求异常", @"");
    }
    @finally{
        
    }
    
    
    [self toCallback];
}



- (void)getRequestError:(NSError*)error{
    if (error)
    {
        WPNSLOG(@"error: %@", error);
        
        response.retCode = AFNETWORKING_ERROR_CODE;
        response.retString = error.description;
        response.errorData = error;
        
        [self toCallback];
    }
}

-(void)runWithoutOperation{
    isSync = NO;
    
    NSString *strUrl = self.operationType;
    strUrl = [strUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    //讲大写ID转换为小写id
    NSMutableDictionary *tmpdict = [NSMutableDictionary dictionaryWithDictionary:self.params];
    if ([tmpdict objectForKey:@"ID"]) {
        [tmpdict setObject:[tmpdict objectForKey:@"ID"] forKey:@"id"];
        [tmpdict removeObjectForKey:@"ID"];
    }
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.requestSerializer.timeoutInterval = self.timeOutRequest;;
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"text/html",@"text/plain",@"application/json", nil];
    NSDictionary *dt = self.headers;
    if (dt) {
        NSArray *keys = [dt allKeys];
        for (int i=0; i<keys.count; ++i) {
            [manager.requestSerializer setValue:dt[keys[i]] forHTTPHeaderField:keys[i]];
        }
    }
    
    if ([[self.sendMethod lowercaseString] isEqualToString:@"get"]) {
        
        [manager GET:strUrl parameters:tmpdict progress:^(NSProgress * _Nonnull downloadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            [self getRequestFinish:responseObject];
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            [self getRequestError:error];
        }];
    }else{
        [manager POST:strUrl parameters:tmpdict progress:^(NSProgress * _Nonnull downloadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            [self getRequestFinish:responseObject];
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            [self getRequestError:error];
        }];
    }
}


//
//
//- (WpResponse *)syncRun
//{
//    isSync = YES;
//    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
//    requestSerializer.timeoutInterval = self.timeOutRequest;
//    //requestSerializer.timeoutInterval = kTimeoutSecondes;;
//    NSDictionary *dt = self.headers;
//    if (dt) {
//        NSArray *keys = [dt allKeys];
//        for (int i=0; i<keys.count; ++i) {
//            [requestSerializer setValue:dt[keys[i]] forHTTPHeaderField:keys[i]];
//        }
//    }
//    NSString *strUrl = self.operationType;
//    strUrl = [strUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//    //讲大写ID转换为小写id
//    NSMutableDictionary *tmpdict = [NSMutableDictionary dictionaryWithDictionary:self.params];
//    if ([tmpdict objectForKey:@"ID"]) {
//        [tmpdict setObject:[tmpdict objectForKey:@"ID"] forKey:@"id"];
//        [tmpdict removeObjectForKey:@"ID"];
//    }
//    NSMutableURLRequest *request = [requestSerializer requestWithMethod:(self.sendMethod?[self.sendMethod uppercaseString]:@"POST") URLString:strUrl parameters:tmpdict error:nil];
//    
//    AFHTTPRequestOperation *requestOperation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
//    AFHTTPResponseSerializer *responseSerializer = [AFJSONResponseSerializer serializer];
//    responseSerializer.acceptableContentTypes = [NSSet setWithObject:@"text/html"];
//    
//    [requestOperation setResponseSerializer:responseSerializer];
//    [requestOperation start];
//    [requestOperation waitUntilFinished];
//    
//    [self getRequestError:requestOperation];
//    return  response;
//}



@end
