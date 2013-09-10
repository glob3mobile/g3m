package org.glob3.mobile.generated; 
public class GLType
{
  private static int _float = 0;
  private static int _unsignedByte = 0;
  private static int _unsignedInt = 0;
  private static int _int = 0;
  private static int _vec2Float = 0;
  private static int _vec3Float = 0;
  private static int _vec4Float = 0;
  private static int _bool = 0;
  private static int _matrix4Float = 0;

  public static int glFloat()
  {
     return _float;
  }
  public static int glUnsignedByte()
  {
     return _unsignedByte;
  }
  public static int glUnsignedInt()
  {
     return _unsignedInt;
  }
  public static int glInt()
  {
     return _int;
  }
  public static int glVec2Float()
  {
     return _vec2Float;
  }
  public static int glVec3Float()
  {
     return _vec3Float;
  }
  public static int glVec4Float()
  {
     return _vec4Float;
  }
  public static int glBool()
  {
     return _bool;
  }
  public static int glMatrix4Float()
  {
     return _matrix4Float;
  }

  public static void init(INativeGL ngl)
  {
    _float = ngl.Type_Float();
    _unsignedByte = ngl.Type_UnsignedByte();
    _unsignedInt = ngl.Type_UnsignedInt();
    _int = ngl.Type_Int();
    _vec2Float = ngl.Type_Vec2Float();
    _vec3Float = ngl.Type_Vec3Float();
    _vec4Float = ngl.Type_Vec4Float();
    _bool = ngl.Type_Bool();
    _matrix4Float = ngl.Type_Matrix4Float();
  }
}