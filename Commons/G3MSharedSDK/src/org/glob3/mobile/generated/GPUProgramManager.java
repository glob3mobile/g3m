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

  public final GPUProgram getProgram(GL gl, GPUProgramState state)
  {
    for (final GPUProgram p : _programs.values()){
    	if (state.isLinkableToProgram(p)) {
        return p;
      }
    }

    int WORKING_JM;

    java.util.ArrayList<String> us = state.getUniformsNames();
    int size = us.size();
    for (int i = 0; i < size; i++)
    {
      if (us.get(i).compareTo("ViewPortExtent") == 0)
      {
        return getProgram(gl, "Billboard");
      }
    }

    return getProgram(gl, "Default");
  }

  public final GPUProgram getProgram(GL gl, GLState glState)
  {
    GLState thisGLState = glState;
    while (thisGLState != null)
    {
      java.util.ArrayList<String> ui = glState.getGPUProgramState().getUniformsNames();
      int sizeI = ui.size();
      for (int j = 0; j < sizeI; j++)
      {
        String name = ui.get(j);
  
        if (name.compareTo("uViewPortExtent") == 0)
        {
          return getProgram(gl, "Billboard");
        }
      }
  
  
      thisGLState = thisGLState.getParent();
    }
  
    int WORKING_JM;
    return getProgram(gl, "Default");
  }


}