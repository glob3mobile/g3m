package org.glob3.mobile.generated;
public class MeasureVertex
{
  public final Geodetic3D _geodetic ;
  public final Vector3D _cartesian ;

  public MeasureVertex(Geodetic3D geodetic, Planet planet)
  {
     _geodetic = new Geodetic3D(geodetic);
     _cartesian = new Vector3D(planet.toCartesian(geodetic));

  }

  public void dispose()
  {

  }

}