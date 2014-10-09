package org.glob3.mobile.generated; 
public class GenericQuadTree_Geodetic2DElement extends GenericQuadTree_Element
{
  public final Geodetic2D _geodetic ;

  public GenericQuadTree_Geodetic2DElement(Geodetic2D geodetic, Object element)
  {
     super(element);
     _geodetic = new Geodetic2D(geodetic);
  }

  public final boolean isSectorElement()
  {
    return false;
  }

  public final Geodetic2D getCenter()
  {
    return _geodetic;
  }

  public final Sector getSector()
  {
    return new Sector(_geodetic, _geodetic);
  }

  public void dispose()
  {
  super.dispose();
  }
}