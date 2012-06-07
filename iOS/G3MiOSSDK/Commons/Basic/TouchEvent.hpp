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


class Touch {
private:
  const Vector2D _pos;
  const Vector2D _prevPos;

public:
  Touch(const Touch& other) : _pos(other._pos), _prevPos(other._prevPos) {
    
  }
  
  Touch(const Vector2D& pos, const Vector2D& prev) : _pos(pos), _prevPos(prev) { }
  
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
  const TouchEventType              _eventType;
  const std::vector<const Touch*> _touchs;
  
  TouchEvent(const TouchEventType& type,
             const std::vector<const Touch*> touchs): _eventType(type), _touchs(touchs) { 
  }
  
public:
  TouchEvent(const TouchEvent& other): _eventType(other._eventType), _touchs(other._touchs) {
    
  }
  
  static TouchEvent create(const TouchEventType& type,
                           const std::vector<const Touch*> Touchs) {
    return TouchEvent(type, Touchs);
  }
  
  static TouchEvent create(const TouchEventType& type,
                           const Touch* touch) {
    const Touch * pa[] = { touch };
    const std::vector<const Touch*> touchs = std::vector<const Touch*>(pa, pa+1);
    return create(type, touchs);
  }
  
  TouchEventType getType() const {
    return _eventType;
  }
  
  const Touch* getTouch(int i) const { return _touchs[i];}
  
  int getNumTouch() const { return _touchs.size(); }
  
  ~TouchEvent() {
    for (int i = 0; i < _touchs.size(); i++) {
      delete _touchs[i];
    }
  }
  
};

#endif
