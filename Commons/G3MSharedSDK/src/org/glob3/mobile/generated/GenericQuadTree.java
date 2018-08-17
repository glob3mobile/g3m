package org.glob3.mobile.generated;import java.util.*;

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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean acceptVisitor(const Sector& sector, const GenericQuadTreeVisitor& visitor) const
  public final boolean acceptVisitor(Sector sector, GenericQuadTreeVisitor visitor)
  {
	boolean aborted = false;
	if (_root != null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: aborted = _root->acceptVisitor(sector, visitor);
	  aborted = _root.acceptVisitor(new Sector(sector), new GenericQuadTreeVisitor(visitor));
	}
	visitor.endVisit(aborted);
	return aborted;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean acceptVisitor(const Geodetic2D& geo, const GenericQuadTreeVisitor& visitor) const
  public final boolean acceptVisitor(Geodetic2D geo, GenericQuadTreeVisitor visitor)
  {
	if (_root != null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const boolean aborted = _root->acceptVisitor(geo, visitor);
	  final boolean aborted = _root.acceptVisitor(new Geodetic2D(geo), new GenericQuadTreeVisitor(visitor));
	  visitor.endVisit(aborted);
	  return aborted;
	}
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean acceptNodeVisitor(GenericQuadTreeNodeVisitor& visitor) const
  public final boolean acceptNodeVisitor(tangible.RefObject<GenericQuadTreeNodeVisitor> visitor)
  {
	if (_root != null)
	{
	  final boolean aborted = _root.acceptNodeVisitor(visitor);
	  visitor.argvalue.endVisit(aborted);
	  return aborted;
	}
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void symbolize(GEOVectorLayer* geoVectorLayer) const
  public final void symbolize(GEOVectorLayer geoVectorLayer)
  {
	if (_root != null)
	{
	  _root.symbolize(geoVectorLayer);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Geodetic2D*> getGeodetics() const
  public final java.util.ArrayList<Geodetic2D> getGeodetics()
  {
	java.util.ArrayList<Geodetic2D> geo = new java.util.ArrayList<Geodetic2D>();
	tangible.RefObject<java.util.ArrayList<Geodetic2D>> tempRef_geo = new tangible.RefObject<java.util.ArrayList<Geodetic2D>>(geo);
	_root.getGeodetics(tempRef_geo);
	geo = tempRef_geo.argvalue;
	return geo;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Sector*> getSectors() const
  public final java.util.ArrayList<Sector> getSectors()
  {
	java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
	tangible.RefObject<java.util.ArrayList<Sector>> tempRef_sectors = new tangible.RefObject<java.util.ArrayList<Sector>>(sectors);
	_root.getSectors(tempRef_sectors);
	sectors = tempRef_sectors.argvalue;
	return sectors;
  }

}
