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
  private double _x;
  private double _y;
  private double _z;
  private double _angle;

  protected final void prepareRender(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.pushMatrix();
	gl.multMatrixf(MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_angle), new Vector3D(_x, _y, _z)));
  
	super.prepareRender(rc);
  }

  protected final void cleanUpRender(RenderContext rc)
  {
	GL gl = rc.getGL();
	gl.popMatrix();
  
	super.prepareRender(rc);
  }

  public SGRotateNode()
  {
	  _x = 0;
	  _y = 0;
	  _z = 0;
	  _angle = 0;

  }

  public final void setX(double x)
  {
	_x = x;
  }

  public final void setY(double y)
  {
	_y = y;
  }

  public final void setZ(double z)
  {
	_z = z;
  }

  public final void setAngle(double angle)
  {
	_angle = angle;
  }

}