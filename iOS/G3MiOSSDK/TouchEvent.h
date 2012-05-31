//
//  TouchEvent.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TouchEvent_h
#define G3MiOSSDK_TouchEvent_h

#include <vector>

#include "Vector2D.hpp"

using namespace std;

class Pointer{
    
    Pointer(Vector2D pos, Vector2D prev);
    Vector2D getPos() const { return _pos;}
    Vector2D getPrevPos() const { return _prevPos;}
    
private:
    Vector2D _pos;
    Vector2D _prevPos;
};

enum TouchEventType { Down, Up, Move, LongPress};
class TouchEvent : vector<Pointer> {
    
    TouchEvent(TouchEventType type);
    
    TouchEventType getType(){ return _eventType;}
    
public:
    TouchEventType _eventType;
};

class TapEvent : Vector2D{};



#endif
