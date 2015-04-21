package org.glob3.mobile.generated; 
public class ShapesRenderer_SceneJSBufferDownloadListener extends IBufferDownloadListener
{
  private ShapesRenderer _shapesRenderer;
  private final String _uriPrefix;
  private boolean _isTransparent;
  private Geodetic3D _position;
  private AltitudeMode _altitudeMode;
  private ShapeLoadListener _listener;
  private boolean _deleteListener;
  private final IThreadUtils _threadUtils;
  private boolean _isBSON;


  public ShapesRenderer_SceneJSBufferDownloadListener(ShapesRenderer shapesRenderer, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener, IThreadUtils threadUtils, boolean isBSON)
  {
     _shapesRenderer = shapesRenderer;
     _uriPrefix = uriPrefix;
     _isTransparent = isTransparent;
     _position = position;
     _altitudeMode = altitudeMode;
     _listener = listener;
     _deleteListener = deleteListener;
     _threadUtils = threadUtils;
     _isBSON = isBSON;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    ILogger.instance().logInfo("Downloaded SceneJS buffer from \"%s\" (%db)", url._path, buffer.size());

    _threadUtils.invokeAsyncTask(new ShapesRenderer_SceneJSParserAsyncTask(_shapesRenderer, url, buffer, _uriPrefix, _isTransparent, _position, _altitudeMode, _listener, _deleteListener, _isBSON), true);

  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Error downloading \"%s\"", url._path);

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }

    if (_position != null)
       _position.dispose();
  }

  public final void onCancel(URL url)
  {
    ILogger.instance().logInfo("Canceled download of \"%s\"", url._path);

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }

    if (_position != null)
       _position.dispose();
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }
}