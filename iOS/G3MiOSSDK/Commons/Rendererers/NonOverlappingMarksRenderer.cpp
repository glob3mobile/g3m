//
//  NonOverlappingMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/1/15.
//
//

#include "NonOverlappingMarksRenderer.hpp"

NonOverlappingMark::NonOverlappingMark(IImageBuilder* imageBuilder, Geodetic3D& position, float springLengthInPixels):
_imageBuilder(imageBuilder),
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_anchorScreenPos(NULL),
_screenPos(NULL)
{
  
}

NonOverlappingMarksRenderer::NonOverlappingMarksRenderer(int maxVisibleMarks):
_maxVisibleMarks(maxVisibleMarks)
{
  
}


void NonOverlappingMarksRenderer::addMark(NonOverlappingMark* mark){
  _marks.push_back(mark);
  
}

void NonOverlappingMarksRenderer::render(const G3MRenderContext* rc, GLState* glState){
  
}