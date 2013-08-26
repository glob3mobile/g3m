//
//  SGNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGNode__
#define __G3MiOSSDK__SGNode__

#include <string>
#include <vector>

#include "GLState.hpp"

class G3MContext;
class G3MRenderContext;
class SGShape;
class GLGlobalState;
class GPUProgramState;

class SGNode {
protected:
  const std::string _id;
  const std::string _sId;
  
  //  SGNode*              _parent;
  std::vector<SGNode*> _children;
  
  
  //  void setParent(SGNode* parent);
  
protected:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  protected G3MContext _context;
#endif
  
  SGShape *_shape;
  
public:
  
  SGNode(const std::string& id,
         const std::string& sId) :
  _id(id),
  _sId(sId),
  _context(NULL),
  _shape(NULL)
  //  _parent(NULL)
  {
  }
  
  virtual ~SGNode();
  
  virtual void initialize(const G3MContext* context,
                          SGShape *shape);
  
  void addNode(SGNode* child);
  
  virtual bool isReadyToRender(const G3MRenderContext* rc);

  virtual void prepareRender(const G3MRenderContext* rc);

  virtual void cleanUpRender(const G3MRenderContext* rc);

  virtual void render(const G3MRenderContext* rc,
                      const GLState* parentState,
                      bool renderNotReadyShapes);

  virtual const GLState* createState(const G3MRenderContext* rc,
                                     const GLState* parentState) { return parentState;}

  int getChildrenCount() const {
    return _children.size();
  }

  SGNode* getChild(int i) const {
    return _children[i];
  }

  virtual void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) {}

  virtual std::string description() {
    return "SGNode";
  };
};

#endif
