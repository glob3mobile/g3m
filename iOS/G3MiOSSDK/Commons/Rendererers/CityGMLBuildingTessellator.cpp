//
//  CityGMLBuildingTessellator.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 13/4/16.
//
//

#include "CityGMLBuildingTessellator.hpp"

short CityGMLBuildingTessellator::addTrianglesCuttingEarsForAllWalls(const CityGMLBuilding* building,
                                                FloatBufferBuilderFromCartesian3D& fbb,
                                                FloatBufferBuilderFromCartesian3D& normals,
                                                ShortBufferBuilder& indexes,
                                                FloatBufferBuilderFromColor& colors,
                                                const double baseHeight,
                                                const Planet& planet,
                                                const short firstIndex,
                                                const Color& color,
                                                const bool includeGround,
                                                const ElevationData* elevationData) {
  const std::vector<CityGMLBuildingSurface*> surfaces = building->getSurfaces();
  
  short buildingFirstIndex = firstIndex;
  for (int w = 0; w < surfaces.size(); w++) {
#ifdef C_CODE
    CityGMLBuildingSurface* s = surfaces[w];
#endif
#ifdef JAVA_CODE
    CityGMLBuildingSurface s = surfaces.get(w);
#endif
    
    if ((!includeGround && s->getType() == GROUND) ||
        !s->isVisible())
    {
      continue;
    }
    
    buildingFirstIndex = s->addTrianglesByEarClipping(fbb, normals, indexes, colors,
                                                      baseHeight, planet,
                                                      buildingFirstIndex, color, elevationData);
  }
  return buildingFirstIndex;
}


Mesh* CityGMLBuildingTessellator::createIndexedMeshWithColorPerVertex(const CityGMLBuilding* building,
                                                 const Planet planet,
                                                 const bool fixOnGround,
                                                 const Color color,
                                                 const bool includeGround,
                                                 const ElevationData* elevationData) {
  
  
  const double baseHeight = fixOnGround ? building->getBaseHeight() : 0;
  
  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  ShortBufferBuilder indexes;
  FloatBufferBuilderFromColor colors;
  
  const short firstIndex = 0;
  addTrianglesCuttingEarsForAllWalls(building,
                                     *fbb,
                                     *normals,
                                     indexes,
                                     colors,
                                     baseHeight,
                                     planet,
                                     firstIndex,
                                     color,
                                     includeGround,
                                     elevationData);
  
  IndexedMesh* im = new IndexedMesh(GLPrimitive::triangles(),
                                    fbb->getCenter(), fbb->create(), true,
                                    indexes.create(),true,
                                    1.0f, 1.0f, NULL,
                                    colors.create(), 1.0f, true, normals->create());
  
  delete fbb;
  delete normals;
  
  return im;
}

Mesh* CityGMLBuildingTessellator::createMesh(const std::vector<CityGMLBuilding*> buildings,
                        const Planet& planet,
                        const bool fixOnGround,
                        const bool includeGround,
                        CityGMLBuildingColorProvider* colorProvider,
                        const ElevationData* elevationData) {
  
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
    
    buildingVertexIndex.push_back((short)vertices->size() / 3);
    processedBuildings.push_back(b);
    
    const double baseHeight = fixOnGround ? b->getBaseHeight() : 0;
    
    if (colorProvider == NULL){
      firstIndex = addTrianglesCuttingEarsForAllWalls(b, *vertices, *normals, *indexes, *colors, baseHeight, planet, firstIndex,
                                                      colorWheel.wheelStep((int)buildings.size(), buildingCounter),
                                                      includeGround,
                                                      elevationData);
      
    } else{
      firstIndex = addTrianglesCuttingEarsForAllWalls(b, *vertices, *normals, *indexes, *colors, baseHeight, planet, firstIndex,
                                                      colorProvider->getColor(b),
                                                      includeGround,
                                                      elevationData);
    }
    
    buildingVertexIndex.push_back((short)vertices->size() / 3);
    
    buildingCounter++;
    
    if (firstIndex > 30000) { //Max number of vertex per mesh (CHECK SHORT RANGE)
      if (cm == NULL) {
        cm = new CompositeMesh();
      }
      
      //Adding new mesh
      IndexedMesh* im = new IndexedMesh(GLPrimitive::triangles(),
                                        vertices->getCenter(),
                                        vertices->create(),
                                        true,
                                        indexes->create(),
                                        true,
                                        1.0f,
                                        1.0f,
                                        NULL,
                                        colors->create(),
                                        1.0f,
                                        true,
                                        normals->create());
      
      cm->addMesh(im);
      meshesCounter++;
      
      //Linking buildings with its mesh
      for (int j = 0; j < processedBuildings.size(); j++) {
        DefaultCityGMLBuildingTessellatorData* data = new DefaultCityGMLBuildingTessellatorData();
        data->_containerMesh = im;
        data->_firstVertexIndexWithinContainerMesh = buildingVertexIndex[j*2];
        data->_lastVertexIndexWithinContainerMesh = buildingVertexIndex[j*2+1];
        
        processedBuildings[j]->setTessellatorData(data);
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
    DefaultCityGMLBuildingTessellatorData* data = new DefaultCityGMLBuildingTessellatorData();
    data->_containerMesh = im;
    data->_firstVertexIndexWithinContainerMesh = buildingVertexIndex[j*2];
    data->_lastVertexIndexWithinContainerMesh = buildingVertexIndex[j*2+1];
    
    processedBuildings[j]->setTessellatorData(data);
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

void CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(const CityGMLBuilding* building, const Color& color){
  
  DefaultCityGMLBuildingTessellatorData* data = (DefaultCityGMLBuildingTessellatorData*) building->getTessllatorData();
  AbstractMesh* mesh = (AbstractMesh*)data->_containerMesh;
  
  if (mesh != NULL){
    //TODO
    IFloatBuffer* colors = mesh->getColorsFloatBuffer();
    
    const int initPos = data->_firstVertexIndexWithinContainerMesh * 4;
    const int finalPos = data->_lastVertexIndexWithinContainerMesh * 4;
    
    for (int i = initPos; i < finalPos;) {
      colors->put(i++, color._red);
      colors->put(i++, color._green);
      colors->put(i++, color._blue);
      colors->put(i++, color._alpha);
    }
  }
}


