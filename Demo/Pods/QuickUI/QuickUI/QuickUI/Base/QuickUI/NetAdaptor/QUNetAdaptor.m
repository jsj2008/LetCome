//
//  QUNetowrkAdaptor.m
//  CaoPanBao
//
//  Created by zhuojian on 14-5-27.
//  Copyright (c) 2014年 weihui. All rights reserved.
//

#import "QUNetAdaptor.h"
#import "QUNetAFAdaptor.h"
#import "WpBaseAdapter.h"
#import "QUJsonParse.h"
#import "WpGlobalOption.h"
#import "QUNetResponse.h"

@implementation QUNetAdaptor
-(void)request:(QUMockParam *)params{
    WpBaseAdapter* adaptor = [self createWpAdaptor];
    
    adaptor.timeOutRequest=self.delayTimeOut;
    
    QUJsonParse* parse=[[QUJsonParse alloc] init];
    adaptor.params=[parse dictionaryFromObjc:params];
    adaptor.operationType=params.operationType;
    adaptor.sendMethod = params.sendMethod;
    if ([self.delegate respondsToSelector:@selector(getHeadersWithParam:)]) {
        adaptor.headers = [NSMutableDictionary dictionaryWithDictionary:[(QUMock*)self.delegate getHeadersWithParam:adaptor.params]];
    }else if([self.delegate respondsToSelector:@selector(getHeaders)]){//兼容老版本
        adaptor.headers = [NSMutableDictionary dictionaryWithDictionary:[(QUMock*)self.delegate getHeaders]];
    }

//    if([adaptor respondsToSelector:@selector(runWithoutOperation)]){
        [adaptor runWithoutOperation];
//    }else{
//        [[WpGlobalOption sharedOption] executeUrlOperation:adaptor];
//    }
}


//-(void)syncRequest:(QUMockParam *)params{
//    WpBaseAdapter* adaptor = [self createWpAdaptor];
//    
//    adaptor.timeOutRequest=self.delayTimeOut;
//    
//    QUJsonParse* parse=[[QUJsonParse alloc] init];
//    adaptor.params=[parse dictionaryFromObjc:params];
//    adaptor.operationType=params.operationType;
//    adaptor.sendMethod = params.sendMethod;
//    if ([self.delegate respondsToSelector:@selector(getHeadersWithParam:)]) {
//        adaptor.headers = [NSMutableDictionary dictionaryWithDictionary:[(QUMock*)self.delegate getHeadersWithParam:adaptor.params]];
//    }else if([self.delegate respondsToSelector:@selector(getHeaders)]){//兼容老版本
//        adaptor.headers = [NSMutableDictionary dictionaryWithDictionary:[(QUMock*)self.delegate getHeaders]];
//    }
//    if([adaptor respondsToSelector:@selector(syncRun)]){
//        WpResponse * response = [adaptor syncRun];
//        [self responseSync:response];
//    }
//}

#pragma mark - WPBaseAdaptor delegate
-(QUNetResponse *)responseSync:(WpResponse *)wpResponse{
    QUNetResponse* response=[[QUNetResponse alloc] init];
    response.pAdapter=self;
    response.pData=wpResponse.data;
    response.pErrorData=wpResponse.errorData;
    response.pRetCode=wpResponse.retCode;
    response.pRetString=wpResponse.retString;
    response.pJsonBody=wpResponse.jsonBody;
    response.pRetServerTime=wpResponse.retServiceTime;
    response.pSessionId = wpResponse.sessionId;
    
    QUMock* mock=(QUMock*)self.delegate;
    
    response.pAdapter.operationType=[mock getOperatorType];
    //兼容AFNetWorking 此步注释
    //response.pReason=[[WpGlobalOption sharedOption] serviceCallBackFromApp:response andShowMessage:YES];
    response.pReason = QU_SERVICE_BACK_OK;
    
    Class cls=[mock getEntityClass];
    
    if(cls)
    {
        if(response.pReason == QU_SERVICE_BACK_OK)
        {
            BOOL normalJson = NO;
            
            NSData *data = [response.pJsonBody dataUsingEncoding:NSUTF8StringEncoding];
            if (data) {
                id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:nil];
                if ([json isKindOfClass:[NSDictionary class]]) {
                    for (NSString *key in ((NSDictionary *)json).allKeys) {
                        if([self isNumText:key]){
                            normalJson = YES;
                            break;
                        };
                    }
                }
                
                if (normalJson) {
                    if ([json isKindOfClass:[NSDictionary class]] ) {
                        Class className = mock.getEntityClass;
                        id calss = [[className alloc] init];
                        NSDictionary *rootDict = nil;
                        QUEntity *en = (QUEntity*)calss;
                        if ([en respondsToSelector:@selector(parseJson:)]) {
                            response.pJsonBody = [en parseJson:json];
                        }else{
                            response.pJsonBody = [rootDict JSONString];
                        }
                    }
                }
                
                response.pEntity=[[[QUJsonParse alloc] init] objFromString:response.pJsonBody withClass:cls withMetmod:[mock getAliasName]];
                
            }
            
        }
    }
    
    
    mock.response=response;
    [mock filterError:response];
    return response;
}


#pragma mark - WPBaseAdaptor delegate
-(void)responseCallback:(WpResponse *)wpResponse
{
    QUNetResponse *response  = [self responseSync:wpResponse];
    [self.delegate QUNetAdaptor:self response:response];
   
    QUMock* mock=(QUMock*)self.delegate;
    
    if(mock.waitView)
        [[ViewControllerManager sharedManager] hideWaitView]; // 网络请求结束，关闭提示
    
}

//是否是纯数字
- (BOOL)isNumText:(NSString *)str{
    NSString * regex        = @"^([0-9])+$";
    NSPredicate * pred      = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    BOOL isMatch            = [pred evaluateWithObject:str];
    if (isMatch) {
        return YES;
    }else{
        return NO;
    }
    
}

@end
