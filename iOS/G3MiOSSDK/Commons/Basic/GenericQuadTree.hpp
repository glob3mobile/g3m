//
//  GenericQuadTree.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/08/13.
//
//

#ifndef __G3MiOSSDK__GenericQuadTree__
#define __G3MiOSSDK__GenericQuadTree__

#include <iostream>

#include "Sector.hpp"
#include "Geodetic2D.hpp"
#include "GEOTileRasterizer.hpp"

class GenericQuadTree_Node;

class GenericQuadTreeNodeVisitor {
public:
  virtual ~GenericQuadTreeNodeVisitor() {
  }

  virtual bool visitNode(const GenericQuadTree_Node* node) = 0;
  virtual void endVisit(bool aborted) const = 0;
};

class GenericQuadTreeVisitor {

  mutable int _comparisonsDone;

public:
  GenericQuadTreeVisitor(): _comparisonsDone(0) {}
  virtual ~GenericQuadTreeVisitor() {
  }

  void addComparisonsDoneWhileVisiting(int n) const { _comparisonsDone += n;}
  int getNComparisonsDone() const { return _comparisonsDone;}

  virtual bool visitElement(const Sector& sector,
                            const void*   element) const = 0;
  virtual bool visitElement(const Geodetic2D& geodetic,
                            const void*   element) const = 0;

  virtual void endVisit(bool aborted) const = 0;

};

///////////////////////////////////////////////////////////////////////////////////////
class GenericQuadTree_Element {
public:
  const void*  _element;

  GenericQuadTree_Element(const void*   element) :
  _element(element)
  {
  }

  virtual bool isSectorElement() const = 0;
  virtual Geodetic2D getCenter() const = 0;
  virtual Sector getSector() const = 0;

  virtual ~GenericQuadTree_Element() {
  }
};

class GenericQuadTree_SectorElement: public GenericQuadTree_Element {
public:
  const Sector _sector;

  GenericQuadTree_SectorElement(const Sector& sector,
                                const void*   element) :
  GenericQuadTree_Element(element),
  _sector(sector) { }
  bool isSectorElement() const { return true;}
  Geodetic2D getCenter() const { return _sector.getCenter();}
  Sector getSector() const { return _sector;}

  ~GenericQuadTree_SectorElement() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
};

class GenericQuadTree_Geodetic2DElement: public GenericQuadTree_Element {
public:
  const Geodetic2D _geodetic;

  GenericQuadTree_Geodetic2DElement(const Geodetic2D& geodetic,
                                    const void*   element) :
  GenericQuadTree_Element(element),
  _geodetic(geodetic) {}
  bool isSectorElement() const { return false;}
  Geodetic2D getCenter() const { return _geodetic;}
  Sector getSector() const { return Sector(_geodetic, _geodetic);}

  ~GenericQuadTree_Geodetic2DElement() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
};
///////////////////////////////////////////////////////////////////////////////////////


class GenericQuadTree_Node {
private:
  const int     _depth;
  Sector*  _sector;

  //  Sector* _elementsSector;

  std::vector<GenericQuadTree_Element*> _elements;

  GenericQuadTree_Node** _children;

  GenericQuadTree_Node(const Sector& sector,
                       GenericQuadTree_Node* parent) :
  _sector(new Sector(sector)),
  _depth( parent->_depth + 1 ),
  _children(NULL)//,
  //  _elementsSector(new Sector(sector))
  {
  }

  void splitNode(int maxElementsPerNode,
                 int maxDepth,
                 double childAreaProportion);

  //  void computeElementsSector();

  GenericQuadTree_Node* getBestNodeForInsertion(GenericQuadTree_Element* element,
                                                double childAreaProportion);

  void increaseNodeSector(GenericQuadTree_Element* element);

public:
  GenericQuadTree_Node(const Sector& sector) :
  _sector(new Sector(sector)),
  _depth(1),
  _children(NULL)//,
  //  _elementsSector(new Sector(sector))
  {
  }

  ~GenericQuadTree_Node();

  Sector getSector() const{ return *_sector;}
  //  Sector getElementsSector() const { return *_elementsSector;}

  bool add(GenericQuadTree_Element* element,
           int maxElementsPerNode,
           int maxDepth,
           double childAreaProportion);

  bool acceptVisitor(const Sector& sector,
                     const GenericQuadTreeVisitor& visitor) const;

  bool acceptVisitor(const Geodetic2D& geo,
                     const GenericQuadTreeVisitor& visitor) const;

  bool acceptNodeVisitor(GenericQuadTreeNodeVisitor& visitor) const;

  double getInsertionCostInSquaredDegrees(const Sector& sector) const;
  double getAreaInSquaredDegreesAfterInsertion(const Sector& sector) const;

  int getDepth() const { return _depth;}
  int getNElements() const { return _elements.size();}
  bool isLeaf() const { return _children == NULL;}

  int getSubtreeNElements() const{
    int n = _elements.size();
    if (_children != NULL) {
      for (int i = 0; i<4;i++) {
        n += _children[i]->getSubtreeNElements();
      }
    }
    return n;
  }

  void symbolize(GEOTileRasterizer* geoTileRasterizer) const;

  void getGeodetics(std::vector<Geodetic2D*>& geo) const;
  void getSectors(std::vector<Sector*>& sectors) const;

  bool remove(const void* element);
};

class GenericQuadTree {
private:
  GenericQuadTree_Node* _root;

  const int _maxElementsPerNode;
  const int _maxDepth;
  const double _childAreaProportion;

  bool add(GenericQuadTree_Element* element);
public:

  GenericQuadTree() :
  _root( NULL ),
  _maxElementsPerNode(1),
  _maxDepth(12),
  _childAreaProportion(0.3)
  {
  }

  GenericQuadTree(int maxElementsPerNode, int maxDepth, double childAreaProportion) :
  _root( NULL ),
  _maxElementsPerNode(maxElementsPerNode),
  _maxDepth(maxDepth),
  _childAreaProportion(childAreaProportion)
  {
  }

  ~GenericQuadTree();

  bool remove(const void* element);

  bool add(const Sector& sector, const void* element);

  bool add(const Geodetic2D& geodetic, const void* element);

  bool acceptVisitor(const Sector& sector,
                     const GenericQuadTreeVisitor& visitor) const;

  bool acceptVisitor(const Geodetic2D& geo,
                     const GenericQuadTreeVisitor& visitor) const;

  bool acceptNodeVisitor(GenericQuadTreeNodeVisitor& visitor) const;

  void symbolize(GEOTileRasterizer* geoTileRasterizer) const;

  std::vector<Geodetic2D*> getGeodetics() const{
    std::vector<Geodetic2D*> geo;
    _root->getGeodetics(geo);
    return geo;
  }
  std::vector<Sector*> getSectors() const{
    std::vector<Sector*> sectors;
    _root->getSectors(sectors);
    return sectors;
  }

};

////////////////////////////////////////////////////////////////////////////

class GenericQuadTree_TESTER {

  class GenericQuadTreeVisitorSector_TESTER: public GenericQuadTreeVisitor {
  public:

    Sector _sec;

    GenericQuadTreeVisitorSector_TESTER(const Sector& s):_sec(s) {}

    bool visitElement(const Sector& sector,
                      const void*   element) const{

      if (_sec.isEquals(sector)) {
        //        std::string* s = (std::string*)element;
        //        printf("ELEMENT -> %s\n", s->c_str());
        return true;
      }
      return false;
    }

    bool visitElement(const Geodetic2D& geodetic,
                      const void*   element) const{
      return false;
    }

    void endVisit(bool aborted) const{
      if (!aborted) {
        ILogger::instance()->logInfo("COULDN'T FIND ELEMENT\n");
      } else{
        //        printf("ELEMENT FOUND WITH %d COMPARISONS\n", getNComparisonsDone() );
        GenericQuadTree_TESTER::_nComparisons += getNComparisonsDone();
        GenericQuadTree_TESTER::_nElements += 1;
      }

    }

  };

  class GenericQuadTreeVisitorGeodetic_TESTER: public GenericQuadTreeVisitor {
  public:
    Geodetic2D _geo;

    GenericQuadTreeVisitorGeodetic_TESTER(const Geodetic2D g):_geo(g) {}

    bool visitElement(const Sector& sector,
                      const void*   element) const{return false;}

    bool visitElement(const Geodetic2D& geodetic,
                      const void*   element) const{

      if (geodetic.isEquals(_geo)) {
        //        std::string* s = (std::string*)element;
        //        printf("ELEMENT -> %s\n", s->c_str());
        return true;
      }
      return false;
    }

    void endVisit(bool aborted) const{
      if (!aborted) {
        ILogger::instance()->logInfo("COULDN'T FIND ELEMENT\n");
      } else{
        //        printf("ELEMENT FOUND WITH %d COMPARISONS\n", getNComparisonsDone() );
        GenericQuadTree_TESTER::_nComparisons += getNComparisonsDone();
        GenericQuadTree_TESTER::_nElements += 1;
      }

    }

  };

  class NodeVisitor_TESTER: public GenericQuadTreeNodeVisitor{
  public:
    int _maxDepth;
    int _meanDepth;

    int _maxNEle;
    int _meanElemDepth;
    int _nNodes;
    int _nElem;

    int _leafMinDepth;
    int _leafMeanDepth;
    int _nLeaf;

    NodeVisitor_TESTER():
    _maxDepth(0), _meanDepth(0), _maxNEle(0), _nNodes(0), _meanElemDepth(0), _nElem(0),
    _leafMinDepth(999999), _leafMeanDepth(0), _nLeaf(0) {}


    bool visitNode(const GenericQuadTree_Node* node) {
      //      printf("NODE D: %d, NE: %d\n", node->getDepth(), node->getNElements());

      int depth = node->getDepth();

      if (node->getNElements() > _maxNEle) {
        _maxNEle = node->getNElements();
      }

      if (_maxDepth < depth) {
        _maxDepth = depth;
      }

      _meanDepth += depth;

      _nNodes++;

      _nElem += node->getNElements();

      _meanElemDepth += node->getNElements() * depth;

      if (node->isLeaf()) {
        if (depth < _leafMinDepth) {
          _leafMinDepth = depth;
        }
        _leafMeanDepth += depth;
        _nLeaf++;
      }


      return false;
    }
    void endVisit(bool aborted) const{
      ILogger::instance()->logInfo("============== \nTREE WITH %d ELEM. \nMAXDEPTH: %d, MEAN NODE DEPTH: %f, MAX NELEM: %d, MEAN ELEM DEPTH: %f\nLEAF NODES %d -> MIN DEPTH: %d, MEAN DEPTH: %f\n============== \n",
                                   _nElem,
                                   _maxDepth,
                                   _meanDepth / (float)_nNodes,
                                   _maxNEle,
                                   _meanElemDepth / (float) _nElem,
                                   _nLeaf,
                                   _leafMinDepth,
                                   _leafMeanDepth / (float) _nLeaf
                                   );
    }
  };

public:
  
  static int _nComparisons;
  static int _nElements;
  
  static int randomInt(int max) {
#ifdef C_CODE
    int i = rand();
#endif
#ifdef JAVA_CODE
    java.util.Random r = new java.util.Random();
    int i = r.nextInt();
#endif
    
    return i % max;
  }
  
  static void run(int nElements, GEOTileRasterizer* rasterizer);
  
  static void run(GenericQuadTree& tree, GEOTileRasterizer* rasterizer);
  
};
#endif /* defined(__G3MiOSSDK__GenericGenericQuadTree__) */
