//
//  CompositeRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CompositeRenderer_h
#define G3MiOSSDK_CompositeRenderer_h

#include "Renderer.h"

#include <vector>

using namespace std;

class CompositeRenderer: public Renderer
{
private:
    vector<Renderer> _renderers;
    
public:
    void initialize(InitializationContext& ic);  
    
    int render(RenderContext &rc);
    
    bool onTapEvent(TapEvent& event);
    bool onTouchEvent(TouchEvent &event);
    
private:
    
};

#endif
