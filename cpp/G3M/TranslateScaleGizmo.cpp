//
//  TranslateScaleGizmo.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

#include "TranslateScaleGizmo.hpp"

TranslateScaleGizmo::TranslateScaleGizmo(const CoordinateSystem& cs, double scale, double size):
_cs(new CoordinateSystem(cs)), _size(size){
  
#define LINE_WIDTH_RATIO 0.02
#define HEAD_LENGTH_RATIO 0.05
#define HEAD_WIDTH_RATIO 1.5
#define SCALE_ARROW_LENGTH_SIZE_RATIO 0.2
  
  double lineWidth = size * LINE_WIDTH_RATIO;
  Vector3D x = _cs->_x.normalized().times(_size);
  Vector3D y = _cs->_y.normalized().times(_size);
  Vector3D z = _cs->_z.normalized().times(_size);
  
  _xArrow = new Arrow(_cs->_origin,
                      x,
                      lineWidth,
                      Color::RED,
                      size * HEAD_LENGTH_RATIO,
                      HEAD_WIDTH_RATIO);
  _xArrow->setArrowListener(this);
  addRenderer(_xArrow);
  
  _yArrow = new Arrow(_cs->_origin,
                      y,
                      lineWidth,
                      Color::GREEN,
                      size * HEAD_LENGTH_RATIO,
                      HEAD_WIDTH_RATIO);
  _yArrow->setArrowListener(this);
  addRenderer(_yArrow);
  
  _zArrow = new Arrow(_cs->_origin,
                      z,
                      lineWidth,
                      Color::BLUE,
                      size * HEAD_LENGTH_RATIO,
                      HEAD_WIDTH_RATIO);
  _zArrow->setArrowListener(this);
  addRenderer(_zArrow);
  
  Vector3D scaleVector = x.add(y).add(z).normalized().times(_size); //Center of arrow
  _scaleArrow = new Arrow(_cs->_origin.add(scaleVector.times(1.0 - SCALE_ARROW_LENGTH_SIZE_RATIO / 2.0)),
                          scaleVector.times(SCALE_ARROW_LENGTH_SIZE_RATIO),
                          lineWidth,
                          Color::fromRGBA255(255, 0, 255, 255),
                          size * HEAD_LENGTH_RATIO,
                          HEAD_WIDTH_RATIO,
                          true);
  _scaleArrow->setArrowListener(this);
  addRenderer(_scaleArrow);
}

void TranslateScaleGizmo::onBaseChanged(const Arrow& arrow) {
  
  if (_scaleArrow == & arrow){
    printf("Scaling\n");
  }else{
    //Translating
    Vector3D base = arrow.getBase();
    Vector3D disp = base.sub(_cs->_origin);
    
    CoordinateSystem* cs = new CoordinateSystem( _cs->changeOrigin(base) );
    delete _cs;
    _cs = cs;
    
    Vector3D xBase = _xArrow->getBase();
    
    Arrow* arrows[3] = {_xArrow, _yArrow, _zArrow};
    for (size_t i = 0; i < 3; i++){
      arrows[i]->setBase(_cs->_origin, false);
    }
    
    Vector3D xBase2 = _xArrow->getBase();
    Vector3D xDisp = xBase2.sub(xBase);
    
    _scaleArrow->setBase(_scaleArrow->getBase().add(disp), false);
    
    printf("Translating to %s\n", _cs->_origin.description().c_str());
  }
}
