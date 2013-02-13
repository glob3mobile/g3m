//
//  IG3MJSONBuilder.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 29/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IG3MJSONBuilder_hpp
#define G3MiOSSDK_IG3MJSONBuilder_hpp

#include <vector>

#include "LayerSet.hpp"
#include "GInitializationTask.hpp"
#include "MarksRenderer.hpp"

class IG3MJSONBuilder{ 

protected:
    std::string _jsonSource;
    
public:
    
    IG3MJSONBuilder(std::string jsonSource):_jsonSource(jsonSource){};
//    virtual void create(LayerSet* layerSet, GInitializationTask* initializationTask, MarkTouchListener* markTouchListener, MarkTouchListener* panoTouchListener)=0;
  virtual ~IG3MJSONBuilder(){};
  
};

#endif
