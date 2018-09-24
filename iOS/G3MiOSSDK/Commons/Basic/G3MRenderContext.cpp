//
//  G3MRenderContext.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

#include "G3MRenderContext.hpp"

#include "ITimer.hpp"
#include "ILogger.hpp"
#include "OrderedRenderable.hpp"
#include <algorithm>


G3MRenderContext::~G3MRenderContext() {
  delete _frameStartTimer;
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
    if (or1 == NULL || or2 == NULL){
        ILogger::instance()->logError("Problem at MyDataSortPredicate");
        return false;
    }
  return ( or1->squaredDistanceFromEye() > or2->squaredDistanceFromEye() );
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

void G3MRenderContext::clearOrderedRenderables(){
//    delete _orderedRenderables;
//    _orderedRenderables = NULL;
}

void G3MRenderContext::clear() {
  _frameStartTimer->start();
    clearOrderedRenderables();
}
