package org.glob3.mobile.generated; 
public class GPUVariable
{


  protected final GPUVariableType _variableType;
  protected final String _name;


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
  
<<<<<<< HEAD
    if (name.compareTo("uAmbientLight") == 0)
    {
      return GPUUniformKey.AMBIENT_LIGHT;
    }
  
    if (name.compareTo("uLightDirection") == 0)
    {
      return GPUUniformKey.LIGHT_DIRECTION;
    }
  
    if (name.compareTo("uLightColor") == 0)
    {
      return GPUUniformKey.LIGHT_COLOR;
    }
  
    if (name.compareTo("uProjection") == 0)
    {
      return GPUUniformKey.PROJECTION;
    }
  
    if (name.compareTo("uCameraModel") == 0)
    {
      return GPUUniformKey.CAMERA_MODEL;
    }
  
    if (name.compareTo("uModel") == 0)
    {
      return GPUUniformKey.MODEL;
=======
    if (name.compareTo("uBillboardPosition") == 0)
    {
      return GPUUniformKey.BILLBOARD_POSITION;
>>>>>>> glstate-rc
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
  
    if (name.compareTo("aNormal") == 0)
    {
      return GPUAttributeKey.NORMAL;
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

  public void dispose()
  {
  }

  public GPUVariable(String name, GPUVariableType type)
  {
     _name = name;
     _variableType = type;
<<<<<<< HEAD
=======
//    createMetadata();
  }

  //Uniform metadata based in our shaders
//  long getKey() const { return _key;}
//  long getGroup() const { return _group;}
//  long getPriority() const { return _priority;}

}
/*
void createMetadata() {
  _group = GROUP_NOGROUP;
  _priority = -1;
  _key = getKeyForName(_name, _variableType);

  if (_key == UNRECOGNIZED) {
    ILogger::instance()->logError("Unrecognized GPU VARAIBLE %s\n", _name.c_str());
  }

  if (_variableType == UNIFORM) {
    if (_key == FLAT_COLOR) {
      _group = GROUP_COLOR;
    }

    if (_key == TEXTURE_EXTENT) {
      _group = GROUP_COLOR;
    }

    if (_key == TRANSLATION_TEXTURE_COORDS) {
      _group = GROUP_COLOR;
    }

    if (_key == TRANSLATION_TEXTURE_COORDS) {
      _group = GROUP_COLOR;
    }
//
//    if (true) { //DELETE
//      if (_key == EnableColorPerVertex) {
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == EnableTexture) {
//        _group = GROUP_COLOR;
//      }
//
//      if ( _key == EnableFlatColor) {
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == FlatColorIntensity) {
//        _group = GROUP_COLOR;
//      }
//
//      if (_key == ColorPerVertexIntensity) {
//        _group = GROUP_COLOR;
//      }
//    }
  }

  if (_variableType == ATTRIBUTE) {

    if (_key == COLOR) {
      _group = GROUP_COLOR;
    }

    if (_key == TEXTURE_COORDS) {
      _group = GROUP_COLOR;
    }
>>>>>>> glstate-rc
  }

}
