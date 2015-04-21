package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////


public class GenericQuadTree_Node
{
  private final int _depth;
  private Sector _sector;

  private java.util.ArrayList<GenericQuadTree_Element> _elements = new java.util.ArrayList<GenericQuadTree_Element>();

  private GenericQuadTree_Node[] _children;

  private GenericQuadTree_Node(Sector sector, GenericQuadTree_Node parent)
  {
     _sector = new Sector(sector);
     _depth = parent._depth + 1;
     _children = null;
  }

  private void splitNode(int maxElementsPerNode, int maxDepth, double childAreaProportion)
  {
    _children = new GenericQuadTree_Node[4];
  
    final Geodetic2D lower = _sector._lower;
    final Geodetic2D upper = _sector._upper;
  
    final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
    final Angle splitLatitude = Angle.midAngle(lower._latitude, upper._latitude);
  
    final Sector sector0 = new Sector(lower, new Geodetic2D(splitLatitude, splitLongitude));
  
    final Sector sector1 = new Sector(new Geodetic2D(lower._latitude, splitLongitude), new Geodetic2D(splitLatitude, upper._longitude));
  
    final Sector sector2 = new Sector(new Geodetic2D(splitLatitude, lower._longitude), new Geodetic2D(upper._latitude, splitLongitude));
  
    final Sector sector3 = new Sector(new Geodetic2D(splitLatitude, splitLongitude), upper);
  
    _children[0] = new GenericQuadTree_Node(sector0, this);
    _children[1] = new GenericQuadTree_Node(sector1, this);
    _children[2] = new GenericQuadTree_Node(sector2, this);
    _children[3] = new GenericQuadTree_Node(sector3, this);
  
    //Split elements by sector
    java.util.ArrayList<GenericQuadTree_Element> elementsToBeInserted = _elements;
    final int size = elementsToBeInserted.size();
    for (int i = 0; i < size; i++)
    {
      this.add(elementsToBeInserted.get(i), maxElementsPerNode, maxDepth, childAreaProportion);
    }
    _elements.clear();
  }

  private GenericQuadTree_Node getBestNodeForInsertion(GenericQuadTree_Element element, double childAreaProportion)
  {
  
  
    Sector sector = element.getSector();
    //  double myArea = _sector->getAngularAreaInSquaredDegrees();
    double myArea = getAreaInSquaredDegreesAfterInsertion(sector);
  
    if (sector.getAngularAreaInSquaredDegrees() > (myArea * childAreaProportion))
    {
      return this;
    }
  
    double minChildInsertionCost = _children[0].getInsertionCostInSquaredDegrees(sector);
    GenericQuadTree_Node bestChildForInsertion = _children[0];
    for (int i = 1; i < 4; i++)
    {
      GenericQuadTree_Node child = _children[i];
      double cost = child.getInsertionCostInSquaredDegrees(sector);
      if (cost < minChildInsertionCost)
      {
        minChildInsertionCost = cost;
        bestChildForInsertion = child;
      }
      else
      {
        if (cost == minChildInsertionCost)
        {
          //        printf("BOTH CHILDREN WITH SAME COST");
  
          int n1 = bestChildForInsertion.getSubtreeNElements();
          int n2 = child.getSubtreeNElements();
  
          if (n2 < n1) //SAME COST BUT LESS CONFLICTS
          {
            minChildInsertionCost = cost;
            bestChildForInsertion = child;
          }
        }
  
      }
    }
  
    if (minChildInsertionCost == 0)
    {
      return bestChildForInsertion;
    }
  
    if (minChildInsertionCost > (myArea / 2)) //We store in parent
    {
      return this;
    }
  
    return bestChildForInsertion;
  
  }

  private void increaseNodeSector(GenericQuadTree_Element element)
  {
    Sector s = _sector;
    if (_sector != null)
       _sector.dispose();
    _sector = new Sector(s.mergedWith(element.getSector()));
  }

  public GenericQuadTree_Node(Sector sector)
  {
     _sector = new Sector(sector);
     _depth = 1;
     _children = null;
  }

  public void dispose()
  {
    final int elementsSize = _elements.size();
    for (int i = 0; i < elementsSize; i++)
    {
      if (_elements.get(i) != null)
         _elements.get(i).dispose();
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        if (_children[i] != null)
           _children[i].dispose();
      }
  
      _children = null;
    }
  
    if (_sector != null)
       _sector.dispose();
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public final boolean add(GenericQuadTree_Element element, int maxElementsPerNode, int maxDepth, double childAreaProportion)
  {
  
    if (_children == null) //LEAF NODE
    {
  
      if (_elements.size() < maxElementsPerNode || _depth == maxDepth) //Tipical insertion
      {
        _elements.add(element);
        increaseNodeSector(element);
        return true;
      }
  
      //Node must create children
      splitNode(maxElementsPerNode, maxDepth, childAreaProportion); //We must split
      return this.add(element, maxElementsPerNode, maxDepth, childAreaProportion); //We try it again, this time as inner node
    }
  
    //INNER NODE
  
    //Calculate best node for insertion
    GenericQuadTree_Node bestInsertionNode = getBestNodeForInsertion(element, childAreaProportion);
  
    if (bestInsertionNode == this)
    {
      _elements.add(element);
      increaseNodeSector(element);
      return true;
    }
  
    //Inserting into child
    increaseNodeSector(element);
    return bestInsertionNode.add(element, maxElementsPerNode, maxDepth, childAreaProportion);
  }

  public final boolean acceptVisitor(Sector sector, GenericQuadTreeVisitor visitor)
  {
  
  
  
    visitor.addComparisonsDoneWhileVisiting(1);
  
    if (!_sector.touchesWith(sector))
    {
      return false;
    }
  
    final int elementsSize = _elements.size();
    visitor.addComparisonsDoneWhileVisiting(elementsSize);
  
    for (int i = 0; i < elementsSize; i++)
    {
      GenericQuadTree_Element element = _elements.get(i);
  
      if (element.isSectorElement()) //Element is associated to sector
      {
        GenericQuadTree_SectorElement e = (GenericQuadTree_SectorElement) element;
        if (e._sector.touchesWith(sector))
        {
          final boolean abort = visitor.visitElement(e._sector, element._element);
          if (abort)
          {
            return true;
          }
        }
      }
      else //Element is associated to geodetic
      {
        GenericQuadTree_Geodetic2DElement e = (GenericQuadTree_Geodetic2DElement) element;
        if (sector.contains(e._geodetic))
        {
          final boolean abort = visitor.visitElement(e._geodetic, element._element);
          if (abort)
          {
            return true;
          }
        }
  
      }
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        GenericQuadTree_Node child = _children[i];
        final boolean abort = child.acceptVisitor(sector, visitor);
        if (abort)
        {
          return true;
        }
      }
    }
  
    return false;
  }

  public final boolean acceptVisitor(Geodetic2D geo, GenericQuadTreeVisitor visitor)
  {
  
  
    visitor.addComparisonsDoneWhileVisiting(1);
  
    if (!_sector.contains(geo))
    {
      return false;
    }
  
    final int elementsSize = _elements.size();
    visitor.addComparisonsDoneWhileVisiting(elementsSize);
  
    for (int i = 0; i < elementsSize; i++)
    {
      GenericQuadTree_Element element = _elements.get(i);
  
      if (element.isSectorElement()) //Element is associated to sector
      {
        GenericQuadTree_SectorElement e = (GenericQuadTree_SectorElement) element;
        if (e._sector.contains(geo))
        {
          final boolean abort = visitor.visitElement(e._sector, element._element);
          if (abort)
          {
            return true;
          }
        }
      }
      else //Element is associated to geodetic
      {
        GenericQuadTree_Geodetic2DElement e = (GenericQuadTree_Geodetic2DElement) element;
        if (geo.isEquals(e._geodetic))
        {
          final boolean abort = visitor.visitElement(e._geodetic, element._element);
          if (abort)
          {
            return true;
          }
        }
  
      }
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        GenericQuadTree_Node child = _children[i];
        final boolean abort = child.acceptVisitor(geo, visitor);
        if (abort)
        {
          return true;
        }
      }
    }
  
    return false;
  }

  public final boolean acceptNodeVisitor(GenericQuadTreeNodeVisitor visitor)
  {
    if (visitor.visitNode(this))
    {
      return true;
    }
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        GenericQuadTree_Node child = _children[i];
        if (child.acceptNodeVisitor(visitor))
        {
          return true;
        }
      }
    }
    return false;
  }

  public final double getInsertionCostInSquaredDegrees(Sector sector)
  {
  
    Sector newSector = _sector.mergedWith(sector);
    double areaAfter = newSector.getAngularAreaInSquaredDegrees();
  
    double areaNow = _sector.getAngularAreaInSquaredDegrees();
    return areaAfter - areaNow;
  }
  public final double getAreaInSquaredDegreesAfterInsertion(Sector sector)
  {
    Sector newSector = _sector.mergedWith(sector);
    return newSector.getAngularAreaInSquaredDegrees();
  }

  public final int getDepth()
  {
    return _depth;
  }

  public final int getNElements()
  {
    return _elements.size();
  }

  public final boolean isLeaf()
  {
    return _children == null;
  }

  public final int getSubtreeNElements()
  {
    int n = _elements.size();
    if (_children != null)
    {
      for (int i = 0; i<4;i++)
      {
        n += _children[i].getSubtreeNElements();
      }
    }
    return n;
  }

  public final void symbolize(GEOVectorLayer geoVectorLayer)
  {
  
    if (_elements.size() > 0)
    {
      java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D>();
  
      coordinates.add(new Geodetic2D(_sector.getSW()));
      coordinates.add(new Geodetic2D(_sector.getNW()));
      coordinates.add(new Geodetic2D(_sector.getNE()));
      coordinates.add(new Geodetic2D(_sector.getSE()));
      coordinates.add(new Geodetic2D(_sector.getSW()));
  
      //    printf("RESTERIZING: %s\n", _sector->description().c_str());
  
      float[] dashLengths = {};
      int dashCount = 0;
  
      Color c = Color.red().wheelStep(12, _depth);
  
      GEO2DLineRasterStyle ls = new GEO2DLineRasterStyle(c, (float)1.0, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0); //const int dashPhase) : - const int dashCount, - float dashLengths[], - const float miterLimit, - const StrokeJoin join, -  const StrokeCap cap, - const float width, - const Color& color,
  
  
      final GEO2DCoordinatesData coordinatesData = new GEO2DCoordinatesData(coordinates);
      GEOLineRasterSymbol symbol = new GEOLineRasterSymbol(coordinatesData, ls);
      coordinatesData._release();
  
      geoVectorLayer.addSymbol(symbol);
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        _children[i].symbolize(geoVectorLayer);
      }
    }
  
  
  }

  public final void getGeodetics(java.util.ArrayList<Geodetic2D> geo)
  {
    for (int i = 0; i < _elements.size(); i++)
    {
      if (!_elements.get(i).isSectorElement())
      {
        geo.add(new Geodetic2D(_elements.get(i).getCenter()));
      }
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        _children[i].getGeodetics(geo);
      }
    }
  
  }
  public final void getSectors(java.util.ArrayList<Sector> sectors)
  {
    for (int i = 0; i < _elements.size(); i++)
    {
      if (_elements.get(i).isSectorElement())
      {
        sectors.add(new Sector(_elements.get(i).getSector()));
      }
    }
  
    if (_children != null)
    {
      for (int i = 0; i < 4; i++)
      {
        _children[i].getSectors(sectors);
      }
    }
  
  }

  public final boolean remove(Object element)
  {
    boolean wasRemoved = false;
  
    final java.util.Iterator<GenericQuadTree_Element> iterator = _elements.iterator();
    while (iterator.hasNext()) {
      final GenericQuadTree_Element item = iterator.next();
      if (item._element == element) {
        iterator.remove();
        wasRemoved = true;
        break;
      }
    }
  
    if (wasRemoved)
    {
      return true;
    }
  
    if (_children != null)
    {
  
      for (int i = 0; i < 4; i++)
      {
        if (_children[i].remove(element))
        {
          //The item was removed from one of my children
  
          //Removing all children if none has an item
          int nChild = 0;
          for (int j = 0; j < 4; j++)
          {
            nChild += _children[j].getSubtreeNElements();
          }
  
          if (nChild == 0)
          {
            for (int j = 0; j < 4; j++)
            {
              if (_children[j] != null)
                 _children[j].dispose();
            }
            _children = null;
            _children = null;
          }
  
          return true;
        }
      }
    }
  
    return false;
  }
}