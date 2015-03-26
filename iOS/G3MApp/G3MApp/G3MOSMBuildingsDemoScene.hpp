//
//  G3MOSMBuildingsDemoScene.h
//  G3MApp
//
//  Created by Pratik Prakash on 3/26/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MOSMBuildingsDemoScene__
#define __G3MApp__G3MOSMBuildingsDemoScene__

#include <stdio.h>
#include "G3MDemoScene.hpp"

class Mark;

class G3MOSMBuildingsDemoScene : public G3MDemoScene {
private:
    long long _requestId;
    
protected:
    void rawActivate(const G3MContext* context);
    
    void rawSelectOption(const std::string& option,
                         int optionIndex);
    
public:
    
    G3MOSMBuildingsDemoScene(G3MDemoModel* model) :
    G3MDemoScene(model, "OSM Buildings", "", 0),
    _requestId(-1)
    {
    }
    
    void deactivate(const G3MContext* context);
    
    
    void addMark(Mark* mark);
    
};

#endif /* defined(__G3MApp__G3MOSMBuildingsDemoScene__) */
