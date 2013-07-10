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
     _root = sector;
     _maxElementsPerNode = 16;
     _maxDepth = 12;
  }

  public QuadTree()
  {
     _sector = new Sector(Sector.fullSphere());
     _root = Sector.fullSphere();
     _maxElementsPerNode = 16;
     _maxDepth = 12;
  }

  public void dispose()
  {
  
  }

  public final void add(Sector sector, Object element)
  {
    _root.add(sector, element, _maxElementsPerNode, _maxDepth);
  }

  public final void visitElements(Sector sector, QuadTreeVisitor visitor)
  {
    _root.visitElements(sector, visitor);
  }

}