//
//  CityGMLBuildingTessellator.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 13/4/16.
//
//

#ifndef CityGMLBuildingTessellator_hpp
#define CityGMLBuildingTessellator_hpp

#include "CityGMLBuilding.hpp"
#include "IndexedMesh.hpp"
#include "CompositeMesh.hpp"
#include "CityGMLBuildingColorProvider.hpp"
#include "Mark.hpp"

class DefaultCityGMLBuildingTessellatorData: public CityGMLBuildingTessellatorData{
public:
  Mesh* _containerMesh;
  short _firstVertexIndexWithinContainerMesh;
  short _lastVertexIndexWithinContainerMesh;
};

class CityGMLBuildingTessellator{
  
public:
  static short addTrianglesCuttingEarsForAllWalls(const CityGMLBuilding* building,
                                                  FloatBufferBuilderFromCartesian3D& fbb,
                                                  FloatBufferBuilderFromCartesian3D& normals,
                                                  ShortBufferBuilder& indexes,
                                                  FloatBufferBuilderFromColor& colors,
                                                  const double baseHeight,
                                                  const Planet& planet,
                                                  const short firstIndex,
                                                  const Color& color,
                                                  const bool includeGround,
                                                  const ElevationData* elevationData);
  
  
  static Mesh* createIndexedMeshWithColorPerVertex(const CityGMLBuilding* building,
                                                   const Planet planet,
                                                   const bool fixOnGround,
                                                   const Color color,
                                                   const bool includeGround,
                                                   const ElevationData* elevationData);
  
  static Mesh* createMesh(const std::vector<CityGMLBuilding*> buildings,
                          const Planet& planet,
                          const bool fixOnGround,
                          const bool includeGround,
                          CityGMLBuildingColorProvider* colorProvider,
                          const ElevationData* elevationData);
  
  static void changeColorOfBuildingInBoundedMesh(const CityGMLBuilding* building, const Color& color);
  
  static Mark* createMark(const CityGMLBuilding* building, const bool fixOnGround) {
    const double deltaH = fixOnGround ? building->getBaseHeight() : 0;
    
    const Geodetic3D center = building->getCenter();
    const Geodetic3D pos = Geodetic3D::fromDegrees(center._latitude._degrees, center._longitude._degrees, center._height
                                                   - deltaH);
    
    Mark* m = new Mark(building->_name, pos, RELATIVE_TO_GROUND, 100.0);
    return m;
  }
  
};

#endif /* CityGMLBuildingTessellator_hpp */
