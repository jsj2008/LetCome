//
//  NSObject+JSON.m
//  QianFangGuJie
//
//  Created by chenyi on 15/8/31.
//  Copyright © 2015年 JYZD. All rights reserved.
//

#import "NSObject+JSON.h"

@implementation NSObject (JSON)
-(NSString*)JSONString{
    NSString *jsonString = nil;
    NSError *error;
    @try {
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:self
                                                           options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                             error:&error];
        if (! jsonData) {
            NSLog(@"Got an error: %@", error);
        } else {
            jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        }

    }
    @catch (NSException *exception) {
        
    }

    return jsonString;
}
@end
