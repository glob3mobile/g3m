package org.glob3.mobile.generated; 
public class MeshRenderer_MeshBufferDownloadListener extends IBufferDownloadListener
{
  private MeshRenderer _meshRenderer;
  private final float _pointSize;
  private final double _deltaHeight;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Color _color;
//#endif
  private Color _color;
  private MeshLoadListener _listener;
  private boolean _deleteListener;
  private final IThreadUtils _threadUtils;
  private boolean _isBSON;
  private final MeshType _meshType;

  private G3MContext _context;


  public MeshRenderer_MeshBufferDownloadListener(MeshRenderer meshRenderer, float pointSize, double deltaHeight, Color color, MeshLoadListener listener, boolean deleteListener, IThreadUtils threadUtils, boolean isBSON, MeshType meshType, G3MContext context)
  {
     _meshRenderer = meshRenderer;
     _pointSize = pointSize;
     _deltaHeight = deltaHeight;
     _color = color;
     _listener = listener;
     _deleteListener = deleteListener;
     _threadUtils = threadUtils;
     _isBSON = isBSON;
     _meshType = meshType;
     _context = context;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    ILogger.instance().logInfo("Downloaded Mesh buffer from \"%s\" (%db)", url.getPath(), buffer.size());

    _threadUtils.invokeAsyncTask(new MeshRenderer_MeshParserAsyncTask(_meshRenderer, url, buffer, _pointSize, _deltaHeight, _color, _listener, _deleteListener, _isBSON, _meshType, _context), true);
    _color = null;
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Error downloading \"%s\"", url.getPath());

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    if (_color != null)
       _color.dispose();
  }

  public final void onCancel(URL url)
  {
    ILogger.instance().logInfo("Canceled download of \"%s\"", url.getPath());

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    if (_color != null)
       _color.dispose();
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

  public void dispose()
  {
    if (_color != null)
       _color.dispose();
  }

}