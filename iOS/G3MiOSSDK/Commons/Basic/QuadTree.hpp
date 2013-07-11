//
//  QuadTree.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__QuadTree__
#define __G3MiOSSDK__QuadTree__

#include "Sector.hpp"

class QuadTreeVisitor {
public:
  virtual ~QuadTreeVisitor() {

  }

  virtual bool visitElement(const Sector& sector,
                            const void*   element) const = 0;
};


class QuadTree_Element {
public:
  const Sector _sector;
  const void*  _element;

  QuadTree_Element(const Sector& sector,
                   const void*   element) :
  _sector(sector),
  _element(element)
  {
  }

  ~QuadTree_Element() {

  }

};


class QuadTree_Node {
private:
  const int     _depth;
  const Sector  _sector;
  std::vector<QuadTree_Element*> _elements;

  QuadTree_Node** _children;

public:
  QuadTree_Node(const Sector& sector) :
  _sector(sector),
  _depth(1),
  _children(NULL)
  {
  }

  QuadTree_Node(const Sector& sector,
                QuadTree_Node* parent) :
  _sector(sector),
  _depth( parent->_depth + 1 ),
  _children(NULL)
  {
  }

  ~QuadTree_Node();

  bool add(const Sector& sector,
           const void* element,
           int maxElementsPerNode,
           int maxDepth);

  bool visitElements(const Sector& sector,
                     const QuadTreeVisitor& visitor) const;

};




class QuadTree {
private:
  const Sector  _sector;
//#ifdef C_CODE
  QuadTree_Node* _root;
//#endif
//#ifdef JAVA_CODE
//  private QuadTree_Node _root;
//#endif

  const int _maxElementsPerNode;
  const int _maxDepth;

public:

  QuadTree(const Sector& sector) :
  _sector( sector ),
  _root( new QuadTree_Node(sector) ),
  _maxElementsPerNode(16),
  _maxDepth(12)
  {
  }

  QuadTree() :
  _sector( Sector::fullSphere() ),
  _root( new QuadTree_Node(Sector::fullSphere()) ),
  _maxElementsPerNode(16),
  _maxDepth(12)
  {
  }

  ~QuadTree();

  bool add(const Sector& sector,
           const void* element);

  void visitElements(const Sector& sector,
                     const QuadTreeVisitor& visitor) const;
  
};

#endif
