//
//  NonOverlappingMarksRenderer.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/1/15.
//
//

#ifndef __G3MiOSSDK__NonOverlappingMarksRenderer__
#define __G3MiOSSDK__NonOverlappingMarksRenderer__

#include "DefaultRenderer.hpp"
#include "Geodetic3D.hpp"
#include "Vector2F.hpp"
#include "Vector3D.hpp"
#include "IImageBuilderListener.hpp"
#include "MutableVector2F.hpp"

class IImageBuilder;
class Geodetic3D;
class Vector2D;
class Camera;
class Planet;
class GLState;
class IImage;
class TextureIDReference;
class Geometry2DGLFeature;
class ViewportExtentGLFeature;
class TexturesHandler;
class NonOverlappingMark;
class SimpleTextureMapping;

class NonOverlappingMarkTouchListener {
public:
  virtual ~NonOverlappingMarkTouchListener() {
  }

  virtual bool touchedMark(const NonOverlappingMark* mark,
                           const Vector2F& touchedPixel) = 0;
};


class MarkWidget {
private:
  GLState* _glState;
  Geometry2DGLFeature* _geo2Dfeature;
  ViewportExtentGLFeature* _viewportExtentGLFeature;
#ifdef C_CODE
  const IImage* _image;
#endif
#ifdef JAVA_CODE
  private IImage _image;
#endif
  std::string _imageName;
  IImageBuilder* _imageBuilder;
  TexturesHandler* _texHandler;

  IFloatBuffer*         _vertices;
  SimpleTextureMapping* _textureMapping;

  float _halfWidth;
  float _halfHeight;

  // Screen position
  float _x;
  float _y;

  class WidgetImageListener: public IImageBuilderListener {
    MarkWidget* _widget;
  public:
    WidgetImageListener(MarkWidget* widget) :
    _widget(widget)
    {
    }

    ~WidgetImageListener() {}

    void imageCreated(const IImage*      image,
                      const std::string& imageName) {
      _widget->prepareWidget(image, imageName);
    }

    void onError(const std::string& error) {
      ILogger::instance()->logError(error);
    }
  };

  void prepareWidget(const IImage*      image,
                     const std::string& imageName);

public:
  MarkWidget(IImageBuilder* imageBuilder);

  ~MarkWidget();

  void init(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc,
              GLState* glState
//              float x,
//              float y
              );

  void setAndClampScreenPos(float x,
                            float y,
                            int viewportWidth,
                            int viewportHeight,
                            float margin);

  void setScreenPos(float x, float y);

  Vector2F getScreenPos() const { return Vector2F(_x, _y); }
  void getScreenPosition(MutableVector2F& result) const {
    result.set(_x, _y);
  }

  void resetPosition();

  void onResizeViewportEvent(int width, int height);

  bool isReady() const{
    return _image != NULL;
  }

  int getWidth() const;
  int getHeight() const;

};


class NonOverlappingMark {
private:
  float _springLengthInPixels;

//  MutableVector2F _widgetScreenPosition;
//  MutableVector2F _anchorScreenPosition;

  mutable Vector3D* _cartesianPos;
  Geodetic3D _geoPosition;

  MutableVector2F _speed;
  MutableVector2F _force;

  MarkWidget* _widget;
  MarkWidget* _anchorWidget;

  GLState* _springGLState;
  IFloatBuffer* _springVertices;
  ViewportExtentGLFeature* _springViewportExtentGLFeature;

  const float _springK;
  const float _maxSpringLength;
  const float _minSpringLength;
  const float _electricCharge;
  const float _anchorElectricCharge;
  const float _resistanceFactor;

  std::string _id;

  NonOverlappingMarkTouchListener* _touchListener;

//  float _enclosingRadius;

public:

  NonOverlappingMark(IImageBuilder* imageBuilderWidget,
                     IImageBuilder* imageBuilderAnchor,
                     const Geodetic3D& position,
                     NonOverlappingMarkTouchListener* touchListener = NULL,
                     float springLengthInPixels = 100.0f,
                     float springK = 70.0f,
                     float minSpringLength = 0.0f,
                     float maxSpringLength = 0.0f,
                     float electricCharge = 3000.0f,
                     float anchorElectricCharge = 2000.0f,
                     float resistanceFactor = 0.95f);

  void setID(const std::string& id) {
    _id = id;
  }

  const std::string getID() const {
    return _id;
  }

  ~NonOverlappingMark();

  Vector3D getCartesianPosition(const Planet* planet) const;

  void computeAnchorScreenPos(const Camera* camera,
                              const Planet* planet);

  Vector2F getScreenPos() const       { return _widget->getScreenPos(); }
  Vector2F getAnchorScreenPos() const { return _anchorWidget->getScreenPos(); }

  void renderWidget(const G3MRenderContext* rc,
                    GLState* glState);

  void renderAnchorWidget(const G3MRenderContext* rc,
                          GLState* glState);

  void renderSpringWidget(const G3MRenderContext* rc,
                          GLState* glState);

  void applyCoulombsLaw(NonOverlappingMark* that);
  void applyCoulombsLawFromAnchor(NonOverlappingMark* that);

  void applyHookesLaw();   //Spring

  void applyForce(float x, float y) {
    _force.add(x, y);
  }

  void updatePositionWithCurrentForce(float timeInSeconds,
                                      int viewportWidth,
                                      int viewportHeight,
                                      float viewportMargin);

  void onResizeViewportEvent(int width, int height);

  void resetWidgetPositionVelocityAndForce() {
    _widget->resetPosition();
//    _widgetScreenPosition.put(NANF, NANF);
    _speed.set(0, 0);
    _force.set(0, 0);
  }

  int getWidth() const;
  int getHeight() const;

  bool onTouchEvent(const Vector2F& touchedPixel);

};


class NonOverlappingMarksVisibilityListener {
public:
  virtual ~NonOverlappingMarksVisibilityListener() {
  }

  virtual void onVisibilityChange(const std::vector<NonOverlappingMark*>& visible) = 0;
};


class NonOverlappingMarksRenderer: public DefaultRenderer {
private:
  const size_t _maxVisibleMarks;
  const float  _viewportMargin;

  std::vector<NonOverlappingMark*> _marks;

  std::vector<NonOverlappingMark*> _visibleMarks;
  IStringBuilder* _visibleMarksIDsBuilder;
  std::string     _visibleMarksIDs;

  std::vector<NonOverlappingMarksVisibilityListener*> _visibilityListeners;

  NonOverlappingMarkTouchListener* _touchListener;

  void computeMarksToBeRendered(const Camera* camera,
                                const Planet* planet);

  long long _lastPositionsUpdatedTime;

  void computeForces(const Camera* camera, const Planet* planet);
  void renderMarks(const G3MRenderContext* rc,
                   GLState* glState);
  void applyForces(long long now, const Camera* camera);

public:
  NonOverlappingMarksRenderer(size_t maxVisibleMarks,
                              float viewportMargin = 5);

  ~NonOverlappingMarksRenderer();

  void addMark(NonOverlappingMark* mark);

  void removeAllMarks();

  void addVisibilityListener(NonOverlappingMarksVisibilityListener* listener) {
    _visibilityListeners.push_back(listener);
  }

  void removeAllListeners();

  RenderState getRenderState(const G3MRenderContext* rc) {
    return RenderState::ready();
  }

  void render(const G3MRenderContext* rc,
              GLState* glState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);
  
  void setTouchListener(NonOverlappingMarkTouchListener* touchListener);
  
};

#endif
