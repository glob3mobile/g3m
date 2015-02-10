

package com.glob3mobile.pointcloud;

public class Classification {

   private final byte _value;


   private Classification(final byte value) {
      _value = value;
   }


   public static final Classification Created           = new Classification((byte) 0);
   public static final Classification Never_Classified  = Created;
   public static final Classification Unclassified      = new Classification((byte) 1);
   public static final Classification Ground            = new Classification((byte) 2);
   public static final Classification Low_Vegetation    = new Classification((byte) 3);
   public static final Classification Medium_Vegetation = new Classification((byte) 4);
   public static final Classification High_Vegetation   = new Classification((byte) 5);
   public static final Classification Building          = new Classification((byte) 6);
   public static final Classification Low_Point         = new Classification((byte) 7);
   public static final Classification Noise             = Low_Point;
   public static final Classification Model_Key_Point   = new Classification((byte) 8);
   public static final Classification Mass_Point        = Model_Key_Point;

   public static final Classification Water             = new Classification((byte) 9);
   public static final Classification Overlap_Points    = new Classification((byte) 12);


}
