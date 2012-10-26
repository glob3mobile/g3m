//
//  AppParser.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 18/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_AppParser_hpp
#define G3MiOSSDK_AppParser_hpp

#include "MarksRenderer.hpp"
#include "JSONObject.hpp"
#include "LayerSet.hpp"

class AppParser {
    
    static const std::string WORLD;
    static const std::string BASELAYER;
    static const std::string BBOX;
    static const std::string CUSTOMDATA;
    
    static const std::string FEATURES;
    static const std::string GEOMETRY;
    static const std::string TYPE;
    static const std::string COORDINATES;
    static const std::string PROPERTIES;
    static const std::string NAME;
    
    static AppParser* _instance;  

private:

  void parseWorldConfiguration(LayerSet* layerSet, MarksRenderer* marks, JSONObject* jsonLayers); 
  void parseGEOJSONPointObject(MarksRenderer* marks, JSONObject* point);

public:
  
  static AppParser* instance();
  void parse(LayerSet* layerSet, MarksRenderer* marks, std::string namelessParameter);
    void parseCustomData(MarksRenderer* marks, JSONObject* jsonLayers);
  
protected:
  
  AppParser();
  AppParser(const AppParser &);
  AppParser &operator= (const AppParser &);
  
};

#endif
