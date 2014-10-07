package org.glob3.mobile.generated; 
public class QuadTree_Node
{
  private java.util.ArrayList<QuadTree_Element> _elements = new java.util.ArrayList<QuadTree_Element>();

  private QuadTree_Node[] _children;

  private QuadTree_Node(Sector sector, QuadTree_Node parent)
  {
     _sector = new Sector(sector);
     _depth = parent._depth + 1;
     _children = null;
  }

  public final int _depth;
  public final Sector _sector ;

  public QuadTree_Node(Sector sector)
  {
     _sector = new Sector(sector);
     _depth = 1;
     _children = null;
  }

  public void dispose()
  {
    final int elementsSize = _elements.size();
    for (int i = 0; i < elementsSize; i++)
    {
      if (_elements.get(i) != null)
         _elements.get(i).dispose();
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        if (_children[i] != null)
           _children[i].dispose();
      }
  
      _children = null;
    }
  }

  public final boolean add(Sector sector, QuadTree_Content content, int maxElementsPerNode, int maxDepth)
  {
    if (!_sector.fullContains(sector))
    {
      return false;
    }
    if (!_sector.touchesWith(sector))
    {
      return false;
    }
  
    if ((_elements.size() < maxElementsPerNode) || (_depth >= maxDepth))
    {
      _elements.add(new QuadTree_Element(sector, content));
      return true;
    }
  
    if (_children == null)
    {
      _children = new QuadTree_Node[4];
  
      final Geodetic2D lower = _sector._lower;
      final Geodetic2D upper = _sector._upper;
  
      final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
      final Angle splitLatitude = Angle.midAngle(lower._latitude, upper._latitude);
  
      final Sector sector0 = new Sector(lower, new Geodetic2D(splitLatitude, splitLongitude));
  
      final Sector sector1 = new Sector(new Geodetic2D(lower._latitude, splitLongitude), new Geodetic2D(splitLatitude, upper._longitude));
  
      final Sector sector2 = new Sector(new Geodetic2D(splitLatitude, lower._longitude), new Geodetic2D(upper._latitude, splitLongitude));
  
      final Sector sector3 = new Sector(new Geodetic2D(splitLatitude, splitLongitude), upper);
  
      _children[0] = new QuadTree_Node(sector0, this);
      _children[1] = new QuadTree_Node(sector1, this);
      _children[2] = new QuadTree_Node(sector2, this);
      _children[3] = new QuadTree_Node(sector3, this);
    }
  
    int selectedChildrenIndex = -1;
    boolean keepHere = false;
    for (int i = 0; i < 4; i++)
    {
      QuadTree_Node child = _children[i];
      if (child._sector.touchesWith(sector))
      {
        if (selectedChildrenIndex == -1)
        {
          selectedChildrenIndex = i;
        }
        else
        {
          keepHere = true;
          break;
        }
      }
    }
  
    if (keepHere)
    {
      _elements.add(new QuadTree_Element(sector, content));
      return true;
    }
  
    if (selectedChildrenIndex >= 0)
    {
      return _children[selectedChildrenIndex].add(sector, content, maxElementsPerNode, maxDepth);
    }
  
    ILogger.instance().logError("Logic error in QuadTree");
    return false;
  }

  public final boolean acceptVisitor(Sector sector, QuadTreeVisitor visitor)
  {
    if (!_sector.touchesWith(sector))
    {
      return false;
    }
  
    final int elementsSize = _elements.size();
    for (int i = 0; i < elementsSize; i++)
    {
      QuadTree_Element element = _elements.get(i);
      if (element._sector.touchesWith(sector))
      {
        final boolean abort = visitor.visitElement(element._sector, element._content);
        if (abort)
        {
          return true;
        }
      }
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        QuadTree_Node child = _children[i];
        final boolean abort = child.acceptVisitor(sector, visitor);
        if (abort)
        {
          return true;
        }
      }
    }
  
    return false;
  }

  public final boolean isEmpty()
  {
    if (!_elements.isEmpty())
    {
      return false;
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        QuadTree_Node child = _children[i];
        if (!child.isEmpty())
        {
          return false;
        }
      }
    }
  
    return true;
  }

}