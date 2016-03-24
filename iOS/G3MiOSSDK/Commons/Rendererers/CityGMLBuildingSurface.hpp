//
//  CityGMLBuildingSurface.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#ifndef CityGMLBuildingSurface_hpp
#define CityGMLBuildingSurface_hpp

#include "Surface.hpp"

enum CityGMLBuildingSurfaceType{
  WALL,
  ROOF,
  GROUND
};

class CityGMLBuildingSurface: public Surface{
  CityGMLBuildingSurfaceType _type;
public:
  CityGMLBuildingSurface(const std::vector<Geodetic3D*>& geodeticCoordinates, CityGMLBuildingSurfaceType type):
  Surface(geodeticCoordinates),
  _type(type){
  }
  
  CityGMLBuildingSurfaceType getType(){
    return _type;
  }
  
  static CityGMLBuildingSurface* createFromArrayOfCityGMLWGS84Coordinates(const std::vector<double> coor, CityGMLBuildingSurfaceType type){
    
    std::vector<Geodetic3D*> geodeticCoordinates;
    
    for (int i = 0; i < coor.size(); i += 3) {
      const double lat = coor[i + 1];
      const double lon = coor[i];
      const double h = coor[i + 2];
      geodeticCoordinates.push_back(new Geodetic3D(Geodetic3D::fromDegrees(lat, lon, h)));
    }
    return new CityGMLBuildingSurface(geodeticCoordinates, type);
  }
};

class CityGLMBuildingWall: public CityGMLBuildingSurface{
public:
  CityGLMBuildingWall(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGMLBuildingSurface(geodeticCoordinates, WALL){}
};

class CityGLMBuildingRoof: public CityGMLBuildingSurface{
public:
  CityGLMBuildingRoof(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGMLBuildingSurface(geodeticCoordinates, ROOF){}
};

class CityGLMBuildingGround: public CityGMLBuildingSurface{
public:
  CityGLMBuildingGround(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGMLBuildingSurface(geodeticCoordinates, GROUND){}
};



#endif /* CityGMLBuildingSurface_hpp */
