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
  private final TilesRenderParameters _parameters;

  private IGLTextureId _texId;


  private final IImage _image;
  private final boolean _isMercatorImage;



  ///#include <iostream>
  
  
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* createTextureCoordinates(const RenderContext* rc, Mesh* mesh) const
  private IFloatBuffer createTextureCoordinates(RenderContext rc, Mesh mesh)
  {
	FloatBufferBuilderFromCartesian2D texCoors = new FloatBufferBuilderFromCartesian2D();
  
	for (int i = 0; i < mesh.getVertexCount(); i++)
	{
	  final Vector3D pos = mesh.getVertex(i);
  
	  final Geodetic2D g = rc.getPlanet().toGeodetic2D(pos);
  
	  if (_isMercatorImage)
	  {
  
		final double latRad = g.latitude().radians();
		final double sec = 1.0/(IMathUtils.instance().cos(g.latitude().radians()));
  
		//double tLatRad = 2*atan(exp(latRad)) - M_PI/2.0;
  
		//double tLatRad = log(tan(latRad)+sec);
		//double tLatRad = 0.5*log((1.0+sin(latRad))/(1.0-sin(latRad)));
		//double tLatRad = atanh(sin(latRad));
		//double tLatRad = asinh(tan(latRad));
  
  
		double tLatRad = IMathUtils.instance().log(IMathUtils.instance().tan(IMathUtils.instance().pi()/4.0)+latRad/2.0);
  
		tLatRad = tLatRad *sec;
		//std::cout<<" Lat: "<<g.latitude().degrees()<<"\n";
		//std::cout<<"tLatRad: "<<tLatRad<<"\n";
		double limit = 85.5 * IMathUtils.instance().pi()/180.0;
		//std::cout<<"limit: "<<limit<<"\n";
		if (tLatRad > limit)
		{
		  tLatRad = limit;
		}
		if (tLatRad<-limit)
		{
		  tLatRad = -limit;
		}
  
		final Geodetic2D mercg = new Geodetic2D(Angle.fromRadians(tLatRad), g.longitude());
		//const Vector3D n = rc->getPlanet()->geodeticSurfaceNormal(mercg);
		final Vector3D m = rc.getPlanet().toCartesian(mercg);
		final Vector3D n = rc.getPlanet().centricSurfaceNormal(m);
  
		final double s = IMathUtils.instance().atan2(n._y, n._x) / (IMathUtils.instance().pi() * 2) + 0.5;
  
		//double t = (tLatRad*sec + M_PI/2.0)/M_PI ;
		double t = IMathUtils.instance().asin(n._z) / IMathUtils.instance().pi() + 0.5;
		//texCoors.push_back(MutableVector2D(s, 1-t));
		texCoors.add((float)s, (float)(1.0-t));
  
	  }
	  else
	  {
		final Vector3D n = rc.getPlanet().geodeticSurfaceNormal(g);
		//const double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
		//double t = asin(n.z()) / M_PI + 0.5 ;
		//texCoors.push_back(MutableVector2D(s, 1-t));
  
		final double s = IMathUtils.instance().atan2(n._y, n._x) / (IMathUtils.instance().pi() * 2) + 0.5;
		final double t = IMathUtils.instance().asin(n._z) / IMathUtils.instance().pi() + 0.5;
  
		texCoors.add((float)s, (float)(1.0-t));
	  }
  
	}
  
	return texCoors.create();
  }


  public SingleImageTileTexturizer(TilesRenderParameters parameters, IImage image, boolean isMercatorImage)
  {
	  _texId = null;
	  _image = image;
	  _parameters = parameters;
	  _renderContext = null;
	  _isMercatorImage = isMercatorImage;
  }

  public void dispose()
  {
	if (_texId != null)
	{
	  if (_renderContext != null)
	  {
		_renderContext.getTexturesHandler().releaseGLTextureId(_texId);
	  }
	}
  }

  public final void initialize(InitializationContext ic, TilesRenderParameters parameters)
  {

  }

  public final Mesh texturize(RenderContext rc, TileRenderContext trc, Tile tile, Mesh mesh, Mesh previousMesh)
  {
	_renderContext = rc; //SAVING CONTEXT
  
	if (_texId == null)
	{
	  _texId = rc.getTexturesHandler().getGLTextureId(_image, GLFormat.rgba(), "SINGLE_IMAGE_TEX", false);
  
	  rc.getFactory().deleteImage(_image);
  
	  if (_texId == null)
	  {
		rc.getLogger().logError("Can't upload texture to GPU");
		return null;
	  }
  
	  rc.getFactory().deleteImage(_image);
	}
  
	TextureMapping texMap = new SimpleTextureMapping(_texId, createTextureCoordinates(rc, mesh), true);
	if (previousMesh != null)
		if (previousMesh != null)
			previousMesh.dispose();
  
	tile.setTextureSolved(true);
	tile.setTexturizerDirty(false);
  
	return new TexturedMesh(mesh, false, texMap, true);
  }

  public final void tileToBeDeleted(Tile tile, Mesh mesh)
  {
  
  }

  public final boolean tileMeetsRenderCriteria(Tile tile)
  {
	return false;
  }

  public final void justCreatedTopTile(RenderContext rc, Tile tile)
  {
  
  }

  public final boolean isReady(RenderContext rc)
  {
	return true;
  }

  public final void ancestorTexturedSolvedChanged(Tile tile, Tile ancestorTile, boolean textureSolved)
  {
  
  }

  public final void onTerrainTouchEvent(EventContext ec, Geodetic3D position, Tile tile)
  {
  }


  public final void tileMeshToBeDeleted(Tile tile, Mesh mesh)
  {
  
  }

}