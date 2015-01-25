//
//  NonOverlappingMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/1/15.
//
//

#include "NonOverlappingMarksRenderer.hpp"

#include "Camera.hpp"
#include "Planet.hpp"

NonOverlappingMark::NonOverlappingMark(IImageBuilder* imageBuilder, Geodetic3D& position, float springLengthInPixels):
_imageBuilder(imageBuilder),
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_anchorScreenPos(NULL),
_screenPos(NULL),
_cartesianPos(NULL),
_dX(0),
_dY(0)
{
  
}

Vector3D NonOverlappingMark::getCartesianPosition(const Planet* planet) const{
  if (_cartesianPos != NULL){
    _cartesianPos = new Vector3D(planet->toCartesian(_geoPosition));
  }
  return *_cartesianPos;
}

void NonOverlappingMark::computeScreenPos(const Camera* cam, const Planet* planet){
  if (_screenPos != NULL){
    delete _screenPos;
  }
  
  _screenPos = new Vector2F(cam->point2Pixel(getCartesianPosition(planet)));
}


void NonOverlappingMark::applyCoulombsLaw(const NonOverlappingMark* that){ //EM

}

void NonOverlappingMark::applyHookesLaw(const NonOverlappingMark* that){   //Spring

}

#pragma-mark Renderer

NonOverlappingMarksRenderer::NonOverlappingMarksRenderer(int maxVisibleMarks):
_maxVisibleMarks(maxVisibleMarks)
{
  
}


void NonOverlappingMarksRenderer::addMark(NonOverlappingMark* mark){
  _marks.push_back(mark);
  
}

std::vector<NonOverlappingMark*> NonOverlappingMarksRenderer::getMarksToBeRendered(const Camera* cam, const Planet* planet) const{
  
  std::vector<NonOverlappingMark*> out;
  
  for (int i = 0; i < _marks.size(); i++) {
    NonOverlappingMark* m = _marks[i];
    
    if (cam->getFrustumInModelCoordinates()->contains(m->getCartesianPosition(planet)) ){
      out.push_back(m);
      if (out.size() == _maxVisibleMarks){
        break;
      }
    }
  }
  
  return out;
}

void NonOverlappingMarksRenderer::render(const G3MRenderContext* rc, GLState* glState){
  
  std::vector<NonOverlappingMark*> visibleMarks = getMarksToBeRendered(rc->getCurrentCamera(), rc->getPlanet());
  
  //Compute Mark Positions
  
  //Draw Lines
  
  //Draw Anchors
  
  //Draw Marks
  
  
}