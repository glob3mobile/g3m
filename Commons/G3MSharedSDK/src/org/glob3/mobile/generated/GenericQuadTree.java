package org.glob3.mobile.generated; 
public class GenericQuadTree
{
  private GenericQuadTree_Node _root;

  private final int _maxElementsPerNode;
  private final int _maxDepth;

  private boolean add(GenericQuadTree_Element element)
  {
  
    if (_root == null)
    {
      _root = new GenericQuadTree_Node(element.getSector());
    }
  
    return _root.add(element, _maxElementsPerNode, _maxDepth);
  }

  public GenericQuadTree()
  {
     _root = null;
     _maxElementsPerNode = 1;
     _maxDepth = 12;
  }

  public void dispose()
  {
    if (_root != null)
       _root.dispose();
  
    super.dispose();
  
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
    final boolean aborted = _root.acceptVisitor(sector, visitor);
    visitor.endVisit(aborted);
    return aborted;
  }

  public final boolean acceptVisitor(Geodetic2D geo, GenericQuadTreeVisitor visitor)
  {
  
    final boolean aborted = _root.acceptVisitor(geo, visitor);
    visitor.endVisit(aborted);
    return aborted;
  
  }

  public final boolean acceptNodeVisitor(GenericQuadTreeNodeVisitor visitor)
  {
    final boolean aborted = _root.acceptNodeVisitor(visitor);
    visitor.endVisit(aborted);
    return aborted;
  }

  public final void symbolize(GEOTileRasterizer geoTileRasterizer)
  {
    _root.symbolize(geoTileRasterizer);
  }

}