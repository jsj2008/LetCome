//
//  PicViewController.h
//  LetCome
//
//  Created by  rjt on 16/8/15.
//  Copyright © 2016年 JYZD. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PicViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *img;
@property (strong,nonatomic) NSString *imgPath;
- (IBAction)closeClicked:(id)sender;
@end
