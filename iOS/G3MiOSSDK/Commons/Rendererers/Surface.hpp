//
//  BuildingSurface.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#ifndef BuildingSurface_hpp
#define BuildingSurface_hpp

#include <vector>
#include "Geodetic3D.hpp"
#include "Planet.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "ShortBufferBuilder.hpp"
#include "Polygon3D.hpp"
#include "MarksRenderer.hpp"
#include "IStringBuilder.hpp"
#include "Mark.hpp"


class Surface {
  
  mutable double _maxHeight;
  
public:
  
  
  std::vector<Geodetic3D*>          _geodeticCoordinates;
  
  Surface(const std::vector<Geodetic3D*>& geodeticCoordinates):
  _geodeticCoordinates(geodeticCoordinates),
  _maxHeight(NAND)
  {
  }
  
  
  
  ~Surface(){
#ifdef C_CODE
    for (size_t i = 0; i < _geodeticCoordinates.size(); i++) {
      delete _geodeticCoordinates[i];
    }
#endif
  }
  
  double getBaseHeight() const {
    double minHeight = 0;
    minHeight = IMathUtils::instance()->maxDouble();
    for (size_t i = 0; i < _geodeticCoordinates.size(); i++) {
      const double h = _geodeticCoordinates[i]->_height;
      if (h < minHeight) {
        minHeight = h;
      }
    }
    return minHeight;
  }
  
  
  Geodetic3D getMin() {
    double minLat = IMathUtils::instance()->maxDouble();
    double minLon = IMathUtils::instance()->maxDouble();
    double minH = IMathUtils::instance()->maxDouble();
    
    for (size_t i = 0; i < _geodeticCoordinates.size(); i++) {
      Geodetic3D* g = _geodeticCoordinates[i];
      const double lon = g->_longitude._degrees;
      if (lon < minLon) {
        minLon = lon;
      }
      const double lat = g->_latitude._degrees;
      if (lat < minLat) {
        minLat = lat;
      }
      const double h = g->_height;
      if (h < minH) {
        minH = h;
      }
    }
    return Geodetic3D::fromDegrees(minLat, minLon, minH);
  }
  
  
  Geodetic3D getMax() {
    double maxLat = IMathUtils::instance()->minDouble();
    double maxLon = IMathUtils::instance()->minDouble();
    double maxH = IMathUtils::instance()->minDouble();
    
    for (size_t i = 0; i < _geodeticCoordinates.size(); i++) {
      Geodetic3D* g = _geodeticCoordinates[i];
      const double lon = g->_longitude._degrees;
      if (lon > maxLon) {
        maxLon = lon;
      }
      const double lat = g->_latitude._degrees;
      if (lat > maxLat) {
        maxLat = lat;
      }
      const double h = g->_height;
      if (h > maxH) {
        maxH = h;
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
  
  std::vector<Vector3D*> createCartesianCoordinates(const Planet& planet,
                                                    const double baseHeight,
                                                    const ElevationData* elevationData) {
    
    std::vector<Vector3D*> coor3D;
    
    if (elevationData == NULL){
      for (size_t i = 0; i < _geodeticCoordinates.size(); i++) {
        Geodetic3D* g= _geodeticCoordinates[i];
        coor3D.push_back(new Vector3D(planet.toCartesian(*g)));
      }
    }
    else{
      for (size_t i = 0; i < _geodeticCoordinates.size(); i++) {
        Geodetic3D* g= _geodeticCoordinates[i];
        double h = elevationData->getElevationAt(g->_latitude, g->_longitude);
        coor3D.push_back(new Vector3D(planet.toCartesian(Geodetic3D::fromDegrees(g->_latitude._degrees,
                                                                                 g->_longitude._degrees,
                                                                                 h + g->_height))));
      }
    }
    return coor3D;
  }
  
  short addTrianglesByEarClipping(FloatBufferBuilderFromCartesian3D& fbb,
                                  FloatBufferBuilderFromCartesian3D& normals,
                                  ShortBufferBuilder& indexes,
                                  FloatBufferBuilderFromColor& colors,
                                  const double baseHeight,
                                  const Planet& planet,
                                  const short firstIndex,
                                  const Color& color,
                                  const ElevationData* elevationData) {
    const std::vector<Vector3D*> cartesianC = createCartesianCoordinates(planet, baseHeight, elevationData);
    const Polygon3D polygon(cartesianC);
    const short lastVertex = polygon.addTrianglesByEarClipping(fbb, normals, indexes, firstIndex);
    
    for (short j = firstIndex; j < lastVertex; j++) {
      colors.add(color);
    }
    return lastVertex;
  }
  
  
  void addMarkersToCorners(MarksRenderer* mr,
                           const double substractHeight) {
    
    for (size_t i = 0; i < _geodeticCoordinates.size(); i++) {
      IStringBuilder* isb = IStringBuilder::newStringBuilder();
      isb->addInt((int)i);
#ifdef C_CODE
      Mark* m = new Mark(isb->getString(), *_geodeticCoordinates[i], ABSOLUTE, 10000.0);
#endif
#ifdef JAVA_CODE
      Mark m = new Mark(isb.getString(), _geodeticCoordinates.get(i), AltitudeMode.ABSOLUTE, 10000.0);
#endif
      delete isb;
      mr->addMark(m);
    }
    
  }
  
  bool includesPoint(const Geodetic3D& g) const{
    int nv = (int) _geodeticCoordinates.size();
    for (int i = 0; i < nv; i++) {
      if (_geodeticCoordinates[i]->isEquals(g)){
        return true;
      }
    }
    return false;
  }
  
  bool isEquivalentTo(const Surface& that){
    if (that._geodeticCoordinates.size() != _geodeticCoordinates.size()){
      return false;
    }
    
    if (that.getMaxHeight() != getMaxHeight()){
      return false;
    }
    
    int nv = (int) _geodeticCoordinates.size();
    
    for (int i = 0; i < nv; i++) {
      Geodetic3D* g = _geodeticCoordinates[i];
      if (!that.includesPoint(*g)){
        return false;
      }
    }
    
    return true;
  }
  
  double getMaxHeight() const{
    if (ISNAN(_maxHeight)){
      _maxHeight = -9999999;
      int nv = (int) _geodeticCoordinates.size();
      for (int i = 0; i < nv; i++) {
        if (_maxHeight < _geodeticCoordinates[i]->_height){ //SIG = MAX LAT
          _maxHeight = _geodeticCoordinates[i]->_height;
        }
      }
    }
    return _maxHeight;
  }
};

#endif /* BuildingSurface_hpp */
