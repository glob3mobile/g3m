package org.glob3.mobile.generated; 
//
//  SingleImageTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  SingleImageTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class SingleImageTileTexturizer extends TileTexturizer
{

  private RenderContext _renderContext;
  private final TileParameters _parameters;
  private int _texID;
  private final IImage _image;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<MutableVector2D> createTextureCoordinates(const RenderContext* rc, Mesh* mesh) const
  private java.util.ArrayList<MutableVector2D> createTextureCoordinates(RenderContext rc, Mesh mesh)
  {
	java.util.ArrayList<MutableVector2D> texCoors = new java.util.ArrayList<MutableVector2D>();
  
	for (int i = 0; i < mesh.getVertexCount(); i++)
	{
  
	  Vector3D pos = mesh.getVertex(i);
  
	  final Geodetic2D g = rc.getPlanet().toGeodetic2D(pos);
	  final Vector3D n = rc.getPlanet().geodeticSurfaceNormal(g);
  
	  final double s = Math.atan2(n.y(), n.x()) / (Math.PI * 2) + 0.5;
	  final double t = Math.asin(n.z()) / Math.PI + 0.5;
  
	  texCoors.add(new MutableVector2D(s, 1-t));
	}
  
	return texCoors;
  }


  public SingleImageTileTexturizer(TileParameters par, IImage image)
  {
	  _texID = -1;
	  _image = image;
	  _parameters = par;
	  _renderContext = null;
  }

  public void dispose()
  {
	if (_texID > -1)
	{
	  if (_renderContext != null)
	  {
		_renderContext.getTexturesHandler().takeTexture(_texID);
	  }
	}
  }

  public final void initialize(InitializationContext ic)
  {

  }

  public final Mesh texturize(RenderContext rc, Tile tile, TileTessellator tessellator, Mesh mesh, Mesh previousMesh)
  {
	_renderContext = rc; //SAVING CONTEXT
  
	if (_texID < 0)
	{
	  _texID = rc.getTexturesHandler().getTextureId(_image, "SINGLE_IMAGE_TEX", _image.getWidth(), _image.getHeight());
  
	  if (_texID < 0)
	  {
		rc.getLogger().logError("Can't upload texture to GPU");
		return null;
	  }
  
	  rc.getFactory().deleteImage(_image);
	}
  
	final TextureMapping texMap = new TextureMapping(_texID, createTextureCoordinates(rc, mesh), rc.getTexturesHandler(), "SINGLE_IMAGE_TEX", _image.getWidth(), _image.getHeight());
  
	if (previousMesh != null)
		if (previousMesh != null)
			previousMesh.dispose();
  
	tile.setTextureSolved(true);
  
	return new TexturedMesh(mesh, false, texMap, true);
  }

  public final void tileToBeDeleted(Tile tile)
  {
  
  }

  public final boolean tileMeetsRenderCriteria(Tile tile)
  {
	return false;
  }

  public final void justCreatedTopTile(Tile tile)
  {
  
  }

  public final boolean isReady(RenderContext rc)
  {
	return true;
  }

}