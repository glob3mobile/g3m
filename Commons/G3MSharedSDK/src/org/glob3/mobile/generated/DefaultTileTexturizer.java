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


public class DefaultTileTexturizer extends TileTexturizer
{
  private LeveledTexturedMesh getMesh(Tile tile)
  {
    DTT_TileTextureBuilderHolder tileBuilderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
    return (tileBuilderHolder == null) ? null : tileBuilderHolder.get().getTexturedMesh();
  }

  public void dispose()
  {
    super.dispose();
  }

  public final RenderState getRenderState(LayerSet layerSet)
  {
    if (layerSet != null)
    {
      return layerSet.getRenderState();
    }
    return RenderState.ready();
  }

  public final void initialize(G3MContext context, TilesRenderParameters parameters)
  {
    // do nothing
  }

  public final Mesh texturize(G3MRenderContext rc, TileTessellator tessellator, TileRasterizer tileRasterizer, LayerTilesRenderParameters layerTilesRenderParameters, LayerSet layerSet, boolean forceFullRender, long tileDownloadPriority, Tile tile, Mesh tessellatorMesh, Mesh previousMesh, boolean logTilesPetitions)
  {
    DTT_TileTextureBuilderHolder builderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
  
  //  TileImageProvider* tileImageProvider = new DebugTileImageProvider();
  //  TileImageProvider* tileImageProvider = new ChessboardTileImageProvider();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: creates the TileImageProvider from the LayerSet (&& Rasterizer?)
    TileImageProvider tileImageProvider = layerSet.getTileImageProvider(rc, layerTilesRenderParameters);
  
    if (tileImageProvider == null)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: error callback
      tile.setTextureSolved(true);
      tile.setTexturizerDirty(false);
      return null;
    }
  
    DTT_TileTextureBuilder builder;
    if (builderHolder == null)
    {
      builder = new DTT_TileTextureBuilder(rc, layerTilesRenderParameters, tileImageProvider, tile, tessellatorMesh, tessellator, tileDownloadPriority, logTilesPetitions, rc.getFrameTasksExecutor()); // this,
                                           // tileRasterizer,
                                           // layerSet->createTileMapPetitions(rc,
                                           //                                  layerTilesRenderParameters,
                                           //                                  tile),
                                           // rc->getDownloader(),
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
  //    builder->cleanTile();
  //    builder->cleanTexturedMesh();
    }
  }

  public final void tileMeshToBeDeleted(Tile tile, Mesh mesh)
  {
    DTT_TileTextureBuilderHolder builderHolder = (DTT_TileTextureBuilderHolder) tile.getTexturizerData();
    if (builderHolder != null)
    {
      DTT_TileTextureBuilder builder = builderHolder.get();
      builder.cancel(false); // cleanTile
  //    builder->cleanTexturedMesh();
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
    if (layerSet == null)
    {
      return false;
    }
  
    return layerSet.onTerrainTouchEvent(ec, position, tile);
  }

//  const TextureIDReference* getTopLevelTextureIdForTile(Tile* tile);

}