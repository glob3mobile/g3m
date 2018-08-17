package org.glob3.mobile.generated;import java.util.*;

public class GenericQuadTree_Geodetic2DElement extends GenericQuadTree_Element
{
  public final Geodetic2D _geodetic = new Geodetic2D();

  public GenericQuadTree_Geodetic2DElement(Geodetic2D geodetic, Object element)
  {
	  super(element);
	  _geodetic = new Geodetic2D(geodetic);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isSectorElement() const
  public final boolean isSectorElement()
  {
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getCenter() const
  public final Geodetic2D getCenter()
  {
	return _geodetic;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector getSector() const
  public final Sector getSector()
  {
	return new Sector(_geodetic, _geodetic);
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif
  }
}
