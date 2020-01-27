package org.glob3.mobile.generated;
//
//  GEOVectorTileImageProvider.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/17/14.
//
//

//
//  GEOVectorTileImageProvider.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/17/14.
//
//




//class GEOVectorLayer;

public class GEOVectorTileImageProvider extends TileImageProvider
{

  public static class GEORasterizerCanvasImageListener extends IImageListener
  {
    private final TileImageContribution _contribution;
    private final String _tileID;

    private TileImageListener _listener;
    private boolean _deleteListener;

    private String getImageID(String tileID)
    {
      IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString("GEOVectorTileImageProvider/");
      isb.addString(tileID);
      final String s = isb.getString();
      if (isb != null)
         isb.dispose();
      return s;
    }

    public GEORasterizerCanvasImageListener(TileImageContribution contribution, String tileID, TileImageListener listener, boolean deleteListener)
    {
       _contribution = contribution;
       _tileID = tileID;
       _listener = listener;
       _deleteListener = deleteListener;
    }

    public final void imageCreated(IImage image)
    {
      _listener.imageCreated(_tileID, image, getImageID(_tileID), _contribution);
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
    private TileImageContribution _contribution;
    private final String _tileID;
    private final Sector _tileSector ;
    private final boolean _tileMercator;
    private final int _tileLevel;
    private final short _resolutionWidth;
    private final short _resolutionHeight;
    private TileImageListener _listener;
    private final boolean _deleteListener;
    private boolean _isCanceled;

    public GEORasterizerFrameTask(GEOVectorTileImageProvider geoVectorTileImageProvider, TileImageContribution contribution, String tileID, Sector tileSector, boolean tileMercator, int tileLevel, Vector2S resolution, TileImageListener listener, boolean deleteListener)
    {
       _geoVectorTileImageProvider = geoVectorTileImageProvider;
       _contribution = contribution;
       _tileID = tileID;
       _tileSector = new Sector(tileSector);
       _tileMercator = tileMercator;
       _tileLevel = tileLevel;
       _resolutionWidth = resolution._x;
       _resolutionHeight = resolution._y;
       _listener = listener;
       _deleteListener = deleteListener;
       _isCanceled = false;
      _geoVectorTileImageProvider._retain();
    }

    public void dispose()
    {
      _geoVectorTileImageProvider.rasterizerDeleted(_tileID);
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
      _geoVectorTileImageProvider.rasterize(_contribution, _tileID, _tileSector, _tileMercator, _tileLevel, _resolutionWidth, _resolutionHeight, _listener, _deleteListener);
      _listener = null; // moves ownership to _geoVectorTileImageProvider
      _contribution = null; // moves ownership to _geoVectorTileImageProvider
    }

    public final void cancel()
    {
      _isCanceled = true;
      _listener.imageCreationCanceled(_tileID);
    }
  }


  private GEOVectorLayer _layer;

  private final java.util.HashMap<String, GEORasterizerFrameTask> _rasterizers = new java.util.HashMap<String, GEORasterizerFrameTask>();


  public GEOVectorTileImageProvider(GEOVectorLayer layer)
  {
     _layer = layer;
  }

  public final TileImageContribution contribution(Tile tile)
  {
    return (_layer == null) ? null : _layer.contribution(tile);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2S resolution, long tileTextureDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor)
  {
  
    final String tileID = tile._id;
    GEORasterizerFrameTask rasterizer = new GEORasterizerFrameTask(this, contribution, tileID, tile._sector, tile._mercator, tile._level, resolution, listener, deleteListener);
    _rasterizers.put(tileID, rasterizer);
  
    frameTasksExecutor.addPreRenderTask(rasterizer);
  }

  public final void cancel(String tileID)
  {
    final GEORasterizerFrameTask rasterizer = _rasterizers.get(tileID);
    if (rasterizer != null) {
      rasterizer.cancel();
    }
  }

  public final void rasterizerDeleted(String tileID)
  {
    _rasterizers.remove(tileID);
  }

  public final void rasterize(TileImageContribution contribution, String tileID, Sector tileSector, boolean tileMercator, int tileLevel, int resolutionWidth, int resolutionHeight, TileImageListener listener, boolean deleteListener)
  {
    ICanvas canvas = IFactory.instance().createCanvas(false);
    canvas.initialize(resolutionWidth, resolutionHeight);
  
    GEORasterProjection projection = new GEORasterProjection(tileSector, tileMercator, resolutionWidth, resolutionHeight);
  
    _layer.getQuadTree().acceptVisitor(tileSector, new GEORasterizerQuadTreeVisitor(canvas, projection, tileLevel));
  
    if (projection != null)
       projection.dispose();
  
    canvas.createImage(new GEORasterizerCanvasImageListener(contribution, tileID, listener, deleteListener), true); // autodelete
  
    if (canvas != null)
       canvas.dispose();
  }

  public final void layerDeleted(GEOVectorLayer layer)
  {
    if (layer != _layer)
    {
      throw new RuntimeException("Logic error");
    }
    _layer = null;
  }

}
