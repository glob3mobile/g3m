//
//  TranslateScaleGizmo.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

#ifndef TranslateScaleGizmo_hpp
#define TranslateScaleGizmo_hpp

#include "CompositeRenderer.hpp"
#include "Arrow.hpp"
#include "CoordinateSystem.hpp"


class TranslateScaleGizmo;

class TranslateScaleGizmoListener{
public:
  virtual ~TranslateScaleGizmoListener(){}
  virtual void onChanged(const TranslateScaleGizmo& gizmo) = 0;
  virtual void onChangeEnded(const TranslateScaleGizmo& gizmo) = 0;
};

class TranslateScaleGizmoArrowListener;


class TranslateScaleGizmo: public CompositeRenderer {
private:
  CoordinateSystem* _cs;
  const double      _size;
  double            _scale;
  const double      _maxScale;

  TranslateScaleGizmoArrowListener* _arrowListener;
  
  TranslateScaleGizmoListener* _listener;
  
  Arrow  *_xArrow, *_yArrow, *_zArrow, *_scaleArrow;
  
  Vector3D getScale1Vector() const{
    return _cs->_x.add(_cs->_y).add(_cs->_z).normalized().times(_size);
  }

  TranslateScaleGizmo(const CoordinateSystem& cs,
                      double size,
                      double scale,
                      double maxScale,
                      double lineWidthRatio,
                      double headLengthRatio,
                      double headWidthRatio,
                      double scaleArrowLengthSizeRatio,
                      bool scaleOption);

public:

  static TranslateScaleGizmo* translateAndScale(const CoordinateSystem& cs,
                                                double size,
                                                double scale,
                                                double maxScale,
                                                double lineWidthRatio,
                                                double headLengthRatio,
                                                double headWidthRatio,
                                                double scaleArrowLengthSizeRatio);

  static TranslateScaleGizmo* translate(const CoordinateSystem& cs,
                                        double size,
                                        double lineWidthRatio,
                                        double headLengthRatio,
                                        double headWidthRatio);

  ~TranslateScaleGizmo();
  
  void onBaseChanged(const Arrow& arrow);
  
  const CoordinateSystem getCoordinateSystem() const{
    return *_cs;
  }
  
  double getScale() const{
    return _scale;
  }
  
  void setListener(TranslateScaleGizmoListener* listener){
    _listener = listener;
  }
  
  void onDraggingEnded(const Arrow& arrow);
  
};

#endif
