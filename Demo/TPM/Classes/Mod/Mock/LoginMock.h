//
//  LoginMock.h
//  LetCome
//
//  Created by  rjt on 16/8/12.
//  Copyright © 2016年 JYZD. All rights reserved.
//

#import "AppMock.h"

@interface LoginParam : AppParam

@property (nonatomic,copy)NSString *email;
@property (nonatomic,copy)NSString *pwd;

@end

@interface LoginMock : AppMock

@end
