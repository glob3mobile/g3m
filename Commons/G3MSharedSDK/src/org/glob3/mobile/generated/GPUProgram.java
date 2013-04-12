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




//#include "G3MError.hpp"

//#include "G3MError.hpp"

//class IFloatBuffer;

//class GL;

public class GPUProgram
{

  //INativeGL* _nativeGL;
  private int _programID;
  private boolean _programCreated;
  private java.util.HashMap<String, Attribute> _attributes = new java.util.HashMap<String, Attribute>();
  private java.util.HashMap<String, GPUUniform> _uniforms = new java.util.HashMap<String, GPUUniform>();
  private String _name;

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

  private void getVariables(GL gl)
  {
  
    //Uniforms
    int n = gl.getProgramiv(this, GLVariable.activeUniforms());
    for (int i = 0; i < n; i++)
    {
      GPUUniform u = gl.getActiveUniform(this, i);
      if (u != null)
         _uniforms.put(u.getName(), u);
    }
  
    //Attributes
    n = gl.getProgramiv(this, GLVariable.activeAttributes());
    for (int i = 0; i < n; i++)
    {
      Attribute a = gl.getActiveAttribute(this, i);
      if (a != null)
         _attributes.put(a.getName(), a);
    }
  
  }

  private GPUProgram()
  {
  }

  private GPUUniform getUniform(String name)
  {
    java.util.HashMap<String, GPUUniform> const_iterator it = _uniforms.indexOf(name);
    if (it != _uniforms.end())
    {
      return it.second;
    }
    else
    {
      return null;
    }
  }
  //Uniform* getUniform(const std::string name) const;
  private Attribute getAttribute(String name)
  {
    java.util.HashMap<String, Attribute> const_iterator it = _attributes.indexOf(name);
    if (it != _attributes.end())
    {
      return it.second;
    }
    else
    {
      return null;
    }
  }



  //#include "G3MError.hpp"
  
  
  public static GPUProgram createProgram(GL gl, String name, String vertexSource, String fragmentSource)
  {
  
    GPUProgram p = new GPUProgram();
  
    p._name = name;
  
    p._programCreated = false;
    //p->_nativeGL = gl->getNative();
    p._programID = gl.createProgram();
  
    // compile vertex shader
    int vertexShader = gl.createShader(ShaderType.VERTEX_SHADER);
    if (!p.compileShader(gl, vertexShader, vertexSource))
    {
      ILogger.instance().logError("GPUProgram: ERROR compiling vertex shader\n");
      p.deleteShader(gl, vertexShader);
      p.deleteProgram(gl, p._programID);
      throw new G3MError("GPUProgram: ERROR compiling vertex shader");
      return null;
    }
  
    // compile fragment shader
    int fragmentShader = gl.createShader(ShaderType.FRAGMENT_SHADER);
    if (!p.compileShader(gl, fragmentShader, fragmentSource))
    {
      ILogger.instance().logError("GPUProgram: ERROR compiling fragment shader\n");
      p.deleteShader(gl, fragmentShader);
      p.deleteProgram(gl, p._programID);
      throw new G3MError("GPUProgram: ERROR compiling fragment shader");
      return null;
    }
  
    gl.bindAttribLocation(p, 0, "Position");
  
    // link program
    if (!p.linkProgram(gl))
    {
      ILogger.instance().logError("GPUProgram: ERROR linking graphic program\n");
      p.deleteShader(gl, vertexShader);
      p.deleteShader(gl, fragmentShader);
      p.deleteProgram(gl, p._programID);
      throw new G3MError("GPUProgram: ERROR linking graphic program");
      return null;
    }
  
    // free shaders
    p.deleteShader(gl, vertexShader);
    p.deleteShader(gl, fragmentShader);
  
    p.getVariables(gl);
  
    return p;
  }

  public void dispose()
  {
  }

  public final String getName()
  {
     return _name;
  }

  public final int getProgramID()
  {
     return _programID;
  }
  public final boolean isCreated()
  {
     return _programCreated;
  }
  public final void deleteProgram(GL gl, int p)
  {
    if (!gl.deleteProgram(p))
    {
      ILogger.instance().logError("GPUProgram: Problem encountered while deleting program.");
    }
    _programCreated = false;
  }

  public final GPUUniformBool getGPUUniformBool(String name)
  {
    GPUUniform u = getUniform(name);
    if (u!= null && u.getType() == GLType.glBool())
    {
      return (GPUUniformBool)u;
    }
    else
    {
      return null;
    }
  }
  public final GPUUniformVec2Float getGPUUniformVec2Float(String name)
  {
    GPUUniform u = getUniform(name);
    if (u!= null && u.getType() == GLType.glVec2Float())
    {
      return (GPUUniformVec2Float)u;
    }
    else
    {
      return null;
    }
  }
  public final GPUUniformVec4Float getGPUUniformVec4Float(String name)
  {
    GPUUniform u = getUniform(name);
    if (u!= null && u.getType() == GLType.glVec4Float())
    {
      return (GPUUniformVec4Float)u;
    }
    else
    {
      return null;
    }
  }
  public final GPUUniformFloat getGPUUniformFloat(String name)
  {
    GPUUniform u = getUniform(name);
    if (u!= null && u.getType() == GLType.glFloat())
    {
      return (GPUUniformFloat)u;
    }
    else
    {
      return null;
    }
  }
  public final GPUUniformMatrix4Float getGPUUniformMatrix4Float(String name)
  {
    GPUUniform u = getUniform(name);
    if (u!= null && u.getType() == GLType.glMatrix4Float())
    {
      return (GPUUniformMatrix4Float)u;
    }
    else
    {
      return null;
    }
  }


  public final AttributeVec1Float getAttributeVec1Float(String name)
  {
    AttributeVecFloat a = (AttributeVecFloat)getAttribute(name);
    if (a!= null && a.getSize() == 1)
    {
      return (AttributeVec1Float)a;
    }
    else
    {
      return null;
    }
  }
  public final AttributeVec2Float getAttributeVec2Float(String name)
  {
    AttributeVecFloat a = (AttributeVecFloat)getAttribute(name);
    if (a!= null && a.getSize() == 2)
    {
      return (AttributeVec2Float)a;
    }
    else
    {
      return null;
    }
  }
  public final AttributeVec3Float getAttributeVec3Float(String name)
  {
    AttributeVecFloat a = (AttributeVecFloat)getAttribute(name);
    if (a!= null && a.getSize() == 3)
    {
      return (AttributeVec3Float)a;
    }
    else
    {
      return null;
    }
  }
  public final AttributeVec4Float getAttributeVec4Float(String name)
  {
    AttributeVecFloat a = (AttributeVecFloat)getAttribute(name);
    if (a!= null && a.getSize() == 4)
    {
      return (AttributeVec4Float)a;
    }
    else
    {
      return null;
    }
  }


  /**
   Must be called when the program is used
   */
  public final void onUsed()
  {
    ILogger.instance().logInfo("GPUProgram %s being used", _name);
  }
  /**
   Must be called when the program is no longer used
   */
  public final void onUnused()
  {
      ILogger.instance().logInfo("GPUProgram %s unused", _name);
  }

  /**
   Must be called before drawing to apply Uniforms and Attributes new values
   */
  public final void applyChanges(GL gl)
  {
    //ILogger::instance()->logInfo("GPUProgram %s applying changes", _name.c_str());
  
    java.util.Iterator<String, GPUUniform> iter;
    for (iter = _uniforms.iterator(); iter.hasNext();)
    {
      iter.second.applyChanges(gl);
    }
  
    java.util.Iterator<String, Attribute> iter2;
    for (iter2 = _attributes.iterator(); iter2.hasNext();)
    {
      iter2.second.applyChanges(gl);
    }
  }
/*
  void setUniform(GL* gl, const std::string& name, const Vector2D& v) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glVec2Float()) {
      ((UniformVec2Float*)u)->set(gl, v);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  
  void setUniform(GL* gl, const std::string& name, double x, double y, double z, double w) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glVec4Float()) {
      ((UniformVec4Float*)u)->set(gl, x,y,z,w);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  
  void setUniform(GL* gl, const std::string& name, bool b) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glBool()) {
      ((UniformBool*)u)->set(gl, b);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  
  void setUniform(GL* gl, const std::string& name, float f) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glFloat()) {
      ((UniformFloat*)u)->set(gl, f);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  
  void setUniform(GL* gl, const std::string& name, const MutableMatrix44D& m) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glMatrix4Float()) {
      ((UniformMatrix4Float*)u)->set(gl, m);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  */


}