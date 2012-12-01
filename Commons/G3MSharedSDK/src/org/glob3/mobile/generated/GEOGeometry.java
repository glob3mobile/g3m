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

  protected final Mesh create2DBoundaryMesh(java.util.ArrayList<Geodetic2D> coordinates, G3MRenderContext rc)
  {
	FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), rc.getPlanet(), Geodetic2D.zero());
  
	final int coordinatesCount = coordinates.size();
	for (int i = 0; i < coordinatesCount; i++)
	{
	  Geodetic2D coordinate = coordinates.get(i);
	  vertices.add(coordinate);
	}
  
	Color color = Color.newFromRGBA(1, 1, 0, 1);
  
	return new DirectMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), 2, color);
  }

  public GEOGeometry()
  {
	  _mesh = null;

  }

  public final void render(G3MRenderContext rc)
  {
	Mesh mesh = getMesh(rc);
	if (mesh != null)
	{
	  mesh.render(rc);
	}
  }

  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  }

}