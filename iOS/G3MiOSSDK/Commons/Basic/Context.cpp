//
//  Context.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Context.hpp"

#include "ITimer.hpp"
#include "IFactory.hpp"

#include "OrderedRenderable.hpp"

G3MRenderContext::~G3MRenderContext() {
  //  delete _frameStartTimer;
  IFactory::instance()->deleteTimer(_frameStartTimer);
  delete _orderedRenderables;

#ifdef JAVA_CODE
  super.dispose();
#endif

}

void G3MRenderContext::addOrderedRenderable(OrderedRenderable* orderedRenderable) const {
  if (_orderedRenderables == NULL) {
    _orderedRenderables = new std::vector<OrderedRenderable*>;
  }
  _orderedRenderables->push_back(orderedRenderable);
}

#ifdef C_CODE
bool MyDataSortPredicate(const OrderedRenderable* or1,
                         const OrderedRenderable* or2) {
  return ( or1->squaredDistanceFromEye() >= or2->squaredDistanceFromEye() );
}
#endif

std::vector<OrderedRenderable*>* G3MRenderContext::getSortedOrderedRenderables() const {
  if (_orderedRenderables != NULL) {
#ifdef C_CODE
    std::sort(_orderedRenderables->begin(),
              _orderedRenderables->end(),
              MyDataSortPredicate);
#endif
#ifdef JAVA_CODE
    java.util.Collections.sort(
                               _orderedRenderables,
                               new java.util.Comparator<OrderedRenderable>() {
                                 @Override
                                 public int compare(final OrderedRenderable or1,
                                                    final OrderedRenderable or2) {
                                   return Double.compare(or2.squaredDistanceFromEye(),
                                                         or1.squaredDistanceFromEye());
                                 }
                               });
#endif
  }

  return _orderedRenderables;
}
