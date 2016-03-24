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

enum CityGLMBuildingSurfaceType{
  WALL,
  ROOF,
  GROUND
};

class CityGLMBuildingSurface: public Surface{
  CityGLMBuildingSurfaceType _type;
public:
  CityGLMBuildingSurface(const std::vector<Geodetic3D*>& geodeticCoordinates, CityGLMBuildingSurfaceType type):
  Surface(geodeticCoordinates),
  _type(type){
  }
  
  CityGLMBuildingSurfaceType getType(){
    return _type;
  }
  
  static CityGLMBuildingSurface* createFromArrayOfCityGMLWGS84Coordinates(const std::vector<double> coor, CityGLMBuildingSurfaceType type){
    
    std::vector<Geodetic3D*> geodeticCoordinates;
    
    for (int i = 0; i < coor.size(); i += 3) {
      const double lat = coor[i + 1];
      const double lon = coor[i];
      const double h = coor[i + 2];
      geodeticCoordinates.push_back(new Geodetic3D(Geodetic3D::fromDegrees(lat, lon, h)));
    }
    return new CityGLMBuildingSurface(geodeticCoordinates, type);
  }
};

class CityGLMBuildingWall: public CityGLMBuildingSurface{
public:
  CityGLMBuildingWall(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGLMBuildingSurface(geodeticCoordinates, WALL){}
};

class CityGLMBuildingRoof: public CityGLMBuildingSurface{
public:
  CityGLMBuildingRoof(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGLMBuildingSurface(geodeticCoordinates, ROOF){}
};

class CityGLMBuildingGround: public CityGLMBuildingSurface{
public:
  CityGLMBuildingGround(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGLMBuildingSurface(geodeticCoordinates, GROUND){}
};



#endif /* CityGMLBuildingSurface_hpp */
