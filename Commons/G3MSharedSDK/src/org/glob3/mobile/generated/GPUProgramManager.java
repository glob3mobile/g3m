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

  private GPUProgram getCompiledProgram(String name)
  {
    return _programs.get(name);
  }

  private GPUProgram compileProgramWithName(GL gl, String name)
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

  private GPUProgram getNewProgram(GL gl, int uniformsCode, int attributesCode)
  {
  
    boolean texture = GPUVariable.codeContainsAttribute(attributesCode, GPUAttributeKey.TEXTURE_COORDS);
    boolean flatColor = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.FLAT_COLOR);
    boolean billboard = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.VIEWPORT_EXTENT);
    boolean color = GPUVariable.codeContainsAttribute(attributesCode, GPUAttributeKey.COLOR);
    boolean transformTC = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.TRANSLATION_TEXTURE_COORDS) || GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.SCALE_TEXTURE_COORDS);
  
    boolean hasLight = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.AMBIENT_LIGHT_COLOR);
  
    if (billboard)
    {
      return compileProgramWithName(gl, "Billboard");
    }
    if (flatColor && !texture && !color)
    {
  
      if (hasLight)
      {
        return compileProgramWithName(gl, "FlatColorMesh_DirectionLight");
      }
  
      return compileProgramWithName(gl, "FlatColorMesh");
    }
  
    if (!flatColor && texture && !color)
    {
      if (hasLight)
      {
        if (transformTC)
        {
          return compileProgramWithName(gl, "TransformedTexCoorTexturedMesh_DirectionLight");
        }
        return compileProgramWithName(gl, "TexturedMesh_DirectionLight");
      }
  
      if (transformTC)
      {
        return compileProgramWithName(gl, "TransformedTexCoorTexturedMesh");
      }
      return compileProgramWithName(gl, "TexturedMesh");
    }
  
    if (!flatColor && !texture && color)
    {
      return compileProgramWithName(gl, "ColorMesh");
    }
  
    if (!flatColor && !texture && !color)
    {
      return compileProgramWithName(gl, "NoColorMesh");
    }
  
    return null;
  }

  private GPUProgram getCompiledProgram(int uniformsCode, int attributesCode)
  {
    for (final GPUProgramData pd : _programs.values()) {
      GPUProgram p = pd._program;
      if ((p.getUniformsCode() == uniformsCode) && (p.getAttributesCode() == attributesCode)) {
        pd._usedSinceLastCleanUp = true; //Marked as used
        return p;
      }
    }
    return null;
  }

  public GPUProgramManager(GPUProgramFactory factory)
  {
     _factory = factory;
  }

  public void dispose()
  {
  }

  public final GPUProgram getProgram(GL gl, int uniformsCode, int attributesCode)
  {
    GPUProgram p = getCompiledProgram(uniformsCode, attributesCode);
    if (p == null)
    {
      p = getNewProgram(gl, uniformsCode, attributesCode);
  
      if (p.getAttributesCode() != attributesCode || p.getUniformsCode() != uniformsCode)
      {
        ILogger.instance().logError("New compiled program does not match GL state.");
      }
  
    }
  
    return p;
  }

  //Remove all shaders that have not been used since last call
  public final void deleteUnusedPrograms()
  {
    Iterator<Object> it = _programs.EntrySet().iterator();
    while (it.hasNext())
    {
      GPUProgramData pd = it.next();
      bool shouldRemove = !(pd._usedSinceLastCleanUp);
      if (shouldRemove){
        ILogger.instance().logInfo("Removing program %s because it hasn't been used in last frames.",
                                     it.second._program.getName());
        pd._program.dispose();
        it.remove();
      } else{
        pd._usedSinceLastCleanUp = false; //Marking as unused
      }
    }
  }
}