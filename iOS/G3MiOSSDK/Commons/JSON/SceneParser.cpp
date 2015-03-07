//
//  SceneParser.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 15/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "SceneParser.hpp"

#include "IJSONParser.hpp"
#include "IStringBuilder.hpp"
#include "IStringUtils.hpp"

#include "JSONBaseObject.hpp"
#include "JSONNumber.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONString.hpp"
#include "JSONBoolean.hpp"

#include "WMSLayer.hpp"
#include "TMSLayer.hpp"
#include "LayerSet.hpp"

#include <stdio.h>
#include <string.h>

#include "MarksRenderer.hpp"
#include "IStringUtils.hpp"

#include "LayerTilesRenderParameters.hpp"
#include "Vector2I.hpp"

#include "LevelTileCondition.hpp"

using namespace std;

const std::string SceneParser::LAYERS = "layers";
const std::string SceneParser::TYPE = "type";
const std::string SceneParser::DATASOURCE = "datasource";
const std::string SceneParser::VERSION = "version";
const std::string SceneParser::MINLEVEL = "minlevel";
const std::string SceneParser::MAXLEVEL = "maxlevel";
const std::string SceneParser::BBOX = "bbox";
const std::string SceneParser::MINX = "minx";
const std::string SceneParser::MINY = "miny";
const std::string SceneParser::MAXX = "maxx";
const std::string SceneParser::MAXY = "maxy";
const std::string SceneParser::SPLITSLONGITUDE = "splitsLongitude";
const std::string SceneParser::SPLITSLATITUDE = "splitsLatitude";
const std::string SceneParser::ISTRANSPARENT = "isTransparent";
const std::string SceneParser::ISENABLED = "isEnabled";
const std::string SceneParser::ITEMS = "items";
const std::string SceneParser::STATUS = "status";
const std::string SceneParser::NAME = "name";
const std::string SceneParser::LEGENDNAME = "legendName";
const std::string SceneParser::URLICON = "urlIcon";
const std::string SceneParser::MINDISTANCE = "minDistance";
const std::string SceneParser::COLORLINE = "colorLine";
const std::string SceneParser::SIZELINE = "sizeLine";
const std::string SceneParser::URLWEB = "urlWeb";
const std::string SceneParser::SHOWLABEL = "showLabel";
const std::string SceneParser::WEB = "web";

const std::string SceneParser::WMS130 = "1.3.0";

SceneParser* SceneParser::_instance = NULL;

SceneParser* SceneParser::instance(){
  if (_instance == NULL){
    _instance = new SceneParser();
  }
  return _instance;
}

SceneParser::SceneParser(){
  _mapLayerType["WMS"] = WMS;
  _mapLayerType["TMS"] = TMS;
  _mapLayerType["THREED"] = THREED;
  _mapLayerType["PLANARIMAGE"] = PLANARIMAGE;
  _mapLayerType["GEOJSON"] = GEOJSON;
  _mapLayerType["SPHERICALIMAGE"] = SPHERICALIMAGE;
  
}

void SceneParser::parse(LayerSet* layerSet, std::string namelessParameter){
  
  _mapGeoJSONSources.clear();
  _panoSources.clear();
  _legend.clear();
  
  const JSONBaseObject* json = IJSONParser::instance()->parse(namelessParameter);
  parserJSONLayerList(layerSet, json->asObject()->getAsObject(LAYERS));
  IJSONParser::instance()->deleteJSONData(json);
}

void SceneParser::parserJSONLayerList(LayerSet* layerSet, const JSONObject* jsonLayers){
  for (int i = 0; i < jsonLayers->size(); i++) {
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addInt(i);
    const JSONObject* jsonLayer = jsonLayers->getAsObject(isb->getString());
    const layer_type layerType = _mapLayerType[jsonLayer->getAsString(TYPE)->value()];
    
    switch (layerType) {
      case WMS:
        parserJSONWMSLayer(layerSet, jsonLayer);
        break;
      case TMS:
        parserJSONTMSLayer(layerSet, jsonLayer);
        break;
      case THREED:
        parserJSON3DLayer(layerSet, jsonLayer);
        break;
      case PLANARIMAGE:
        parserJSONPlanarImageLayer(layerSet, jsonLayer);
        break;
      case GEOJSON:
        parserGEOJSONLayer(layerSet, jsonLayer);
        break;
      case SPHERICALIMAGE:
        parserJSONSphericalImageLayer(layerSet, jsonLayer);
        break;
    }
    delete isb;
  }
}

void SceneParser::parserJSONWMSLayer(LayerSet* layerSet, const JSONObject* jsonLayer){
  cout << "Parsing WMS Layer " << jsonLayer->getAsString(NAME)->value() << "..." << endl;
  
  const std::string jsonDatasource = jsonLayer->getAsString(DATASOURCE)->value();
  const int lastIndex = IStringUtils::instance()->indexOf(jsonDatasource,"?");
  const std::string jsonURL = IStringUtils::instance()->substring(jsonDatasource, 0, lastIndex+1);
  
  const std::string jsonVersion = jsonLayer->getAsString(VERSION)->value();
  
  const int jsonSplitsLat = atoi(jsonLayer->getAsString(SPLITSLATITUDE)->value().c_str());
  const int jsonSplitsLon = atoi(jsonLayer->getAsString(SPLITSLONGITUDE)->value().c_str());
  
  bool transparent = getValueBooleanParam(jsonLayer->getAsString(ISTRANSPARENT));
  
  std::string format = getFormat(transparent);
  
  LevelTileCondition* levelTileCondition = getLevelCondition(jsonLayer->getAsString(MINLEVEL),jsonLayer->getAsString(MAXLEVEL));
  Sector sector = getSector(jsonLayer->getAsObject(BBOX));
  
  const JSONArray* jsonItems = jsonLayer->getAsArray(ITEMS);
  IStringBuilder *layersName = IStringBuilder::newStringBuilder();
  
  for (int i = 0; i<jsonItems->size(); i++) {
    const JSONObject* jsOBJ = jsonItems->getAsObject(i);
    if (jsOBJ != NULL) {
      if (jsOBJ->getAsNumber(STATUS, 0) == 1 || jsOBJ->getAsBoolean(STATUS, false)) {
        layersName->addString(jsOBJ->getAsString(NAME)->value());
        layersName->addString(",");
      }
    }
  }
  std::string layersSecuence = layersName->getString();
  if (layersName->getString().length() > 0) {
    layersSecuence = IStringUtils::instance()->substring(layersSecuence, 0, layersSecuence.length()-1);
  }
  
  delete layersName;
  
  //TODO check if wms 1.1.1 is neccessary to have it into account
  WMSServerVersion wmsVersion = WMS_1_1_0;
  if (jsonVersion.compare(WMS130)==0) {
    wmsVersion = WMS_1_3_0;
  }
  
  WMSLayer* wmsLayer = new WMSLayer(URL::escape(layersSecuence),
                                    URL(jsonURL, false),
                                    wmsVersion,
                                    sector,
                                    format,
                                    "EPSG:4326",
                                    "",
                                    transparent,
                                    levelTileCondition, TimeInterval::fromDays(30), true, new LayerTilesRenderParameters(Sector::fullSphere(),jsonSplitsLat,jsonSplitsLon,0,19,LayerTilesRenderParameters::defaultTileTextureResolution(),LayerTilesRenderParameters::defaultTileMeshResolution(),false));
  layerSet->addLayer(wmsLayer);
}

void SceneParser::parserJSONTMSLayer(LayerSet* layerSet, const JSONObject* jsonLayer){
  cout << "Parsing TMS Layer " << jsonLayer->getAsString(NAME)->value() << "..." << endl;
  cout << "Parsing TMS Layer " << jsonLayer->description() << "..." << endl;
  const std::string jsonURL = jsonLayer->getAsString(DATASOURCE)->value();
  
  const int jsonSplitsLat = atoi(jsonLayer->getAsString(SPLITSLATITUDE)->value().c_str());
  const int jsonSplitsLon = atoi(jsonLayer->getAsString(SPLITSLONGITUDE)->value().c_str());
  
  bool transparent = getValueBooleanParam(jsonLayer->getAsString(ISTRANSPARENT));
  
  bool enabled = getValueBooleanParam(jsonLayer->getAsString(ISENABLED));
  
  std::string format = getFormat(transparent);
  
  LevelTileCondition* levelTileCondition = getLevelCondition(jsonLayer->getAsString(MINLEVEL),jsonLayer->getAsString(MAXLEVEL));
  
  Sector sector = getSector(jsonLayer->getAsObject(BBOX));
  
  const JSONArray* jsonItems = jsonLayer->getAsArray(ITEMS);
  IStringBuilder *layersName = IStringBuilder::newStringBuilder();
  
  for (int i = 0; i<jsonItems->size(); i++) {
    const JSONObject* jsOBJ = jsonItems->getAsObject(i);
    if (jsOBJ != NULL) {
      if (jsOBJ->getAsNumber(STATUS, 0) == 1 || jsOBJ->getAsBoolean(STATUS, false)) {
        layersName->addString(jsOBJ->getAsString(NAME)->value());
        layersName->addString(",");
      }
    }
  }
  std::string layersSecuence = layersName->getString();
  if (layersName->getString().length() > 0) {
    layersSecuence = IStringUtils::instance()->substring(layersSecuence, 0, layersSecuence.length()-1);
  }
  
  delete layersName;
  
  TMSLayer* tmsLayer = new TMSLayer(URL::escape(layersSecuence),
                                    URL(jsonURL, false),
                                    sector,
                                    format,
                                    transparent,
                                    levelTileCondition,
                                    TimeInterval::fromDays(30),
                                    true,
                                    new LayerTilesRenderParameters(Sector::fullSphere(),jsonSplitsLat,jsonSplitsLon,0,19,LayerTilesRenderParameters::defaultTileTextureResolution(),LayerTilesRenderParameters::defaultTileMeshResolution(),false));
  if (!enabled) {
    tmsLayer->setEnable(enabled);
  }
  
  tmsLayer->setTitle(jsonLayer->getAsString(NAME)->value());
  
  layerSet->addLayer(tmsLayer);
}

LevelTileCondition* SceneParser::getLevelCondition(const JSONString* jsonMinLevel, const JSONString* jsonMaxLevel){
  LevelTileCondition* levelTileCondition = NULL;
  if (jsonMinLevel != NULL && jsonMaxLevel != NULL) {
    int minLevel = atoi(jsonMinLevel->value().c_str());
    int maxLevel = atoi(jsonMaxLevel->value().c_str());
    if (minLevel <= 0) {
      minLevel = 0;
    }
    if (maxLevel >= 19){
      maxLevel = 19;
    }
    if (minLevel < maxLevel){
      levelTileCondition = new LevelTileCondition(minLevel, maxLevel);
    }
  }
  return levelTileCondition;
}

bool SceneParser::getValueBooleanParam(const JSONString* param){
  bool booleanParam = true;
  if(param!=NULL){
    if(param->value() == "false"){
      booleanParam = false;
    }
  }
  return booleanParam;
}

Sector SceneParser::getSector(const JSONObject* jsonBBOX){
  if (jsonBBOX != NULL){
    double minx = jsonBBOX->getAsNumber(MINX)->value();
    double miny = jsonBBOX->getAsNumber(MINY)->value();
    double maxx = jsonBBOX->getAsNumber(MAXX)->value();
    double maxy = jsonBBOX->getAsNumber(MAXY)->value();
    if (minx < -180) {
      minx = -180;
    }
    if (miny < -90){
      miny = -90;
    }
    if (maxx > 180){
      maxx = 180;
    }
    if (maxy > 90) {
      maxy = 90;
    }
    if (minx < maxx && miny < maxy) {
      return Sector(Geodetic2D(Angle::fromDegrees(miny), Angle::fromDegrees(minx)),
                    Geodetic2D(Angle::fromDegrees(maxy), Angle::fromDegrees(maxx)));
      
    }
  }
  return Sector::fullSphere();
}

std::string SceneParser::getFormat(bool transparent){
  std::string format = "image/png";
  if (!transparent) {
    format = "image/jpeg";
  }
  return format;
}


void SceneParser::parserJSON3DLayer(LayerSet* layerSet, const JSONObject* jsonLayer){
  cout << "Parsing 3D Layer " << jsonLayer->getAsString(NAME)->value() << "..." << endl;
}

void SceneParser::parserJSONPlanarImageLayer(LayerSet* layerSet, const JSONObject* jsonLayer){
  cout << "Parsing Pano Layer " << jsonLayer->getAsString(NAME)->value() << "..." << endl;
  
  const std::string geojsonDatasource = jsonLayer->getAsString(DATASOURCE)->value();
  
  const JSONArray* jsonItems = jsonLayer->getAsArray(ITEMS);
  for (int i = 0; i<jsonItems->size(); i++) {
    
    const std::string namefile = jsonItems->getAsObject(i)->getAsString(NAME)->value();
    
    IStringBuilder *url = IStringBuilder::newStringBuilder();
    url->addString(geojsonDatasource);
    url->addString("/");
    url->addString(namefile);
    
    _panoSources.push_back(url->getString());
    
    delete url;
  }
}

void SceneParser::parserGEOJSONLayer(LayerSet* layerSet, const JSONObject* jsonLayer){
  cout << "Parsing GEOJSON Layer " << jsonLayer->getAsString(NAME)->value() << "..." << endl;
  
  const std::string geojsonDatasource = jsonLayer->getAsString(DATASOURCE)->value();
  
  std::vector<std::map<std::string, std::string>* > legendLayer;
  
  const JSONArray* jsonItems = jsonLayer->getAsArray(ITEMS);
  for (int i = 0; i<jsonItems->size(); i++) {
    
    const std::string namefile = jsonItems->getAsObject(i)->getAsString(NAME)->value();
    const std::string urlIcon = jsonItems->getAsObject(i)->getAsString(URLICON)->value();
    const std::string minDistance = jsonItems->getAsObject(i)->getAsString(MINDISTANCE)->value();
    const std::string colorLine = jsonItems->getAsObject(i)->getAsString(COLORLINE)->value();
    const std::string sizeLine = jsonItems->getAsObject(i)->getAsString(SIZELINE)->value();
    const std::string showLabel = jsonItems->getAsObject(i)->getAsString(SHOWLABEL)->value();
    const std::string urlWeb = jsonItems->getAsObject(i)->getAsString(URLWEB)->value();
    
    IStringBuilder *url = IStringBuilder::newStringBuilder();
    url->addString(geojsonDatasource);
    
    const IStringUtils* iISU = IStringUtils::instance();
    
    ////// Check if we have an url to web service or an url to a geojson file
    
    if (iISU->endsWith(geojsonDatasource,"=") || iISU->endsWith(geojsonDatasource,"?") || iISU->endsWith(geojsonDatasource,"&")) {
      url->addString(namefile);
    }
    else {
      url->addString("/");
      url->addString(namefile);
    }
    
    std::string basicname;
    if (iISU->indexOf(namefile,".") != -1) {
      basicname = iISU->substring(namefile, 0, iISU->indexOf(namefile, "."));
    }
    else {
      basicname = namefile;
    }
    
    /////
    
    const std::string namefileTruncated = iISU->capitalize(iISU->replaceSubstring(basicname, "_", " "));
    
    std::string nameFileFormatted;
    int pos = IStringUtils::instance()->indexOf(namefileTruncated, "-");
    if (pos != -1){
      nameFileFormatted = iISU->substring(namefileTruncated, 0, pos) + " - " + iISU->substring(namefileTruncated, pos+1, namefileTruncated.length());
    } else {
      nameFileFormatted = namefileTruncated;
    }
    
    // Check if scene file has nameleyend on each geojson file
    // else it takes from nameFileFormatted, without accent and probably with grammar mistakes due to it is taken from a file served by an URL
    // when SceneGenerater will be updated this ckeck can be removed
    const JSONString* legendNameJSON = jsonItems->getAsObject(i)->getAsString(LEGENDNAME);
    std::string legendName;
    if (legendNameJSON != NULL) {
      legendName = legendNameJSON->value();
    } else {
      legendName = nameFileFormatted;
    }
    
    std::map<std::string, std::string>* geojsonMetadata = new std::map<std::string, std::string>;
    
#ifdef C_CODE
    geojsonMetadata->insert(std::make_pair(URLICON,urlIcon));
    geojsonMetadata->insert(std::make_pair(NAME,nameFileFormatted));
    geojsonMetadata->insert(std::make_pair(LEGENDNAME,legendName));
    geojsonMetadata->insert(std::make_pair(COLORLINE,colorLine));
    geojsonMetadata->insert(std::make_pair(WEB,urlWeb));
    geojsonMetadata->insert(std::make_pair(MINDISTANCE,minDistance));
    geojsonMetadata->insert(std::make_pair(SIZELINE,sizeLine));
    geojsonMetadata->insert(std::make_pair(SHOWLABEL,showLabel));
#endif
#ifdef JAVA_CODE
    geojsonMetadata.put(URLICON,urlIcon);
    geojsonMetadata.put(NAME,namefileTruncated);
    geojsonMetadata.put(LEGENDNAME,legendName);
    geojsonMetadata.put(COLORLINE,colorLine);
    geojsonMetadata.put(WEB,urlWeb);
    geojsonMetadata.put(MINDISTANCE,minDistance);
    geojsonMetadata.put(SIZELINE,sizeLine);
    geojsonMetadata.put(SHOWLABEL,showLabel);
#endif
    
    legendLayer.push_back(geojsonMetadata);
    
    _mapGeoJSONSources[url->getString()] = geojsonMetadata;
    
    delete url;
  }
  
  _legend[jsonLayer->getAsString(NAME)->value()] = legendLayer;
}

void SceneParser::parserJSONSphericalImageLayer(LayerSet* layerSet, const JSONObject* jsonLayer){
  cout << "Parsing GEOJSON Layer not available" << endl;
}

std::map<std::string, std::map<std::string, std::string>* > SceneParser::getMapGeoJSONSources(){
  return _mapGeoJSONSources;
}

std::vector<std::string> SceneParser::getPanoSources(){
  return _panoSources;
}

std::map<std::string, std::vector <std::map<std::string, std::string>* > > SceneParser::getLegend(){
  return _legend;
}

void SceneParser::updateMapGeoJSONSourcesValue(std::string fileUrl, std::string key, std::string value){
#ifdef C_CODE
  _mapGeoJSONSources[fileUrl]->at(key) = value;
#endif
#ifdef JAVA_CODE
  _mapGeoJSONSources.get(fileUrl).put(key, value);
#endif
}
