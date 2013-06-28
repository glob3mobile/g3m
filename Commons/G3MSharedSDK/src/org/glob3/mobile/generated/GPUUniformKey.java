package org.glob3.mobile.generated; 
public enum GPUUniformKey
{
  UNRECOGNIZED_UNIFORM(0),
  FLAT_COLOR(1),
  MODELVIEW(2),
  TEXTURE_EXTENT(3),
  VIEWPORT_EXTENT(4),
  TRANSLATION_TEXTURE_COORDS(5),
  SCALE_TEXTURE_COORDS(6),
  POINT_SIZE(7);

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