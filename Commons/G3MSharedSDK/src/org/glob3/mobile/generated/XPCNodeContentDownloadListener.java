package org.glob3.mobile.generated;
public class XPCNodeContentDownloadListener extends IBufferDownloadListener
{
  private final XPCPointCloud _pointCloud;
  private XPCNode _node;

  private final IThreadUtils _threadUtils;
  private final Planet _planet;


  public XPCNodeContentDownloadListener(XPCPointCloud pointCloud, XPCNode node, IThreadUtils threadUtils, Planet planet)
  {
     _pointCloud = pointCloud;
     _node = node;
     _threadUtils = threadUtils;
     _planet = planet;
    _pointCloud._retain();
    _node._retain();
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    if (_node.isCanceled())
    {
      if (buffer != null)
         buffer.dispose();
    }
    else
    {
      if (_pointCloud.isVerbose())
      {
        ILogger.instance().logInfo("Downloaded metadata for \"%s\" node \"%s\" (bytes=%d)",
                                   _pointCloud.getCloudName(),
                                   _node.getID(),
                                   buffer.size());
      }

      _threadUtils.invokeAsyncTask(new XPCNodeContentParserAsyncTask(_pointCloud, _node, buffer, _planet), true);
    }
  }

  public final void onError(URL url)
  {
    _node.errorDownloadingContent();
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

  public void dispose()
  {
    _node._release();
    _pointCloud._release();
  }


}