package org.glob3.mobile.generated; 
//
//  GPUProgramManager.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

//
//  GPUProgramManager.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//




public class GPUProgramManager
{

  private java.util.HashMap<String, GPUProgram> _programs = new java.util.HashMap<String, GPUProgram>();

  private GPUProgramFactory _factory;

  public GPUProgramManager(GPUProgramFactory factory)
  {
     _factory = factory;
  }

  public void dispose()
  {

  super.dispose();

  }

  public final GPUProgram getCompiledProgram(String name)
  {
    return _programs.get(name);
  }

  public final GPUProgram getProgram(GL gl, String name)
  {

    GPUProgram prog = getCompiledProgram(name);
    if (prog == null)
    {
      final GPUProgramSources ps = _factory.get(name);

      //Compile new Program
      if (ps != null)
      {
        prog = GPUProgram.createProgram(gl, ps._name, ps._vertexSource, ps._fragmentSource);
        if (prog == null)
        {
          ILogger.instance().logError("Problem at creating program named %s.", name);
          return null;
        }

        _programs.put(name, prog);
      }

    }
    return prog;
  }

//  GPUProgram* getProgram(GL* gl, const GPUProgramState& state) {
///#ifdef C_CODE
//    for(std::map<std::string, GPUProgram*>::const_iterator it = _programs.begin();
//        it != _programs.end(); it++) {
//      if (state.isLinkableToProgram(*it->second)) {
//        return it->second;
//      }
//    }
///#endif
///#ifdef JAVA_CODE
//    for (final GPUProgram p : _programs.values()) {
//    	if (state.isLinkableToProgram(p)) {
//        return p;
//      }
//    }
///#endif
//    
//    int WORKING_JM;
//    
//    std::vector<std::string>* us = state.getUniformsNames();
//    int size = us->size();
//    for (int i = 0; i < size; i++) {
//      if (us->at(i).compare("ViewPortExtent") == 0) {
//        return getProgram(gl, "Billboard");
//      }
//    }
//    
//    return getProgram(gl, "Default");
//  }

  public final GPUProgram getNewProgram(GL gl, int uniformsCode, int attributesCode)
  {
  
    boolean texture = GPUVariable.codeContainsAttribute(attributesCode, GPUAttributeKey.TEXTURE_COORDS);
    boolean flatColor = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.FLAT_COLOR);
    boolean billboard = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.VIEWPORT_EXTENT);
    boolean color = GPUVariable.codeContainsAttribute(attributesCode, GPUAttributeKey.COLOR);
    boolean transformTC = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.TRANSLATION_TEXTURE_COORDS) || GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.SCALE_TEXTURE_COORDS);
  
    /*
     #ifdef C_CODE
     const GLState* thisGLState = glState;
     #endif
     #ifdef JAVA_CODE
     GLState thisGLState = glState;
     #endif
     while (thisGLState != NULL) {
     std::vector<int>* ui = thisGLState->getGPUProgramState()->getUniformsKeys();
     int sizeI = ui->size();
     for (int j = 0; j < sizeI; j++) {
     int key = ui->at(j);
  
     if (key == VIEWPORT_EXTENT) {
     billboard = true;
  
     if (!GPUVariable::codeContainsUniform(uniformsCode, VIEWPORT_EXTENT)) {
     int a = 0;
     a++;
     }
  
     }
  
     if (key == FLAT_COLOR) {
     flatColor = true;
     }
  
     //      if (key == TRANSLATION_TEXTURE_COORDS) {
     //        texture = true;
     //      }
  
     if (key == TRANSLATION_TEXTURE_COORDS || key == SCALE_TEXTURE_COORDS) {
     transformTC = true;
     }
     }
  
     std::vector<int>* ai = thisGLState->getGPUProgramState()->getAttributeKeys();
     sizeI = ai->size();
     for (int j = 0; j < sizeI; j++) {
     int key = ai->at(j);
  
     //      if (key == TEXTURE_COORDS) {
     //        texture = true;
     //      }
  
     if (key == COLOR) {
     color = true;
  
     if (!GPUVariable::codeContainsAttribute(attributesCode, COLOR)) {
     int a = 0;
     a++;
     }
     }
     }
  
     thisGLState = thisGLState->getParent();
     }
     */
    if (billboard)
    {
      return getProgram(gl, "Billboard");
    }
    if (flatColor && !texture && !color)
    {
      return getProgram(gl, "FlatColorMesh");
    }
  
    if (!flatColor && texture && !color)
    {
      if (transformTC)
      {
        return getProgram(gl, "TransformedTexCoorTexturedMesh");
      }
      return getProgram(gl, "TexturedMesh");
    }
  
    if (!flatColor && !texture && color)
    {
      return getProgram(gl, "ColorMesh");
    }
  
    return null;
  }

  public final GPUProgram getCompiledProgram(int uniformsCode, int attributesCode)
  {
    for (final GPUProgram p : _programs.values()) {
      if ((p.getUniformsCode() == uniformsCode) && (p.getAttributesCode() == attributesCode)) {
        return p;
      }
    }
    return null;
  }

  public final GPUProgram getProgram(GL gl, int uniformsCode, int attributesCode)
  {
    GPUProgram p = getCompiledProgram(uniformsCode, attributesCode);
    if (p == null)
    {
      p = getNewProgram(gl, uniformsCode, attributesCode);
    }
    return p;
  }


}