package org.glob3.mobile.generated; 
public class GPUVariable
{

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
  
    if (name.compareTo("uAmbientLightColor") == 0)
    {
      return GPUUniformKey.AMBIENT_LIGHT_COLOR;
    }
  
    if (name.compareTo("uDiffuseLightDirection") == 0)
    {
      return GPUUniformKey.DIFFUSE_LIGHT_DIRECTION;
    }
  
    if (name.compareTo("uDiffuseLightColor") == 0)
    {
      return GPUUniformKey.DIFFUSE_LIGHT_COLOR;
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
    }
  
    if (name.compareTo("uBillboardPosition") == 0)
    {
      return GPUUniformKey.BILLBOARD_POSITION;
    }
  
    if (name.compareTo("uRotationCenterTexCoord") == 0)
    {
      return GPUUniformKey.ROTATION_CENTER_TEXTURE_COORDS;
    }
  
    if (name.compareTo("uRotationAngleTexCoord") == 0)
    {
      return GPUUniformKey.ROTATION_ANGLE_TEXTURE_COORDS;
    }
  
    if (name.compareTo("Sampler") == 0)
    {
      return GPUUniformKey.SAMPLER;
    }
  
    if (name.compareTo("Sampler2") == 0)
    {
      return GPUUniformKey.SAMPLER2;
    }
  
    if (name.compareTo("Sampler3") == 0)
    {
      return GPUUniformKey.SAMPLER3;
    }
  
    if (name.compareTo("uTranslation2D") == 0)
    {
      return GPUUniformKey.TRANSLATION_2D;
    }
  
    if (name.compareTo("uBillboardAnchor") == 0)
    {
      return GPUUniformKey.BILLBOARD_ANCHOR;
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
  
    if (name.compareTo("aTextureCoord2") == 0)
    {
      return GPUAttributeKey.TEXTURE_COORDS_2;
    }
  
    if (name.compareTo("aTextureCoord3") == 0)
    {
      return GPUAttributeKey.TEXTURE_COORDS_3;
    }
  
    if (name.compareTo("aNormal") == 0)
    {
      return GPUAttributeKey.NORMAL;
    }
  
    if (name.compareTo("aPosition2D") == 0)
    {
      return GPUAttributeKey.POSITION_2D;
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

  public static boolean hasUniform(int code, int u)
  {
    return ((code >> u) & 0x00000001) != 0;
  }
  public static boolean hasAttribute(int code, int a)
  {
    return ((code >> a) & 0x00000001) != 0;
  }

  public static boolean hasUniform(int code, GPUUniformKey u)
  {
    if (u == GPUUniformKey.UNRECOGNIZED_UNIFORM)
    {
      return false;
    }
    final int index = u.getValue();
    return hasUniform(code, index);
  }
  public static boolean hasAttribute(int code, GPUAttributeKey a)
  {
    if (a == GPUAttributeKey.UNRECOGNIZED_ATTRIBUTE)
    {
      return false;
    }
    final int index = a.getValue();
    return hasAttribute(code, index);
  }

  public void dispose()
  {
  }

  public final GPUVariableType _variableType;
  public final String _name;

  public GPUVariable(String name, GPUVariableType type)
  {
     _name = name;
     _variableType = type;
  }


}