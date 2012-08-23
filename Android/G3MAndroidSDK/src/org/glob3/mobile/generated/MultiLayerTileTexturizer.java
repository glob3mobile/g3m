package org.glob3.mobile.generated; 
//
//  MultiLayerTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

//
//  MultiLayerTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerSet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTextureBuilder;


public class MultiLayerTileTexturizer extends TileTexturizer
{
  private final LayerSet _layerSet;

  private IDownloader _downloader;
  private final TilesRenderParameters _parameters;

//  std::map<TileKey, TileTextureBuilder*> _builders;

  private float[] _texCoordsCache;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float[] getTextureCoordinates(const TileRenderContext* trc) const
  private float[] getTextureCoordinates(TileRenderContext trc)
  {
	if (_texCoordsCache == null)
	{
	  java.util.ArrayList<MutableVector2D> texCoordsV = trc.getTessellator().createUnitTextCoords();
  
	  final int texCoordsSize = texCoordsV.size();
	  float[] texCoordsA = new float[2 * texCoordsSize];
	  int p = 0;
	  for (int i = 0; i < texCoordsSize; i++)
	  {
		texCoordsA[p++] = (float) texCoordsV.get(i).x();
		texCoordsA[p++] = (float) texCoordsV.get(i).y();
	  }
  
	  texCoordsV = null;
  
	  _texCoordsCache = texCoordsA;
	}
	return _texCoordsCache;
  }

  private int _pendingTopTileRequests;

  private TexturesHandler _texturesHandler;

  public MultiLayerTileTexturizer(LayerSet layerSet)
  {
	  _layerSet = layerSet;
	  _downloader = null;
	  _parameters = null;
	  _texCoordsCache = null;
	  _pendingTopTileRequests = 0;
	  _texturesHandler = null;

  }

  public final void countTopTileRequest()
  {
	_pendingTopTileRequests--;
  }

  public void dispose()
  {
	if (_texCoordsCache != null)
	{
	  _texCoordsCache = null;
	  _texCoordsCache = null;
	}
  }

  public final boolean isReady(RenderContext rc)
  {
	final boolean isReady = _pendingTopTileRequests <= 0;
	return isReady;
  }

  public final void initialize(InitializationContext ic, TilesRenderParameters parameters)
  {
	_downloader = ic.getDownloader();
	_parameters = parameters;
  }

  public final Mesh texturize(RenderContext rc, TileRenderContext trc, Tile tile, Mesh tessellatorMesh, Mesh previousMesh)
  {
	_texturesHandler = rc.getTexturesHandler();
  
  
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
	if (builderHolder == null)
	{
	  builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this, rc, _layerSet, _parameters, _downloader, tile, tessellatorMesh, getTextureCoordinates(trc)));
	  tile.setTexturizerData(builderHolder);
	}
  
	int __TODO_tune_render_budget;
	/*
	 We user a budget for builder.start() as it can (syncronously if cached data) upload a texture
	 to the GPU, causing a bootleneck when too many tiles are renderered for the first time. (dgd)
	 */
	//  const bool startBuilder = ((tile->getLevel() == _parameters->_topLevel) ||
	//                             (trc->getStatistics()->getBuildersStartsInFrame() < 1) ||
	//                             (rc->getFrameStartTimer()->elapsedTime().milliseconds() < 33));
	//  if (startBuilder) {
	//    trc->getStatistics()->computeBuilderStartInFrame();
	//    builderHolder->get()->start();
	//    tile->setTexturizerDirty(false);
	//  }
  
  
	if (trc.isForcedFullRender())
	{
	  builderHolder.get().start();
	}
	else
	{
	  rc.getFrameTasksExecutor().addPreRenderTask(new BuilderStartTask(builderHolder.get()));
	}
  
	tile.setTexturizerDirty(false);
	return builderHolder.get().getMesh();
  
  }

  public final void tileToBeDeleted(Tile tile, Mesh mesh)
  {
  
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
	if (builderHolder != null)
	{
	  builderHolder.get().cancel();
	  builderHolder.get().cleanTile();
	  builderHolder.get().cleanMesh();
	}
	else
	{
	  if (mesh != null)
	  {
		System.out.print("break (point) on me 4\n");
	  }
	}
  }

  public final boolean tileMeetsRenderCriteria(Tile tile)
  {
	return false;
  }

  public final void justCreatedTopTile(RenderContext rc, Tile tile)
  {
	java.util.ArrayList<Petition> petitions = _layerSet.createTilePetitions(rc, tile, _parameters._tileTextureWidth, _parameters._tileTextureHeight);
  
  
	_pendingTopTileRequests += petitions.size();
  
	final int priority = 1000000000;
	for (int i = 0; i < petitions.size(); i++)
	{
	  final Petition petition = petitions.get(i);
	  _downloader.request(new URL(petition.getURL()), priority, new TopTileDownloadListener(this), true);
  
	  if (petition != null)
		  petition.dispose();
	}
  }

  public final void ancestorTexturedSolvedChanged(Tile tile, Tile ancestorTile, boolean textureSolved)
  {
	if (textureSolved == false)
	{
	  return;
	}
  
	if (tile.isTextureSolved())
	{
	  return;
	}
  
	TileTextureBuilderHolder ancestorBuilderHolder = (TileTextureBuilderHolder) ancestorTile.getTexturizerData();
	if (ancestorBuilderHolder == null)
	{
	  return;
	}
	LeveledTexturedMesh ancestorMesh = ancestorBuilderHolder.get().getMesh();
	if (ancestorMesh == null)
	{
	  return;
	}
  
	TileTextureBuilderHolder tileBuilderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
	if (tileBuilderHolder == null)
	{
	  return;
	}
	LeveledTexturedMesh tileMesh = tileBuilderHolder.get().getMesh();
	if (tileMesh == null)
	{
	  return;
	}
  
	final GLTextureID glTextureId = ancestorMesh.getTopLevelGLTextureID();
	if (glTextureId.isValid())
	{
	  _texturesHandler.retainGLTextureId(glTextureId);
  
	  final int level = tile.getLevel() - ancestorTile.getLevel() - _parameters._topLevel;
  
	  if (!tileMesh.setGLTextureIDForLevel(level, glTextureId))
	  {
		_texturesHandler.releaseGLTextureId(glTextureId);
	  }
	}
  }

  public final GLTextureID getTopLevelGLTextureIDForTile(Tile tile)
  {
	LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizerMesh();
  
	return (mesh == null) ? GLTextureID.invalid() : mesh.getTopLevelGLTextureID();
  }

//  void deleteBuilder(TileKey key,
//                     TileTextureBuilder* builder);


  public final void onTerrainTouchEvent(Geodetic3D g3d, Tile tile)
  {
	_layerSet.onTerrainTouchEvent(g3d, tile);
  }

  public final void tileMeshToBeDeleted(Tile tile, Mesh mesh)
  {
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
	if (builderHolder != null)
	{
	  builderHolder.get().cancel();
	  builderHolder.get().cleanMesh();
	}
	else
	{
	  if (mesh != null)
	  {
		System.out.print("break (point) on me 5\n");
	  }
	}
  }

}