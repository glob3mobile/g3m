package org.glob3.mobile.generated;import java.util.*;

public class GenericQuadTree_SectorElement extends GenericQuadTree_Element
{
  public final Sector _sector = new Sector();

  public GenericQuadTree_SectorElement(Sector sector, Object element)
  {
	  super(element);
	  _sector = new Sector(sector);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isSectorElement() const
  public final boolean isSectorElement()
  {
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getCenter() const
  public final Geodetic2D getCenter()
  {
	return _sector.getCenter();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector getSector() const
  public final Sector getSector()
  {
	return _sector;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif
  }
}
