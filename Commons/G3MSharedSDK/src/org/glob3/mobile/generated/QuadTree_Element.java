package org.glob3.mobile.generated; 
public class QuadTree_Element
{
  public final Sector _sector ;
  public final QuadTree_Content _content;

  public QuadTree_Element(Sector sector, QuadTree_Content content)
  {
     _sector = new Sector(sector);
     _content = content;
  }

  public void dispose()
  {
    if (_content != null)
       _content.dispose();
  }

}