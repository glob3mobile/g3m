//
//  SurfaceElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/2/13.
//
//

#include "SurfaceElevationProvider.hpp"

SurfaceElevationProvider::SurfaceElevationProvider_Visitor::SurfaceElevationProvider_Visitor(const Sector& sector,
                                                                                             const ElevationData* ed,
                                                                                             double verticalExaggeration):
_sector(sector), _elevationData(ed), _verticalExaggeration(verticalExaggeration){}

bool SurfaceElevationProvider::SurfaceElevationProvider_Visitor::visitElement(const Sector& sector,
                                                                              const void*   element) const{
  SurfaceElevationListener* listener = (SurfaceElevationListener*) element;
  listener->elevationChanged(sector, _elevationData, _verticalExaggeration);
  return false;
}

bool SurfaceElevationProvider::SurfaceElevationProvider_Visitor::visitElement(const Geodetic2D& geodetic,
                                                                              const void*   element) const{
  SurfaceElevationListener* listener = (SurfaceElevationListener*) element;
  double height = _elevationData->getElevationAt(geodetic);
  listener->elevationChanged(geodetic, height, _verticalExaggeration);
  return false;
}

void SurfaceElevationProvider::SurfaceElevationProvider_Tree::notifyListeners(const Sector& sector,
                                                                              const ElevationData* ed,
                                                                              double verticalExaggeration) const{
  SurfaceElevationProvider_Visitor visitor(sector, ed, verticalExaggeration);
  acceptVisitor(sector, visitor);
}