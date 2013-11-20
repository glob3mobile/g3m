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

  public final GPUProgram getNewProgram(GL gl, int uniformsCode, int attributesCode)
  {
  
    boolean texture = GPUVariable.codeContainsAttribute(attributesCode, GPUAttributeKey.TEXTURE_COORDS);
    boolean flatColor = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.FLAT_COLOR);
    boolean billboard = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.VIEWPORT_EXTENT);
    boolean color = GPUVariable.codeContainsAttribute(attributesCode, GPUAttributeKey.COLOR);
    boolean transformTC = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.TRANSLATION_TEXTURE_COORDS) || GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.SCALE_TEXTURE_COORDS);
  
    boolean hasLight = GPUVariable.codeContainsUniform(uniformsCode, GPUUniformKey.AMBIENT_LIGHT);
  
    if (billboard)
    {
      return getProgram(gl, "Billboard");
    }
    if (flatColor && !texture && !color)
    {
  
      if (hasLight)
      {
        return getProgram(gl, "FlatColorMesh+DirectionLight");
      }
  
      return getProgram(gl, "FlatColorMesh");
    }
  
    if (!flatColor && texture && !color)
    {
  
      if (hasLight)
      {
        if (transformTC)
        {
          return getProgram(gl, "TransformedTexCoorTexturedMesh+DirectionLight");
        }
        return getProgram(gl, "TexturedMesh+DirectionLight");
      }
  
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
  
    if (!flatColor && !texture && !color)
    {
      return getProgram(gl, "NoColorMesh");
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