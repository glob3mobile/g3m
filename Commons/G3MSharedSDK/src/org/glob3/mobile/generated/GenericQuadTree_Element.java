package org.glob3.mobile.generated;import java.util.*;

///////////////////////////////////////////////////////////////////////////////////////
public abstract class GenericQuadTree_Element
{
  public final Object _element;

  public GenericQuadTree_Element(Object element)
  {
	  _element = element;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isSectorElement() const = 0;
  public abstract boolean isSectorElement();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Geodetic2D getCenter() const = 0;
  public abstract Geodetic2D getCenter();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Sector getSector() const = 0;
  public abstract Sector getSector();

  public void dispose()
  {
  }
}
