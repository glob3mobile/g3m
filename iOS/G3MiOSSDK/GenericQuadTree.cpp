//
//  GenericQuadTree.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/08/13.
//
//

#include "GenericQuadTree.hpp"

GenericQuadTree_Node::~GenericQuadTree_Node() {
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

void GenericQuadTree_Node::splitNode(int maxElementsPerNode,
                                     int maxDepth){
  _children = new GenericQuadTree_Node*[4];

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

  _children[0] = new GenericQuadTree_Node(sector0, this);
  _children[1] = new GenericQuadTree_Node(sector1, this);
  _children[2] = new GenericQuadTree_Node(sector2, this);
  _children[3] = new GenericQuadTree_Node(sector3, this);

  //Split elements by sector
  const int size = _elements.size();
  for (int i = 0; i < size; i++) {
    GenericQuadTree_Element* e = _elements[i];

    for (int j = 0; j < 4; j++){    
      GenericQuadTree_Node* child = _children[j];
      if (child->getSector().contains( e->getCenter()) ){
        child->add(e, maxElementsPerNode, maxDepth);
      }
    }
  }

  _elements.clear();
}

bool GenericQuadTree_Node::add(GenericQuadTree_Element* element,
                               int maxElementsPerNode,
                               int maxDepth) {
  Geodetic2D position = element->getCenter();

  if (!_sector.contains(position)) {
    return false;
  }

  if (_children == NULL){ //LEAF NODE

    _elements.push_back(element);
    if (_elements.size() > maxElementsPerNode && _depth < maxDepth){
      //WE SPLIT THE NODE
      splitNode(maxElementsPerNode, maxDepth);
    }
  } else{ //INNER NODE

    for (int j = 0; j < 4; j++){
      GenericQuadTree_Node* child = _children[j];
      if (child->getSector().contains( element->getCenter()) ){
        child->add(element, maxElementsPerNode, maxDepth);
      }
    }
  }
  return true;

}

bool GenericQuadTree_Node::acceptVisitor(const Sector& sector,
                                  const GenericQuadTreeVisitor& visitor) const {
  if (!_sector.touchesWith(sector)) {
    return false;
  }

  const int elementsSize = _elements.size();
  for (int i = 0; i < elementsSize; i++) {
    GenericQuadTree_Element* element = _elements[i];

    if (element->isSectorElement()){ //Element is associated to sector
      GenericQuadTree_SectorElement* e = (GenericQuadTree_SectorElement*) element;
      if (e->_sector.touchesWith(sector)) {
        const bool abort = visitor.visitElement(e->_sector, element->_element);
        if (abort) {
          return true;
        }
      }
    } else{ //Element is associated to geodetic
      GenericQuadTree_Geodetic2DElement* e = (GenericQuadTree_Geodetic2DElement*) element;
      if (sector.contains(e->_geodetic)) {
        const bool abort = visitor.visitElement(e->_geodetic, element->_element);
        if (abort) {
          return true;
        }
      }
      
    }
  }

  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      GenericQuadTree_Node* child = _children[i];
      const bool abort = child->acceptVisitor(sector, visitor);
      if (abort) {
        return true;
      }
    }
  }

  return false;
}

GenericQuadTree::~GenericQuadTree() {
  delete _root;
}

bool GenericQuadTree::add(const Sector& sector,
                   const void* element) {
  GenericQuadTree_SectorElement* sectorElement = new GenericQuadTree_SectorElement(sector, element);
  return _root->add(sectorElement, _maxElementsPerNode, _maxDepth);
}

bool GenericQuadTree::add(const Geodetic2D& geodetic,
                          const void* element) {
  GenericQuadTree_Geodetic2DElement* geodeticElement = new GenericQuadTree_Geodetic2DElement(geodetic, element);
  return _root->add(geodeticElement, _maxElementsPerNode, _maxDepth);
}

bool GenericQuadTree::acceptVisitor(const Sector& sector,
                             const GenericQuadTreeVisitor& visitor) const {
  const bool aborted = _root->acceptVisitor(sector, visitor);
  visitor.endVisit(aborted);
  return aborted;
}

///////////////////////////////////////////////////////////////////////
void GenericQuadTree_TESTER::run(int nElements){

  GenericQuadTree tree;

  for (int i = 0; i < nElements; i++) {

    int type = rand();
    if (type%2 == 0){

      Sector s = Sector::fromDegrees(rand(), rand(), rand(), rand());
      std::string* element = new std::string();
      *element = "SECTOR ELEMENT " + s.description();

      tree.add(s, element);

    } else{

      Geodetic2D geo = Geodetic2D::fromDegrees(rand(), rand());
      std::string* element = new std::string();
      *element = "GEODETIC ELEMENT " + geo.description();

      tree.add(geo, element);
    }
  }
  
}