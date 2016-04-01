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
  
  bool _isVisible;
  
public:
  
  ~CityGMLBuildingSurface(){
  }
  
  void setIsVisible(bool b){
    _isVisible = b;
  }
  
  bool isVisible(){
    return _isVisible;
  }
  
  CityGMLBuildingSurface(const std::vector<Geodetic3D*>& geodeticCoordinates, CityGMLBuildingSurfaceType type):
  Surface(geodeticCoordinates),
  _type(type),
  _isVisible(true){
  }
  
  CityGMLBuildingSurfaceType getType(){
    return _type;
  }
  
  static CityGMLBuildingSurface* createFromArrayOfCityGMLWGS84Coordinates(const std::vector<double> coor,
                                                                          CityGMLBuildingSurfaceType type);
};

class CityGMLBuildingWall: public CityGMLBuildingSurface{
public:
  CityGMLBuildingWall(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGMLBuildingSurface(geodeticCoordinates, WALL){}
};

class CityGMLBuildingRoof: public CityGMLBuildingSurface{
public:
  CityGMLBuildingRoof(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGMLBuildingSurface(geodeticCoordinates, ROOF){}
};

class CityGMLBuildingGround: public CityGMLBuildingSurface{
public:
  CityGMLBuildingGround(const std::vector<Geodetic3D*>& geodeticCoordinates):
  CityGMLBuildingSurface(geodeticCoordinates, GROUND){}
};



#endif /* CityGMLBuildingSurface_hpp */
