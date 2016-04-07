//
//  CityGMLBuilding.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#include "CityGMLBuilding.hpp"

Mesh* CityGMLBuilding::createMesh(const std::vector<CityGMLBuilding*> buildings,
                                  const Planet& planet,
                                  const bool fixOnGround,
                                  const bool includeGround,
                                  CityGMLBuildingColorProvider* colorProvider) {
  
  CompositeMesh* cm = NULL;
  int buildingCounter = 0;
  int meshesCounter = 0;
  
  
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  ShortBufferBuilder* indexes = new ShortBufferBuilder();
  FloatBufferBuilderFromColor* colors = new FloatBufferBuilderFromColor();
  
  std::vector<short> buildingVertexIndex;
  std::vector<CityGMLBuilding*> processedBuildings;
  
  const Color colorWheel = Color::red();
  
  short firstIndex = 0;
  for (int i = 0; i < buildings.size(); i++) {
    CityGMLBuilding* b = buildings[i];
    
    buildingVertexIndex.push_back(vertices->size() / 3);
    processedBuildings.push_back(b);
    
    const double baseHeight = fixOnGround ? b->getBaseHeight() : 0;
    
    if (colorProvider == NULL){
      firstIndex = b->addTrianglesCuttingEarsForAllWalls(*vertices, *normals, *indexes, *colors, baseHeight, planet, firstIndex,
                                                         colorWheel.wheelStep((int)buildings.size(), buildingCounter),
                                                         includeGround);
      
    } else{
      firstIndex = b->addTrianglesCuttingEarsForAllWalls(*vertices, *normals, *indexes, *colors, baseHeight, planet, firstIndex,
                                                         colorProvider->getColor(b),
                                                         includeGround);
    }
    
    buildingVertexIndex.push_back(vertices->size() / 3);
    
    buildingCounter++;
    
    if (firstIndex > 30000) { //Max number of vertex per mesh (CHECK SHORT RANGE)
      if (cm == NULL) {
        cm = new CompositeMesh();
      }
      
      //Adding new mesh
      IndexedMesh* im = new IndexedMesh(GLPrimitive::triangles(), vertices->getCenter(), vertices->create(), true, indexes->create(),
                                        true, 1.0f, 1.0f, NULL, colors->create(), 1.0f, true, normals->create());
      
      cm->addMesh(im);
      meshesCounter++;
      
      //Linking buildings with its mesh
      for (int j = 0; j < processedBuildings.size(); j++) {
        processedBuildings[j]->setContainerMesh(im, buildingVertexIndex[j*2], buildingVertexIndex[j*2+1]);
      }
      
      //Reset
      buildingVertexIndex.clear();
      processedBuildings.clear();
      
      delete vertices;
      delete normals;
#ifdef C_CODE
      delete indexes;
#endif
      delete colors;
      vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
      normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
      indexes = new ShortBufferBuilder();
      colors = new FloatBufferBuilderFromColor();
      firstIndex = 0;
    }
  }
  
  //Adding last mesh
  IndexedMesh* im = new IndexedMesh(GLPrimitive::triangles(), vertices->getCenter(), vertices->create(), true, indexes->create(),
                                    true, 1.0f, 1.0f, NULL, colors->create(), 1.0f, true, normals->create());
  
  //Linking buildings with its mesh
  for (int j = 0; j < processedBuildings.size(); j++) {
    processedBuildings[j]->setContainerMesh(im, buildingVertexIndex[j*2], buildingVertexIndex[j*2+1]);
  }
  
  delete vertices;
  delete normals;
#ifdef C_CODE
  delete indexes;
#endif
  delete colors;
  
  if (cm == NULL){
    ILogger::instance()->logInfo("One single mesh created for %d buildings", buildingCounter);
    return im;
  }
  
  cm->addMesh(im);
  meshesCounter++;
  
  ILogger::instance()->logInfo("%d meshes created for %d buildings", meshesCounter, buildingCounter);
  return cm;
}