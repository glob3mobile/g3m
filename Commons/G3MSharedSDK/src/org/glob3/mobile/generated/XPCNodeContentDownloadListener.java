package org.glob3.mobile.generated;
public class XPCNodeContentDownloadListener extends IBufferDownloadListener
{
  private final XPCPointCloud _pointCloud;
  private XPCNode _node;
  private final IThreadUtils _threadUtils;
  private final Planet _planet;
  private final BoundingVolume _fence;


  public XPCNodeContentDownloadListener(XPCPointCloud pointCloud, XPCNode node, IThreadUtils threadUtils, Planet planet, BoundingVolume fence)
  {
     _pointCloud = pointCloud;
     _node = node;
     _threadUtils = threadUtils;
     _planet = planet;
     _fence = fence;
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
//      if (_pointCloud->isVerbose()) {
///#ifdef C_CODE
//        ILogger::instance()->logInfo("Downloaded content for \"%s\" node \"%s\" (bytes=%ld)",
//                                     _pointCloud->getCloudName().c_str(),
//                                     _node->getID().c_str(),
//                                     buffer->size());
///#endif
///#ifdef JAVA_CODE
//        ILogger.instance().logInfo("Downloaded content for \"%s\" node \"%s\" (bytes=%d)",
//                                   _pointCloud.getCloudName(),
//                                   _node.getID(),
//                                   buffer.size());
///#endif
//      }

      _threadUtils.invokeAsyncTask(new XPCNodeContentParserAsyncTask(_pointCloud, _node, buffer, _planet, (_fence == null) ? null : _fence.copy()), true);
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

    if (_fence != null)
       _fence.dispose();
  }


}