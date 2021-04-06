package org.glob3.mobile.generated;
public class XPCPointCloud_OperationBufferDownloadListener extends IBufferDownloadListener
{
  private XPCPointCloud _pointCloud;
  private XPCPointCloudUpdateListener _listener;
  private final boolean _deleteListener;

  public XPCPointCloud_OperationBufferDownloadListener(XPCPointCloud pointCloud, XPCPointCloudUpdateListener listener, boolean deleteListener)
  {
     _pointCloud = pointCloud;
     _listener = listener;
     _deleteListener = deleteListener;
    _pointCloud._retain();
  }

  public void dispose()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }

    _pointCloud._release();
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    ByteBufferIterator it = new ByteBufferIterator(buffer);

    byte version = it.nextUInt8();
    if (version != 1)
    {
      ILogger.instance().logError("Unsupported format version");
      return;
    }

    final boolean success = (it.nextUInt8() != 0);
    if (success)
    {
      final long updatedPoints = it.nextInt64();
      if (_listener != null)
      {
        _listener.onPointCloudUpdateSuccess(updatedPoints);
      }
      _pointCloud.reload();
    }
    else
    {
      final String errorMessage = it.nextZeroTerminatedString();
      if (_listener != null)
      {
        _listener.onPointCloudUpdateFail(errorMessage);
      }
      _pointCloud.reload();
    }

    if (it.hasNext())
    {
      throw new RuntimeException("Logic error");
    }

    if (buffer != null)
       buffer.dispose();
  }

  public final void onError(URL url)
  {
    if (_listener != null)
    {
      _listener.onPointCloudUpdateFail("Communication error: " + url._path);
    }
    _pointCloud.reload();
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

}