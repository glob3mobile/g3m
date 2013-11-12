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
  LayerSet*       _layerSet;
  bool            _satelliteLayerEnabled;
  Shape*          _plane;
  ShapesRenderer* _shapeRenderer;
  MarksRenderer*  _marksRenderer;
  MeshRenderer*   _meshRenderer;

public:
  G3MAppUserData(LayerSet*       layerSet,
                 ShapesRenderer* shapeRenderer,
                 MarksRenderer*  marksRenderer,
                 MeshRenderer*   meshRenderer) :
  _layerSet(layerSet),
  _shapeRenderer(shapeRenderer),
  _marksRenderer(marksRenderer),
  _meshRenderer(meshRenderer)
  {
  }

  LayerSet* getLayerSet() {
    return _layerSet;
  }

  void setSatelliteLayerEnabled(bool satelliteLayerEnabled) {
    _satelliteLayerEnabled = satelliteLayerEnabled;
  }

  bool getSatelliteLayerEnabled() const {
    return _satelliteLayerEnabled;
  }

  void setPlane(Shape* plane) {
    _plane = plane;
  }

  Shape* getPlane() const {
    return _plane;
  }

  ShapesRenderer* getShapeRenderer() const {
    return _shapeRenderer;
  }

  MarksRenderer* getMarksRenderer() const {
    return _marksRenderer;
  }

  MeshRenderer* getMeshRenderer() const {
    return _meshRenderer;
  }
  
};
