package org.glob3.mobile.generated; 
//
//  Vector3F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//





public class Vector3F
{
   public Vector3F normalized()
   {
     final double d = length();
     return new Vector3F((float)(_x / d), (float)(_y / d), (float)(_z / d));
   }
}