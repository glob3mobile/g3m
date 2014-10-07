package org.glob3.mobile.generated; 
public class QuadTree
{
  private QuadTree_Node _root;

  private final int _maxElementsPerNode;
  private final int _maxDepth;


  public QuadTree(Sector sector)
  {
     _root = new QuadTree_Node(sector);
     _maxElementsPerNode = 1;
     _maxDepth = 12;
  }

  public QuadTree()
  {
     _root = new QuadTree_Node(Sector.fullSphere());
     _maxElementsPerNode = 1;
     _maxDepth = 12;
  }

  public void dispose()
  {
    if (_root != null)
       _root.dispose();
  }

  public final boolean add(Sector sector, QuadTree_Content content)
  {
    return _root.add(sector, content, _maxElementsPerNode, _maxDepth);
  }

  public final boolean acceptVisitor(Sector sector, QuadTreeVisitor visitor)
  {
    final boolean aborted = _root.acceptVisitor(sector, visitor);
    visitor.endVisit(aborted);
    return aborted;
  }

  public final void clear()
  {
    Sector sector = _root._sector;
  
    if (_root != null)
       _root.dispose();
    _root = new QuadTree_Node(sector);
  }

  public final boolean isEmpty()
  {
    return _root.isEmpty();
  }

  public final Sector getSector()
  {
    return _root._sector;
  }

}