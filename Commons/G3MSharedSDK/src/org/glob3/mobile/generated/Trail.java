package org.glob3.mobile.generated; 
//
//  TrailsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

//
//  TrailsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;

public class Trail
{
  private boolean _visible;
  private final int _maxSteps;
  private boolean _positionsDirty;

  private Color _color ;
  private final float _lineWidth;

  private java.util.ArrayList<Geodetic3D> _positions = new java.util.ArrayList<Geodetic3D>();

  private Mesh createMesh(Planet planet)
  {
	FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), planet, Geodetic3D.fromDegrees(0, 0, 0));
	IntBufferBuilder indices = new IntBufferBuilder();
  
	for (int i = 0; i < _positions.size(); i++)
	{
  	  vertices.add( _positions.get(i) );
  
	  indices.add(i);
	}
  
	return new IndexedMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), _lineWidth, new Color(_color));
  }

  private Mesh _mesh;
  private Mesh getMesh(Planet planet)
  {
	if (_positionsDirty || (_mesh == null))
	{
	  if (_mesh != null)
		  _mesh.dispose();
  
	  _mesh = createMesh(planet);
	}
	return _mesh;
  }

  public Trail(int maxSteps, Color color, float lineWidth)
  {
	  _maxSteps = maxSteps;
	  _visible = true;
	  _positionsDirty = true;
	  _mesh = null;
	  _color = color;
	  _lineWidth = lineWidth;
  }

  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  }

  public final void render(RenderContext rc)
  {
	if (_visible)
	{
	  Mesh mesh = getMesh(rc.getPlanet());
	  if (mesh != null)
	  {
		mesh.render(rc);
	  }
	}
  }

  public final void setVisible(boolean visible)
  {
	_visible = visible;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isVisible() const
  public final boolean isVisible()
  {
	return _visible;
  }

  public final void addPosition(Geodetic3D position)
  {
	_positionsDirty = true;

	if (_maxSteps > 0)
	{
	  while (_positions.size() >= _maxSteps)
	  {
		// const int lastIndex = _positions.size() - 1;
		final int index = 0;

		if (_positions.get(index) != null)
			_positions.get(index).dispose();

		_positions.remove( index );
	  }
	}

	_positions.add(new Geodetic3D(position));
  }

}