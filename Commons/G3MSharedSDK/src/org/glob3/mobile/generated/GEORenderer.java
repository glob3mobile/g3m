package org.glob3.mobile.generated;import java.util.*;

//
//  GEORenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEORenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOSymbolizer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MeshRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarksRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ShapesRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOVectorLayer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORenderer_ObjectSymbolizerPair;

public class GEORenderer extends DefaultRenderer
{

  private static class LoadQueueItem
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	public final URL _url = new URL();
	public final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final URL _url = new public();
	public final TimeInterval _timeToCache = new public();
//#endif
	public GEOSymbolizer _symbolizer;
	public final long _priority;
	public final boolean _readExpired;
	public final boolean _isBSON;

	public LoadQueueItem(URL url, GEOSymbolizer symbolizer, long priority, TimeInterval timeToCache, boolean readExpired, boolean isBSON)
	{
		_url = new URL(url);
		_symbolizer = symbolizer;
		_priority = priority;
		_timeToCache = new TimeInterval(timeToCache);
		_readExpired = readExpired;
		_isBSON = isBSON;

	}

	public void dispose()
	{
	}
  }

  private void drainLoadQueue()
  {
	final int loadQueueSize = _loadQueue.size();
	for (int i = 0; i < loadQueueSize; i++)
	{
	  LoadQueueItem item = _loadQueue.get(i);
	  requestBuffer(item._url, item._symbolizer, item._priority, item._timeToCache, item._readExpired, item._isBSON);
	  if (item != null)
		  item.dispose();
	}
  
	_loadQueue.clear();
  }

  private void cleanLoadQueue()
  {
	final int loadQueueSize = _loadQueue.size();
	for (int i = 0; i < loadQueueSize; i++)
	{
	  LoadQueueItem item = _loadQueue.get(i);
	  if (item != null)
		  item.dispose();
	}
  
	_loadQueue.clear();
  }


  private java.util.ArrayList<GEORenderer_ObjectSymbolizerPair> _children = new java.util.ArrayList<GEORenderer_ObjectSymbolizerPair>();

  private final GEOSymbolizer _defaultSymbolizer;

  private MeshRenderer _meshRenderer;
  private ShapesRenderer _shapesRenderer;
  private MarksRenderer _marksRenderer;
  private GEOVectorLayer _geoVectorLayer;

  private java.util.ArrayList<LoadQueueItem> _loadQueue = new java.util.ArrayList<LoadQueueItem>();

  private void requestBuffer(URL url, GEOSymbolizer symbolizer, long priority, TimeInterval timeToCache, boolean readExpired, boolean isBSON)
  {
  //  ILogger::instance()->logInfo("Requesting GEOObject from \"%s\"", url._path.c_str());
	IDownloader downloader = _context.getDownloader();
	downloader.requestBuffer(url, priority, timeToCache, readExpired, new GEORenderer_GEOObjectBufferDownloadListener(this, symbolizer, _context.getThreadUtils(), isBSON), true);
  }


  /**
   Creates a GEORenderer.

   defaultSymbolizer: Default Symbolizer, can be NULL.  In case of NULL, one instance of GEOSymbolizer must be passed in every call to addGEOObject();

   meshRenderer:   Can be NULL as long as no GEOMarkSymbol is used in any symbolizer.
   shapesRenderer: Can be NULL as long as no GEOShapeSymbol is used in any symbolizer.
   marksRenderer:  Can be NULL as long as no GEOMeshSymbol is used in any symbolizer.
   geoVectorLayer: Can be NULL as long as no GEORasterSymbol is used in any symbolizer.

   */
  public GEORenderer(GEOSymbolizer defaultSymbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
	  _defaultSymbolizer = defaultSymbolizer;
	  _meshRenderer = meshRenderer;
	  _shapesRenderer = shapesRenderer;
	  _marksRenderer = marksRenderer;
	  _geoVectorLayer = geoVectorLayer;
	initialize(null);
  }

  public void dispose()
  {
	if (_defaultSymbolizer != null)
		_defaultSymbolizer.dispose();
  
	final int childrenCount = _children.size();
	for (int i = 0; i < childrenCount; i++)
	{
	  GEORenderer_ObjectSymbolizerPair pair = _children.get(i);
	  if (pair != null)
		  pair.dispose();
	}
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  /**
   Add a new GEOObject.

   symbolizer: The symbolizer to be used for the given geoObject.  Can be NULL as long as a defaultSymbolizer was given in the GEORenderer constructor.
   */
  public final void addGEOObject(GEOObject geoObject)
  {
	  addGEOObject(geoObject, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void addGEOObject(GEOObject* geoObject, GEOSymbolizer* symbolizer = null)
  public final void addGEOObject(GEOObject geoObject, GEOSymbolizer symbolizer)
  {
	if ((symbolizer == null) && (_defaultSymbolizer == null))
	{
	  ILogger.instance().logError("Can't add a geoObject without a symbolizer if the defaultSymbolizer was not given in the GEORenderer constructor");
	  if (geoObject != null)
		  geoObject.dispose();
	}
	else
	{
	  _children.add(new GEORenderer_ObjectSymbolizerPair(geoObject, symbolizer));
	}
  }

  public final void onChangedContext()
  {
	if (_context != null)
	{
	  drainLoadQueue();
	}
  }

  public final void onLostContext()
  {
	if (_context == null)
	{
	  cleanLoadQueue();
	}
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
	final int childrenCount = _children.size();
	if (childrenCount > 0)
	{
	  for (int i = 0; i < childrenCount; i++)
	  {
		final GEORenderer_ObjectSymbolizerPair pair = _children.get(i);
  
		if (pair._geoObject != null)
		{
		  final GEOSymbolizer symbolizer = (pair._symbolizer == null) ? _defaultSymbolizer : pair._symbolizer;
  
		  pair._geoObject.symbolize(rc, symbolizer, _meshRenderer, _shapesRenderer, _marksRenderer, _geoVectorLayer);
		}
  
		if (pair != null)
			pair.dispose();
	  }
	  _children.clear();
	}
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MeshRenderer* getMeshRenderer() const
  public final MeshRenderer getMeshRenderer()
  {
	return _meshRenderer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MarksRenderer* getMarksRenderer() const
  public final MarksRenderer getMarksRenderer()
  {
	return _marksRenderer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ShapesRenderer* getShapesRenderer() const
  public final ShapesRenderer getShapesRenderer()
  {
	return _shapesRenderer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOVectorLayer* getGEOVectorLayer() const
  public final GEOVectorLayer getGEOVectorLayer()
  {
	return _geoVectorLayer;
  }

  public final void setGEOVectorLayer(GEOVectorLayer geoVectorLayer, boolean deletePrevious)
  {
	if (geoVectorLayer != _geoVectorLayer)
	{
	  if (deletePrevious)
	  {
		if (_geoVectorLayer != null)
			_geoVectorLayer.dispose();
	  }
	  _geoVectorLayer = geoVectorLayer;
	}
  }

  public final void loadJSON(URL url)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadJSON(url, null, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true);
	loadJSON(new URL(url), null, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void loadJSON(URL url, GEOSymbolizer symbolizer)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadJSON(url, symbolizer, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true);
	loadJSON(new URL(url), symbolizer, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void loadJSON(URL url, GEOSymbolizer symbolizer, long priority, TimeInterval timeToCache, boolean readExpired)
  {
	if (_context == null)
	{
	  _loadQueue.add(new LoadQueueItem(url, symbolizer, priority, timeToCache, readExpired, false)); // isBson
	}
	else
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: requestBuffer(url, symbolizer, priority, timeToCache, readExpired, false);
	  requestBuffer(new URL(url), symbolizer, priority, new TimeInterval(timeToCache), readExpired, false); // isBson
	}
  }

  public final void loadBSON(URL url)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadBSON(url, null, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true);
	loadBSON(new URL(url), null, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void loadBSON(URL url, GEOSymbolizer symbolizer)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadBSON(url, symbolizer, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true);
	loadBSON(new URL(url), symbolizer, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true);
  }

  public final void loadBSON(URL url, GEOSymbolizer symbolizer, long priority, TimeInterval timeToCache, boolean readExpired)
  {
	if (_context == null)
	{
	  _loadQueue.add(new LoadQueueItem(url, symbolizer, priority, timeToCache, readExpired, true)); // isBson
	}
	else
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: requestBuffer(url, symbolizer, priority, timeToCache, readExpired, true);
	  requestBuffer(new URL(url), symbolizer, priority, new TimeInterval(timeToCache), readExpired, true); // isBson
	}
  }

  public final void setEnable(boolean enable)
  {
	super.setEnable(enable);
  
	if (_meshRenderer != null)
	{
	  _meshRenderer.setEnable(enable);
	}
	if (_shapesRenderer != null)
	{
	  _shapesRenderer.setEnable(enable);
	}
	if (_marksRenderer != null)
	{
	  _marksRenderer.setEnable(enable);
	}
	if (_geoVectorLayer != null)
	{
	  _geoVectorLayer.setEnable(enable);
	}
  }

}
