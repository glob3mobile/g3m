package org.glob3.mobile.generated; 
public class QuadTree
{
  private final Sector _sector ;
  private QuadTree_Node _root;

  private final int _maxElementsPerNode;
  private final int _maxDepth;


  public QuadTree(Sector sector)
  {
     _sector = new Sector(sector);
     _root = new QuadTree_Node(sector);
     _maxElementsPerNode = 16;
     _maxDepth = 12;
  }

  public QuadTree()
  {
     _sector = new Sector(Sector.fullSphere());
     _root = new QuadTree_Node(Sector.fullSphere());
     _maxElementsPerNode = 16;
     _maxDepth = 12;
  }

  public void dispose()
  {
    if (_root != null)
       _root.dispose();
  }

  public final boolean add(Sector sector, Object element)
  {
    return _root.add(sector, element, _maxElementsPerNode, _maxDepth);
  }

  public final boolean acceptVisitor(Sector sector, QuadTreeVisitor visitor)
  {
    final boolean aborted = _root.acceptVisitor(sector, visitor);
    visitor.endVisit(aborted);
    return aborted;
  }

}