package org.glob3.mobile.generated; 
public class ShapesRenderer_SceneJSParserAsyncTask extends GAsyncTask
{
  private ShapesRenderer _shapesRenderer;
  private final URL _url;
  private IByteBuffer _buffer;
  private final String _uriPrefix;
  private final boolean _isTransparent;
  private Geodetic3D _position;
  private AltitudeMode _altitudeMode;
  private ShapeLoadListener _listener;
  private final boolean _deleteListener;
  private final boolean _isBSON;

  private SGShape _sgShape;

  public ShapesRenderer_SceneJSParserAsyncTask(ShapesRenderer shapesRenderer, URL url, IByteBuffer buffer, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener, boolean isBSON)
  {
     _shapesRenderer = shapesRenderer;
     _url = url;
     _buffer = buffer;
     _uriPrefix = uriPrefix;
     _isTransparent = isTransparent;
     _position = position;
     _altitudeMode = altitudeMode;
     _listener = listener;
     _deleteListener = deleteListener;
     _isBSON = isBSON;
     _sgShape = null;
  }

  public final void runInBackground(G3MContext context)
  {
    if (_isBSON)
    {
      _sgShape = SceneJSShapesParser.parseFromBSON(_buffer, _uriPrefix, _isTransparent, _position, _altitudeMode);
    }
    else
    {
      _sgShape = SceneJSShapesParser.parseFromJSON(_buffer, _uriPrefix, _isTransparent, _position, _altitudeMode);
    }

    if (_buffer != null)
       _buffer.dispose();
    _buffer = null;
  }

  public void dispose()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    if (_buffer != null)
       _buffer.dispose();
    super.dispose();
  }

  public final void onPostExecute(G3MContext context)
  {
    if (_sgShape == null)
    {
      ILogger.instance().logError("Error parsing SceneJS from \"%s\"", _url._path);
      if (_position != null)
         _position.dispose();
      _position = null;
    }
    else
    {
      if (_listener != null)
      {
        _listener.onBeforeAddShape(_sgShape);
      }

      ILogger.instance().logInfo("Adding SGShape to _shapesRenderer");
      _shapesRenderer.addShape(_sgShape);

      if (_listener != null)
      {
        _listener.onAfterAddShape(_sgShape);
      }

      _sgShape = null;
    }
  }

}