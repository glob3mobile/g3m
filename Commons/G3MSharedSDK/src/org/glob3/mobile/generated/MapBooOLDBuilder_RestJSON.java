package org.glob3.mobile.generated; 
public class MapBooOLDBuilder_RestJSON extends IBufferDownloadListener
{
  private MapBooOLDBuilder _builder;

  public MapBooOLDBuilder_RestJSON(MapBooOLDBuilder builder)
  {
     _builder = builder;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    _builder.parseApplicationEventsJSON(buffer.getAsString(), url);
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