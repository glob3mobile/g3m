package org.glob3.mobile.generated;import java.util.*;

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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return _root->add(sector, content, _maxElementsPerNode, _maxDepth);
	return _root.add(new Sector(sector), content, _maxElementsPerNode, _maxDepth);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean acceptVisitor(const Sector& sector, const QuadTreeVisitor& visitor) const
  public final boolean acceptVisitor(Sector sector, QuadTreeVisitor visitor)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const boolean aborted = _root->acceptVisitor(sector, visitor);
	final boolean aborted = _root.acceptVisitor(new Sector(sector), new QuadTreeVisitor(visitor));
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEmpty() const
  public final boolean isEmpty()
  {
	return _root.isEmpty();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector getSector() const
  public final Sector getSector()
  {
	return _root._sector;
  }

}
