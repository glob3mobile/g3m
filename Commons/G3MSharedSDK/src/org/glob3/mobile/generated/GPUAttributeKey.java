package org.glob3.mobile.generated; 
public enum GPUAttributeKey
{
  UNRECOGNIZED_ATTRIBUTE(0),
  POSITION(1),
  TEXTURE_COORDS(2),
  COLOR(3);

   private int intValue;
   private static java.util.HashMap<Integer, GPUAttributeKey> mappings;
   private static java.util.HashMap<Integer, GPUAttributeKey> getMappings()
   {
      if (mappings == null)
      {
         synchronized (GPUAttributeKey.class)
         {
            if (mappings == null)
            {
               mappings = new java.util.HashMap<Integer, GPUAttributeKey>();
            }
         }
      }
      return mappings;
   }

   private GPUAttributeKey(int value)
   {
      intValue = value;
      GPUAttributeKey.getMappings().put(value, this);
   }

   public int getValue()
   {
      return intValue;
   }

   public static GPUAttributeKey forValue(int value)
   {
      return getMappings().get(value);
   }
}