package org.glob3.mobile.generated; 
public class MeshRenderer_PointCloudBufferDownloadListener extends IBufferDownloadListener
{
  private MeshRenderer _meshRenderer;
  private final float _pointSize;
  private final double _deltaHeight;
  private MeshLoadListener _listener;
  private boolean _deleteListener;
  private final IThreadUtils _threadUtils;
  private boolean _isBSON;

  private G3MContext _context;


  public MeshRenderer_PointCloudBufferDownloadListener(MeshRenderer meshRenderer, float pointSize, double deltaHeight, MeshLoadListener listener, boolean deleteListener, IThreadUtils threadUtils, boolean isBSON, G3MContext context)
  {
     _meshRenderer = meshRenderer;
     _pointSize = pointSize;
     _deltaHeight = deltaHeight;
     _listener = listener;
     _deleteListener = deleteListener;
     _threadUtils = threadUtils;
     _isBSON = isBSON;
     _context = context;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    ILogger.instance().logInfo("Downloaded PointCloud buffer from \"%s\" (%db)", url.getPath(), buffer.size());

    _threadUtils.invokeAsyncTask(new MeshRenderer_PointCloudParserAsyncTask(_meshRenderer, url, buffer, _pointSize, _deltaHeight, _listener, _deleteListener, _isBSON, _context), true);

  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Error downloading \"%s\"", url.getPath());

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
  }

  public final void onCancel(URL url)
  {
    ILogger.instance().logInfo("Canceled download of \"%s\"", url.getPath());

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

}