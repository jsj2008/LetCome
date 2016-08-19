//
//  RegisterMock.h
//  LetCome
//
//  Created by  rjt on 16/8/12.
//  Copyright © 2016年 JYZD. All rights reserved.
//

#import "AppMock.h"

@interface RegisterParam : AppParam

@property (nonatomic,copy)NSString *email;
@property (nonatomic,copy)NSString *pwd;
@property (nonatomic,copy)NSString *fullname;
@end

@interface RegisterMock : AppMock

@end
