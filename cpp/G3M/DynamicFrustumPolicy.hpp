//
//  DynamicFrustumPolicy.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/17.
//
//

#ifndef DynamicFrustumPolicy_hpp
#define DynamicFrustumPolicy_hpp

#include "FrustumPolicy.hpp"


class DynamicFrustumPolicy : public FrustumPolicy {

public:
  ~DynamicFrustumPolicy();

  const Vector2D calculateFrustumZNearAndZFar(const Camera& camera) const;

  const FrustumPolicy* copy() const;

};

#endif
