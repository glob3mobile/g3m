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

//  std::map<GPUUniformKey, GPUUniformValue*> _uniformValues;
//  std::map<GPUAttributeKey, GPUAttributeValue*> _attributesValues;

  private GPUUniformValue[] _uniformValues = new GPUUniformValue[32];
  private GPUAttributeValue[] _attributeValues = new GPUAttributeValue[32];


  //void GPUProgramState::linkToProgram(GPUProgram* prog) const{
  //
  //  if (_linkedProgram == prog){
  //    return; //Already linked
  //  }
  //
  //  for (int i = 0; i < 32; i++) {
  //    GPUUniformValue* u = _uniformValues[i];
  //    if (u != NULL){
  //      if (!u->linkToGPUProgram(prog, i)){
  //        return;
  //      }
  //    }
  //    GPUAttributeValue* a = _attributeValues[i];
  //    if (a != NULL){
  //      if (!a->linkToGPUProgram(prog, i)){
  //        return;
  //      }
  //    }
  //  }
  //
  //  _linkedProgram = prog;
  //}
  
  //void GPUProgramState::applyChanges(GL* gl) const{
  //  if (_linkedProgram == NULL){
  //    ILogger::instance()->logError("Trying to use unlinked GPUProgramState.");
  //  }
  //  applyValuesToLinkedProgram();
  //  _linkedProgram->applyChanges(gl);
  //}
  
  private boolean setGPUUniformValue(GPUUniformKey key, GPUUniformValue v)
  {
  
    //  GPUUniform* prevLinkedUniform = NULL;
    boolean uniformExisted = false;
  
    final int index = key.getValue();
  
    GPUUniformValue u = _uniformValues[index];
    if (u != null)
    {
      //    prevLinkedUniform = u->getLinkedUniform();
      //    delete u;
      u._release();
      uniformExisted = true;
    }
  
    //  v->linkToGPUUniform(prevLinkedUniform);
    _uniformValues[index] = v;
  
    if (!uniformExisted)
    {
      onUniformsChanged();
    }
  
    return uniformExisted;
  }
  private boolean setGPUAttributeValue(GPUAttributeKey key, GPUAttributeValue v)
  {
    //  GPUAttribute* prevLinkedAttribute = NULL;
    boolean attributeExisted = false;
  
    final int index = key.getValue();
  
    GPUAttributeValue a = _attributeValues[index];
    if (a != null)
    {
      //    prevLinkedAttribute = a->getLinkedAttribute();
      a._release();
      attributeExisted = true;
    }
  
    //  v->linkToGPUAttribute(prevLinkedAttribute);
    _attributeValues[index] = v;
  
    if (!attributeExisted)
    {
      onAttributesChanged();
    }
  
    return attributeExisted;
  }

  private int _uniformsCode;
  private int _attributeCode;

  private int _highestUniformKey;
  private int _highestAttributeKey;

//  mutable std::vector<int>* _uniformKeys;
//  mutable std::vector<int>* _attributeKeys;

//  mutable GPUProgram* _linkedProgram;

//  void onStructureChanged();

  private void onUniformsChanged()
  {
    _uniformsCode = 0;
    for (int i = 0; i < 32; i++)
    {
      if (_uniformValues[i] != null)
      {
        _highestUniformKey = i;
      }
    }
  }
  private void onAttributesChanged()
  {
    _attributeCode = 0;
    for (int i = 0; i < 32; i++)
    {
      if (_attributeValues[i] != null)
      {
        _highestAttributeKey = i;
      }
    }
  }


  public GPUProgramState() //_linkedProgram(NULL),
//  _uniformKeys(NULL),
//  _attributeKeys(NULL),
  {
     _uniformsCode = 0;
     _attributeCode = 0;
     _highestUniformKey = 0;
     _highestAttributeKey = 0;
    for (int i = 0; i < 32; i++)
    {
      _uniformValues[i] = null;
      _attributeValues[i] = null;
    }
  }


  //void GPUProgramState::onStructureChanged(){
  ////  delete _uniformKeys;
  ////  _uniformKeys = NULL;
  //  //    _linkedProgram = NULL;
  //  _uniformsCode = 0;
  //  _attributeCode = 0;
  //
  ////  if (_attributeKeys != NULL){
  ////    delete _attributeKeys;
  ////    _attributeKeys = NULL;
  ////  }
  //
  //  _highestAttributeKey = 0;
  //  _highestUniformKey = 0;
  //  for (int i = 0; i < 32; i++) {
  //    if (_uniformValues[i] != NULL){
  //      _highestUniformKey = i;
  //    }
  //    if (_attributeValues[i] != NULL){
  //      _highestAttributeKey = i;
  //    }
  //  }
  //}
  
  public void dispose()
  {
    clear();
    //  delete _uniformKeys;
  }

  public final void clear()
  {
    //  _linkedProgram = NULL;
  
    for (int i = 0; i < 32; i++)
    {
      //delete _uniformValues[i];
      GPUUniformValue u = _uniformValues[i];
      if (u != null)
      {
        u._release();
        _uniformValues[i] = null;
      }
      //    delete _attributeValues[i];
      GPUAttributeValue a = _attributeValues[i];
      if (a != null)
      {
        a._release();
        _attributeValues[i] = null;
      }
    }
  }

  public final boolean setUniformValue(GPUUniformKey key, boolean b)
  {
    return setGPUUniformValue(key, new GPUUniformValueBool(b));
  }

  public final boolean setUniformValue(GPUUniformKey key, float f)
  {
    return setGPUUniformValue(key, new GPUUniformValueFloat(f));
  }

  public final boolean setUniformValue(GPUUniformKey key, Vector2D v)
  {
    return setGPUUniformValue(key, new GPUUniformValueVec2Float((float)v._x, (float)v._y));
  }

  public final boolean setUniformValue(GPUUniformKey key, float x, float y)
  {
    return setGPUUniformValue(key, new GPUUniformValueVec2Float(x, y));
  }

  public final boolean setUniformValue(GPUUniformKey key, float x, float y, float z, float w)
  {
    return setGPUUniformValue(key, new GPUUniformValueVec4Float(x,y,z,w));
  }

  public final boolean setUniformValue(GPUUniformKey key, Color color)
  {
    return setGPUUniformValue(key, new GPUUniformValueVec4Float(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
  }

//  bool setUniformMatrixValue(GPUUniformKey key, const MutableMatrix44D& m, bool isTransform);

  public final boolean setAttributeValue(GPUAttributeKey key, IFloatBuffer buffer, int attributeSize, int arrayElementSize, int index, boolean normalized, int stride)
  {
    switch (attributeSize)
    {
      case 1:
        return setGPUAttributeValue(key, new GPUAttributeValueVec1Float(buffer, arrayElementSize, index, stride, normalized));
      case 2:
        return setGPUAttributeValue(key, new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized));
      case 3:
        return setGPUAttributeValue(key, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized));
      case 4:
        return setGPUAttributeValue(key, new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized));
      default:
        ILogger.instance().logError("Invalid size for Attribute.");
        return false;
    }
  }

//  void setAttributeEnabled(GPUAttributeKey key, bool enabled);

  //bool GPUProgramState::setUniformMatrixValue(GPUUniformKey key, const MutableMatrix44D& m, bool isTransform){
  //  GPUUniformValueMatrix4FloatTransform *uv = new GPUUniformValueMatrix4FloatTransform(m, isTransform);
  //  return setGPUUniformValue(key, uv);
  //}
  
  //void GPUProgramState::setAttributeEnabled(GPUAttributeKey key, bool enabled){
  //  //TODO: REMOVE FUNCTION
  //  if (!enabled){
  //    setAttributeDisabled(key);
  //  }
  //}
  
  public final void setAttributeDisabled(GPUAttributeKey key)
  {
    setGPUAttributeValue(key, new GPUAttributeValueDisabled());
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void applyChanges(GL gl);

//  void linkToProgram(GPUProgram* prog) const;
//  
//  bool isLinkedToProgram() const{
//    return _linkedProgram != NULL;
//  }
//  
//  GPUProgram* getLinkedProgram() const{
//    return _linkedProgram;
//  }

//  std::vector<int>* getUniformsKeys() const;
//  std::vector<int>* getAttributeKeys() const;

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("PROGRAM STATE\n==========\n");
  
    for(int i = 0; i <= _highestUniformKey; i++)
    {
      GPUUniformValue v = _uniformValues[i];
      if (v != null)
      {
        isb.addString("Uniform ");
        isb.addInt(i);
        isb.addString(":\n");
        isb.addString(v.description() + "\n");
      }
    }
  
    for(int i = 0; i <= _highestAttributeKey; i++)
    {
      GPUAttributeValue v = _attributeValues[i];
      if (v != null)
      {
        isb.addString("Uniform ");
        isb.addInt(i);
        isb.addString(":\n");
        isb.addString(v.description() + "\n");
      }
    }
  
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

//  void applyValuesToLinkedProgram() const;

  public final boolean removeGPUUniformValue(GPUUniformKey key)
  {
  
    final int index = key.getValue();
  
    if (_uniformValues[index] != null)
    {
      _uniformValues[index]._release();
      _uniformValues[index] = null;
      onUniformsChanged();
      return true;
    }
    return false;
  }

  public final int getUniformsCode()
  {
    if (_uniformsCode == 0)
    {
      for (int i = 0; i <= _highestUniformKey; i++)
      {
        if (_uniformValues[i] != null)
        {
          _uniformsCode = _uniformsCode | GPUVariable.getUniformCode(i);
        }
      }
    }
    return _uniformsCode;
  }

  public final int getAttributesCode()
  {
    if (_attributeCode == 0)
    {
      for (int i = 0; i <= _highestAttributeKey; i++)
      {
        if (_attributeValues[i] != null)
        {
          _attributeCode = _attributeCode | GPUVariable.getAttributeCode(i);
        }
      }
    }
    return _attributeCode;
  }


  /*
   bool setUniformValue(const std::string& name, bool b){
   return setUniformValue(getKeyForName(name, UNIFORM), b);
   }
   
   bool setUniformValue(const std::string& name, float f){
   return setUniformValue(getKeyForName(name, UNIFORM), f);
   }
   
   bool setUniformValue(const std::string& name, const Vector2D& v){
   return setUniformValue(getKeyForName(name, UNIFORM), v);
   }
   
   bool setUniformValue(const std::string& name, double x, double y){
   return setUniformValue(getKeyForName(name, UNIFORM), x, y);
   }
   
   bool setUniformValue(const std::string& name, double x, double y, double z, double w){
   return setUniformValue(getKeyForName(name, UNIFORM), x, y, z, w);
   }
   
   bool setUniformMatrixValue(const std::string& name, const MutableMatrix44D& m, bool isTransform){
   return setUniformMatrixValue(getKeyForName(name, UNIFORM), m, isTransform);
   }
   
   bool setAttributeValue(const std::string& name,
   IFloatBuffer* buffer, int attributeSize,
   int arrayElementSize, int index, bool normalized, int stride){
   return setAttributeValue(getKeyForName(name, ATTRIBUTE),
   buffer, attributeSize,
   arrayElementSize, index, normalized, stride);
   }
   
   void setAttributeEnabled(const std::string& name, bool enabled){
   setAttributeEnabled(getKeyForName(name, ATTRIBUTE), enabled);
   }
   void setAttributeDisabled(const std::string& name){
   setAttributeDisabled(getKeyForName(name, ATTRIBUTE));
   }
   */


  //void GPUProgramState::applyValuesToLinkedProgram() const{
  //
  //  for (int i = 0; i < 32; i++) {
  //    GPUUniformValue* u = _uniformValues[i];
  //    if (u != NULL){
  //      u->setValueToLinkedUniform();
  //    }
  //    GPUAttributeValue* a = _attributeValues[i];
  //    if (a != NULL){
  //      a->setValueToLinkedAttribute();
  //    }
  //  }
  //}
  
  public final void applyValuesToProgram(GPUProgram prog)
  {
  
    for (int i = 0; i <= _highestUniformKey; i++)
    {
      GPUUniformValue u = _uniformValues[i];
      if (u != null)
      {
        prog.setGPUUniformValue(i, u);
      }
    }
  
    for (int i = 0; i <= _highestAttributeKey; i++)
    {
      GPUAttributeValue a = _attributeValues[i];
      if (a != null)
      {
        prog.setGPUAttributeValue(i, a);
      }
    }
  
    //  for (int i = 0; i < 32; i++) {
    //    GPUUniformValue* u = _uniformValues[i];
    //    if (u != NULL){
    //      prog->setGPUUniformValue(i, u);
    //    }
    //    GPUAttributeValue* a = _attributeValues[i];
    //    if (a != NULL){
    //      prog->setGPUAttributeValue(i, a);
    //    }
    //  }
  }

}