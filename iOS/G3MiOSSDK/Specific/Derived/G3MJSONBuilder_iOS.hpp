//
//  G3MJSONBuilder.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 30/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_G3MJSONBuilder_iOS_hpp
#define G3MiOSSDK_G3MJSONBuilder_iOS_hpp

#include "IG3MJSONBuilder.hpp"

#include "G3MWidget_iOS.h"


class G3MJSONBuilder_iOS : public IG3MJSONBuilder {
    
    G3MWidget_iOS* _g3mWidget;
    
public:
    G3MJSONBuilder_iOS(std::string jsonSource, G3MWidget_iOS* g3mWidget);
    void create(LayerSet* layerSet, GInitializationTask* initializationTask, MarkTouchListener* markTouchListener, MarkTouchListener* panoTouchListener);
};

#endif
