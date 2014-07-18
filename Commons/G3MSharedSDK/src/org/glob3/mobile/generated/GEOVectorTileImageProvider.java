package org.glob3.mobile.generated; 
//
//  GEOVectorTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/17/14.
//
//

//
//  GEOVectorTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/17/14.
//
//




//class GEOVectorLayer;
//class IThreadUtils;

public class GEOVectorTileImageProvider extends TileImageProvider
{

  public static class GEORasterizerCanvasImageListener extends IImageListener
  {
    private final TileImageContribution _contribution;
    private final String _tileId;

    private TileImageListener _listener;
    private boolean _deleteListener;

    private String getImageId(String tileId)
    {
      IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString("GEOVectorTileImageProvider/");
      isb.addString(tileId);
      final String s = isb.getString();
      if (isb != null)
         isb.dispose();
      return s;
    }

    public GEORasterizerCanvasImageListener(TileImageContribution contribution, String tileId, TileImageListener listener, boolean deleteListener)
    {
       _contribution = contribution;
       _tileId = tileId;
       _listener = listener;
       _deleteListener = deleteListener;
    }

    public final void imageCreated(IImage image)
    {
      _listener.imageCreated(_tileId, image, getImageId(_tileId), _contribution);
      if (_deleteListener)
      {
        if (_listener != null)
           _listener.dispose();
      }
    }
  }


  public static class GEORasterizerQuadTreeVisitor extends QuadTreeVisitor
  {
    private ICanvas _canvas;
    private GEORasterProjection _projection;
    private final int _tileLevel;

    public GEORasterizerQuadTreeVisitor(ICanvas canvas, GEORasterProjection projection, int tileLevel)
    {
       _canvas = canvas;
       _projection = projection;
       _tileLevel = tileLevel;
    }

    public final boolean visitElement(Sector sector, QuadTree_Content content)
    {
      GEORasterSymbol symbol = (GEORasterSymbol) content;
      symbol.rasterize(_canvas, _projection, _tileLevel);
      return false;
    }

    public final void endVisit(boolean aborted)
    {
    
    }

  }


  public static class GEORasterizerFrameTask extends FrameTask
  {
    private GEOVectorTileImageProvider _geoVectorTileImageProvider;
    private final TileImageContribution _contribution;
    private final String _tileId;
    private final Sector _tileSector ;
    private final boolean _tileMercator;
    private final int _tileLevel;
    private final Vector2I _resolution = new Vector2I();
    private TileImageListener _listener;
    private final boolean _deleteListener;
    private boolean _isCanceled;

    public GEORasterizerFrameTask(GEOVectorTileImageProvider geoVectorTileImageProvider, TileImageContribution contribution, String tileId, Sector tileSector, boolean tileMercator, int tileLevel, Vector2I resolution, TileImageListener listener, boolean deleteListener)
    {
       _geoVectorTileImageProvider = geoVectorTileImageProvider;
       _contribution = contribution;
       _tileId = tileId;
       _tileSector = new Sector(tileSector);
       _tileMercator = tileMercator;
       _tileLevel = tileLevel;
       _resolution = new Vector2I(resolution);
       _listener = listener;
       _deleteListener = deleteListener;
       _isCanceled = false;
      _geoVectorTileImageProvider._retain();
    }

    public void dispose()
    {
      _geoVectorTileImageProvider.rasterizerDeleted(_tileId);
      _geoVectorTileImageProvider._release();
    
      if (_deleteListener)
      {
        if (_listener != null)
           _listener.dispose();
      }
    
      if (_contribution != null)
      {
        _contribution._release();
      }
    
      super.dispose();
    }

    public final boolean isCanceled(G3MRenderContext rc)
    {
      return _isCanceled;
    }

    public final void execute(G3MRenderContext rc)
    {
      _geoVectorTileImageProvider.rasterize(_contribution, _tileId, _tileSector, _tileMercator, _tileLevel, _resolution, _listener, _deleteListener);
      _listener = null; // moves ownership to _geoVectorTileImageProvider
      _contribution = null; // moves ownership to _geoVectorTileImageProvider
    }

    public final void cancel()
    {
      _isCanceled = true;
    }
  }


  private final GEOVectorLayer _layer;
  private final IThreadUtils _threadUtils;

  private final java.util.HashMap<String, GEORasterizerFrameTask> _rasterizers = new java.util.HashMap<String, GEORasterizerFrameTask>();



  ///#include "TileImageProvider.hpp"
  
  public GEOVectorTileImageProvider(GEOVectorLayer layer, IThreadUtils threadUtils)
  {
     _layer = layer;
     _threadUtils = threadUtils;
  
  }

  public final TileImageContribution contribution(Tile tile)
  {
    return _layer.contribution(tile);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor)
  {
  
    final String tileId = tile._id;
    GEORasterizerFrameTask rasterizer = new GEORasterizerFrameTask(this, contribution, tileId, tile._sector, tile._mercator, tile._level, resolution, listener, deleteListener);
    _rasterizers.put(tileId, rasterizer);
  
    frameTasksExecutor.addPreRenderTask(rasterizer);
  }

  public final void cancel(String tileId)
  {
    final GEORasterizerFrameTask rasterizer = _rasterizers.get(tileId);
    if (rasterizer != null) {
      rasterizer.cancel();
    }
  }

  public final void rasterizerDeleted(String tileId)
  {
    _rasterizers.remove(tileId);
  //  final GEORasterizerFrameTask rasterizer = _rasterizers.remove(tileId);
  //  if (rasterizer != null) {
  //    rasterizer.dispose();
  //  }
  }

  public final void rasterize(TileImageContribution contribution, String tileId, Sector tileSector, boolean tileMercator, int tileLevel, Vector2I resolution, TileImageListener listener, boolean deleteListener)
  {
    final int width = resolution._x;
    final int height = resolution._y;
  
    ICanvas canvas = IFactory.instance().createCanvas();
    canvas.initialize(width, height);
  
    GEORasterProjection projection = new GEORasterProjection(tileSector, tileMercator, width, height);
  
    _layer.getQuadTree().acceptVisitor(tileSector, new GEORasterizerQuadTreeVisitor(canvas, projection, tileLevel));
  
    if (projection != null)
       projection.dispose();
  
    canvas.createImage(new GEORasterizerCanvasImageListener(contribution, tileId, listener, deleteListener), true); // autodelete
  
    if (canvas != null)
       canvas.dispose();
  }

}