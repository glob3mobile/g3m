//
//  NonOverlappingMarksRenderer.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/1/15.
//
//

#ifndef __G3MiOSSDK__NonOverlappingMarksRenderer__
#define __G3MiOSSDK__NonOverlappingMarksRenderer__

#include <stdio.h>

#include "DefaultRenderer.hpp"
#include "Geodetic3D.hpp"
#include "Vector2F.hpp"
#include "Vector3D.hpp"
#include "IImageBuilderListener.hpp"

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

class MarkWidget{
  GLState* _glState;
  Geometry2DGLFeature* _geo2Dfeature;
  ViewportExtentGLFeature* _viewportExtent;
  
  const IImage* _image;
  std::string _imageName;
  IImageBuilder* _imageBuilder;
  TexturesHandler* _texHandler;
  
  float _halfWidth;
  float _halfHeight;
  
  float _x, _y; //Screen position
  
  class WidgetImageListener: public IImageBuilderListener{
    MarkWidget* _widget;
  public:
    WidgetImageListener(MarkWidget* widget):_widget(widget){}
    
    virtual void imageCreated(const IImage*      image,
                              const std::string& imageName){
      _widget->prepareWidget(image, imageName);
    }
    
    virtual void onError(const std::string& error){
      ILogger::instance()->logError(error);
    }
    
  };
  
  void prepareWidget(const IImage*      image,
                     const std::string& imageName);
  
public:
  MarkWidget(IImageBuilder* imageBuilder);
  
  ~MarkWidget();
  
  void init(const G3MRenderContext *rc,
            float viewportWidth, float viewportHeight);
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void setScreenPos(float x, float y);
  Vector2F getScreenPos() const{ return Vector2F(_x, _y);}
  void resetPosition();
  
  float getHalfWidth() const{ return _halfWidth;}
  float getHalfHeight() const{ return _halfHeight;}
  
  void onResizeViewportEvent(int width, int height);
  
  bool isReady() const{
    return _image != NULL;
  }
  
};


class NonOverlappingMark{
  float _springLengthInPixels;
  
  mutable Vector3D* _cartesianPos;
  Geodetic3D _geoPosition;
  
  float _dX, _dY; //Velocity vector (pixels per second)
  float _fX, _fY; //Applied Force
  
  MarkWidget _widget;
  MarkWidget _anchorWidget;
  
  const float _springK;
  const float _electricCharge;
  const float _maxWidgetSpeedInPixels;
  
public:
  
  NonOverlappingMark(IImageBuilder* imageBuilderWidget,
                     IImageBuilder* imageBuilderAnchor,
                     const Geodetic3D& position,
                     float springLengthInPixels = 10.0f,
                     float springK = 200.0f,
                     float electricCharge = 3000.0f,
                     float maxWidgetSpeedInPixels = 20.0f);
  
  ~NonOverlappingMark();
  
  Vector3D getCartesianPosition(const Planet* planet) const;
  
  void computeAnchorScreenPos(const Camera* cam, const Planet* planet);
  
  Vector2F getScreenPos() const{ return _widget.getScreenPos();}
  Vector2F getAnchorScreenPos() const{ return _anchorWidget.getScreenPos();}
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void applyCoulombsLaw(NonOverlappingMark* that); //EM
  void applyCoulombsLawFromAnchor(NonOverlappingMark* that);
  
  void applyHookesLaw();   //Spring
  
  void applyForce(float x, float y){
    _fX += x;
    _fY += y;
  }
  
  void updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight);
  
  void onResizeViewportEvent(int width, int height);
  
  void resetWidgetPositionVelocityAndForce(){ _widget.resetPosition(); _dX = 0; _dY = 0; _fX = 0; _fY = 0;}
  
};

class NonOverlappingMarksRenderer: public DefaultRenderer{
  
  int _maxVisibleMarks;
  
  std::vector<NonOverlappingMark*> _visibleMarks;
  std::vector<NonOverlappingMark*> _marks;
  
  void computeMarksToBeRendered(const Camera* cam, const Planet* planet);
  
  long long _lastPositionsUpdatedTime;
  
  GLState * _connectorsGLState;
  void renderConnectorLines(const G3MRenderContext* rc);
  
  void computeForces(const Camera* cam, const Planet* planet);
  void renderMarks(const G3MRenderContext* rc, GLState* glState);
  void applyForces(long long now, const Camera* cam);
  
  
public:
  NonOverlappingMarksRenderer(int maxVisibleMarks = 30);
  
  ~NonOverlappingMarksRenderer();
  
  void addMark(NonOverlappingMark* mark);
  
  virtual RenderState getRenderState(const G3MRenderContext* rc) {
    return RenderState::ready();
  }
  
  virtual void render(const G3MRenderContext* rc,
                      GLState* glState);
  
  virtual bool onTouchEvent(const G3MEventContext* ec,
                            const TouchEvent* touchEvent) {
    return false;
  }
  
  virtual void onResizeViewportEvent(const G3MEventContext* ec, int width, int height);
  
  virtual void start(const G3MRenderContext* rc) {
    
  }
  
  virtual void stop(const G3MRenderContext* rc) {
    
  }
  
  virtual SurfaceElevationProvider* getSurfaceElevationProvider() {
    return NULL;
  }
  
  virtual PlanetRenderer* getPlanetRenderer() {
    return NULL;
  }
  
  virtual bool isPlanetRenderer() {
    return false;
  }
  
  
};

#endif /* defined(__G3MiOSSDK__NonOverlappingMarksRenderer__) */
