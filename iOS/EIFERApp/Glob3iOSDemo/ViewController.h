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

#include <string>
#include <vector>


class MapBooBuilder_iOS;

class Sector;
class Mesh;
class MeshRenderer;
class MarksRenderer;
class PipesRenderer;
class HUDRenderer;
class LabelImageBuilder;
class ElevationData;
class CityGMLBuilding;
class CityGMLRenderer;
class MyEDCamConstrainer;
class GEOVectorLayer;
class PointCloudEvolutionTask;
class LayerSet;
class ShapesRenderer;
class Planet;
class DeviceAttitudeCameraHandler;
class Geodetic3D;
class CompositeElevationDataProvider;

@interface ViewController : UIViewController <UIPickerViewDelegate, UIAlertViewDelegate>  {
  IBOutlet G3MWidget_iOS* G3MWidget;
    __weak IBOutlet UIView *PositionView;

    __weak IBOutlet UIButton *PositionButton;
    __weak IBOutlet UILabel *PositionLabel;
  MapBooBuilder_iOS* _g3mcBuilder;
  
  const Planet * _planet;

  HUDRenderer* _hudRenderer;
  
  struct CityGMLModelFile{
    std::string _fileName;
    bool _needsToBeFixedOnGround;
  };
  
  std::vector<CityGMLModelFile> _cityGMLFiles;
  size_t _modelsLoadedCounter;
  
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
  
  //LayerSet
  LayerSet* layerSet;
  
  PointCloudEvolutionTask* _pointCloudTask;
  
  CityGMLBuilding* _buildingShowingPC;
  
  //Location Mode
  bool _locationUsesRealGPS;
  DeviceAttitudeCameraHandler* _dac;
  
  __weak IBOutlet UIButton *_showMenuButton;
  __weak IBOutlet NSLayoutConstraint *_menuHeightConstraint;
  __weak IBOutlet UIView *_menuView;
  __weak IBOutlet UIView *_waitingMessageView;
  __weak IBOutlet UIProgressView *_progressBar;
  __weak IBOutlet UIPickerView *_dataPicker;
  __weak IBOutlet UIView *cameraView;
  __weak IBOutlet UIStackView *_headerView;
  __weak IBOutlet UIButton *locationModeButton;
}

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@property __weak IBOutlet UILabel *timeLabel;
@property MeshRenderer* meshRenderer;
@property MeshRenderer* pipeMeshRenderer;
@property MeshRenderer* holeRenderer;
@property MeshRenderer* meshRendererPC;
@property MarksRenderer* marksRenderer;
@property MarksRenderer* falseMarksRenderer;
@property PipesRenderer* pipesRenderer;
@property ShapesRenderer* shapesRenderer;
@property CityGMLRenderer* cityGMLRenderer;

@property GEOVectorLayer* vectorLayer;

@property CompositeElevationDataProvider *combo;
@property const ElevationData *elevationData;
@property ElevationData *holeElevData;
@property MyEDCamConstrainer* camConstrainer;

-(void) loadCityModel;

-(void) onCityModelLoaded;
-(void) onProgress;
-(int) getMapMode;
-(void) addPointCloudMesh:(Mesh*) pc;
-(void) removePointCloudMesh;

-(void) loadSolarRadiationPointCloudForBuilding:(CityGMLBuilding*) building;

- (void) setBuildingsActive:(bool)active;
- (void) setPipesActive:(bool)active;
- (void) setCorrectionActive:(bool)active;
- (void) setMode:(int)activeMode;
- (void) setActiveColor:(int)row;
- (void) setWidgetAnimation:(bool)active;
- (void) setAlphaMethod:(int) alphaMethod;
- (void) setHole:(bool) enable;
- (void) setDitch:(bool) enable;

- (void) changeHole:(Geodetic3D) position;
- (void) addPipeMeshes;
- (bool) isHole;

- (Geodetic3D) getMarkPosition;
- (Angle) getMarkHeading;
- (void) activePositionFixer;
- (void) updatePositionFixer:(NSString *)message;
- (IBAction)PositionSetterAction:(id)sender;

@end
