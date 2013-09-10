//
//  G3MToolbar.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 06/02/13.
//
//

#import "G3MToolbar.h"

@implementation G3MToolbar

@synthesize visible;

- (id)initWithFrame:(CGRect)frame
{
  self = [super initWithFrame: frame];
  if (self) {
    UIColor* bgColor = [UIColor colorWithRed: 0.0
                                       green: 0.0
                                        blue: 0.0
                                       alpha: 0.7];
    [self baseInit: bgColor
            height: frame.size.height];
  }
  
  return self;
}

- (id) init: (UIColor*) bgColor
     height: (int) height
{
  self = [super init];
  if (self) {
    [self baseInit: bgColor
            height: height];
  }
  return self;
}

- (id) init
{
  self = [super init];
  if (self) {
    UIColor* bgColor = [UIColor colorWithRed: 0.0
                                       green: 0.0
                                        blue: 0.0
                                       alpha: 0.7];
    [self baseInit: bgColor
            height: 68];
  }
  return self;
}

- (void) baseInit: (UIColor*) bgColor
           height: (int) height
{
  CGRect screenFrame = [self getScreenFrame];
  [self setBackgroundColor: bgColor];
  [self setFrame: CGRectMake(0, screenFrame.size.height - height, screenFrame.size.width, height)];
  [self setAutoresizingMask: (UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleBottomMargin)];
  [self setCenter: CGPointMake(screenFrame.size.width / 2, screenFrame.size.height + ([self frame].size.height / 2))];
  
  currentOrientation = UIDeviceOrientationPortrait;  
  [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
  [[NSNotificationCenter defaultCenter] addObserver: self selector: @selector(deviceOrientationDidChange:) name: UIDeviceOrientationDidChangeNotification object: nil];
}

- (CGRect) getScreenFrame
{
  CGRect screenFrame = [[UIScreen mainScreen] bounds];
  if (UIDeviceOrientationIsLandscape([UIDevice currentDevice].orientation)) {
    screenFrame = CGRectMake(0, 0, screenFrame.size.height, screenFrame.size.width);
  }
  return screenFrame;
}

- (void) show
{
  CGRect screenFrame = [self getScreenFrame];
  
  [UIView beginAnimations: @"slide up" context: nil];
  [UIView setAnimationDuration: 0.5];
  [self setCenter: CGPointMake(screenFrame.size.width / 2, (screenFrame.size.height - 20) - ([self frame].size.height / 2))];
  [UIView commitAnimations];
}

- (void) hide
{
  CGRect screenFrame = [self getScreenFrame];
  
  [UIView beginAnimations: @"slide down" context: nil];
  [UIView setAnimationDuration: 0.5];
  [self setCenter: CGPointMake(screenFrame.size.width / 2, screenFrame.size.height + ([self frame].size.height / 2))];
  [UIView commitAnimations];
}

- (void) setVisible: (BOOL)isVisible
{
    self->visible = isVisible;

    if ([self visible]) {
      [self show];
    }
    else {
      [self hide];
    }
}

- (void) clear
{
  NSArray *subviews = [self subviews];
  
  if ([subviews count] == 0) return;
  
  for (UIView *subview in subviews) {
    [subview removeFromSuperview];
  }
}

- (void) addTool:(UIView *)view
{
  [self addSubview: view];
}

- (void)deviceOrientationDidChange:(NSNotification *)notification {
  //Obtaining the current device orientation
  UIDeviceOrientation orientation = [[UIDevice currentDevice] orientation];
  
  //Ignoring specific orientations
  if (orientation == UIDeviceOrientationFaceUp || orientation == UIDeviceOrientationFaceDown || orientation == UIDeviceOrientationUnknown || currentOrientation == orientation) {
    return;
  }
  [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(relayoutLayers) object:nil];
  //Responding only to changes in landscape or portrait
  currentOrientation = orientation;
  [self performSelector:@selector(orientationChanged) withObject:nil afterDelay:0];
}

- (void) orientationChanged
{
  [self setVisible: [self visible]];
}

- (void) dealloc
{
  [self clear];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
