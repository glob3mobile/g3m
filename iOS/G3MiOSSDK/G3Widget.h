//
//  G3Widget.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_G3Widget_h
#define G3MiOSSDK_G3Widget_h

#include "Renderer.h"

class Globe{};

class G3Widget
{
public:
    void create(Globe *g, Renderer *r);
    
    void render();
    
private:
    Renderer *_renderer;
    Globe *g;
};

#endif
