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

class MarkWidget{
  GLState* _glState;
  Geometry2DGLFeature* _geo2Dfeature;
  ViewportExtentGLFeature* _viewportExtent;
  
public:
  MarkWidget(const TextureIDReference* texID,
             float width, float height,
             float viewportWidth, float viewportHeight);
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void setScreenPos(float x, float y);
  
  void onResizeViewportEvent(int width, int height);
  
};


class NonOverlappingMark{
  IImageBuilder* _imageBuilder;
  float _springLengthInPixels;

  mutable Vector3D* _cartesianPos;
  Geodetic3D _geoPosition;
  
  float _dX, _dY; //Velocity vector (pixels per second)
  Vector2F* _anchorScreenPos;
  Vector2F* _screenPos;
  
  const IImage* _image;
  std::string _imageName;
  
  class NonOverlappingMarkImageListener: public IImageBuilderListener{
    NonOverlappingMark* _mark;
  public:
    NonOverlappingMarkImageListener(NonOverlappingMark* mark):_mark(mark){}
    
    virtual void imageCreated(const IImage*      image,
                              const std::string& imageName){
      _mark->_image = image;
      _mark->_imageName = imageName;
    }
    
    virtual void onError(const std::string& error){
      ILogger::instance()->logError(error);
    }
    
  };
  
  MarkWidget* _widget;
  
  MarkWidget* _anchorWidget;
  
public:
  
  NonOverlappingMark(IImageBuilder* imageBuilder,
                     const Geodetic3D& position,
                     float springLengthInPixels);
  
  Vector3D getCartesianPosition(const Planet* planet) const;
  
  void computeAnchorScreenPos(const Camera* cam, const Planet* planet);
  
  Vector2F* getScreenPos() const{ return _screenPos;}
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void applyCoulombsLaw(NonOverlappingMark* that); //EM
  
  void applyHookesLaw();   //Spring
  
  void applyForce(float x, float y){
    _dX += x;
    _dY += y;
  }
  
  void updatePositionWithCurrentForce(double elapsedMS);
  
  void onResizeViewportEvent(int width, int height);
  
};

class NonOverlappingMarksRenderer: public DefaultRenderer{
  
  int _maxVisibleMarks;
  
  std::vector<NonOverlappingMark*> _marks;
  
  std::vector<NonOverlappingMark*> getMarksToBeRendered(const Camera* cam, const Planet* planet) const;
  
  long long _lastPositionsUpdatedTime;
  
  
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
