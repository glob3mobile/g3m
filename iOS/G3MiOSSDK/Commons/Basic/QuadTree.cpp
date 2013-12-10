//
//  QuadTree.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "QuadTree.hpp"


QuadTree_Node::~QuadTree_Node() {
  const int elementsSize = _elements.size();
  for (int i = 0; i < elementsSize; i++) {
    delete _elements[i];
  }

  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      delete _children[i];
    }

    delete [] _children;
  }
}


bool QuadTree_Node::add(const Sector& sector,
                        const QuadTree_Content* content,
                        int maxElementsPerNode,
                        int maxDepth) {
  if (!_sector.fullContains(sector)) {
    return false;
  }
  if (!_sector.touchesWith(sector)) {
    return false;
  }

  if ((_elements.size() < maxElementsPerNode) ||
      (_depth >= maxDepth)) {
    _elements.push_back( new QuadTree_Element(sector, content) );
    return true;
  }

  if (_children == NULL) {
    _children = new QuadTree_Node*[4];

    const Geodetic2D lower = _sector._lower;
    const Geodetic2D upper = _sector._upper;

    const Angle splitLongitude = Angle::midAngle(lower._longitude, upper._longitude);
    const Angle splitLatitude  = Angle::midAngle(lower._latitude,  upper._latitude);

    const Sector sector0(lower,
                         Geodetic2D(splitLatitude, splitLongitude));

    const Sector sector1(Geodetic2D(lower._latitude, splitLongitude),
                         Geodetic2D(splitLatitude, upper._longitude));

    const Sector sector2(Geodetic2D(splitLatitude, lower._longitude),
                         Geodetic2D(upper._latitude, splitLongitude));

    const Sector sector3(Geodetic2D(splitLatitude, splitLongitude),
                         upper);

    _children[0] = new QuadTree_Node(sector0, this);
    _children[1] = new QuadTree_Node(sector1, this);
    _children[2] = new QuadTree_Node(sector2, this);
    _children[3] = new QuadTree_Node(sector3, this);
  }

  int selectedChildrenIndex = -1;
  bool keepHere = false;
  for (int i = 0; i < 4; i++) {
    QuadTree_Node* child = _children[i];
    if (child->_sector.touchesWith(sector)) {
      if (selectedChildrenIndex == -1) {
        selectedChildrenIndex = i;
      }
      else {
        keepHere = true;
        break;
      }
    }
  }

  if (keepHere) {
    _elements.push_back( new QuadTree_Element(sector, content) );
    return true;
  }

  if (selectedChildrenIndex >= 0) {
    return _children[selectedChildrenIndex]->add(sector,
                                                 content,
                                                 maxElementsPerNode,
                                                 maxDepth);
  }

  ILogger::instance()->logError("Logic error in QuadTree");
  return false;
}

bool QuadTree_Node::acceptVisitor(const Sector& sector,
                                  const QuadTreeVisitor& visitor) const {
  if (!_sector.touchesWith(sector)) {
    return false;
  }

  const int elementsSize = _elements.size();
  for (int i = 0; i < elementsSize; i++) {
    QuadTree_Element* element = _elements[i];
    if (element->_sector.touchesWith(sector)) {
      const bool abort = visitor.visitElement(element->_sector, element->_content);
      if (abort) {
        return true;
      }
    }
  }

  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      QuadTree_Node* child = _children[i];
      const bool abort = child->acceptVisitor(sector, visitor);
      if (abort) {
        return true;
      }
    }
  }

  return false;
}

bool QuadTree_Node::isEmpty() const {
  if (!_elements.empty()) {
    return false;
  }

  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      QuadTree_Node* child = _children[i];
      if (!child->isEmpty()) {
        return false;
      }
    }
  }

  return true;
}

QuadTree::~QuadTree() {
  delete _root;
}

bool QuadTree::add(const Sector& sector,
                   const QuadTree_Content* content) {
  return _root->add(sector, content, _maxElementsPerNode, _maxDepth);
}

bool QuadTree::acceptVisitor(const Sector& sector,
                             const QuadTreeVisitor& visitor) const {
  const bool aborted = _root->acceptVisitor(sector, visitor);
  visitor.endVisit(aborted);
  return aborted;
}

bool QuadTree::isEmpty() const {
  return _root->isEmpty();
}

void QuadTree::clear() {
  Sector sector = _root->_sector;

  delete _root;
  _root = new QuadTree_Node(sector);
}
