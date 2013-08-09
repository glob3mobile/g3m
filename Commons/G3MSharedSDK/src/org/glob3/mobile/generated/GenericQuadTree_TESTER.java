package org.glob3.mobile.generated; 
import java.util.*;

////////////////////////////////////////////////////////////////////////////

public class GenericQuadTree_TESTER
{

  private static class GenericQuadTreeVisitorSector_TESTER extends GenericQuadTreeVisitor
  {

    public Sector _sec ;

    public GenericQuadTreeVisitorSector_TESTER(Sector s)
    {
       _sec = new Sector(s);
    }

    public final boolean visitElement(Sector sector, Object element)
    {

      if (_sec.isEqualsTo(sector))
      {
//        std::string* s = (std::string*)element;
//        printf("ELEMENT -> %s\n", s->c_str());
        return true;
      }
      return false;
    }

    public final boolean visitElement(Geodetic2D geodetic, Object element)
    {
      return false;
    }

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
    public Geodetic2D _geo ;

    public GenericQuadTreeVisitorGeodetic_TESTER(Geodetic2D g)
    {
       _geo = new Geodetic2D(g);
    }

    public final boolean visitElement(Sector sector, Object element)
    {
       return false;
    }

    public final boolean visitElement(Geodetic2D geodetic, Object element)
    {

      if (geodetic.isEqualsTo(_geo))
      {
//        std::string* s = (std::string*)element;
//        printf("ELEMENT -> %s\n", s->c_str());
        return true;
      }
      return false;
    }

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
    public final void endVisit(boolean aborted)
    {
      ILogger.instance().logInfo("============== \nTREE WITH %d ELEM. \nMAXDEPTH: %d, MEAN NODE DEPTH: %f, MAX NELEM: %d, MEAN ELEM DEPTH: %f\nLEAF NODES %d -> MIN DEPTH: %d, MAX DEPTH %f\n============== \n", _nElem, _maxDepth, _meanDepth / (float)_nNodes, _maxNEle, _meanElemDepth / (float) _nElem, _nLeaf, _leafMinDepth, _leafMinDepth / (float) _nLeaf);
    }
  }


  public static int _nComparisons = 0;
  public static int _nElements = 0;

  public static int random(int max)
  {
    java.util.Random r = new java.util.Random();
    int i = r.nextInt();

    return i % max;
  }

  public static void run(int nElements, GEOTileRasterizer rasterizer)
  {
  
    _nElements = 0;
    _nComparisons = 0;
  
  
    GenericQuadTree tree = new GenericQuadTree();
    java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
    java.util.ArrayList<Geodetic2D> geos = new java.util.ArrayList<Geodetic2D>();
  
    for (int i = 0; i < nElements; i++)
    {
  
  
      double minLat = RandomNumbers.nextNumber(180) -90; // rand()%180 -90;
      double minLon = RandomNumbers.nextNumber(360) - 180; //rand()%360 -180;
  
      int type = RandomNumbers.nextNumber();
      if (type%2 == 0)
      {
  
        double maxLat = minLat + RandomNumbers.nextNumber(90 - (int)minLat); //rand()%(90 - (int)minLat);
        double maxLon = minLon + RandomNumbers.nextNumber(90 - (int)minLat); //rand()%(180 - (int)minLon);
  
        Sector s = Sector.fromDegrees(minLat, minLon, maxLat, maxLon);
        sectors.add(new Sector(s));
        String desc = "SECTOR ELEMENT " + s.description();
        String element = new String();
        element = desc;
  
        if (!tree.add(s, element))
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
  
        if (!tree.add(geo, element))
        {
          ILogger.instance().logInfo("ERROR");
        }
      }
    }
  
    for (int i = 0; i < sectors.size(); i++)
    {
      Sector s = *sectors.get(i);
      GenericQuadTreeVisitorSector_TESTER vis = new GenericQuadTreeVisitorSector_TESTER(s);
      tree.acceptVisitor(s, vis);
    }
  
    for (int i = 0; i < geos.size(); i++)
    {
      Geodetic2D g = *geos.get(i);
      GenericQuadTreeVisitorGeodetic_TESTER vis = new GenericQuadTreeVisitorGeodetic_TESTER(g);
      tree.acceptVisitor(g, vis);
    }
  
    NodeVisitor_TESTER nodeVis = new NodeVisitor_TESTER();
    tree.acceptNodeVisitor(nodeVis);
  
    if (rasterizer != null)
    {
      tree.symbolize(rasterizer);
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