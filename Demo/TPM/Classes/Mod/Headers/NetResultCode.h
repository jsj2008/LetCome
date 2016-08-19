//
//  ErrorCode.h
//  QianFangGuJie
//
//  Created by kinghy on 15/4/6.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#ifndef QianFangGuJie_ErrorCode_h
#define QianFangGuJie_ErrorCode_h

//简单的将数字转成字符串的方法
#define code2str(code) [NSString stringWithFormat:@"%d",code]

//忽略所有错误代码
#define IGNORE_ERROR_CODE                   -999999      

//登陆相关请求
#define NET_SUCCESS_CODE                    0            //请求成功

#define NET_DISCONNECT_CODE                 9999999       //网络异常

#define NET_ERROR_CODE_LOGIN_FAILED         79001        //登录失败。
#define NET_ERROR_CODE_VARIFYPIC_NEEDED     79002        //密码错误,失败次数过多，请输入验证码。
#define NET_ERROR_CODE_USER_LOCKED          79003        //您多次登录失败，账号已被锁定。
#define NET_ERROR_CODE_VARIFYPIC_OVERTIME   79007        //验证码超时。
#define NET_ERROR_CODE_PASSWORD_WRONG       79008        //密码错误。
#define NET_ERROR_CODE_UNKNOWN_ERROR        70014        //抱歉，系统发生了一个错误。
#define NET_ERROR_CODE_INTEGRAL_NOT_ENOUGH  30023        //可用积分余额不足
#define NET_ERROR_CODE_EXCHANGE_RATE_REFRESH  30037      //汇率不正确
#define NET_ERROR_CODE_GET_CODE_FAIL        110009       //获取期指沪金沪银code失败
#define NET_ERROR_CODE_DYAN_DELEGATE        30002        //尚未签署递延协议

#define NET_ERROR_CODE_UNSIGN_DELEGATE      74000        //尚未签署协议
#define NET_ERROR_CODE_UNSIGN_AGREEMENT     74016        //尚未签署合约

#define NET_ERROR_CODE_SESSION_TIME_OUT     110006      //登录超时


#endif
