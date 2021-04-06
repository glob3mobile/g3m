//
//  ShapeFilter.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/30/21.
//

#ifndef ShapeFilter_hpp
#define ShapeFilter_hpp

class Shape;

class ShapeFilter {
public:
  virtual ~ShapeFilter() {
  }

  virtual bool test(const Shape* shape) const = 0;

};

#endif

