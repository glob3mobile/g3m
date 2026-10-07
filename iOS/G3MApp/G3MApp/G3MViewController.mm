//
//  G3MViewController.m
//  G3MApp
//
//  Created by Mari Luz Mateo on 18/02/13.
//

#import "G3MViewController.h"
#import "G3MColorGradingPanel.h"

#import <G3MiOSSDK/G3MWidget_iOS.h>
#import <G3MiOSSDK/G3MBuilder_iOS.hpp>
#import <G3MiOSSDK/NSString_CppAdditions.h>

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
                           size_t groupIndex,
                           const std::string& option,
                           int optionIndex) {
    [_viewController onChangedOptionInGroup: groupIndex
                                    inScene: scene];
  }

  void showDialog(const std::string& title,
                  const std::string& message) const {
    UIAlertController* alert = [UIAlertController alertControllerWithTitle: [NSString stringWithCppString:title]
                                                                   message: [NSString stringWithCppString:message]
                                                            preferredStyle: UIAlertControllerStyleAlert];
    [alert addAction: [UIAlertAction actionWithTitle: @"OK"
                                               style: UIAlertActionStyleDefault
                                             handler: nil]];
    [_viewController presentViewController: alert
                                  animated: YES
                                completion: nil];
  }

  void showColorGradingPanel(G3MColorGradingDemoScene* scene) {
    [_viewController showColorGradingPanel: scene];
  }

  void hideColorGradingPanel() {
    [_viewController hideColorGradingPanel];
  }
};

+(UIButton*) createSelector
{
  UIButtonConfiguration* configuration;
  if (@available(iOS 26.0, *)) {
    configuration = [UIButtonConfiguration glassButtonConfiguration];
  }
  else {
    configuration = [UIButtonConfiguration grayButtonConfiguration];
  }
  configuration.cornerStyle        = UIButtonConfigurationCornerStyleCapsule;
  configuration.image              = [UIImage systemImageNamed: @"chevron.up.chevron.down"];
  configuration.imagePlacement     = NSDirectionalRectEdgeTrailing;
  configuration.imagePadding       = 6;
  configuration.titleLineBreakMode = NSLineBreakByTruncatingMiddle;

  UIButton* selector = [UIButton buttonWithConfiguration: configuration
                                           primaryAction: nil];
  selector.showsMenuAsPrimaryAction                  = YES;
  selector.preferredMenuElementOrder                 = UIContextMenuConfigurationElementOrderFixed;
  selector.translatesAutoresizingMaskIntoConstraints = NO;
  return selector;
}

+(void) setTitle:(const std::string&) title
        subtitle:(const std::string&) subtitle
      ofSelector:(UIButton*) selector
{
  UIButtonConfiguration* configuration = selector.configuration;
  configuration.title    = [NSString stringWithCppString: title];
  configuration.subtitle = subtitle.empty() ? nil : [NSString stringWithCppString: subtitle];
  selector.configuration = configuration;
}

-(void) createSelectors
{
  UILayoutGuide* safeArea = self.view.safeAreaLayoutGuide;

  _demoSelector = [G3MViewController createSelector];
  [self.view addSubview: _demoSelector];

  _optionSelectors = [[UIStackView alloc] init];
  _optionSelectors.axis      = UILayoutConstraintAxisVertical;
  _optionSelectors.alignment = UIStackViewAlignmentTrailing;
  _optionSelectors.spacing   = 8;
  _optionSelectors.translatesAutoresizingMaskIntoConstraints = NO;
  [self.view addSubview: _optionSelectors];

  [NSLayoutConstraint activateConstraints: @[
    [_demoSelector.topAnchor                constraintEqualToAnchor: safeArea.topAnchor               constant: 8],
    [_demoSelector.trailingAnchor           constraintEqualToAnchor: safeArea.trailingAnchor          constant: -20],
    [_demoSelector.leadingAnchor            constraintGreaterThanOrEqualToAnchor: safeArea.leadingAnchor constant: 20],
    [_optionSelectors.bottomAnchor          constraintEqualToAnchor: safeArea.bottomAnchor            constant: -20],
    [_optionSelectors.trailingAnchor        constraintEqualToAnchor: safeArea.trailingAnchor          constant: -20],
    [_optionSelectors.leadingAnchor         constraintGreaterThanOrEqualToAnchor: safeArea.leadingAnchor constant: 20]
  ]];
}

-(UIMenu*) createDemoMenu
{
  NSMutableArray<UIMenuElement*>* actions = [NSMutableArray array];
  for (size_t i = 0; i < _demoModel->getScenesCount(); i++) {
    const G3MDemoScene* scene = _demoModel->getScene(i);
    const std::string sceneName = scene->getName();
    G3MDemoModel* demoModel = _demoModel;
    UIAction* action = [UIAction actionWithTitle: [NSString stringWithCppString: sceneName]
                                           image: nil
                                      identifier: nil
                                         handler: ^(UIAction* _) {
      demoModel->selectScene(sceneName);
    }];
    action.state = _demoModel->isSelectedScene(scene) ? UIMenuElementStateOn : UIMenuElementStateOff;
    [actions addObject: action];
  }
  return [UIMenu menuWithChildren: actions];
}

-(UIMenu*) createMenuForGroup:(size_t) groupIndex
                      ofScene:(G3MDemoScene*) scene
{
  const G3MDemoOptionGroup* group = scene->getOptionGroup(groupIndex);
  NSMutableArray<UIMenuElement*>* actions = [NSMutableArray array];
  for (size_t i = 0; i < group->getOptionsCount(); i++) {
    const std::string option = group->getOption(i);
    UIAction* action = [UIAction actionWithTitle: [NSString stringWithCppString: option]
                                           image: nil
                                      identifier: nil
                                         handler: ^(UIAction* _) {
      scene->selectOption(groupIndex, option);
    }];
    action.state = group->isSelectedOption(option) ? UIMenuElementStateOn : UIMenuElementStateOff;
    [actions addObject: action];
  }
  return [UIMenu menuWithTitle: [NSString stringWithCppString: group->getName()]
                      children: actions];
}

-(void) updateSelector:(UIButton*) selector
              forGroup:(size_t) groupIndex
               ofScene:(G3MDemoScene*) scene
{
  const G3MDemoOptionGroup* group = scene->getOptionGroup(groupIndex);
  [G3MViewController setTitle: group->getTitle()
                     subtitle: group->getName()
                   ofSelector: selector];
  selector.menu   = [self createMenuForGroup: groupIndex
                                     ofScene: scene];
  selector.hidden = (group->getOptionsCount() == 0);
}

-(void) onChangedScene:(const G3MDemoScene*) scene
{
  [G3MViewController setTitle: scene->getName()
                     subtitle: ""
                   ofSelector: _demoSelector];
  _demoSelector.menu = [self createDemoMenu];

  for (UIView* selector in _optionSelectors.arrangedSubviews) {
    [selector removeFromSuperview];
  }

  G3MDemoScene* selectedScene = _demoModel->getSelectedScene();
  for (size_t groupIndex = 0; groupIndex < selectedScene->getOptionGroupsCount(); groupIndex++) {
    UIButton* selector = [G3MViewController createSelector];
    [self updateSelector: selector
                forGroup: groupIndex
                 ofScene: selectedScene];
    [_optionSelectors addArrangedSubview: selector];
  }
}

-(void) onChangedOptionInGroup:(size_t) groupIndex
                       inScene:(const G3MDemoScene*) scene
{
  UIButton* selector = (UIButton*) _optionSelectors.arrangedSubviews[groupIndex];
  [self updateSelector: selector
              forGroup: groupIndex
               ofScene: _demoModel->getSelectedScene()];
}

-(void) showColorGradingPanel:(G3MColorGradingDemoScene*) scene
{
  [self hideColorGradingPanel];

  _colorGradingPanel = [[G3MColorGradingPanel alloc] initWithScene: scene];
  _colorGradingPanel.translatesAutoresizingMaskIntoConstraints = NO;
  [self.view addSubview: _colorGradingPanel];

  UILayoutGuide* safeArea = self.view.safeAreaLayoutGuide;
  NSLayoutConstraint* preferredWidth = [_colorGradingPanel.widthAnchor constraintEqualToConstant: 360];
  preferredWidth.priority = UILayoutPriorityDefaultHigh;
  [NSLayoutConstraint activateConstraints: @[
    [_colorGradingPanel.bottomAnchor   constraintEqualToAnchor: _optionSelectors.topAnchor               constant: -12],
    [_colorGradingPanel.trailingAnchor constraintEqualToAnchor: safeArea.trailingAnchor                  constant: -20],
    [_colorGradingPanel.leadingAnchor  constraintGreaterThanOrEqualToAnchor: safeArea.leadingAnchor      constant: 20],
    preferredWidth
  ]];
}

-(void) hideColorGradingPanel
{
  [_colorGradingPanel removeFromSuperview];
  _colorGradingPanel = nil;
}

- (void)viewDidLoad
{
  [super viewDidLoad];

  [self createSelectors];

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
  self.g3mWidget = nil;

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

@end
