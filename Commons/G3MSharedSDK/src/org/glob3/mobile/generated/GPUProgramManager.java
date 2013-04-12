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





//#include "G3MError.hpp"
//#include "G3MError.hpp"

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

  public GPUProgramManager()
  {
    if (_factory != null)
       _factory.dispose();

    for (java.util.Iterator<String, GPUProgram> it = _programs.iterator(); it.hasNext();)
    {
      it.next().getValue() = null;
    }
  }

  public final GPUProgram getProgram(String name)
  {

    java.util.Iterator<String, GPUProgram> it = _programs.indexOf(name);
    if (it.hasNext())
    {
      return it.next().getValue();
    }
    else
    {
      final GPUProgramSources ps = _factory.get(name);
      GPUProgram prog = null;

      //Compile new Program
      if (ps != null)
      {
        prog = GPUProgram.createProgram(_gl, ps._name, ps._vertexSource, ps._fragmentSource);
        if (prog == null)
        {
          ILogger.instance().logError("Problem at creating program named %s.", name);
          return null;
        }

        //_programs[prog->getName()] = prog;

        _programs.insert(std.<String, GPUProgram>pair(prog.getName(),prog));
      }
      return prog;
    }
  }

  public final GPUProgram getProgram(GPUProgramState state)
  {
    int WORKING_JM;
    return getProgram("DefaultProgram");
  }


}