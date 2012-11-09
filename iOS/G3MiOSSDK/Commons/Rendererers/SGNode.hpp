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

class InitializationContext;
class RenderContext;

class SGNode {
private:
  std::string _id;
  std::string _sId;

  SGNode*              _parent;
  std::vector<SGNode*> _children;


  void setParent(SGNode* parent);

protected:
#ifdef C_CODE
  const InitializationContext* _initializationContext;
#endif
#ifdef JAVA_CODE
  protected InitializationContext _initializationContext;
#endif

  virtual void prepareRender(const RenderContext* rc);

  virtual void cleanUpRender(const RenderContext* rc);

  virtual void rawRender(const RenderContext* rc);

public:

  SGNode() :
  _initializationContext(NULL),
  _parent(NULL)
  {

  }

  virtual ~SGNode();

  void initialize(const InitializationContext* ic);

  void addNode(SGNode* child);

  void setId(const std::string& id) {
    _id = id;
  }

  void setSId(const std::string& sId) {
    _sId = sId;
  }

  virtual bool isReadyToRender(const RenderContext* rc);

  void render(const RenderContext* rc);

};

#endif
