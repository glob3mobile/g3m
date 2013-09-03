package org.glob3.mobile.generated; 
//class MarksRenderer;
public class PanoDownloadListener extends IBufferDownloadListener
{

    private static final String NAME = "name";
    private static final String POSITION = "position";
    private static final String LAT = "lat";
    private static final String LON = "lon";

    private MarksRenderer _marksRenderer;
    private MarkTouchListener _panoTouchListener;
    private String _urlIcon;

    public PanoDownloadListener(MarksRenderer marksRenderer, MarkTouchListener panoTouchListener, String urlIcon)
    {
      _marksRenderer = marksRenderer;
      _panoTouchListener = panoTouchListener;
      _urlIcon = urlIcon;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      String String = buffer.getAsString();
      final JSONBaseObject json = IJSONParser.instance().parse(String);
      ILogger.instance().logInfo(url.getPath());
      parseMETADATA(IStringUtils.instance().substring(url.getPath(), 0, IStringUtils.instance().indexOf(url.getPath(), "/info.txt")), json.asObject());
      IJSONParser.instance().deleteJSONData(json);
      if (buffer != null)
         buffer.dispose();
    }

    public final void onError(URL url)
    {
        ILogger.instance().logError("The requested pano could not be found!");
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
    
      Mark mark = new Mark(new URL(_urlIcon,false), new Geodetic3D(latitude, longitude, 0), AltitudeMode.RELATIVE_TO_GROUND, 0, new PanoMarkUserData(json.getAsString(NAME).value(), new URL(url, false)),true, _panoTouchListener,true);
    
      _marksRenderer.addMark(mark);
    }
}