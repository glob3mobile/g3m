//
//  AppParser.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 18/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_AppParser_hpp
#define G3MiOSSDK_AppParser_hpp

#include "MarksRenderer.hpp"
#include "JSONObject.hpp"
#include "LayerSet.hpp"

const std::string world = "_world";
const std::string baselayer = "_baselayer";
const std::string bbox = "_bbox";
const std::string customdata = "_customdata";

const std::string features = "features";
const std::string geometry = "geometry";
const std::string type = "type";
const std::string coordinates = "coordinates";
const std::string properties = "properties";
const std::string name = "name";

class AppParser {
  
  static AppParser* _instance;  

private:

  void parseWorldConfiguration(LayerSet* layerSet, MarksRenderer* marks, JSONObject* jsonLayers);
  void parseCustomData(MarksRenderer* marks, JSONObject* jsonLayers);
  void parseGEOJSONPointObject(MarksRenderer* marks, JSONObject* point);
    
public:
  
  static AppParser* instance();
  void parse(LayerSet* layerSet, MarksRenderer* marks, std::string namelessParameter);
  
protected:
  
  AppParser();
  AppParser(const AppParser &);
  AppParser &operator= (const AppParser &);
  
};

#endif
