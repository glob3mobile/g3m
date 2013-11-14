package org.glob3.mobile.generated; 
public enum MeshType
{
  POINT_CLOUD,
  MESH;

   public int getValue()
   {
      return this.ordinal();
   }

   public static MeshType forValue(int value)
   {
      return values()[value];
   }
}