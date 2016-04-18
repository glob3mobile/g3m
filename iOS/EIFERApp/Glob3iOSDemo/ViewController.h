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


@interface ViewController : UIViewController <UIPickerViewDelegate>  {
  IBOutlet G3MWidget_iOS* G3MWidget;

  MapBooBuilder_iOS* _g3mcBuilder;
  
  const Planet * _planet;
  MeshRenderer* _meshRenderer;
  MarksRenderer* _marksRenderer;
  HUDRenderer* _hudRenderer;
  
  LabelImageBuilder* _labelBuilder;
  
  std::vector<std::string> _cityGMLFiles;
  size_t _modelsLoadedCounter;
  
  std::vector<CityGMLBuilding*> _buildings;
  
  NSArray* _pickerArray;
}

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

-(void) createPointCloud:(ElevationData*) ed withDescriptor:(const std::string&) pointCloudDescriptor;
-(void) addBuildings:(const std::vector<CityGMLBuilding*>&) buildings withED:(const ElevationData*) ed;

-(void) requestPointCloud:(ElevationData*) ed;
-(void) loadCityModel:(ElevationData*) ed;

-(IBAction)switchVR:(id)sender;

@end
