//
//  GenericQuadTree.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/08/13.
//
//

#include "GenericQuadTree.hpp"

#include "GEOLineRasterSymbol.hpp"
#include "ICanvas.hpp"

#include "GEOVectorLayer.hpp"

#pragma mark NODE

GenericQuadTree_Node::~GenericQuadTree_Node() {
  const size_t elementsSize = _elements.size();
  for (size_t i = 0; i < elementsSize; i++) {
    delete _elements[i];
  }

  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      delete _children[i];
    }

    delete [] _children;
  }

  delete _sector;
}

void GenericQuadTree_Node::splitNode(int maxElementsPerNode,
                                     int maxDepth,
                                     double childAreaProportion) {
  _children = new GenericQuadTree_Node*[4];

  const Geodetic2D lower = _sector->_lower;
  const Geodetic2D upper = _sector->_upper;

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
  std::vector<GenericQuadTree_Element*> elementsToBeInserted = _elements;
  const size_t size = elementsToBeInserted.size();
  for (size_t i = 0; i < size; i++) {
    add(elementsToBeInserted[i], maxElementsPerNode, maxDepth, childAreaProportion);
  }
  _elements.clear();
}

GenericQuadTree_Node* GenericQuadTree_Node::getBestNodeForInsertion(GenericQuadTree_Element* element,
                                                                    double childAreaProportion) {


  Sector sector = element->getSector();
  //  double myArea = _sector->getAngularAreaInSquaredDegrees();
  double myArea = getAreaInSquaredDegreesAfterInsertion(sector);

  if (sector.getAngularAreaInSquaredDegrees() > (myArea * childAreaProportion)) {
    return this;
  }

  double minChildInsertionCost = _children[0]->getInsertionCostInSquaredDegrees(sector);
  GenericQuadTree_Node* bestChildForInsertion = _children[0];
  for (int i = 1; i < 4; i++) {
    GenericQuadTree_Node* child = _children[i];
    double cost = child->getInsertionCostInSquaredDegrees(sector);
    if (cost < minChildInsertionCost) {
      minChildInsertionCost = cost;
      bestChildForInsertion = child;
    }
    else {
      if (cost == minChildInsertionCost) {
        //        printf("BOTH CHILDREN WITH SAME COST");

        size_t n1 = bestChildForInsertion->getSubtreeNElements();
        size_t n2 = child->getSubtreeNElements();

        if (n2 < n1) { //SAME COST BUT LESS CONFLICTS
          minChildInsertionCost = cost;
          bestChildForInsertion = child;
        }
      }

    }
  }

  if (minChildInsertionCost == 0) {
    return bestChildForInsertion;
  }

  if (minChildInsertionCost > (myArea / 2)) { //We store in parent
    return this;
  }

  return bestChildForInsertion;

}

bool GenericQuadTree_Node::add(GenericQuadTree_Element* element,
                               int maxElementsPerNode,
                               int maxDepth,
                               double childAreaProportion) {

  if (_children == NULL) { //LEAF NODE

    if (_elements.size() < maxElementsPerNode || _depth == maxDepth) { //Tipical insertion
      _elements.push_back(element);
      increaseNodeSector(element);
      return true;
    }

    //Node must create children
    splitNode(maxElementsPerNode, maxDepth, childAreaProportion);//We must split
    return add(element, maxElementsPerNode, maxDepth, childAreaProportion); //We try it again, this time as inner node
  }

  //INNER NODE

  //Calculate best node for insertion
  GenericQuadTree_Node* bestInsertionNode = getBestNodeForInsertion(element, childAreaProportion);

  if (bestInsertionNode == this) {
    _elements.push_back(element);
    increaseNodeSector(element);
    return true;
  }

  //Inserting into child
  increaseNodeSector(element);
  return bestInsertionNode->add(element, maxElementsPerNode, maxDepth, childAreaProportion);
}

bool GenericQuadTree_Node::remove(const void* element) {
  bool wasRemoved = false;

#ifdef C_CODE
  for (int i = 0; i < _elements.size(); i++) {
    GenericQuadTree_Element* item = _elements[i];
    if (item->_element == element) {
      _elements.erase(_elements.begin() + i);
      delete item;
      wasRemoved = true;
      break;
    }
  }
#endif
#ifdef JAVA_CODE
  final java.util.Iterator<GenericQuadTree_Element> iterator = _elements.iterator();
  while (iterator.hasNext()) {
    final GenericQuadTree_Element item = iterator.next();
    if (item._element == element) {
      iterator.remove();
      wasRemoved = true;
      break;
    }
  }
#endif

  if (wasRemoved) {
    return true;
  }

  if (_children != NULL) {

    for (int i = 0; i < 4; i++) {
      if (_children[i]->remove(element)) {
        //The item was removed from one of my children

        //Removing all children if none has an item
        int nChild = 0;
        for (int j = 0; j < 4; j++) {
          nChild += _children[j]->getSubtreeNElements();
        }

        if (nChild == 0) {
          for (int j = 0; j < 4; j++) {
            delete _children[j];
          }
          delete [] _children;
          _children = NULL;
        }

        return true;
      }
    }
  }

  return false;
}

bool GenericQuadTree_Node::acceptVisitor(const Sector& sector,
                                         const GenericQuadTreeVisitor& visitor) const {



  visitor.addComparisonsDoneWhileVisiting(1);

  if (!_sector->touchesWith(sector)) {
    return false;
  }

  const size_t elementsSize = _elements.size();
  visitor.addComparisonsDoneWhileVisiting(elementsSize);

  for (int i = 0; i < elementsSize; i++) {
    GenericQuadTree_Element* element = _elements[i];

    if (element->isSectorElement()) { //Element is associated to sector
      GenericQuadTree_SectorElement* e = (GenericQuadTree_SectorElement*) element;
      if (e->_sector.touchesWith(sector)) {
        const bool abort = visitor.visitElement(e->_sector, element->_element);
        if (abort) {
          return true;
        }
      }
    }
    else { //Element is associated to geodetic
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

bool GenericQuadTree_Node::acceptVisitor(const Geodetic2D& geo,
                                         const GenericQuadTreeVisitor& visitor) const {


  visitor.addComparisonsDoneWhileVisiting(1);

  if (!_sector->contains(geo)) {
    return false;
  }

  const size_t elementsSize = _elements.size();
  visitor.addComparisonsDoneWhileVisiting(elementsSize);

  for (size_t i = 0; i < elementsSize; i++) {
    GenericQuadTree_Element* element = _elements[i];

    if (element->isSectorElement()) { //Element is associated to sector
      GenericQuadTree_SectorElement* e = (GenericQuadTree_SectorElement*) element;
      if (e->_sector.contains(geo)) {
        const bool abort = visitor.visitElement(e->_sector, element->_element);
        if (abort) {
          return true;
        }
      }
    }
    else { //Element is associated to geodetic
      GenericQuadTree_Geodetic2DElement* e = (GenericQuadTree_Geodetic2DElement*) element;
      if (geo.isEquals(e->_geodetic)) {
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
      const bool abort = child->acceptVisitor(geo, visitor);
      if (abort) {
        return true;
      }
    }
  }

  return false;
}

bool GenericQuadTree_Node::acceptNodeVisitor(GenericQuadTreeNodeVisitor& visitor) const {
  if (visitor.visitNode(this)) {
    return true;
  }
  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      GenericQuadTree_Node* child = _children[i];
      if (child->acceptNodeVisitor(visitor)) {
        return true;
      }
    }
  }
  return false;
}

double GenericQuadTree_Node::getInsertionCostInSquaredDegrees(const Sector& sector) const {

  Sector newSector = _sector->mergedWith(sector);
  double areaAfter = newSector.getAngularAreaInSquaredDegrees();

  double areaNow = _sector->getAngularAreaInSquaredDegrees();
  return areaAfter - areaNow;
}

double GenericQuadTree_Node::getAreaInSquaredDegreesAfterInsertion(const Sector& sector) const {
  Sector newSector = _sector->mergedWith(sector);
  return newSector.getAngularAreaInSquaredDegrees();
}

void GenericQuadTree_Node::increaseNodeSector(GenericQuadTree_Element* element) {
  Sector s = *_sector;
  delete _sector;
  _sector = new Sector(s.mergedWith(element->getSector()));
}

void GenericQuadTree_Node::symbolize(GEOVectorLayer* geoVectorLayer) const {

  if (_elements.size() > 0) {
    std::vector<Geodetic2D*>* coordinates = new std::vector<Geodetic2D*>();

    coordinates->push_back( new Geodetic2D( _sector->getSW() ) );
    coordinates->push_back( new Geodetic2D( _sector->getNW() ) );
    coordinates->push_back( new Geodetic2D( _sector->getNE() ) );
    coordinates->push_back( new Geodetic2D( _sector->getSE() ) );
    coordinates->push_back( new Geodetic2D( _sector->getSW() ) );

    //    printf("RESTERIZING: %s\n", _sector->description().c_str());

    float dashLengths[] = {};
    int dashCount = 0;

    Color c = Color::RED.wheelStep(12, _depth);

    GEO2DLineRasterStyle ls(c, //const Color&     color,
                            (float)1.0, //const float      width,
                            CAP_ROUND, // const StrokeCap  cap,
                            JOIN_ROUND, //const StrokeJoin join,
                            1,//const float      miterLimit,
                            dashLengths,//float            dashLengths[],
                            dashCount,//const int        dashCount,
                            0);//const int        dashPhase) :


    const GEO2DCoordinatesData* coordinatesData = new GEO2DCoordinatesData(coordinates);
    GEOLineRasterSymbol * symbol = new GEOLineRasterSymbol(coordinatesData, ls);
    coordinatesData->_release();

    geoVectorLayer->addSymbol(symbol);
  }

  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      _children[i]->symbolize(geoVectorLayer);
    }
  }


}

void GenericQuadTree_Node::getGeodetics(std::vector<Geodetic2D*>& geo) const {
  for (int i = 0; i < _elements.size(); i++) {
    if (!_elements[i]->isSectorElement()) {
      geo.push_back( new Geodetic2D(_elements[i]->getCenter()) );
    }
  }

  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      _children[i]->getGeodetics(geo);
    }
  }

}

void GenericQuadTree_Node::getSectors(std::vector<Sector*>& sectors) const {
  for (int i = 0; i < _elements.size(); i++) {
    if (_elements[i]->isSectorElement()) {
      sectors.push_back( new Sector(_elements[i]->getSector()) );
    }
  }

  if (_children != NULL) {
    for (int i = 0; i < 4; i++) {
      _children[i]->getSectors(sectors);
    }
  }

}

#pragma mark TREE

GenericQuadTree::~GenericQuadTree() {
  delete _root;
}

bool GenericQuadTree::add(GenericQuadTree_Element* element) {

  if (_root == NULL) {
    _root = new GenericQuadTree_Node(element->getSector());
  }

  return _root->add(element, _maxElementsPerNode, _maxDepth, _childAreaProportion);
}

bool GenericQuadTree::remove(const void* element) {
  if (_root != NULL) {
    return _root->remove(element);
  }
  return false;
}

bool GenericQuadTree::add(const Sector& sector,
                          const void* element) {
  GenericQuadTree_SectorElement* sectorElement = new GenericQuadTree_SectorElement(sector, element);
  return add(sectorElement);
}

bool GenericQuadTree::add(const Geodetic2D& geodetic,
                          const void* element) {
  GenericQuadTree_Geodetic2DElement* geodeticElement = new GenericQuadTree_Geodetic2DElement(geodetic, element);
  return add(geodeticElement);
}

bool GenericQuadTree::acceptVisitor(const Sector& sector,
                                    const GenericQuadTreeVisitor& visitor) const {
  bool aborted = false;
  if (_root != NULL) {
    aborted = _root->acceptVisitor(sector, visitor);
  }
  visitor.endVisit(aborted);
  return aborted;
}

bool GenericQuadTree::acceptVisitor(const Geodetic2D& geo,
                                    const GenericQuadTreeVisitor& visitor) const {
  if (_root != NULL) {
    const bool aborted = _root->acceptVisitor(geo, visitor);
    visitor.endVisit(aborted);
    return aborted;
  }
  return false;
}

bool GenericQuadTree::acceptNodeVisitor(GenericQuadTreeNodeVisitor& visitor) const {
  if (_root != NULL) {
    const bool aborted = _root->acceptNodeVisitor(visitor);
    visitor.endVisit(aborted);
    return aborted;
  }
  return false;
}

void GenericQuadTree::symbolize(GEOVectorLayer* geoVectorLayer) const {
  if (_root != NULL) {
    _root->symbolize(geoVectorLayer);
  }
}


#pragma mark TESTER


int GenericQuadTree_TESTER::_nComparisons = 0;
int GenericQuadTree_TESTER::_nElements = 0;

void GenericQuadTree_TESTER::run(int nElements,  GEOVectorLayer* geoVectorLayer) {

  _nElements = 0;
  _nComparisons = 0;


  GenericQuadTree tree;
  std::vector<Sector*> sectors;
  std::vector<Geodetic2D*> geos;

  for (int i = 0; i < nElements; i++) {
    double minLat = randomInt(180) -90;// rand()%180 -90;
    double minLon = randomInt(360) - 180;//rand()%360 -180;

    int type = randomInt(2);
    if (type == 0) {

      double maxLat = minLat + randomInt(90 - (int)minLat); //rand()%(90 - (int)minLat);
      double maxLon = minLon + randomInt(90 - (int)minLat);//rand()%(180 - (int)minLon);

      Sector s = Sector::fromDegrees(minLat, minLon, maxLat, maxLon);
      sectors.push_back(new Sector(s));
      std::string desc = "SECTOR ELEMENT " + s.description();
      std::string* element = new std::string();
      *element = desc;

      if (!tree.add(s, element)) {
        ILogger::instance()->logInfo("ERROR");
      }

    }
    else {
      Geodetic2D geo = Geodetic2D::fromDegrees(minLat, minLon);
      geos.push_back(new Geodetic2D(geo));
      std::string desc = "GEODETIC ELEMENT " + geo.description();
      std::string* element = new std::string();
      *element = desc;

      if (!tree.add(geo, element)) {
        ILogger::instance()->logInfo("ERROR");
      }
    }
  }

  for (int i = 0; i < sectors.size(); i++) {
#ifdef C_CODE
    Sector s = *(sectors[i]);
#endif
#ifdef JAVA_CODE
    Sector s = sectors.get(i);
#endif
    GenericQuadTreeVisitorSector_TESTER vis(s);
    tree.acceptVisitor(s, vis);
  }

  for (int i = 0; i < geos.size(); i++) {
#ifdef C_CODE
    Geodetic2D g = *(geos[i]);
#endif
#ifdef JAVA_CODE
    Geodetic2D g = geos.get(i);
#endif
    GenericQuadTreeVisitorGeodetic_TESTER vis(g);
    tree.acceptVisitor(g, vis);
  }

  NodeVisitor_TESTER nodeVis;
  tree.acceptNodeVisitor(nodeVis);

//  if (rasterizer != NULL) {
//    tree.symbolize(rasterizer);
//  }
  
  if (geoVectorLayer != NULL) {
    tree.symbolize(geoVectorLayer);
  }

  double c_e = (float)_nComparisons / _nElements;
  ILogger::instance()->logInfo("NElements Found = %d, Mean NComparisons = %f -> COEF: %f\n", _nElements, c_e, c_e / _nElements);

}

void GenericQuadTree_TESTER::run(GenericQuadTree& tree, GEOVectorLayer* geoVectorLayer) {

  _nElements = 0;
  _nComparisons = 0;

  std::vector<Sector*> sectors = tree.getSectors();
  std::vector<Geodetic2D*> geos = tree.getGeodetics();

  for (int i = 0; i < sectors.size(); i++) {
#ifdef C_CODE
    Sector s = *(sectors[i]);
#endif
#ifdef JAVA_CODE
    Sector s = sectors.get(i);
#endif
    GenericQuadTreeVisitorSector_TESTER vis(s);
    tree.acceptVisitor(s, vis);
    delete sectors[i];
  }

  for (int i = 0; i < geos.size(); i++) {
#ifdef C_CODE
    Geodetic2D g = *(geos[i]);
#endif
#ifdef JAVA_CODE
    Geodetic2D g = geos.get(i);
#endif
    GenericQuadTreeVisitorGeodetic_TESTER vis(g);
    tree.acceptVisitor(g, vis);

    delete geos[i];
  }

  NodeVisitor_TESTER nodeVis;
  tree.acceptNodeVisitor(nodeVis);
  
  if (geoVectorLayer != NULL) {
    tree.symbolize(geoVectorLayer);
  }
  
  double c_e = (float)_nComparisons / _nElements;
  ILogger::instance()->logInfo("NElements Found = %d, Mean NComparisons = %f -> COEF: %f\n", _nElements, c_e, c_e / _nElements);
  
}
