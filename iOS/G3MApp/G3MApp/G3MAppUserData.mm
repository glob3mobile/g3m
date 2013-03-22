//
//  G3MAppUserDataWrapper.mm
//  G3MApp
//
//  Created by Mari Luz Mateo on 22/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MAppUserData.hpp"

void G3MAppUserData::setLayerSet(LayerSet* layerSet) {
  _layerSet = layerSet;
}

LayerSet* G3MAppUserData::getLayerSet() {
  return _layerSet;
}


void G3MAppUserData::setSatelliteLayerEnabled(bool satelliteLayerEnabled) {
  _satelliteLayerEnabled = satelliteLayerEnabled;
}

bool G3MAppUserData::getSatelliteLayerEnabled() {
  return _satelliteLayerEnabled;
}

void G3MAppUserData::setPlane(Shape* plane) {
  _plane = plane;
}

Shape* G3MAppUserData::getPlane() {
  return _plane;
}


void G3MAppUserData::setShapeRenderer(ShapesRenderer* shapeRenderer) {
  _shapeRenderer = shapeRenderer;
}

ShapesRenderer* G3MAppUserData::getShapeRenderer() {
  return _shapeRenderer;
}


void G3MAppUserData::setMarkerRenderer(MarksRenderer* markerRenderer) {
  _markerRenderer = markerRenderer;
}

MarksRenderer* G3MAppUserData::getMarkerRenderer() {
  return _markerRenderer;
}


void G3MAppUserData::setMeshRenderer(MeshRenderer* meshRenderer) {
  _meshRenderer = meshRenderer;
}

MeshRenderer* G3MAppUserData::getMeshRenderer() {
  return _meshRenderer;
}