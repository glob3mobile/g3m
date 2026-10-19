package org.glob3.mobile.generated;
//
//  Cylinder.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/2/21.
//




public class Cylinder
{

  public final Vector3D _start ;
  public final Vector3D _end ;
  public final double _startRadius;
  public final double _endRadius;

  public Cylinder(Vector3D start, Vector3D end, double startRadius, double endRadius)
  {
     _start = start;
     _end = end;
     _startRadius = startRadius;
     _endRadius = endRadius;

  }

}