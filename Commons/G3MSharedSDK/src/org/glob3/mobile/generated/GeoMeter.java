package org.glob3.mobile.generated; 
//
//  GeoMeter.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/12/13.
//
//

//
//  GeoMeter.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/12/13.
//
//




//class Planet;
//class Geodetic2D;
//class Sector;

public class GeoMeter
{

  private static final Planet _planet = Planet.createSphericalEarth();


  public static double getDistance(Geodetic2D g1, Geodetic2D g2)
  {
  
    final Vector3D g1n = _planet.centricSurfaceNormal(_planet.toCartesian(g1));
    final Vector3D g2n = _planet.centricSurfaceNormal(_planet.toCartesian(g2));
  
    final Angle angle = g1n.angleBetween(g2n);
  
    final double dist = angle._radians * _planet.getRadii()._x;
  
    return dist;
  }

  public static double getArea(java.util.ArrayList<Geodetic2D> polygon)
  {
  
    final int size = polygon.size();
    if (size < 3)
    {
      return 0;
    }
  
    //Computing center (polygon must be convex)
    double minLat = 90;
    double maxLat = -90;
    double minLon = 180;
    double maxLon = -180;
    for (int i = 0; i < size; i++)
    {
      final double lat = polygon.get(i)._latitude._degrees;
      final double lon = polygon.get(i)._longitude._degrees;
  
      if (lat < minLat)
      {
        minLat = lat;
      }
      if (lat > maxLat)
      {
        maxLat = lat;
      }
      if (lon < minLon)
      {
        minLon = lon;
      }
      if (lon > maxLon)
      {
        maxLon = lon;
      }
  
    }
  
    final Geodetic2D center = Geodetic2D.fromDegrees((minLat + maxLat) / 2, (minLon + maxLon)/2);
  
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
  
    Geodetic2D previousVertex = polygon.get(0);
  
    final Vector3D previousVertexNormal = new Vector3D(previousVertex._longitude._degrees - center._longitude._degrees, previousVertex._latitude._degrees - center._latitude._degrees, 0);
  
    double previousVertexDistToCenter = getDistance(*previousVertex, center);
    IMathUtils mu = IMathUtils.instance();
    for (int i = 1; i < size; i++)
    {
  
      final Geodetic2D vertex = polygon.get(i);
  
      final double distToPreviousVertex = getDistance(vertex, *previousVertex);
      final double vertexDistToCenter = getDistance(vertex, center);
  
      final Vector3D vertexNormal = new Vector3D(vertex._longitude._degrees - center._longitude._degrees, vertex._latitude._degrees - center._latitude._degrees, 0);
  
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
      if (a < b)
      {
        double aux = a;
        a = b;
        b = aux;
      }
  
      if (b < c)
      {
        double aux = b;
        b = c;
        c = aux;
      }
  
      if (a < b)
      {
        double aux = a;
        a = b;
        b = aux;
      }
  
  
      final double T = mu.sqrt((a + (b+c)) * (c - (a-b)) * (c + (a-b)) * (a + (b-c))) / 4.0;
  
      if ((T != T))
      {
        ILogger.instance().logError("NaN sub-area.");
      }
      else
      {
  
        final Vector3D crossedVector = vertexNormal.cross(previousVertexNormal);
        final boolean outerFace = crossedVector._z >= 0;
        if (outerFace)
        {
          accumulatedArea += T;
        }
        else
        {
          accumulatedArea -= T;
        }
  
      }
  
      //Storing last vertex
      previousVertexDistToCenter = vertexDistToCenter;
      previousVertex = vertex;
  
      if (previousVertexNormal != null)
         previousVertexNormal.dispose();
      previousVertexNormal = vertexNormal;
    }
  
    if (previousVertexNormal != null)
       previousVertexNormal.dispose();
  
    return accumulatedArea;
  
  }

  public static double getArea(Sector sector)
  {
    java.util.ArrayList<Geodetic2D> polygon = new java.util.ArrayList<Geodetic2D>();
    polygon.add(new Geodetic2D(sector.getNE()));
    polygon.add(new Geodetic2D(sector.getSE()));
    polygon.add(new Geodetic2D(sector.getSW()));
    polygon.add(new Geodetic2D(sector.getNW()));
    double a = getArea(polygon);
  
    final int size = polygon.size();
    for (int i = 0; i < size; i++)
    {
      if (polygon.get(i) != null)
         polygon.get(i).dispose();
    }
  
    return a;
  }

}