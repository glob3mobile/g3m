//
//  SceneParser.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 15/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SceneParser_hpp
#define G3MiOSSDK_SceneParser_hpp


#include "JSONObject.hpp"
#include "ILogger.hpp"
#include "LayerSet.hpp"

#include "Renderer.hpp"
#include "IDownloader.hpp"
#include "MarksRenderer.hpp"
#include "LevelTileCondition.hpp"

enum layer_type {
  WMS,TMS,THREED,PLANARIMAGE,GEOJSON,SPHERICALIMAGE
};

class SceneParser{
  static const std::string LAYERS;
  static const std::string TYPE;
  static const std::string DATASOURCE;
  static const std::string VERSION;
  static const std::string BBOX;
  static const std::string MINX;
  static const std::string MINY;
  static const std::string MAXX;
  static const std::string MAXY;
  static const std::string SPLITSLONGITUDE;
  static const std::string SPLITSLATITUDE;
  static const std::string ISTRANSPARENT;
  static const std::string ISENABLED;
  static const std::string ITEMS;
  static const std::string MINLEVEL;
  static const std::string MAXLEVEL;
  static const std::string STATUS;
  static const std::string NAME;
  static const std::string LEGENDNAME;
  static const std::string URLICON;
  static const std::string MINDISTANCE;
  static const std::string COLORLINE;
  static const std::string SIZELINE;
  static const std::string URLWEB;
  static const std::string SHOWLABEL;
  static const std::string WEB;
  
  static const std::string WMS130;
  
  static SceneParser* _instance;
  std::map<std::string, layer_type> _mapLayerType;
  std::map<std::string, std::map<std::string, std::string>* > _mapGeoJSONSources;
  std::vector<std::string> _panoSources;
  std::map<std::string, std::vector <std::map<std::string, std::string>* > > _legend;
  
public:
  
  static SceneParser* instance();
  void parse(LayerSet* layerSet, std::string namelessParameter);
  std::map<std::string, std::map<std::string, std::string>* > getMapGeoJSONSources();
  std::vector<std::string> getPanoSources();
  std::map<std::string, std::vector <std::map<std::string, std::string>* > > getLegend();
  void updateMapGeoJSONSourcesValue(std::string fileUrl, std::string key, std::string value);
  
  
private:
  void parserJSONLayerList(LayerSet* layerSet, const JSONObject* jsonLayers);
  void parserJSONWMSLayer(LayerSet* layerSet, const JSONObject* jsonLayer);
  void parserJSONTMSLayer(LayerSet* layerSet, const JSONObject* jsonLayer);
  void parserJSON3DLayer(LayerSet* layerSet, const JSONObject* jsonLayer);
  void parserJSONPlanarImageLayer(LayerSet* layerSet, const JSONObject* jsonLayer);
  void parserJSONSphericalImageLayer(LayerSet* layerSet, const JSONObject* jsonLayer);
  void parserGEOJSONLayer(LayerSet* layerSet, const JSONObject* jsonLayer);
  
  LevelTileCondition* getLevelCondition(const JSONString* jsonMinLevel, const JSONString* jsonMaxLevel);
  Sector getSector(const JSONObject* jsonBBOX);
  bool getValueBooleanParam(const JSONString* jsonBBOX);
  std::string getFormat(bool transparent);
  
protected:
  SceneParser();
  SceneParser(const SceneParser &);
  SceneParser &operator= (const SceneParser &);
  
};

#endif

