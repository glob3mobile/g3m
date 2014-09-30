package org.glob3.mobile.generated; 
public class MapBooBuilder_DummyListener extends IBufferDownloadListener
{
  public MapBooBuilder_DummyListener()
  {
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
    if (buffer != null)
       buffer.dispose();
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Can't download %s", url._path);
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