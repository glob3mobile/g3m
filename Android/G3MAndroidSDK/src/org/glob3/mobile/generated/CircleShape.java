package org.glob3.mobile.generated; 
//
//  CircleShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

//
//  CircleShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//



public class CircleShape extends MeshShape
{
  private float _radius;
  private int _steps;
  private Color _color;

  protected final Mesh createMesh(RenderContext rc)
  {
  
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
  
	IntBufferBuilder indices = new IntBufferBuilder();
  
	// first is the center
	vertices.add(0.0, 0.0, 0.0);
	indices.add(0);
  
	final double twicePi = IMathUtils.instance().pi() * 2;
  
	for (int i = 0; i <= _steps; i++)
	{
	  final double angleInRadians = i * twicePi / _steps;
	  final double x = _radius * IMathUtils.instance().cos(angleInRadians);
	  final double y = _radius * IMathUtils.instance().sin(angleInRadians);
	  vertices.add(x, y, 0);
  
	  indices.add(i + 1);
	}
  
	return new IndexedMesh(GLPrimitive.triangleFan(), true, Vector3D.zero(), vertices.create(), indices.create(), 1, new Color(_color));
  }

  public CircleShape(Geodetic3D position, float radius, Color color, int steps)
  {
	  super(position);
	  _radius = radius;
	  _color = new Color(color);
	  _steps = steps;

  }

  public final void setRadius(float radius)
  {
	if (_radius != radius)
	{
	  _radius = radius;
	  cleanMesh();
	}
  }

  public final void setColor(Color color)
  {
	_color = null;
	_color = new Color(color);
	cleanMesh();
  }


}