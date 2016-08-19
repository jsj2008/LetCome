//
//  QUAdaptor.m
//  WeiPay
//
//  Created by zhuojian on 14-3-13.
//  Copyright (c) 2014年 weihui. All rights reserved.
//

#import "QUAdaptor.h"
#import "QUSource.h"
#import "QUTableView.h"
#import "QUNibHelper.h"
#import "QUSection.h"
#import "QUKeyboardToolbar.h"
#import "QUTextField.h"

NSString *const MJRefreshFooterPullToRefresh = @"上拉即可翻页";
NSString *const MJRefreshFooterReleaseToRefresh = @"释放即可翻页";
NSString *const MJRefreshFooterRefreshing = @"加载中";

NSString *const MJRefreshHeaderPullToRefresh = @"下拉即可刷新";
NSString *const MJRefreshHeaderReleaseToRefresh = @"释放即可刷新";
NSString *const MJRefreshHeaderRefreshing = @"加载中";

@interface QUAdaptor()<UIGestureRecognizerDelegate>
{
//    UITapGestureRecognizer* tapGestureRecognizer;
}
@property(nonatomic,strong)NSMutableDictionary* gestureDict;  // 手势字典
@end

@implementation QUAdaptor

+(instancetype)adaptor{
    return [[self alloc] init];
}

-(id)init{
    
    self=[super init];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
    
    self.gestureDict=[NSMutableDictionary dictionaryWithCapacity:10];
    
    self.pNibHelper=[[QUNibHelper alloc] init];
    
    return self;
}

+(id)adaptorWithTableView:(QUTableView*)tableView nibArray:(NSArray*)array delegate:(id<QUAdaptorDelegate>)delegate
{
    QUAdaptor* adaptor=nil;
    @synchronized(self)
    {
        adaptor= [self adaptor];
        
        adaptor.delegate=delegate;
        
        
        QUSource* source=[[QUSource alloc] init];
        
        [adaptor bindSource:source andTableView:tableView nibArray:array];
    }
    
    
    return adaptor;
}


-(void)bindSource:(QUSource *)sources andTableView:(QUTableView *)tableView nibName:(NSString *)nibName{
   
    [self bindSource:sources andTableView:tableView nibArray:@[nibName]];
}

-(void)bindSource:(QUSource *)sources andTableView:(QUTableView *)tableView nibArray:(NSArray *)nibArray{
    self.pSectionInfo=[[NSMutableDictionary alloc] initWithCapacity:10];
    
    [self initTap:tableView];

    
    /** 记录section高度 */
    
    for (NSString* nibName in nibArray)
    {
        NSArray* list=[QUNibHelper loadNibNamed:nibName];
        
        for (QUSection* s in list) {
            QUSectionInfo* i=[[QUSectionInfo alloc] init];
            i.frame=s.frame;
            [self.pSectionInfo setObject:i forKey:NSStringFromClass([s class])];
        }
    }
    
    
    
    self.pSources=sources;
    self.pTableView=tableView;
    self.pNibName=[nibArray objectAtIndex:0];
    self.pNibNameArray=nibArray;
    self.pTableView.delegate=self;
    self.pTableView.dataSource=self;
//    [self.pTableView reloadData];
}

#pragma mark 下拉刷新控件
// 启用上拉刷新
-(void)setRefreshDelegateHeader:(id<QURefreshBaseViewDelegate>)refreshDelegate{
    if(!self.refreshHeaderView){
        __weak QUAdaptor *_self = self;
        _headerDelegate = refreshDelegate;
        _refreshHeaderView = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
            if ([_self.headerDelegate respondsToSelector:@selector(refreshViewBeginRefreshing:)]) {
                [_self.headerDelegate refreshViewBeginRefreshing:_self.refreshHeaderView];
            }
        }];
        // 设置文字
        [(MJRefreshNormalHeader*)_refreshHeaderView setTitle:MJRefreshHeaderPullToRefresh forState:MJRefreshStateIdle];
        [(MJRefreshNormalHeader*)_refreshHeaderView setTitle:MJRefreshHeaderReleaseToRefresh forState:MJRefreshStatePulling];
        [(MJRefreshNormalHeader*)_refreshHeaderView setTitle:MJRefreshHeaderRefreshing forState:MJRefreshStateRefreshing];
        ((MJRefreshNormalHeader*)_refreshHeaderView).lastUpdatedTimeLabel.hidden = YES;
        // 下拉刷新
        _pTableView.mj_header = _refreshHeaderView ;
        // 设置自动切换透明度(在导航栏下面自动隐藏)
        _pTableView.mj_header.automaticallyChangeAlpha = YES;
    }
}

// 启用下拉刷新
-(void)setRefreshDelegateFooter:(id<QURefreshBaseViewDelegate>)refreshDelegate{
    if(!self.refreshFooterView){
        __weak QUAdaptor *_self = self;
        _footerDelegate = refreshDelegate;
        // 上拉刷新
        _refreshFooterView = [MJRefreshBackNormalFooter footerWithRefreshingBlock:^{
            if ([_self.footerDelegate respondsToSelector:@selector(refreshViewBeginRefreshing:)]) {
                [_self.footerDelegate refreshViewBeginRefreshing:_self.refreshFooterView];
            }
        }];
        // 设置文字
        [(MJRefreshBackNormalFooter*)_refreshFooterView setTitle:MJRefreshFooterPullToRefresh forState:MJRefreshStateIdle];
        [(MJRefreshBackNormalFooter*)_refreshFooterView setTitle:MJRefreshFooterReleaseToRefresh forState:MJRefreshStatePulling];
        [(MJRefreshBackNormalFooter*)_refreshFooterView setTitle:MJRefreshFooterRefreshing forState:MJRefreshStateRefreshing];
        
        _pTableView.mj_footer = _refreshFooterView;
    }
    
    
}

#pragma mark TableViewDelegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[self.pSources entityAtIndex:section] count];
}

static NSString *CellIdentifier = @"Cell";

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
   
    QUEntity* et=[self.pSources entityAtSection:indexPath.section rows:indexPath.row];
    
//    NSString* sectionClassName=[self.pSources sectionAtIndex:indexPath.row];
    
//    NSString* key=NSStringFromClass(et.class);
    NSString* sectionClassName=[self.pSources sectionAtKey:et.key];
    
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:sectionClassName];
    
    
    if (cell == nil) {
        
        Class cls=NSClassFromString(sectionClassName);

        cell=[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:sectionClassName];
        
        QUSection* section= [QUNibHelper loadNibArray:self.pNibNameArray ofClass:cls];
        
//        QUSection* section=[self.pNibHelper loadNibArray:self.pNibNameArray ofClass:cls];
        
        if(!section)
            QULog(@"QUNibHelper: failed to load nib");
        
        [section sectionWillLoad]; // 初始化Section 数据
        
        [cell.contentView addSubview:section];
        
        cell.contentView.frame=section.frame;
        
        cell.accessoryType = et.accessoryType; //显示最右边的箭头
        cell.selectionStyle=et.selectionStyle;
        
        float iosVer=[[[UIDevice currentDevice] systemVersion] floatValue];
        if(iosVer>=7.f) // must be ios7+
        {
            cell.separatorInset=et.separatorInset;
        }
        
        {
            
            if(!et.selectedBgColor)
                et.selectedBgColor=[UIColor clearColor];
            
            if(!et.borderLineColor)
                et.borderLineColor=[UIColor clearColor];
            
            //取消选中颜色
            UIView *backView = [[UIView alloc] initWithFrame:cell.frame];
            
            cell.selectedBackgroundView = backView;
            cell.selectedBackgroundView.backgroundColor = et.selectedBgColor;
    
            //取消边框线
            [cell setBackgroundView:[[UIView alloc] init]];          //取消边框线
            cell.backgroundColor = et.borderLineColor;
        }
        
        /** 初始化构造Section时调用 */
        if([self.delegate respondsToSelector:@selector(QUAdaptor:willDidLoadSection:willDidLoadEntity:)])
        {
            [self.delegate QUAdaptor:self willDidLoadSection:section willDidLoadEntity:et];
        }
       
    }
    
    QUSection* section=[[cell.contentView subviews] objectAtIndex:0];

    section.pIndexPath=indexPath;  // Cell行信息记录
    
    section.section=indexPath.section;
    section.rows=indexPath.row;
    
    section.pAdaptor=self;
    
    section.entity=et;
    
    et.pSection=section; 
    
    if(self.delegate)
    {
        
        [section layoutSection];

        if([self.delegate respondsToSelector:@selector(QUAdaptor:forSection:forEntity:)]){
            [self.delegate QUAdaptor:self forSection:section forEntity:et];
        }else if([self.delegate respondsToSelector:@selector(SQUAdaptor:forSection:forEntity:)]){
            [self.delegate SQUAdaptor:self forSection:section forEntity:et];
        }
        
    }
    return cell;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [self.pSources count];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if(self.delegate==nil)
        return;
    
    QUEntity* et=[self.pSources entityAtSection:indexPath.section rows:indexPath.row];
    
//    QUSection* section=[[[tableView cellForRowAtIndexPath:indexPath] subviews] objectAtIndex:0];
    QUSection* section=[[[[tableView cellForRowAtIndexPath:indexPath] contentView] subviews] objectAtIndex:0];
    if([self.delegate respondsToSelector:@selector(QUAdaptor:selectedSection:entity:)]){
        [self.delegate QUAdaptor:self selectedSection:section entity:et];
    }else if([self.delegate respondsToSelector:@selector(SQUAdaptor:selectedSection:entity:)]){
        [self.delegate SQUAdaptor:self selectedSection:section entity:et];
    }
    
    
    [[NSNotificationCenter defaultCenter] postNotificationName:QU_TABLE_CELL_SELECTED_NOTIFY object:nil userInfo:nil];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    [tableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionTop animated:YES];
    
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if ([self.delegate respondsToSelector:@selector(MyQUTableView:commitEditingStyle:forRowAtIndexPath:)]) {
        [self.delegate MyQUTableView:tableView commitEditingStyle:editingStyle forRowAtIndexPath:indexPath];
    }
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if([self.delegate respondsToSelector:@selector(MyQUTableView:canEditRowAtIndexPath:)]){
        return [self.delegate MyQUTableView:tableView canEditRowAtIndexPath:indexPath];
    }
    return NO;
}

-(void)tableView:(UITableView *)tableView didEndEditingRowAtIndexPath:(NSIndexPath *)indexPath{
    if([self.delegate respondsToSelector:@selector(MyQUTableView:didEndEditingRowAtIndexPath:)]){
        [self.delegate MyQUTableView:tableView didEndEditingRowAtIndexPath:indexPath];
    }

}

-(void)tableView:(UITableView *)tableView willBeginEditingRowAtIndexPath:(NSIndexPath *)indexPath{
    if([self.delegate respondsToSelector:@selector(MyQUTableView:willBeginEditingRowAtIndexPath:)]){
        [self.delegate MyQUTableView:tableView willBeginEditingRowAtIndexPath:indexPath];
    }
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    QUEntity* et=[self.pSources entityAtSection:indexPath.section rows:indexPath.row];

    NSString* sectionClassName=[self.pSources sectionAtKey:et.key];
    
    QUSectionInfo* info=[self.pSectionInfo objectForKey:sectionClassName];
    
    CGRect frame=[info frame];
    NSLog(@"height:%.f",frame.size.height);

    return frame.size.height;
}


-(void)notifyChanged{
    
    if(self.pTableView)
        [self.pTableView reloadData];
    
}



#pragma mark - keyboard Notification
- (void)keyboardWillShow:(NSNotification *)notification {
    
    self.pKeyBoardVisible=YES;
    //    NSDictionary *userInfo = [notification userInfo];
    
}

- (void)keyboardWillHide:(NSNotification *)notification {
    
    self.pKeyBoardVisible=NO;
    //    NSDictionary* userInfo = [notification userInfo];
    
}

#pragma mark -textField Delegate

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField        // return NO to disallow editing.
{
    /* 禁用软键盘上方出现快捷toolbar
    if (textField.inputAccessoryView == nil) {
        QUKeyboardToolbar* toolbar=[QUKeyboardToolbar keyboardToolbar];
        // Loading the AccessoryView nib file sets the accessoryView outlet.
        textField.inputAccessoryView = toolbar;
        toolbar.textField=(QUTextField*)textField;
    }*/
    
    
    if([self.delegate respondsToSelector:@selector(QUAdaptor:textFieldBeginEditing:)])
        [self.delegate QUAdaptor:self textFieldBeginEditing:(QUTextField *)textField];
    
    
    QUSection* section=(QUSection*)[textField superview];
    
    QUTableView* table= [section.pAdaptor pTableView];
    //    CGPoint pt= [table contentOffset];
    //    pt=CGPointMake(0, pt.y+180.f);
    
    
    CGPoint srcPt=textField.bounds.origin;
    srcPt.x=0.f;
    //    CGPoint toSectionPt=[section convertPoint:srcPt toView:section];
    
    float minOffsetY=90.f;
    
    if([self.delegate respondsToSelector:@selector(QUAdaptor:willOffsetForTextField:)])
        minOffsetY=[self.delegate QUAdaptor:self willOffsetForTextField:textField];
    
    CGPoint toPt= [section convertPoint:srcPt toView:table ];
    
    /** 如果小于最小滚动值则忽略 */
    if(toPt.y<=minOffsetY)
        return YES;
    
    toPt.y=toPt.y-minOffsetY;
    //
    //
    [table setContentOffset:toPt animated:YES];
    return YES;
}

- (BOOL)textFieldShouldEndEditing:(UITextField *)textField{
    [textField resignFirstResponder];

//    if([textField isMemberOfClass:[QUTextField class]])
//    {
        QUSection* section=(QUSection*)[textField superview];
        
        if([self.delegate respondsToSelector:@selector(QUAdaptor:willChangeSection:willChangeTextField:)])
            [self.delegate QUAdaptor:self willChangeSection:section  willChangeTextField:(QUTextField *)textField];
//    }

    
    return YES;
}

- (void)textFieldEditChanged:(UITextField *)textField
{

    if([self.delegate respondsToSelector:@selector(QUAdaptor:textFieldEditChanged:)])
        [self.delegate QUAdaptor:self textFieldEditChanged:(QUTextField*)textField];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    if([self.delegate respondsToSelector:@selector(QUAdaptor:textView:shouldChangeTextInRange:replacementText:)])
        return [self.delegate QUAdaptor:self textView:textView shouldChangeTextInRange:range replacementText:text];
    
    return YES;
}


#pragma mark - helper
/**
 隐藏键盘
 */
-(void)hideKeyboard{
    [[NSNotificationCenter defaultCenter] postNotificationName:QU_TABLE_CELL_SELECTED_NOTIFY object:nil userInfo:nil];
}

/** 刷新指定的Section */
-(void)notifyChangedForSectionClass:(Class)class{
    [self notifyChangedForSectionClass:class withAnimation:UITableViewRowAnimationNone];
}

/** 刷新指定Section带动画*/
-(void)notifyChangedForSectionClass:(Class)class withAnimation:(UITableViewRowAnimation)animation{
    [self notifyChangedForSectionClass:class withTag:nil withAnimation:animation];
}

/** 刷新指定Tag的Section */
-(void)notifyChangedForSectionClass:(Class)class withTag:(NSNumber*)tag{
    [self notifyChangedForSectionClass:class withTag:tag withAnimation:UITableViewRowAnimationNone];
}

/** 刷新指定的Section 带tag */
-(void)notifyChangedForSectionClass:(Class)class withTag:(NSNumber*)tag withAnimation:(UITableViewRowAnimation)animation
{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
        NSDictionary *userInfo;
        
        if(tag)
            userInfo=@{@"sectionClass": class,@"animation":[NSNumber numberWithInt:animation],@"tag":tag};
        else
            userInfo=@{@"sectionClass": class,@"animation":[NSNumber numberWithInt:animation]};
        
        
        [[NSNotificationCenter defaultCenter] postNotificationName:QU_TABLE_CELL_SELECTED_RELOAD_NOTIFY object:nil userInfo:userInfo];
        
//        NSNotificationQueue *nq =[NSNotificationQueue defaultQueue];
//        [nq enqueueNotification:[NSNotification notificationWithName:QU_TABLE_CELL_SELECTED_RELOAD_NOTIFY object:nil userInfo:userInfo]postingStyle:NSPostASAP];
        

        
    });
   
    
    
}

/** 插入Section在指定的Section前 带tag */
-(void)notifyInsertBeforeSectionClass:(Class)class withTag:(NSNumber*)tag withAnimation:(UITableViewRowAnimation)animation
{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
        NSDictionary *userInfo;
        
        if(tag)
            userInfo=@{@"sectionClass": class,@"animation":[NSNumber numberWithInt:animation],@"tag":tag};
        else
            userInfo=@{@"sectionClass": class,@"animation":[NSNumber numberWithInt:animation]};
        
        
        [[NSNotificationCenter defaultCenter] postNotificationName:QU_TABLE_CELL_SELECTED_INSERT_NOTIFY object:nil userInfo:userInfo];
        
        //        NSNotificationQueue *nq =[NSNotificationQueue defaultQueue];
        //        [nq enqueueNotification:[NSNotification notificationWithName:QU_TABLE_CELL_SELECTED_RELOAD_NOTIFY object:nil userInfo:userInfo]postingStyle:NSPostASAP];
        
        
        
    });
    
    
    
}



-(void)dealloc{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
    
}

#pragma mark - tap recognize
-(void)initTap:(QUTableView*)tableView{
    [self addGestureRecognizer:tableView];
}

-(void)removeGestureRecognizer{
    NSString* key=[NSString stringWithFormat:@"%ld",[self.pTableView hash]];
    id gesutre=[self.gestureDict objectForKey:key];
    
    if(gesutre) // 如果手势存在则删除
    {
        [self.pTableView removeGestureRecognizer:gesutre];
        [self.gestureDict removeObjectForKey:key];
    }
}

//-(void)restoreGestureRecognizer{
//    
//    if(!self.tapGestureRecognizer)
//        [self addGestureRecognizer:self.pTableView];
//}

-(void)addGestureRecognizer:(UIView*)gestureView{
//    if(self.tapGestureRecognizer)
//    {
//        [gestureView removeGestureRecognizer:self.tapGestureRecognizer];
//        self.tapGestureRecognizer=nil;
//    }
    
    [self.gestureDict setObject:gestureView forKey:[NSString stringWithFormat:@"%ld",[gestureView hash]]];
    
    UITapGestureRecognizer* tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)];
    /*the number of fingers that must be on the screen*/
    tapGestureRecognizer.numberOfTouchesRequired = 1;
    /*the total number of taps to be performed before the gesture is recognized*/
    tapGestureRecognizer.numberOfTapsRequired = 1;
    tapGestureRecognizer.delegate=self;
    tapGestureRecognizer.cancelsTouchesInView=NO;
    
//    [gestureView addGestureRecognizer:tapGestureRecognizer];
    
    

}



- (void)handleGesture:(UIGestureRecognizer *)gestureRecognizer
{
    UIView *view = [gestureRecognizer view]; // 这个view是手势所属的view，也就是增加手势的那个view
    
    [[NSNotificationCenter defaultCenter] postNotificationName:QU_TABLE_CELL_SELECTED_NOTIFY object:nil userInfo:nil];
    
    if([self.delegate respondsToSelector:@selector(QUAdaptor:tapView:)])
        [self.delegate QUAdaptor:self tapView:view];
    
}

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer{
    if([self.delegate respondsToSelector:@selector(QUAdaptorShouldCancelsTouchesInView:)])
    {
        gestureRecognizer.cancelsTouchesInView=[self.delegate QUAdaptorShouldCancelsTouchesInView:self];
    }
    
    return YES;
}

// 询问delegate，两个手势是否同时接收消息，返回YES同事接收。返回NO，不同是接收（如果另外一个手势返回YES，则并不能保证不同时接收消息）the default implementation returns NO。
// 这个函数一般在一个手势接收者要阻止另外一个手势接收自己的消息的时候调用
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer{
    return NO;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch{
//    UIView* section=[self sectionView:touch.view];
    
    BOOL isSystemViewEvent=YES;
    
    if([self.delegate respondsToSelector:@selector(QUAdaptor:shouldTapView:)]) // 用户过滤处理手势
    {
        return [self.delegate QUAdaptor:self shouldTapView:touch.view];
    }
    {
        if([touch.view isKindOfClass:[QUSection class]]     // 系统默认过滤手势
           || [touch.view isKindOfClass:[UIButton class]]
           ) // 过滤浮层点击事件
        {
            isSystemViewEvent= NO;
        }
    }
    
    return isSystemViewEvent;
}

-(UIView*)sectionView:(UIView*)view{
    
    if(view==nil)
        return nil;
    
    if([view isKindOfClass:[QUSection class]]) // 直接返回view
        return view;
    
    UIView* v=[view superview];
    
    if(![v isKindOfClass:[QUSection class]])
    {
        v= [self sectionView:view];
    }
    
    return v;
}

/** 设置section高度 */
-(void)setSectionFrame:(CGRect)frame sectionClassName:(NSString*)sectionClassName
{
    QUSectionInfo* info=[self.pSectionInfo objectForKey:sectionClassName];
    info.frame=frame;
}

#pragma mark - notify
/** 发送数据变更 */
-(void)postChangedWithEntity:(QUEntity *)entity{
    if([self.delegate respondsToSelector:@selector(QUAdaptor:forSection:forEntity:)])
    {
        [self.delegate QUAdaptor:self forSection:entity.pSection forEntity:entity];
    }else if([self.delegate respondsToSelector:@selector(SQUAdaptor:forSection:forEntity:)])
    {
        [self.delegate SQUAdaptor:self forSection:entity.pSection forEntity:entity];
    }
}

/** 发送数据变更 */
-(void)postChangedWithEntity:(QUEntity *)entity section:(QUSection*)section{
    if([self.delegate respondsToSelector:@selector(QUAdaptor:forSection:forEntity:)])
    {
        [self.delegate QUAdaptor:self forSection:section forEntity:entity];
    }else if([self.delegate respondsToSelector:@selector(SQUAdaptor:forSection:forEntity:)])
    {
        [self.delegate SQUAdaptor:self forSection:section forEntity:entity];
    }
}

/** 滚动到顶部 */
-(void)scrollSectionToTop{
    [self.pTableView scrollSectionToTop];
}

@end
