package org.glob3.mobile.generated; 
public class GenericQuadTree
{
  private GenericQuadTree_Node _root;

  private final int _maxElementsPerNode;
  private final int _maxDepth;
  private final double _childAreaProportion;

  private boolean add(GenericQuadTree_Element element)
  {
  
    if (_root == null)
    {
      _root = new GenericQuadTree_Node(element.getSector());
    }
  
    return _root.add(element, _maxElementsPerNode, _maxDepth, _childAreaProportion);
  }

  public GenericQuadTree()
  {
     _root = null;
     _maxElementsPerNode = 1;
     _maxDepth = 12;
     _childAreaProportion = 0.3;
  }

  public GenericQuadTree(int maxElementsPerNode, int maxDepth, double childAreaProportion)
  {
     _root = null;
     _maxElementsPerNode = maxElementsPerNode;
     _maxDepth = maxDepth;
     _childAreaProportion = childAreaProportion;
  }

  public void dispose()
  {
    if (_root != null)
       _root.dispose();
  }

  public final boolean remove(Object element)
  {
    if (_root != null)
    {
      return _root.remove(element);
    }
    return false;
  }

  public final boolean add(Sector sector, Object element)
  {
    GenericQuadTree_SectorElement sectorElement = new GenericQuadTree_SectorElement(sector, element);
    return add(sectorElement);
  }

  public final boolean add(Geodetic2D geodetic, Object element)
  {
    GenericQuadTree_Geodetic2DElement geodeticElement = new GenericQuadTree_Geodetic2DElement(geodetic, element);
    return add(geodeticElement);
  }

  public final boolean acceptVisitor(Sector sector, GenericQuadTreeVisitor visitor)
  {
    boolean aborted = false;
    if (_root != null)
    {
      aborted = _root.acceptVisitor(sector, visitor);
    }
    visitor.endVisit(aborted);
    return aborted;
  }

  public final boolean acceptVisitor(Geodetic2D geo, GenericQuadTreeVisitor visitor)
  {
    if (_root != null)
    {
      final boolean aborted = _root.acceptVisitor(geo, visitor);
      visitor.endVisit(aborted);
      return aborted;
    }
    return false;
  }

  public final boolean acceptNodeVisitor(GenericQuadTreeNodeVisitor visitor)
  {
    if (_root != null)
    {
      final boolean aborted = _root.acceptNodeVisitor(visitor);
      visitor.endVisit(aborted);
      return aborted;
    }
    return false;
  }

  public final void symbolize(GEOVectorLayer geoVectorLayer)
  {
    if (_root != null)
    {
      _root.symbolize(geoVectorLayer);
    }
  }

  public final java.util.ArrayList<Geodetic2D> getGeodetics()
  {
    java.util.ArrayList<Geodetic2D> geo = new java.util.ArrayList<Geodetic2D>();
    _root.getGeodetics(geo);
    return geo;
  }
  public final java.util.ArrayList<Sector> getSectors()
  {
    java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
    _root.getSectors(sectors);
    return sectors;
  }

}