//
//  SGShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGShape__
#define __G3MiOSSDK__SGShape__

#include "Shape.hpp"

#include <string>

class SGNode;

class SGShape : public Shape {
private:
  SGNode* _node;
  const std::string _uriPrefix;

  const bool _isTransparent;

public:

  SGShape(SGNode* node,
          const std::string& uriPrefix,
          bool isTransparent,
          Geodetic3D* position,
          AltitudeMode altitudeMode) :
  Shape(position, altitudeMode),
  _node(node),
  _uriPrefix(uriPrefix),
  _isTransparent(isTransparent)
  {
    
  }

  SGNode* getNode() const {
    return _node;
  }

  const std::string getURIPrefix() const {
    return _uriPrefix;
  }

  void initialize(const G3MContext* context);

  bool isReadyToRender(const G3MRenderContext* rc);

  void rawRender(const G3MRenderContext* rc,
                 GLState* parentState,
                 bool renderNotReadyShapes);

  bool isTransparent(const G3MRenderContext* rc) {
    return _isTransparent;
  }
  
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const
  {
    std::vector<double> intersections;
    return intersections;
  }
  
  
};

#endif
