package org.glob3.mobile.generated; 
public class Sector_Geodetic2DCachedData
{
  private final Vector3D _cartesian ;
  private final Vector3D _geodeticSurfaceNormal ;


  public Sector_Geodetic2DCachedData(Planet planet, Geodetic2D position)
  {
     _cartesian = new Vector3D(planet.toCartesian(position));
     _geodeticSurfaceNormal = new Vector3D(planet.geodeticSurfaceNormal(_cartesian));

  }

  public final boolean test(Vector3D eye)
  {
    return _geodeticSurfaceNormal.dot(eye.sub(_cartesian)) > 0;
  }
}
//Vector2D Sector::getTranslationFactor(const Sector& that) const {
//  const Vector2D uv = that.getUVCoordinates(_lower);
//  const double scaleY = _deltaLatitude.div(that._deltaLatitude);
//  return Vector2D(uv._x, uv._y - scaleY);
//}
