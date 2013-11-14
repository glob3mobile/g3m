package org.glob3.mobile.generated; 
public class MapBooBuilder_RestJSON extends IBufferDownloadListener
{
  private MapBooBuilder _builder;

  public MapBooBuilder_RestJSON(MapBooBuilder builder)
  {
     _builder = builder;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    _builder.parseApplicationJSON(buffer.getAsString(), url);
    if (buffer != null)
       buffer.dispose();
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Can't download %s", url.getPath());
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