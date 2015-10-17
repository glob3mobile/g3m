package org.glob3.mobile.generated; 
//
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



//class LeveledTexturedMesh;
//class TextureIDReference;
//class IImageBuilder;
//class IImage;


public class DefaultTileTexturizer extends TileTexturizer
{

  private IImageBuilder _defaultBackGroundImageBuilder;
  private boolean _defaultBackGroundImageLoaded;
  private IImage _defaultBackGroundImage;
  private String _defaultBackGroundImageName;

  private LeveledTexturedMesh getMesh(Tile tile)
  {
    DTT_TileTextureBuilderHolder tileBuilderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
    return (tileBuilderHolder == null) ? null : tileBuilderHolder.get().getTexturedMesh();
  }


  public java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();


  public DefaultTileTexturizer(IImageBuilder defaultBackGroundImageBuilder)
  {
     _defaultBackGroundImageBuilder = defaultBackGroundImageBuilder;
     _defaultBackGroundImageLoaded = false;
    ILogger.instance().logInfo("Create texturizer...");
  
  }

  public void dispose()
  {
    super.dispose();
  }

  public final RenderState getRenderState(LayerSet layerSet)
  {
    //ILogger::instance()->logInfo("Check render state texturizer...");
    if (_errors.size() > 0)
    {
      return RenderState.error(_errors);
    }
    if (!_defaultBackGroundImageLoaded)
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
  
    _defaultBackGroundImageBuilder.build(context, new DTT_IImageBuilderListener(this), true);
  
    // do nothing
  }

  public final Mesh texturize(G3MRenderContext rc, TileTessellator tessellator, LayerTilesRenderParameters layerTilesRenderParameters, LayerSet layerSet, boolean forceFullRender, long tileDownloadPriority, Tile tile, Mesh tessellatorMesh, Mesh previousMesh, boolean logTilesPetitions)
  {
    DTT_TileTextureBuilderHolder builderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
  
    TileImageProvider tileImageProvider = layerSet.getTileImageProvider(rc, layerTilesRenderParameters);
  
    if (tileImageProvider == null)
    {
      tile.setTextureSolved(true);
      tile.setTexturizerDirty(false);
      return null;
    }
  
    DTT_TileTextureBuilder builder;
    if (builderHolder == null)
    {
      builder = new DTT_TileTextureBuilder(rc, layerTilesRenderParameters, tileImageProvider, tile, tessellatorMesh, tessellator, tileDownloadPriority, logTilesPetitions, rc.getFrameTasksExecutor(), _defaultBackGroundImage, _defaultBackGroundImageName);
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
  
    if (forceFullRender)
    {
      builder.start();
    }
    else
    {
      rc.getFrameTasksExecutor().addPreRenderTask(new DTT_TileTextureBuilderStartTask(builder));
    }
  
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

  public final boolean tileMeetsRenderCriteria(Tile tile)
  {
    return false;
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
    return (layerSet == null) ? false : layerSet.onTerrainTouchEvent(ec, position, tile);
  }

  public final IImageBuilder getDefaultBackGroundImageBuilder()
  {
    return _defaultBackGroundImageBuilder;
  }

  public final IImage getDefaultBackGroundImage()
  {
    return _defaultBackGroundImage;
  }

  public final void setDefaultBackGroundImage(IImage defaultBackGroundImage)
  {
    _defaultBackGroundImage = defaultBackGroundImage;
  }

  public final String getDefaultBackGroundImageName()
  {
    return _defaultBackGroundImageName;
  }

  public final void setDefaultBackGroundImageName(String defaultBackGroundImageName)
  {
    _defaultBackGroundImageName = defaultBackGroundImageName;
  }

  public final void setDefaultBackGroundImageLoaded(boolean defaultBackGroundImageLoaded)
  {
    _defaultBackGroundImageLoaded = defaultBackGroundImageLoaded;
  }

}