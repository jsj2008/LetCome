//
//  IndexViewController.m
//  LetCome
//
//  Created by  rjt on 16/8/12.
//  Copyright © 2016年 JYZD. All rights reserved.
//

#import "IndexViewController.h"
#import "LoginMock.h"
#import "LoginEntity.h"
#import "RegisterMock.h"
#import "AFNetworking.h"
#import "PicViewController.h"


@interface IndexViewController ()<UIImagePickerControllerDelegate,UINavigationControllerDelegate,UIAlertViewDelegate>
- (IBAction)loginClicked:(id)sender;
- (IBAction)registerClicked:(id)sender;
- (IBAction)cameraClicked:(id)sender;


@property (weak, nonatomic) IBOutlet UITextField *emailField;
@property (weak, nonatomic) IBOutlet UITextField *pwdField;
@property (weak, nonatomic) IBOutlet UITextField *fullnameField;

@property (strong, nonatomic) LoginMock *loginMock;
@property (strong, nonatomic) RegisterMock *registerMock;
@property (weak, nonatomic) IBOutlet UITextView *textView;
@property (strong, nonatomic) UIImage *image;
@property (strong, nonatomic) NSString *retVal;
@end

@implementation IndexViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)loginClicked:(id)sender {
    self.loginMock = [LoginMock mock];
    LoginParam *p = [LoginParam param];
    p.email = self.emailField.text;
    p.pwd = self.pwdField.text;
    DEFINED_WEAK_SELF
    self.loginMock.returnBlock = ^(QUNetAdaptor *adaptor,QUNetResponse *response,AppMock *mock){
        LoginEntity *e = (LoginEntity*)response.pEntity;
        NSMutableString * s= [NSMutableString string];
        [s appendFormat:@"result:%@\n",e.result];
        [s appendFormat:@"error_code:%@\n",e.error_code];
        [s appendFormat:@"error_msg:%@\n",e.error_msg];
        [s appendFormat:@"uid:%@\n",e.uid];
        [s appendFormat:@"sessionid:%@\n",e.sessionid];
        [s appendFormat:@"fullname:%@\n",e.fullname];
        _self.textView.text = s;
    };
    
    [self.loginMock run:p];
}

- (IBAction)registerClicked:(id)sender {
    self.registerMock = [RegisterMock mock];
    RegisterParam *p = [RegisterParam param];
    p.email = self.emailField.text;
    p.pwd = self.pwdField.text;
    p.fullname = self.fullnameField.text;
    DEFINED_WEAK_SELF
    self.registerMock.returnBlock = ^(QUNetAdaptor *adaptor,QUNetResponse *response,AppMock *mock){
        LoginEntity *e = (LoginEntity*)response.pEntity;
        NSMutableString * s= [NSMutableString string];
        [s appendFormat:@"result:%@\n",e.result];
        [s appendFormat:@"error_code:%@\n",e.error_code];
        [s appendFormat:@"error_msg:%@\n",e.error_msg];
        [s appendFormat:@"uid:%@\n",e.uid];
        [s appendFormat:@"sessionid:%@\n",e.sessionid];
        [s appendFormat:@"fullname:%@\n",e.fullname];
        _self.textView.text = s;
    };
    
    [self.registerMock run:p];
}

- (IBAction)cameraClicked:(id)sender {
    UIImagePickerController *imagePicker=[[UIImagePickerController alloc] init];
    imagePicker.delegate=self;
    //    imagePicker.view.frame=s
    if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]){
        imagePicker.sourceType=UIImagePickerControllerSourceTypeCamera;
        
    }
    // imagePicker.allowsEditing=YES;
    //    [self.view addSubview:imagePicker.view];
    [self presentViewController:imagePicker animated:YES completion:^{
        
    }];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingImage:(UIImage *)image editingInfo:(NSDictionary *)editingInfo {
    
}
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info{
    [self dismissViewControllerAnimated:YES completion:nil];
    NSLog(@"%@",info);
    UIImage *image=[info objectForKey:UIImagePickerControllerOriginalImage];
    
    self.image=image;
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    //随便写
    [manager.requestSerializer setValue:@"99" forHTTPHeaderField:@"let_come_uid"];
    [manager.requestSerializer setValue:@"12345677" forHTTPHeaderField:@"let_come_sessionid"];
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"text/html",@"text/plain",@"application/json", nil];
    [manager POST:[NSString stringWithFormat:@"%@/image/upload",SERVER_URL_USER] parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
//        NSData *fData = UIImageJPEGRepresentation(self.photo, 0.5);
        NSData *imageData = UIImageJPEGRepresentation(image, 0.1);
        
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        formatter.dateFormat = @"yyyyMMddHHmmss";
        NSString *str = [formatter stringFromDate:[NSDate date]];
        NSString *fileName = [NSString stringWithFormat:@"%@.jpg", str];
        
        // 上传图片，以文件流的格式
        [formData appendPartWithFileData:imageData name:@"myfiles" fileName:fileName mimeType:@"image/jpeg"];
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSDictionary *dict = (NSDictionary*)responseObject;
        if ([[dict valueForKey:@"result"] isEqualToString:@"Y"]) {
            UIAlertView *view = [[UIAlertView alloc] initWithTitle:nil message:@"上传图片成功" delegate:self cancelButtonTitle:@"关闭" otherButtonTitles:@"去查看", nil];
            _retVal = [dict valueForKey:@"retVal"];
            [view show];
        }

    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
    }];
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (buttonIndex != alertView.cancelButtonIndex) {
        PicViewController *c = [[PicViewController alloc] init];
        NSString *str = [NSString stringWithFormat:@"%@/image/getimg?id=%@",SERVER_URL_USER,_retVal];
        c.imgPath = str;
        [self presentViewController:c animated:YES completion:nil];
        
    }
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker{
    [self dismissViewControllerAnimated:YES completion:^{

    }];
}
@end
