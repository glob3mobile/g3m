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
        ///#warning DETECT COLISSION WITH COLLECTION OF GPUPROGRAM
        if (prog == null)
        {
          ILogger.instance().logError("Problem at creating program named %s.", name);
          return null;
        }
  
        _programs.put(name, prog);
      }
      else
      {
        ILogger.instance().logError("No shader sources for program named %s.", name);
      }
  
    }
    return prog;
  }

  private GPUProgram getNewProgram(GL gl, int uniformsCode, int attributesCode)
  {
  
    final boolean texture = GPUVariable.hasAttribute(attributesCode, GPUAttributeKey.TEXTURE_COORDS);
    final boolean flatColor = GPUVariable.hasUniform(uniformsCode, GPUUniformKey.FLAT_COLOR);
    final boolean billboard = GPUVariable.hasUniform(uniformsCode, GPUUniformKey.VIEWPORT_EXTENT);
    final boolean color = GPUVariable.hasAttribute(attributesCode, GPUAttributeKey.COLOR);
    final boolean transformTC = (GPUVariable.hasUniform(uniformsCode, GPUUniformKey.TRANSLATION_TEXTURE_COORDS) || GPUVariable.hasUniform(uniformsCode, GPUUniformKey.SCALE_TEXTURE_COORDS));
    final boolean rotationTC = GPUVariable.hasUniform(uniformsCode, GPUUniformKey.ROTATION_ANGLE_TEXTURE_COORDS);
    final boolean hasLight = GPUVariable.hasUniform(uniformsCode, GPUUniformKey.AMBIENT_LIGHT_COLOR);
  
    final boolean hasTexture2 = GPUVariable.hasUniform(uniformsCode, GPUUniformKey.SAMPLER2);
  //  const bool hasTexture3 = GPUVariable::hasUniform(uniformsCode, SAMPLER3);
  
    final boolean is2D = GPUVariable.hasAttribute(attributesCode, GPUAttributeKey.POSITION_2D);
  
  //  const bool bbAnchor = GPUVariable::hasUniform(uniformsCode,    BILLBOARD_ANCHOR);
  
  
    if (is2D)
    {
      if (flatColor)
      {
        return compileProgramWithName(gl, "FlatColor2DMesh");
      }
      return compileProgramWithName(gl, "Textured2DMesh");
    }
  
    if (billboard)
    {
      if (transformTC)
      {
        return compileProgramWithName(gl, "Billboard_TransformedTexCoor");
      }
  
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
  
      if (hasTexture2)
      {
  
        if (transformTC)
        {
          if (rotationTC)
          {
            return compileProgramWithName(gl, "FullTransformedTexCoorMultiTexturedMesh");
          }
          return compileProgramWithName(gl, "TransformedTexCoorMultiTexturedMesh");
        }
  
        return compileProgramWithName(gl, "MultiTexturedMesh");
      }
  
      if (hasLight)
      {
        if (transformTC)
        {
  //        if (rotationTC) {
  //          return compileProgramWithName(gl, "TransformedTexCoorWithRotationTexturedMesh_DirectionLight");
  //        }
          return compileProgramWithName(gl, "TransformedTexCoorTexturedMesh_DirectionLight");
        }
        return compileProgramWithName(gl, "TexturedMesh_DirectionLight");
      }
  
      if (transformTC)
      {
        if (rotationTC)
        {
          return compileProgramWithName(gl, "FullTransformedTexCoorTexturedMesh");
        }
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
    for (final GPUProgram p : _programs.values()) {
      if ((p.getUniformsCode() == uniformsCode) && (p.getAttributesCode() == attributesCode)) {
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
      if (p == null)
      {
        ILogger.instance().logError("Problem at compiling program.");
        return null;
      }
  
      ///#warning AVOID getAttributesCode and getUniformsCode calls
      if (p.getAttributesCode() != attributesCode || p.getUniformsCode() != uniformsCode)
      {
        ///#warning GIVE MORE DETAIL
        ILogger.instance().logError("New compiled program does not match GL state.");
      }
    }
  
    p.addReference();
  
    return p;
  }

  public final void removeUnused()
  {
    final java.util.Iterator<java.util.Map.Entry<String, GPUProgram>> iterator = _programs.entrySet().iterator();
    while (iterator.hasNext()) {
      final java.util.Map.Entry<String, GPUProgram> entry = iterator.next();
      final GPUProgram program = entry.getValue();
      if (program.getNReferences() == 0) {
        ILogger.instance().logInfo("Deleting program %s", program.getName());
        iterator.remove();
      }
    }
  }
}