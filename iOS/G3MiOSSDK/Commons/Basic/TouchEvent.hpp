//
//  TouchEvent.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TouchEvent
#define G3MiOSSDK_TouchEvent

#include <vector>

#include "Vector2I.hpp"


class Touch {
private:
#ifdef C_CODE
  const Vector2I _pos;
  const Vector2I _prevPos;
#endif
#ifdef JAVA_CODE
  private final Vector2I _pos;
  private final Vector2I _prevPos;
#endif
  const unsigned char _tapCount;

public:
  Touch(const Touch& other) :
  _pos(other._pos),
  _prevPos(other._prevPos),
  _tapCount(other._tapCount)
  {}

  Touch(const Vector2I& pos,
        const Vector2I& prev,
        const unsigned char tapCount=(unsigned char)0):
  _pos(pos),
  _prevPos(prev),
  _tapCount(tapCount)
  { }

  const Vector2I getPos() const { return _pos; }
  const Vector2I getPrevPos() const { return _prevPos; }
  const unsigned char getTapCount() const { return _tapCount; }

  ~Touch() {
  }
};


enum TouchEventType {
  Down,
  Up,
  Move,
  LongPress,
  DownUp
};


class TouchEvent {
private:
  const TouchEventType             _eventType;
  const std::vector<const Touch*>  _touchs;
  const bool                       _shiftPressed;
  const bool                       _ctrlPressed;
  const double                     _wheelDelta;


  TouchEvent(const TouchEventType& type,
             const std::vector<const Touch*> touchs,
             bool shift,
             bool ctrl,
             double wheelDelta) :
  _eventType(type),
  _touchs(touchs),
  _shiftPressed(shift),
  _ctrlPressed(ctrl),
  _wheelDelta(wheelDelta)
  {
  }

public:
  TouchEvent(const TouchEvent& other):
  _eventType(other._eventType),
  _touchs(other._touchs),
  _shiftPressed(other._shiftPressed),
  _ctrlPressed(other._ctrlPressed),
  _wheelDelta(other._wheelDelta) {
  }

  static TouchEvent* create(const TouchEventType& type,
                            const std::vector<const Touch*> touchs) {
    return new TouchEvent(type, touchs, false, false, 0.0);
  }

  static TouchEvent* create(const TouchEventType& type,
                            const std::vector<const Touch*> touchs,
                            bool shift,
                            bool ctrl,
                            double wheelDelta) {
    return new TouchEvent(type, touchs, shift, ctrl, wheelDelta);
  }

  static TouchEvent* create(const TouchEventType& type,
                            const Touch* touch) {
    const std::vector<const Touch*> touchs(1, touch);

    return create(type, touchs);
  }

  static TouchEvent* create(const TouchEventType& type,
                            const Touch* touch,
                            bool shift,
                            bool ctrl,
                            double wheelDelta) {
    const std::vector<const Touch*> touchs(1, touch);

    return create(type, touchs, shift, ctrl, wheelDelta);
  }

  TouchEventType getType() const {
    return _eventType;
  }

  const Touch* getTouch(int i) const {
    return _touchs[i];
  }

  int getTouchCount() const {
    return _touchs.size();
  }

  unsigned char getTapCount() const;

  ~TouchEvent() {
    for (unsigned int i = 0; i < _touchs.size(); i++) {
      delete _touchs[i];
    }
  }

};

#endif
