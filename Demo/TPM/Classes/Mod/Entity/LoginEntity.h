//
//  LoginEntity.h
//  LetCome
//
//  Created by  rjt on 16/8/12.
//  Copyright © 2016年 JYZD. All rights reserved.
//

#import <QuickUI/QuickUI.h>

@interface LoginEntity : QUEntity
@property (nonatomic,copy)NSString *result;
@property (nonatomic,copy)NSString *error_code;
@property (nonatomic,copy)NSString *error_msg;
@property (nonatomic,copy)NSString *uid;
@property (nonatomic,copy)NSString *sessionid;
@property (nonatomic,copy)NSString *fullname;

@end
