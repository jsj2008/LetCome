//
//  AppDelegate.m
//  QianFangGuJie
//
//  Created by 余龙 on 14/12/19.
//  Copyright (c) 2014年 余龙. All rights reserved.
//

#import "AppDelegate.h"
#import "IndexViewController.h"


@interface AppDelegate ()<QUMockDelegate>


@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:[[IndexViewController alloc] init]];
    
    [self.window makeKeyAndVisible];
    
    return YES;
}


- (void)applicationWillEnterForeground:(UIApplication *)application {

}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    
}

- (void)applicationWillTerminate:(UIApplication *)application {
    
}



@end
