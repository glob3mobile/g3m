//
//  TouchEvent.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//

#include "TouchEvent.hpp"

unsigned char TouchEvent::getTapCount() const
{
  if (_touchs.empty()) return 0;
  return _touchs[0]->getTapCount();
}
