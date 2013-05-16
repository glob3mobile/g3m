//
//  SGGeometryNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGGeometryNode__
#define __G3MiOSSDK__SGGeometryNode__

#include "SGNode.hpp"

class IFloatBuffer;
class IShortBuffer;
class GPUProgramState;

class SGGeometryNode : public SGNode {
private:
  const int     _primitive;
  IFloatBuffer* _vertices;
  IFloatBuffer* _colors;
  IFloatBuffer* _uv;
  IFloatBuffer* _normals;
  IShortBuffer* _indices;
  
  GLState _glState;
  GPUProgramState _programState;

public:

  SGGeometryNode(const std::string& id,
                 const std::string& sId,
                 int                primitive,
                 IFloatBuffer*      vertices,
                 IFloatBuffer*      colors,
                 IFloatBuffer*      uv,
                 IFloatBuffer*      normals,
                 IShortBuffer*      indices) :
  SGNode(id, sId),
  _primitive(primitive),
  _vertices(vertices),
  _colors(colors),
  _uv(uv),
  _normals(normals),
  _indices(indices)
  {

  }

  ~SGGeometryNode();


  void rawRender(const G3MRenderContext* rc,
                 const GLState& parentState, const GPUProgramState* parentProgramState);

//  GLState* createState(const G3MRenderContext* rc,
//                             const GLState& parentState) {
//    return NULL;
//  }
//  
//  GPUProgramState * createGPUProgramState(const G3MRenderContext *rc, const GPUProgramState *parentState);
  
  //Idle if this is not a drawable client
  void getGLStateAndGPUProgramState(GLState** glState, GPUProgramState** progState){
    _programState.clear();
    (*glState) = &_glState;
    (*progState) = &_programState;
  }

  void modifyGLState(GLState& glState) const;
  void modifyGPUProgramState(GPUProgramState& progState) const;
  
};

#endif
