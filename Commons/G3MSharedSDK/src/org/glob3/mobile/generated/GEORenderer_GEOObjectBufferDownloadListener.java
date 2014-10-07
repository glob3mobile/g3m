package org.glob3.mobile.generated; 
public class GEORenderer_GEOObjectBufferDownloadListener extends IBufferDownloadListener
{
  private GEORenderer _geoRenderer;
  private GEOSymbolizer _symbolizer;
  private final IThreadUtils _threadUtils;
  private final boolean _isBSON;

  public GEORenderer_GEOObjectBufferDownloadListener(GEORenderer geoRenderer, GEOSymbolizer symbolizer, IThreadUtils threadUtils, boolean isBSON)
  {
     _geoRenderer = geoRenderer;
     _symbolizer = symbolizer;
     _threadUtils = threadUtils;
     _isBSON = isBSON;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    ILogger.instance().logInfo("Downloaded GEOObject buffer from \"%s\" (%db)", url._path, buffer.size());

    _threadUtils.invokeAsyncTask(new GEORenderer_GEOObjectParserAsyncTask(url, buffer, _geoRenderer, _symbolizer, _isBSON), true);
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Error downloading \"%s\"", url._path);
  }

  public final void onCancel(URL url)
  {
    ILogger.instance().logInfo("Canceled download of \"%s\"", url._path);
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }
}