package org.glob3.mobile.generated;
//class MarksRenderer;
public class PanoDownloadListener extends IBufferDownloadListener
{

    private static final String NAME = "name";
    private static final String POSITION = "position";
    private static final String LAT = "lat";
    private static final String LON = "lon";

    private MarksRenderer _panoMarksRenderer;
    private String _urlIcon;

    public PanoDownloadListener(MarksRenderer panoMarksRenderer, String urlIcon)
    {
      _panoMarksRenderer = panoMarksRenderer;
      _urlIcon = urlIcon;
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

    public void dispose()
    {
    }
    private void parseMETADATA(String url, JSONObject json)
    {
    
      final Angle latitude = Angle.fromDegrees(json.getAsObject(POSITION).getAsNumber(LAT).value());
      final Angle longitude = Angle.fromDegrees(json.getAsObject(POSITION).getAsNumber(LON).value());
    
      Mark mark = new Mark(new URL(_urlIcon,false), new Geodetic3D(latitude, longitude, 0), AltitudeMode.RELATIVE_TO_GROUND, 0, new PanoMarkUserData(json.getAsString(NAME).value(), new URL(url, false)),true);
    
      _panoMarksRenderer.addMark(mark);
    }
}
