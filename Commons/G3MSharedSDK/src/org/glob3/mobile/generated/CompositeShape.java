package org.glob3.mobile.generated;//
//  CompositeShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/6/12.
//
//

//
//  CompositeShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/6/12.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramState;

public abstract class CompositeShape extends Shape
{
  private java.util.ArrayList<Shape> _children = new java.util.ArrayList<Shape>();

  public CompositeShape()
  {
	  super(null, AltitudeMode.ABSOLUTE);

  }

  public CompositeShape(Geodetic3D position, AltitudeMode altitudeMode)
  {
	  super(position, altitudeMode);

  }


  public void dispose()
  {
	int childrenCount = _children.size();
	for (int i = 0; i < childrenCount; i++)
	{
	  Shape child = _children.get(i);
	  if (child != null)
		  child.dispose();
	}
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

  public final void addShape(Shape shape)
  {
	_children.add(shape);
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
	int childrenCount = _children.size();
	for (int i = 0; i < childrenCount; i++)
	{
	  Shape child = _children.get(i);
	  if (child.isReadyToRender(rc))
	  {
		return true;
	  }
	}
  
	return false;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
	int childrenCount = _children.size();
	for (int i = 0; i < childrenCount; i++)
	{
	  Shape child = _children.get(i);
	  child.render(rc, parentState, renderNotReadyShapes);
	}
  }
}
