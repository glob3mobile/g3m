package org.glob3.mobile.generated;import java.util.*;

public class WMSBilElevationDataProvider_BufferDownloadListener implements IBufferDownloadListener
{
  private final Sector _sector = new Sector();
  private final int _width;
  private final int _height;

  private IElevationDataListener _listener;
  private final boolean _autodeleteListener;

  private final double _deltaHeight;


  public WMSBilElevationDataProvider_BufferDownloadListener(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener, double deltaHeight)
  {
	  _sector = new Sector(sector);
	  _width = extent._x;
	  _height = extent._y;
	  _listener = listener;
	  _autodeleteListener = autodeleteListener;
	  _deltaHeight = deltaHeight;

  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
	final Vector2I resolution = new Vector2I(_width, _height);

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: ShortBufferElevationData* elevationData = BilParser::parseBil16(_sector, resolution, buffer, _deltaHeight);
	ShortBufferElevationData elevationData = BilParser.parseBil16(new Sector(_sector), new Vector2I(resolution), buffer, _deltaHeight);
	if (buffer != null)
		buffer.dispose();

	if (elevationData == null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _listener->onError(_sector, resolution);
	  _listener.onError(new Sector(_sector), new Vector2I(resolution));
	}
	else
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _listener->onData(_sector, resolution, elevationData);
	  _listener.onData(new Sector(_sector), new Vector2I(resolution), elevationData);
	}

	if (_autodeleteListener)
	{
	  if (_listener != null)
		  _listener.dispose();
	  _listener = null;
	}
  }

  public final void onError(URL url)
  {
	final Vector2I resolution = new Vector2I(_width, _height);

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _listener->onError(_sector, resolution);
	_listener.onError(new Sector(_sector), new Vector2I(resolution));
	if (_autodeleteListener)
	{
	  if (_listener != null)
		  _listener.dispose();
	  _listener = null;
	}
  }

  public final void onCancel(URL url)
  {

  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {

  }


}
