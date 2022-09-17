//
//  TouchEvent.hpp
//  G3M
//
//  Created by José Miguel S N on 31/05/12.
//

#ifndef G3M_TouchEvent
#define G3M_TouchEvent

#include <vector>

#include "Vector2F.hpp"


class Touch {
private:
  const Vector2F      _pos;
  const unsigned char _tapCount;
  const double        _wheelDelta;

  Touch(const Touch& other);

public:
  Touch(const Vector2F& pos,
        const unsigned char tapCount,
        const double wheelDelta=0.0):
  _pos(pos),
  _tapCount(tapCount),
  _wheelDelta(wheelDelta)
  {
#ifdef JAVA_CODE
    if (_pos == null) {
      throw new RuntimeException("_pos is null");
    }
#endif
  }

  const Touch* clone() const {
    return new Touch(_pos,
                     _tapCount,
                     _wheelDelta);
  }

  const Vector2F getPos() const { return _pos; }
  const unsigned char getTapCount() const { return _tapCount; }
  const double getMouseWheelDelta() const { return _wheelDelta; }

  ~Touch() {
  }

  const std::string description() const;

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

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
  const TouchEventType            _eventType;
  const std::vector<const Touch*> _touchs;


  TouchEvent(const TouchEventType& type,
             const std::vector<const Touch*>& touchs
             ) :
  _eventType(type),
  _touchs(touchs)
  {
  }

  TouchEvent(const TouchEvent& other);

public:

  static TouchEvent* create(const TouchEventType& type,
                            const std::vector<const Touch*>& touchs) {
    return new TouchEvent(type, touchs);
  }

  static TouchEvent* create(const TouchEventType& type,
                            const Touch* touch) {
    const std::vector<const Touch*> touchs(1, touch);

    return create(type, touchs);
  }

  TouchEventType getType() const {
    return _eventType;
  }

  const Touch* getTouch(int i) const {
    return _touchs[i];
  }

  size_t getTouchCount() const {
    return _touchs.size();
  }

  unsigned char getTapCount() const;

  ~TouchEvent() {
    for (size_t i = 0; i < _touchs.size(); i++) {
      delete _touchs[i];
    }
  }

  const std::string description() const;

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
