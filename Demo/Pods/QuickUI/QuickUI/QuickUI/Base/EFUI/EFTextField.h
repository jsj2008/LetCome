//
//  EFTextField.h
//  QianFangGuJie
//
//  Created by  rjt on 15/4/17.
//  Copyright (c) 2015年 余龙. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum _TextFieldType{
    TextFieldNormal,//正常模式
    TextFieldNumberLetter,//数字字母模式
    TextFieldNumber//数字模式
}TextFieldType;
@interface EFTextField : UITextField

//检查输入框输入内容有效性
@property TextFieldType type;
@property int minLength;
@property int maxLength;
-(BOOL)isValid;

@end
