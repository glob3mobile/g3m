//
//  ViewController.h
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <G3MiOSSDK/G3MWidget_iOS.h>


#import "CameraViewController.h"


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
class MyEDCamConstrainer;
class GEOVectorLayer;

@interface ViewController : UIViewController <UIPickerViewDelegate, UIAlertViewDelegate>  {
  IBOutlet G3MWidget_iOS* G3MWidget;

  MapBooBuilder_iOS* _g3mcBuilder;
  
  const Planet * _planet;

  HUDRenderer* _hudRenderer;
  
  struct CityGMLModelFile{
    std::string _fileName;
    bool _needsToBeFixedOnGround;
  };
  
  std::vector<CityGMLModelFile> _cityGMLFiles;
  
  
  std::vector<std::string> _pointCloudFiles;
  size_t _modelsLoadedCounter;
  size_t _pointCloudsLoaded;
  
  std::vector<Mesh*> _pointClouds;
  
  NSArray* _pickerArray;
  bool _useDem;
  bool _isMenuAvailable;
  
  //VR
  Geodetic3D* _prevPos;
  Angle* _prevHeading;
  Angle* _prevRoll;
  Angle* _prevPitch;
  
  //VC
  CameraViewController* _camVC;
  
  __weak IBOutlet UIButton *_showMenuButton;
  __weak IBOutlet NSLayoutConstraint *_menuHeightConstraint;
  __weak IBOutlet UIView *_menuView;
  __weak IBOutlet UIView *_waitingMessageView;
  __weak IBOutlet UIProgressView *_progressBar;
  __weak IBOutlet UIPickerView *_dataPicker;
  __weak IBOutlet UIView *cameraView;
  __weak IBOutlet UIStackView *_headerView;
}

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@property __weak IBOutlet UILabel *_timeLabel;
@property MeshRenderer* meshRenderer;
@property MeshRenderer* meshRendererPC;
@property MarksRenderer* marksRenderer;
@property CityGMLRenderer* cityGMLRenderer;

@property GEOVectorLayer* vectorLayer;

@property const ElevationData* elevationData;
@property MyEDCamConstrainer* camConstrainer;

-(void) createPointCloudWithDescriptor:(const std::string&) pointCloudDescriptor;
//-(void) addBuildings:(const std::vector<CityGMLBuilding*>&) buildings
//     withThreadUtils: (const IThreadUtils*) threadUtils;

-(void) requestPointCloud;
-(void) loadCityModelWithThreadUtils;

-(void) onCityModelLoaded;
-(void) onPointCloudLoaded;
-(void) onProgress;
-(void) loadSolarRadiationPointCloud;

-(IBAction)switchVR:(id)sender;

@end
