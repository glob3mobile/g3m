//
//  AppParser.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 18/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "AppParser.hpp"

#include "IJSONParser.hpp"

#include "JSONBaseObject.hpp"
#include "JSONNumber.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONString.hpp"
#include "JSONBoolean.hpp"

#include "LayerSet.hpp"
#include "WMSLayer.hpp"

#include "MarksRenderer.hpp"

AppParser* AppParser::_instance = NULL;

AppParser* AppParser::instance(){
  if (_instance == NULL){
    _instance = new AppParser();
  }
  return _instance;
}

AppParser::AppParser(){
}

void AppParser::parse(LayerSet* layerSet, MarksRenderer* marks, std::string namelessParameter){
  JSONObject* json = IJSONParser::instance()->parse(namelessParameter)->getObject();
  parseWorldConfiguration(layerSet, marks, json->getObjectForKey(world)->getObject());
  IJSONParser::instance()->deleteJSONData(json);
}

void AppParser::parseWorldConfiguration(LayerSet* layerSet, MarksRenderer* marks, JSONObject* jsonWorld){
  std::string jsonBaseLayer = jsonWorld->getObjectForKey(baselayer)->getString()->getValue();
  JSONArray* jsonBbox = jsonWorld->getObjectForKey(bbox)->getArray();
  
  if (jsonBaseLayer == "BING"){
    WMSLayer* bing = new WMSLayer("ve",
                                  URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?"),
                                  WMS_1_1_0,
                                  Sector::fromDegrees(jsonBbox->getElement(1)->getNumber()->getDoubleValue(), jsonBbox->getElement(0)->getNumber()->getDoubleValue(), jsonBbox->getElement(3)->getNumber()->getDoubleValue(), jsonBbox->getElement(2)->getNumber()->getDoubleValue()),
                                  "image/jpeg",
                                  "EPSG:4326",
                                  "",
                                  false,
                                  NULL);
    layerSet->addLayer(bing);
  }else{
    WMSLayer* osm = new WMSLayer("osm",
                                 URL("http://wms.latlon.org/"),
                                 WMS_1_1_0,
                                 Sector::fromDegrees(jsonBbox->getElement(1)->getNumber()->getDoubleValue(), jsonBbox->getElement(0)->getNumber()->getDoubleValue(), jsonBbox->getElement(3)->getNumber()->getDoubleValue(), jsonBbox->getElement(2)->getNumber()->getDoubleValue()),                                 
                                 "image/jpeg",
                                 "EPSG:4326",
                                 "",
                                 false,
                                 NULL);
    layerSet->addLayer(osm);
  }
  parseCustomData(marks, jsonWorld->getObjectForKey(customdata)->getObject());
}

void AppParser::parseCustomData(MarksRenderer* marks, JSONObject* jsonCustomData){
  JSONArray* jsonFeatures = jsonCustomData->getObjectForKey(features)->getArray();
  for (int i = 0; i < jsonFeatures->getSize(); i++) {
    JSONObject* jsonFeature = jsonFeatures->getElement(i)->getObject();
    JSONObject* jsonGeometry = jsonFeature->getObjectForKey(geometry)->getObject();
    std::string jsonType = jsonGeometry->getObjectForKey(type)->getString()->getValue();
    if (jsonType == "Point"){
      parseGEOJSONPointObject(marks, jsonFeature);
    }
  }
}
  
void AppParser::parseGEOJSONPointObject(MarksRenderer* marks, JSONObject* point){
    JSONObject* jsonProperties = point->getObjectForKey(properties)->getObject();
    JSONObject* jsonGeometry = point->getObjectForKey(geometry)->getObject();
    JSONArray* jsonCoordinates = jsonGeometry->getObjectForKey(coordinates)->getArray();
    
    Mark* mark = new Mark(jsonProperties->getObjectForKey(name)->getString()->getValue(),
                        "g3m-marker.png",
                          Geodetic3D(Angle::fromDegrees(jsonCoordinates->getElement(1)->getNumber()->getDoubleValue()), Angle::fromDegrees(jsonCoordinates->getElement(0)->getNumber()->getDoubleValue()), 0));
    marks->addMark(mark);
}