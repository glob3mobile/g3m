//
//  GeoMeter.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/12/13.
//
//

#include "GeoMeter.hpp"

#include "Planet.hpp"
#include "Geodetic2D.hpp"
#include "Sector.hpp"
#include "ILogger.hpp"

const Planet* GeoMeter::_planet = Planet::createSphericalEarth(); //Considering world as sphere;

double GeoMeter::getDistance(const Geodetic2D& g1, const Geodetic2D& g2){

  const Vector3D g1n = _planet->centricSurfaceNormal(_planet->toCartesian(g1));
  const Vector3D g2n = _planet->centricSurfaceNormal(_planet->toCartesian(g2));

  const Angle angle = g1n.angleBetween(g2n);

  const double dist = angle._radians * _planet->getRadii()._x;

  return dist;
}

double GeoMeter::getArea(const std::vector<Geodetic2D*>& polygon){

  const int size = polygon.size();
  if (size < 3){
    return 0;
  }

  //Computing center (polygon must be convex)
  double minLat = 90;
  double maxLat = -90;
  double minLon = 180;
  double maxLon = -180;
  for (int i = 0; i < size; i++) {
    const double lat = polygon[i]->_latitude._degrees;
    const double lon = polygon[i]->_longitude._degrees;

    if (lat < minLat){
      minLat = lat;
    }
    if (lat > maxLat){
      maxLat = lat;
    }
    if (lon < minLon){
      minLon = lon;
    }
    if (lon > maxLon){
      maxLon = lon;
    }

  }

  const Geodetic2D center = Geodetic2D::fromDegrees((minLat + maxLat) / 2, (minLon + maxLon)/2);

  double accumulatedArea = 0.0;
  
  /*
#ifdef C_CODE
  const Geodetic2D* previousVertex = polygon[0];

  const Vector3D* previousVertexNormal =
  new Vector3D(previousVertex->_longitude._degrees - center._longitude._degrees,
               previousVertex->_latitude._degrees - center._latitude._degrees,
               0);
#endif
#ifdef JAVA_CODE
  Geodetic2D previousVertex = polygon.get(0);
  Vector3D previousVertexNormal = new Vector3D(previousVertex._longitude._degrees - center._longitude._degrees, previousVertex._latitude._degrees - center._latitude._degrees, 0);
#endif
   */
  
#ifdef C_CODE
  const Geodetic2D* previousVertex = polygon[0];
#else
  Geodetic2D previousVertex = polygon.get(0);
#endif
  
  const Vector3D* previousVertexNormal =
  new Vector3D(previousVertex->_longitude._degrees - center._longitude._degrees,
               previousVertex->_latitude._degrees - center._latitude._degrees,
               0);

  double previousVertexDistToCenter = getDistance(*previousVertex, center);
  IMathUtils* mu = IMathUtils::instance();
  for (int i = 1; i < size; i++) {

    const Geodetic2D* vertex = polygon[i];

    const double distToPreviousVertex = getDistance(*vertex, *previousVertex);
    const double vertexDistToCenter = getDistance(*vertex, center);

    const Vector3D* vertexNormal =
                    new Vector3D(vertex->_longitude._degrees - center._longitude._degrees,
                                 vertex->_latitude._degrees - center._latitude._degrees,
                                 0);

    //Heron's Formule [ http://en.wikipedia.org/wiki/Heron%27s_formula ]

    /*

     //FORMULE IS UNSTABLE WITH CERTAIN ANGLES
     const double& a = previousVertexDistToCenter;
     const double& b = distToPreviousVertex;
     const double& c = vertexDistToCenter;

     const double s = (a + b + c) / 2;
     const double T = mu->sqrt(s * (s-a) * (s-b) * (s-c));
     */

    double a = previousVertexDistToCenter;
    double b = distToPreviousVertex;
    double c = vertexDistToCenter;

    //ENSURING a > b > c
    if (a < b){
      double aux = a;
      a = b;
      b = aux;
    }

    if (b < c){
      double aux = b;
      b = c;
      c = aux;
    }

    if (a < b){
      double aux = a;
      a = b;
      b = aux;
    }


    const double T = mu->sqrt( (a + (b+c)) * (c - (a-b)) * (c + (a-b)) * (a + (b-c)) ) / 4.0;

    if (ISNAN(T)){
      ILogger::instance()->logError("NaN sub-area.");
    } else{

      const Vector3D crossedVector = vertexNormal->cross(*previousVertexNormal);
      const bool outerFace = crossedVector._z >= 0;
      if (outerFace){
        accumulatedArea += T;
      } else{
        accumulatedArea -= T;
      }

    }

    //Storing last vertex
    previousVertexDistToCenter = vertexDistToCenter;
    previousVertex = vertex;

    delete previousVertexNormal;
    previousVertexNormal = vertexNormal;
  }

  delete previousVertexNormal;

  return accumulatedArea;

}

double GeoMeter::getArea(const Sector& sector){
  std::vector<Geodetic2D*> polygon;
  polygon.push_back(new Geodetic2D(sector.getNE()));
  polygon.push_back(new Geodetic2D(sector.getSE()));
  polygon.push_back(new Geodetic2D(sector.getSW()));
  polygon.push_back(new Geodetic2D(sector.getNW()));
  double a = getArea(polygon);

  const int size = polygon.size();
  for (int i = 0; i < size; i++) {
    delete polygon[i];
  }
  
  return a;
}