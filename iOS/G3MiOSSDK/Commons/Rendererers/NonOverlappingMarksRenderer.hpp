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

class IImageBuilder;
class Geodetic3D;
class Vector2D;
class Camera;
class Planet;
class GLState;
class IImage;


class NonOverlappingMark{
  IImageBuilder* _imageBuilder;
  float _springLengthInPixels;

  mutable Vector3D* _cartesianPos;
  Geodetic3D _geoPosition;
  
  float _dX, _dY; //Velocity vector (pixels per second)
  Vector2F* _anchorScreenPos;
  Vector2F* _screenPos;
  
  GLState* _glState;
  
  IImage* _image;
public:
  
  NonOverlappingMark(IImageBuilder* imageBuilder, Geodetic3D& position, float springLengthInPixels);
  
  Vector3D getCartesianPosition(const Planet* planet) const;
  
  void computeScreenPos(const Camera* cam, const Planet* planet);
  
  Vector2F* getScreenPos() const{ return _screenPos;}
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void applyCoulombsLaw(const NonOverlappingMark* that); //EM
  
  void applyHookesLaw(const NonOverlappingMark* that);   //Spring
  
};

class NonOverlappingMarksRenderer: public DefaultRenderer{
  
  int _maxVisibleMarks;
  
  std::vector<NonOverlappingMark*> _marks;
  
  std::vector<NonOverlappingMark*> getMarksToBeRendered(const Camera* cam, const Planet* planet) const;
  
  
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
  
  virtual void onResizeViewportEvent(const G3MEventContext* ec,
                                     int width, int height){}
  
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
