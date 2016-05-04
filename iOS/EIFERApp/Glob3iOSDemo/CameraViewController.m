//
//  CameraViewController.m
//  EIFER App
//
//  Created by Jose Miguel SN on 4/5/16.
//
//

#import "CameraViewController.h"

@interface CameraViewController ()

@end

@implementation CameraViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  [self startRecordingSession];
}

-(void) startRecordingSession{
  session = [[AVCaptureSession alloc] init];
  session.sessionPreset = AVCaptureSessionPresetMedium;
  
  CALayer *viewLayer = self.cameraView.layer;
  NSLog(@"viewLayer = %@", viewLayer);
  
  captureVideoPreviewLayer = [[AVCaptureVideoPreviewLayer alloc] initWithSession:session];
  
  captureVideoPreviewLayer.frame = self.cameraView.bounds;
  
  [self.cameraView.layer addSublayer:captureVideoPreviewLayer];
  
  captureVideoPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
  
  AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
  
  NSError *error = nil;
  AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:device error:&error];
  
  if (!input) {
    NSLog(@"ERROR: trying to open camera: %@", error);    // Handle the error appropriately.
  }
  [session addInput:input];
  
  [session startRunning];
  
  [self adjustCameraLayer];
}

-(void) stopRecordingSession{
  [session stopRunning];
  [captureVideoPreviewLayer removeFromSuperlayer];

}

-(void) adjustCameraLayer{
  
  captureVideoPreviewLayer.frame = self.cameraView.bounds;
  
  //Orientation
  UIInterfaceOrientation orientation = [[UIApplication sharedApplication] statusBarOrientation];
  AVCaptureVideoOrientation ori;
  switch (orientation)
  {
    case UIInterfaceOrientationPortrait:
      ori = AVCaptureVideoOrientationPortrait;
      break;
    case UIInterfaceOrientationLandscapeRight:
      ori = AVCaptureVideoOrientationLandscapeRight; //home button on right. Refer to .h not doc
      break;
    case UIInterfaceOrientationLandscapeLeft:
      ori = AVCaptureVideoOrientationLandscapeLeft; //home button on left. Refer to .h not doc
      break;
    default:
      ori = AVCaptureVideoOrientationPortrait; //for portrait upside down. Refer to .h not doc
      break;
  }
  
  [captureVideoPreviewLayer.connection setVideoOrientation:ori];
  
}

- (void)viewWillTransitionToSize:(CGSize)size
       withTransitionCoordinator:(id<UIViewControllerTransitionCoordinator>)coordinator{
  
  [coordinator animateAlongsideTransition:^(id<UIViewControllerTransitionCoordinatorContext> context)
   {

   } completion:^(id<UIViewControllerTransitionCoordinatorContext> context)
   {
     [self adjustCameraLayer];
   }];
  
  [super viewWillTransitionToSize:size withTransitionCoordinator:coordinator];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
