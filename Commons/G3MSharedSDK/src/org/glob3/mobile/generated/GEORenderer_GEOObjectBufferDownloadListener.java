package org.glob3.mobile.generated; 
public class GEORenderer_GEOObjectBufferDownloadListener extends IBufferDownloadListener
{
  private GEORenderer _geoRenderer;
  private GEOSymbolizer _symbolizer;
  private final IThreadUtils _threadUtils;

  public GEORenderer_GEOObjectBufferDownloadListener(GEORenderer geoRenderer, GEOSymbolizer symbolizer, IThreadUtils threadUtils)
  {
     _geoRenderer = geoRenderer;
     _symbolizer = symbolizer;
     _threadUtils = threadUtils;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    _threadUtils.invokeAsyncTask(new GEOObjectParserAsyncTask(url, buffer, _geoRenderer, _symbolizer), true);

    //    _threadUtils->invokeInBackground(new GEOObjectParserTask(url,
    //                                                             buffer,
    //                                                             _geoRenderer,
    //                                                             _symbolizer),
    //                                     true);

    //    GEOObject* geoObject = GEOJSONParser::parse(buffer);
    //
    //    if (geoObject == NULL) {
    //      ILogger::instance()->logError("Error parsing GEOJSON from \"%s\"", url.getPath().c_str());
    //    }
    //    else {
    //      _geoRenderer->addGEOObject(geoObject, _symbolizer);
    //    }
    //
    //    delete buffer;
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Error downloading \"%s\"", url.getPath());
  }

  public final void onCancel(URL url)
  {
    ILogger.instance().logInfo("Canceled download of \"%s\"", url.getPath());
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }
}