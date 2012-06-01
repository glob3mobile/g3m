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
public:
    Pointer(const Vector2D pos, const Vector2D prev);
    Vector2D getPos() const { return _pos;}
    Vector2D getPrevPos() const { return _prevPos;}
    
private:
    const Vector2D _pos;
    const Vector2D _prevPos;
};

enum TouchEventType { Down, Up, Move, LongPress};
class TouchEvent {
public:
    TouchEvent(TouchEventType type);
    
    TouchEventType getType(){ return _eventType;}
    
    static TouchEvent create(const TouchEventType& type, const std::vector<Pointer> pointers) {
        return TouchEvent(DOwn);
    }
    
private:
    TouchEventType _eventType;
};

class TapEvent : Vector2D{
public:
    TapEvent(double x, double y): Vector2D(x,y){}
};



#endif
