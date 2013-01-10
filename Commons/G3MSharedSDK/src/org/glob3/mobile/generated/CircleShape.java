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

  protected final Mesh createMesh(G3MRenderContext rc)
  {
  
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
  
	// first is the center
	vertices.add(0.0, 0.0, 0.0);
  
	final double twicePi = IMathUtils.instance().pi() * 2;
  
	for (int i = 0; i <= _steps; i++)
	{
	  final double angleInRadians = i * twicePi / _steps;
	  final double x = _radius * IMathUtils.instance().cos(angleInRadians);
	  final double y = _radius * IMathUtils.instance().sin(angleInRadians);
	  vertices.add(x, y, 0);
	}
  
	Color color = (_color == null) ? null : new Color(_color);
  
	return new DirectMesh(GLPrimitive.triangleFan(), true, Vector3D.zero(), vertices.create(), 1, 1, color);
  }

  public CircleShape(Geodetic3D position, float radius, Color color)
  {
	  this(position, radius, color, 64);
  }
  public CircleShape(Geodetic3D position, float radius)
  {
	  this(position, radius, null, 64);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: CircleShape(Geodetic3D* position, float radius, Color* color = null, int steps = 64) : MeshShape(position), _radius(radius), _color(color), _steps(steps)
  public CircleShape(Geodetic3D position, float radius, Color color, int steps)
  {
	  super(position);
	  _radius = radius;
	  _color = color;
	  _steps = steps;

  }

  public void dispose()
  {
	if (_color != null)
		_color.dispose();
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
	if (_color != color)
	{
	  if (_color != null)
		  _color.dispose();
	  _color = color;
	  cleanMesh();
	}
  }


}