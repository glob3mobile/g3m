package org.glob3.mobile.generated;
public class PanoDownloadListener extends IBufferDownloadListener
{
  private MarksRenderer _panoMarksRenderer;
  private final String _urlIcon;

  private void parseMETADATA(String url, JSONObject json)
  {
  
    final Angle latitude = Angle.fromDegrees(json.getAsObject("position").getAsNumber("lat").value());
    final Angle longitude = Angle.fromDegrees(json.getAsObject("position").getAsNumber("lon").value());
  
    Mark mark = new Mark(new URL(_urlIcon), new Geodetic3D(latitude, longitude, 0), AltitudeMode.RELATIVE_TO_GROUND, 0, new PanoMarkUserData(json.getAsString("name").value(), new URL(url)), true);
  
    mark.setOnScreenSizeOnProportionToImage(2.0f, 2.0f);
  
    _panoMarksRenderer.addMark(mark);
  }

  public PanoDownloadListener(MarksRenderer panoMarksRenderer, String urlIcon)
  {
     _panoMarksRenderer = panoMarksRenderer;
     _urlIcon = urlIcon;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    String String = buffer.getAsString();
    final JSONBaseObject json = IJSONParser.instance().parse(String);
    ILogger.instance().logInfo(url._path);
    parseMETADATA(IStringUtils.instance().substring(url._path, 0, IStringUtils.instance().indexOf(url._path, "/info.txt")), json.asObject());
    IJSONParser.instance().deleteJSONData(json);
    if (buffer != null)
       buffer.dispose();
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("The requested pano could not be found! ->"+url.description());
  }

  public final void onCancel(URL url)
  {
  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#import "IJSONParser.hpp"
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#import "JSONNumber.hpp"
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#import "JSONString.hpp"
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#import "MarksRenderer.hpp"
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#import "JSONObject.hpp"
