package org.glob3.mobile.generated; 
public class QuadTree extends Disposable
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
  
    JAVA_POST_DISPOSE
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