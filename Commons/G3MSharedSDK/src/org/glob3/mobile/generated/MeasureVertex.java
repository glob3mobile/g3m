package org.glob3.mobile.generated;
public class MeasureVertex
{
  public final Geodetic3D _geodetic ;
  public final float _verticalExaggeration;
  public final double _deltaHeight;

  public final Vector3D _cartesian ;

  public MeasureVertex(Geodetic3D geodetic, float verticalExaggeration, double deltaHeight, Planet planet)
  {
     _geodetic = new Geodetic3D(geodetic);
     _verticalExaggeration = verticalExaggeration;
     _deltaHeight = deltaHeight;
     _cartesian = new Vector3D(planet.toCartesian(geodetic));

  }

  public void dispose()
  {

  }

}