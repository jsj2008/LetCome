//
//  SeverUrl.h
//  QianFangGuJie
//
//  Created by  rjt on 15/5/19.
//  Copyright (c) 2015年 JYZD. All rights reserved.
//

#ifndef QianFangGuJie_SeverUrl_h
#define QianFangGuJie_SeverUrl_h
//绑定拉钩宝地址 模拟期指地址

#if defined(WP_TEST_SERVER) // 测试环境
//拉钩宝绑定地址
#define H5_URL @"http://192.168.6.75:8005"
#define SERVER_URL_USER @"http://192.168.4.105:8080"
#define SERVER_URL_PROD @"http://192.168.6.75:8005"

#define CUSTOMER_SERVICE_ADDRESS @"http://192.168.6.75:8005/callcenter/callService?session_id="

//#define HQ_URL_DAY @"http://hq.yuncaopan.com.cn"
//#define HQ_URL_NIGHT @"http://hq.yuncaopan.com.cn"
#define HQ_URL_DAY @"http://192.168.6.66:2050"
#define HQ_URL_NIGHT @"http://192.168.6.66:2050"

#define CPB_LOGIN_URL @"http://hclwap.yuncaopan.com.cn/user/login"
#define CPB_REG_URL @"http://hclwap.yuncaopan.com.cn/regsuccess/gounionreg"

#elif defined(WP_WLANTEST_SERVER) // 测试环境
//拉钩宝绑定地址
#define H5_URL @"http://115.159.194.244:8080"
#define SERVER_URL_USER @"http://115.159.194.244:8080/LetCome"
#define SERVER_URL_PROD @"http://192.168.6.73:8005"

#define CUSTOMER_SERVICE_ADDRESS @"http://192.168.6.113:8005/callcenter/callService?session_id="

#define HQ_URL_DAY @"http://192.168.6.66:2050"
#define HQ_URL_NIGHT @"http://192.168.6.66:2050"

#define CPB_LOGIN_URL @"http://hclwap.yuncaopan.com.cn/user/login"
//#define CPB_LOGIN_URL @"http://192.168.6.113:8210/user/login"
#define CPB_REG_URL @"http://hclwap.yuncaopan.com.cn/regsuccess/gounionreg"

#elif defined(WP_LINKTEST_SERVER) // 外网环境

//拉钩宝绑定地址
#define H5_URL @"http://180.168.176.2:8005"
#define SERVER_URL_USER @"http://180.168.176.2:8116"
#define SERVER_URL_PROD @"http://180.168.176.2:8115"

#define CUSTOMER_SERVICE_ADDRESS @"http://180.168.176.2:8005/callcenter/callService?session_id="

#define HQ_URL_DAY @"http://hq.yuncaopan.com.cn"
#define HQ_URL_NIGHT @"http://hq.yuncaopan.com.cn"

#define CPB_LOGIN_URL @"http://hclwap.yuncaopan.com.cn/user/login"
#define CPB_REG_URL @"http://hclwap.yuncaopan.com.cn/regsuccess/gounionreg"

#elif defined(WP_DEBUG) //fake环境

//拉钩宝绑定地址
//#define H5_URL2 @"http://qfgjh5.wcp.sina.com:8005"
#define H5_URL @"http://192.168.6.75:8005"
#define SERVER_URL_USER @"http://192.168.4.105:8080/fake/rjc"
#define SERVER_URL_PROD @"http://192.168.4.105:8080/fake/rjc"

#define CUSTOMER_SERVICE_ADDRESS @"http://192.168.6.75:8005/callcenter/callService?session_id="

#define HQ_URL_DAY @"http://hq.yuncaopan.com.cn"
#define HQ_URL_NIGHT @"http://hq.yuncaopan.com.cn"
#define CPB_LOGIN_URL @"http://hclwap.yuncaopan.com.cn/user/login"
#define CPB_REG_URL @"http://hclwap.yuncaopan.com.cn/regsuccess/gounionreg"

#else // 生产环境
//拉钩宝绑定地址
#define H5_URL @"http://hclwap.yuncaopan.com.cn"

#define SERVER_URL_USER @"http://hcluser.yuncaopan.com.cn"
#define SERVER_URL_PROD @"http://hclpro.yuncaopan.com.cn"

#define CUSTOMER_SERVICE_ADDRESS @"http://a50.sina.com/callcenter/callService?session_id="

#define HQ_URL_DAY @"http://hq.yuncaopan.com.cn"
#define HQ_URL_NIGHT @"http://hq.yuncaopan.com.cn"
#define CPB_LOGIN_URL @"http://hclwap.yuncaopan.com.cn/user/login"
#define CPB_REG_URL @"http://hclwap.yuncaopan.com.cn/regsuccess/gounionreg"

#endif

#endif
