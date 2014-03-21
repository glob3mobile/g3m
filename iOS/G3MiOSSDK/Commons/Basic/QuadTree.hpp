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

class QuadTree_Content;

class QuadTreeVisitor {
public:
  virtual ~QuadTreeVisitor() {
  }

  virtual bool visitElement(const Sector&           sector,
                            const QuadTree_Content* content) const = 0;

  virtual void endVisit(bool aborted) const = 0;

};

class QuadTree_Content {
public:
#ifdef C_CODE
  virtual ~QuadTree_Content() {
  }
#else
  // useless, it's here only to make the C++ => Java translator creates an interface intead of an empty class
  virtual void unusedMethod() const = 0;
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  
};

class QuadTree_Element {
public:
  const Sector            _sector;
  const QuadTree_Content* _content;

  QuadTree_Element(const Sector&           sector,
                   const QuadTree_Content* content) :
  _sector(sector),
  _content(content)
  {
  }

  ~QuadTree_Element() {
    delete _content;
  }

};


class QuadTree_Node {
private:
  std::vector<QuadTree_Element*> _elements;

  QuadTree_Node** _children;

  QuadTree_Node(const Sector& sector,
                QuadTree_Node* parent) :
  _sector(sector),
  _depth( parent->_depth + 1 ),
  _children(NULL)
  {
  }

public:
  const int     _depth;
  const Sector  _sector;

  QuadTree_Node(const Sector& sector) :
  _sector(sector),
  _depth(1),
  _children(NULL)
  {
  }

  ~QuadTree_Node();

  bool add(const Sector& sector,
           const QuadTree_Content* content,
           int maxElementsPerNode,
           int maxDepth);

  bool acceptVisitor(const Sector& sector,
                     const QuadTreeVisitor& visitor) const;

  bool isEmpty() const;

};




class QuadTree {
private:
  QuadTree_Node* _root;

  const int _maxElementsPerNode;
  const int _maxDepth;

public:

  QuadTree(const Sector& sector) :
  _root( new QuadTree_Node(sector) ),
  _maxElementsPerNode(1),
  _maxDepth(12)
  {
  }

  QuadTree() :
  _root( new QuadTree_Node(Sector::fullSphere()) ),
  _maxElementsPerNode(1),
  _maxDepth(12)
  {
  }

  ~QuadTree();

  bool add(const Sector& sector,
           const QuadTree_Content* content);

  bool acceptVisitor(const Sector& sector,
                     const QuadTreeVisitor& visitor) const;

  void clear();

  bool isEmpty() const;

};

#endif
