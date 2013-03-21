//
//  G3MAppUserDataWrapper.hpp
//  G3MApp
//
//  Created by Mari Luz Mateo on 22/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <G3MiOSSDK/G3MWidget.hpp>
class LayerSet;
class Shape;
class ShapesRenderer;
class MarksRenderer;
class MeshRenderer;

class G3MAppUserData : public WidgetUserData {
  
private:
  LayerSet* _layerSet;
  bool _satelliteLayerEnabled;
  Shape* _plane;
  ShapesRenderer* _shapeRenderer;
  MarksRenderer* _markerRenderer;
  MeshRenderer* _meshRenderer;
  
public:
  G3MAppUserData() {}
  
  void setLayerSet(LayerSet* layerSet);
  LayerSet* getLayerSet();
  
  void setSatelliteLayerEnabled(bool satelliteLayerEnabled);
  bool getSatelliteLayerEnabled();
  
  void setPlane(Shape* plane);
  Shape* getPlane();
  
  void setShapeRenderer(ShapesRenderer* shapeRenderer);
  ShapesRenderer* getShapeRenderer();
  
  void setMarkerRenderer(MarksRenderer* markerRenderer);
  MarksRenderer* getMarkerRenderer();
  
  void setMeshRenderer(MeshRenderer* meshRenderer);
  MeshRenderer* getMeshRenderer();
};
