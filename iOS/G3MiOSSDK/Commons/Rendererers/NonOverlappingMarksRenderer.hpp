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
#include "Vector2D.hpp"

class IImageBuilder;
class Geodetic3D;
class Vector2D;


class NonOverlappingMark{
  IImageBuilder* _imageBuilder;
  float _springLengthInPixels;
  Geodetic3D _geoPosition;
  
  Vector2D* _anchorScreenPos;
  Vector2D* _screenPos;
public:
  NonOverlappingMark(IImageBuilder* imageBuilder, Geodetic3D& position, float springLengthInPixels);
  
};

class NonOverlappingMarksRenderer: public DefaultRenderer{
  
  int _maxVisibleMarks;
  
  std::vector<NonOverlappingMark*> _marks;
  
  
public:
  NonOverlappingMarksRenderer(int maxVisibleMarks = 30);
  
  void addMark(NonOverlappingMark* mark);
  
  virtual RenderState getRenderState(const G3MRenderContext* rc) {
    return RenderState::ready();
  }
  
  virtual void render(const G3MRenderContext* rc,
                      GLState* glState) = 0;
  
  virtual bool onTouchEvent(const G3MEventContext* ec,
                            const TouchEvent* touchEvent) {
    return false;
  }
  
  virtual void onResizeViewportEvent(const G3MEventContext* ec,
                                     int width, int height) = 0;
  
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
