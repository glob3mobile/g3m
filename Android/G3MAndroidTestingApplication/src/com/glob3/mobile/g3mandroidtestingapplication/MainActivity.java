package com.glob3.mobile.g3mandroidtestingapplication;

import java.util.LinkedList;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MRenderContext;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;


public class MainActivity
extends
Activity {

	private G3MWidget_Android _g3mWidget;
	private RelativeLayout    _placeHolder;


	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		final G3MBuilder_Android builder = new G3MBuilder_Android(this);
		// builder.getPlanetRendererBuilder().setRenderDebug(true);

		// final ShapesRenderer shapesRenderer = new ShapesRenderer();
		// builder.addRenderer(shapesRenderer);

		final MarksRenderer marksRenderer = new MarksRenderer(true);
		builder.addRenderer(marksRenderer);

		final MeshRenderer meshRenderer = new MeshRenderer();
		meshRenderer.loadBSONMesh(new URL("file:///1951_r.bson"), Color.white());
		builder.addRenderer(meshRenderer);

		// final ShapeLoadListener Plistener = new ShapeLoadListener() {
		// @Override
		// public void onBeforeAddShape(final SGShape shape) {
		// // shape.setScale(2000);
		// //shape.setRoll(Angle.fromDegrees(-90));
		// }
		//
		//
		// @Override
		// public void onAfterAddShape(final SGShape shape) {
		//
		//
		// ILogger.instance().logInfo("Downloaded Building");
		//
		// final double fromDistance = 10000;
		// final double toDistance = 1000;
		//
		// final Angle fromAzimuth = Angle.fromDegrees(-90);
		// final Angle toAzimuth = Angle.fromDegrees(270);
		//
		// final Angle fromAltitude = Angle.fromDegrees(90);
		// final Angle toAltitude = Angle.fromDegrees(15);
		//
		// shape.orbitCamera(TimeInterval.fromSeconds(5), fromDistance,
		// toDistance, fromAzimuth, toAzimuth, fromAltitude,
		// toAltitude);
		//
		//
		// }
		//
		//
		// @Override
		// public void dispose() {
		// // TODO Auto-generated method stub
		//
		// }
		// };
		//
		//
		// shapesRenderer.loadBSONSceneJS(new URL("file:///target.bson"), "",
		// false, new Geodetic3D(Angle.fromDegrees(35.6452500000),
		// Angle.fromDegrees(-97.214), 30), AltitudeMode.RELATIVE_TO_GROUND,
		// Plistener);
		//
		//
		// builder.addRenderer(shapesRenderer);

		// if (false) {
		// shapesRenderer.loadBSONSceneJS(new URL("file:///A320.bson"),
		// URL.FILE_PROTOCOL + "textures-A320/", false,
		// new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24),
		// Angle.fromDegreesMinutesSeconds(-77, 2, 10.92),
		// 10000), AltitudeMode.ABSOLUTE, new ShapeLoadListener() {
		//
		// @Override
		// public void onBeforeAddShape(final SGShape shape) {
		// // TODO Auto-generated method stub
		// final double scale = 1e5;
		// shape.setScale(scale, scale, scale);
		// shape.setPitch(Angle.fromDegrees(90));
		//
		// }
		//
		//
		// @Override
		// public void onAfterAddShape(final SGShape shape) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		//
		// @Override
		// public void dispose() {
		// // TODO Auto-generated method stub
		//
		// }
		// }, true);
		// }

		// if (false) { // Testing lights
		// shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 0, 0),
		// AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(
		// 1000000, 1000000, 1000000), (float) 1.0, Color.red(), Color.black(),
		// true)); // With normals
		//
		// shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 180,
		// 0), AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(
		// 1000000, 1000000, 1000000), (float) 1.0, Color.blue(), Color.black(),
		// true)); // With normals
		//
		// }

		// if (false) { // Adding and deleting marks
		//
		// final int time = 1; // SECS
		//
		// final GTask markTask = new GTask() {
		// ArrayList<Mark> _marks = new ArrayList<Mark>();
		//
		//
		// int randomInt(final int max) {
		// return (int) (Math.random() * max);
		// }
		//
		//
		// @Override
		// public void run(final G3MContext context) {
		// final double lat = randomInt(180) - 90;
		// final double lon = randomInt(360) - 180;
		//
		// final Mark m1 = new Mark("RANDOM MARK", new
		// URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
		// Geodetic3D.fromDegrees(lat, lon, 0), AltitudeMode.RELATIVE_TO_GROUND,
		// 1e9);
		// marksRenderer.addMark(m1);
		//
		// _marks.add(m1);
		// if (_marks.size() > 5) {
		//
		// marksRenderer.removeAllMarks();
		//
		// for (int i = 0; i < _marks.size(); i++) {
		// _marks.get(i).dispose();
		// }
		//
		//
		// _marks.clear();
		//
		// }
		//
		// }
		// };
		//
		// builder.addPeriodicalTask(new
		// PeriodicalTask(TimeInterval.fromSeconds(time), markTask));
		// }

		// if (false) {
		//
		// final GInitializationTask initializationTask = new
		// GInitializationTask() {
		//
		// @Override
		// public void run(final G3MContext context) {
		//
		// final IBufferDownloadListener listener = new
		// IBufferDownloadListener() {
		//
		// @Override
		// public void onError(final URL url) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		//
		// @Override
		// public void onDownload(final URL url,
		// final IByteBuffer buffer,
		// final boolean expired) {
		// // TODO Auto-generated method stub
		//
		// final Shape shape = SceneJSShapesParser.parseFromBSON(buffer,
		// URL.FILE_PROTOCOL + "2029/", true,
		// Geodetic3D.fromDegrees(0, 0, 0), AltitudeMode.ABSOLUTE);
		//
		// shapesRenderer.addShape(shape);
		// }
		//
		//
		// @Override
		// public void onCanceledDownload(final URL url,
		// final IByteBuffer buffer,
		// final boolean expired) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		//
		// @Override
		// public void onCancel(final URL url) {
		// // TODO Auto-generated method stub
		//
		// }
		// };
		//
		// context.getDownloader().requestBuffer(new URL(URL.FILE_PROTOCOL +
		// "2029/2029.bson"), 1000, TimeInterval.forever(),
		// true, listener, true);
		//
		//
		// }
		//
		//
		// @Override
		// public boolean isDone(final G3MContext context) {
		// // TODO Auto-generated method stub
		// return true;
		// }
		//
		// };
		//
		// builder.setInitializationTask(initializationTask);
		//
		// }

		//      if (false) {
		//
		//         final int time = 10; // SECS
		//
		//         final GTask elevationTask = new GTask() {
		//
		//            ElevationDataProvider _elevationDataProvider1 = new SingleBillElevationDataProvider(new URL(
		//                                                                   "file:///full-earth-2048x1024.bil", false),
		//                                                                   Sector.fullSphere(), new Vector2I(2048, 1024));
		//
		//
		//            @Override
		//            public void run(final G3MContext context) {
		//               final PlanetRenderer pr = _g3mWidget.getG3MWidget().getPlanetRenderer();
		//
		//               final Random r = new Random();
		//
		//               final int i = r.nextInt() % 4;
		//               switch (i) {
		//                  case 0:
		//                     pr.setElevationDataProvider(_elevationDataProvider1, false);
		//                     break;
		//                  case 1:
		//
		//                     final ElevationDataProvider _elevationDataProvider2 = new SingleBillElevationDataProvider(new URL(
		//                              "file:///caceres-2008x2032.bil", false), Sector.fromDegrees(39.4642996294239623,
		//                              -6.3829977122432933, 39.4829891936013553, -6.3645288909498845), new Vector2I(2008, 2032), 0);
		//
		//
		//                     pr.setElevationDataProvider(_elevationDataProvider2, true);
		//                     break;
		//                  case 2:
		//                     pr.setVerticalExaggeration(r.nextInt() % 5);
		//                     break;
		//                  case 3:
		//                     pr.setElevationDataProvider(null, false);
		//                     break;
		//
		//                  default:
		//                     break;
		//               }
		//
		//               final ElevationDataProvider edp = pr.getElevationDataProvider();
		//               if (edp != null) {
		//                  edp.setEnabled((r.nextInt() % 2) == 0);
		//               }
		//            }
		//         };
		//
		//         builder.addPeriodicalTask(new PeriodicalTask(TimeInterval.fromSeconds(time), elevationTask));
		//
		//    }

		//BEGINNING OF CODE FOR PRECACHING AREA

		//Las Palmas de GC
		Geodetic2D upper = Geodetic2D.fromDegrees(28.20760859532738, -15.3314208984375);
		Geodetic2D lower = Geodetic2D.fromDegrees(28.084096949164735, -15.4852294921875);

		PrecacherInitializationTask pit = new PrecacherInitializationTask(null, upper, lower, 6);
		builder.setInitializationTask(pit);
		_g3mWidget = builder.createWidget();  
		pit.setWidget(_g3mWidget);

		_placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
		_placeHolder.addView(_g3mWidget);
		
		//END OF CODE FOR PRECACHING AREA

	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}


//BEGINNING OF CODE FOR PRECACHING AREA

class PrecacherInitializationTask extends GInitializationTask {

	private class PrecacherDownloadListener extends IImageDownloadListener {

		PrecacherInitializationTask _task;

		public PrecacherDownloadListener(PrecacherInitializationTask task) {
			_task = task;
		}

		@Override
		public void onDownload(URL url, IImage image, boolean expired) {
			// TODO Auto-generated method stub
			_task.imageDownloaded();

		}

		@Override
		public void onError(URL url) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancel(URL url) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCanceledDownload(URL url, IImage image, boolean expired) {
			// TODO Auto-generated method stub

		}

	}

	private int _nImagesDownloaded = 0;
	private LinkedList<String> _urls;
	private boolean _done = false;
	private G3MWidget_Android _widget = null;

	private Geodetic2D _upper, _lower;
	private int _level;

	public void setWidget(G3MWidget_Android widget){
		_widget = widget;
	}

	public PrecacherInitializationTask(G3MWidget_Android widget, Geodetic2D upper, Geodetic2D lower, int level) {
		_widget = widget;
		_upper = upper;
		_lower = lower;
		_level = level;
	}

	public void imageDownloaded() {
		_nImagesDownloaded++;
		if (_nImagesDownloaded % 10 == 0) {
			ILogger.instance().logInfo("IMAGE DOWNLOADED %d\n",
					_nImagesDownloaded);
		}
		int size = _urls.size();
		if (_nImagesDownloaded == size) {
			ILogger.instance().logInfo("ALL IMAGES DOWNLOADED \n");
			_done = true;
		}
	}

	@Override
	public void run(G3MContext context) {

		G3MWidget widget = _widget.getG3MWidget();

		_urls = widget.getPlanetRenderer().getTilesURL(_lower, _upper, _level);

		IDownloader downloader = context.getDownloader();

		for (int i = 0; i < _urls.size(); i++) {
			String url = _urls.get(i);
			downloader.requestImage(new URL(url), 1000,
					TimeInterval.fromSeconds(0), false,
					new PrecacherDownloadListener(this), true);
		}

	}

	@Override
	public boolean isDone(G3MContext context) {
		return _done;
	}

}

//END OF CODE FOR PRECACHING AREA
