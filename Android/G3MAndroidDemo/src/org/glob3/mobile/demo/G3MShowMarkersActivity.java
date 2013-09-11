

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.SceneJSShapesParser;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.JSONParser_Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;


public class G3MShowMarkersActivity
         extends
            Activity {

   ShapesRenderer            _shapeRenderer  = new ShapesRenderer();
   MarksRenderer             _weatherMarkers = new MarksRenderer(false);
   private G3MWidget_Android _widgetAndroid;
   private boolean           _WeatherMarkerIsDone;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.bar_glob3_template);
      final G3MBuilder_Android builder = new G3MBuilder_Android(getApplicationContext());

      builder.setInitializationTask(getWeatherMarkerLayersTask());

      if (true) {
         final Sector spain = Sector.fromDegrees(27.3174927, -18.5284423, 45.0299024, 5.4084426);
         builder.setShownSector(spain);
      }

      builder.addRenderer(_weatherMarkers);
      builder.addRenderer(_shapeRenderer);
      builder.setLogFPS(true);
      final Planet planet = Planet.createFlatEarth();
      builder.setPlanet(planet);

      //Always after setting params
      _widgetAndroid = builder.createWidget();
      final LinearLayout g3mLayout = (LinearLayout) findViewById(R.id.glob3);
      g3mLayout.addView(_widgetAndroid);

   }


   private GInitializationTask getWeatherMarkerLayersTask() {
      final GInitializationTask initializationTask = new GInitializationTask() {

         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new IBufferDownloadListener() {


               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer,
                                      final boolean expired) {

                  final String response = buffer.getAsString();
                  final IJSONParser parser = new JSONParser_Android();
                  final JSONBaseObject jsonObject = parser.parse(response);
                  final JSONObject object = jsonObject.asObject();
                  final JSONArray list = object.getAsArray("list");
                  for (int i = 0; i < list.size(); i++) {

                     final JSONObject city = list.getAsObject(i);

                     final JSONObject coords = city.getAsObject("coord");
                     final Geodetic2D position = new Geodetic2D(Angle.fromDegrees(coords.getAsNumber("lat").value()),
                              Angle.fromDegrees(coords.getAsNumber("lon").value()));
                     final JSONArray weather = city.getAsArray("weather");
                     final JSONObject weatherObject = weather.getAsObject(0);


                     String icon = "";
                     if (weatherObject.getAsString("icon", "DOUBLE").equals("DOUBLE")) {
                        icon = "" + (int) weatherObject.getAsNumber("icon").value() + "d.png";
                        if (icon.length() < 7) {
                           icon = "0" + icon;
                        }
                     }
                     else {
                        icon = weatherObject.getAsString("icon", "DOUBLE") + ".png";
                     }


                     _weatherMarkers.addMark(new Mark( //
                              city.getAsString("name", ""), //
                              new URL("http://openweathermap.org/img/w/" + icon, false), //
                              new Geodetic3D(position, 0), //
                              AltitudeMode.RELATIVE_TO_GROUND, 0, //
                              true, //
                              14));
                  }


                  _WeatherMarkerIsDone = true;
               }


               @Override
               public void onError(final URL url) {
                  Toast.makeText(getApplicationContext(), "Error retrieving  weather data", Toast.LENGTH_SHORT).show();

               }


               @Override
               public void onCancel(final URL url) {
                  //DO Nothing
               }


               @Override
               public void onCanceledDownload(final URL url,
                                              final IByteBuffer data,
                                              final boolean expired) {
                  //Do Nothing
               }
            };

            downloader.requestBuffer( //
                     new URL("http://openweathermap.org/data/2.1/find/city?bbox=-80,-180,80,180,4&cluster=yes", false), //
                     0, //
                     TimeInterval.fromHours(1.0), //
                     false, //
                     listener, //
                     false);

            //////

            //Seymour-plane on Gran Canaria
            if (true) {
               final URL planeFilePath = new URL("file:///seymour-plane.json", false);
               final IBufferDownloadListener listenerPlane = new IBufferDownloadListener() {

                  @Override
                  public void onDownload(final URL url,
                                         final IByteBuffer buffer,
                                         final boolean expired) {
                     Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();

                     final Shape plane = SceneJSShapesParser.parseFromJSON(buffer, URL.FILE_PROTOCOL, false);
                     if (plane != null) {
                        plane.setPosition(new Geodetic3D(Angle.fromDegrees(28.127222), Angle.fromDegrees(-15.431389), 10000));

                        final double scale = 1000;
                        plane.setScale(scale, scale, scale);
                        plane.setPitch(Angle.fromDegrees(90));
                        plane.setHeading(Angle.fromDegrees(0));
                        plane.setAnimatedPosition(TimeInterval.fromSeconds(60), new Geodetic3D(Angle.fromDegrees(28.127222),
                                 Angle.fromDegrees(-15.431389), 10000), Angle.fromDegrees(90), Angle.fromDegrees(720));

                        _shapeRenderer.addShape(plane);
                        ILogger.instance().logInfo("PLANE SHOWN");
                     }
                  }


                  @Override
                  public void onError(final URL url) {
                  }


                  @Override
                  public void onCancel(final URL url) {
                  }


                  @Override
                  public void onCanceledDownload(final URL url,
                                                 final IByteBuffer data,
                                                 final boolean expired) {
                  }
               };

               downloader.requestBuffer(planeFilePath, 1000, TimeInterval.fromHours(1.0), true, listenerPlane, true);
            }
         }


         @Override
         public boolean isDone(final G3MContext context) {
            if (_WeatherMarkerIsDone) {
               _widgetAndroid.setAnimatedCameraPosition(new Geodetic3D(Angle.fromDegrees(45d), Angle.fromDegrees(0.d), 3000000),
                        TimeInterval.fromSeconds(3));
               return true;
            }
            return false;
         }
      };
      return initializationTask;
   }
}
