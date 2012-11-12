//
//  CompositeShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/6/12.
//
//

#ifndef __G3MiOSSDK__CompositeShape__
#define __G3MiOSSDK__CompositeShape__

#include "Shape.hpp"

#include <vector>

class CompositeShape : public Shape {
private:
  std::vector<Shape*> _children;

public:
  CompositeShape() :
  Shape(NULL)
  {

  }

  CompositeShape(Geodetic3D* position) :
  Shape(position)
  {

  }


  ~CompositeShape();

  void addShape(Shape* shape);

  bool isReadyToRender(const RenderContext* rc);

  void rawRender(const RenderContext* rc);

};

#endif
