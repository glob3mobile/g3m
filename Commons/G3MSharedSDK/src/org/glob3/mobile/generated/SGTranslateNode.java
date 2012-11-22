package org.glob3.mobile.generated; 
//
//  SGTranslateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGTranslateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



public class SGTranslateNode extends SGNode
{
  private double _x;
  private double _y;
  private double _z;

  protected final void prepareRender(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.pushMatrix();
	gl.multMatrixf(MutableMatrix44D.createTranslationMatrix(_x, _y, _z));
  
	super.prepareRender(rc);
  }

  protected final void cleanUpRender(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
	gl.popMatrix();
  
	super.cleanUpRender(rc);
  }


  public SGTranslateNode()
  {
	  _x = 0;
	  _y = 0;
	  _z = 0;

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

}