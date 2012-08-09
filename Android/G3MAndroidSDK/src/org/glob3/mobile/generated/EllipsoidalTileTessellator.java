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
	vertices.add(planet.toVector3D(g).asMutableVector3D());
  }

  private static void addVertex(Planet planet, java.util.ArrayList<MutableVector3D> vertices, Geodetic2D g)
  {
	vertices.add(planet.toVector3D(g).asMutableVector3D());
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
	  int resol_1 = _resolution - 1;
	  int posS = 0;
    
	  // compute offset for vertices
	  final Vector3D sw = planet.toVector3D(sector.getSW());
	  final Vector3D nw = planet.toVector3D(sector.getNW());
	  final double offset = nw.sub(sw).length() * 1e-3;
    
	  // west side
	  for (int j = 0; j<resol_1; j++)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(0.0, (double)j/resol_1), offset);
		addVertex(planet, vertices, g);
		indices.add(posS++);
	  }
    
	  // south side
	  for (int i = 0; i<resol_1; i++)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resol_1, 1.0), offset);
		addVertex(planet, vertices, g);
		indices.add(posS++);
	  }
    
	  // east side
	  for (int j = resol_1; j>0; j--)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(1.0, (double)j/resol_1), offset);
		addVertex(planet, vertices, g);
		indices.add(posS++);
	  }
    
	  // north side
	  for (int i = resol_1; i>0; i--)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resol_1, 0.0), offset);
		addVertex(planet, vertices, g);
		indices.add(posS++);
	  }
    
	  final Color color = new Color(Color.fromRGBA((float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0));
	  final Vector3D center = planet.toVector3D(sector.getCenter());
    
	  return IndexedMesh.CreateFromVector3D(vertices, GLPrimitive.LineLoop, CenterStrategy.GivenCenter, center, indices, color);
	}

  public EllipsoidalTileTessellator(int resolution, boolean skirted)
  {
	  _resolution = resolution;
	  _skirted = skirted;

  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createMesh(const RenderContext* rc, const Tile* tile) const
  public Mesh createMesh(RenderContext rc, Tile tile)
  {
  
	final Sector sector = tile.getSector();
	final Planet planet = rc.getPlanet();
  
	// create vertices coordinates
	java.util.ArrayList<MutableVector3D> vertices = new java.util.ArrayList<MutableVector3D>();
	int resol_1 = _resolution - 1;
	for (int j = 0; j<_resolution; j++)
	{
	  for (int i = 0; i<_resolution; i++)
	  {
		addVertex(planet, vertices, sector.getInnerPoint((double)i/resol_1, (double)j/resol_1));
	  }
	}
  
	// create indices
	java.util.ArrayList<Integer> indices = new java.util.ArrayList<Integer>();
	for (int j = 0; j<resol_1; j++)
	{
	  if (j > 0)
	  {
		indices.add(j *_resolution);
	  }
	  for (int i = 0; i < _resolution; i++)
	  {
		indices.add(j *_resolution + i);
		indices.add(j *_resolution + i + _resolution);
	  }
	  indices.add(j *_resolution + 2 *_resolution - 1);
	}
  
	// create skirts
	if (_skirted)
	{
  
	  // compute skirt height
	  final Vector3D sw = planet.toVector3D(sector.getSW());
	  final Vector3D nw = planet.toVector3D(sector.getNW());
	  final double skirtHeight = nw.sub(sw).length() * 0.05;
  
	  indices.add(0);
	  int posS = _resolution * _resolution;
  
	  // west side
	  for (int j = 0; j<resol_1; j++)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(0.0, (double)j/resol_1), -skirtHeight);
		addVertex(planet, vertices, g);
		indices.add(j *_resolution);
		indices.add(posS++);
	  }
  
	  // south side
	  for (int i = 0; i<resol_1; i++)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resol_1, 1.0), -skirtHeight);
		addVertex(planet, vertices, g);
		indices.add(resol_1 *_resolution + i);
		indices.add(posS++);
	  }
  
	  // east side
	  for (int j = resol_1; j>0; j--)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(1.0, (double)j/resol_1), -skirtHeight);
		addVertex(planet, vertices, g);
		indices.add(j *_resolution + resol_1);
		indices.add(posS++);
	  }
  
	  // north side
	  for (int i = resol_1; i>0; i--)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resol_1, 0.0), -skirtHeight);
		addVertex(planet, vertices, g);
		indices.add(i);
		indices.add(posS++);
	  }
  
	  // last triangles
	  indices.add(0);
	  indices.add(_resolution *_resolution);
	}
  
	final Color color = new Color(Color.fromRGBA((float) 0.1, (float) 0.1, (float) 0.1, (float) 1.0));
	final Vector3D center = planet.toVector3D(sector.getCenter());
  
	return IndexedMesh.CreateFromVector3D(vertices, GLPrimitive.TriangleStrip, CenterStrategy.GivenCenter, center, indices, color);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReadyToRender(const RenderContext *rc) const
  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<MutableVector2D>* createUnitTextCoords() const
  public java.util.ArrayList<MutableVector2D> createUnitTextCoords()
  {
  
	java.util.ArrayList<MutableVector2D> textCoords = new java.util.ArrayList<MutableVector2D>();
  
	final int n = _resolution;
  
	float[] u = new float[n *n];
	float[] v = new float[n *n];
  
	for (int j = 0; j<n; j++)
	{
	  for (int i = 0; i<n; i++)
	  {
		int pos = j *n + i;
		u[pos] = (float) i / (n-1);
		v[pos] = (float) j / (n-1);
	  }
	}
  
	for (int j = 0; j<n; j++)
	{
	  for (int i = 0; i<n; i++)
	  {
		int pos = j *n + i;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
	}
  
	// create skirts
	if (_skirted)
	{
	  // west side
	  for (int j = 0; j<n-1; j++)
	  {
		int pos = j *n;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
  
	  // south side
	  for (int i = 0; i<n-1; i++)
	  {
		int pos = (n-1)*n + i;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
  
	  // east side
	  for (int j = n-1; j>0; j--)
	  {
		int pos = j *n + n - 1;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
  
	  // north side
	  for (int i = n-1; i>0; i--)
	  {
		int pos = i;
		textCoords.add(new MutableVector2D(u[pos], v[pos]));
	  }
	}
  
	// free temp memory
	u = null;
	v = null;
  
	return textCoords;
  }


}