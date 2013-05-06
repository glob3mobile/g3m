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

class G3MContext;
class G3MRenderContext;
class SGShape;
class GLState;
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
  
  virtual void rawRender(const G3MRenderContext* rc,
                         const GLState& parentState, const GPUProgramState* parentProgramState);
  
  virtual void render(const G3MRenderContext* rc,
                      const GLState& parentState, const GPUProgramState* parentProgramState);
  
  //  SGShape* getShape() const {
  //    if (_shape != NULL) {
  //      return _shape;
  //    }
  //    if (_parent != NULL) {
  //      return _parent->getShape();
  //    }
  //    return NULL;
  //  }
  
  virtual GLState* createState(const G3MRenderContext* rc,
                               const GLState& parentState);
  
  virtual GPUProgramState* createGPUProgramState(const G3MRenderContext* rc,
                                                 const GPUProgramState* parentState);
  
};

#endif
