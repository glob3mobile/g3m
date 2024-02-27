package org.glob3.mobile.generated;
//
//  GPUVariable.cpp
//  G3M
//
//  Created by Jose Miguel SN on 20/06/13.
//
//

//
//  GPUVariable.hpp
//  G3M
//
//  Created by Jose Miguel SN on 20/06/13.
//
//





public class GPUVariable
{

  public static GPUUniformKey getUniformKey(String name)
  {
    if (name.equals("uFlatColor"))
    {
      return GPUUniformKey.FLAT_COLOR;
    }
    else if (name.equals("uModelview"))
    {
      return GPUUniformKey.MODELVIEW;
    }
    else if (name.equals("uTextureExtent"))
    {
      return GPUUniformKey.TEXTURE_EXTENT;
    }
    else if (name.equals("uViewPortExtent"))
    {
      return GPUUniformKey.VIEWPORT_EXTENT;
    }
    else if (name.equals("uTranslationTexCoord"))
    {
      return GPUUniformKey.TRANSLATION_TEXTURE_COORDS;
    }
    else if (name.equals("uScaleTexCoord"))
    {
      return GPUUniformKey.SCALE_TEXTURE_COORDS;
    }
    else if (name.equals("uPointSize"))
    {
      return GPUUniformKey.POINT_SIZE;
    }
    else if (name.equals("uAmbientLightColor"))
    {
      return GPUUniformKey.AMBIENT_LIGHT_COLOR;
    }
    else if (name.equals("uDiffuseLightDirection"))
    {
      return GPUUniformKey.DIFFUSE_LIGHT_DIRECTION;
    }
    else if (name.equals("uDiffuseLightColor"))
    {
      return GPUUniformKey.DIFFUSE_LIGHT_COLOR;
    }
    else if (name.equals("uProjection"))
    {
      return GPUUniformKey.PROJECTION;
    }
    else if (name.equals("uCameraModel"))
    {
      return GPUUniformKey.CAMERA_MODEL;
    }
    else if (name.equals("uModel"))
    {
      return GPUUniformKey.MODEL;
    }
    else if (name.equals("uBillboardPosition"))
    {
      return GPUUniformKey.BILLBOARD_POSITION;
    }
    else if (name.equals("uRotationCenterTexCoord"))
    {
      return GPUUniformKey.ROTATION_CENTER_TEXTURE_COORDS;
    }
    else if (name.equals("uRotationAngleTexCoord"))
    {
      return GPUUniformKey.ROTATION_ANGLE_TEXTURE_COORDS;
    }
    else if (name.equals("Sampler"))
    {
      return GPUUniformKey.SAMPLER;
    }
    else if (name.equals("Sampler2"))
    {
      return GPUUniformKey.SAMPLER2;
    }
    else if (name.equals("Sampler3"))
    {
      return GPUUniformKey.SAMPLER3;
    }
    else if (name.equals("uTranslation2D"))
    {
      return GPUUniformKey.TRANSLATION_2D;
    }
    else if (name.equals("uBillboardAnchor"))
    {
      return GPUUniformKey.BILLBOARD_ANCHOR;
    }
    else if (name.equals("uCameraPosition"))
    {
      return GPUUniformKey.CAMERA_POSITION;
    }
    else
    {
      return GPUUniformKey.UNRECOGNIZED_UNIFORM;
    }
  }
  public static GPUAttributeKey getAttributeKey(String name)
  {
    if (name.equals("aPosition"))
    {
      return GPUAttributeKey.POSITION;
    }
    else if (name.equals("aColor"))
    {
      return GPUAttributeKey.COLOR;
    }
    else if (name.equals("aTextureCoord"))
    {
      return GPUAttributeKey.TEXTURE_COORDS;
    }
    else if (name.equals("aTextureCoord2"))
    {
      return GPUAttributeKey.TEXTURE_COORDS_2;
    }
    else if (name.equals("aTextureCoord3"))
    {
      return GPUAttributeKey.TEXTURE_COORDS_3;
    }
    else if (name.equals("aNormal"))
    {
      return GPUAttributeKey.NORMAL;
    }
    else if (name.equals("aPosition2D"))
    {
      return GPUAttributeKey.POSITION_2D;
    }
    else
    {
      return GPUAttributeKey.UNRECOGNIZED_ATTRIBUTE;
    }
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

  public final String _name;
  public final GPUVariableType _variableType;

  public GPUVariable(String name, GPUVariableType type)
  {
     _name = name;
     _variableType = type;
  }

}