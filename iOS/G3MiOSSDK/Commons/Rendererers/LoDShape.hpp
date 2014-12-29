#include "Shape.hpp"

#include <string>

class SGNode;
class OrientedBox;
class BoundingVolume;
class SGShape;

class LoDLevel{

  
public:
  SGShape* _sgShape; //DUE TO A TERRIBLE DEPENDENCY FOR INITIALIZATION
  SGNode* const _node;
  const std::string _uriPrefix;
  const double _perfectDistanceSquared;
  const bool _isTransparent;
  
  OrientedBox* _boundingVolume;
  
  LoDLevel(SGNode* node,
           const std::string uriPrefix,
           double perfectDistance,
           bool isTransparent):
  _node(node),
  _uriPrefix(uriPrefix),
  _perfectDistanceSquared(perfectDistance * perfectDistance),
  _isTransparent(isTransparent),
  _boundingVolume(NULL),
  _sgShape(NULL){
    
  }
  
  ~LoDLevel();
};

class LoDShape : public Shape {
private:
  

  
  std::vector<LoDLevel*> _nodes;
  
  GLState* _glState;
  
  LoDLevel* _lastRenderedLevel;
  
  void calculateRenderableLevel(const G3MRenderContext* rc);
 
  
protected:
  BoundingVolume* getBoundingVolume(const G3MRenderContext *rc);
  
  
public:
  
  LoDShape(std::vector<LoDLevel*>& lodLevels,
           Geodetic3D* position,
          AltitudeMode altitudeMode) :
  Shape(position, altitudeMode),
  _lastRenderedLevel(NULL)
  {
    _glState = new GLState();
    
    if (lodLevels.size() < 1){
      ILogger::instance()->logError("LoDShape not valid");
      return;
    }
    
    bool transparent = false;
    for (int i = 0; i < lodLevels.size(); i++) {
      _nodes.push_back(lodLevels.at(i));
      if (lodLevels.at(i)->_isTransparent){
        transparent = true;
      }
    }
    
    _lastRenderedLevel = lodLevels.at(0);
    
    if (transparent) {
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
  
  ~LoDShape();
  
  SGNode* getNode() const {
    
    if (_lastRenderedLevel != NULL){
      return _lastRenderedLevel->_node;
    }
    
    return NULL;
  }
  
  const std::string getURIPrefix() const {
    if (_lastRenderedLevel != NULL){
      return _lastRenderedLevel->_uriPrefix;
    }
    
    return "NOT RENDERED YET. MANY URIS";
  }
  
  void initialize(const G3MContext* context);
  
  bool isReadyToRender(const G3MRenderContext* rc);
  
  void rawRender(const G3MRenderContext* rc,
                 GLState* parentState,
                 bool renderNotReadyShapes);
  
  bool isTransparent(const G3MRenderContext* rc) {
    
    if (_lastRenderedLevel != NULL){
      return _lastRenderedLevel->_isTransparent;
    }
    
    return false;
  }
  
  std::vector<double> intersectionsDistances(const Planet* planet,
                                             const Camera* camera,
                                             const Vector3D& origin,
                                             const Vector3D& direction);
  
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction)
  {
    std::vector<double> intersections;
    return intersections;
  }
  
  void zRawRender(const G3MRenderContext* rc, GLState* parentGLState);
  
  
  bool isVisible(const G3MRenderContext *rc);
  
  void setSelectedDrawMode(bool mode) {}
  
  GEORasterSymbol* createRasterSymbolIfNeeded() const {
    return NULL;
  }
  
  
};