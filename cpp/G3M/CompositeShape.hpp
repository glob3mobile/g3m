//
//  CompositeShape.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/6/12.
//
//

#ifndef __G3M__CompositeShape__
#define __G3M__CompositeShape__

#include "Shape.hpp"

#include <vector>


class CompositeShape : public Shape {
private:
  std::vector<Shape*> _children;

public:
  CompositeShape() :
  Shape(NULL, ABSOLUTE)
  {

  }

  CompositeShape(Geodetic3D* position, AltitudeMode altitudeMode) :
  Shape(position, altitudeMode)
  {

  }


  ~CompositeShape();

  void addShape(Shape* shape);

  bool isReadyToRender(const G3MRenderContext* rc);

  void rawRender(const G3MRenderContext* rc,
                 GLState* parentState,
                 bool renderNotReadyShapes);

  bool isTransparent(const G3MRenderContext* rc);

  std::vector<double> intersectionsDistances(const Planet* planet,
                                             const Vector3D& origin,
                                             const Vector3D& direction) const;

};

#endif
