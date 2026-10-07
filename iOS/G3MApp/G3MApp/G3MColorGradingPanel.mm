//
//  G3MColorGradingPanel.mm
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#import "G3MColorGradingPanel.h"

#include <G3M/Angle.hpp>
#include <G3M/Color.hpp>

#include "G3MColorGradingDemoScene.hpp"


@implementation G3MColorGradingPanel {
  G3MColorGradingDemoScene* _scene;

  UISlider*    _saturationSlider;
  UISlider*    _hueSlider;
  UISlider*    _contrastSlider;
  UISlider*    _brightnessSlider;
  UIColorWell* _tintWell;
  UISlider*    _tintIntensitySlider;

  UILabel* _saturationValue;
  UILabel* _hueValue;
  UILabel* _contrastValue;
  UILabel* _brightnessValue;
  UILabel* _tintIntensityValue;
}

+(UIVisualEffect*) createBackgroundEffect
{
  if (@available(iOS 26.0, *)) {
    return [UIGlassEffect effectWithStyle: UIGlassEffectStyleClear];
  }
  return [UIBlurEffect effectWithStyle: UIBlurEffectStyleSystemUltraThinMaterial];
}

+(UILabel*) createLabel:(NSString*) text
{
  UILabel* label = [[UILabel alloc] init];
  label.text = text;
  label.font = [UIFont preferredFontForTextStyle: UIFontTextStyleFootnote];
  label.textColor = [UIColor labelColor];
  return label;
}

+(UILabel*) createValueLabel
{
  UILabel* label = [G3MColorGradingPanel createLabel: @""];
  label.font = [UIFont monospacedDigitSystemFontOfSize: label.font.pointSize
                                                weight: UIFontWeightRegular];
  label.textAlignment = NSTextAlignmentRight;
  [label.widthAnchor constraintEqualToConstant: 44].active = YES;
  return label;
}

-(UISlider*) createSliderFrom:(float) minimum
                           to:(float) maximum
{
  UISlider* slider = [[UISlider alloc] init];
  slider.minimumValue = minimum;
  slider.maximumValue = maximum;
  [slider addTarget: self
             action: @selector(onChange)
   forControlEvents: UIControlEventValueChanged];
  return slider;
}

+(UIStackView*) createRowNamed:(NSString*) name
                       control:(UIView*) control
                         value:(UIView*) value
{
  UILabel* nameLabel = [G3MColorGradingPanel createLabel: name];
  [nameLabel.widthAnchor constraintEqualToConstant: 76].active = YES;

  NSMutableArray<UIView*>* views = [NSMutableArray arrayWithObjects: nameLabel, control, nil];
  if (value != nil) {
    [views addObject: value];
  }
  UIStackView* row = [[UIStackView alloc] initWithArrangedSubviews: views];
  row.axis      = UILayoutConstraintAxisHorizontal;
  row.alignment = UIStackViewAlignmentCenter;
  row.spacing   = 8;
  return row;
}

-(UIButton*) createButtonTitled:(NSString*) title
                         action:(SEL) action
{
  UIButtonConfiguration* configuration;
  if (@available(iOS 26.0, *)) {
    configuration = [UIButtonConfiguration glassButtonConfiguration];
  }
  else {
    configuration = [UIButtonConfiguration grayButtonConfiguration];
  }
  configuration.title       = title;
  configuration.cornerStyle = UIButtonConfigurationCornerStyleCapsule;
  UIButton* button = [UIButton buttonWithConfiguration: configuration
                                         primaryAction: nil];
  [button addTarget: self
             action: action
   forControlEvents: UIControlEventTouchUpInside];
  return button;
}

-(instancetype) initWithScene:(G3MColorGradingDemoScene*) scene
{
  self = [super initWithFrame: CGRectZero];
  if (self) {
    _scene = scene;

    UIVisualEffectView* background = [[UIVisualEffectView alloc] initWithEffect: [G3MColorGradingPanel createBackgroundEffect]];
    background.layer.cornerRadius  = 24;
    background.layer.cornerCurve   = kCACornerCurveContinuous;
    background.clipsToBounds       = YES;
    background.translatesAutoresizingMaskIntoConstraints = NO;
    [self addSubview: background];

    _saturationSlider    = [self createSliderFrom: 0    to: 2];
    _hueSlider           = [self createSliderFrom: -180 to: 180];
    _contrastSlider      = [self createSliderFrom: 0    to: 2];
    _brightnessSlider    = [self createSliderFrom: 0    to: 2];
    _tintIntensitySlider = [self createSliderFrom: 0    to: 1];

    _tintWell = [[UIColorWell alloc] init];
    _tintWell.supportsAlpha = NO;
    _tintWell.title         = @"Tint";
    [_tintWell addTarget: self
                  action: @selector(onChange)
        forControlEvents: UIControlEventValueChanged];

    _saturationValue    = [G3MColorGradingPanel createValueLabel];
    _hueValue           = [G3MColorGradingPanel createValueLabel];
    _contrastValue      = [G3MColorGradingPanel createValueLabel];
    _brightnessValue    = [G3MColorGradingPanel createValueLabel];
    _tintIntensityValue = [G3MColorGradingPanel createValueLabel];

    UIStackView* tintControls = [[UIStackView alloc] initWithArrangedSubviews: @[_tintWell, _tintIntensitySlider]];
    tintControls.axis      = UILayoutConstraintAxisHorizontal;
    tintControls.alignment = UIStackViewAlignmentCenter;
    tintControls.spacing   = 8;

    UIStackView* buttons = [[UIStackView alloc] initWithArrangedSubviews: @[[self createButtonTitled: @"Reset" action: @selector(reset)],
                                                                            [self createButtonTitled: @"Log"   action: @selector(log)]]];
    buttons.axis         = UILayoutConstraintAxisHorizontal;
    buttons.distribution = UIStackViewDistributionFillEqually;
    buttons.spacing      = 8;

    UIStackView* rows = [[UIStackView alloc] initWithArrangedSubviews: @[
      [G3MColorGradingPanel createRowNamed: @"Saturation" control: _saturationSlider    value: _saturationValue],
      [G3MColorGradingPanel createRowNamed: @"Hue"        control: _hueSlider           value: _hueValue],
      [G3MColorGradingPanel createRowNamed: @"Contrast"   control: _contrastSlider      value: _contrastValue],
      [G3MColorGradingPanel createRowNamed: @"Brightness" control: _brightnessSlider    value: _brightnessValue],
      [G3MColorGradingPanel createRowNamed: @"Tint"       control: tintControls         value: _tintIntensityValue],
      buttons
    ]];
    rows.axis    = UILayoutConstraintAxisVertical;
    rows.spacing = 6;
    rows.translatesAutoresizingMaskIntoConstraints = NO;
    [background.contentView addSubview: rows];

    [NSLayoutConstraint activateConstraints: @[
      [background.topAnchor      constraintEqualToAnchor: self.topAnchor],
      [background.bottomAnchor   constraintEqualToAnchor: self.bottomAnchor],
      [background.leadingAnchor  constraintEqualToAnchor: self.leadingAnchor],
      [background.trailingAnchor constraintEqualToAnchor: self.trailingAnchor],
      [rows.topAnchor            constraintEqualToAnchor: background.contentView.topAnchor      constant: 14],
      [rows.bottomAnchor         constraintEqualToAnchor: background.contentView.bottomAnchor   constant: -14],
      [rows.leadingAnchor        constraintEqualToAnchor: background.contentView.leadingAnchor  constant: 16],
      [rows.trailingAnchor       constraintEqualToAnchor: background.contentView.trailingAnchor constant: -16]
    ]];

    [self reset];
  }
  return self;
}

-(void) reset
{
  _saturationSlider.value    = 1;
  _hueSlider.value           = 0;
  _contrastSlider.value      = 1;
  _brightnessSlider.value    = 1;
  _tintWell.selectedColor    = [UIColor whiteColor];
  _tintIntensitySlider.value = 0;
  [self onChange];
}

-(Color) tint
{
  CGFloat red, green, blue, alpha;
  [_tintWell.selectedColor getRed: &red green: &green blue: &blue alpha: &alpha];
  return Color::fromRGBA((float) red, (float) green, (float) blue, 1);
}

-(void) onChange
{
  _saturationValue.text    = [NSString stringWithFormat: @"%.2f",  _saturationSlider.value];
  _hueValue.text           = [NSString stringWithFormat: @"%.0f°", _hueSlider.value];
  _contrastValue.text      = [NSString stringWithFormat: @"%.2f",  _contrastSlider.value];
  _brightnessValue.text    = [NSString stringWithFormat: @"%.2f",  _brightnessSlider.value];
  _tintIntensityValue.text = [NSString stringWithFormat: @"%.2f",  _tintIntensitySlider.value];

  _scene->setCustomGrade(_saturationSlider.value,
                         Angle::fromDegrees(_hueSlider.value),
                         _contrastSlider.value,
                         _brightnessSlider.value,
                         [self tint],
                         _tintIntensitySlider.value);
}

-(void) log
{
  _scene->logCustomGrade(_saturationSlider.value,
                         Angle::fromDegrees(_hueSlider.value),
                         _contrastSlider.value,
                         _brightnessSlider.value,
                         [self tint],
                         _tintIntensitySlider.value);
}

@end
