//
//  WpCommonFunction.h
//  WeiboPaySdkLib
//
//  Created by Mark on 13-3-18.
//  Copyright (c) 2013年 Mark. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#define VERSIONFILE_UPDATE              @"update"

#pragma mark - WpCommonFunction

@interface WpCommonFunction : NSObject

#pragma mark - 版本升级

+ (NSString*)JsonStringFromDict:(NSDictionary*)dict andKey:(const NSString*)key;
// 得到目录在Document目录下的路径（修改为将文件保存到Cache目录）
+ (NSString*)getFolderPathInDocument:(NSString*)folderPath;
// 得到文件在Document目录下的路径
+ (NSString*)getFilePathInDocument:(NSString*)folderPath withFileName:(NSString*)fileName andFileType:(NSString*)fileType;

// 将数字转化为带有2位小数的标准浮点型字符串
+ (NSString *) transformStringTo2PointFloatString:(NSString *)floatString;

//获取当前时间的13位时间戳
+ (NSString *)getNowData;
// 将豪秒转化为标准时间
+ (NSString *)dateFromMilliscondServer:(NSString*)dateServer;
// 按豪秒返回当前的时间 年-月
+ (NSString *) monthFromMilliSecondServer:(NSString *)dateServer;
// 将秒转化为标准时间
+ (NSString *)dateFromScondServer:(NSString*)dateServer;
// 按秒返回当前的月份
+ (int) monthFromSecondServer:(NSString *)dateServer;

// 移除字符串中所有的空格
+ (NSString*)stringRemoveAllWhiteSpace:(NSString*)str;
// 判断字符串是否为空
+ (BOOL)isStringNull:(NSString*)str;
// 使用正则表达式校验字符串
+ (BOOL)testRegex:(NSString*)regex WithString:(NSString*)str;
// 判断字符是否全部相同
+ (BOOL) isSameWithAllChar:(NSString *)targetString;
// 判断字符串是否为连续的数字
+ (BOOL) isContinuousNumber:(NSString *)numString;

// 检查新支付密码的格式
+ (BOOL)verifyNewPaymentPassword:(NSString*)password;

// 金额大小比较
+ (NSComparisonResult)comparePrice1:(NSString*)price1 andPrice2:(NSString*)price2;

// 得到WeiboPay设备唯一标识
+ (NSString*)getUniqueDeviceIdentifier;

// 获得设备标识
+ (NSString*)macaddress;


// alert message
// 打开一个警告对话框
+ (void)messageBoxOneButtonWithMessage:(NSString*)message andTitle:(NSString*)title andButton:(NSString*)buttonTitle andTag:(NSInteger)tag andDelegate:(id)delegate;
+ (void)messageBoxWithMessage:(NSString*)message;
+ (void)messageBoxOneButtonWithMessage:(NSString*)message andTag:(NSInteger)tag andDelegate:(id)delegate;
+ (void)messageBoxOneButtonWithMessage:(NSString*)message andTitle:(NSString*)title andTag:(NSInteger)tag andDelegate:(id)delegate;
+ (void)messageBoxTwoButtonWithMessage:(NSString*)message andTitle:(NSString*)title andLeftButton:(NSString*)leftButtonTitle andRightButton:(NSString*)rightButtonTitle andTag:(NSInteger)tag andDelegate:(id)delegate;
+ (void)messageBoxTwoButtonWithMessage:(NSString*)message andTag:(NSInteger)tag andDelegate:(id)delegate;
+ (void)messageBoxTwoButtonWithMessage:(NSString*)message andTitle:(NSString*)title andTag:(NSInteger)tag andDelegate:(id)delegate;
+ (void)messageBoxTwoButtonWithMessage:(NSString*)message andTag:(NSInteger)tag andDelegate:(id)delegate andPayload:(id)payload;
+ (void)messageBoxTwoButtonWithMessage:(NSString*)message andTitle:(NSString*)title andLeftButton:(NSString*)leftButtonTitle andRightButton:(NSString*)rightButtonTitle andTag:(NSInteger)tag andDelegate:(id)delegate andPayload:(id)payload;
+ (BOOL)isShowMessageBox;

// 设置圆角
+ (void)setView:(UIView*)view cornerRadius:(CGFloat)radius color:(CGColorRef)color borderWidth:(CGFloat)width;

#pragma mark - 获取mobile + avatar数据
+ (NSArray *)getLocalAccountMobile;
// 根据手机号得到（mobileNo + avatarImage）数据
+ (NSArray *)getLocalDataWithMobileNo:(NSString *)mobileNo;
// 根据memberID得到（mobileNo + avatarImage）数据
+ (NSArray *)getLocalDataWithMemberId:(NSString *)memberId;
// 获取保存在第一个的手机号
+ (NSArray *)getLocalDataWithFirst;
// 将数据保存在本地（string）
+ (void) saveAccountMobileToLocalData:(NSString *)mobileString :(NSString *)avatarString;
// 删除数据(string)
+ (void) deleteAccountMobileFromLocalData:(NSString *)mobileString;


// 保存memberID和Cookies值
+ (NSArray *)getCookiesAndMemberIDFromLocal;
// 删除memberID和Cookies值(退出登录是)
+ (void) delegateCookiesAndMemberIDFromLocal;

#pragma mark - 判断用户是否查看了引导页
+ (BOOL)getDeviceDoesLookoverGuidePageFromLocal;

+ (void) saveDeviceLookoverGuidePageToLocal;


// 隐藏键盘
+ (void)hideKeyboardWithScrollView:(UIScrollView*)scrollView;


/** 自定义alert弹出动画*/
+(void)animationForAlertShow:(UIView *)view;

/** 使手机号码中间四位变成星号*/
+(NSString *)makePhoneNumSecurity:(NSString *)phone;

/** 拨打客服电话*/
+(void)telPhoneToCustomerService;

/** 将图片转为Base64字符串（头像上传） */
+(NSString *)transformImageDataToBase64String:(NSData *)imagedata;

/** 将Base64字符串转成图片（图片下载） */
+(NSData *)transformBase64StringToImageData:(NSString *)base64Str;


/** 校验输入金额格式是否正确并改变按钮是否可按 -- 可输入小数点及小数点后两位*/
+(BOOL)textMoneyTextAndButtonStatusContainRadixPoint:(UIButton*)button withAgree:(BOOL)isAgree andTextField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string;

/** 校验输入金额格式是否正确并改变按钮是否可按 -- 不可以输入小数点*/
+(BOOL)textMoneyTextAndButtonStatusWithOutRadixPoint:(UIButton*)button withAgree:(BOOL)isAgree andTextField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string;

/** 百分比转换成浮点型  如:75% --> 0.75 */
+(float)changePercentToFloat:(NSString*)percentStr;

/** 在视图中间显示浮层 */
+(void)showNotifyHUDAtViewCenter:(UIView*)view withErrorMessage:(NSString *)errorMessage withTextFiled:(UITextField*)textField;

/** 在视图中间显示浮层 */
+(void)showNotifyHUDAtViewCenter:(UIView*)view  withErrorMessage:(NSString *)errorMessage;

/** 在视图底部显示浮层 */
+(void)showNotifyHUDAtViewBottom:(UIView*)view  withErrorMessage:(NSString *)errorMessage;

// 生成规格化的手机号
+ (NSString*)generateMobileNoCode:(NSString*)str;

/** uicolor 转 uiimage*/
+ (UIImage*)createImageWithColor: (UIColor*) color;

/** 将手机号码字符串转成3-4-4格式 */
+ (NSString*)makePhoneNumStrFormat: (NSString*) phoneStr;

+(NSString*)changeDateFomat:(NSString*)date DateMark:(NSString*)mark;

/** 当前时间是否在给定区间内,24小时制,时间区间必须在同一天内，如:09:00至17:00 **/
+(BOOL)isTime:(NSString*)time between:(NSString *)fromdate and:(NSString *)todate;


#pragma mark - 数字增加逗号
+(NSString*)MoneyFormat:(NSString*)money;

#pragma mark - 日期加小数点
+(NSString*)DateFormat:(NSString*)date;

//获取IOS机型
+(NSString*)machineName;
//获取IOS机型编号
+(int)machineKind;

@end
