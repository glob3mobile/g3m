//
//  TouchEvent.cpp
//  G3M
//
//  Created by José Miguel S N on 31/05/12.
//

#include "TouchEvent.hpp"

#include "IStringBuilder.hpp"


unsigned char TouchEvent::getTapCount() const
{
  //  if (_touchs.empty()) return 0;
  //  return _touchs[0]->getTapCount();
  return _touchs.empty() ? 0 : _touchs[0]->getTapCount();
}


const std::string TouchEvent::description() const
{
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(TouchEvent type=");

  {
    std::string eventTypeName = "";
    switch (_eventType) {
      case Down:
        eventTypeName = "Down";
        break;

      case Up:
        eventTypeName = "Up";
        break;

      case Move:
        eventTypeName = "Move";
        break;

      case LongPress:
        eventTypeName = "LongPress";
        break;

      case DownUp:
        eventTypeName = "DownUp";
        break;

      default:
        eventTypeName = "<<unkown>>";
        break;
    }

    isb->addString(eventTypeName);
  }

  isb->addString(", touches=(");
  for (size_t i = 0; i < _touchs.size(); i++) {
    if (i > 0) {
      isb->addString(", ");
    }
    const Touch* touch = _touchs[i];
    isb->addString(touch->description());
  }
  isb->addString(")");

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

  isb->addString(", wheelDelta=");
  isb->addDouble(_wheelDelta);

  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
