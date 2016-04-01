//
//  CityGMLBuilding.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#ifndef CityGMLBuilding_hpp
#define CityGMLBuilding_hpp

#include "CityGMLBuildingSurface.hpp"
#include "IndexedMesh.hpp"
#include "CompositeMesh.hpp"
#include <string>
#include <vector>


class CityGMLBuilding {
  
public:
  
  const std::string                _name;
  const int                        _roofTypeCode;
  
#ifdef C_CODE
  const std::vector<CityGMLBuildingSurface*> _surfaces;
#endif
#ifdef JAVA_CODE
  public final java.util.ArrayList<CityGMLBuildingSurface> _surfaces;
#endif
  
  CityGMLBuilding(const std::string name,
                  int roofType,
                  std::vector<CityGMLBuildingSurface*> walls):
  _name(name),
  _roofTypeCode(roofType),
  _surfaces(walls)
  {
  }
  
  ~CityGMLBuilding()
  {
    for (int i = 0; i < _surfaces.size(); i++) {
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[i];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(i);
#endif
      delete s;
    }
  }
  
  
  double getBaseHeight() {
    double min = IMathUtils::instance()->maxDouble();
    for (int i = 0; i < _surfaces.size(); i++) {
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[i];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(i);
#endif
      const double h = s->getBaseHeight();
      if (min > h) {
        min = h;
      }
    }
    return min;
  }
  
  
  std::string description() {
    
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("Building Name: " + _name + "\nRoof Type: ");
    isb->addInt(_roofTypeCode);
    for (int i = 0; i < _surfaces.size(); i++) {
      isb->addString("\n Wall: Coordinates: ");
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[i];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(i);
#endif
      for (int j = 0; j < s->_geodeticCoordinates.size(); j += 3) {
#ifdef C_CODE
        Geodetic3D* g = s->_geodeticCoordinates.at(j);
#endif
#ifdef JAVA_CODE
        Geodetic3D g = s->_geodeticCoordinates.get(j);
#endif
        isb->addString(g->description());
      }
    }
    std::string s = isb->getString();
    delete isb;
    return s;
  }
  
  short addTrianglesCuttingEarsForAllWalls(FloatBufferBuilderFromCartesian3D& fbb,
                                           FloatBufferBuilderFromCartesian3D& normals,
                                           ShortBufferBuilder& indexes,
                                           FloatBufferBuilderFromColor& colors,
                                           const double baseHeight,
                                           const Planet& planet,
                                           const short firstIndex,
                                           const Color& color,
                                           const bool includeGround) const {
    short buildingFirstIndex = firstIndex;
    for (int w = 0; w < _surfaces.size(); w++) {
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[w];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(w);
#endif
      
      if ((!includeGround && s->getType() == GROUND) ||
          !s->isVisible())
      {
        continue;
      }
      
      buildingFirstIndex = s->addTrianglesByEarClipping(fbb, normals, indexes, colors,
                                                        baseHeight, planet,
                                                        buildingFirstIndex, color);
    }
    return buildingFirstIndex;
  }
  
  
  Mesh* createIndexedMeshWithColorPerVertex(const Planet planet,
                                            const bool fixOnGround,
                                            const Color color,
                                            const bool includeGround) {
    
    
    const double baseHeight = fixOnGround ? getBaseHeight() : 0;
    
    FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
    FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    ShortBufferBuilder indexes;
    FloatBufferBuilderFromColor colors;
    
    const short firstIndex = 0;
    addTrianglesCuttingEarsForAllWalls(*fbb, *normals, indexes, colors, baseHeight, planet, firstIndex, color, includeGround);
    
    IndexedMesh* im = new IndexedMesh(GLPrimitive::triangles(),
                                      fbb->getCenter(), fbb->create(), true,
                                      indexes.create(),true,
                                      1.0f, 1.0f, NULL,
                                      colors.create(), 1.0f, true, normals->create());
    
    delete fbb;
    delete normals;
    
    return im;
  }
  
  
  static Mesh* createSingleIndexedMeshWithColorPerVertexForBuildings(const std::vector<CityGMLBuilding*> buildings,
                                                                     const Planet& planet,
                                                                     const bool fixOnGround,
                                                                     const bool includeGround) {
    
    CompositeMesh* cm = NULL;
    int buildingCounter = 0;
    int meshesCounter = 0;
    
    
    FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
    FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    ShortBufferBuilder* indexes = new ShortBufferBuilder();
    FloatBufferBuilderFromColor* colors = new FloatBufferBuilderFromColor();
    
    const Color colorWheel = Color::red();
    
    short firstIndex = 0;
    for (int i = 0; i < buildings.size(); i++) {
      CityGMLBuilding* b = buildings[i];
      
      const double baseHeight = fixOnGround ? b->getBaseHeight() : 0;
      firstIndex = b->addTrianglesCuttingEarsForAllWalls(*fbb, *normals, *indexes, *colors, baseHeight, planet, firstIndex,
                                                         colorWheel.wheelStep((int)buildings.size(), buildingCounter),
                                                         includeGround);
      
      buildingCounter++;
      
      if (firstIndex > 30000) { //Max number of vertex per mesh (CHECK SHORT RANGE)
        if (cm == NULL) {
          cm = new CompositeMesh();
        }
        
        //Adding new mesh
        IndexedMesh* im = new IndexedMesh(GLPrimitive::triangles(), fbb->getCenter(), fbb->create(), true, indexes->create(),
                                          true, 1.0f, 1.0f, NULL, colors->create(), 1.0f, true, normals->create());
        
        cm->addMesh(im);
        meshesCounter++;
        
        //Reset
        
        delete fbb;
        delete normals;
#ifdef C_CODE
        delete indexes;
#endif
        delete colors;
        fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
        normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
        indexes = new ShortBufferBuilder();
        colors = new FloatBufferBuilderFromColor();
        firstIndex = 0;
      }
    }
    
    //Adding last mesh
    IndexedMesh* im = new IndexedMesh(GLPrimitive::triangles(), fbb->getCenter(), fbb->create(), true, indexes->create(),
                                      true, 1.0f, 1.0f, NULL, colors->create(), 1.0f, true, normals->create());
    
    delete fbb;
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
  
  Geodetic3D getMin() {
    double minLat = IMathUtils::instance()->maxDouble();
    double minLon =  IMathUtils::instance()->maxDouble();
    double minH =  IMathUtils::instance()->maxDouble();
    
    for (int i = 0; i < _surfaces.size(); i++) {
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[i];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(i);
#endif
      const Geodetic3D min = s->getMin();
      if (min._longitude._degrees < minLon) {
        minLon = min._longitude._degrees;
      }
      if (min._latitude._degrees < minLat) {
        minLat = min._latitude._degrees;
      }
      if (min._height < minH) {
        minH = min._height;
      }
    }
    return Geodetic3D::fromDegrees(minLat, minLon, minH);
  }
  
  
  Geodetic3D getMax() {
    double maxLat = IMathUtils::instance()->minDouble();
    double maxLon = IMathUtils::instance()->minDouble();
    double maxH = IMathUtils::instance()->minDouble();
    
    for (int i = 0; i < _surfaces.size(); i++) {
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[i];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(i);
#endif
      const Geodetic3D min = s->getMax();
      if (min._longitude._degrees > maxLon) {
        maxLon = min._longitude._degrees;
      }
      if (min._latitude._degrees > maxLat) {
        maxLat = min._latitude._degrees;
      }
      if (min._height > maxH) {
        maxH = min._height;
      }
    }
    return Geodetic3D::fromDegrees(maxLat, maxLon, maxH);
  }
  
  
  Geodetic3D getCenter() {
    const Geodetic3D min = getMin();
    const Geodetic3D max = getMax();
    
    return Geodetic3D::fromDegrees((min._latitude._degrees + max._latitude._degrees) / 2,
                                   (min._longitude._degrees + max._longitude._degrees) / 2, (min._height + max._height) / 2);
  }
  
  
  Mark* createMark(const bool fixOnGround) {
    const double deltaH = fixOnGround ? getBaseHeight() : 0;
    
    const Geodetic3D center = getCenter();
    const Geodetic3D pos = Geodetic3D::fromDegrees(center._latitude._degrees, center._longitude._degrees, center._height
                                                   - deltaH);
    
    Mark* m = new Mark(_name, pos, ABSOLUTE, 100.0);
    return m;
  }
  
  
  void addMarkersToCorners(MarksRenderer* mr,
                           const bool fixOnGround) {
    
    const double deltaH = fixOnGround ? getBaseHeight() : 0;
    
    for (int i = 0; i < _surfaces.size(); i++) {
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[i];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(i);
#endif
      s->addMarkersToCorners(mr, deltaH);
    }
  }
  
  static int checkWallsVisibility(CityGMLBuilding* b1, CityGMLBuilding* b2){
    int nInvisibleWalls = 0;
    for (int i = 0; i < b1->_surfaces.size(); i++) {
      CityGMLBuildingSurface* s1 = b1->_surfaces[i];
      if (s1->getType() == WALL){
        for (int j = 0; j < b2->_surfaces.size(); j++) {
          CityGMLBuildingSurface* s2 = b2->_surfaces[j];
          if (s2->getType() == WALL){
            if (s1->isEquivalentTo(*s2)){
              s1->setIsVisible(false);
              s2->setIsVisible(false);
              nInvisibleWalls++;
              //ILogger::instance()->logInfo("Two building surfaces are equivalent, so we mark them as invisible.");
            }
          }
        }
      }
    }
    return nInvisibleWalls;
  }
  
//  static int checkWallsVisibilityBetweenConsecutiveBuildings(const std::vector<CityGMLBuilding*> buildings){
//    int nInvisibleWalls = 0;
//    for (int i = 0; i < buildings.size() - 1; i++){
//      CityGMLBuilding* b1 = buildings[i];
//      CityGMLBuilding* b2 = buildings[i+1];
//      nInvisibleWalls += checkWallsVisibility(b1, b2);
//    }
//    return nInvisibleWalls;
//  }
  
  static int checkWallsVisibility(const std::vector<CityGMLBuilding*> buildings){
    int nInvisibleWalls = 0;
    for (int i = 0; i < buildings.size() - 1; i++){
      CityGMLBuilding* b1 = buildings[i];
      
      for (int j = i+1; j < i+30 && j < buildings.size() - 1; j++){
        CityGMLBuilding* b2 = buildings[j];
        nInvisibleWalls += checkWallsVisibility(b1, b2);
      }
      
    }
    return nInvisibleWalls;
  }
};

#endif /* CityGMLBuilding_hpp */
