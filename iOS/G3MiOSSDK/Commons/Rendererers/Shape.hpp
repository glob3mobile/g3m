//
//  Shape.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__Shape__
#define __G3MiOSSDK__Shape__

#include "Geodetic3D.hpp"
#include "Context.hpp"
#include "Vector3D.hpp"
class MutableMatrix44D;

class Shape {
private:
  Geodetic3D* _position;

  Angle*      _heading;
  Angle*      _pitch;

  double      _scaleX;
  double      _scaleY;
  double      _scaleZ;

  
  MutableMatrix44D* _transformMatrix;
  MutableMatrix44D* createTransformMatrix(const Planet* planet);
  MutableMatrix44D* getTransformMatrix(const Planet* planet);

protected:
  virtual void cleanTransformMatrix();

public:
  Shape(Geodetic3D* position) :
  _position( position ),
  _heading( new Angle(Angle::zero()) ),
  _pitch( new Angle(Angle::zero()) ),
  _scaleX(1),
  _scaleY(1),
  _scaleZ(1),
  _transformMatrix(NULL) {

  }

  virtual ~Shape();
  
  const Geodetic3D getPosition() const {
    return *_position;
  }

  const Angle getHeading() const {
    return *_heading;
  }

  const Angle getPitch() const {
    return *_pitch;
  }

  void setPosition(Geodetic3D* position) {
    delete _position;
    _position = position;
    cleanTransformMatrix();
  }

  void setHeading(const Angle& heading) {
    delete _heading;
    _heading = new Angle(heading);
    cleanTransformMatrix();
  }

  void setPitch(const Angle& pitch) {
    delete _pitch;
    _pitch = new Angle(pitch);
    cleanTransformMatrix();
  }

  void setScale(double scaleX,
                double scaleY,
                double scaleZ) {
    _scaleX = scaleX;
    _scaleY = scaleY;
    _scaleZ = scaleZ;

    cleanTransformMatrix();
  }

  void setScale(const Vector3D& scale) {
    setScale(scale._x,
             scale._y,
             scale._z);
  }

  void render(const RenderContext* rc);

  virtual void initialize(const InitializationContext* ic) {

  }

  virtual bool isReadyToRender(const RenderContext* rc) = 0;
  
  virtual void rawRender(const RenderContext* rc) = 0;

};

#endif
