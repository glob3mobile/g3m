package org.glob3.mobile.generated;//
//  DefaultTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/16/14.
//
//

//
//  DefaultTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/16/14.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LeveledTexturedMesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureIDReference;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;


public class DefaultTileTexturizer extends TileTexturizer
{

  private IImageBuilder _defaultBackgroundImageBuilder;
  private boolean _defaultBackgroundImageLoaded;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IImage _defaultBackgroundImage;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IImage _defaultBackgroundImage = new internal();
//#endif
  private String _defaultBackgroundImageName;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LeveledTexturedMesh* getMesh(Tile* tile) const
  private LeveledTexturedMesh getMesh(Tile tile)
  {
	DTT_TileTextureBuilderHolder tileBuilderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
	return (tileBuilderHolder == null) ? null : tileBuilderHolder.get().getTexturedMesh();
  }


  public java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();


  public DefaultTileTexturizer(IImageBuilder defaultBackgroundImageBuilder)
  {
	  _defaultBackgroundImageBuilder = defaultBackgroundImageBuilder;
	  _defaultBackgroundImageLoaded = false;
	ILogger.instance().logInfo("Create texturizer...");
  
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final RenderState getRenderState(LayerSet layerSet)
  {
	//ILogger::instance()->logInfo("Check render state texturizer...");
	if (_errors.size() > 0)
	{
	  return RenderState.error(_errors);
	}
	if (!_defaultBackgroundImageLoaded)
	{
	  return RenderState.busy();
	}
	if (layerSet != null)
	{
	  return layerSet.getRenderState();
	}
	return RenderState.ready();
  }

  public final void initialize(G3MContext context, TilesRenderParameters parameters)
  {
	ILogger.instance().logInfo("Initializing texturizer...");
  
	_defaultBackgroundImageBuilder.build(context, new DTT_IImageBuilderListener(this), true);
  
	// do nothing
  }

  public final Mesh texturize(G3MRenderContext rc, PlanetRenderContext prc, Tile tile, Mesh tessellatorMesh, Mesh previousMesh)
  {
	DTT_TileTextureBuilderHolder builderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
  
	TileImageProvider tileImageProvider = prc._layerSet.getTileImageProvider(rc, prc._layerTilesRenderParameters);
  
	if (tileImageProvider == null)
	{
	  tile.setTextureSolved(true);
	  tile.setTexturizerDirty(false);
	  return null;
	}
  
	DTT_TileTextureBuilder builder;
	if (builderHolder == null)
	{
	  final long tileTexturePriority = (prc._tilesRenderParameters._incrementalTileQuality ? prc._tileDownloadPriority + prc._layerTilesRenderParameters._maxLevel - tile._level : prc._tileDownloadPriority + tile._level);
  
	  builder = new DTT_TileTextureBuilder(rc, prc._layerTilesRenderParameters, tileImageProvider, tile, tessellatorMesh, prc._tessellator, tileTexturePriority, prc._logTilesPetitions, rc.getFrameTasksExecutor(), _defaultBackgroundImage, _defaultBackgroundImageName);
	  builderHolder = new DTT_TileTextureBuilderHolder(builder);
	  tile.setTexturizerData(builderHolder);
	}
	else
	{
	  builder = builderHolder.get();
	}
  
	// get the mesh just before calling start(), if not... the start(ed) task can finish immediately
	// and as one consequence the builder got deleted and the "builder" pointer becomes a dangling pointer
	Mesh texturizedMesh = builder.getTexturedMesh();
  
	rc.getFrameTasksExecutor().addPreRenderTask(new DTT_TileTextureBuilderStartTask(builder));
  
	tile.setTexturizerDirty(false);
  
	return texturizedMesh;
  }

  public final void tileToBeDeleted(Tile tile, Mesh mesh)
  {
	DTT_TileTextureBuilderHolder builderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
	if (builderHolder != null)
	{
	  DTT_TileTextureBuilder builder = builderHolder.get();
	  builder.cancel(true); // cleanTile
	}
  }

  public final void tileMeshToBeDeleted(Tile tile, Mesh mesh)
  {
	DTT_TileTextureBuilderHolder builderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
	if (builderHolder != null)
	{
	  DTT_TileTextureBuilder builder = builderHolder.get();
	  builder.cancel(false); // cleanTile
	}
  }

  public final void justCreatedTopTile(G3MRenderContext rc, Tile tile, LayerSet layerSet)
  {
	// do nothing
  }

  public final void ancestorTexturedSolvedChanged(Tile tile, Tile ancestorTile, boolean textureSolved)
  {
	if (!textureSolved)
	{
	  return;
	}
  
	if (tile.isTextureSolved())
	{
	  return;
	}
  
	LeveledTexturedMesh ancestorMesh = getMesh(ancestorTile);
	if (ancestorMesh == null)
	{
	  return;
	}
  
	final TextureIDReference glTextureId = ancestorMesh.getTopLevelTextureId();
	if (glTextureId == null)
	{
	  return;
	}
  
	LeveledTexturedMesh tileMesh = getMesh(tile);
	if (tileMesh == null)
	{
	  return;
	}
  
	final TextureIDReference glTextureIdRetainedCopy = glTextureId.createCopy();
  
	final int level = tile._level - ancestorTile._level;
	if (!tileMesh.setGLTextureIdForLevel(level, glTextureIdRetainedCopy))
	{
	  if (glTextureIdRetainedCopy != null)
		  glTextureIdRetainedCopy.dispose();
	}
  }

  public final boolean onTerrainTouchEvent(G3MEventContext ec, Geodetic3D position, Tile tile, LayerSet layerSet)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return (layerSet == null) ? false : layerSet->onTerrainTouchEvent(ec, position, tile);
	return (layerSet == null) ? false : layerSet.onTerrainTouchEvent(ec, new Geodetic3D(position), tile);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IImageBuilder* getDefaultBackgroundImageBuilder() const
  public final IImageBuilder getDefaultBackgroundImageBuilder()
  {
	return _defaultBackgroundImageBuilder;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IImage* getDefaultBackgroundImage() const
  public final IImage getDefaultBackgroundImage()
  {
	return _defaultBackgroundImage;
  }

  public final void setDefaultBackgroundImage(IImage defaultBackgroundImage)
  {
	_defaultBackgroundImage = defaultBackgroundImage;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getDefaultBackgroundImageName() const
  public final String getDefaultBackgroundImageName()
  {
	return _defaultBackgroundImageName;
  }

  public final void setDefaultBackgroundImageName(String defaultBackgroundImageName)
  {
	_defaultBackgroundImageName = defaultBackgroundImageName;
  }

  public final void setDefaultBackgroundImageLoaded(boolean defaultBackgroundImageLoaded)
  {
	_defaultBackgroundImageLoaded = defaultBackgroundImageLoaded;
  }

}
