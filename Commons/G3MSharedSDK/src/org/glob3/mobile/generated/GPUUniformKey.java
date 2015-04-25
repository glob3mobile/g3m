package org.glob3.mobile.generated; 
public enum GPUUniformKey
{
  UNRECOGNIZED_UNIFORM(-1),
  FLAT_COLOR(0),
  MODELVIEW(1),
  TEXTURE_EXTENT(2),
  VIEWPORT_EXTENT(3),
  TRANSLATION_TEXTURE_COORDS(4),
  SCALE_TEXTURE_COORDS(5),
  POINT_SIZE(6),
  AMBIENT_LIGHT_COLOR(7),
  DIFFUSE_LIGHT_DIRECTION(8),
  DIFFUSE_LIGHT_COLOR(9),
  PROJECTION(10),
  CAMERA_MODEL(11),
  MODEL(12),
  POINT_LIGHT_POSITION(13),
  POINT_LIGHT_COLOR(14),
  BILLBOARD_POSITION(15),
  ROTATION_CENTER_TEXTURE_COORDS(16),
  ROTATION_ANGLE_TEXTURE_COORDS(17),
  SAMPLER(18),
  SAMPLER2(19),
  SAMPLER3(20),
  TRANSLATION_2D(21),
  BILLBOARD_ANCHOR(22);

   private int intValue;
   private static java.util.HashMap<Integer, GPUUniformKey> mappings;
   private static java.util.HashMap<Integer, GPUUniformKey> getMappings()
   {
      if (mappings == null)
      {
         synchronized (GPUUniformKey.class)
         {
            if (mappings == null)
            {
               mappings = new java.util.HashMap<Integer, GPUUniformKey>();
            }
         }
      }
      return mappings;
   }

   private GPUUniformKey(int value)
   {
      intValue = value;
      GPUUniformKey.getMappings().put(value, this);
   }

   public int getValue()
   {
      return intValue;
   }

   public static GPUUniformKey forValue(int value)
   {
      return getMappings().get(value);
   }
}