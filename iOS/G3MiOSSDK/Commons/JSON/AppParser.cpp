//
//  AppParser.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 18/10/12.
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

const std::string AppParser::WORLD = "_world";
const std::string AppParser::BASELAYER = "_baselayer";
const std::string AppParser::BBOX = "_bbox";
const std::string AppParser::CUSTOMDATA = "_customdata";
const std::string AppParser::FEATURES = "features";
const std::string AppParser::GEOMETRY = "geometry";
const std::string AppParser::TYPE = "type";
const std::string AppParser::COORDINATES = "coordinates";
const std::string AppParser::PROPERTIES = "properties";
const std::string AppParser::NAME = "name";

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
  parseWorldConfiguration(layerSet, marks, json->getObjectForKey(WORLD)->getObject());
  IJSONParser::instance()->deleteJSONData(json);
}

void AppParser::parseWorldConfiguration(LayerSet* layerSet, MarksRenderer* marks, JSONObject* jsonWorld){
  std::string jsonBaseLayer = jsonWorld->getObjectForKey(BASELAYER)->getString()->getValue();
  JSONArray* jsonBbox = jsonWorld->getObjectForKey(BBOX)->getArray();
  
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
  parseCustomData(marks, jsonWorld->getObjectForKey(CUSTOMDATA)->getObject());
}

void AppParser::parseCustomData(MarksRenderer* marks, JSONObject* jsonCustomData){
  JSONArray* jsonFeatures = jsonCustomData->getObjectForKey(FEATURES)->getArray();
  for (int i = 0; i < jsonFeatures->getSize(); i++) {
    JSONObject* jsonFeature = jsonFeatures->getElement(i)->getObject();
      JSONObject* jsonGeometry = jsonFeature->getObjectForKey(GEOMETRY)->getObject();
    std::string jsonType = jsonGeometry->getObjectForKey(TYPE)->getString()->getValue();
    if (jsonType == "Point"){
      parseGEOJSONPointObject(marks, jsonFeature);
    }
  }
}
  
void AppParser::parseGEOJSONPointObject(MarksRenderer* marks, JSONObject* point){
    JSONObject* jsonProperties = point->getObjectForKey(PROPERTIES)->getObject();
    JSONObject* jsonGeometry = point->getObjectForKey(GEOMETRY)->getObject();
    JSONArray* jsonCoordinates = jsonGeometry->getObjectForKey(COORDINATES)->getArray();
    
    Mark* mark = new Mark(jsonProperties->getObjectForKey(NAME)->getString()->getValue(),
                        URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png"),
                          Geodetic3D(Angle::fromDegrees(jsonCoordinates->getElement(1)->getNumber()->getDoubleValue()), Angle::fromDegrees(jsonCoordinates->getElement(0)->getNumber()->getDoubleValue()), 0));
    marks->addMark(mark);
}