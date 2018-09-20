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
    
    GLState* _glState;
    
    bool _ownsNode;
    
public:
    
    SGShape(SGNode* node,
            bool ownsNode,
            const std::string& uriPrefix,
            bool isTransparent,
            Geodetic3D* position,
            AltitudeMode altitudeMode,
            Condition* renderingCondition = NULL) :
    Shape(position, altitudeMode, renderingCondition),
    _node(node),
    _ownsNode(ownsNode),
    _uriPrefix(uriPrefix),
    _isTransparent(isTransparent)
    {
        _glState = new GLState();
        if (_isTransparent) {
            _glState->addGLFeature(new BlendingModeGLFeature(true,
                                                             GLBlendFactor::srcAlpha(),
                                                             GLBlendFactor::oneMinusSrcAlpha()),
                                   false);
        }
        else {
            _glState->addGLFeature(new BlendingModeGLFeature(false,
                                                             GLBlendFactor::srcAlpha(),
                                                             GLBlendFactor::oneMinusSrcAlpha()),
                                   false);
        }
    }
    
    ~SGShape();
    
    SGShape* clone(Geodetic3D* position,
                   AltitudeMode altitudeMode,
                   Condition* renderingCondition) const;
    
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
    
    std::vector<double> intersectionsDistances(const Planet* planet,
                                               const Vector3D& origin,
                                               const Vector3D& direction) const;
    
    
};

#endif
