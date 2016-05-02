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
    
    if (s->isVisible())
    {
      buildingFirstIndex = s->addTrianglesByEarClipping(fbb, normals, indexes, colors,
                                                        baseHeight, planet,
                                                        buildingFirstIndex, color, elevationData);
    }
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

CityGMLBuildingTessellatorData* CityGMLBuildingTessellator::createData(short firstV,
                                                                       short lastV,
                                                                       Mesh* mesh,
                                                                       const FloatBufferBuilderFromCartesian3D& vertices){
  
  //Creating sphere
  std::vector<Vector3D*> vs;
  for (short i = firstV; i < lastV; i++) {
    vs.push_back(new Vector3D(vertices.getAbsoluteVector3D(i)));
  }
  
  Sphere bSphere = Sphere::createSphereContainingPoints(vs);
  
  for (size_t i = 0; i < vs.size(); i++) {
    delete vs[i];
  }
  
  return new DefaultCityGMLBuildingTessellatorData(mesh,
                                                   firstV,
                                                   lastV,
                                                   new Sphere(bSphere));
  
}

Mesh* CityGMLBuildingTessellator::createMesh(const std::vector<CityGMLBuilding*> buildings,
                                             const Planet& planet,
                                             const bool fixOnGround,
                                             const bool checkSurfacesVisibility,
                                             CityGMLBuildingColorProvider* colorProvider,
                                             const ElevationData* elevationData) {
  
  CompositeMesh* cm = NULL;
  int buildingCounter = 0;
  int meshesCounter = 0;
  
  if (checkSurfacesVisibility){
    int n = CityGMLBuilding::checkWallsVisibility(buildings);
    ILogger::instance()->logInfo("Removed %d invisible walls from the model.", n);
  }
  
  
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
    
    int firstV = ((int)vertices->size()) / 3;
    buildingVertexIndex.push_back((short)firstV);
    
    processedBuildings.push_back(b);
    
    const double baseHeight = fixOnGround ? b->getBaseHeight() : 0;
    
    if (colorProvider == NULL){
      firstIndex = addTrianglesCuttingEarsForAllWalls(b, *vertices, *normals, *indexes, *colors, baseHeight, planet, firstIndex,
                                                      colorWheel.wheelStep((int)buildings.size(), buildingCounter),
                                                      elevationData);
      
    } else{
      firstIndex = addTrianglesCuttingEarsForAllWalls(b, *vertices, *normals, *indexes, *colors, baseHeight, planet, firstIndex,
                                                      colorProvider->getColor(b),
                                                      elevationData);
    }
    
    int lastV = ((int)vertices->size()) / 3;
    buildingVertexIndex.push_back((short)lastV);
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
        
        processedBuildings[j]->setTessellatorData(createData(buildingVertexIndex[j*2],
                                                             buildingVertexIndex[j*2+1],
                                                             im,
                                                             *vertices));
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
    
    processedBuildings[j]->setTessellatorData(createData(buildingVertexIndex[j*2],
                                                         buildingVertexIndex[j*2+1],
                                                         im,
                                                         *vertices));
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
  
  if (data == NULL){
    THROW_EXCEPTION("NO TESSELLATOR DATA FOR BUILDING");
  }
  
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

Mark* CityGMLBuildingTessellator::createMark(const CityGMLBuilding* building, const bool fixOnGround) {
  const double deltaH = fixOnGround ? building->getBaseHeight() : 0;
  
  const Geodetic3D center = building->getCenter();
  const Geodetic3D pos = Geodetic3D::fromDegrees(center._latitude._degrees, center._longitude._degrees, center._height
                                                 - deltaH);
  
  Mark* m = new Mark(building->_name, pos, RELATIVE_TO_GROUND, 100.0);
  return m;
}


const Sphere* CityGMLBuildingTessellator::getSphereOfBuilding(const CityGMLBuilding* b){
  DefaultCityGMLBuildingTessellatorData* data = (DefaultCityGMLBuildingTessellatorData*) b->getTessllatorData();
  if (data == NULL){
    return NULL;
  }
  return data->_bSphere;
}

bool CityGMLBuildingTessellator::areClose(const CityGMLBuilding* b1, const CityGMLBuilding* b2){
  
  const double threshold = 0.005;
  
  double dLat = b1->_surfaces[0]->_geodeticCoordinates[0]->_latitude._degrees -
                b2->_surfaces[0]->_geodeticCoordinates[0]->_latitude._degrees;
  dLat = IMathUtils::instance()->abs(dLat);
  
  if (dLat > threshold){
    return false;
  }
  
  double dLon = b1->_surfaces[0]->_geodeticCoordinates[0]->_longitude._degrees -
                b2->_surfaces[0]->_geodeticCoordinates[0]->_longitude._degrees;
  dLon = IMathUtils::instance()->abs(dLon);
  
  if (dLon > threshold){
    return false;
  }
  
  return true;
  
  /*
   
   //This implementation does not work prior to the mesh generation
  const Sphere* s1 = getSphereOfBuilding(b1);
  const Sphere* s2 = getSphereOfBuilding(b2);
  
  if (s1 == NULL || s2 == NULL){
    return true;
  }
  
  return s1->touchesSphere(s2);
   */
}
