//
//  ViewController.h
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <G3MiOSSDK/G3MWidget_iOS.h>
class MapBooBuilder_iOS;

class Sector;
class Mesh;
class MeshRenderer;
class MarksRenderer;
class HUDRenderer;
class LabelImageBuilder;
class ElevationData;
class CityGMLBuilding;
class CityGMLRenderer;


@interface ViewController : UIViewController <UIPickerViewDelegate>  {
  IBOutlet G3MWidget_iOS* G3MWidget;

  MapBooBuilder_iOS* _g3mcBuilder;
  
  const Planet * _planet;

  HUDRenderer* _hudRenderer;
  
//  LabelImageBuilder* _labelBuilder;
  
  std::vector<std::string> _cityGMLFiles;
  std::vector<std::string> _pointCloudFiles;
  size_t _modelsLoadedCounter;
  size_t _pointCloudsLoaded;
  
  std::vector<Mesh*> _pointClouds;
  
//  std::vector<CityGMLBuilding*> _buildings;
  
  NSArray* _pickerArray;
  bool _useDem;
  
  __weak IBOutlet UIButton *_showMenuButton;
  __weak IBOutlet NSLayoutConstraint *_menuHeightConstraint;
  __weak IBOutlet UIView *_menuView;
  __weak IBOutlet UIView *_waitingMessageView;
  __weak IBOutlet UIProgressView *_progressBar;
}

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@property __weak IBOutlet UILabel *_timeLabel;
@property MeshRenderer* meshRenderer;
@property MarksRenderer* marksRenderer;
@property CityGMLRenderer* cityGMLRenderer;
@property const ElevationData* elevationData;

-(void) createPointCloudWithDescriptor:(const std::string&) pointCloudDescriptor;
//-(void) addBuildings:(const std::vector<CityGMLBuilding*>&) buildings
//     withThreadUtils: (const IThreadUtils*) threadUtils;

-(void) requestPointCloud;
-(void) loadCityModelWithThreadUtils;

-(void) onCityModelLoaded;
-(void) onPointCloudLoaded;
-(void) onProgress;

-(IBAction)switchVR:(id)sender;

@end
