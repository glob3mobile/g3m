//
//  InitializationContext.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Context.hpp"

#include "ITimer.hpp"
#include "IFactory.hpp"

RenderContext::~RenderContext() {
//  delete _frameStartTimer;
  IFactory::instance()->deleteTimer(_frameStartTimer);
}
