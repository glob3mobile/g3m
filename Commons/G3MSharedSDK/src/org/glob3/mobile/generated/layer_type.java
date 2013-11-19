package org.glob3.mobile.generated; 
//
//  SceneParser.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 15/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  SceneParser.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 15/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public enum layer_type
{
  WMS,
  TMS,
  THREED,
  PLANARIMAGE,
  GEOJSON,
  SPHERICALIMAGE;

   public int getValue()
   {
      return this.ordinal();
   }

   public static layer_type forValue(int value)
   {
      return values()[value];
   }
}