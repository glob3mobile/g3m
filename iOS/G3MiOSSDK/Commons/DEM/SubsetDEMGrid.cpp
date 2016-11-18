//
//  SubsetDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/16.
//
//

#include "SubsetDEMGrid.hpp"

#include "Vector3D.hpp"
#include "Projection.hpp"
#include "Vector2D.hpp"
#include "IMathUtils.hpp"
//#include "ILogger.hpp"


const SubsetDEMGrid* SubsetDEMGrid::create(const DEMGrid* grid,
                                           const Sector&  sector) {
  const Projection* projection = grid->getProjection();
  const Vector2D lowerUV = projection->getUV(sector._lower);
  const Vector2D upperUV = projection->getUV(sector._upper);

//  ILogger::instance()->logInfo("%s -> %s | %s",
//                               lowerUV.description().c_str(),
//                               upperUV.description().c_str(),
//                               upperUV.sub(lowerUV).description().c_str());

  const Vector2I gridExtent =  grid->getExtent();

  const IMathUtils* mu = IMathUtils::instance();

  const int offsetX = (int) mu->round(lowerUV._x * gridExtent._x);
  const int offsetY = (int) mu->round(upperUV._y * gridExtent._y);
  const int width   = (int) (mu->round(upperUV._x * gridExtent._x) - offsetX);
  const int height  = (int) (mu->round(lowerUV._y * gridExtent._y) - offsetY);

  return new SubsetDEMGrid(grid,
                           sector,
                           Vector2I(width, height),
                           offsetX,
                           offsetY);
}

SubsetDEMGrid::SubsetDEMGrid(const DEMGrid*  grid,
                             const Sector&   sector,
                             const Vector2I& extent,
                             const int       offsetX,
                             const int       offsetY) :
DecoratorDEMGrid(grid, sector, extent),
_offsetX(offsetX),
_offsetY(offsetY)
{

}

SubsetDEMGrid::~SubsetDEMGrid() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

double SubsetDEMGrid::getElevationAt(int x, int y) const {
  return _grid->getElevationAt(_offsetX + x,
                               _offsetY + y);
}
