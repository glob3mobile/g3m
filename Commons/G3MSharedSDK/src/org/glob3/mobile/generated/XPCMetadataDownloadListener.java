package org.glob3.mobile.generated;
public class XPCMetadataDownloadListener extends IBufferDownloadListener
{
  private XPCPointCloud _pointCloud;
  private final IThreadUtils _threadUtils;


  public XPCMetadataDownloadListener(XPCPointCloud pointCloud, IThreadUtils threadUtils)
  {
     _pointCloud = pointCloud;
     _threadUtils = threadUtils;
    _pointCloud._retain();
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    ILogger.instance().logInfo("Downloaded metadata for \"%s\" (bytes=%d)", _pointCloud.getCloudName(), buffer.size());

    _threadUtils.invokeAsyncTask(new XPCMetadataParserAsyncTask(_pointCloud, buffer), true);
  }

  public final void onError(URL url)
  {
    _pointCloud.errorDownloadingMetadata();
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
    _pointCloud._release();
  }

}