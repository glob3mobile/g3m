package org.glob3.mobile.generated; 
public abstract class IGPUProgram
{

   protected int _programID;

   protected IGPUProgram()
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
   protected GPUUniform[] _uniforms = new GPUUniform[32];
   protected GPUAttribute[] _attributes = new GPUAttribute[32];
   protected int _nAttributes;
   protected int _nUniforms;
   protected GPUUniform[] _createdUniforms;
   protected GPUAttribute[] _createdAttributes;

   protected int _uniformsCode;
   protected int _attributesCode;

   protected String _name;

   protected GL _gl;

   protected int _nReferences; //Number of items that reference this Program

   protected abstract void getVariables(GL gl);

   public void dispose()
   {
   }
   public final int getProgramID()
   {
      return _programID;
   }

   public void onUsed() //previous implementation did nothing...
   {
   }
   public abstract void onUnused(GL gl);
   public abstract void applyChanges(GL gl);
   public abstract void deleteShader(GL gl, int shader);
   public abstract void deleteProgram(GL gl, IGPUProgram p);

   public final String getName()
   {
      return _name;
   }
   public final int getGPUAttributesNumber()
   {
      return _nAttributes;
   }
   public final int getGPUUniformsNumber()
   {
      return _nUniforms;
   }


   //Getters for uniforms
   public final GPUUniform getGPUUniform(String name)
   {
   	final int key = GPUVariable.getUniformKey(name).getValue();
      return _uniforms[key];
   }

   //getters for Attributes
   public final GPUAttribute getGPUAttribute(String name)
   {
   	final int key = GPUVariable.getAttributeKey(name).getValue();
      return _attributes[key];
   }

   public final GPUUniformBool getGPUUniformBool(String name)
   {
      GPUUniform u = getGPUUniform(name);
      if (u != null && u._type == GLType.glBool())
      {
         return (GPUUniformBool)u;
      }
      return null;
   }
   public final GPUUniformVec2Float getGPUUniformVec2Float(String name)
   {
      GPUUniform u = getGPUUniform(name);
      if (u != null && u._type == GLType.glVec2Float())
      {
         return (GPUUniformVec2Float)u;
      }
      return null;
   }
   public final GPUUniformVec4Float getGPUUniformVec4Float(String name)
   {
      GPUUniform u = getGPUUniform(name);
      if (u != null && u._type == GLType.glVec4Float())
      {
         return (GPUUniformVec4Float)u;
      }
      return null;
   }
   public final GPUUniformFloat getGPUUniformFloat(String name)
   {
      GPUUniform u = getGPUUniform(name);
      if (u != null && u._type == GLType.glFloat())
      {
         return (GPUUniformFloat)u;
      }
      return null;
   }
   public final GPUUniformMatrix4Float getGPUUniformMatrix4Float(String name)
   {
      GPUUniform u = getGPUUniform(name);
      if (u != null && u._type == GLType.glMatrix4Float())
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
      if (a != null && a._size == 1 && a._type == GLType.glFloat())
      {
         return (GPUAttributeVec1Float)a;
      }
      return null;
   
   }
   public final GPUAttributeVec2Float getGPUAttributeVec2Float(String name)
   {
      GPUAttributeVec2Float a = (GPUAttributeVec2Float)getGPUAttribute(name);
      if (a != null && a._size == 2 && a._type == GLType.glFloat())
      {
         return (GPUAttributeVec2Float)a;
      }
      return null;
   
   }
   public final GPUAttributeVec3Float getGPUAttributeVec3Float(String name)
   {
      GPUAttributeVec3Float a = (GPUAttributeVec3Float)getGPUAttribute(name);
      if (a != null && a._size == 3 && a._type == GLType.glFloat())
      {
         return (GPUAttributeVec3Float)a;
      }
      return null;
   
   }
   public final GPUAttributeVec4Float getGPUAttributeVec4Float(String name)
   {
      GPUAttributeVec4Float a = (GPUAttributeVec4Float)getGPUAttribute(name);
      if (a != null && a._size == 4 && a._type == GLType.glFloat())
      {
         return (GPUAttributeVec4Float)a;
      }
      return null;
   
   }

   public final GPUUniform getUniformOfType(String name, int type)
   {
      GPUUniform u = null;
      if (type == GLType.glBool())
      {
         u = getGPUUniformBool(name);
      }
      else
      {
         if (type == GLType.glVec2Float())
         {
            u = getGPUUniformVec2Float(name);
         }
         else
         {
            if (type == GLType.glVec4Float())
            {
               u = getGPUUniformVec4Float(name);
            }
            else
            {
               if (type == GLType.glFloat())
               {
                  u = getGPUUniformFloat(name);
               }
               else
                  if (type == GLType.glMatrix4Float())
                  {
                  u = getGPUUniformMatrix4Float(name);
                  }
            }
         }
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
         ILogger.instance().logError("Uniform [key=%d] not found", key);
         return;
      }
      u.set(v);
   }
   public final void setGPUAttributeValue(int key, GPUAttributeValue v)
   {
      GPUAttribute a = _attributes[key];
      if (a == null)
      {
         ILogger.instance().logError("Attribute [key=%d] not found", key);
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