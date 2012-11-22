//
//  SGRotateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGRotateNode__
#define __G3MiOSSDK__SGRotateNode__

#include "SGNode.hpp"

class SGRotateNode : public SGNode {
private:
  double _x;
  double _y;
  double _z;
  double _angle;

protected:
  void prepareRender(const G3MRenderContext* rc);

  void cleanUpRender(const G3MRenderContext* rc);

public:
  SGRotateNode() :
  _x(0),
  _y(0),
  _z(0),
  _angle(0)
  {

  }

  void setX(double x) {
    _x = x;
  }

  void setY(double y) {
    _y = y;
  }

  void setZ(double z) {
    _z = z;
  }

  void setAngle(double angle) {
    _angle = angle;
  }

};

#endif
