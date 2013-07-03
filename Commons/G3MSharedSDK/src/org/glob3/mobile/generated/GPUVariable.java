package org.glob3.mobile.generated; 
public class GPUVariable
{


  protected final GPUVariableType _variableType;
  protected final String _name;

  //Uniform metadata based in our shaders
//  long _key;
//  long _group;
//  long _priority;

//  void createMetadata();


//  static const int UNRECOGNIZED;
//  
//  static const int FLAT_COLOR;
//  static const int MODELVIEW;
//  static const int TEXTURE_EXTENT;
//  static const int VIEWPORT_EXTENT;
//  static const int TRANSLATION_TEXTURE_COORDS;
//  static const int SCALE_TEXTURE_COORDS;
//  static const int POINT_SIZE;
//  
//  static const int POSITION;
//  static const int TEXTURE_COORDS;
//  static const int COLOR;
//  
//  //To be deleted
//  static const int EnableColorPerVertex;
//  static const int EnableTexture;
//  static const int EnableFlatColor;
//  static const int FlatColorIntensity;
//  static const int ColorPerVertexIntensity;

//  static int getKeyForName(const std::string& name, GPUVariableType variableType);

  public static GPUUniformKey getUniformKey(String name)
  {
  
    if (name.compareTo("uFlatColor") == 0)
    {
      return GPUUniformKey.FLAT_COLOR;
    }
  
    if (name.compareTo("uModelview") == 0)
    {
      return GPUUniformKey.MODELVIEW;
    }
  
    if (name.compareTo("uTextureExtent") == 0)
    {
      return GPUUniformKey.TEXTURE_EXTENT;
    }
  
    if (name.compareTo("uViewPortExtent") == 0)
    {
      return GPUUniformKey.VIEWPORT_EXTENT;
    }
  
    if (name.compareTo("uTranslationTexCoord") == 0)
    {
      return GPUUniformKey.TRANSLATION_TEXTURE_COORDS;
    }
  
    if (name.compareTo("uScaleTexCoord") == 0)
    {
      return GPUUniformKey.SCALE_TEXTURE_COORDS;
    }
  
    if (name.compareTo("uPointSize") == 0)
    {
      return GPUUniformKey.POINT_SIZE;
    }
  
    return GPUUniformKey.UNRECOGNIZED_UNIFORM;
  }
  public static GPUAttributeKey getAttributeKey(String name)
  {
  
    if (name.compareTo("aPosition") == 0)
    {
      return GPUAttributeKey.POSITION;
    }
  
    if (name.compareTo("aColor") == 0)
    {
      return GPUAttributeKey.COLOR;
    }
  
    if (name.compareTo("aTextureCoord") == 0)
    {
      return GPUAttributeKey.TEXTURE_COORDS;
    }
  
    return GPUAttributeKey.UNRECOGNIZED_ATTRIBUTE;
  }

  public static int getUniformCode(GPUUniformKey u)
  {
    if (u == GPUUniformKey.UNRECOGNIZED_UNIFORM)
    {
      return 0;
    }
    final int index = u.getValue();
    return getUniformCode(index);
  }
  public static int getAttributeCode(GPUAttributeKey a)
  {
    if (a == GPUAttributeKey.UNRECOGNIZED_ATTRIBUTE)
    {
      return 0;
    }
    final int index = a.getValue();
    return getUniformCode(index);
  }

  public static int getUniformCode(int u)
  {
    return 0x00000001 << u;
  }
  public static int getAttributeCode(int a)
  {
    return 0x00000001 << a;
  }

  public static boolean codeContainsUniform(int code, int u)
  {
    return ((code >> u) & 0x00000001) != 0;
  }
  public static boolean codeContainsAttribute(int code, int a)
  {
    return ((code >> a) & 0x00000001) != 0;
  }


  //const int UNRECOGNIZED = -1;
  //const int FLAT_COLOR = 1;
  //const int MODELVIEW = 2;
  //const int TEXTURE_EXTENT = 3;
  //const int VIEWPORT_EXTENT = 4;
  //const int TRANSLATION_TEXTURE_COORDS = 5;
  //const int SCALE_TEXTURE_COORDS = 6;
  //const int POINT_SIZE = 7;
  //
  //const int POSITION = 8;
  //const int TEXTURE_COORDS = 9;
  //const int COLOR = 10;
  //
  ////TODO: DELETE
  //const int EnableColorPerVertex = 11;
  //const int EnableTexture = 12;
  //const int EnableFlatColor = 13;
  //const int FlatColorIntensity = 14;
  //const int ColorPerVertexIntensity = 15;
  //
  //const int GROUP_NOGROUP = -1;
  //const int GROUP_COLOR = 1;
  
  public static boolean codeContainsUniform(int code, GPUUniformKey u)
  {
    if (u == GPUUniformKey.UNRECOGNIZED_UNIFORM)
    {
      return false;
    }
    final int index = u.getValue();
    return codeContainsUniform(code, index);
  }
  public static boolean codeContainsAttribute(int code, GPUAttributeKey a)
  {
    if (a == GPUAttributeKey.UNRECOGNIZED_ATTRIBUTE)
    {
      return false;
    }
    final int index = a.getValue();
    return codeContainsAttribute(code, index);
  }


//  static const int GROUP_COLOR;
//  static const int GROUP_NOGROUP;

  public void dispose()
  {
  }

  public GPUVariable(String name, GPUVariableType type)
  {
     _name = name;
     _variableType = type;
//    createMetadata();
  }

  //Uniform metadata based in our shaders
//  long getKey() const { return _key;}
//  long getGroup() const { return _group;}
//  long getPriority() const { return _priority;}

}
/*
void createMetadata(){
  _group = GROUP_NOGROUP;
  _priority = -1;
  _key = getKeyForName(_name, _variableType);

  if (_key == UNRECOGNIZED){
    ILogger::instance()->logError("Unrecognized GPU VARAIBLE %s\n", _name.c_str());
  }

  if (_variableType == UNIFORM){
    if (_key == FLAT_COLOR){
      _group = GROUP_COLOR;
    }

    if (_key == TEXTURE_EXTENT){
      _group = GROUP_COLOR;
    }

    if (_key == TRANSLATION_TEXTURE_COORDS){
      _group = GROUP_COLOR;
    }

    if (_key == TRANSLATION_TEXTURE_COORDS){
      _group = GROUP_COLOR;
    }
//
//    if (true){ //DELETE
//      if (_key == EnableColorPerVertex){
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == EnableTexture){
//        _group = GROUP_COLOR;
//      }
//
//      if ( _key == EnableFlatColor){
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == FlatColorIntensity){
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == ColorPerVertexIntensity){
//        _group = GROUP_COLOR;
//      }
//    }
  }

  if (_variableType == ATTRIBUTE){

    if (_key == COLOR){
      _group = GROUP_COLOR;
    }

    if (_key == TEXTURE_COORDS){
      _group = GROUP_COLOR;
    }
  }


}
*/