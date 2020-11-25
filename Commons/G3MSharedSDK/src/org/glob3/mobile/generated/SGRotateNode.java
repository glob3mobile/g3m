package org.glob3.mobile.generated;
//
//  SGRotateNode.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGRotateNode.hpp
//  G3M
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

  private MutableMatrix44D _rotationMatrix = new MutableMatrix44D();

  private GLState _glState;

  public SGRotateNode(String id, String sID, double x, double y, double z, double angle)
  {
     super(id, sID);
     _x = x;
     _y = y;
     _z = z;
     _angle = angle;
     _rotationMatrix = new MutableMatrix44D(MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_angle), new Vector3D(_x, _y, _z)));
     _glState = new GLState();
    _glState.addGLFeature(new ModelTransformGLFeature(_rotationMatrix.asMatrix44D()), false);
  }

  public void dispose()
  {
    _glState._release();
    super.dispose();
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    _glState.setParent(parentState);
    return _glState;
  }

  public final String description()
  {
    return "SGRotateNode";
  }

}