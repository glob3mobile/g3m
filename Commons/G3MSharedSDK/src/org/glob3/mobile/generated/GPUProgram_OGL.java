package org.glob3.mobile.generated; 
//
//  GPUProgram_OGL.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//

//
//  GPUProgram_OGL.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//



public class GPUProgram_OGL extends IGPUProgram
{

   public static GPUProgram_OGL createProgram(GL gl, String name, String vertexSource, String fragmentSource)
   {
   
      GPUProgram_OGL p = new GPUProgram_OGL();
   
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

   protected final void getVariables(GL gl)
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
   private void deleteProgram(GL gl, IGPUProgram p)
   {
      if (!gl.deleteProgram(p))
      {
         ILogger.instance().logError("GPUProgram: Problem encountered while deleting program.");
      }
   }



}