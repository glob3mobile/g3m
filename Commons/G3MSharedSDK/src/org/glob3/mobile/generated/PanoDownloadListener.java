

package org.glob3.mobile.generated;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarksRenderer;
public class PanoDownloadListener
         implements
            IBufferDownloadListener {

   private static final String     NAME     = "name";
   private static final String     POSITION = "position";
   private static final String     LAT      = "lat";
   private static final String     LON      = "lon";

   private final MarksRenderer     _marksRenderer;
   private final MarkTouchListener _panoTouchListener;


   public PanoDownloadListener(final MarksRenderer marksRenderer,
                               final MarkTouchListener panoTouchListener) {
      _marksRenderer = marksRenderer;
      _panoTouchListener = panoTouchListener;
   }


   @Override
   public final void onDownload(final URL url,
                                final IByteBuffer buffer) {
      final String String = buffer.getAsString();
      final JSONBaseObject json = IJSONParser.instance().parse(String);
      ILogger.instance().logInfo(url.getPath());
      parseMETADATA(
               IStringUtils.instance().substring(url.getPath(), 0, IStringUtils.instance().indexOf(url.getPath(), "/info.txt")),
               json.asObject());
      IJSONParser.instance().deleteJSONData(json);
   }


   @Override
   public final void onError(final URL url) {
      ILogger.instance().logError("The requested pano could not be found!");
   }


   @Override
   public final void onCancel(final URL url) {
   }


   @Override
   public final void onCanceledDownload(final URL url,
                                        final IByteBuffer data) {
   }


   public void dispose() {
   }


   private void parseMETADATA(final String url,
                              final JSONObject json) {

      final Angle latitude = Angle.fromDegrees(json.getAsObject(POSITION).getAsNumber(LAT).doubleValue());
      final Angle longitude = Angle.fromDegrees(json.getAsObject(POSITION).getAsNumber(LON).doubleValue());

      final Mark mark = new Mark(new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), new Geodetic3D(latitude,
               longitude, 0), 0, new PanoMarkUserData(json.getAsString(NAME).value(), new URL(url, false)), true,
               _panoTouchListener, true);

      _marksRenderer.addMark(mark);
   }
}
