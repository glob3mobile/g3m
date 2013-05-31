

package org.glob3.mobile.generated;

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


   private void applyValuesToLinkedProgram() {
      final Object[] uni = _uniformValues.values().toArray();
      for (final Object element : uni) {
         ((GPUUniformValue) element).setValueToLinkedUniform();
      }

      final Object[] attEnabled = _attributesEnabled.values().toArray();
      for (final Object element : attEnabled) {
         final attributeEnabledStruct a = (attributeEnabledStruct) element;
         if (a.attribute == null) {
            ILogger.instance().logError("NO ATTRIBUTE LINKED");
         }
         else {
            a.attribute.setEnable(a.value);
         }
      }

      final Object[] att = _attributesValues.values().toArray();
      for (final Object element : att) {
         ((GPUAttributeValue) element).setValueToLinkedAttribute();
      }
   }

   private GPUProgram _lastProgramUsed;


   public GPUProgramState() {
      _lastProgramUsed = null;
   }


   public void dispose() {
      clear();
   }


   public final void clear() {
      _lastProgramUsed = null;
      _uniformValues.clear();

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


   public final void setUniformValue(final String name,
                                     final MutableMatrix44D m) {

      final Object[] uni = _uniformValues.values().toArray();
      final Object[] uniNames = _uniformValues.keySet().toArray();
      for (int i = 0; i < uni.length; i++) {
         final String thisName = (String) uniNames[i];
         final GPUUniformValue uv = (GPUUniformValue) uni[i];
         if ((thisName.compareTo(name) == 0) && (uv.getType() == GLType.glMatrix4Float())) {
            final GPUUniformValueMatrix4FloatStack v = (GPUUniformValueMatrix4FloatStack) uv;
            v.loadMatrix(m);
            return;
         }
      }

      setUniformValue(name, new GPUUniformValueMatrix4FloatStack(m));
   }


   public final void multiplyUniformValue(final String name,
                                          final MutableMatrix44D m) {

      final Object[] uni = _uniformValues.values().toArray();
      final Object[] uniNames = _uniformValues.keySet().toArray();
      for (int i = 0; i < uni.length; i++) {
         final String thisName = (String) uniNames[i];
         final GPUUniformValue uv = (GPUUniformValue) uni[i];
         if ((thisName.compareTo(name) == 0) && (uv.getType() == GLType.glMatrix4Float())) {
            final GPUUniformValueMatrix4FloatStack v = (GPUUniformValueMatrix4FloatStack) uv;
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


   public final void linkToProgram(final GPUProgram prog) {

      _lastProgramUsed = prog;


      _lastProgramUsed = prog;

      final Object[] uni = _uniformValues.values().toArray();
      final Object[] uniNames = _uniformValues.keySet().toArray();
      for (int i = 0; i < uni.length; i++) {
         final String name = (String) uniNames[i];
         final GPUUniformValue v = (GPUUniformValue) uni[i];

         final int type = v.getType();
         final GPUUniform u = prog.getUniformOfType(name, type);

         if (u == null) {
            ILogger.instance().logError("UNIFORM " + name + " NOT FOUND");
         }
         else {
            v.linkToGPUUniform(u);
         }
      }

      final Object[] attEnabled = _attributesEnabled.values().toArray();
      final Object[] attEnabledNames = _attributesEnabled.keySet().toArray();
      for (int i = 0; i < attEnabled.length; i++) {
         final String name = (String) attEnabledNames[i];
         final attributeEnabledStruct ae = (attributeEnabledStruct) attEnabled[i];

         final GPUAttribute a = prog.getGPUAttribute(name);
         if (a == null) {
            ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name + ". COULDN'T CHANGE ENABLED STATE.");
         }
         else {
            ae.attribute = a;
         }
      }

      final Object[] att = _attributesValues.values().toArray();
      final Object[] attNames = _attributesValues.keySet().toArray();
      for (int i = 0; i < att.length; i++) {
         final String name = (String) attNames[i];
         final GPUAttributeValue v = (GPUAttributeValue) att[i];

         final int type = v.getType();
         final int size = v.getAttributeSize();
         if ((type == GLType.glFloat()) && (size == 1)) {
            final GPUAttributeVec1Float a = prog.getGPUAttributeVec1Float(name);
            if (a == null) {
               ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
            }
            else {
               v.linkToGPUAttribute(a);
            }
            continue;
         }

         if ((type == GLType.glFloat()) && (size == 2)) {
            final GPUAttributeVec2Float a = prog.getGPUAttributeVec2Float(name);
            if (a == null) {
               ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
            }
            else {
               v.linkToGPUAttribute(a);
            }
            continue;
         }

         if ((type == GLType.glFloat()) && (size == 3)) {
            GPUAttribute a = prog.getGPUAttributeVec3Float(name);
            if (a == null) {
               a = prog.getGPUAttributeVec4Float(name); //A VEC3 COLUD BE STORED IN A VEC4 ATTRIBUTE
            }

            if (a == null) {
               ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
            }
            else {
               v.linkToGPUAttribute(a);
            }
            continue;
         }

         if ((type == GLType.glFloat()) && (size == 4)) {
            final GPUAttributeVec4Float a = prog.getGPUAttributeVec4Float(name);
            if (a == null) {
               ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
            }
            else {
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


   public final java.util.ArrayList<String> getUniformsNames() {
      final java.util.ArrayList<String> us = new java.util.ArrayList<String>();


      final Object[] uniNames = _uniformValues.keySet().toArray();
      for (final Object uniName : uniNames) {
         final String name = (String) uniName;
         us.add(name);
      }

      return us;
   }


   public final String description() {
      final String desc = "PROGRAM STATE\n==========\n";
      //TODO: IMPLEMENT
      return desc;
   }


   public final boolean isLinkableToProgram(final GPUProgram program) {
      if (program.getGPUAttributesNumber() != _attributesEnabled.size()) {
         return false;
      }

      int nDisabledAtt = 0;

      final Object[] attEnabled = _attributesEnabled.values().toArray();
      final Object[] attEnabledNames = _uniformValues.keySet().toArray();
      for (int i = 0; i < attEnabled.length; i++) {
         final String thisName = (String) attEnabledNames[i];
         final attributeEnabledStruct ae = (attributeEnabledStruct) attEnabled[i];

         if (ae.value == false) {
            nDisabledAtt++;
         }
         if (program.getGPUAttribute(thisName) == null) {
            return false;
         }
      }


      if (program.getGPUAttributesNumber() != (_attributesValues.size() + nDisabledAtt)) {
         return false;
      }

      if (program.getGPUUniformsNumber() != _uniformValues.size()) {
         return false;
      }


      final Object[] uniNames = _uniformValues.keySet().toArray();
      for (final Object uniName : uniNames) {
         final String thisName = (String) uniName;
         if (program.getGPUUniform(thisName) == null) {
            return false;
         }
      }

      final Object[] attNames = _attributesValues.keySet().toArray();
      for (final Object attName : attNames) {
         final String thisName = (String) attName;
         if (program.getGPUAttribute(thisName) == null) {
            return false;
         }
      }

      return true;
   }

}
