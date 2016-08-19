//
//  MyViewController.m
//  CaoPanBao
//
//  Created by zhuojian on 14-4-18.
//  Copyright (c) 2014年 Mark. All rights reserved.
//

#import "MyViewController.h"
#import "UIImage+RTTint.h"
#import "QUEntity.h"
#import "QUBLL.h"

#define kTagLeftArrowView 1  // 左边按钮箭头
#define kTagLeftLabelView 2  // 左边按钮标题

#define kNavRightButonOffsetIOS7 16 // 导航条右边按钮向右偏移值
#define kNavRightButonTextOffsetIOS7 8 // 导航条右边文字按钮向右偏移值
#define kNavLeftButonTextOffsetIOS7 10 // 导航条左边文字按钮向左偏移值
#define kNavLeftLabelTextOffsetIOS7 8 // 导航条左边文字按钮向左偏移值


#define kNavRightButonOffset IOS7_OR_LATER?kNavRightButonOffsetIOS7:0
#define kNavRightButonTextOffset IOS7_OR_LATER?kNavRightButonTextOffsetIOS7:0

#define kNavLeftButonTextOffset IOS7_OR_LATER?kNavLeftButonTextOffsetIOS7:0 // 导航条左边文字按钮向左偏移值
#define kNavLeftLabelTextOffset IOS7_OR_LATER?kNavLeftLabelTextOffsetIOS7:kNavLeftLabelTextOffsetIOS7+12 // 导航条左边文字按钮向左偏移值

#define kNavigationBarHighLightColor [UIColor colorWithRed:136.f/255.f green:136.f/255.f blue:136.f/255.f alpha:1.f]

@interface MyViewController () <SINavigationMenuDelegate>{
    UIImageView * titleImgView;
}
@property(nonatomic,strong)NSString* leftNormalImageName;
@end
@implementation MyViewController

+(instancetype)controller{
    NSString* nibName=NSStringFromClass([self class]);
    MyViewController* controller=[[self alloc] initWithNibName:nibName bundle:nil];
    
    
    return controller;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        
        [[UIApplication sharedApplication] setStatusBarHidden:NO];
    }
    self.blls = [NSHashTable weakObjectsHashTable];
    self.statusBarStyle = UIStatusBarStyleLightContent;//默认为白色
    return self;
}

- (void) loadView
{
    [super loadView];
    self.leftButtonType = kNav_Left_Button_Back;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBar.translucent = NO;
    
    //    [self initNavigationBar];
    //
    //    if(self.leftButtonType == kNav_Left_Button_Cancel)
    //        [self showCancelButton];
    //    else if (self.leftButtonType == kNav_Left_Button_None)
    //        self.navigationItem.hidesBackButton = YES;
    //    else
    //        [self showBackButton];
    
    [self initQuickUI];
    
    [self initQuickMock];
    
//    [self.navigationController.navigationBar setBackgroundImage:[BaseConfigureManager sharedManager].navBgImg forBarMetrics:UIBarMetricsDefault];
    UINavigationBar * bar = self.navigationController.navigationBar;
    bar.barTintColor = [BaseConfigureManager sharedManager].navBgColor;
    [self.navigationController.navigationBar setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:[UIColor whiteColor],NSForegroundColorAttributeName,[UIFont systemFontOfSize:18.f],NSFontAttributeName,nil]];
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];
}

-(UIImage *)imageWithR:(float)red G:(float)green B:(float)bule alpha:(float)alpha{
    CGSize imageSize = CGSizeMake(50, 50);
    UIGraphicsBeginImageContextWithOptions(imageSize, 0, [UIScreen mainScreen].scale);
    [[UIColor colorWithRed:red/255.0 green:green/255.0 blue:bule/255.0 alpha:alpha] set];
    UIRectFill(CGRectMake(0, 0, imageSize.width, imageSize.height));
    UIImage *pressedColorImg = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return pressedColorImg;
}

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    NSEnumerator *enumerator = [_blls objectEnumerator];
    id obj = nil;
    while (obj = [enumerator nextObject]) {
        if ([obj isKindOfClass:[QUBLL class]]) {
            QUBLL *bll = (QUBLL*)obj;
            [bll controllerDidAppear];
        }
    }
    
    if(self.title){
//        NSLog(@"viewDidAppear : %@",self.title);
//        [MobClick beginLogPageView:self.title];
    }else{
        NSLog(@"%@ cannot find title",[self class]);
    }
    
}
- (void)viewWillAppear:(BOOL)animated
{
//    NSLog(@"viewWillAppear : %@",self.title);
    [super viewWillAppear:animated];
    //
//    [[ViewControllerManager sharedManager]hideWaitView];
    // 若当前无用户登录数据，则不做定时器
//    kShareAppDelegate.topController = self;
    //    if ([self isKindOfClass:[IndexViewController class]] || [self isKindOfClass:[STOOrderViewController class]]) {
    //        kShareAppDelegate.nav.canDragBack = NO;
    //    }
    //    else
    //        kShareAppDelegate.nav.canDragBack = NO;
    //    if ([WpCommonFunction getCookiesAndMemberIDFromLocal])
    //    {
    //        if ([self isMemberOfClass:[WHDrawPswViewController class]])
    //        {
    //            // 当用户当前处于手势密码页，不做保存本地时间的更新
    //            [WCFGestureTimer sharedTimer].isInvalidateTimerMark = NO;
    //        }
    //
    //        // 初始化弹出手势密码的计时器
    //        [[WCFGestureTimer sharedTimer] createTime];
    //    }
    //
    
    // 显示状态栏
    [[UIApplication sharedApplication] setStatusBarStyle:self.statusBarStyle];
    [[UIApplication sharedApplication] setStatusBarHidden:NO];
    
    // 显示导航条
    [[self navigationController] setNavigationBarHidden:NO animated:NO];
    
    [self viewWillAppearTodoSomeThing];
    NSEnumerator *enumerator = [_blls objectEnumerator];
    id obj = nil;
    while (obj = [enumerator nextObject]) {
        if ([obj isKindOfClass:[QUBLL class]]) {
            QUBLL *bll = (QUBLL*)obj;
            [bll controllerWillAppear];
        }
    }
}


- (void)viewWillAppearTodoSomeThing {
    
}

- (void)viewDidDisappearTodoSomeThing {
    
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    NSEnumerator *enumerator = [_blls objectEnumerator];
    id obj = nil;
    while (obj = [enumerator nextObject]) {
        if ([obj isKindOfClass:[QUBLL class]]) {
            QUBLL *bll = (QUBLL*)obj;
            [bll controllerDidDisappear];
        }
    }
    [self viewDidDisappearTodoSomeThing];
    if(self.title){
//        NSLog(@"viewDidDisappear : %@",self.title);
//        [MobClick endLogPageView:self.title];
    }
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
// NSLog(@"viewWillDisappear : %@",self.title);
}


- (void) dealloc
{
    //  [[ViewControllerManager sharedManager] showWaitView:self.navigationController.view];
    
    //  [[ViewControllerManager sharedManager] hideWaitView];
    NSLog(@"%@ has delloced",self.class);
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)initNavigationBar{
    
    //    self.navigationController.navigationBar.translucent = NO;
    //    float version = [[[UIDevice currentDevice] systemVersion] floatValue];
    //
    //    UIImage *backgroundImage = nil;
    //    if (version >= 5.0 && version<7.0) {
    //        backgroundImage = [UIImage imageNamed:@"88"];
    //
    //        [self.navigationController.navigationBar setBackgroundImage:backgroundImage forBarMetrics:UIBarMetricsDefault];
    //        //        self.navigationController.navigationBar.backgroundColor = [UIColor whiteColor];
    //        [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleBlackOpaque];
    //    }
    //    else if(version>=7.0)
    //    {
    //        backgroundImage = [UIImage imageNamed:@"128"];
    //
    //        UINavigationBar* bar=self.navigationController.navigationBar;
    //        //        bar.backgroundColor = [UIColor whiteColor];
    //        [bar setBackgroundImage:backgroundImage forBarPosition:UIBarPositionTopAttached barMetrics:UIBarMetricsDefault];
    //        [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
    //    }
    [self setNavigationTitle];
}

-(void)setNavigationTitle{
    
    UIColor *titleColor = [UIColor whiteColor];
    
    UIFont  *titleFont = [UIFont boldSystemFontOfSize:18.f];
    
    NSDictionary *dict = [NSDictionary dictionaryWithObjectsAndKeys:titleColor,NSForegroundColorAttributeName,titleFont, NSFontAttributeName, nil];
    
    self.navigationController.navigationBar.titleTextAttributes = dict;
    
    //    if (self.navigationBarTitle) self.navigationItem.title = self.navigationBarTitle;
    
    self.navigationItem.title = self.navigationBarTitle ? self.navigationBarTitle :@"";
}


- (void) setNavigationTitle:(NSString *)stockName andCode:(NSString *)stockCode andImg:(UIImage*)img showImg:(BOOL)show{
    
    UILabel *titleLabel=[[UILabel alloc]initWithFrame:CGRectMake(0, -10, 100, 19)];
    titleLabel.text=stockName;
    [titleLabel setFont:[UIFont fontWithName:@"Helvetica-Bold" size:19]];
    titleLabel.textAlignment=NSTextAlignmentCenter;
    titleLabel.textColor=[UIColor whiteColor];
    
    
    
    
    UILabel *titleCode=[[UILabel alloc]initWithFrame:CGRectMake(0, 13, 100, 15)];
    if (stockCode!=nil) {
        titleCode.text=[NSString stringWithFormat:@"%@",stockCode];
    }else{
        titleCode.text=@"";
    }
    [titleCode setFont:[UIFont systemFontOfSize:13]];
    titleCode.textAlignment=NSTextAlignmentCenter;
    titleCode.textColor=[UIColor colorWithRed:186/255.0 green:218/255.0 blue:255/255.0 alpha:1.0];
    
    UIView *titleview=[[UIView alloc]initWithFrame:titleLabel.frame];
    [titleview addSubview:titleLabel];
    [titleview addSubview:titleCode];
    
    if (img) {
        [titleImgView removeFromSuperview];
        titleImgView = [[UIImageView alloc] initWithImage:img];
        titleImgView.frame = CGRectMake(titleLabel.frame.size.width, titleLabel.frame.origin.y+2, 38, 15);
        titleImgView.hidden = !show;
        [titleview addSubview:titleImgView];
    }
    
    self.titleView = titleview;
    self.navigationItem.titleView = titleview;
}

-(BOOL)isTitleImgShow{
    return titleImgView && !titleImgView.hidden;
}

-(void)showTitleImg{
    titleImgView.hidden=NO;
}

-(void)hideTitleImg{
    titleImgView.hidden=YES;
}


-(void)showBackButton
{
    
    //    if ([self isMemberOfClass:[MainViewController class]]) {   //针对该app做了一个返回键的判断处理
    //
    ////        [self showLeftNormalButton:@"home" highLightImage:@"home_hover" selector:nil];
    //
    //    } else {
    //
    //        if ([self isMemberOfClass:NSClassFromString(@"GraphViewController")]) return;
    //
    //        NSArray *controllerArr = @[@"IndexViewController"];
    //
    //        for (NSString *controName in controllerArr) {
    //
    //
    //            if ([self isMemberOfClass:NSClassFromString(controName)])  {
    //                [self showLeftNormalButton:@"home" highLightImage:@"home_hover" selector:nil];
    //                return;
    //            }
    //        }
    
    
    [self showLeftNormalButton:@"back_white" highLightImage:@"back_white" selector:nil];
    //    }
    
    
    
}

//  创建取消按钮
-(void) showCancelButton
{
    [self showLeftNormalButton:@"navibar_btn_cancel_normal" highLightImage:nil selector:nil];
}


-(void)showRightButtonNormalImage:(NSString *)normalImageName highLightImage:(NSString*)highLightImageName selector:(SEL)tagSelector
{
    
    [self showRightButtonNormalImage:normalImageName highLightImage:highLightImageName selector:tagSelector beTintColor:YES];
    
}

-(void)showRightButtonNormalImage:(NSString *)normalImageName highLightImage:(NSString*)highLightImageName selector:(SEL)tagSelector beTintColor:(BOOL)beTintColor
{
    //    beTintColor=NO;
    //    normalImageName=@"my_message_unread";//@"my_message";
    if(IOS7_OR_LATER)
    {
        [self showRightButtonNormalImageIos7Above:normalImageName beTintColor:beTintColor selector:tagSelector];
    }
    else{
        [self showRightButtonNormalImageIos6:normalImageName highLightImage:highLightImageName selector:tagSelector];
    }
    
}


-(void)showRightButtonNormalImageIos7Above:(NSString *)normalImageName beTintColor:(BOOL)beTintColor selector:(SEL)tagSelector{
    
    
    UIImage *normalImage = [UIImage imageNamed:normalImageName];
    if(beTintColor==NO)
        normalImage=[normalImage imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIBarButtonItem* item;
    
    if(tagSelector) // 内置的选择器实现，否则通过委托方式触发事件
        item=[[UIBarButtonItem alloc] initWithImage:normalImage style:UIBarButtonItemStyleDone target:self action:tagSelector];
    
    if(self.delegate)
        item=[[UIBarButtonItem alloc] initWithImage:normalImage style:UIBarButtonItemStyleDone target:self action:@selector(goRight:)];
    
    /**------------设置item的颜色————————————————*/
    //    item.tintColor=[UIColor blackColor];
    
    UIBarButtonItem *flexSpacer = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace
                                                                               target:self
                                                                               action:nil];
    
    /**——----------设置item偏移位置-----------**/
    //    flexSpacer.width = -12.f;
    
    
    [self.navigationItem setRightBarButtonItems:[NSArray arrayWithObjects:flexSpacer,item, nil]];
    
    //    [self.navigationItem setRightBarButtonItem:item];
}

-(void)showRightButtonNormalImageIos6:(NSString *)normalImageName highLightImage:(NSString*)highLightImageName selector:(SEL)tagSelector{
    
    UIButton* rightButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    UIImage *normalImage = [UIImage imageNamed:normalImageName];
    
    rightButton.frame = CGRectMake(kNavRightButonOffset, 0, normalImage.size.width+25, normalImage.size.height);
    rightButton.backgroundColor=[UIColor clearColor];
    [rightButton setImage:normalImage forState:UIControlStateNormal];
    
    if(highLightImageName)
    {
        [rightButton setImage:[UIImage imageNamed:highLightImageName] forState:UIControlStateHighlighted];
    }
    
    if(tagSelector) // 内置的选择器实现，否则通过委托方式触发事件
        [rightButton addTarget:self action:tagSelector forControlEvents:UIControlEventTouchUpInside];
    
    if(self.delegate)
        [rightButton addTarget:self action:@selector(goRight:) forControlEvents:UIControlEventTouchUpInside];
    
    UIView* mainView=[[UIView alloc] initWithFrame: CGRectMake(0, 0, rightButton.frame.size.width-5, rightButton.frame.size.height)];
    [mainView addSubview:rightButton];
    
    
    [self.navigationItem setRightBarButtonItem:[[UIBarButtonItem alloc] initWithCustomView:mainView]];
    
}

-(void)showRightButtonTitle:(NSString *)title andSelector:(SEL)tagSelector
{
    UIButton *rightButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    rightButton.frame = CGRectMake(kNavRightButonTextOffset, 0, title.length * 17, 44);
    
    [rightButton setTitle:title forState:UIControlStateNormal];
    
    [rightButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [rightButton setTitleColor:kNavigationBarHighLightColor forState:UIControlStateHighlighted];
    
    [rightButton.titleLabel setFont:[UIFont systemFontOfSize:16.0f]];
    
    if(tagSelector)// 内置的选择器实现，否则通过委托方式触发事件
        [rightButton addTarget:self action:tagSelector forControlEvents:UIControlEventTouchUpInside];
    
    if(self.delegate)
        [rightButton addTarget:self action:@selector(goRight:) forControlEvents:UIControlEventTouchUpInside];
    
    UIView* mainView=[[UIView alloc] initWithFrame: CGRectMake(0, 0,title.length * 17, 44)];
    [mainView addSubview:rightButton];
    
    [self.navigationItem setRightBarButtonItem:[[UIBarButtonItem alloc] initWithCustomView:mainView]];
}

-(void)showLeftNormalButton:(NSString *)normalImageName highLightImage:(NSString*)highLightImageName selector:(SEL)tagSelector
{
    self.leftNormalImageName=normalImageName;
    UIImage* normalImage = [UIImage imageNamed:normalImageName];
    
    UIButton* backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    UIImageView* leftView=[[UIImageView alloc] initWithImage:normalImage];
    CGRect leftViewFrame=leftView.frame;
    
    //用来改变原有系统图标的边距
    //    leftViewFrame.origin.x-=kNavLeftButonTextOffset;
    //    leftViewFrame.origin.x-=8.f;
    
    leftView.frame=leftViewFrame;
    leftView.tag=kTagLeftArrowView;
    
    
    backButton.frame = CGRectMake(0.f, 0, normalImage.size.width, normalImage.size.height);
    backButton.backgroundColor=[UIColor clearColor];
    
    if (highLightImageName)
    {
        UIImage* highLightImage = [UIImage imageNamed:highLightImageName];
        
        [backButton setImage:highLightImage forState:UIControlStateHighlighted];
    }
    
    if (!tagSelector) {
        tagSelector = @selector(goBack:);
    }
    
    
    
    [backButton addTarget:self action:tagSelector forControlEvents:UIControlEventTouchUpInside];
    
    [backButton addSubview:leftView];
    
    [backButton addTarget:self action:@selector(goBack:) forControlEvents:UIControlEventTouchUpInside];
    [backButton addTarget:self action:@selector(goBackHightLight:) forControlEvents:UIControlEventTouchDown];
    [backButton addTarget:self action:@selector(goBackNormal:) forControlEvents:UIControlEventTouchUpOutside];
    [backButton addTarget:self action:@selector(goBackNormal:) forControlEvents:UIControlEventTouchCancel];
    
    [self.navigationItem setLeftBarButtonItem:[[UIBarButtonItem alloc] initWithCustomView:backButton]];
    //    UIBarButtonItem *barItem = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    //    self.navigationItem.backBarButtonItem = barItem;    //serve well
}

-(void)showLeftButtonTitle:(NSString *)title
{
    
    // title=[NSString stringWithFormat:@"%@",title];
    
    // self.pLeftTitle=title;
    
    UIImage* image=[UIImage imageNamed:@"navigation_bar_back_normal"];
    
    UIImageView* leftView=[[UIImageView alloc] initWithImage:image];
    
    
    CGRect leftViewFrame=leftView.frame;
    
    leftViewFrame.origin.x-=kNavLeftButonTextOffset;
    leftViewFrame.origin.x+=2.f;
    leftViewFrame.origin.y+=10;
    
    leftView.frame=leftViewFrame;
    //    leftView.backgroundColor=[UIColor blueColor];
    
    leftView.tag=kTagLeftArrowView;
    
    UIButton* mainView=[UIButton buttonWithType:UIButtonTypeCustom];
    mainView.frame=CGRectMake(2, 0, title.length * 17, 44);
    mainView.backgroundColor=[UIColor clearColor];
    
    UILabel* label=[[UILabel alloc] initWithFrame:CGRectMake(kNavLeftLabelTextOffset, -1, title.length * 17, 44)];
    label.tag=kTagLeftLabelView;
    label.text=title;
    label.textColor=[UIColor blackColor];
    label.backgroundColor=[UIColor clearColor];
    CGRect rectLabel=label.frame;
    rectLabel.size.width+=16;
    rectLabel.origin.x+=2.f;
    label.frame=rectLabel;
    
    
    CGRect mainFrame=mainView.frame;
    mainFrame.size.width=label.frame.size.width;
    mainView.frame=mainFrame;
    
    [mainView addSubview:label];
    [mainView addSubview:leftView];
    
    [mainView addTarget:self action:@selector(goBack:) forControlEvents:UIControlEventTouchUpInside];
    [mainView addTarget:self action:@selector(goBackHightLight:) forControlEvents:UIControlEventTouchDown];
    [mainView addTarget:self action:@selector(goBackNormal:) forControlEvents:UIControlEventTouchUpOutside];
    [mainView addTarget:self action:@selector(goBackNormal:) forControlEvents:UIControlEventTouchCancel];
    
    
    [self.navigationItem setLeftBarButtonItem:[[UIBarButtonItem alloc] initWithCustomView:mainView]];
    
    
    
    
    
    
    
    
    
}


#pragma mark - 设置title的下拉子标题
- (void)displayNavigationTitleItmes:(NSArray *)itmes withController:(MyViewController *)controller{
    
    //只有一个标题时
    if (itmes.count == 1) {
        
        self.navigationBarTitle = itmes[0];
        //        self.navigationItem.title = self.navigationBarTitle;
        [self setNavigationTitle];
        
    }
    //有多个子标题时
    else  if (controller.navigationItem) {
        
        NSArray *titleArr = [itmes sortedArrayWithOptions:NSSortConcurrent usingComparator:^NSComparisonResult(id obj1, id obj2) {
            
            if ([obj1 sizeWithFont:[UIFont boldSystemFontOfSize:18.0f] constrainedToSize:CGSizeMake(MAXFLOAT, 21)].width > [obj2 sizeWithFont:[UIFont boldSystemFontOfSize:18.0f] constrainedToSize:CGSizeMake(MAXFLOAT, 21)].width)
                return (NSComparisonResult)NSOrderedDescending;
            
            if ([obj1 sizeWithFont:[UIFont boldSystemFontOfSize:18.0f] constrainedToSize:CGSizeMake(MAXFLOAT, 21)].width < [obj2 sizeWithFont:[UIFont boldSystemFontOfSize:18.0f] constrainedToSize:CGSizeMake(MAXFLOAT, 21)].width) {
                
                return (NSComparisonResult)NSOrderedAscending;
            }
            
            return (NSComparisonResult)NSOrderedSame;
        }];
        
        
        CGRect frame = CGRectMake(0.0, 0.0,  [[titleArr lastObject] sizeWithFont:[UIFont boldSystemFontOfSize:18.0f] constrainedToSize:CGSizeMake(MAXFLOAT, 21)].width, controller.navigationController.navigationBar.bounds.size.height);
        SINavigationMenuView *menu = [[SINavigationMenuView alloc] initWithFrame:frame title:itmes[0]];
        [menu displayMenuInView:controller.view];
        
        
        menu.items = itmes;
        menu.delegate = self;
        
        //显示下拉框
        controller.navigationItem.titleView = menu;
        
        _menuView = menu;
    }
    
    
    
}



#pragma mark - action
-(IBAction)goBackHightLight:(id)sender
{
    UIButton* btn=(UIButton*)sender;
    UIImageView* arrowView=(UIImageView*)[btn viewWithTag:kTagLeftArrowView];
    
    UILabel* labelView=(UILabel*)[btn viewWithTag:kTagLeftLabelView];
    
    
    UIImage* hoverImage=[UIImage imageNamed:@"home_hover"];
    
    if(self.leftNormalImageName)
        hoverImage= [[UIImage imageNamed:self.leftNormalImageName] rt_darkenWithLevel:0.5f];
    
    [arrowView setImage:hoverImage];
    [arrowView setHighlightedImage:hoverImage];
    [arrowView setHighlighted:YES];
    
    labelView.textColor=kNavigationBarHighLightColor;
}

-(IBAction)goBackNormal:(id)sender
{
    UIButton* btn=(UIButton*)sender;
    UIImageView* arrowView=(UIImageView*)[btn viewWithTag:kTagLeftArrowView];
    
    UILabel* labelView=(UILabel*)[btn viewWithTag:kTagLeftLabelView];
    
    UIImage* hoverImage=[UIImage imageNamed:@"home"];
    
    if(self.leftNormalImageName)
        hoverImage= [UIImage imageNamed:self.leftNormalImageName];
    
    [arrowView setImage:hoverImage];
    
    [arrowView setHighlighted:NO];
    
    labelView.textColor=[UIColor blackColor];
    
}

-(IBAction)goBack:(id)sender{
    
    [self goBackNormal:sender];
    
    if(self.delegate)
    {
        if([self.delegate respondsToSelector:@selector(MyViewControllerOnBack)])
        {
            [self.delegate MyViewControllerOnBack];
            return;
        }
    }
    
    [self.navigationController popViewControllerAnimated:YES];
    
}



-(IBAction)goRight:(id)sender
{
    if(self.delegate)
    {
        if([self.delegate respondsToSelector:@selector(MyViewController:onRightButton:)])
        {
            [self.delegate MyViewController:self onRightButton:sender];
            return;
        }
    }
}
- (void) clickTabBarItem
{
    
}

#pragma mark - QuickUI - LifeCycle

-(void)initQuickUI{
    
}

-(void)initQuickMock{
    
}


@end


