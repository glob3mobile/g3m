package org.glob3.mobile.generated;import java.util.*;

////////////////////////////////////////////////////////////////////////////

public class GenericQuadTree_TESTER
{

  private static class GenericQuadTreeVisitorSector_TESTER extends GenericQuadTreeVisitor
  {

	public Sector _sec = new Sector();

	public GenericQuadTreeVisitorSector_TESTER(Sector s)
	{
		_sec = new Sector(s);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean visitElement(const Sector& sector, const Object* element) const
	public final boolean visitElement(Sector sector, Object element)
	{

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (_sec.isEquals(sector))
	  if (_sec.isEquals(new Sector(sector)))
	  {
		//        std::string* s = (std::string*)element;
		//        printf("ELEMENT -> %s\n", s->c_str());
		return true;
	  }
	  return false;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean visitElement(const Geodetic2D& geodetic, const Object* element) const
	public final boolean visitElement(Geodetic2D geodetic, Object element)
	{
	  return false;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void endVisit(boolean aborted) const
	public final void endVisit(boolean aborted)
	{
	  if (!aborted)
	  {
		ILogger.instance().logInfo("COULDN'T FIND ELEMENT\n");
	  }
	  else
	  {
		//        printf("ELEMENT FOUND WITH %d COMPARISONS\n", getNComparisonsDone() );
		GenericQuadTree_TESTER._nComparisons += getNComparisonsDone();
		GenericQuadTree_TESTER._nElements += 1;
	  }

	}

  }

  private static class GenericQuadTreeVisitorGeodetic_TESTER extends GenericQuadTreeVisitor
  {
	public Geodetic2D _geo = new Geodetic2D();

	public GenericQuadTreeVisitorGeodetic_TESTER(Geodetic2D g)
	{
		_geo = new Geodetic2D(g);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean visitElement(const Sector& sector, const Object* element) const
	public final boolean visitElement(Sector sector, Object element)
	{
	  return false;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean visitElement(const Geodetic2D& geodetic, const Object* element) const
	public final boolean visitElement(Geodetic2D geodetic, Object element)
	{

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (geodetic.isEquals(_geo))
	  if (geodetic.isEquals(new Geodetic2D(_geo)))
	  {
		//        std::string* s = (std::string*)element;
		//        printf("ELEMENT -> %s\n", s->c_str());
		return true;
	  }
	  return false;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void endVisit(boolean aborted) const
	public final void endVisit(boolean aborted)
	{
	  if (!aborted)
	  {
		ILogger.instance().logInfo("COULDN'T FIND ELEMENT\n");
	  }
	  else
	  {
		//        printf("ELEMENT FOUND WITH %d COMPARISONS\n", getNComparisonsDone() );
		GenericQuadTree_TESTER._nComparisons += getNComparisonsDone();
		GenericQuadTree_TESTER._nElements += 1;
	  }

	}

  }

  private static class NodeVisitor_TESTER extends GenericQuadTreeNodeVisitor
  {
	public int _maxDepth;
	public int _meanDepth;

	public int _maxNEle;
	public int _meanElemDepth;
	public int _nNodes;
	public int _nElem;

	public int _leafMinDepth;
	public int _leafMeanDepth;
	public int _nLeaf;

	public NodeVisitor_TESTER()
	{
		_maxDepth = 0;
		_meanDepth = 0;
		_maxNEle = 0;
		_nNodes = 0;
		_meanElemDepth = 0;
		_nElem = 0;
		_leafMinDepth = 999999;
		_leafMeanDepth = 0;
		_nLeaf = 0;
	}


	public final boolean visitNode(GenericQuadTree_Node node)
	{
	  //      printf("NODE D: %d, NE: %d\n", node->getDepth(), node->getNElements());

	  int depth = node.getDepth();

	  if (node.getNElements() > _maxNEle)
	  {
		_maxNEle = node.getNElements();
	  }

	  if (_maxDepth < depth)
	  {
		_maxDepth = depth;
	  }

	  _meanDepth += depth;

	  _nNodes++;

	  _nElem += node.getNElements();

	  _meanElemDepth += node.getNElements() * depth;

	  if (node.isLeaf())
	  {
		if (depth < _leafMinDepth)
		{
		  _leafMinDepth = depth;
		}
		_leafMeanDepth += depth;
		_nLeaf++;
	  }


	  return false;
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void endVisit(boolean aborted) const
	public final void endVisit(boolean aborted)
	{
	  ILogger.instance().logInfo("============== \nTREE WITH %d ELEM. \nMAXDEPTH: %d, MEAN NODE DEPTH: %f, MAX NELEM: %d, MEAN ELEM DEPTH: %f\nLEAF NODES %d -> MIN DEPTH: %d, MEAN DEPTH: %f\n============== \n", _nElem, _maxDepth, _meanDepth / (float)_nNodes, _maxNEle, _meanElemDepth / (float) _nElem, _nLeaf, _leafMinDepth, _leafMeanDepth / (float) _nLeaf);
	}
  }


  public static int _nComparisons = 0;
  public static int _nElements = 0;

  public static int randomInt(int max)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	int i = tangible.RandomNumbers.nextNumber();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	java.util.Random r = new java.util.Random();
	int i = r.nextInt();
//#endif

	return i % max;
  }

  public static void run(int nElements, GEOVectorLayer geoVectorLayer)
  {
  
	_nElements = 0;
	_nComparisons = 0;
  
  
	GenericQuadTree tree = new GenericQuadTree();
	java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
	java.util.ArrayList<Geodetic2D> geos = new java.util.ArrayList<Geodetic2D>();
  
	for (int i = 0; i < nElements; i++)
	{
	  double minLat = randomInt(180) -90; // rand()%180 -90;
	  double minLon = randomInt(360) - 180; //rand()%360 -180;
  
	  int type = randomInt(2);
	  if (type == 0)
	  {
  
		double maxLat = minLat + randomInt(90 - (int)minLat); //rand()%(90 - (int)minLat);
		double maxLon = minLon + randomInt(90 - (int)minLat); //rand()%(180 - (int)minLon);
  
		Sector s = Sector.fromDegrees(minLat, minLon, maxLat, maxLon);
		sectors.add(new Sector(s));
		String desc = "SECTOR ELEMENT " + s.description();
		String element = new String();
		element = desc;
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!tree.add(s, element))
		if (!tree.add(new Sector(s), element))
		{
		  ILogger.instance().logInfo("ERROR");
		}
  
	  }
	  else
	  {
		Geodetic2D geo = Geodetic2D.fromDegrees(minLat, minLon);
		geos.add(new Geodetic2D(geo));
		String desc = "GEODETIC ELEMENT " + geo.description();
		String element = new String();
		element = desc;
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!tree.add(geo, element))
		if (!tree.add(new Geodetic2D(geo), element))
		{
		  ILogger.instance().logInfo("ERROR");
		}
	  }
	}
  
	for (int i = 0; i < sectors.size(); i++)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  Sector s = *(sectors.get(i));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  Sector s = sectors.get(i);
//#endif
	  GenericQuadTreeVisitorSector_TESTER vis = new GenericQuadTreeVisitorSector_TESTER(s);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: tree.acceptVisitor(s, vis);
	  tree.acceptVisitor(new Sector(s), new GenericQuadTreeVisitorSector_TESTER(vis));
	}
  
	for (int i = 0; i < geos.size(); i++)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  Geodetic2D g = *(geos.get(i));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  Geodetic2D g = geos.get(i);
//#endif
	  GenericQuadTreeVisitorGeodetic_TESTER vis = new GenericQuadTreeVisitorGeodetic_TESTER(g);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: tree.acceptVisitor(g, vis);
	  tree.acceptVisitor(new Geodetic2D(g), new GenericQuadTreeVisitorGeodetic_TESTER(vis));
	}
  
	NodeVisitor_TESTER nodeVis = new NodeVisitor_TESTER();
	tangible.RefObject<GenericQuadTreeNodeVisitor> tempRef_nodeVis = new tangible.RefObject<GenericQuadTreeNodeVisitor>(nodeVis);
	tree.acceptNodeVisitor(tempRef_nodeVis);
	nodeVis = tempRef_nodeVis.argvalue;
  
  //  if (rasterizer != NULL) {
  //    tree.symbolize(rasterizer);
  //  }
  
	if (geoVectorLayer != null)
	{
	  tree.symbolize(geoVectorLayer);
	}
  
	double c_e = (float)_nComparisons / _nElements;
	ILogger.instance().logInfo("NElements Found = %d, Mean NComparisons = %f -> COEF: %f\n", _nElements, c_e, c_e / _nElements);
  
  }

  public static void run(tangible.RefObject<GenericQuadTree> tree, GEOVectorLayer geoVectorLayer)
  {
  
	_nElements = 0;
	_nComparisons = 0;
  
	java.util.ArrayList<Sector> sectors = tree.argvalue.getSectors();
	java.util.ArrayList<Geodetic2D> geos = tree.argvalue.getGeodetics();
  
	for (int i = 0; i < sectors.size(); i++)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  Sector s = *(sectors.get(i));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  Sector s = sectors.get(i);
//#endif
	  GenericQuadTreeVisitorSector_TESTER vis = new GenericQuadTreeVisitorSector_TESTER(s);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: tree.acceptVisitor(s, vis);
	  tree.argvalue.acceptVisitor(new Sector(s), new GenericQuadTreeVisitorSector_TESTER(vis));
	  if (sectors.get(i) != null)
		  sectors.get(i).dispose();
	}
  
	for (int i = 0; i < geos.size(); i++)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  Geodetic2D g = *(geos.get(i));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  Geodetic2D g = geos.get(i);
//#endif
	  GenericQuadTreeVisitorGeodetic_TESTER vis = new GenericQuadTreeVisitorGeodetic_TESTER(g);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: tree.acceptVisitor(g, vis);
	  tree.argvalue.acceptVisitor(new Geodetic2D(g), new GenericQuadTreeVisitorGeodetic_TESTER(vis));
  
	  if (geos.get(i) != null)
		  geos.get(i).dispose();
	}
  
	NodeVisitor_TESTER nodeVis = new NodeVisitor_TESTER();
	tangible.RefObject<GenericQuadTreeNodeVisitor> tempRef_nodeVis = new tangible.RefObject<GenericQuadTreeNodeVisitor>(nodeVis);
	tree.argvalue.acceptNodeVisitor(tempRef_nodeVis);
	nodeVis = tempRef_nodeVis.argvalue;
  
	if (geoVectorLayer != null)
	{
	  tree.argvalue.symbolize(geoVectorLayer);
	}
  
	double c_e = (float)_nComparisons / _nElements;
	ILogger.instance().logInfo("NElements Found = %d, Mean NComparisons = %f -> COEF: %f\n", _nElements, c_e, c_e / _nElements);
  
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark NODE

///////////////////////////////
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark TREE

///////////////////////////////////////////////////////////////////////

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark TESTER
