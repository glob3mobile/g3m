//
//  FrustumPolicy.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/17.
//
//

#ifndef FrustumPolicy_hpp
#define FrustumPolicy_hpp

class Vector2D;
class Camera;

class FrustumPolicy {
protected:
  FrustumPolicy();

public:
  virtual ~FrustumPolicy();

  virtual const Vector2D calculateFrustumZNearAndZFar(const Camera& camera) const = 0;

  virtual const FrustumPolicy* copy() const = 0;

};

#endif
