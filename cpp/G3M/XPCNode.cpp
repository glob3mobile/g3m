//
//  XPCNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCNode.hpp"

#include "JSONArray.hpp"
#include "JSONObject.hpp"
#include "JSONNumber.hpp"
#include "JSONString.hpp"
#include "Box.hpp"
#include "G3MRenderContext.hpp"
#include "Planet.hpp"
#include "Sphere.hpp"

#include "Sector.hpp"
#include "XPCParsing.hpp"
#include "XPCPointCloud.hpp"


const std::vector<XPCNode*>* XPCNode::fromJSON(const JSONArray* jsonArray) {
  if (jsonArray == NULL) {
    return NULL;
  }
  
  std::vector<XPCNode*>* result = new std::vector<XPCNode*>();

  const size_t size = jsonArray->size();

  for (size_t i = 0; i < size; i++) {
    const JSONObject* jsonObject = jsonArray->getAsObject(i);
    XPCNode* dimension = XPCNode::fromJSON(jsonObject);
    if (dimension == NULL) {
      // release the memory allocated up to here
      for (size_t j = 0; j < result->size(); j++) {
        delete result->at(j);
      }
      delete result;

      return NULL;
    }

    result->push_back( dimension );
  }

  return result;
}


XPCNode* XPCNode::fromJSON(const JSONObject* jsonObject) {

  const std::string id = jsonObject->getAsString("id")->value();

  const Sector* sector = XPCParsing::parseSector( jsonObject->getAsArray("sector") );

  const double minZ = jsonObject->getAsNumber("minZ")->value();
  const double maxZ = jsonObject->getAsNumber("maxZ")->value();

  return new XPCNode(id,
                     sector,
                     minZ,
                     maxZ);
}


XPCNode::XPCNode(const std::string& id,
                 const Sector* sector,
                 const double minZ,
                 const double maxZ) :
_id(id),
_sector(sector),
_minZ(minZ),
_maxZ(maxZ),
_bounds(NULL),
_renderedInPreviousFrame(false),
_projectedArea(-1),
_projectedAreaTS(-1),
_loadedContent(false),
_loadingContent(false),
_children(NULL),
_childrenSize(0)
{

}


XPCNode::~XPCNode() {
  delete _sector;

  for (size_t i = 0; i < _childrenSize; i++) {
    XPCNode* child = _children->at(i);
    delete child;
  }
#ifdef C_CODE
  delete _children;
#endif
}

const Sphere* XPCNode::getBounds(const G3MRenderContext* rc,
                                 const XPCPointCloud* pointCloud) {
  if (_bounds == NULL) {
    _bounds = calculateBounds(rc, pointCloud);
  }
  return _bounds;
}

Sphere* XPCNode::calculateBounds(const G3MRenderContext* rc,
                                 const XPCPointCloud* pointCloud) {
  const Planet* planet = rc->getPlanet();

  const float deltaHeight          = pointCloud->getDeltaHeight();
  const float verticalExaggeration = pointCloud->getVerticalExaggeration();

#ifdef C_CODE
  const Vector3D c[10] = {
    planet->toCartesian( _sector->getNE()     , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNE()     , (_maxZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNW()     , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNW()     , (_maxZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSE()     , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSE()     , (_maxZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSW()     , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSW()     , (_maxZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getCenter() , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getCenter() , (_maxZ + deltaHeight) * verticalExaggeration )
  };

  std::vector<Vector3D> points(c, c+10);
#endif
#ifdef JAVA_CODE
  java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(10);
  points.add( planet.toCartesian( _sector.getNE()     , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNE()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNW()     , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNW()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSE()     , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSE()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSW()     , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSW()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getCenter() , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getCenter() , (_maxZ + deltaHeight) * verticalExaggeration ) );
#endif

#warning TODO: check if the sphere fits into the parent's one
  //  if (_parent) {
  //    _parent->updateBoundingSphereWith(rc, vectorSet, _boundingSphere);
  //  }

  //return Sphere::enclosingSphere(points, 0.1);
  return Sphere::enclosingSphere(points, 0);
}

long long XPCNode::render(const XPCPointCloud* pointCloud,
                          const G3MRenderContext* rc,
                          GLState* glState,
                          const Frustum* frustum,
                          long long nowInMS) {

  long long renderedCount = 0;

  bool renderedInThisFrame = false;

  const Sphere* bounds = getBounds(rc, pointCloud);
  if (bounds != NULL) {
    const bool isVisible = bounds->touchesFrustum(frustum);
    if (isVisible) {
      if ((_projectedArea == -1) || ((_projectedAreaTS + 25) < nowInMS)) {
        const double projectedArea = bounds->projectedArea(rc);
        _projectedArea   = projectedArea;
        _projectedAreaTS = nowInMS;
      }

      const bool isBigEnough = (_projectedArea >= pointCloud->getMinProjectedArea());
      if (isBigEnough) {
        renderedInThisFrame = true;

        if (_loadedContent) {
          renderedCount += rawRender(pointCloud,
                                     rc,
                                     glState);
        }
        else {
          if (!_loadingContent) {
            _loadingContent = true;
            loadContent(rc);
          }
        }

        if (_children != NULL) {
          for (size_t i = 0; i < _childrenSize; i++) {
            XPCNode* child = _children->at(i);
            renderedCount += child->render(pointCloud,
                                           rc,
                                           glState,
                                           frustum,
                                           nowInMS);
          }
        }

      }
    }
  }

  if (_renderedInPreviousFrame != renderedInThisFrame) {
    if (_renderedInPreviousFrame) {
      unload();
    }
    _renderedInPreviousFrame = renderedInThisFrame;
  }

  return renderedCount;
}
