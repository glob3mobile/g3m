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

#include "OrderedRenderable.hpp"

RenderContext::~RenderContext() {
  //  delete _frameStartTimer;
  IFactory::instance()->deleteTimer(_frameStartTimer);
  delete _orderedRenderables;
}

void RenderContext::addOrderedRenderable(OrderedRenderable* orderedRenderable) {
  if (_orderedRenderables == NULL) {
    _orderedRenderables = new std::vector<OrderedRenderable*>;
  }
  _orderedRenderables->push_back(orderedRenderable);
}

#ifdef C_CODE
bool MyDataSortPredicate(const OrderedRenderable* or1,
                         const OrderedRenderable* or2) {
  return ( or1->distanceFromEye() >= or2->distanceFromEye() );
}
#endif

std::vector<OrderedRenderable*>* RenderContext::getSortedOrderedRenderables() const {
  if (_orderedRenderables != NULL) {
#ifdef C_CODE
    std::sort(_orderedRenderables->begin(),
              _orderedRenderables->end(),
              MyDataSortPredicate);
#endif
#ifdef JAVA_CODE
    TODO_sort_orderedRenderables;
#endif
  }

  return _orderedRenderables;
}
