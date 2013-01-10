package org.glob3.mobile.generated; 
//
//  GEOGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEOGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;

public abstract class GEOGeometry extends GEOObject
{
  private Mesh _mesh;

  protected Mesh getMesh(G3MRenderContext rc)
  {
	if (_mesh == null)
	{
	  _mesh = createMesh(rc);
	}
	return _mesh;
  }

  protected abstract Mesh createMesh(G3MRenderContext rc);

  protected final Mesh create2DBoundaryMesh(java.util.ArrayList<Geodetic2D> coordinates, Color color, float lineWidth, G3MRenderContext rc)
  {
	FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), rc.getPlanet(), Geodetic2D.zero());
  
	final int coordinatesCount = coordinates.size();
	for (int i = 0; i < coordinatesCount; i++)
	{
	  Geodetic2D coordinate = coordinates.get(i);
	  vertices.add(coordinate);
	  // vertices.add( Geodetic3D(*coordinate, 50) );
	}
  
	return new DirectMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), lineWidth, 1, color);
  }

  public GEOGeometry()
  {
	  _mesh = null;

  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
	Mesh mesh = getMesh(rc);
	if (mesh != null)
	{
	  final Extent extent = mesh.getExtent();
  
	  if (extent.touches(rc.getCurrentCamera().getFrustumInModelCoordinates()))
	  {
		GLState state = new GLState(parentState);
		state.disableDepthTest();
		mesh.render(rc, state);
	  }
	}
  }

  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  }

}