//
//  G3MViewController.m
//  G3MApp
//
//  Created by Mari Luz Mateo on 18/02/13.
//

#import "G3MViewController.h"

#import <G3MiOSSDK/G3MWidget_iOS.h>
#import <G3MiOSSDK/G3MBuilder_iOS.hpp>
#import <G3MiOSSDK/NSString_CppAdditions.h>

#import "G3MSelectDemoSceneViewController.h"
#import "G3MSelectOptionViewController.h"

#include "G3MDemoBuilder_iOS.hpp"
#include "G3MDemoModel.hpp"
#include "G3MDemoScene.hpp"
#include "G3MDemoListener.hpp"



@implementation G3MViewController

- (BOOL) prefersStatusBarHidden {
  return YES;
}

class DemoListener : public G3MDemoListener {
private:
  G3MViewController* _viewController;

public:
  DemoListener(G3MViewController* viewController) :
  _viewController(viewController)
  {
  }

  void onChangedScene(const G3MDemoScene* scene) {
    [_viewController onChangedScene: scene];
  }


  void onChangeSceneOption(G3MDemoScene* scene,
                           const std::string& option,
                           int optionIndex) {
    [_viewController onChangedOption: option
                             inScene: scene];
  }

  void showDialog(const std::string& title,
                  const std::string& message) const {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle: [NSString stringWithCppString:title]
                                                    message: [NSString stringWithCppString:message]
                                                   delegate: nil
                                          cancelButtonTitle: @"OK"
                                          otherButtonTitles: nil];
    [alert show];
  }
};

-(void) onChangedScene:(const G3MDemoScene*) scene
{
  [self.demoSelector setTitle: [NSString stringWithCppString: scene->getName()]
                     forState: UIControlStateNormal];

  [self.optionSelector setTitle: [NSString stringWithCppString: scene->getOptionSelectorDefaultTitle()]
                       forState: UIControlStateNormal];
  self.optionSelector.hidden = (scene->getOptionsCount() == 0);
}

-(void) onChangedOption:(const std::string&) option
                inScene:(const G3MDemoScene*) scene
{
  [self.optionSelector setTitle: [NSString stringWithCppString: option]
                       forState: UIControlStateNormal];
}

- (void)viewDidLoad
{
  [super viewDidLoad];

  G3MDemoListener* listener = new DemoListener(self);

  G3MDemoBuilder_iOS demoBuilder(new G3MBuilder_iOS(self.g3mWidget),
                                 listener);
  demoBuilder.initializeWidget();

  _demoModel = demoBuilder.getModel();
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];

  // Let's get the show on the road!
  [self.g3mWidget startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  // Stop the glob3 render
  [self.g3mWidget stopAnimation];

  [super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  self.g3mWidget        = nil;
  self.demoSelector     = nil;
  self.optionSelector   = nil;

  [super viewDidUnload];
}

- (void)dealloc
{
  delete _demoModel;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
  // Return YES for supported orientations
  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  }
  else {
    return YES;
  }
}

- (void)viewWillAppear:(BOOL)animated {
  [super viewWillAppear:animated];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue
                 sender:(id)sender {

  //NSLog(@"Prepare for segue: %@", [segue identifier]);

  if ([segue.destinationViewController isKindOfClass:[G3MSelectDemoSceneViewController class]]) {
    G3MSelectDemoSceneViewController* viewController = (G3MSelectDemoSceneViewController*) segue.destinationViewController;

    if ( [segue isKindOfClass:[UIStoryboardPopoverSegue class]]) {
      UIStoryboardPopoverSegue* popoverSegue = (UIStoryboardPopoverSegue*)segue;
      viewController.popoverController = popoverSegue.popoverController;
    }

    viewController.demoModel = _demoModel;
  }

  if ([segue.destinationViewController isKindOfClass:[G3MSelectOptionViewController class]]) {
    G3MSelectOptionViewController* viewController = (G3MSelectOptionViewController*) segue.destinationViewController;

    if ( [segue isKindOfClass:[UIStoryboardPopoverSegue class]]) {
      UIStoryboardPopoverSegue* popoverSegue = (UIStoryboardPopoverSegue*)segue;
      viewController.popoverController = popoverSegue.popoverController;
    }

    viewController.scene = _demoModel->getSelectedScene();
  }
}

@end
