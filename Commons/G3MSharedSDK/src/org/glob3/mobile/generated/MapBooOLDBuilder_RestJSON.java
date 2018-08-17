package org.glob3.mobile.generated;import java.util.*;

public class MapBooOLDBuilder_RestJSON implements IBufferDownloadListener
{
  private MapBooOLDBuilder _builder;

  public MapBooOLDBuilder_RestJSON(MapBooOLDBuilder builder)
  {
	  _builder = builder;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _builder->parseApplicationEventsJSON(buffer->getAsString(), url);
	_builder.parseApplicationEventsJSON(buffer.getAsString(), new URL(url));
	if (buffer != null)
		buffer.dispose();
  }

  public final void onError(URL url)
  {
	ILogger.instance().logError("Can't download %s", url._path.c_str());
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
