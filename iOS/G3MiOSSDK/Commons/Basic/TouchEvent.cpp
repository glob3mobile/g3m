//
//  TouchEvent.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TouchEvent.hpp"

unsigned char TouchEvent::getTapCount() const
{
  if (_touchs.empty()) return 0;
  return _touchs[0]->getTapCount();
}
