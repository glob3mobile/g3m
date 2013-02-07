package org.glob3.mobile.generated; 
//
//  SGRotateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGRotateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



public class SGRotateNode extends SGNode
{
  private final double _x;
  private final double _y;
  private final double _z;
  private final double _angle;

  public SGRotateNode(String id, String sId, double x, double y, double z, double angle)
  {
     super(id, sId);
     _x = x;
     _y = y;
     _z = z;
     _angle = angle;

  }

  public final void prepareRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
  
    gl.pushMatrix();
    gl.multMatrixf(MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_angle), new Vector3D(_x, _y, _z)));
  
    super.prepareRender(rc);
  }

  public final void cleanUpRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
    gl.popMatrix();
  
    super.prepareRender(rc);
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    return null;
  }

}