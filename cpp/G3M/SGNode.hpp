//
//  SGNode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3M__SGNode__
#define __G3M__SGNode__

#include <string>
#include <vector>

class G3MContext;
class G3MRenderContext;
class GLState;
class MutableMatrix44D;
class MutableVector3D;


class SGNode {
protected:
  const std::string _id;
  const std::string _sID;

  std::vector<SGNode*> _children;

protected:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  protected G3MContext _context;
#endif

  std::string _uriPrefix;

public:

  SGNode(const std::string& id,
         const std::string& sID) :
  _id(id),
  _sID(sID),
  _context(NULL),
  _uriPrefix("")
  {
  }

  virtual ~SGNode();

  virtual void initialize(const G3MContext* context,
                          const std::string& uriPrefix);

  void addNode(SGNode* child);

  virtual bool isReadyToRender(const G3MRenderContext* rc);

  virtual void prepareRender(const G3MRenderContext* rc);

  virtual void cleanUpRender(const G3MRenderContext* rc);

  virtual void render(const G3MRenderContext* rc,
                      const GLState* parentState,
                      bool renderNotReadyShapes);

  virtual const GLState* createState(const G3MRenderContext* rc,
                                     const GLState* parentState) {
    return parentState;
  }

  /** grows lower/upper to hold this node's vertices and its children's, in the shape's model space; transform: from this node to model space */
  virtual void addToModelBounds(const MutableMatrix44D& transform,
                                MutableVector3D& lower,
                                MutableVector3D& upper) const;

  size_t getChildrenCount() const {
    return _children.size();
  }

  SGNode* getChild(size_t i) const {
    return _children[i];
  }

  virtual void rawRender(const G3MRenderContext* rc,
                         const GLState* parentGLState) {
  }

  virtual const std::string description() {
    return "SGNode";
  }
  
};

#endif
