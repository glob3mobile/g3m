//
//  MenuViewController.m
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 30/10/15.
//
//

#import "MenuViewController.h"

#import "ViewController.h"

#import <G3MiOSSDK/MarksRenderer.hpp>

#import "AppDelegate.h"



@interface MenuViewController ()

@end



@implementation MenuViewController

@synthesize textField;
@synthesize _theVC;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
  
  
  AppDelegate* delegate = [UIApplication sharedApplication].delegate;
  
  galaxiesSwitch.on = delegate.showingGalaxies;
  
  switchStars0.on = [delegate areStarsActive:0];
  switchStars1.on = [delegate areStarsActive:1];
  switchStars2.on = [delegate areStarsActive:2];
  switchStars3.on = [delegate areStarsActive:3];
  switchStars4.on = [delegate areStarsActive:4];

}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  //galaxiesSwitch.on = _theVC._galaxies->isEnable();
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(IBAction)backToMenu:(id)sender{
  [self performSegueWithIdentifier:@"back" sender:nil];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

-(NSString*) readFile: (NSString*) file{
  
  NSString* path = [[NSBundle mainBundle] pathForResource:file
                                                   ofType:@"txt"];
  
  NSString* content = [NSString stringWithContentsOfFile:path
                                                encoding:NSUTF8StringEncoding
                                                   error:NULL];
  
  return content;
  
}

-(IBAction)showText:(id)sender{
  
  switch ([sender tag]) {
    case 0:
      textField.text = [self readFile:@"zodiaco"];
      break;
    case 1:
      textField.text = [self readFile:@"norte"];
      break;
    case 2:
      textField.text = [self readFile:@"heroes"];
      break;
    case 3:
      textField.text = [self readFile:@"cefeo"];
      break;
    case 4:
      textField.text = [self readFile:@"lactea"];
      break;
    default:
      break;
  }
  
  
}

-(IBAction)showGalaxies:(id)sender{
  bool v = ((UISwitch*) sender).on ;
  ((AppDelegate*)[UIApplication sharedApplication].delegate).showingGalaxies = v;
}

-(IBAction)switchStars:(id)sender{
  bool v = ((UISwitch*) sender).on ;
  int i = ((UISwitch*) sender).tag;
  [((AppDelegate*)[UIApplication sharedApplication].delegate) setStarsActive:i value:v];
}

@end
