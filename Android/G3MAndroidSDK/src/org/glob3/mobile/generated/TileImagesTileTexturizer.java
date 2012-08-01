package org.glob3.mobile.generated; 
//
//  TileImagesTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  TileImagesTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//








//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TilePetitions;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class WMSLayer;

public class TileImagesTileTexturizer extends TileTexturizer
{

  private IFactory _factory; // FINAL WORD REMOVE BY CONVERSOR RULE
  private TexturesHandler _texHandler;
  private Downloader _downloader;

  private java.util.ArrayList<TilePetitions> _tilePetitions = new java.util.ArrayList<TilePetitions>();
  private java.util.ArrayList<TilePetitions> _tilePetitionsTopTile = new java.util.ArrayList<TilePetitions>();

  private final TileParameters _parameters;

  private WMSLayer _layer;
  private LayerSet _layerSet;

  private java.util.ArrayList<MutableVector2D> _texCoordsCache;

  private TilePetitions createTilePetitions(Tile tile)
  {
	java.util.ArrayList<Petition> pet = _layerSet.createTilePetitions(_factory, tile, _parameters._tileTextureWidth, _parameters._tileTextureHeight);
  //  if (pet.size() > 1){
  //    for (int i = 0; i < pet.size(); i++) {
  //      printf("%s\n", pet[i]->getURL().c_str());
  //    }
  //  }
  
	return new TilePetitions(tile.getLevel(), tile.getRow(), tile.getColumn(), tile.getSector(), pet);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<MutableVector2D> getTextureCoordinates(const TileTessellator* tessellator) const
  private java.util.ArrayList<MutableVector2D> getTextureCoordinates(TileTessellator tessellator)
  {
	if (_texCoordsCache == null)
	{
	  _texCoordsCache = tessellator.createUnitTextCoords();
	}
  
	return _texCoordsCache;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void translateAndScaleFallBackTex(Tile* tile, Tile* fallbackTile, TextureMapping* tmap) const
  private void translateAndScaleFallBackTex(Tile tile, Tile fallbackTile, TextureMapping tmap)
  {
	final Sector tileSector = tile.getSector();
	final Sector fallbackTileSector = fallbackTile.getSector();
  
	tmap.setTranslationAndScale(tileSector.getTranslationFactor(fallbackTileSector), tileSector.getScaleFactor(fallbackTileSector));
  }

  private void registerNewRequest(Tile tile)
  {
	if (getRegisteredTilePetitions(tile) == null) //NOT YET REQUESTED
	{
	  int priority = tile.getLevel(); //DOWNLOAD PRIORITY SET TO TILE LEVEL
	  TilePetitions tp = createTilePetitions(tile);
	  _tilePetitions.add(tp); //STORED
	  tp.requestToNet(_downloader, priority);
	}
  }

  private int getTexture(Tile tile)
  {
	TilePetitions tp = getRegisteredTilePetitions(tile);
  
	if (tp!= null)
	{
  
	  if (tp.getTexID() > -1)
	  {
		return tp.getTexID();
	  }
	  else
	  {
		if (tp.allFinished())
		{
		  tp.createTexture(_texHandler, _factory, _parameters._tileTextureWidth, _parameters._tileTextureHeight);
		  return tp.getTexID();
		}
	  }
	}
  
	return -1;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TilePetitions* getRegisteredTilePetitions(Tile* tile) const
  private TilePetitions getRegisteredTilePetitions(Tile tile)
  {
	for (int i = 0; i < _tilePetitions.size(); i++)
	{
	  TilePetitions tp = _tilePetitions.get(i);
	  if (tile.getLevel() == tp.getLevel() && tile.getRow() == tp.getRow() && tile.getColumn() == tp.getColumn())
	  {
		return tp;
	  }
	}
	return null;
  }
  private void removeRegisteredTilePetitions(Tile tile)
  {
	TilePetitions tp = null;
	for (int i = 0; i < _tilePetitions.size(); i++)
	{
	  tp = _tilePetitions.get(i);
	  if (tile.getLevel() == tp.getLevel() && tile.getRow() == tp.getRow() && tile.getColumn() == tp.getColumn())
	  {
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		_tilePetitions.erase(_tilePetitions.iterator() + i);
		if (tp != null)
			tp.dispose();
		break;
	  }
	}
  }

  private Mesh getFallBackTexturedMesh(Tile tile, TileTessellator tessellator, Mesh tessellatorMesh, Mesh previousMesh)
  {
	int texID = -1;
	Tile fbTile = tile.getParent();
	while (fbTile != null && texID < 0)
	{
  
	  TilePetitions tp = createTilePetitions(fbTile);
	  tp.requestToCache(_downloader);
	  if (tp.allFinished())
	  {
		tp.createTexture(_texHandler, _factory, _parameters._tileTextureWidth, _parameters._tileTextureHeight);
		texID = tp.getTexID();
	  }
  
	  //We do no store the petitions
	  if (tp != null)
		  tp.dispose();
  
	  if (texID > -1)
		  break;
	  fbTile = fbTile.getParent(); //TRYING TO CREATE FALLBACK TEXTURE FROM ANTECESOR
	}
  
	//CREATING MESH
	if (texID > -1)
	{
	  TextureMapping tMap = new TextureMapping(texID, getTextureCoordinates(tessellator));
	  translateAndScaleFallBackTex(tile, fbTile, tMap);
	  TexturedMesh texMesh = new TexturedMesh(tessellatorMesh, false, tMap, true);
	  if (previousMesh != null)
		  previousMesh.dispose(); //If a new mesh has been produced we delete the previous one
	  return texMesh;
	}
  
	return null;
  }

  private Mesh getNewTextureMesh(Tile tile, TileTessellator tessellator, Mesh tessellatorMesh, Mesh previousMesh)
  {
	//THE TEXTURE HAS BEEN LOADED???
	int texID = getTexture(tile);
	if (texID > -1)
	{
	  tile.setTextureSolved(true);
  
	  //printf("TEXTURIZED %d, %d, %d\n", tile->getLevel(), tile->getRow(), tile->getColumn());
  
	  TextureMapping tMap = new TextureMapping(texID, getTextureCoordinates(tessellator));
	  TexturedMesh texMesh = new TexturedMesh(tessellatorMesh, false, tMap, true);
	  if (previousMesh != null)
		  previousMesh.dispose(); //If a new mesh has been produced we delete the previous one
	  return texMesh;
	}
	else
	{
	  return null;
	}
  }


  public TileImagesTileTexturizer(TileParameters parameters, Downloader downloader, LayerSet layerSet, IFactory factory)
  {
	  _parameters = parameters;
	  _layer = null;
	  _downloader = downloader;
	  _texCoordsCache = null;
	  _layerSet = layerSet;
	  _factory = factory;
  }

  public void dispose()
  {
	if (_texCoordsCache != null)
	{
	  _texCoordsCache = null;
	}
  }

  public final Mesh texturize(RenderContext rc, Tile tile, TileTessellator tessellator, Mesh tessellatorMesh, Mesh previousMesh, ITimer timer)
  {
	//STORING CONTEXT
	_factory = rc.getFactory();
	_texHandler = rc.getTexturesHandler();
	_downloader = rc.getDownloaderOLD();
  
	//printf("TP SIZE: %lu\n", _tilePetitions.size());
  
	int __TODO_tune_TEXTURIZER_render_budget;
  //  if (timer != NULL) {
  //    if ( timer->elapsedTime().milliseconds() > 50 ) {
  //      return getFallBackTexturedMesh(tile, tessellator, tessellatorMesh, previousMesh);
  //    }
  //  }
  
	Mesh mesh = getNewTextureMesh(tile, tessellator, tessellatorMesh, previousMesh);
	if (mesh == null)
	{
	  //REGISTERING PETITION AND SENDING TO THE NET IF NEEDED
	  registerNewRequest(tile);
	  mesh = getNewTextureMesh(tile, tessellator, tessellatorMesh, previousMesh);
  
	  //If we still can't get a new TexturedMesh we try to get a FallBack Mesh
	  if (mesh == null)
	  {
		mesh = getFallBackTexturedMesh(tile, tessellator, tessellatorMesh, previousMesh);
	  }
	}
  
	return mesh;
  }

  public final void tileToBeDeleted(Tile tile)
  {
  
	TilePetitions tp = getRegisteredTilePetitions(tile);
  
	if (tp != null)
	{
	  if (tp.getTexID() > -1)
	  {
		_texHandler.takeTexture(tp.getTexID());
	  }
  
	  tp.cancelPetitions(_downloader);
  
	  removeRegisteredTilePetitions(tile);
	}
  
  
  }

  public final boolean tileMeetsRenderCriteria(Tile tile)
  {
	// TODO: compare tile->level with maxLevel in WMS-layer definition
	return false;
  }

  public final void justCreatedTopTile(Tile tile)
  {
	int priority = 9999; //MAX PRIORITY
	TilePetitions tp = createTilePetitions(tile);
	_tilePetitionsTopTile.add(tp); //STORED
	_tilePetitions.add(tp);
	tp.requestToNet(_downloader, priority);
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	int todo_JM;
  //  for (int i = 0; i < _tilePetitionsTopTile.size(); i++) {
  //    if (!_tilePetitionsTopTile[i]->allFinished()) {
  //      return false;
  //    }
  //  }
  //  if (_tilePetitionsTopTile.size() > 0) {
  //    _tilePetitionsTopTile.clear();
  //  }
  
	return true;
  }


}