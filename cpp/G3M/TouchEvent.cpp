//
//  TouchEvent.cpp
//  G3M
//
//  Created by José Miguel S N on 31/05/12.
//

#include "TouchEvent.hpp"

#include "IStringBuilder.hpp"
//#include "ILogger.hpp"


TouchEvent::TouchEvent(const TouchEventType& type,
                       const std::vector<const Touch*>& touchs,
                       const double wheelDelta) :
_type(type),
_touchs(touchs),
_wheelDelta(wheelDelta)
{
  //  ILogger* logger = ILogger::instance();
  //  if (logger != NULL) {
  //    logger->logInfo(description());
  //  }
}


unsigned char TouchEvent::getTapCount() const
{
  return _touchs.empty() ? 0 : _touchs[0]->getTapCount();
}


const std::string TouchEvent::description() const
{
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(TouchEvent ");

  {
    std::string typeName = "";
    switch (_type) {
      case Down:
        typeName = "Down";
        break;

      case Up:
        typeName = "Up";
        break;

      case Move:
        typeName = "Move";
        break;

      case LongPress:
        typeName = "LongPress";
        break;

      case DownUp:
        typeName = "DownUp";
        break;

      case MouseWheel:
        typeName = "MouseWheel";
        break;

      default:
        typeName = "<<unkown>>";
        break;
    }

    isb->addString(typeName);
  }

  isb->addString(" touches=(");
  for (size_t i = 0; i < _touchs.size(); i++) {
    if (i > 0) {
      isb->addString(", ");
    }
    const Touch* touch = _touchs[i];
    isb->addString(touch->description());
  }
  isb->addString(")");

  if (_wheelDelta != 0) {
    isb->addString(", wheelDelta=");
    isb->addDouble(_wheelDelta);
  }

  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string Touch::description() const
{
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(Touch pos=");

#ifdef C_CODE
  isb->addString(_pos.description());
#else
  isb.addString((_pos == null) ? "null" : _pos.description());
#endif

  isb->addString(", tapCount=");
  isb->addInt(_tapCount);

  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}


const TouchEvent* TouchEvent::clone() const {
  std::vector<const Touch*> clonedTouchs;

  for (size_t i = 0; i < _touchs.size(); i++) {
    const Touch* touch = _touchs[i];
    clonedTouchs.push_back(touch->clone());
  }

  return new TouchEvent(_type,
                        clonedTouchs,
                        _wheelDelta);
}

bool Touch::isEquals(const Touch* that) const {
  return ((this->_tapCount == that->_tapCount) &&
          this->_pos.isEquals(that->_pos));
}

bool TouchEvent::isEquals(const TouchEvent* that) const {
  if (that == NULL) {
    return false;
  }

  if (this->getType() != that->getType()) {
    return false;
  }

  if (this->getTapCount() != that->getTapCount()) {
    return false;
  }

  if (this->getTouchCount() != that->getTouchCount()) {
    return false;
  }

  for (size_t i = 0; i < _touchs.size(); i++) {
    const Touch* thisTouch = this->_touchs[i];
    const Touch* thatTouch = that->_touchs[i];
    if (!thisTouch->isEquals(thatTouch)) {
      return false;
    }
  }

  return true;
}
