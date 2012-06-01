//
//  TouchEvent.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TouchEvent.h"

Pointer::Pointer(const Vector2D pos, const Vector2D prev):
_pos(pos), _prevPos(prev){}

TouchEvent::TouchEvent(TouchEventType type): _eventType(type){}

