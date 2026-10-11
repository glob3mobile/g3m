package org.glob3.mobile.generated;
//
//  GPUUniformKey.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

//
//  GPUUniformKey.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//


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
  MODEL(10),
  BILLBOARD_POSITION(11),
  ROTATION_CENTER_TEXTURE_COORDS(12),
  ROTATION_ANGLE_TEXTURE_COORDS(13),
  SAMPLER(14),
  SAMPLER2(15),
  SAMPLER3(16),
  TRANSLATION_2D(17),
  BILLBOARD_ANCHOR(18),
  CAMERA_POSITION(19),
  RIBBON_WIDTH(20),
  GROUND_HAZE_PASS(21),
  SPACE_COLOR(22),
  FULL_STAR_MAGNITUDE(23),
  STAR_SIZE_EXPONENT(24),
  COLOR_MATRIX(25),
  BILLBOARD_COLOR_FACTOR(26),
  HORIZON_COLOR(27),
  SKY_RAYLEIGH_SCATTERING(28);

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