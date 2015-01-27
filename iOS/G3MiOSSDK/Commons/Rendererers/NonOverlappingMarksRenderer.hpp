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
  MarkWidget(IImageBuilder* imageBuilder,
             const G3MRenderContext *rc,
             float viewportWidth, float viewportHeight);
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void setScreenPos(float x, float y);
  
  void onResizeViewportEvent(int width, int height);
  
  bool isReady() const{
    return _image != NULL;
  }
  
};


class NonOverlappingMark{
  IImageBuilder* _imageBuilderWidget;
  IImageBuilder* _imageBuilderAnchor;
  float _springLengthInPixels;

  mutable Vector3D* _cartesianPos;
  Geodetic3D _geoPosition;
  
  float _dX, _dY; //Velocity vector (pixels per second)
  Vector2F* _anchorScreenPos;
  Vector2F* _screenPos;
  
  MarkWidget* _widget;
  MarkWidget* _anchorWidget;
  
public:
  
  NonOverlappingMark(IImageBuilder* imageBuilderWidget,
                     IImageBuilder* imageBuilderAnchor,
                     const Geodetic3D& position,
                     float springLengthInPixels);
  
  Vector3D getCartesianPosition(const Planet* planet) const;
  
  void computeAnchorScreenPos(const Camera* cam, const Planet* planet);
  
  Vector2F* getScreenPos() const{ return _screenPos;}
  Vector2F* getAnchorScreenPos() const{ return _anchorScreenPos;}
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void applyCoulombsLaw(NonOverlappingMark* that); //EM
  
  void applyHookesLaw();   //Spring
  
  void applyForce(float x, float y){
    _dX += x;
    _dY += y;
  }
  
  void updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight);
  
  void onResizeViewportEvent(int width, int height);
  
};

class NonOverlappingMarksRenderer: public DefaultRenderer{
  
  int _maxVisibleMarks;
  
  std::vector<NonOverlappingMark*> _visibleMarks;
  std::vector<NonOverlappingMark*> _marks;
  
  void computeMarksToBeRendered(const Camera* cam, const Planet* planet);
  
  long long _lastPositionsUpdatedTime;
  
  GLState * _connectorsGLState;
  void renderConnectorLines(const G3MRenderContext* rc);
  
  
public:
  NonOverlappingMarksRenderer(int maxVisibleMarks = 30);
  
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
