//
//  Constant.h
//  CaoPanBao
//
//  Created by QDS on 14-12-9.
//  Copyright (c) 2014年 weihui. All rights reserved.
//

#ifndef CaoPanBao_Constant_h
#define CaoPanBao_Constant_h

// 宏定义
#if !defined(WP_DEBUG)
#define WPNSLOG(...) do {} while (0)
#define ISDEBUG false
#else
#define WPNSLOG(...) NSLog(__VA_ARGS__)
#define ISDEBUG true
#endif

// 底部tabbar的高度设置为49
#define KTabBarHeight 49.0
#define kCurrentDeviceWidth [UIScreen mainScreen].bounds.size.width
#define kCurrentDeciceHeight [UIScreen mainScreen].bounds.size.height

#define IOS_Version [[[UIDevice currentDevice] systemVersion] floatValue]

#define IOS7_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0)

#define IOS8_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
#define IOS9_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 9.0)

#define IOS6_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 6.0)


#define VERSION  [[[NSBundle mainBundle] infoDictionary] valueForKey:@"CFBundleShortVersionString"]
#define kAppName  [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)kCFBundleExecutableKey]
#define kAppDisplayName  [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)CFBundleDisplayName]
//Dock的背景图片修改
//#define kDockBackgroundImage @"tabbar_slider.png"
//Dock上button内如文字根图片站位比例尺寸
#define kTitleRatio 0.3
//Dock高度
#define kDockHight   49

//全局动画时长
#define kDurationTime 0.3
//dock
#define kTab_order_buy 0
#define kTab_order_sell 1
#define kTab_order_checkout 2
#define kTab_order_discover 3
#define kTab_order_mine 4


//手机规格

#define iPhone4 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640,960), [[UIScreen mainScreen] currentMode].size) : NO)

#define iPhone5 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 1136), [[UIScreen mainScreen] currentMode].size) : NO)

#define iPhone6 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(750, 1334), [[UIScreen mainScreen] currentMode].size) : NO)

#define iPhone6Plus ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(1242, 2208), [[UIScreen mainScreen] currentMode].size) : NO)

//iPhone6到5的宽高比
#define WIDTH6TO5 320/375
#define HEIGTH6TO5 480/667
//iphone6到6p
#define WIDTH6TO6P 414/375
#define HEIGTH6TO6P 736/667

//键盘延时出现时间
#define DelayShowKeyBoard 0.1

// 自定义RGB色值
#define Color_Bg_RGB(x, y, z) [UIColor colorWithRed:x/255.0f green:y/255.0f blue:z/255.0f alpha:1.0f]
#define Color_Bg_RGBA(x, y, z,a) [UIColor colorWithRed:x/255.0f green:y/255.0f blue:z/255.0f alpha:a]

//全局大按钮蓝色Color_Bg_RGB(89, 161, 246)
#define Color_Confirm_Blue Color_Bg_RGB(89.0f, 161.0f,  246.0f)
#define Color_Confirm_Blue_Highlighy Color_Bg_RGB(80.0f, 148.0f,  228.0f)
#define Color_Confirm_Gray Color_Bg_RGB(171.0f, 171.0f, 171.0f)//三级文字



//高亮文字
#define Color_HighLight_Orange Color_Bg_RGB(241.0f, 146.0f,  45.0f)

//递延错误提示
#define Color_DYAN_BG Color_Bg_RGB(129.0f, 129.0f,  129.0f)

//超级链接颜色
#define Color_Url_Blue Color_Bg_RGB(48.0f, 138.0f, 244.0f)//三级文字

// 全局颜色变量的宏定义，可参考规范图
#define Color_Bg_222222 Color_Bg_RGB(34.0f, 34.0f,  34.0f) //一级文字
#define Color_Bg_757575 Color_Bg_RGB(117.0f, 117.0f, 117.0f)//二级文字
#define Color_Bg_ababab Color_Bg_RGB(171.0f, 171.0f, 171.0f)//三级文字
#define Color_Bg_6a6a6a Color_Bg_RGB(106.0f, 106.0f, 106.0f)//分组文字
#define Color_Bg_007aff Color_Bg_RGB(0.0f, 122.0f, 255.0f)//全局蓝色
#define Color_Bg_efefef Color_Bg_RGB(239.0f, 239.0f, 239.0f)//背景灰
#define Color_Dock_Selected Color_Bg_RGB(35.0f, 189.0f, 222.0f)//全局蓝色

#define Color_Bg_484848 Color_Bg_RGB(72.0f, 72.0f, 72.0f)
#define Color_Bg_f2f2f2 Color_Bg_RGB(242.0f, 242.0f, 242.0f)
#define Color_Bg_000000 Color_Bg_RGB(0.0f,   0.0f,   0.0f)
#define Color_Bg_ef2121 Color_Bg_RGB(31.0f, 33.0f, 33.0f)
#define Color_Bg_333333 Color_Bg_RGB(51.0f,  51.0f,  51.0f)
#define Color_Bg_CustomPlaceholder Color_Bg_RGB(207.0f, 207.0f, 211.0f)
#define Color_Bg_Money  Color_Bg_RGB(251.f,136.f,1.f)
#define Color_Bg_Enable Color_Bg_RGB(18.f, 141.f, 226.0f) //按钮可点击时的背景颜色
#define Color_Bg_SelectBT [UIColor colorWithRed:196.f/255.f green:196.f/255.f blue:196.f/255.f alpha:1.f] // 选中按钮背景颜色

//#define Color_Up_Red Color_Bg_RGB(236.0f,86.0f, 122.0f)//涨红2
#define Color_Up_Red Color_Bg_RGB(255.0f,83.0f, 111.0f)//涨红3
//#define Color_Down_Green Color_Bg_RGB(15.0f, 190.0f, 172.0f)//跌绿2
#define Color_Down_Green Color_Bg_RGB(24.0f, 182.0f, 118.0f)//跌绿3
#define Color_Up_Red_Change Color_Bg_RGB(255.0f,173.0f, 186.0f)//涨红涨幅变动
#define Color_Down_Green_Change Color_Bg_RGB(119.0f, 219.0f, 179.0f)//跌绿跌幅变动

#define Color_DS_Gray Color_Bg_RGB(34.0f, 34.0f, 34.0f)//平灰
#define Color_Bg_Blue Color_Bg_RGB(157.0f, 208.0f, 249.0f)//成交量蓝色
#define Color_Bg_Black1 Color_Bg_RGB(34.0f, 34.0f, 34.0f)//盘口文字
#define Color_Bg_Black2 Color_Bg_RGB(66.0f, 66.0f, 66.0f)//盘口数量
#define Color_Bg_Gray Color_Bg_RGB(186.0f, 199.0f, 219.0f)//成交量
#define Color_Bg_GrayFont Color_Bg_RGB(135.0f, 135.0f, 135.0f)//成交量数字0
//#define Color_Time_Line Color_Bg_RGB(100.0f, 132.0f, 180.0f)//分时折线色
//#define Color_Time_Fill Color_Bg_RGBA(143.0f, 189.0f, 255.0f, 0.4f)//分时填充色

#define Color_Time_Line Color_Bg_RGB(10.f,130.f, 250.f)//分时折线色
#define Color_Time_Fill Color_Bg_RGBA(10.f,130.f, 250.f, 0.4f)//分时填充色
#define Color_Time_VOL_BG Color_Bg_RGB(140.f,145.f, 150.f)//买量背景色
//前端面试题2016.03.15.docx

#define Color_Section_Disabled Color_Bg_RGB(245.0f, 245.0f, 245.0f)//section不能点击

#define Color_Bg_Gray_Line Color_Bg_RGB(197.0f, 197.0f, 197.0f)//Tab分割线
#define Color_Btn_Disabled Color_Bg_RGB(197.0f, 197.0f, 197.0f)//按钮不可点标准色

#define Color_Btn_Border Color_Bg_RGB(189.0f,189.0f,189.0f)
#define Color_Btn_Selected_Bg Color_Bg_RGB(248.0f,95.0f,119.0f)
#define Color_Btn_Enabled_Bg Color_Bg_RGB(255.0f,149.0f,0.0f)
#define Color_Button_Title Color_Bg_RGB(119.0f,119.0f,119.0f)
#define Color_Btn_Disable Color_Bg_RGB(221.0f,221.0f,221.0f)

#define Color_Bg_Btn_Blue Color_Bg_RGB(204.0f, 228.0f, 255.0f)
#define Color_Bg_Text_Chosed_Blue Color_Bg_RGB(35.0f, 189.0f, 222.0f)
#define Color_Bg_Text_UnChosed_Blue Color_Bg_RGB(117.0f, 117.0f, 117.0f)
#define Color_Bg_Text_UnChosed_Blue2 Color_Bg_RGB(107.0f, 129.0f, 162.0f)
#define QU_BLACK_COLOR_TABLE_BG [UIColor blackColor]

#define kColorBgBtnDisabled Color_Bg_RGB(224, 224, 224)
#define kColorTextBtnDisabled Color_Bg_RGB(190, 190, 190)

#define Color_Bg_Headimg Color_Bg_RGB(175.0f, 218.0f, 255.0f)

//额度背景
#define Color_Bg_Qoute Color_Bg_RGB(100.0f, 184.0f, 255.0f)
#define Color_Used_Qoute Color_Bg_RGB(225.0f, 225.0f, 225.0f)

// 友盟APPKey
#define UMENG_APPKEY @"5357353e56240b2f810855c1"



/** 弹出验证手势密码页的时间间隔 */
// 生产时间为10，其他为1
#if defined(WP_TEST_SERVER) // 测试环境
#define kShowGesturesViewIntervalTime 1
#elif defined(WP_DEVELOP_SERVER) // 开发环境
#define kShowGesturesViewIntervalTime 1
#elif defined(WP_LINKTEST_SERVER) // 联调测试环境
#define kShowGesturesViewIntervalTime 1
#else // 生产环境
#define kShowGesturesViewIntervalTime 10
#endif


/** AppDelegate的弘 */
#define  kShareAppDelegate ((AppDelegate *)[UIApplication sharedApplication].delegate)
//#define  kGetProductionName ([[NSUserDefaults standardUserDefaults] objectForKey:PRODUCTIONTYPE])

// App Store Link
#define AppStoreLink                @"http://itunes.apple.com/cn/app/id859903036?mt=8"

// 支付密码所包含字符的正则表达式
#define CPB_Regex_PayPassword @"^[A-Za-z0-9]+$"

// 身份证校验规则
#define CPB_Regex_SfzID @"^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$"

// 银行卡号校验规则 15~25位数字
#define CPB_Regex_BankCardNO @"^[0-9\\-]{15,25}$"

// 金额格式
#define PRICE_REGEX_STRING  @"^[0-9]+(.[0-9]{1,2})?$|^[0-9]*$"

//锁屏时间
#define CLOSETIME 15*60
//#define CLOSETIME 1
//手势锁屏时间
#define GESTURECLOSETIME 5*60
//#define GESTURECLOSETIME 1

// long to string
#define fffLongToString(x) [NSString stringWithFormat:@"%ld",x]

// 获取本地化字符
#define fLocalStr(LocaledString)  NSLocalizedString(LocaledString, nil)



/** 获取本地化字符,带1个参数
 key 键名(localizable.string 中常量)
 value 键值（变量)
 */
#define fLocalStr1(key,a) [NSString stringWithFormat:NSLocalizedString(key, nil),a]

/** 获取本地化字符，带2个参数
 key 键名(localizable.string 中常量)
 value 键值（变量1)
 value 键值（变量2)
 */
#define fLocalStr2(key,a,b) [NSString stringWithFormat:NSLocalizedString(key, nil),a,b]


#define QU_BLACK_COLOR_LINE [UIColor colorWithRed:221.f/255.f green:221.f/255.f blue:221.f/255.f alpha:1.f] // 水平线颜色
#define LOGIN_HEAD_GRAY [UIColor colorWithRed:209.f/255.f green:209.f/255.f blue:209.f/255.f alpha:1.f]
#define CPB_HEAD_GRAY [UIColor colorWithRed:181.f/255.f green:181.f/255.f blue:181.f/255.f alpha:1.f]


//cell选中时的背景色
#define CPB_COLOR_CELL_BG_SELECTED2 [UIColor colorWithRed:28.f/255.f green:32.f/255.f blue:31.f/255.f alpha:1]
//忽略的版本号
#define IGNOREVERSION @"ignoreVersion"



#define GRAY_LINE_COLOR [UIColor colorWithRed:223/255.0 green:223/255.0 blue:223/255.0 alpha:1.0f]
#define CPB_COLOR_1 [UIColor colorWithRed:230.f/255.f green:8.f/255.f blue:30.f/255.f alpha:1] //
#define CPB_COLOR_2 [UIColor colorWithRed:248.f/255.f green:182.f/255.f blue:45.f/255.f alpha:1]
#define CPB_COLOR_5 [UIColor colorWithRed:110.f/255.f green:185.f/255.f blue:43.f/255.f alpha:1]
#define CPB_COLOR_9 [UIColor colorWithRed:153.f/255.f green:153.f/255.f blue:153.f/255.f alpha:1]
#define CPB_COLOR_10 [UIColor colorWithRed:221.f/255.f green:221.f/255.f blue:221.f/255.f alpha:1]
#define CPB_COLOR_11 [UIColor colorWithRed:204.f/255.f green:204.f/255.f blue:204.f/255.f alpha:1]
#define FIVE_LINE_COLOR [UIColor colorWithRed:100.f/255.f green:100.f/255.f blue:100.f/255.f alpha:1]
#define TEN_LINE_COLOR [UIColor colorWithRed:255.f/255.f green:199.f/255.f blue:59.f/255.f alpha:1]
#define TWENTY_LINE_COLOR [UIColor colorWithRed:50.f/255.f green:171.f/255.f blue:246.f/255.f alpha:1]
#define SIXTY_LINE_COLOR  [UIColor colorWithRed:194.f/255.f green:90.f/255.f blue:255.f/255.f alpha:1]


#define BACK(block) dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), block)
#define MAIN(block) dispatch_async(dispatch_get_main_queue(),block)

#define kChangeTabbarIndex @"kChangeTabbarIndex" //tabbar改变通知
#define kChangeBuyViewIndex @"kChangeBuyViewIndex" //productBuyViewController改变index通知

#define kBecomeBackground @"kBecomeBackground" //进入后台
#define kBeEnterAction @"kBeEnterAction" //进入前台
#define kDockItemChanged @"kDockItemChanged" //Dock切换

//弱引用self
#define DEFINED_WEAK_SELF __weak typeof(self) _self = self;

#define kTimeoutSecondes 30.f
#define AFNETWORKING_ERROR_CODE             9999999      //AFNetworking错误

#endif
