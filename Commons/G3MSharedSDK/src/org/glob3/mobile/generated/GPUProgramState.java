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




public class GPUProgramState
{

//  struct attributeEnabledStruct{
//    bool value;
//    mutable GPUAttribute* attribute;
//  };
  private java.util.HashMap<String, GPUUniformValue> _uniformValues = new java.util.HashMap<String, GPUUniformValue>();
  private java.util.HashMap<String, GPUAttributeValue> _attributesValues = new java.util.HashMap<String, GPUAttributeValue>();
//  std::map<std::string, attributeEnabledStruct> _attributesEnabled;

  private boolean setGPUUniformValue(String name, GPUUniformValue v)
  {
  
    GPUUniform prevLinkedUniform = null;
    boolean uniformExisted = false;
  
  
    GPUUniformValue pv = _uniformValues.get(name);
    if (pv != null){
      uniformExisted = true;
      prevLinkedUniform = pv.getLinkedUniform();
    }
  
    v.linkToGPUUniform(prevLinkedUniform);
    _uniformValues.put(name, v);
  
    if (!uniformExisted)
    {
      onStructureChanged();
    }
  
    return uniformExisted;
  }
  private boolean setGPUAttributeValue(String name, GPUAttributeValue v)
  {
    GPUAttribute prevLinkedAttribute = null;
    boolean attributeExisted = false;
    GPUAttributeValue pv = _attributesValues.get(name);
    if (pv != null){
      attributeExisted = true;
      prevLinkedAttribute = pv.getLinkedAttribute();
    }
  
    v.linkToGPUAttribute(prevLinkedAttribute);
    _attributesValues.put(name, v);
  
    if (!attributeExisted)
    {
      onStructureChanged();
    }
  
    return attributeExisted;
  }

  private java.util.ArrayList<String> _uniformNames;

  private GPUProgram _lastProgramUsed;

  private void onStructureChanged()
  {
    _uniformNames = null;
    _uniformNames = null;
    _lastProgramUsed = null;
  }


  public GPUProgramState()
  {
     _lastProgramUsed = null;
     _uniformNames = null;
  }

  public void dispose()
  {
    clear();
    _uniformNames = null;
  }

  public final void clear()
  {
    _lastProgramUsed = null;
    _uniformValues.clear();
  
    //  _attributesEnabled.clear();
    _attributesValues.clear();
  }

  public final boolean setUniformValue(String name, boolean b)
  {
    return setGPUUniformValue(name, new GPUUniformValueBool(b));
  }

  public final boolean setUniformValue(String name, float f)
  {
    return setGPUUniformValue(name, new GPUUniformValueFloat(f));
  }

  public final boolean setUniformValue(String name, Vector2D v)
  {
    return setGPUUniformValue(name, new GPUUniformValueVec2Float(v._x, v._y));
  }

  public final boolean setUniformValue(String name, double x, double y, double z, double w)
  {
    return setGPUUniformValue(name, new GPUUniformValueVec4Float(x,y,z,w));
  }

//  bool setUniformValue(const std::string& name, const MutableMatrix44D* m);
//  
//  bool multiplyUniformValue(const std::string& name, const MutableMatrix44D* m);


  //bool GPUProgramState::setUniformValue(const std::string& name, const MutableMatrix44D* m){
  //
  ///#ifdef C_CODE
  //  for(std::map<std::string, GPUUniformValue*> ::iterator it = _uniformValues.begin();
  //      it != _uniformValues.end();
  //      it++){
  //    std::string thisName = it->first;
  //    GPUUniformValue* uv = (GPUUniformValue*)it->second;
  //    if (thisName.compare(name) == 0 && uv->getType() == GLType::glMatrix4Float()){
  //      GPUUniformValueMatrix4FloatStack* v = (GPUUniformValueMatrix4FloatStack*)it->second;
  //      v->loadMatrix(m);
  //      return true;
  //    }
  //  }
  ///#endif
  ///#ifdef JAVA_CODE
  //  final Object[] uni = _uniformValues.values().toArray();
  //  final Object[] uniNames = _uniformValues.keySet().toArray();
  //  for (int i = 0; i < uni.length; i++) {
  //    final String thisName =  (String)uniNames[i];
  //    final GPUUniformValue uv = (GPUUniformValue) uni[i];
  //    if ((thisName.compareTo(name) == 0) && (uv.getType() == GLType.glMatrix4Float()))
  //    {
  //      final GPUUniformValueMatrix4FloatStack v = (GPUUniformValueMatrix4FloatStack)uv;
  //      v.loadMatrix(m);
  //      return true;
  //    }
  //  }
  ///#endif
  //
  //  return setGPUUniformValue(name, new GPUUniformValueMatrix4FloatStack(m));
  //}
  
  //bool GPUProgramState::multiplyUniformValue(const std::string& name, const MutableMatrix44D* m){
  //
  ///#ifdef C_CODE
  //
  //  for(std::map<std::string, GPUUniformValue*> ::iterator it = _uniformValues.begin();
  //      it != _uniformValues.end();
  //      it++){
  //    std::string thisName = it->first;
  //    GPUUniformValue* uv = (GPUUniformValue*)it->second;
  //    if (thisName.compare(name) == 0 && uv->getType() == GLType::glMatrix4Float()){
  //      GPUUniformValueMatrix4FloatStack* v = (GPUUniformValueMatrix4FloatStack*)it->second;
  //      v->multiplyMatrix(m);
  //      return true;
  //    }
  //  }
  //
  ///#endif
  ///#ifdef JAVA_CODE
  //  final Object[] uni = _uniformValues.values().toArray();
  //  final Object[] uniNames = _uniformValues.keySet().toArray();
  //  for (int i = 0; i < uni.length; i++) {
  //    final String thisName =  (String) uniNames[i];
  //    final GPUUniformValue uv = (GPUUniformValue) uni[i];
  //    if ((thisName.compareTo(name) == 0) && (uv.getType() == GLType.glMatrix4Float()))
  //    {
  //      final GPUUniformValueMatrix4FloatStack v = (GPUUniformValueMatrix4FloatStack)uv;
  //      v.multiplyMatrix(m);
  //      return;
  //    }
  //  }
  ///#endif
  //
  //  ILogger::instance()->logError("CAN'T MULTIPLY UNLOADED MATRIX");
  //  return false;
  //
  //}
  
  public final boolean setUniformMatrixValue(String name, MutableMatrix44D m, boolean isTransform)
  {
    GPUUniformValueMatrix4FloatTransform uv = new GPUUniformValueMatrix4FloatTransform(m, isTransform);
    return setGPUUniformValue(name, uv);
  }

  public final boolean setAttributeValue(String name, IFloatBuffer buffer, int attributeSize, int arrayElementSize, int index, boolean normalized, int stride)
  {
    switch (attributeSize)
    {
      case 1:
        return setGPUAttributeValue(name, new GPUAttributeValueVec1Float(buffer, arrayElementSize, index, stride, normalized));
      case 2:
        return setGPUAttributeValue(name, new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized));
      case 3:
        return setGPUAttributeValue(name, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized));
      case 4:
        return setGPUAttributeValue(name, new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized));
      default:
        ILogger.instance().logError("Invalid size for Attribute.");
        return false;
    }
  }

  public final void setAttributeEnabled(String name, boolean enabled)
  {
    //TODO: REMOVE FUNCTION
    if (!enabled)
    {
      setAttributeDisabled(name);
    }
  
    //  attributeEnabledStruct ae;
    //  ae.value = enabled;
    //  ae.attribute = NULL;
    //
    //  _attributesEnabled[name] = ae;
  }
  public final void setAttributeDisabled(String name)
  {
    setGPUAttributeValue(name, new GPUAttributeValueDisabled());
  }

  public final void applyChanges(GL gl)
  {
    if (_lastProgramUsed == null)
    {
      ILogger.instance().logError("Trying to use unlinked GPUProgramState.");
    }
    applyValuesToLinkedProgram();
    _lastProgramUsed.applyChanges(gl);
  }

  public final void linkToProgram(GPUProgram prog)
  {
  
    if (_lastProgramUsed == prog)
    {
      return; //Already linked
    }
  
  
    final Object[] uni = _uniformValues.values().toArray();
    final Object[] uniNames = _uniformValues.keySet().toArray();
    for (int i = 0; i < uni.length; i++) {
      final String name = (String)uniNames[i];
      final GPUUniformValue v = (GPUUniformValue) uni[i];
  
      final int type = v.getType();
      final GPUUniform u = prog.getUniformOfType(name, type); //Getting uniform from program
  
      if (u == null) {
        ILogger.instance().logError("UNIFORM " + name + " NOT FOUND");
        return;
      }
      v.linkToGPUUniform(u);
    }
  
    final Object[] att = _attributesValues.values().toArray();
    final Object[] attNames = _attributesValues.keySet().toArray();
    for (int i = 0; i < att.length; i++) {
      final String name = (String)attNames[i];
      final GPUAttributeValue v = (GPUAttributeValue)att[i];
  
      GPUAttribute a = null; //Getting attribute from program
      if (!v.getEnabled()){
        a = prog.getGPUAttribute(name);
      } else{
        final int type = v.getType();
        final int size = v.getAttributeSize();
        if (type==GLType.glFloat()){
          a = prog.getGPUAttributeVecXFloat(name,size);
        }
      }
  
      if (a == null){
        ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        return;
      }
      v.linkToGPUAttribute(a);
    }
  
  
  
    _lastProgramUsed = prog;
  }

  public final boolean isLinkedToProgram()
  {
    return _lastProgramUsed != null;
  }

  public final GPUProgram getLinkedProgram()
  {
    return _lastProgramUsed;
  }

  public final java.util.ArrayList<String> getUniformsNames()
  {
  
    if (_uniformNames == null)
    {
  
      _uniformNames = new java.util.ArrayList<String>();
  
  
      final Object[] uniNames = _uniformValues.keySet().toArray();
      for (int i = 0; i < uniNames.length; i++) {
        final String name = (String) uniNames[i];
        _uniformNames.add(name);
      }
  
    }
    return _uniformNames;
  }

  public final String description()
  {
    String desc = "PROGRAM STATE\n==========\n";
    //TODO: IMPLEMENT
    return desc;
  }

  public final boolean isLinkableToProgram(GPUProgram program)
  {
  
    if (program.getGPUAttributesNumber() != (_attributesValues.size())) {
      return false;
    }
  
    if (program.getGPUUniformsNumber() != _uniformValues.size()) {
      return false;
    }
  
  
    final Object[] uniNames = _uniformValues.keySet().toArray();
    for (int i = 0; i < uniNames.length; i++) {
      String thisName = (String) uniNames[i];
      if (program.getGPUUniform(thisName) == null) {
        return false;
      }
    }
  
    final Object[] attNames = _attributesValues.keySet().toArray();
    for (int i = 0; i < attNames.length; i++) {
      String thisName = (String) attNames[i];
      if (program.getGPUAttribute(thisName) == null) {
        return false;
      }
    }
  
    return true;
  }

  public final void applyValuesToLinkedProgram()
  {
    final Object[] uni = _uniformValues.values().toArray();
    for (int i = 0; i < uni.length; i++) {
      ((GPUUniformValue)uni[i]).setValueToLinkedUniform();
    }
  
    //  final Object[] attEnabled = _attributesEnabled.values().toArray();
    //  for (int i = 0; i < attEnabled.length; i++) {
    //    attributeEnabledStruct a = (attributeEnabledStruct) attEnabled[i];
    //    if (a.attribute == null) {
    //      ILogger.instance().logError("NO ATTRIBUTE LINKED");
    //    }
    //    else {
    //      a.attribute.setEnable(a.value);
    //    }
    //  }
  
    final Object[] att = _attributesValues.values().toArray();
    for (int i = 0; i < att.length; i++) {
      ((GPUAttributeValue)att[i]).setValueToLinkedAttribute();
    }
  }

}