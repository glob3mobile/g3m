

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BSONParser;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.FloatBufferBuilderFromColor;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.JSONParser_Android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;


public class G3MNetCDFActivity
         extends
            Activity {

   private G3MWidget_Android                          _widgetAndroid;
   private boolean                                    _isDone         = false;
   private final ShapesRenderer                       _shapesRenderer = new ShapesRenderer();
   private final ArrayList<ArrayList<WindModelCsiro>> _wmsss          = new ArrayList<ArrayList<WindModelCsiro>>();
   private final ArrayList<Mesh>                      meshes          = new ArrayList<Mesh>();
   private final ArrayList<BoxShape>                  _boxShapes      = new ArrayList<BoxShape>();
   final MeshRenderer                                 _meshRenderer   = new MeshRenderer();

   private int                                        _period         = 0;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.bar_glob3_template);


      final G3MBuilder_Android builder = new G3MBuilder_Android(getApplicationContext());


      //Single period with points
      builder.setInitializationTask(getInitializationTaskSinglePeriodMeshes(builder.getPlanet()));
      builder.addRenderer(_meshRenderer);

      _widgetAndroid = builder.createWidget();


      final LinearLayout layout = (LinearLayout) findViewById(R.id.glob3);
      layout.addView(_widgetAndroid);

   }


   @SuppressWarnings("unused")
   private PeriodicalTask PaintMeshesTask() {
      final PeriodicalTask periodicalTask = new PeriodicalTask(TimeInterval.fromSeconds(0.2), new GTask() {

         @Override
         public void run(final G3MContext context) {

            if (_isDone) {

               if (_period == 18) {
                  _period = 0;
               }

               final Mesh mesh = meshes.get(_period);

               _meshRenderer.clearMeshes();
               _meshRenderer.addMesh(mesh);
            }
         }
      });
      return periodicalTask;

   }


   @SuppressWarnings("unused")
   private PeriodicalTask PaintGeometriesTask() {
      final PeriodicalTask periodicalTask = new PeriodicalTask(TimeInterval.fromSeconds(0.2), new GTask() {

         @Override
         public void run(final G3MContext context) {

            if (_isDone) {

               if (_period == 18) {
                  _period = 0;
               }


               final ArrayList<WindModelCsiro> periodWMS = _wmsss.get(_period);

               if (_period == 0) {

                  Log.e(G3MNetCDFActivity.this.toString(), "period_" + _period + "<--");
                  for (final WindModelCsiro wms : periodWMS) {

                     //  Log.e(G3MNetCDFActivity.this.toString(), "wms" + wms.toString());

                     final Geodetic2D position = new Geodetic2D(Angle.fromDegrees(wms.getLatitude()),
                              Angle.fromDegrees(wms.getLongitude()));

                     final ArrayList<Float> meridWinds = wms.getMeridWind();
                     //        final ArrayList<Float> zonalWinds = wms.getZonalWind();
                     final ArrayList<Float> levels = wms.getLevels();


                     for (int i = 0; i < meridWinds.size(); i++) {

                        final Vector3D extent = new Vector3D(150000, 150000, 1);
                        final float borderWidth = 2f;


                        final Color fromColor = Color.fromRGBA(0, 0, 1, 1);
                        final Color toColor = Color.fromRGBA(1, 0, 0, 1);

                        //         Log.e(G3MNetCDFActivity.this.toString(), "" + normalize(meridWinds.get(i), -10, 10, 1, 0));

                        final Color interpolatedColor = fromColor.mixedWith(toColor, normalize(meridWinds.get(i), -10, 10, 1, 0));
                        final Geodetic3D position3D = new Geodetic3D(position, levels.get(i) * 10000);

                        _boxShapes.add(new BoxShape(position3D, AltitudeMode.RELATIVE_TO_GROUND, extent, borderWidth,
                                 interpolatedColor, interpolatedColor));

                     }
                  }
                  for (final BoxShape bs : _boxShapes) {
                     _shapesRenderer.addShape(bs);
                  }
               }
               else {

                  Log.e(G3MNetCDFActivity.this.toString(), "period_" + _period);

                  for (int a = 0; a < periodWMS.size(); a++) {

                     final ArrayList<Float> meridWinds = periodWMS.get(a).getMeridWind();


                     for (int i = 0; i < meridWinds.size(); i++) {

                        final Color fromColor = Color.fromRGBA(0, 0, 1, 1);
                        final Color toColor = Color.fromRGBA(1, 0, 0, 1);

                        final Color interpolatedColor = fromColor.mixedWith(toColor, normalize(meridWinds.get(i), -10, 10, 1, 0));

                        _boxShapes.get(a).setSurfaceColor(interpolatedColor);

                     }
                  }

               }


               _period++;
            }
         }
      });
      return periodicalTask;
   }


   private float normalize(final float value,
                           final float max,
                           final float min,
                           final float new_max,
                           final float new_min) {
      return (((value - min) / (max - min)) * (new_max - new_min)) + new_min;
   }


   private GInitializationTask getInitializationTaskSinglePeriodMeshes(final Planet planet) {
      final GInitializationTask initializationTask = new GInitializationTask() {

         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new IBufferDownloadListener() {

               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer,
                                      final boolean expired) {

                  //                  final String response = buffer.getAsString();
                  //                  final IJSONParser parser = new JSONParser_Android();
                  //                  final JSONBaseObject jsonObject = parser.parse(response);
                  //                  final JSONObject object = jsonObject.asObject();


                  final JSONBaseObject objectBase = BSONParser.parse(buffer);
                  final JSONObject object = objectBase.asObject();

                  Log.e(G3MNetCDFActivity.this.toString(), object.description());


                  final ArrayList<String> periodKeys = object.keys();

                  for (final String period : periodKeys) {

                     final JSONObject yearObject = object.getAsObject(period);

                     final JSONArray features = yearObject.getAsArray("features");

                     final FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);

                     final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();

                     for (int i = 0; i < features.size(); i++) {

                        final JSONObject feature = features.getAsObject(i);

                        vertices.add(new Geodetic3D(Angle.fromDegrees(feature.getAsNumber("lat").value()),
                                 Angle.fromDegrees(feature.getAsNumber("lon").value()), 100000));

                        final JSONArray values = feature.getAsArray("values");


                        final Color fromColor = Color.fromRGBA(0, 0, 1, 1);
                        final Color toColor = Color.fromRGBA(1, 0, 0, 1);
                        for (int a = 0; a < values.size(); a++) {
                           final JSONObject value = values.getAsObject(a);

                           final Color interpolatedColor = fromColor.mixedWith(toColor,
                                    normalize(Double.valueOf(value.getAsNumber("mw").value()).floatValue(), -10, 10, 1, 0));

                           colors.add(interpolatedColor);
                        }


                        _isDone = true;
                     }

                     final float lineWidth = 1;
                     final Color flatColor = null;

                     _meshRenderer.addMesh(new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(),
                              lineWidth, 12F, flatColor, colors.create()));
                     Log.e(G3MNetCDFActivity.this.toString(), "Num of registry in netCDF:" + features.size());


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
            downloader.requestBuffer( //
                     new URL("file:///ACCESS-A.2011020104.nc3.slice10.bson", false), //
                     0, //
                     TimeInterval.forever(), //
                     true, //
                     listener, //
                     false);
         }


         @Override
         public boolean isDone(final G3MContext context) {
            if (_isDone) {
               _widgetAndroid.setAnimatedCameraPosition(
                        new Geodetic3D(Angle.fromDegrees(-24d), Angle.fromDegrees(153.d), 8000000), TimeInterval.fromSeconds(3));
               return true;
            }
            return false;
         }

      };
      return initializationTask;
   }


   @SuppressWarnings("unused")
   private GInitializationTask getInitializationTask() {

      final GInitializationTask initializationTask = new GInitializationTask() {

         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new IBufferDownloadListener() {

               @Override
               public void onError(final URL url) {
                  Log.e(G3MNetCDFActivity.this.toString(), "ERROR downloading json");

               }


               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer,
                                      final boolean expired) {


                  final String response = buffer.getAsString();
                  final IJSONParser parser = new JSONParser_Android();

                  final JSONBaseObject jsonObject = parser.parse(response);


                  final JSONObject object = jsonObject.asObject();

                  final ArrayList<String> periodKeys = object.keys();

                  for (final String period : periodKeys) {

                     final JSONObject yearObject = object.getAsObject(period);

                     final JSONArray features = yearObject.getAsArray("features");

                     for (int i = 0; i < features.size(); i++) {

                        final JSONObject feature = features.getAsObject(i);


                        final Geodetic2D position = new Geodetic2D(Angle.fromDegrees(feature.getAsNumber("latitude").value()),
                                 Angle.fromDegrees(feature.getAsNumber("longitude").value()));

                        final JSONArray values = feature.getAsArray("values");


                        for (int a = 0; a < values.size(); a++) {


                           final JSONObject value = values.getAsObject(a);

                           final Vector3D extent = new Vector3D(150000, 150000, 1);
                           final float borderWidth = 2f;


                           final Color fromColor = Color.fromRGBA(0, 0, 1, 1);
                           final Color toColor = Color.fromRGBA(1, 0, 0, 1);

                           final Color interpolatedColor = fromColor.mixedWith(toColor,
                                    normalize(Double.valueOf(value.getAsNumber("merid_wnd").value()).floatValue(), -10, 10, 1, 0));

                           final Geodetic3D position3D = new Geodetic3D(position, (value.getAsNumber("level").value() * 10000));

                           _shapesRenderer.addShape(new BoxShape(position3D, AltitudeMode.RELATIVE_TO_GROUND, extent,
                                    borderWidth, interpolatedColor, interpolatedColor));
                        }


                        Log.d(G3MNetCDFActivity.this.toString(), "longitude:" + feature.getAsNumber("longitude").value());
                        Log.d(G3MNetCDFActivity.this.toString(), "latitude:" + feature.getAsNumber("latitude").value());

                        _isDone = true;
                     }


                     Log.e(G3MNetCDFActivity.this.toString(), "Num of registry in netCDF:" + features.size());


                  }


               }


               @Override
               public void onCanceledDownload(final URL url,
                                              final IByteBuffer data,
                                              final boolean expired) {
               }


               @Override
               public void onCancel(final URL url) {
               }
            };

            //            downloader.requestBuffer(new URL("http://csiro.glob3mobile.com/yassiDebug.json", false), 0, TimeInterval.forever(),
            //                     listener, false);
            downloader.requestBuffer( //
                     new URL("http://csiro.glob3mobile.com/ACCESS-A.2011020104.nc13slice10.json", false), //
                     0, //
                     TimeInterval.forever(), //
                     true, //
                     listener, //
                     false);
         }


         //You need say if the inizialition task has finished, if return true, the globe start without finish this task
         // in other case you MUST to manage this boolean.
         @Override
         public boolean isDone(final G3MContext context) {
            if (_isDone) {
               return true;
            }
            return false;
         }
         //            return true;
         //         }
      };
      return initializationTask;
   }


   @SuppressWarnings("unused")
   private GInitializationTask getInitializationTaskCreateMeshes(final Planet planet) {

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

                  final ArrayList<String> periodKeys = object.keys();


                  for (final String period : periodKeys) {


                     final JSONObject yearObject = object.getAsObject(period);

                     final JSONArray features = yearObject.getAsArray("features");

                     final FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);

                     final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();


                     for (int i = 0; i < features.size(); i++) {

                        //   final WindModelCsiro wms = new WindModelCsiro();
                        final JSONObject feature = features.getAsObject(i);

                        vertices.add(new Geodetic3D(Angle.fromDegrees(feature.getAsNumber("latitude").value()),
                                 Angle.fromDegrees(feature.getAsNumber("longitude").value()), 100000));


                        final JSONArray values = feature.getAsArray("values");

                        //                        final ArrayList<Float> meridWinds = new ArrayList<Float>();
                        //                        final ArrayList<Float> zonalWinds = new ArrayList<Float>();
                        //                        final ArrayList<Float> levels = new ArrayList<Float>();

                        final Color fromColor = Color.fromRGBA(0, 0, 1, 1);
                        final Color toColor = Color.fromRGBA(1, 0, 0, 1);

                        for (int a = 0; a < values.size(); a++) {
                           final JSONObject value = values.getAsObject(a);

                           final Color interpolatedColor = fromColor.mixedWith(toColor,
                                    normalize(Double.valueOf(value.getAsNumber("merid_wnd").value()).floatValue(), -10, 10, 1, 0));

                           colors.add(interpolatedColor);
                        }
                     }

                     final float lineWidth = 100;
                     final Color flatColor = null;
                     meshes.add(new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), lineWidth,
                              50.0F, flatColor, colors.create()));
                  }

                  _isDone = true;


                  _widgetAndroid.setAnimatedCameraPosition(
                           new Geodetic3D(Angle.fromDegreesMinutes(-27, 43), Angle.fromDegreesMinutes(153, 15), 17000000),
                           TimeInterval.fromSeconds(5));
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

            downloader.requestBuffer( //
                     new URL("http://csiro.glob3mobile.com/21.periods.json", false), //
                     0, //
                     TimeInterval.forever(), //
                     true, //
                     listener, //
                     false);
         }


         @Override
         public boolean isDone(final G3MContext context) {
            if (_isDone) {
               return true;
            }
            return false;
         }

      };


      return initializationTask;

   }


   @SuppressWarnings("unused")
   private GInitializationTask getInitializationTaskPeriods() {

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

                  final ArrayList<String> periodKeys = object.keys();


                  for (final String period : periodKeys) {


                     final JSONObject yearObject = object.getAsObject(period);

                     final JSONArray features = yearObject.getAsArray("features");
                     final ArrayList<WindModelCsiro> _wmss = new ArrayList<WindModelCsiro>();
                     for (int i = 0; i < features.size(); i++) {

                        final WindModelCsiro wms = new WindModelCsiro();
                        final JSONObject feature = features.getAsObject(i);
                        wms.setLatitude(Double.valueOf(feature.getAsNumber("latitude").value()).floatValue());
                        wms.setLongitude(Double.valueOf(feature.getAsNumber("longitude").value()).floatValue());
                        final JSONArray values = feature.getAsArray("values");

                        final ArrayList<Float> meridWinds = new ArrayList<Float>();
                        final ArrayList<Float> zonalWinds = new ArrayList<Float>();
                        final ArrayList<Float> levels = new ArrayList<Float>();

                        for (int a = 0; a < values.size(); a++) {
                           final JSONObject value = values.getAsObject(a);
                           meridWinds.add(Double.valueOf(value.getAsNumber("merid_wnd").value()).floatValue());
                           zonalWinds.add(Double.valueOf(value.getAsNumber("zonal_wnd").value()).floatValue());
                           levels.add(Double.valueOf(value.getAsNumber("level").value()).floatValue());
                        }
                        wms.setMeridWind(meridWinds);
                        wms.setZonalWind(zonalWinds);
                        wms.setLevels(levels);
                        _wmss.add(wms);
                     }
                     _wmsss.add(_wmss);
                  }

                  _isDone = true;
                  _widgetAndroid.setAnimatedCameraPosition(new Geodetic3D(Angle.fromDegreesMinutes(-27, 43), //
                           Angle.fromDegreesMinutes(153, 15), 17000000), TimeInterval.fromSeconds(5));
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

            downloader.requestBuffer( //
                     new URL("http://csiro.glob3mobile.com/21.periods.json", false), //
                     0, //
                     TimeInterval.forever(), //
                     true, //
                     listener, //
                     false);
         }


         @Override
         public boolean isDone(final G3MContext context) {
            if (_isDone) {
               return true;
            }
            return false;
         }

      };


      return initializationTask;

   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

}
