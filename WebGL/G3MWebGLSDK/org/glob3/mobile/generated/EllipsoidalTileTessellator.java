package org.glob3.mobile.generated; 
//
//  EllipsoidalTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  EllipsoidalTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//





public class EllipsoidalTileTessellator extends TileTessellator
{

  private final int _resolution;
  private final boolean _skirted;

  private static void addVertex(Planet planet, java.util.ArrayList<MutableVector3D> vertices, Geodetic3D g)
  {
	vertices.add(planet.toCartesian(g).asMutableVector3D());
  }

  private static void addVertex(Planet planet, java.util.ArrayList<MutableVector3D> vertices, Geodetic2D g)
  {
	vertices.add(planet.toCartesian(g).asMutableVector3D());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createDebugMesh(const RenderContext* rc, const Tile* tile) const
  public final Mesh createDebugMesh(RenderContext rc, Tile tile)
  {
	final Sector sector = tile.getSector();
	final Planet planet = rc.getPlanet();
  
	// create vectors
	java.util.ArrayList<MutableVector3D> vertices = new java.util.ArrayList<MutableVector3D>();
	java.util.ArrayList<MutableVector2D> texCoords = new java.util.ArrayList<MutableVector2D>();
	java.util.ArrayList<Integer> indices = new java.util.ArrayList<Integer>();
	final int resolutionMinus1 = _resolution - 1;
	int posS = 0;
  
	// compute offset for vertices
	final Vector3D sw = planet.toCartesian(sector.getSW());
	final Vector3D nw = planet.toCartesian(sector.getNW());
	final double offset = nw.sub(sw).length() * 1e-3;
  
	// west side
	for (int j = 0; j < resolutionMinus1; j++)
	{
	  final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(0, (double)j/resolutionMinus1), offset);
	  addVertex(planet, vertices, g);
	  indices.add(posS++);
	}
  
	// south side
	for (int i = 0; i < resolutionMinus1; i++)
	{
	  final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resolutionMinus1, 1), offset);
	  addVertex(planet, vertices, g);
	  indices.add(posS++);
	}
  
	// east side
	for (int j = resolutionMinus1; j > 0; j--)
	{
	  final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(1, (double)j/resolutionMinus1), offset);
	  addVertex(planet, vertices, g);
	  indices.add(posS++);
	}
  
	// north side
	for (int i = resolutionMinus1; i > 0; i--)
	{
	  final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resolutionMinus1, 0), offset);
	  addVertex(planet, vertices, g);
	  indices.add(posS++);
	}
  
	final Color color = new Color(Color.fromRGBA((float) 1.0, (float) 0, (float) 0, (float) 1.0));
	final Vector3D center = planet.toCartesian(sector.getCenter());
  
	return IndexedMesh.createFromVector3D(vertices, GLPrimitive.LineLoop, CenterStrategy.GivenCenter, center, indices, color);
  }

  public EllipsoidalTileTessellator(int resolution, boolean skirted)
  {
	  _resolution = resolution;
	  _skirted = skirted;
	int __TODO_width_and_height_resolutions;
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createMesh(const RenderContext* rc, const Tile* tile) const
  public final Mesh createMesh(RenderContext rc, Tile tile)
  {
  
	final Sector sector = tile.getSector();
	final Planet planet = rc.getPlanet();
  
	/*
	 very crude draft of a posible latitude-based dynamic resolution to avoid the
	 oversampling in the poles. (dgd)
	 */
	//int resolution = _resolution * tile->getSector().getCenter().latitude().sinus();
	//if (resolution < 0) {
	//  resolution *= -1;
	//}
	final int resolution = _resolution;
	final int resolutionMinus1 = resolution - 1;
  
	// create vertices coordinates
	java.util.ArrayList<MutableVector3D> vertices = new java.util.ArrayList<MutableVector3D>();
	for (int j = 0; j < resolution; j++)
	{
	  for (int i = 0; i < resolution; i++)
	  {
		addVertex(planet, vertices, sector.getInnerPoint((double) i / resolutionMinus1, (double) j / resolutionMinus1));
	  }
	}
  
	// create indices
	java.util.ArrayList<Integer> indices = new java.util.ArrayList<Integer>();
	for (int j = 0; j < resolutionMinus1; j++)
	{
	  if (j > 0)
	  {
		indices.add(j *resolution);
	  }
	  for (int i = 0; i < resolution; i++)
	  {
		indices.add(j *resolution + i);
		indices.add(j *resolution + i + resolution);
	  }
	  indices.add(j *resolution + 2 *resolutionMinus1);
	}
  
	// create skirts
	if (_skirted)
	{
  
	  // compute skirt height
	  final Vector3D sw = planet.toCartesian(sector.getSW());
	  final Vector3D nw = planet.toCartesian(sector.getNW());
	  final double skirtHeight = nw.sub(sw).length() * 0.05;
  
	  indices.add(0);
	  int posS = resolution * resolution;
  
	  // west side
	  for (int j = 0; j < resolutionMinus1; j++)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(0, (double)j/resolutionMinus1), -skirtHeight);
		addVertex(planet, vertices, g);
		indices.add(j *resolution);
		indices.add(posS++);
	  }
  
	  // south side
	  for (int i = 0; i < resolutionMinus1; i++)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resolutionMinus1, 1), -skirtHeight);
		addVertex(planet, vertices, g);
		indices.add(resolutionMinus1 *resolution + i);
		indices.add(posS++);
	  }
  
	  // east side
	  for (int j = resolutionMinus1; j > 0; j--)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(1, (double)j/resolutionMinus1), -skirtHeight);
		addVertex(planet, vertices, g);
		indices.add(j *resolution + resolutionMinus1);
		indices.add(posS++);
	  }
  
	  // north side
	  for (int i = resolutionMinus1; i > 0; i--)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resolutionMinus1, 0), -skirtHeight);
		addVertex(planet, vertices, g);
		indices.add(i);
		indices.add(posS++);
	  }
  
	  // last triangles
	  indices.add(0);
	  indices.add(resolution *resolution);
	}
  
	final Color color = new Color(Color.fromRGBA((float) 0.1, (float) 0.1, (float) 0.1, (float) 1.0));
	final Vector3D center = planet.toCartesian(sector.getCenter());
  
	return IndexedMesh.createFromVector3D(vertices, GLPrimitive.TriangleStrip, CenterStrategy.GivenCenter, center, indices, color);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReady(const RenderContext *rc) const
  public final boolean isReady(RenderContext rc)
  {
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<MutableVector2D>* createUnitTextCoords() const
  public final java.util.ArrayList<MutableVector2D> createUnitTextCoords()
  {
  
	java.util.ArrayList<MutableVector2D> textCoords = new java.util.ArrayList<MutableVector2D>();
  
	final int resolution = _resolution;
	final int resolutionMinus1 = resolution - 1;
  
	float[] u = new float[resolution * resolution];
	float[] v = new float[resolution * resolution];
  
	for (int j = 0; j < resolution; j++)
	{
	  for (int i = 0; i < resolution; i++)
	  {
		final int pos = j *resolution + i;
		u[pos] = (float) i / resolutionMinus1;
		v[pos] = (float) j / resolutionMinus1;
	  }
	}
  
	for (int j = 0; j < resolution; j++)
	{
	  for (int i = 0; i < resolution; i++)
	  {
		final int pos = j *resolution + i;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
	}
  
	// create skirts
	if (_skirted)
	{
	  // west side
	  for (int j = 0; j < resolutionMinus1; j++)
	  {
		final int pos = j *resolution;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
  
	  // south side
	  for (int i = 0; i < resolutionMinus1; i++)
	  {
		final int pos = resolutionMinus1 * resolution + i;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
  
	  // east side
	  for (int j = resolutionMinus1; j > 0; j--)
	  {
		final int pos = j *resolution + resolutionMinus1;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
  
	  // north side
	  for (int i = resolutionMinus1; i > 0; i--)
	  {
		final int pos = i;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
	}
  
	// free temp memory
	u = null;
	v = null;
  
	return textCoords;
  }

}