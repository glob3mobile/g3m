package org.glob3.mobile.generated; 
public class GPUProgram
{
  private int _programID;

  private GPUUniform[] _uniforms = new GPUUniform[32];
  private GPUAttribute[] _attributes = new GPUAttribute[32];
  private int _nAttributes;
  private int _nUniforms;

  private GPUUniform[] _createdUniforms;
  private GPUAttribute[] _createdAttributes;

  private int _uniformsCode;
  private int _attributesCode;

  private String _name;

  private GL _gl;

  private int _nReferences; //Number of items that reference this Program

  private boolean compileShader(GL gl, int shader, String source)
  {
    boolean result = gl.compileShader(shader, source);
  
    ///#if defined(DEBUG)
    //  _nativeGL->printShaderInfoLog(shader);
    ///#endif
  
    if (result)
    {
      gl.attachShader(_programID, shader);
    }
    else
    {
      ILogger.instance().logError("GPUProgram: Problem encountered while compiling shader.");
    }
  
    return result;
  }
  private boolean linkProgram(GL gl)
  {
    boolean result = gl.linkProgram(_programID);
    ///#if defined(DEBUG)
    //  _nativeGL->printProgramInfoLog(_programID);
    ///#endif
    return result;
  }
  private void deleteShader(GL gl, int shader)
  {
    if (!gl.deleteShader(shader))
    {
      ILogger.instance().logError("GPUProgram: Problem encountered while deleting shader.");
    }
  }
  private void deleteProgram(GL gl, GPUProgram p)
  {
    if (!gl.deleteProgram(p))
    {
      ILogger.instance().logError("GPUProgram: Problem encountered while deleting program.");
    }
  }

  private void getVariables(GL gl)
  {
  
    for (int i = 0; i < 32; i++)
    {
      _uniforms[i] = null;
      _attributes[i] = null;
    }
  
    //Uniforms
    _uniformsCode = 0;
    _nUniforms = gl.getProgramiv(this, GLVariable.activeUniforms());
  
    int counter = 0;
    _createdUniforms = new GPUUniform[_nUniforms];
  
    for (int i = 0; i < _nUniforms; i++)
    {
      GPUUniform u = gl.getActiveUniform(this, i);
      if (u != null)
      {
        _uniforms[u.getIndex()] = u;
  
        final int code = GPUVariable.getUniformCode(u._key);
        _uniformsCode = _uniformsCode | code;
      }
  
      _createdUniforms[counter++] = u; //Adding to created uniforms array
    }
  
    //Attributes
    _attributesCode = 0;
    _nAttributes = gl.getProgramiv(this, GLVariable.activeAttributes());
  
    counter = 0;
    _createdAttributes = new GPUAttribute[_nAttributes];
  
    for (int i = 0; i < _nAttributes; i++)
    {
      GPUAttribute a = gl.getActiveAttribute(this, i);
      if (a != null)
      {
        _attributes[a.getIndex()] = a;
  
        final int code = GPUVariable.getAttributeCode(a._key);
        _attributesCode = _attributesCode | code;
      }
  
      _createdAttributes[counter++] = a;
    }
  
    //ILogger::instance()->logInfo("Program with Uniforms Bitcode: %d and Attributes Bitcode: %d", _uniformsCode, _attributesCode);
  }

  private GPUProgram()
  {
     _createdAttributes = null;
     _createdUniforms = null;
     _nUniforms = 0;
     _nAttributes = 0;
     _uniformsCode = 0;
     _attributesCode = 0;
     _gl = null;
     _nReferences = 0;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GPUProgram(GPUProgram that);



  public void dispose()
  {
  
    //ILogger::instance()->logInfo("Deleting program %s", _name.c_str());
  
    //  if (_manager != NULL) {
    //    _manager->compiledProgramDeleted(this->_name);
    //  }
  
    for (int i = 0; i < _nUniforms; i++)
    {
      if (_createdUniforms[i] != null)
         _createdUniforms[i].dispose();
    }
  
    for (int i = 0; i < _nAttributes; i++)
    {
      if (_createdAttributes[i] != null)
         _createdAttributes[i].dispose();
    }
  
    _createdAttributes = null;
    _createdUniforms = null;
  
    if (!_gl.deleteProgram(this))
    {
      ILogger.instance().logError("GPUProgram: Problem encountered while deleting program.");
    }
  }

  public static GPUProgram createProgram(GL gl, String name, String vertexSource, String fragmentSource)
  {
  
    GPUProgram p = new GPUProgram();
  
    p._name = name;
    p._programID = gl.createProgram();
    p._gl = gl;
  
    // compile vertex shader
    int vertexShader = gl.createShader(ShaderType.VERTEX_SHADER);
    if (!p.compileShader(gl, vertexShader, vertexSource))
    {
      ILogger.instance().logError("GPUProgram: ERROR compiling vertex shader :\n %s\n", vertexSource);
      gl.printShaderInfoLog(vertexShader);
  
      p.deleteShader(gl, vertexShader);
      p.deleteProgram(gl, p);
      return null;
    }
  
    //  ILogger::instance()->logInfo("VERTEX SOURCE: \n %s", vertexSource.c_str());
  
    // compile fragment shader
    int fragmentShader = gl.createShader(ShaderType.FRAGMENT_SHADER);
    if (!p.compileShader(gl, fragmentShader, fragmentSource))
    {
      ILogger.instance().logError("GPUProgram: ERROR compiling fragment shader :\n %s\n", fragmentSource);
      gl.printShaderInfoLog(fragmentShader);
  
      p.deleteShader(gl, fragmentShader);
      p.deleteProgram(gl, p);
      return null;
    }
  
    //  ILogger::instance()->logInfo("FRAGMENT SOURCE: \n %s", fragmentSource.c_str());
  
    //gl->bindAttribLocation(p, 0, POSITION);
  
    // link program
    if (!p.linkProgram(gl))
    {
      ILogger.instance().logError("GPUProgram: ERROR linking graphic program\n");
      p.deleteShader(gl, vertexShader);
      p.deleteShader(gl, fragmentShader);
      p.deleteProgram(gl, p);
      ILogger.instance().logError("GPUProgram: ERROR linking graphic program");
      return null;
    }
  
    //Mark shaders for deleting when program is deleted
    p.deleteShader(gl, vertexShader);
    p.deleteShader(gl, fragmentShader);
  
    p.getVariables(gl);
  
    if (gl.getError() != GLError.noError())
    {
      ILogger.instance().logError("Error while compiling program");
    }
  
    return p;
  }

  public final String getName()
  {
     return _name;
  }

  public final int getProgramID()
  {
     return _programID;
  }

  public final int getGPUAttributesNumber()
  {
     return _nAttributes;
  }
  public final int getGPUUniformsNumber()
  {
     return _nUniforms;
  }

  public final GPUUniform getGPUUniform(String name)
  {
    final int key = GPUVariable.getUniformKey(name).getValue();
    return _uniforms[key];
  }
  public final GPUAttribute getGPUAttribute(String name)
  {
    final int key = GPUVariable.getAttributeKey(name).getValue();
    return _attributes[key];
  }

  public final GPUUniformBool getGPUUniformBool(String name)
  {
    GPUUniform u = getGPUUniform(name);
    if (u!= null && u._type == GLType.glBool())
    {
      return (GPUUniformBool)u;
    }
    return null;
  }
  public final GPUUniformVec2Float getGPUUniformVec2Float(String name)
  {
    GPUUniform u = getGPUUniform(name);
    if (u!= null && u._type == GLType.glVec2Float())
    {
      return (GPUUniformVec2Float)u;
    }
    return null;
  }
  public final GPUUniformVec4Float getGPUUniformVec4Float(String name)
  {
    GPUUniform u = getGPUUniform(name);
    if (u!= null && u._type == GLType.glVec4Float())
    {
      return (GPUUniformVec4Float)u;
    }
    return null;
  }
  public final GPUUniformFloat getGPUUniformFloat(String name)
  {
    GPUUniform u = getGPUUniform(name);
    if (u!= null && u._type == GLType.glFloat())
    {
      return (GPUUniformFloat)u;
    }
    return null;
  }
  public final GPUUniformMatrix4Float getGPUUniformMatrix4Float(String name)
  {
    GPUUniform u = getGPUUniform(name);
    if (u!= null && u._type == GLType.glMatrix4Float())
    {
      return (GPUUniformMatrix4Float)u;
    }
    return null;
  
  }

  public final GPUAttribute getGPUAttributeVecXFloat(String name, int x)
  {
    switch (x)
    {
      case 1:
        return getGPUAttributeVec1Float(name);
      case 2:
        return getGPUAttributeVec2Float(name);
      case 3:
        return getGPUAttributeVec3Float(name);
      case 4:
        return getGPUAttributeVec4Float(name);
      default:
        return null;
    }
  }
  public final GPUAttributeVec1Float getGPUAttributeVec1Float(String name)
  {
    GPUAttributeVec1Float a = (GPUAttributeVec1Float)getGPUAttribute(name);
    if (a!= null && a._size == 1 && a._type == GLType.glFloat())
    {
      return (GPUAttributeVec1Float)a;
    }
    return null;
  
  }
  public final GPUAttributeVec2Float getGPUAttributeVec2Float(String name)
  {
    GPUAttributeVec2Float a = (GPUAttributeVec2Float)getGPUAttribute(name);
    if (a!= null && a._size == 2 && a._type == GLType.glFloat())
    {
      return (GPUAttributeVec2Float)a;
    }
    return null;
  
  }
  public final GPUAttributeVec3Float getGPUAttributeVec3Float(String name)
  {
    GPUAttributeVec3Float a = (GPUAttributeVec3Float)getGPUAttribute(name);
    if (a!= null && a._size == 3 && a._type == GLType.glFloat())
    {
      return (GPUAttributeVec3Float)a;
    }
    return null;
  
  }
  public final GPUAttributeVec4Float getGPUAttributeVec4Float(String name)
  {
    GPUAttributeVec4Float a = (GPUAttributeVec4Float)getGPUAttribute(name);
    if (a!= null && a._size == 4 && a._type == GLType.glFloat())
    {
      return (GPUAttributeVec4Float)a;
    }
    return null;
  
  }


  /**
   Must be called when the program is used
   */
  public final void onUsed()
  {
    //  ILogger::instance()->logInfo("GPUProgram %s being used", _name.c_str());
  }
  /**
   Must be called when the program is no longer used
   */
  public final void onUnused(GL gl)
  {
    //ILogger::instance()->logInfo("GPUProgram %s unused", _name.c_str());
  
    for (int i = 0; i < _nUniforms; i++)
    {
      if (_createdUniforms[i] != null) //Texture Samplers return null
      {
        _createdUniforms[i].unset();
      }
    }
  
    for (int i = 0; i < _nAttributes; i++)
    {
      if (_createdAttributes[i] != null)
      {
        _createdAttributes[i].unset(gl);
      }
    }
  }

  /**
   Must be called before drawing to apply Uniforms and Attributes new values
   */
  public final void applyChanges(GL gl)
  {
  
    for (int i = 0; i < _nUniforms; i++)
    {
      GPUUniform uniform = _createdUniforms[i];
      if (uniform != null) //Texture Samplers return null
      {
        uniform.applyChanges(gl);
      }
    }
  
    for (int i = 0; i < _nAttributes; i++)
    {
      GPUAttribute attribute = _createdAttributes[i];
      if (attribute != null)
      {
        attribute.applyChanges(gl);
      }
    }
  }

  public final GPUUniform getUniformOfType(String name, int type)
  {
    GPUUniform u = null;
    if (type == GLType.glBool())
    {
      u = getGPUUniformBool(name);
    }
    else if (type == GLType.glVec2Float())
    {
      u = getGPUUniformVec2Float(name);
    }
    else if (type == GLType.glVec4Float())
    {
      u = getGPUUniformVec4Float(name);
    }
    else if (type == GLType.glFloat())
    {
      u = getGPUUniformFloat(name);
    }
    else if (type == GLType.glMatrix4Float())
    {
      u = getGPUUniformMatrix4Float(name);
    }
    else
    {
      ILogger.instance().logError("Invalid uniform type");
    }
  
    return u;
  }

  public final GPUUniform getGPUUniform(int key)
  {
    return _uniforms[key];
  }
  public final GPUAttribute getGPUAttribute(int key)
  {
    return _attributes[key];
  }
  public final GPUAttribute getGPUAttributeVecXFloat(int key, int x)
  {
    GPUAttribute a = getGPUAttribute(key);
    if (a._type == GLType.glFloat() && a._size == x)
    {
      return a;
    }
    return null;
  }

  public final int getAttributesCode()
  {
     return _attributesCode;
  }
  public final int getUniformsCode()
  {
     return _uniformsCode;
  }

  public final void setGPUUniformValue(int key, GPUUniformValue v)
  {
    GPUUniform u = _uniforms[key];
    if (u == null)
    {
      ILogger.instance().logError("Uniform [key=%d] not found in program %s", key, _name);
      return;
    }
    u.set(v);
  }
  public final void setGPUAttributeValue(int key, GPUAttributeValue v)
  {
    GPUAttribute a = _attributes[key];
    if (a == null)
    {
      ILogger.instance().logError("Attribute [key=%d] not found in program %s", key, _name);
      return;
    }
    a.set(v);
  }

  public final void addReference()
  {
     ++_nReferences;
  }
  public final void removeReference()
  {
     --_nReferences;
  }
  public final int getNReferences()
  {
     return _nReferences;
  }

}