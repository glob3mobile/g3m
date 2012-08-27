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
//class TileTextureBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerSet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;

public class MultiLayerTileTexturizer extends TileTexturizer
{
  private final LayerSet _layerSet;
  private TilesRenderParameters _parameters;

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
	_parameters = parameters;
  }

  public final Mesh texturize(RenderContext rc, TileRenderContext trc, Tile tile, Mesh tessellatorMesh, Mesh previousMesh)
  {
	_texturesHandler = rc.getTexturesHandler();
  
  
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
	if (builderHolder == null)
	{
	  builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this, rc, _layerSet, _parameters, rc.getDownloader(), tile, tessellatorMesh, getTextureCoordinates(trc)));
	  tile.setTexturizerData(builderHolder);
	}
  
	if (trc.isForcedFullRender())
	{
	  builderHolder.get().start();
	}
	else
	{
//C++ TO JAVA CONVERTER TODO TASK: Java does not allow declaring types within methods:
//	  class BuilderStartTask : public FrameTask
//	  {
//	  private:
//		TileTextureBuilder* _builder;
//  
//	  public:
//		BuilderStartTask(TileTextureBuilder* builder) : _builder(builder)
//		{
//		  _builder->_retain();
//		}
//  
//		virtual ~BuilderStartTask()
//		{
//		  _builder->_release();
//		}
//  
//		void execute(const RenderContext* rc)
//		{
//		  _builder->start();
//		}
//  
//		boolean isCanceled(const RenderContext *rc)
//		{
//		  return _builder->isCanceled();
//		}
//	  };
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
	java.util.ArrayList<Petition> petitions = _layerSet.createTileMapPetitions(rc, tile, _parameters._tileTextureWidth, _parameters._tileTextureHeight);
  
  
	_pendingTopTileRequests += petitions.size();
  
	final int priority = 1000000000;
	for (int i = 0; i < petitions.size(); i++)
	{
	  final Petition petition = petitions.get(i);
	  rc.getDownloader().request(new URL(petition.getURL()), priority, new TopTileDownloadListener(this), true);
  
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
  
	final GLTextureId glTextureId = ancestorMesh.getTopLevelGLTextureId();
	if (glTextureId.isValid())
	{
	  _texturesHandler.retainGLTextureId(glTextureId);
  
	  final int level = tile.getLevel() - ancestorTile.getLevel() - _parameters._topLevel;
  
	  if (!tileMesh.setGLTextureIdForLevel(level, glTextureId))
	  {
		_texturesHandler.releaseGLTextureId(glTextureId);
	  }
	}
  }

  public final GLTextureId getTopLevelGLTextureIdForTile(Tile tile)
  {
	LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizerMesh();
  
	return (mesh == null) ? GLTextureId.invalid() : mesh.getTopLevelGLTextureId();
  }

  public final void onTerrainTouchEvent(EventContext ec, Geodetic3D position, Tile tile)
  {
	_layerSet.onTerrainTouchEvent(ec, position, tile);
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