package org.glob3.mobile.specific;

import java.util.ArrayList;
import java.util.Random;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BusyRenderer;
import org.glob3.mobile.generated.CPUTextureBuilder;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.Downloader;
import org.glob3.mobile.generated.DummyRenderer;
import org.glob3.mobile.generated.EffectTarget;
import org.glob3.mobile.generated.EffectsScheduler;
import org.glob3.mobile.generated.EllipsoidalTileTessellator;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GL;
import org.glob3.mobile.generated.GLErrorRenderer;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LatLonMeshRenderer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.SGCubeNode;
import org.glob3.mobile.generated.SampleEffect;
import org.glob3.mobile.generated.SceneGraphRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimplePlanetRenderer;
import org.glob3.mobile.generated.SingleImageTileTexturizer;
import org.glob3.mobile.generated.TextureBuilder;
import org.glob3.mobile.generated.TexturesHandler;
import org.glob3.mobile.generated.TileImagesTileTexturizer;
import org.glob3.mobile.generated.TileParameters;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileTexturizer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.SimpleCameraConstrainer;

import android.R.bool;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class G3MWidget_Android extends GLSurfaceView implements
		OnGestureListener {

	G3MWidget _widget;
	ES2Renderer _es2renderer;

	final MotionEventProcessor _motionEventProcessor = new MotionEventProcessor();

	public G3MWidget_Android(Context context) {
		super(context);

		setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
		setEGLConfigChooser(true); // IT GIVES US A RGB DEPTH OF 8 BITS PER
									// CHANNEL, HAVING TO FORCE PROPER BUFFER
									// ALLOCATION

		// Detect Long-Press events
		setLongClickable(true);

		// Debug flags
		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
	}

	// The initialization of _widget occurs when the android widget is resized
	// to the screen size
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (_widget != null)
			return; // No further initialization needed

		int width = getWidth();
		int height = getHeight();

		// Creating factory
		IFactory factory = new Factory_Android(this.getContext());

		// RENDERERS
		CompositeRenderer comp = new CompositeRenderer();

		// Camera must be first
		CameraRenderer cr = new CameraRenderer();
		comp.addRenderer(cr);

		// Dummy cube
		if (false) {
			DummyRenderer dummy = new DummyRenderer();
			comp.addRenderer(dummy);
		}

		// simple planet renderer, with a basic world image
		if (true) {
			SimplePlanetRenderer spr = new SimplePlanetRenderer("world.jpg");
			comp.addRenderer(spr);
		}

		// marks renderer
		if (true) {
			MarksRenderer marks = new MarksRenderer();
			comp.addRenderer(marks);
			Mark m1 = new Mark("Fuerteventura", "plane.png", new Geodetic3D(
					Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0));
			marks.addMark(m1);

			Mark m2 = new Mark("Las Palmas", "plane.png", new Geodetic3D(
					Angle.fromDegrees(28.05), Angle.fromDegrees(-15.36), 0));
			marks.addMark(m2);

			Random r = new Random();
			for (int i = 0; i < 1000; i++) {
				Angle latitude = Angle
						.fromDegrees((int) (r.nextInt() % 180) - 90);
				Angle longitude = Angle
						.fromDegrees((int) (r.nextInt() % 360) - 180);

				marks.addMark(new Mark("Random", "mark.png", new Geodetic3D(
						latitude, longitude, 0)));
			}
		}

		if (true) {
			EffectsScheduler scheduler = new EffectsScheduler();
			scheduler.startEffect(new DummyEffect(TimeInterval.fromSeconds(3)));
			comp.addRenderer(scheduler);
		}

		ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
		IGL gl = new GL2();

		TexturesHandler texturesHandler = new TexturesHandler();

		_widget = G3MWidget.create(factory, logger, gl, texturesHandler, Planet
				.createEarth(), comp, width, height, Color.fromRGBA(
				(float) 0.0, (float) 0.1, (float) 0.2, (float) 1.0), true);

		// SETTING RENDERER
		_es2renderer = new ES2Renderer(this.getContext(), _widget);
		setRenderer(_es2renderer);
	}

	public boolean onTouchEvent(MotionEvent event) {

		final TouchEvent te = _motionEventProcessor.processEvent(event);

		if (te != null) {
			// SEND MESSAGE TO RENDER THREAD
			queueEvent(new Runnable() {
				public void run() {
					_widget.onTouchEvent(te);
				}
			});
			return true;
		} else {
			return false;
		}
	}

	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	

	private void initWidgetDemo(int width, int height, Context ctx, String documentsDirectory)
	{
	  IFactory factory = new Factory_Android(ctx);
	  ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
	  
	  NativeGL2_Android nGL = new NativeGL2_Android(); 
	  GL gl  = new GL(nGL);
	  
	  // composite renderer is the father of the rest of renderers
	  CompositeRenderer comp = new CompositeRenderer();
	  
	  // camera renderer and handlers
	  CameraRenderer cameraRenderer = new CameraRenderer();
	  cameraRenderer.addHandler(new CameraSingleDragHandler());
	  cameraRenderer.addHandler(new CameraDoubleDragHandler());
	  cameraRenderer.addHandler(new CameraRotationHandler());
	  cameraRenderer.addHandler(new CameraDoubleTapHandler());
	  comp.addRenderer(cameraRenderer);

	  
	  //STORAGE
	  FileSystemStorage fss = new FileSystemStorage(documentsDirectory);
	  Downloader downloaderOLD = new Downloader(fss, 5, factory.createNetwork());
	  IDownloader downloader = new Downloader_Android(1 * 1024 * 1024,
	                                               64 * 1024 * 1024,
	                                               ".G3M_Cache",
	                                               8);

	  //LAYERS
	  LayerSet layerSet = new LayerSet();
	  WMSLayer baseLayer = new WMSLayer("bmng200405", "http://www.nasa.network.com/wms?", 
	                                     "1.3", "image/jpeg", Sector.fullSphere(), "EPSG:4326", "", false,
	                                     Angle.nan(), Angle.nan());

	  WMSLayer vias = new WMSLayer("VIAS",
	                                "http://idecan2.grafcan.es/ServicioWMS/Callejero",
	                                "1.1.0", "image/gif", 
	                                Sector.fromDegrees(22.5,-22.5, 33.75, -11.25),
	                                "EPSG:4326", "", true,
	                                Angle.nan(), Angle.nan());
	  
	  WMSLayer pnoa = new WMSLayer("PNOA",
	                                "http://www.idee.es/wms/PNOA/PNOA",
	                                "1.1.0", "image/png", 
	                                Sector.fromDegrees(21,-18, 45, 6),
	                                "EPSG:4326", "", true,
	                                Angle.nan(), Angle.nan());
	  
	  //ORDER IS IMPORTANT
	  layerSet.add(baseLayer);
	  layerSet.add(pnoa);
	  layerSet.add(vias);
	  
	  // very basic tile renderer
	  if (true) {
	    TileParameters parameters = TileParameters.createDefault(true);
	    
	    TileTexturizer texturizer = NULL;
	    if (true) {
	      texturizer = new TileImagesTileTexturizer(parameters, downloaderOLD, layerSet, factory); //WMS
	    }
	    else {
	      //SINGLE IMAGE
	      IImage singleWorldImage = factory.createImageFromFileName("world.jpg");
	      texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage);
	    }
	    
	    const bool showStatistics = true;
	    TileRenderer tr = new TileRenderer(new EllipsoidalTileTessellator(parameters._tileResolution, true),
	                                        texturizer,
	                                        parameters,
	                                        showStatistics);
	    comp.addRenderer(tr);
	  }
	  
	  if (false) {
	    // dummy renderer with a simple box
	    DummyRenderer dum = new DummyRenderer();
	    comp.addRenderer(dum);
	  }
	  
	  if (false) {
	    // simple planet renderer, with a basic world image
	    SimplePlanetRenderer spr = new SimplePlanetRenderer("world.jpg");
	    comp.addRenderer(spr);
	  }
	  
	  if (false) {
	    // marks renderer
	    MarksRenderer marks = new MarksRenderer();
	    comp.addRenderer(marks);
	    
	    Mark m1 = new Mark("Fuerteventura",
	                        "g3m-marker.png",
	                        new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0));
	    //m1->addTouchListener(listener);
	    marks.addMark(m1);
	    
	    
	    Mark m2 = new Mark("Las Palmas",
	                        "g3m-marker.png",
	                        new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-15.36), 0));
	    //m2->addTouchListener(listener);
	    marks.addMark(m2);
	    
	    if (false) {

	    	Random r = new Random();
	      for (int i = 0; i < 500; i++) {
	        final Angle latitude = Angle.fromDegrees( (int) (r.nextInt() % 180) - 90 );
	        final Angle longitude = Angle.fromDegrees( (int) (r.nextInt() % 360) - 180 );
	        //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());
	        
	        marks.addMark(new Mark("Random",
	                                "g3m-marker.png",
	                                new Geodetic3D(latitude, longitude, 0)));
	      }
	    }
	  }
	  
	  if (true) {
	    LatLonMeshRenderer renderer = new LatLonMeshRenderer();
	    comp.addRenderer(renderer);
	  }
	  
	  EffectsScheduler scheduler = new EffectsScheduler();
	  
	  if (false) {
	    EffectTarget target = null;
	    scheduler.startEffect(new SampleEffect(TimeInterval.fromSeconds(2)),
	                           target);
	  }
	  
	  if (false) {
	    SceneGraphRenderer sgr = new SceneGraphRenderer();
	    SGCubeNode cube = new SGCubeNode();
	    // cube->setScale(Vector3D(6378137.0, 6378137.0, 6378137.0));
	    sgr.getRootNode().addChild(cube);
	    comp.addRenderer(sgr);
	  }
	  
	  comp.addRenderer(new GLErrorRenderer());
	  
	  
	  TextureBuilder texBuilder = new CPUTextureBuilder();
	  TexturesHandler texturesHandler = new TexturesHandler(gl, factory, texBuilder, false);
	  
	  Planet planet = Planet.createEarth();
	  
	  BusyRenderer busyRenderer = new BusyRenderer();
	  
	  ArrayList<ICameraConstrainer> cameraConstraint;
	  cameraConstraint.push_back(new SimpleCameraConstrainer());

	  _widget = G3MWidget::create(factory,
	                              logger,
	                              gl,
	                              texturesHandler,
	                              downloaderOLD,
	                              downloader,
	                              planet, 
	                              cameraConstraint,
	                              comp,
	                              busyRenderer,
	                              scheduler,
	                              width, height,
	                              Color::fromRGBA((float)0, (float)0.1, (float)0.2, (float)1),
	                              true);
	                              
	                             
	}

}
