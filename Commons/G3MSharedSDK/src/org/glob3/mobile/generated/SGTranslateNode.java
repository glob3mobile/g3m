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
  private final double _x;
  private final double _y;
  private final double _z;


  public SGTranslateNode(String id, String sId, double x, double y, double z)
  {
     super(id, sId);
     _x = x;
     _y = y;
     _z = z;

  }

  public final void prepareRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
  
    gl.pushMatrix();
    gl.multMatrixf(MutableMatrix44D.createTranslationMatrix(_x, _y, _z));
  
    super.prepareRender(rc);
  }

  public final void cleanUpRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
    gl.popMatrix();
  
    super.cleanUpRender(rc);
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    return null;
  }

}