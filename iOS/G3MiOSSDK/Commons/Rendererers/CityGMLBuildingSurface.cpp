//
//  CityGMLBuildingSurface.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#include "CityGMLBuildingSurface.hpp"

CityGMLBuildingSurface* CityGMLBuildingSurface::createFromArrayOfCityGMLWGS84Coordinates(const std::vector<double> coor, CityGMLBuildingSurfaceType type){
  
  std::vector<Geodetic3D*> geodeticCoordinates;
  
  for (int i = 0; i < coor.size(); i += 3) {
    const double lat = coor[i + 1];
    const double lon = coor[i];
    const double h = coor[i + 2];
    geodeticCoordinates.push_back(new Geodetic3D(Geodetic3D::fromDegrees(lat, lon, h)));
  }
  
  switch (type) {
    case WALL:
      return new CityGMLBuildingWall(geodeticCoordinates);
    case GROUND:
      return new CityGMLBuildingGround(geodeticCoordinates);
    case ROOF:
      return new CityGMLBuildingRoof(geodeticCoordinates);
  }
  
  return NULL;
}
