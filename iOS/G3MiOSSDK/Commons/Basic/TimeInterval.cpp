//
//  TimeInterval.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "TimeInterval.hpp"

#include "IMathUtils.hpp"

TimeInterval TimeInterval::forever() {
  return TimeInterval(IMathUtils::instance()->maxInt64());
}
