//
//  TranslateScaleGizmo.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

#include "TranslateScaleGizmo.hpp"
#include "Color.hpp"


class TranslateScaleGizmoArrowListener : public ArrowListener {
  TranslateScaleGizmo* _gizmo;
public:
  
  TranslateScaleGizmoArrowListener(TranslateScaleGizmo* gizmo):
  _gizmo(gizmo) {}
  
  void onBaseChanged(const Arrow& arrow) {
    _gizmo->onBaseChanged(arrow);
  }
  
  void onDraggingEnded(const Arrow& arrow) {
    _gizmo->onDraggingEnded(arrow);
  }
};

TranslateScaleGizmo::~TranslateScaleGizmo() {
  delete _arrowListener;
  delete _cs;
}


TranslateScaleGizmo::TranslateScaleGizmo(const CoordinateSystem& cs, double scale, double size, double maxScale):
_cs(new CoordinateSystem(cs)), _size(size), _scale(scale), _maxScale(maxScale),
_listener(NULL) {
  
#define LINE_WIDTH_RATIO 0.01
#define HEAD_LENGTH_RATIO 0.05
#define HEAD_WIDTH_RATIO 1.5
#define SCALE_ARROW_LENGTH_SIZE_RATIO 0.15
  
  double lineWidth = size * LINE_WIDTH_RATIO;
  Vector3D x = _cs->_x.normalized().times(_size);
  Vector3D y = _cs->_y.normalized().times(_size);
  Vector3D z = _cs->_z.normalized().times(_size);
  
  _arrowListener = new TranslateScaleGizmoArrowListener(this);
  
  _xArrow = new Arrow(_cs->_origin,
                      x,
                      lineWidth,
                      Color::RED,
                      size * HEAD_LENGTH_RATIO,
                      HEAD_WIDTH_RATIO);
  _xArrow->setArrowListener(_arrowListener);
  addRenderer(_xArrow);
  
  _yArrow = new Arrow(_cs->_origin,
                      y,
                      lineWidth,
                      Color::GREEN,
                      size * HEAD_LENGTH_RATIO,
                      HEAD_WIDTH_RATIO);
  _yArrow->setArrowListener(_arrowListener);
  addRenderer(_yArrow);
  
  _zArrow = new Arrow(_cs->_origin,
                      z,
                      lineWidth,
                      Color::BLUE,
                      size * HEAD_LENGTH_RATIO,
                      HEAD_WIDTH_RATIO);
  _zArrow->setArrowListener(_arrowListener);
  addRenderer(_zArrow);
  
  Vector3D scaleVector = getScale1Vector().times(scale); //Center of arrow
  _scaleArrow = new Arrow(_cs->_origin.add(scaleVector),
                          scaleVector.times(SCALE_ARROW_LENGTH_SIZE_RATIO),
                          lineWidth,
                          Color::fromRGBA255(255, 0, 255, 255),
                          size * HEAD_LENGTH_RATIO,
                          HEAD_WIDTH_RATIO,
                          true);
  _scaleArrow->setArrowListener(_arrowListener);
  addRenderer(_scaleArrow);
}

void TranslateScaleGizmo::onBaseChanged(const Arrow& arrow) {
  if (_scaleArrow == &arrow) {
    
    Vector3D scaleVector = getScale1Vector();
    Vector3D scaleV = arrow.getBase().sub(_cs->_origin).div(scaleVector);
    _scale = scaleV.maxAxis();
    
    if (_scale < 0) {
      _scaleArrow->setBase(_cs->_origin, false);
      _scale = 0;
    }else if (_scale > _maxScale) {
      _scaleArrow->setBase(_cs->_origin.add(scaleVector.times(_maxScale)), false);
      _scale = _maxScale;
    }
  }
  else {
    //Translating
    Vector3D base = arrow.getBase();
    Vector3D disp = base.sub(_cs->_origin);
    
    CoordinateSystem* cs = new CoordinateSystem( _cs->changeOrigin(base) );
    delete _cs;
    _cs = cs;
    
//    Vector3D xBase = _xArrow->getBase();
    
    Arrow* arrows[3] = {_xArrow, _yArrow, _zArrow};
    for (size_t i = 0; i < 3; i++) {
      arrows[i]->setBase(_cs->_origin, false);
    }
    
//    Vector3D xBase2 = _xArrow->getBase();
//    Vector3D xDisp = xBase2.sub(xBase);
    
    _scaleArrow->setBase(_scaleArrow->getBase().add(disp), false);
  }
  
  if (_listener) {
    _listener->onChanged(*this);
  }
}

void TranslateScaleGizmo::onDraggingEnded(const Arrow& arrow) {
  if (_listener) {
    _listener->onChangeEnded(*this);
  }
}
