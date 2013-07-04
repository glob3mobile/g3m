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

  bool isReadyToRender(const G3MRenderContext* rc);

  void rawRender(const G3MRenderContext* rc,
                 const GLState& parentState,
                 bool renderNotReadyShapes);

};

#endif
