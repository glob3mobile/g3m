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

class Shape {
protected:
  Geodetic3D*  _position;
  Angle*       _heading;
  Angle*       _pitch;

public:
  Shape(const Geodetic3D& position,
        const Angle& heading,
        const Angle& pitch) :
  _position( new Geodetic3D(position) ),
  _heading( new Angle(heading) ),
  _pitch( new Angle(pitch) ) {

  }

  virtual ~Shape() {
    delete _position;
    delete _heading;
    delete _pitch;
  }

  Geodetic3D getPosition() const {
    return *_position;
  }

  Angle getHeading() const {
    return *_heading;
  }

  Angle getPitch() const {
    return *_pitch;
  }

  void setPosition(const Geodetic3D& position) {
    delete _position;
    _position = new Geodetic3D(position);
  }

  void render(const RenderContext* rc);

  virtual bool isReadyToRender(const RenderContext* rc) = 0;
  
  virtual void rawRender(const RenderContext* rc) = 0;

};

#endif
