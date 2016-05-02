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
#include <string>
#include <vector>
#include "Sphere.hpp"

class CityGMLBuildingProperty{
public:
  const std::string _name;
  CityGMLBuildingProperty(const std::string& name):_name(name){}
};

class CityGMLBuildingNumericProperty: public CityGMLBuildingProperty{
public:
  const double _value;
  CityGMLBuildingNumericProperty(const std::string& name, double value):
  CityGMLBuildingProperty(name), _value(value){}
};

class CityGMLBuildingTessellatorData{
public:
  virtual ~CityGMLBuildingTessellatorData(){}
};

class CityGMLBuilding {
  
  Mesh* _containerMesh;
  short _firstVertexIndexWithinContainerMesh;
  short _lastVertexIndexWithinContainerMesh;
  
  std::vector<CityGMLBuildingNumericProperty*> _numericProperties;
  
  CityGMLBuildingTessellatorData* _tessellatorData;
  
public:
  
  const std::string                _name;
  const int                        _roofTypeCode;
  
#ifdef C_CODE
  std::vector<CityGMLBuildingSurface*> _surfaces;
#endif
#ifdef JAVA_CODE
  public final java.util.ArrayList<CityGMLBuildingSurface> _surfaces;
#endif
  
  CityGMLBuilding(const std::string name,
                  int roofType,
                  std::vector<CityGMLBuildingSurface*> walls):
  _name(name),
  _roofTypeCode(roofType),
  _surfaces(walls),
  _tessellatorData(NULL)
  {
  }
  
  ~CityGMLBuilding()
  {
    delete _tessellatorData;
    
    removeSurfaceData();
    
    for (size_t i = 0; i < _numericProperties.size(); i++){
      delete _numericProperties[i];
    }
  }
  
  void removeSurfaceData(){
    for (size_t i = 0; i < _surfaces.size(); i++) {
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[i];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(i);
#endif
      delete s;
    }
    
    _surfaces.clear();
  }
  
  std::vector<CityGMLBuildingSurface*> getSurfaces() const{
    return _surfaces;
  }
  
  void addNumericProperty(CityGMLBuildingNumericProperty* value){
    _numericProperties.push_back(value);
  }
  
  double getNumericProperty(std::string name){
    
    for (size_t i = 0; i < _numericProperties.size(); i++){
      if (_numericProperties[i]->_name == name){
        return _numericProperties[i]->_value;
      }
    }
    return NAND;
  }
  
  std::string getPropertiesDescription() const;
  
  
  double getBaseHeight() const {
    double min = IMathUtils::instance()->maxDouble();
    for (size_t i = 0; i < _surfaces.size(); i++) {
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
    for (size_t i = 0; i < _surfaces.size(); i++) {
      isb->addString("\n Wall: Coordinates: ");
#ifdef C_CODE
      CityGMLBuildingSurface* s = _surfaces[i];
#endif
#ifdef JAVA_CODE
      CityGMLBuildingSurface s = _surfaces.get(i);
#endif
      for (size_t j = 0; j < s->_geodeticCoordinates.size(); j += 3) {
#ifdef C_CODE
        Geodetic3D* g = s->_geodeticCoordinates.at(j);
#endif
#ifdef JAVA_CODE
        Geodetic3D g = s._geodeticCoordinates.get(j);
#endif
        isb->addString(g->description());
      }
    }
    std::string s = isb->getString();
    delete isb;
    return s;
  }
  
  Geodetic3D getMin() const{
    double minLat = IMathUtils::instance()->maxDouble();
    double minLon =  IMathUtils::instance()->maxDouble();
    double minH =  IMathUtils::instance()->maxDouble();
    
    for (size_t i = 0; i < _surfaces.size(); i++) {
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
  
  
  Geodetic3D getMax() const {
    double maxLat = IMathUtils::instance()->minDouble();
    double maxLon = IMathUtils::instance()->minDouble();
    double maxH = IMathUtils::instance()->minDouble();
    
    for (size_t i = 0; i < _surfaces.size(); i++) {
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
  
  
  Geodetic3D getCenter() const {
    const Geodetic3D min = getMin();
    const Geodetic3D max = getMax();
    
    return Geodetic3D::fromDegrees((min._latitude._degrees + max._latitude._degrees) / 2,
                                   (min._longitude._degrees + max._longitude._degrees) / 2, (min._height + max._height) / 2);
  }
  
  void addMarkersToCorners(MarksRenderer* mr,
                           const bool fixOnGround) {
    
    const double deltaH = fixOnGround ? getBaseHeight() : 0;
    
    for (size_t i = 0; i < _surfaces.size(); i++) {
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
    for (size_t i = 0; i < b1->_surfaces.size(); i++) {
#ifdef C_CODE
      CityGMLBuildingSurface* s1 = b1->_surfaces[i];
#endif
#ifdef JAVA_CODE
      final CityGMLBuildingSurface s1 = b1._surfaces.get(i);
#endif
      
      //Grounds are internal by definition
      if (s1->getType() == GROUND){
        s1->setIsVisible(false);
        nInvisibleWalls++;
        continue;
      }
      
      if (s1->getType() == WALL){
        for (size_t j = 0; j < b2->_surfaces.size(); j++) {
#ifdef C_CODE
          CityGMLBuildingSurface* s2 = b2->_surfaces[j];
#endif
#ifdef JAVA_CODE
          final CityGMLBuildingSurface s2 = b2._surfaces.get(j);
#endif
          if (s2->getType() == WALL){
#ifdef C_CODE
            if (s1->isEquivalentTo(*s2)){
#endif
#ifdef JAVA_CODE
              if (s1.isEquivalentTo(s2)){
#endif
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
    
    static int checkWallsVisibility(const std::vector<CityGMLBuilding*> buildings){
      int nInvisibleWalls = 0;
      for (size_t i = 0; i < buildings.size() - 1; i++){
        CityGMLBuilding* b1 = buildings[i];
        
        for (size_t j = i+1; j < i+30 && j < buildings.size() - 1; j++){
          CityGMLBuilding* b2 = buildings[j];
          nInvisibleWalls += checkWallsVisibility(b1, b2);
        }
        
      }
      return nInvisibleWalls;
    }
    
    CityGMLBuildingTessellatorData* getTessllatorData() const{
      return _tessellatorData;
    }
    
    void setTessellatorData(CityGMLBuildingTessellatorData* data){
      delete _tessellatorData;
      _tessellatorData = data;
    }
    
    int getNumberOfVertex(){
      int n = 0;
      for (size_t i = 0; i < _surfaces.size(); i++) {
        n += (int)_surfaces[i]->_geodeticCoordinates.size();
      }
      return n;
    }
  };
  
  
  
#endif /* CityGMLBuilding_hpp */
