package org.glob3.mobile.generated; 
public class QuadTree_Element
{
  public final Sector _sector ;
  public final QuadTree_Content _element;

  public QuadTree_Element(Sector sector, QuadTree_Content element)
  {
     _sector = new Sector(sector);
     _element = element;
  }

  public void dispose()
  {
    if (_element != null)
       _element.dispose();
  }

}