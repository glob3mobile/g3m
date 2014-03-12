//
//  Vector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Vector2D.hpp"

#include "MutableVector2D.hpp"

#include "IStringBuilder.hpp"

MutableVector2D Vector2D::asMutableVector2D() const {
  return MutableVector2D(_x, _y);
}

const std::string Vector2D::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(V2D ");
  isb->addDouble(_x);
  isb->addString(", ");
  isb->addDouble(_y);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const double Vector2D::distanceTo(const Vector2D& that) const {
  return IMathUtils::instance()->sqrt( squaredDistanceTo(that) );
}

const double Vector2D::squaredDistanceTo(const Vector2D& that) const {
  const double dx = _x - that._x;
  const double dy = _y - that._y;
  return (dx * dx) + (dy * dy);
}

double Vector2D::distanceToSegment(const Vector2D& A, const Vector2D& B) const{

  //Check this out: http://luisrey.wordpress.com/2008/07/06/distancia-punto-1/

  if (A.isEquals(B)){
    return this->distanceTo(A);
  }

  const double ax = A._x;
  const double ay = A._y;
  const double bx = B._x;
  const double by = B._y;
  const double cx = this->_x;
  const double cy = this->_y;

  const double dx = bx - ax;
  const double dy = by - ay;

  double u = ((cx-ax)*dx) + ((cy-ay)*dy) / (dx*dx + dy*dy);

  if (u <= 0.0){
    return this->distanceTo(A);
  } else if (u >= 1.0){
    return this->distanceTo(B);
  } else{

    //Point proyection is inside the segment
    Vector2D proyection(ax + u * dx, ay + u * dy);
    return distanceTo(proyection);
  }

}
