//
//  NSData+JSON.m
//  QianFangGuJie
//
//  Created by chenyi on 15/8/31.
//  Copyright © 2015年 JYZD. All rights reserved.
//

#import "NSData+JSON.h"

@implementation NSData (JSON)
-(id)objectFromJSONData{
    NSError *error ;
    NSMutableDictionary *jsonDic = [NSJSONSerialization JSONObjectWithData: self options:NSJSONReadingMutableLeaves error:&error];
    if (error) {
        NSLog(@"Got an error: %@", error);
        return nil;
    }
    return jsonDic;
}
@end
