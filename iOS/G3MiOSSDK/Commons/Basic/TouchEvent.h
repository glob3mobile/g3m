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


class Pointer {
private:
  const Vector2D _pos;
  const Vector2D _prevPos;
  
  Pointer& operator=(const Pointer& v);
  
public:
  Pointer(const Pointer& other) : _pos(other._pos), _prevPos(other._prevPos) {
    
  }
  
  Pointer(const Vector2D& pos, const Vector2D& prev) : _pos(pos), _prevPos(prev) { }
  
  const Vector2D getPos() const { return _pos; }
  const Vector2D getPrevPos() const { return _prevPos; }
};


enum TouchEventType {
  Down,
  Up,
  Move,
  LongPress
};


class TouchEvent {
private:
  const TouchEventType       _eventType;
  const std::vector<Pointer> _pointers;
  
  TouchEvent(const TouchEventType& type,
             const std::vector<Pointer> pointers): _eventType(type), _pointers(pointers) { }
  
public:
  TouchEvent(const TouchEvent& other): _eventType(other._eventType), _pointers(other._pointers) {
    
  }
  
  static TouchEvent create(const TouchEventType& type,
                           const std::vector<Pointer> pointers) {
    return TouchEvent(type, pointers);
  }
  
  static TouchEvent create(const TouchEventType& type,
                           const std::vector<const Pointer*> pointers) {

    int __TODODODODO;
    
//    const std::vector<Pointer> poi = std::vector<Pointer>(pointers.size());
//
//    for(int i = 0; i < pointers.size(); i++) {
//      poi.push
//    }
    
    //return TouchEvent(type, pointers);
  }
  
  static TouchEvent create(const TouchEventType& type,
                           const Pointer& pointer) {
    const Pointer pa[] = { pointer };
    const std::vector<Pointer> pointers = std::vector<Pointer>(pa, pa+1);
    return TouchEvent::create(type, pointers);
  }
  
  TouchEventType getType() {
    return _eventType;
  }  
};

#endif
