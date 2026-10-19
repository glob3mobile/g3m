//
//  GenericQuadTree.hpp
//  G3M
//
//  Created by Jose Miguel SN on 07/08/13.
//
//

#ifndef __G3M__GenericQuadTree__
#define __G3M__GenericQuadTree__

#include "Sector.hpp"
#include "Geodetic2D.hpp"
#include <cstdlib>
#include <vector>
#include "ILogger.hpp"


class GenericQuadTree_Node;

class GenericQuadTreeNodeVisitor {
public:
  virtual ~GenericQuadTreeNodeVisitor() {
  }

  virtual bool visitNode(const GenericQuadTree_Node* node) = 0;
  virtual void endVisit(bool aborted) const = 0;
};

class GenericQuadTreeVisitor {

  mutable size_t _comparisonsDone;

public:
  GenericQuadTreeVisitor(): _comparisonsDone(0) {}
  virtual ~GenericQuadTreeVisitor() {
  }

  void addComparisonsDoneWhileVisiting(size_t n) const {
    _comparisonsDone += n;
  }

  size_t getNComparisonsDone() const {
    return _comparisonsDone;
  }

  virtual bool visitElement(const Sector& sector,
                            const void*   element) const = 0;
  virtual bool visitElement(const Geodetic2D& geodetic,
                            const void*   element) const = 0;

  virtual void endVisit(bool aborted) const = 0;

};


class GenericQuadTree_Element {
public:
  const void*  _element;

  GenericQuadTree_Element(const void* element) :
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
  _sector(sector)
  {
  }

  bool isSectorElement() const {
    return true;
  }

  Geodetic2D getCenter() const {
    return _sector.getCenter();
  }

  Sector getSector() const {
    return _sector;
  }

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
  _geodetic(geodetic) {
  }

  bool isSectorElement() const {
    return false;
  }

  Geodetic2D getCenter() const {
    return _geodetic;
  }

  Sector getSector() const {
    return Sector(_geodetic, _geodetic);
  }

  ~GenericQuadTree_Geodetic2DElement() {
#ifdef JAVA_CODE
  super.dispose();
#endif
  }
};



class GenericQuadTree_Node {
private:
  const int _depth;
  Sector*   _sector;

  std::vector<GenericQuadTree_Element*> _elements;

  GenericQuadTree_Node** _children;

  GenericQuadTree_Node(const Sector& sector,
                       GenericQuadTree_Node* parent) :
  _sector(new Sector(sector)),
  _depth( parent->_depth + 1 ),
  _children(NULL)
  {
  }

  void splitNode(int maxElementsPerNode,
                 int maxDepth,
                 double childAreaProportion);

  GenericQuadTree_Node* getBestNodeForInsertion(GenericQuadTree_Element* element,
                                                double childAreaProportion);

  void increaseNodeSector(GenericQuadTree_Element* element);

public:
  GenericQuadTree_Node(const Sector& sector) :
  _sector(new Sector(sector)),
  _depth(1),
  _children(NULL)
  {
  }

  ~GenericQuadTree_Node();

  Sector getSector() const {
    return *_sector;
  }

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

  int getDepth() const {
    return _depth;
  }

  size_t getNElements() const {
    return _elements.size();
  }

  bool isLeaf() const {
    return _children == NULL;
  }

  size_t getSubtreeNElements() const {
    size_t n = _elements.size();
    if (_children != NULL) {
      for (int i = 0; i<4;i++) {
        n += _children[i]->getSubtreeNElements();
      }
    }
    return n;
  }


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

};

#endif
