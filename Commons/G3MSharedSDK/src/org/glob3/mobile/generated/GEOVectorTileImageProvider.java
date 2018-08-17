package org.glob3.mobile.generated;import java.util.*;

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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOVectorLayer;

public class GEOVectorTileImageProvider extends TileImageProvider
{

  public static class GEORasterizerCanvasImageListener implements IImageListener
  {
	private final TileImageContribution _contribution;
	private final String _tileId;

	private TileImageListener _listener;
	private boolean _deleteListener;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getImageId(const String& tileId) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean visitElement(const Sector& sector, const QuadTree_Content* content) const
	public final boolean visitElement(Sector sector, QuadTree_Content content)
	{
	  GEORasterSymbol symbol = (GEORasterSymbol) content;
	  symbol.rasterize(_canvas, _projection, _tileLevel);
	  return false;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void endVisit(boolean aborted) const
	public final void endVisit(boolean aborted)
	{
    
	}

  }


  public static class GEORasterizerFrameTask extends FrameTask
  {
	private GEOVectorTileImageProvider _geoVectorTileImageProvider;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final TileImageContribution _contribution;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public TileImageContribution _contribution = new internal();
//#endif
	private final String _tileId;
	private final Sector _tileSector = new Sector();
	private final boolean _tileMercator;
	private final int _tileLevel;
	private final int _resolutionWidth;
	private final int _resolutionHeight;
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
		_resolutionWidth = resolution._x;
		_resolutionHeight = resolution._y;
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
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
	}

	public final boolean isCanceled(G3MRenderContext rc)
	{
	  return _isCanceled;
	}

	public final void execute(G3MRenderContext rc)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _geoVectorTileImageProvider->rasterize(_contribution, _tileId, _tileSector, _tileMercator, _tileLevel, _resolutionWidth, _resolutionHeight, _listener, _deleteListener);
	  _geoVectorTileImageProvider.rasterize(_contribution, _tileId, new Sector(_tileSector), _tileMercator, _tileLevel, _resolutionWidth, _resolutionHeight, _listener, _deleteListener);
	  _listener = null; // moves ownership to _geoVectorTileImageProvider
	  _contribution = null; // moves ownership to _geoVectorTileImageProvider
	}

	public final void cancel()
	{
	  _isCanceled = true;
	  _listener.imageCreationCanceled(_tileId);
	}
  }


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GEOVectorLayer _layer;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public GEOVectorLayer _layer = new internal();
//#endif

  private final java.util.HashMap<String, GEORasterizerFrameTask> _rasterizers = new java.util.HashMap<String, GEORasterizerFrameTask>();


  public GEOVectorTileImageProvider(GEOVectorLayer layer)
  {
	  _layer = layer;
  }

  public final TileImageContribution contribution(Tile tile)
  {
	return (_layer == null) ? null : _layer.contribution(tile);
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	if (_rasterizers.containsKey(tileId))
	{
	  GEORasterizerFrameTask rasterizer = _rasterizers.get(tileId);
	  rasterizer.cancel();
	}
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final GEORasterizerFrameTask rasterizer = _rasterizers.get(tileId);
	if (rasterizer != null)
	{
	  rasterizer.cancel();
	}
//#endif
  }

  public final void rasterizerDeleted(String tileId)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	if (_rasterizers.containsKey(tileId))
	{
	  _rasterizers.remove(tileId);
	}
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_rasterizers.remove(tileId);
//#endif
  }

  public final void rasterize(TileImageContribution contribution, String tileId, Sector tileSector, boolean tileMercator, int tileLevel, int resolutionWidth, int resolutionHeight, TileImageListener listener, boolean deleteListener)
  {
	ICanvas canvas = IFactory.instance().createCanvas(false);
	canvas.initialize(resolutionWidth, resolutionHeight);
  
	GEORasterProjection projection = new GEORasterProjection(tileSector, tileMercator, resolutionWidth, resolutionHeight);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _layer->getQuadTree().acceptVisitor(tileSector, GEORasterizerQuadTreeVisitor(canvas, projection, tileLevel));
	_layer.getQuadTree().acceptVisitor(new Sector(tileSector), new GEORasterizerQuadTreeVisitor(canvas, projection, tileLevel));
  
	if (projection != null)
		projection.dispose();
  
	canvas.createImage(new GEORasterizerCanvasImageListener(contribution, tileId, listener, deleteListener), true); // autodelete
  
	if (canvas != null)
		canvas.dispose();
  }

  public final void layerDeleted(GEOVectorLayer layer)
  {
	if (layer != _layer)
	{
	  THROW_EXCEPTION("Logic error");
	}
	_layer = null;
  }

}
