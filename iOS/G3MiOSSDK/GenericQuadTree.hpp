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

class GenericQuadTreeVisitor {
public:
  virtual ~GenericQuadTreeVisitor() {

  }

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

class GenericQuadTree_SectorElement: public GenericQuadTree_Element{
public:
  const Sector _sector;
  
  GenericQuadTree_SectorElement(const Sector& sector,
                          const void*   element) :
  _sector(sector),
  GenericQuadTree_Element(element){ }
  bool isSectorElement() const { return true;}
  Geodetic2D getCenter() const { return _sector.getCenter();}
  Sector getSector() const { return _sector;}

  ~GenericQuadTree_SectorElement() {}
};

class GenericQuadTree_Geodetic2DElement: public GenericQuadTree_Element {
public:
  const Geodetic2D _geodetic;

  GenericQuadTree_Geodetic2DElement(const Geodetic2D& geodetic,
                          const void*   element) :
  _geodetic(geodetic),
  GenericQuadTree_Element(element){}
  bool isSectorElement() const { return false;}
  Geodetic2D getCenter() const { return _geodetic;}
  Sector getSector() const { return Sector(_geodetic, _geodetic);}

  ~GenericQuadTree_Geodetic2DElement() {}
};
///////////////////////////////////////////////////////////////////////////////////////


class GenericQuadTree_Node {
private:
  const int     _depth;
  const Sector  _sector;

  Sector* _elementsSector;

  std::vector<GenericQuadTree_Element*> _elements;

  GenericQuadTree_Node** _children;

  GenericQuadTree_Node(const Sector& sector,
                GenericQuadTree_Node* parent) :
  _sector(sector),
  _depth( parent->_depth + 1 ),
  _children(NULL),
  _elementsSector(new Sector(sector))
  {
  }

  void splitNode(int maxElementsPerNode,
                 int maxDepth);

  void computeElementsSector();

public:
  GenericQuadTree_Node(const Sector& sector) :
  _sector(sector),
  _depth(1),
  _children(NULL),
  _elementsSector(new Sector(sector))
  {
  }

  ~GenericQuadTree_Node();

  Sector getSector() const{ return _sector;}
  Sector getElementsSector() const { return *_elementsSector;}

  bool add(GenericQuadTree_Element* element,
           int maxElementsPerNode,
           int maxDepth);

  bool acceptVisitor(const Sector& sector,
                     const GenericQuadTreeVisitor& visitor) const;

};

class GenericQuadTree {
private:
  GenericQuadTree_Node* _root;

  const int _maxElementsPerNode;
  const int _maxDepth;

public:

  GenericQuadTree(const Sector& sector) :
  _root( new GenericQuadTree_Node(sector) ),
  _maxElementsPerNode(1),
  _maxDepth(12)
  {
  }

  GenericQuadTree() :
  _root( new GenericQuadTree_Node(Sector::fullSphere()) ),
  _maxElementsPerNode(1),
  _maxDepth(12)
  {
  }

  ~GenericQuadTree();

  bool add(const Sector& sector, const void* element);

  bool add(const Geodetic2D& geodetic, const void* element);

  bool acceptVisitor(const Sector& sector,
                     const GenericQuadTreeVisitor& visitor) const;
  
};

////////////////////////////////////////////////////////////////////////////

class GenericQuadTree_TESTER {

  class GenericQuadTreeVisitor_TESTER {
  public:

    bool visitElement(const Sector& sector,
                      const void*   element) const{
      std::string* s = (std::string*)element;
      printf("ELEMENT -> %s\n", s->c_str());
      return true;
    }
    
    bool visitElement(const Geodetic2D& geodetic,
                              const void*   element) const{
      std::string* s = (std::string*)element;
      printf("ELEMENT -> %s\n", s->c_str());
      return true;
    }

    void endVisit(bool aborted) const{}
    
  };

public:
  static void run(int nElements);

};
#endif /* defined(__G3MiOSSDK__GenericGenericQuadTree__) */
