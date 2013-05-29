

package org.glob3.mobile.generated;

import java.util.Iterator;
import java.util.Map;
//
//  GPUProgramState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//


//
//  GPUProgramState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//


public class GPUProgramState {

   private static class attributeEnabledStruct {
      public boolean      value;
      public GPUAttribute attribute;
   }

   private final java.util.HashMap<String, GPUUniformValue>        _uniformValues     = new java.util.HashMap<String, GPUUniformValue>();
   private final java.util.HashMap<String, GPUAttributeValue>      _attributesValues  = new java.util.HashMap<String, GPUAttributeValue>();
   private final java.util.HashMap<String, attributeEnabledStruct> _attributesEnabled = new java.util.HashMap<String, attributeEnabledStruct>();


   private void setUniformValue(final String name,
                                final GPUUniformValue v) {
      _uniformValues.put(name, v);
   }


   private void setAttributeValue(final String name,
                                  final GPUAttributeValue v) {
      _attributesValues.put(name, v);
   }


   private void applyValuesToLinkedProgram()
  {
     {
     final Iterator it = _uniformValues.entrySet().iterator();
     while (it.hasNext()) {
         final Map.Entry pairs = (Map.Entry)it.next();
         final GPUUniformValue v = (GPUUniformValue) pairs.getValue();
         v.setValueToLinkedUniform();
     }
     }
     
     {
     final Iterator it = _attributesEnabled.entrySet().iterator();
     while (it.hasNext()) {
         final Map.Entry pairs = (Map.Entry)it.next();
         final attributeEnabledStruct a = (attributeEnabledStruct) pairs.getValue();
         if (a.attribute == null) {
          ILogger.instance().logError("NO ATTRIBUTE LINKED");
     }
     else
     {
       a.setEnable(it.second.value);
     }
         v.setValueToLinkedUniform();
     }
     }
     
     
    for(final java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
      final String name = it.first;
      final GPUUniformValue v = it.second;
      v.setValueToLinkedUniform();
    }
  
    for(final java.util.HashMap<String, attributeEnabledStruct> const_iterator it = _attributesEnabled.iterator(); it != _attributesEnabled.end(); it++)
    {
      final GPUAttribute a = it.second.attribute;
      if (a == null)
      {
        ILogger.instance().logError("NO ATTRIBUTE LINKED");
      }
      else
      {
        a.setEnable(it.second.value);
      }
    }
  
    for(final java.util.HashMap<String, GPUAttributeValue> const_iterator it = _attributesValues.iterator(); it != _attributesValues.end(); it++)
    {
      final GPUAttributeValue v = it.second;
      v.setValueToLinkedAttribute();
    }
  }

   private GPUProgram _lastProgramUsed;


   public GPUProgramState() {
      _lastProgramUsed = null;
   }


   public void dispose() {
      clear();
   }


   public final void clear()
  {
    _lastProgramUsed = null;
  
    for(final java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
      it.second = null;
    }
    _uniformValues.clear();
  
    for(final java.util.HashMap<String, GPUAttributeValue> const_iterator it = _attributesValues.iterator(); it != _attributesValues.end(); it++)
    {
      it.second = null;
    }
    _attributesEnabled.clear();
    _attributesValues.clear();
  }


   public final void setUniformValue(final String name,
                                     final boolean b) {
      setUniformValue(name, new GPUUniformValueBool(b));
   }


   public final void setUniformValue(final String name,
                                     final float f) {
      setUniformValue(name, new GPUUniformValueFloat(f));
   }


   public final void setUniformValue(final String name,
                                     final Vector2D v) {
      setUniformValue(name, new GPUUniformValueVec2Float(v._x, v._y));
   }


   public final void setUniformValue(final String name,
                                     final double x,
                                     final double y,
                                     final double z,
                                     final double w) {
      setUniformValue(name, new GPUUniformValueVec4Float(x, y, z, w));
   }


   public final void setUniformValue(final String name, final MutableMatrix44D m)
  {
  
    for(final java.util.HashMap<String, GPUUniformValue> iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
      final String thisName = it.first;
      final GPUUniformValue uv = (GPUUniformValue)it.second;
      if ((thisName.compareTo(name) == 0) && (uv.getType() == GLType.glMatrix4Float()))
      {
        final GPUUniformValueMatrix4FloatStack v = (GPUUniformValueMatrix4FloatStack)it.second;
        v.loadMatrix(m);
        return;
      }
    }
  
    setUniformValue(name, new GPUUniformValueMatrix4FloatStack(m));
  }


   public final void multiplyUniformValue(final String name, final MutableMatrix44D m)
  {
  
    for(final java.util.HashMap<String, GPUUniformValue> iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
      final String thisName = it.first;
      final GPUUniformValue uv = (GPUUniformValue)it.second;
      if ((thisName.compareTo(name) == 0) && (uv.getType() == GLType.glMatrix4Float()))
      {
        final GPUUniformValueMatrix4FloatStack v = (GPUUniformValueMatrix4FloatStack)it.second;
        v.multiplyMatrix(m);
        return;
      }
    }
  
    ILogger.instance().logError("CAN'T MULTIPLY UNLOADED MATRIX");
  
  }


   public final void setAttributeValue(final String name,
                                       final IFloatBuffer buffer,
                                       final int attributeSize,
                                       final int arrayElementSize,
                                       final int index,
                                       final boolean normalized,
                                       final int stride) {
      switch (attributeSize) {
         case 1:
            setAttributeValue(name, new GPUAttributeValueVec1Float(buffer, arrayElementSize, index, stride, normalized));
            break;

         case 2:
            setAttributeValue(name, new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized));
            break;

         case 3:
            setAttributeValue(name, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized));
            break;

         case 4:
            setAttributeValue(name, new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized));
            break;

         default:
            ILogger.instance().logError("Invalid size for Attribute.");
            break;
      }
   }


   public final void setAttributeEnabled(final String name,
                                         final boolean enabled) {
      final attributeEnabledStruct ae = new attributeEnabledStruct();
      ae.value = enabled;
      ae.attribute = null;

      _attributesEnabled.put(name, ae);
   }


   public final void applyChanges(final GL gl) {
      if (_lastProgramUsed == null) {
         ILogger.instance().logError("Trying to use unlinked GPUProgramState.");
      }
      applyValuesToLinkedProgram();
      _lastProgramUsed.applyChanges(gl);
   }


   public final void linkToProgram(final GPUProgram prog)
  {
  
    _lastProgramUsed = prog;
  
    for(final java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
  
      final String name = it.first;
      final GPUUniformValue v = it.second;
  
      GPUUniform u = null;
      final int type = v.getType();
      if (type == GLType.glBool())
      {
        u = prog.getGPUUniformBool(name);
      }
      else
      {
        if (type == GLType.glVec2Float())
        {
          u = prog.getGPUUniformVec2Float(name);
        }
        else
        {
          if (type == GLType.glVec4Float())
          {
            u = prog.getGPUUniformVec4Float(name);
          }
          else
          {
            if (type == GLType.glFloat())
            {
              u = prog.getGPUUniformFloat(name);
            }
            else
              if (type == GLType.glMatrix4Float())
              {
                u = prog.getGPUUniformMatrix4Float(name);
              }
          }
        }
      }
  
      if (u == null)
      {
        ILogger.instance().logError("UNIFORM " + name + " NOT FOUND");
      }
      else
      {
        v.linkToGPUUniform(u);
      }
    }
  
    for(final java.util.HashMap<String, attributeEnabledStruct> const_iterator it = _attributesEnabled.iterator(); it != _attributesEnabled.end(); it++)
    {
      final String name = it.first;
      final GPUAttribute a = prog.getGPUAttribute(name);
      if (a == null)
      {
        ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name + ". COULDN'T CHANGE ENABLED STATE.");
      }
      else
      {
        it.second.attribute = a;
      }
    }
  
    for(final java.util.HashMap<String, GPUAttributeValue> const_iterator it = _attributesValues.iterator(); it != _attributesValues.end(); it++)
    {
  
      final String name = it.first;
      final GPUAttributeValue v = it.second;
  
      final int type = v.getType();
      final int size = v.getAttributeSize();
      if ((type == GLType.glFloat()) && (size == 1))
      {
        final GPUAttributeVec1Float a = prog.getGPUAttributeVec1Float(name);
        if (a == null)
        {
          ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        }
        else
        {
          v.linkToGPUAttribute(a);
        }
        continue;
      }
  
      if ((type == GLType.glFloat()) && (size == 2))
      {
        final GPUAttributeVec2Float a = prog.getGPUAttributeVec2Float(name);
        if (a == null)
        {
          ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        }
        else
        {
          v.linkToGPUAttribute(a);
        }
        continue;
      }
  
      if ((type == GLType.glFloat()) && (size == 3))
      {
        GPUAttribute a = prog.getGPUAttributeVec3Float(name);
        if (a == null)
        {
          a = prog.getGPUAttributeVec4Float(name); //A VEC3 COLUD BE STORED IN A VEC4 ATTRIBUTE
        }
  
        if (a == null)
        {
          ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        }
        else
        {
          v.linkToGPUAttribute(a);
        }
        continue;
      }
  
      if ((type == GLType.glFloat()) && (size == 4))
      {
        final GPUAttributeVec4Float a = prog.getGPUAttributeVec4Float(name);
        if (a == null)
        {
          ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        }
        else
        {
          v.linkToGPUAttribute(a);
        }
        continue;
      }
  
    }
  }


   public final boolean isLinkedToProgram() {
      return _lastProgramUsed != null;
   }


   public final GPUProgram getLinkedProgram() {
      return _lastProgramUsed;
   }


   public final java.util.ArrayList<String> getUniformsNames()
  {
    final java.util.ArrayList<String> us = new java.util.ArrayList<String>();
  
      for(final java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++) //state->
      {
        us.add(it.first);
      }
  
    return us;
  }


   public final String description()
  {
    String desc = "PROGRAM STATE\n==========\n";
  
    for(final java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
  
      final String name = it.first;
      final GPUUniformValue v = it.second;
  
      desc += "Uniform " + name + ":\n";
      desc += v.description() + "\n";
    }
  
    for(final java.util.HashMap<String, attributeEnabledStruct> const_iterator it = _attributesEnabled.iterator(); it != _attributesEnabled.end(); it++)
    {
      final String name = it.first;
      desc += "Attribute " + name;
      if (it.second.value) {
         desc += " ENABLED\n";
      }
      else {
         desc += " ENABLED\n";
      }
    }
  
    for(final java.util.HashMap<String, GPUAttributeValue> const_iterator it = _attributesValues.iterator(); it != _attributesValues.end(); it++)
    {
  
      final String name = it.first;
      final GPUAttributeValue v = it.second;
  
      desc += "Attribute " + name + ":\n";
      desc += v.description() + "\n";
    }
  
    return desc;
  }


   public final boolean isLinkableToProgram(final GPUProgram program)
  {
  
    if (program.getGPUAttributesNumber() != _attributesEnabled.size())
    {
      return false;
    }
  
    int nDisabledAtt = 0;
    for(final java.util.HashMap<String, attributeEnabledStruct> const_iterator it = _attributesEnabled.iterator(); it != _attributesEnabled.end(); it++)
    {
      if (it.second.value == false)
      {
        nDisabledAtt++;
      }
      if (program.getGPUAttribute(it.first) == null)
      {
        return false;
      }
    }
  
    if (program.getGPUAttributesNumber() != (_attributesValues.size() + nDisabledAtt))
    {
      return false;
    }
  
    if (program.getGPUUniformsNumber() != _uniformValues.size())
    {
      return false;
    }
  
    for(final java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
      if (program.getGPUUniform(it.first) == null)
      {
        return false;
      }
    }
  
    for(final java.util.HashMap<String, GPUAttributeValue> const_iterator it = _attributesValues.iterator(); it != _attributesValues.end(); it++)
    {
      if (program.getGPUAttribute(it.first) == null)
      {
        return false;
      }
    }
  
    return true;
  }
}
