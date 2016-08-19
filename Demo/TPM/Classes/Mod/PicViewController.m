//
//  PicViewController.m
//  LetCome
//
//  Created by  rjt on 16/8/15.
//  Copyright © 2016年 JYZD. All rights reserved.
//

#import "PicViewController.h"
#import "UIImageView+WebCache.h"

@interface PicViewController ()

@end

@implementation PicViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewWillAppear:(BOOL)animated{
    [_img sd_setImageWithURL:[NSURL URLWithString:_imgPath]];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)closeClicked:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}
@end
