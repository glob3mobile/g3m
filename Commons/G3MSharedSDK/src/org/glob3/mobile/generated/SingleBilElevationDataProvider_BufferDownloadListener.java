package org.glob3.mobile.generated;import java.util.*;

public class SingleBilElevationDataProvider_BufferDownloadListener implements IBufferDownloadListener
{
  private SingleBilElevationDataProvider _singleBilElevationDataProvider;
  private final Sector _sector = new Sector();
  private final int _resolutionWidth;
  private final int _resolutionHeight;

  private final double _deltaHeight;

  public SingleBilElevationDataProvider_BufferDownloadListener(SingleBilElevationDataProvider singleBilElevationDataProvider, Sector sector, int resolutionWidth, int resolutionHeight, double deltaHeight)
  {
	  _singleBilElevationDataProvider = singleBilElevationDataProvider;
	  _sector = new Sector(sector);
	  _resolutionWidth = resolutionWidth;
	  _resolutionHeight = resolutionHeight;
	  _deltaHeight = deltaHeight;

  }

  public final void notifyProviderHasBeenDeleted()
  {
	_singleBilElevationDataProvider = null;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
	if (_singleBilElevationDataProvider != null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: ShortBufferElevationData* elevationData = BilParser::parseBil16(_sector, Vector2I(_resolutionWidth, _resolutionHeight), buffer, _deltaHeight);
	  ShortBufferElevationData elevationData = BilParser.parseBil16(new Sector(_sector), new Vector2I(_resolutionWidth, _resolutionHeight), buffer, _deltaHeight);

	  _singleBilElevationDataProvider.onElevationData(elevationData);
	}
	if (buffer != null)
		buffer.dispose();
  }

  public final void onError(URL url)
  {
	if (_singleBilElevationDataProvider != null)
	{
	  _singleBilElevationDataProvider.onElevationData(null);
	}
  }

  public final void onCancel(URL url)
  {
	ILogger.instance().logInfo("SingleBilElevationDataProvider download petition was canceled.");
	if (_singleBilElevationDataProvider != null)
	{
	  _singleBilElevationDataProvider.onElevationData(null);
	}
  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {
  }
}
