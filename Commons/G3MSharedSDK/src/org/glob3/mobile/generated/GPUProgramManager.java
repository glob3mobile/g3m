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
  private GL _gl;

  public GPUProgramManager(GL gl, GPUProgramFactory factory)
  {
     _gl = gl;
     _factory = factory;
  }

  public void dispose()
  {

  }

  public final GPUProgram getCompiledProgram(String name)
  {
    return _programs.get(name);
  }

  public final GPUProgram getProgram(String name)
  {

    GPUProgram prog = getCompiledProgram(name);
    if (prog == null)
    {
      final GPUProgramSources ps = _factory.get(name);

      //Compile new Program
      if (ps != null)
      {
        prog = GPUProgram.createProgram(_gl, ps._name, ps._vertexSource, ps._fragmentSource);
        if (prog == null)
        {
          ILogger.instance().logError("Problem at creating program named %s.", name);
          return null;
        }

        _programs.put(name, prog);

        //_programs.insert ( std::pair<std::string, GPUProgram*>(prog->getName(),prog) );
      }

    }
    return prog;
  }

  public final GPUProgram getProgram(GPUProgramState state)
  {
    final Objects[] progs = _programs.values().toArray();
    for (int i = 0; i < progs.length; i++) {
      GPUProgram p = (GPUProgram)progs[i];
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
        return getProgram("Billboard");
      }
    }

    return getProgram("Default");
  }


}