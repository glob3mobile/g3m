package org.glob3.mobile.generated; 
//
//  EllipsoidalTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  EllipsoidalTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class EllipsoidalTileTessellator extends TileTessellator
{

  private final String _textureFilename;

  private static void addVertex(Planet planet, java.util.ArrayList<MutableVector3D> vertices, java.util.ArrayList<MutableVector2D> texCoords, Geodetic2D g)
  {
	vertices.add(planet.toVector3D(g).asMutableVector3D());


	Vector3D n = planet.geodeticSurfaceNormal(g);

	double s = Math.atan2(n.y(), n.x()) / (Math.PI * 2) + 0.5;
	double t = Math.asin(n.z()) / Math.PI + 0.5;

//    const double s = (g.longitude().degrees() + 180) / 360;
//    const double t = (g.latitude().degrees() + 90) / 180;

	MutableVector2D texCoord = new MutableVector2D(s, 1-t);
	texCoords.add(texCoord);

  }

  public EllipsoidalTileTessellator(String textureFilename)
  {
	  _textureFilename = textureFilename;

  }

  public void dispose()
  {
  }


  ///#include "math.h"
  
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createMesh(const RenderContext* rc, const Tile* tile) const
  public Mesh createMesh(RenderContext rc, Tile tile)
  {
	int ___diego_at_work;
  
	final int texID = rc.getTexturesHandler().getTextureIdFromFileName(rc, _textureFilename, 2048, 1024);
  
	if (texID < 1)
	{
	  rc.getLogger().logError("Can't load file %s", _textureFilename);
	  return null;
	}
  
	final Sector sector = tile.getSector();
	final Planet planet = rc.getPlanet();
  
	java.util.ArrayList<MutableVector3D> vertices = new java.util.ArrayList<MutableVector3D>();
	java.util.ArrayList<MutableVector2D> texCoords = new java.util.ArrayList<MutableVector2D>();
	addVertex(planet, vertices, texCoords, sector.getSW());
	addVertex(planet, vertices, texCoords, sector.getSE());
	addVertex(planet, vertices, texCoords, sector.getNW());
	addVertex(planet, vertices, texCoords, sector.getNE());
  
	java.util.ArrayList<Integer> indexes = new java.util.ArrayList<Integer>();
	indexes.add(0);
	indexes.add(1);
	indexes.add(2);
	indexes.add(3);
  
  //  double r = (rand() % 100) / 100.0;
  //  double g = (rand() % 100) / 100.0;
  //  double b = (rand() % 100) / 100.0;
  //  const Color color = Color::fromRGB(r, g, b, 1);
  //
  //  return new IndexedTriangleStripMesh(vertices, indexes, color);
	return new IndexedMesh(vertices, GLPrimitive.TriangleStrip, indexes, texID, texCoords);
  }
}