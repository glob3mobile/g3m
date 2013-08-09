package org.glob3.mobile.generated; 
import java.util.*;

public class GenericQuadTree_Geodetic2DElement extends GenericQuadTree_Element
{
  public final Geodetic2D _geodetic ;

  public GenericQuadTree_Geodetic2DElement(Geodetic2D geodetic, Object element)
  {
     _geodetic = new Geodetic2D(geodetic);
     super(element);
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
  }
}