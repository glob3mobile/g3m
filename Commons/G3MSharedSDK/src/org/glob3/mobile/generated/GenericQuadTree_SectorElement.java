package org.glob3.mobile.generated; 
public class GenericQuadTree_SectorElement extends GenericQuadTree_Element
{
  public final Sector _sector ;

  public GenericQuadTree_SectorElement(Sector sector, Object element)
  {
     super(element);
     _sector = new Sector(sector);
  }

  public final boolean isSectorElement()
  {
    return true;
  }

  public final Geodetic2D getCenter()
  {
    return _sector.getCenter();
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public void dispose()
  {
  super.dispose();
  }
}