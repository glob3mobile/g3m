package org.glob3.mobile.generated; 
//
//  GPUProgram.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

//
//  GPUProgram.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//




//class IFloatBuffer;

//class GL;

public class GPUProgram
{

  private INativeGL _nativeGL;
  private int _programID;
  private boolean _programCreated;
  private java.util.ArrayList<Attribute> _attributes = new java.util.ArrayList<Attribute>();
  private java.util.ArrayList<Uniform> _uniforms = new java.util.ArrayList<Uniform>();

  private boolean compileShader(int shader, String source)
  {
    boolean result = _nativeGL.compileShader(shader, source);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
    _nativeGL.printShaderInfoLog(shader);
//#endif
  
    if (result)
    {
      _nativeGL.attachShader(_programID, shader);
    }
    else
    {
      ILogger.instance().logError("GPUProgram: Problem encountered while compiling shader.");
    }
  
    return result;
  }
  private boolean linkProgram()
  {
    boolean result = _nativeGL.linkProgram(_programID);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
    _nativeGL.printProgramInfoLog(_programID);
//#endif
    return result;
  }
  private void deleteShader(int shader)
  {
    if (!_nativeGL.deleteShader(shader))
    {
      ILogger.instance().logError("GPUProgram: Problem encountered while deleting shader.");
    }
  }

  private void getVariables()
  {
  
    //Uniforms
    int n = _nativeGL.getProgramiv(this, GLVariable.activeUniforms());
    for (int i = 0; i < n; i++)
    {
      Uniform u = _nativeGL.getActiveUniform(this, i);
      _uniforms.add(u);
    }
  
    //Attributes
    n = _nativeGL.getProgramiv(this, GLVariable.activeAttributes());
    for (int i = 0; i < n; i++)
    {
      Attribute a = _nativeGL.getActiveAttribute(this, i);
      _attributes.add(a);
    }
  
  }


  public GPUProgram(INativeGL nativeGL, String vertexSource, String fragmentSource)
  {
  
    _programCreated = false;
  
    _nativeGL = nativeGL;
    _programID = _nativeGL.createProgram();
  
  
    // compile vertex shader
    int vertexShader = _nativeGL.createShader(ShaderType.VERTEX_SHADER);
    if (!compileShader(vertexShader, vertexSource))
    {
      ILogger.instance().logError("GPUProgram: ERROR compiling vertex shader\n");
      deleteShader(vertexShader);
      deleteProgram(_programID);
      return;
    }
  
    // compile fragment shader
    int fragmentShader = _nativeGL.createShader(ShaderType.FRAGMENT_SHADER);
    if (!compileShader(fragmentShader, fragmentSource))
    {
      ILogger.instance().logError("GPUProgram: ERROR compiling fragment shader\n");
      deleteShader(fragmentShader);
      deleteProgram(_programID);
      return;
    }
  
    _nativeGL.bindAttribLocation(this, 0, "Position");
  
    // link program
    if (!linkProgram())
    {
      ILogger.instance().logError("GPUProgram: ERROR linking graphic program\n");
      deleteShader(vertexShader);
      deleteShader(fragmentShader);
      deleteProgram(_programID);
      return;
    }
  
    // free shaders
    deleteShader(vertexShader);
    deleteShader(fragmentShader);
  
    getVariables();
  
    _programCreated = true; //Program fully created
  
  }

  public void dispose()
  {
  }

  public final int getProgramID()
  {
     return _programID;
  }
  public final boolean isCreated()
  {
     return _programCreated;
  }
  public final void deleteProgram(int p)
  {
    if (!_nativeGL.deleteProgram(p))
    {
      ILogger.instance().logError("GPUProgram: Problem encountered while deleting program.");
    }
    _programCreated = false;
  }

  public final Uniform getUniform(String name)
  {
    for (int i = 0; i < _uniforms.size(); i++)
    {
      Uniform u = _uniforms.get(i);
      if (u != null && name.equals(u.getName()))
      {
        return u;
      }
    }
    return null;
  }
  public final Attribute getAttribute(String name)
  {
    for (int i = 0; i < _uniforms.size(); i++)
    {
      Attribute a = _attributes.get(i);
      if (a != null && name.equals(a.getName()))
      {
        return a;
      }
    }
    return null;
  }


}