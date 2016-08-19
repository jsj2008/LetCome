//
//  EFTextField.m
//  QianFangGuJie
//
//  Created by  rjt on 15/4/17.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import "EFTextField.h"

#define kFilterNumber @"1234567890"
#define kFilterLetter @"abcdefghijklmnopqrstuvwxyz"

@interface EFTextField(){
    BOOL hasChangedView;//标识是否改变的窗体位置
    CGRect keyboardRect;//键盘位置
}
@end;

@implementation EFTextField

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(id)initWithFrame:(CGRect)frame{
    if (self = [super initWithFrame:frame]) {
        [self initType];
    }
    return self;
}

-(void)awakeFromNib{
    [self initType];
}



-(void)initType{
    hasChangedView = NO;
    
//    [self addTarget:self action:@selector(endInput:) forControlEvents:UIControlEventEditingDidEndOnExit];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShown:) name:UIKeyboardWillShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter]  addObserver:self selector:@selector(keyboardWillHidden:) name:UIKeyboardWillHideNotification object:nil];
}

//获取最外层view
-(UIView *)getScreenView{
    UIView * view = nil;
    view = [self superview];
    while (view && [view superview]) {
        view = [view superview];
    }
    return view;
}

- (void) keyboardWillShown:(NSNotification *) notif
{
    NSDictionary *info = [notif userInfo];
    NSValue *value = [info objectForKey:UIKeyboardFrameBeginUserInfoKey];
    keyboardRect = [value CGRectValue];
//    NSLog(@"%.f",keyboardRect.size.height);
    [self touchInput:nil];

}

- (void) keyboardWillHidden:(NSNotification *) notif
{
    if (hasChangedView) {
        hasChangedView = NO;
        UIView *screenView = [self getScreenView];
        const float movementDuration = 0.3f; // tweak as needed
        [UIView beginAnimations: @"anim" context: nil];
        [UIView setAnimationBeginsFromCurrentState: YES];
        [UIView setAnimationDuration: movementDuration];
        screenView.frame = CGRectMake(0,0,screenView.frame.size.width,screenView.frame.size.height);
        [UIView commitAnimations];
    }
    
}



-(void)touchInput:(id)sender{

    if ([self isFirstResponder] && keyboardRect.origin.y
        >0 && hasChangedView == NO) {
        
        UIView *screenView = [self getScreenView];
        
        CGRect absRect = [screenView convertRect:self.frame fromView:self];
        
        if ((absRect.origin.y+absRect.size.height + 90) > (screenView.frame.size.height-keyboardRect.size.height)) {
            hasChangedView = YES;

            const float movementDuration = 0.3f; // tweak as needed
            int movement = (screenView.frame.size.height-keyboardRect.size.height) - ( absRect.origin.y+absRect.size.height + 90);
            [UIView beginAnimations: @"anim" context: nil];
            
            [UIView setAnimationBeginsFromCurrentState: YES];
            
            [UIView setAnimationDuration: movementDuration];
            
            screenView.frame = CGRectOffset(screenView.frame, 0, movement);
            
            [UIView commitAnimations];
            
        }
    }
}

-(void)checkInput:(id)sender{
    switch (self.type) {
        case TextFieldNumber:
            [self filterNumber];
            break;
        case TextFieldNumberLetter:
            [self filterNumberLetter];
            break;
        default:
            break;
    }
}


//过滤只能数字
-(void)filterNumber{
    NSString * str = self.text;
    NSMutableString *newstr = [NSMutableString string];
    for(int i=0;str!=nil && i<str.length && i<self.maxLength;++i){
        NSString* find = [str substringWithRange:NSMakeRange(i, 1)];
        NSRange range = [kFilterNumber rangeOfString:find];
        if (range.location != NSNotFound) {
            [newstr appendString:find];
        }
    }
    self.text = newstr;
}

//过滤输入只能数字和密码，区分大小写
-(void)filterNumberLetter{
    NSString * str = self.text;
    NSMutableString *newstr = [NSMutableString string];
    NSString * filter = [NSString stringWithFormat:@"%@%@%@",kFilterNumber,[kFilterLetter uppercaseString],[kFilterLetter lowercaseString]];
    for(int i=0;str!=nil && i<str.length && i<self.maxLength;++i){
        NSString* find = [str substringWithRange:NSMakeRange(i, 1)];
        NSRange range = [filter rangeOfString:find];
        if (range.location != NSNotFound) {
            [newstr appendString:find];
        }
    }
    self.text = newstr;
}



-(BOOL)verifyLetter:(NSString*)str{
    BOOL flg = YES;
    for(int i=0;str!=nil && i<str.length;++i){
        int asciiCode = [str characterAtIndex:0]; // 65
        if (!((asciiCode>=65 && asciiCode <= 90) || (asciiCode>=97 && asciiCode <= 122))) {
            flg = NO;
            break;
        }
    }

    return flg;
}

-(BOOL)isValid{
    if (self.text.length>=self.minLength && self.text.length<=self.maxLength) {
        return YES;
    }
    return NO;
}


-(void)dealloc{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


@end
