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

TranslateScaleGizmo* TranslateScaleGizmo::translateAndScale(const CoordinateSystem& cs,
                                                            double size,
                                                            double scale,
                                                            double maxScale,
                                                            double lineWidthRatio,
                                                            double headLengthRatio,
                                                            double headWidthRatio,
                                                            double scaleArrowLengthSizeRatio) {
  return new TranslateScaleGizmo(cs,
                                 size,
                                 scale,
                                 maxScale,
                                 lineWidthRatio,
                                 headLengthRatio,
                                 headWidthRatio,
                                 scaleArrowLengthSizeRatio,
                                 true /* scaleOption */);
}

TranslateScaleGizmo* TranslateScaleGizmo::translate(const CoordinateSystem& cs,
                                                    double size,
                                                    double lineWidthRatio,
                                                    double headLengthRatio,
                                                    double headWidthRatio) {
  return new TranslateScaleGizmo(cs,
                                 size,
                                 1, // scale
                                 1, // maxScale
                                 lineWidthRatio,
                                 headLengthRatio,
                                 headWidthRatio,
                                 1, // scaleArrowLengthSizeRatio
                                 false /* scaleOption */);
}


TranslateScaleGizmo::~TranslateScaleGizmo() {
  delete _listener;
  delete _arrowListener;
  delete _cs;
}

TranslateScaleGizmo::TranslateScaleGizmo(const CoordinateSystem& cs,
                                         double size,
                                         double scale,
                                         double maxScale,
                                         double lineWidthRatio,
                                         double headLengthRatio,
                                         double headWidthRatio,
                                         double scaleArrowLengthSizeRatio,
                                         bool scaleOption):
_cs(new CoordinateSystem(cs)),
_size(size),
_scale(scale),
_maxScale(maxScale),
_listener(NULL)
{
  const double lineWidth = size * lineWidthRatio;

  _arrowListener = new TranslateScaleGizmoArrowListener(this);

  {
    const Vector3D x = _cs->_x.normalized().times(_size);

    _xArrow = new Arrow(_cs->_origin,
                        x,
                        lineWidth,
                        Color::RED,
                        size * headLengthRatio,
                        headWidthRatio);
    _xArrow->setArrowListener(_arrowListener);
    addRenderer(_xArrow);
  }

  {
    const Vector3D y = _cs->_y.normalized().times(_size);

    _yArrow = new Arrow(_cs->_origin,
                        y,
                        lineWidth,
                        Color::GREEN,
                        size * headLengthRatio,
                        headWidthRatio);
    _yArrow->setArrowListener(_arrowListener);
    addRenderer(_yArrow);
  }

  {
    const Vector3D z = _cs->_z.normalized().times(_size);

    _zArrow = new Arrow(_cs->_origin,
                        z,
                        lineWidth,
                        Color::BLUE,
                        size * headLengthRatio,
                        headWidthRatio);
    _zArrow->setArrowListener(_arrowListener);
    addRenderer(_zArrow);
  }

  {
    if (scaleOption) {
      Vector3D scaleVector = getScale1Vector().times(scale); //Center of arrow
      _scaleArrow = new Arrow(_cs->_origin.add(scaleVector),
                              scaleVector.times(scaleArrowLengthSizeRatio),
                              lineWidth,
                              Color::fromRGBA255(255, 0, 255, 255),
                              size * headLengthRatio,
                              headWidthRatio,
                              true);
      _scaleArrow->setArrowListener(_arrowListener);
      addRenderer(_scaleArrow);
    }
    else {
      _scaleArrow = NULL;
    }
  }
}

void TranslateScaleGizmo::onBaseChanged(const Arrow& arrow) {
  if (_scaleArrow == &arrow) {
    const Vector3D scaleVector = getScale1Vector();
    const Vector3D scaleV = arrow.getBase().sub(_cs->_origin).div(scaleVector);
    _scale = scaleV.maxAxis();
    
    if (_scale < 0) {
      _scaleArrow->setBase(_cs->_origin, false);
      _scale = 0;
    }
    else if (_scale > _maxScale) {
      _scaleArrow->setBase(_cs->_origin.add(scaleVector.times(_maxScale)), false);
      _scale = _maxScale;
    }
  }
  else {
    // Translating
    const Vector3D base = arrow.getBase();
    const Vector3D disp = base.sub(_cs->_origin);
    
    CoordinateSystem* cs = new CoordinateSystem( _cs->changeOrigin(base) );
    delete _cs;
    _cs = cs;
    
    // Vector3D xBase = _xArrow->getBase();
    
    Arrow* arrows[3] = {_xArrow, _yArrow, _zArrow};
    for (size_t i = 0; i < 3; i++) {
      arrows[i]->setBase(_cs->_origin, false);
    }
    
    // Vector3D xBase2 = _xArrow->getBase();
    // Vector3D xDisp = xBase2.sub(xBase);

    if (_scaleArrow != NULL) {
      _scaleArrow->setBase(_scaleArrow->getBase().add(disp), false);
    }
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
