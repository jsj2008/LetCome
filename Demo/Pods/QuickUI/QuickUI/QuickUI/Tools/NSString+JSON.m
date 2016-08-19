//
//  NSString+JSON.m
//  QianFangGuJie
//
//  Created by chenyi on 15/8/31.
//  Copyright © 2015年 JYZD. All rights reserved.
//

#import "NSString+JSON.h"

@implementation NSString (JSON)
-(id)objectFromJSONString{
    
    NSError *error ;
    id jsonObj = [NSJSONSerialization JSONObjectWithData: [self dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableLeaves error:&error];
    if (error) {
        NSLog(@"Got an error: %@", error);
        return nil;
    }
    @try {
    if ([jsonObj isKindOfClass:[NSDictionary class]]) {
        jsonObj = [NSMutableDictionary dictionaryWithDictionary:jsonObj];
    }else if([jsonObj isKindOfClass:[NSArray class]])
        jsonObj = [NSArray arrayWithArray:jsonObj];
    }
    @catch (NSException *exception) {
        NSLog(@"error:%@",exception);
    }

    return jsonObj;
}
@end
