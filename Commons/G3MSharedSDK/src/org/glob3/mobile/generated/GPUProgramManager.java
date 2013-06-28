package org.glob3.mobile.generated; 
//
//  GPUProgramManager.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

//
//  GPUProgramManager.h
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
//        it != _programs.end(); it++){
//      if (state.isLinkableToProgram(*it->second)){
//        return it->second;
//      }
//    }
///#endif
///#ifdef JAVA_CODE
//    for (final GPUProgram p : _programs.values()){
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
//      if (us->at(i).compare("ViewPortExtent") == 0){
//        return getProgram(gl, "Billboard");
//      }
//    }
//    
//    return getProgram(gl, "Default");
//  }

  public final GPUProgram getProgram(GL gl, GLState glState)
  {
  
    boolean texture = false;
    boolean flatColor = false;
    boolean billboard = false;
    boolean color = false;
    boolean transformTC = false;
  
    GLState thisGLState = glState;
    while (thisGLState != null)
    {
      java.util.ArrayList<Integer> ui = thisGLState.getGPUProgramState().getUniformsKeys();
      int sizeI = ui.size();
      for (int j = 0; j < sizeI; j++)
      {
        int key = ui.get(j);
  
        if (key == GPUUniformKey.VIEWPORT_EXTENT.getValue())
        {
          billboard = true;
        }
  
        if (key == GPUUniformKey.FLAT_COLOR.getValue())
        {
          flatColor = true;
        }
  
  //      if (key == TRANSLATION_TEXTURE_COORDS){
  //        texture = true;
  //      }
  
        if (key == GPUUniformKey.TRANSLATION_TEXTURE_COORDS.getValue() || key == GPUUniformKey.SCALE_TEXTURE_COORDS.getValue())
        {
          transformTC = true;
        }
      }
  
      java.util.ArrayList<Integer> ai = thisGLState.getGPUProgramState().getAttributeKeys();
      sizeI = ai.size();
      for (int j = 0; j < sizeI; j++)
      {
        int key = ai.get(j);
  
        if (key == GPUAttributeKey.TEXTURE_COORDS.getValue())
        {
          texture = true;
        }
  
        if (key == GPUAttributeKey.COLOR.getValue())
        {
          color = true;
        }
      }
  
      thisGLState = thisGLState.getParent();
    }
  
    if (billboard)
    {
      return getProgram(gl, "Billboard");
    }
    else
    {
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
        else
        {
          return getProgram(gl, "TexturedMesh");
        }
      }
  
      if (!flatColor && !texture && color)
      {
        return getProgram(gl, "ColorMesh");
      }
  
    }
  
  
    return null;
  
  }


}