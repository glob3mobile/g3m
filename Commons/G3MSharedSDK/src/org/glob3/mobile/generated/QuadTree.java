package org.glob3.mobile.generated; 
public class QuadTree
{
  private final Sector _sector ;
///#ifdef C_CODE
  private QuadTree_Node _root;
///#endif
///#ifdef JAVA_CODE
//  private QuadTree_Node _root;
///#endif

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
  
  }

  public final boolean add(Sector sector, Object element)
  {
    return _root.add(sector, element, _maxElementsPerNode, _maxDepth);
  }

  public final void visitElements(Sector sector, QuadTreeVisitor visitor)
  {
    _root.visitElements(sector, visitor);
  }

}