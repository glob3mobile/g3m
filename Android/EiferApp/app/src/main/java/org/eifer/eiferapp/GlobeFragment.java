package org.eifer.eiferapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.eifer.eiferapp.g3mutils.AltitudeFixerLM;
import org.eifer.eiferapp.g3mutils.CorrectedAltitudeFixedLM;
import org.eifer.eiferapp.g3mutils.HoleCoverHelper;
import org.eifer.eiferapp.g3mutils.KarlsruheVirtualWalkLM;
import org.eifer.eiferapp.g3mutils.MeteoListener;
import org.eifer.eiferapp.g3mutils.MyCityGMLBuildingTouchedListener;
import org.eifer.eiferapp.g3mutils.MyCityGMLRendererListener;
import org.eifer.eiferapp.g3mutils.MyEDCamConstrainer;
import org.eifer.eiferapp.g3mutils.MyEDListener;
import org.eifer.eiferapp.g3mutils.MyHoleListener;
import org.eifer.eiferapp.g3mutils.PipeTouchedListener;
import org.eifer.eiferapp.g3mutils.PipesModel;
import org.eifer.eiferapp.g3mutils.PipesRenderer;
import org.eifer.eiferapp.g3mutils.PointCloudEvolutionTask;
import org.eifer.eiferapp.g3mutils.SchlossListener;
import org.eifer.eiferapp.g3mutils.UtilityNetworkParser;
import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.CityGMLBuilding;
import org.glob3.mobile.generated.CityGMLBuildingTessellator;
import org.glob3.mobile.generated.CityGMLRenderer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeElevationDataProvider;
import org.glob3.mobile.generated.Cylinder;
import org.glob3.mobile.generated.DeviceAttitudeCameraHandler;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.EllipsoidalPlanet;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MEventContext;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILocationModifier;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.RandomBuildingColorPicker;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.TerrainTouchListener;
import org.glob3.mobile.generated.Tile;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2F;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.ViewMode;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import java.util.ArrayList;
import java.util.Timer;


/**
 * Created by chano on 9/11/17.
 */

public class GlobeFragment extends Fragment{
    public GlobeFragment() {
    }
    //------------------------------
    // Variables y clases
    //------------------------------

    class CityGMLModelFile {
        public String _fileName;
        public boolean _needsToBeFixedOnGround;
    };

    private ArrayList<CityGMLModelFile> _cityGMLFiles = new ArrayList<>();

    public G3MWidget_Android widget;

    public CityGMLRenderer cityGMLRenderer;
    public MyEDCamConstrainer camConstrainer;
    private ShapesRenderer shapesRenderer;
    private MeshRenderer pipeMeshRenderer;
    private MeshRenderer meshRenderer;
    private MeshRenderer holeRenderer;
    private MeshRenderer meshRendererPointCloud;
    private PipesRenderer pipesRenderer;

    public CompositeElevationDataProvider combo;
    public ElevationData elevData = null;
    private ElevationData holeElevData = null; //hole change.
    private boolean isUsingDem;

    private int _modelsLoadedCounter;
    private int buildingColor = 0;
    private int mapMode = 0;
    private boolean correction = false;

    boolean _isMenuAvailable = false;
    //VR
    Geodetic3D _prevPos = null;
    Angle _prevHeading = null;
    Angle _prevRoll = null;
    Angle _prevPitch = null;
    //VC
    //CameraViewController _camVC; // ¿Qué será esto?

    PointCloudEvolutionTask _pointCloudTask;
    CityGMLBuilding _buildingShowingPC;
    //Location Mode
    boolean _locationUsesRealGPS = true;
    DeviceAttitudeCameraHandler _dac = null;
    MarksRenderer marksRenderer,falseMarksRenderer;

    public Mark positionMark; public Angle heading = null;

    //------------------------------
    // Código globero
    //------------------------------

    private void initActivity(Bundle savedInstanceState){
        // TODO stuff here.
        addBuildings();
        initGlobe();
        initTask(true);
    }

    private void addBuildings(){

       addCityGMLFile("file:///innenstadt_ost_4326_lod2.gml",false);
	   //addCityGMLFile("file:///innenstadt_west_4326_lod2.gml",false);
	   //addCityGMLFile("file:///technologiepark_WGS84.gml",true);
        addCityGMLFile("file:///tpk_lod3_t1_wgs84.gml",true);

	 //  [self addCityGMLFile:"file:///AR_demo_with_buildings.gml" needsToBeFixOnGround:true]; //NOT WORKING
	   /*addCityGMLFile("file:///hagsfeld_4326_lod2.gml",false);
	   addCityGMLFile("file:///durlach_4326_lod2_PART_1.gml",false);
	   addCityGMLFile("file:///durlach_4326_lod2_PART_2.gml",false);
	   addCityGMLFile("file:///hohenwettersbach_4326_lod2.gml",false);
	   addCityGMLFile("file:///bulach_4326_lod2.gml",false);
	   addCityGMLFile("file:///daxlanden_4326_lod2.gml",false);
	   addCityGMLFile("file:///knielingen_4326_lod2_PART_1.gml",false);
	   addCityGMLFile("file:///knielingen_4326_lod2_PART_2.gml",false);
       addCityGMLFile("file:///knielingen_4326_lod2_PART_3.gml",false); */
        //addCityGMLFile("file:///building-1.gml",false);
        //addCityGMLFile("file:///building-2.gml",false);
    }

    private void addCityGMLFile(String fileName, boolean needsToBeFixOnGround){
        CityGMLModelFile m = new CityGMLModelFile();
        m._fileName = fileName;
        m._needsToBeFixedOnGround = needsToBeFixOnGround;
        _cityGMLFiles.add(m);
    }

    private void initGlobe(){
        G3MBuilder_Android builder = new G3MBuilder_Android(getActivity());


        final LayerSet layerSet = new LayerSet();
        layerSet.addLayer(LayerBuilder.createBingLayer(true));
        final Planet planet = EllipsoidalPlanet.createEarth();
        builder.setPlanet(planet);
        builder.getPlanetRendererBuilder().setLayerSet(layerSet);

        pipeMeshRenderer = new MeshRenderer();
        //builder.addRenderer(pipeMeshRenderer);

        pipesRenderer = new PipesRenderer(pipeMeshRenderer,this);
        pipesRenderer.setHoleMode(isHole());
        pipesRenderer.setTouchListener(new PipeTouchedListener() {
            @Override
            public void onPipeTouched(final Cylinder pipe, final Cylinder.CylinderMeshInfo info) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        // set title
                        alertDialogBuilder.setTitle(getString(R.string.pipes_title));
                        // Set message
                        String msg = info.getMessage();
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.pipes_ok),new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) { }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });
            }
        });
        builder.addRenderer(pipesRenderer);

        holeRenderer = new MeshRenderer();
        builder.addRenderer(holeRenderer);
        meshRenderer = new MeshRenderer();
        //builder.addRenderer(meshRenderer);
        meshRendererPointCloud = new MeshRenderer();
        builder.addRenderer(meshRendererPointCloud);
        marksRenderer = new MarksRenderer(false);
        builder.addRenderer(marksRenderer);
        positionMark = new Mark(new URL("file:///bolita.png",false),
                Geodetic3D.fromDegrees(28,-15.50,0),
                AltitudeMode.ABSOLUTE);
        marksRenderer.addMark(positionMark);
        marksRenderer.setEnable(false);

        falseMarksRenderer = new MarksRenderer(false);
        builder.addRenderer(falseMarksRenderer);
        shapesRenderer = new ShapesRenderer();
        builder.addRenderer(shapesRenderer);


        cityGMLRenderer = new CityGMLRenderer(meshRenderer,
                null /* marksRenderer */,
                null);

        cityGMLRenderer.setTouchListener(new MyCityGMLBuildingTouchedListener(this));
        builder.addRenderer(cityGMLRenderer);

        camConstrainer = new MyEDCamConstrainer(null,"",getActivity()); //Wait for ED to arrive
        builder.addCameraConstraint(camConstrainer);
        builder.setBackgroundColor(Color.transparent());

        widget = builder.createWidget();


        RelativeLayout _placeHolder = (RelativeLayout) this.getActivity().findViewById(R.id.g3mWidgetHolderShort);
        _placeHolder.addView(widget);
    }

    public void activePositionFixer(){
        marksRenderer.setEnable(true);
        camConstrainer.setMark(positionMark);
    }

    public void stopPositionFixer(){
        camConstrainer.setMark(null);
        marksRenderer.setEnable(false);
        heading = camConstrainer.markHeading;
    }

    private void initTask(boolean useDem){
        isUsingDem = useDem;
        widget.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        widget.setZOrderMediaOverlay(true);
        if (useDem){
            HoleCoverHelper.loadHoleImage(widget.getG3MContext().getDownloader(),
                    widget.getG3MContext().getThreadUtils(),
                    "file:///holetexture.jpg");
            HoleCoverHelper.loadCoverImage(widget.getG3MContext().getDownloader(),
                    widget.getG3MContext().getThreadUtils(),
                    "file:///covertexture3.jpg");
            Geodetic3D pipeCenter = Geodetic3D.fromDegrees(0,0,0); //Geodetic3D.fromDegrees(49.01664816, 8.43442120,0);
            Planet p = widget.getG3MContext().getPlanet();
            Vector3D v1= p.toCartesian(pipeCenter);
            Vector3D v2 = new Vector3D(v1._x - 10, v1._y - 10, v1._z);
            Vector3D v3 = new Vector3D(v1._x + 10, v1._y + 10, v1._z);
            Geodetic3D l = p.toGeodetic3D(v2);
            Geodetic3D u = p.toGeodetic3D(v3);
            Geodetic2D lower = Geodetic2D.fromDegrees(
                    Math.min(l._latitude._degrees, u._latitude._degrees),
                    Math.min(l._longitude._degrees, u._longitude._degrees));
            Geodetic2D upper = Geodetic2D.fromDegrees(
                    Math.max(l._latitude._degrees, u._latitude._degrees),
                    Math.max(l._longitude._degrees, u._longitude._degrees));

            Sector holeSector = new Sector(lower,upper);
            SingleBilElevationDataProvider holeEdp = new SingleBilElevationDataProvider(new URL("file:///hole3.bil"),
                    holeSector,
                    new Vector2I(11, 11));

            holeEdp.requestElevationData(holeSector, new Vector2I(11, 11),new MyHoleListener(this), true);


            Sector karlsruheSector = Sector.fromDegrees(48.9397891179, 8.27643508429, 49.0930546874, 8.5431344933);
            SingleBilElevationDataProvider edp = new SingleBilElevationDataProvider(new URL("file:///ka_31467.bil"),
                    karlsruheSector,
                    new Vector2I(308, 177));

            edp.requestElevationData(karlsruheSector, new Vector2I(308, 177),new MyEDListener(this), true);

            combo = new CompositeElevationDataProvider();
            combo.addElevationDataProvider(holeEdp);
            combo.addElevationDataProvider(edp);
            widget.getG3MWidget().getPlanetRenderer().setElevationDataProvider(combo,true);
            widget.getG3MWidget().getPlanetRenderer().addTerrainTouchListener(new TerrainTouchListener(){

                @Override
                public void dispose() {}

                @Override
                public boolean onTerrainTouch(G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position,
                                              Tile tile) {
                    if (isHole()){
                        if (mapMode == 0)
                            changeHole(position);
                        else{
                            Vector3D cameraDir = camera.getViewDirection();
                            Camera c = new Camera(camera.getTimestamp());
                            c.copyFrom(camera,true);
                            c.translateCamera(cameraDir.normalized().times(25));
                            Geodetic3D cameraPos = c.getGeodeticPosition();
                            changeHole(cameraPos);
                        }

                    }


                    return false;
                }

            });;
            //camConstrainer.setEDP(combo);
            //camConstrainer.setThreadUtils(widget.getG3MContext().getThreadUtils());
        }
        else{
            loadCityModel();
        }
    }

    public void changeHole(Geodetic3D position){
        Planet planet = widget.getG3MContext().getPlanet();
        IThreadUtils _tUtils = widget.getG3MContext().getThreadUtils();

        Vector3D v1= planet.toCartesian(position);
        Vector3D v2 = new Vector3D(v1._x - 10, v1._y - 10, v1._z);
        Vector3D v3 = new Vector3D(v1._x + 10, v1._y + 10, v1._z);
        Geodetic3D l = planet.toGeodetic3D(v2);
        Geodetic3D u = planet.toGeodetic3D(v3);
        Geodetic2D lower = Geodetic2D.fromDegrees(
                Math.min(l._latitude._degrees, u._latitude._degrees),
                Math.min(l._longitude._degrees, u._longitude._degrees));
        Geodetic2D upper = Geodetic2D.fromDegrees(
                Math.max(l._latitude._degrees, u._latitude._degrees),
                Math.max(l._longitude._degrees, u._longitude._degrees));
        final Sector holeSector = new Sector(lower,upper);
        HoleCoverHelper.generateHoleCover(holeSector, v1, planet,widget.getG3MWidget().getRenderContext(), elevData, holeRenderer);

        ILogger.instance().logError(holeSector.description());
        final SingleBilElevationDataProvider holeEdp = new SingleBilElevationDataProvider(new URL("file:///hole3.bil"),
                holeSector,
                new Vector2I(11, 11));
        //holeEdp.requestElevationData(holeSector, new Vector2I(11, 11),new MyHoleListener(this), true);


        _tUtils.invokeInRendererThread(new GTask(){

            @Override
            public void run(G3MContext context) {
                // TODO Auto-generated method stub
                holeElevData.setSector(holeSector);
                combo.changeFirstEDP(holeEdp);
            }
        }, true);
    }

    public void loadCityModel(){
        MainActivity activity = (MainActivity) getActivity();
        activity.openProgressDialog(_cityGMLFiles.size());
        if ((isUsingDem && holeElevData != null && elevData != null) || (!isUsingDem)) {

            for (int i = 0; i < _cityGMLFiles.size(); i++) {

                //NativeUtils.assignHtmlToElementId(_modelsLoadedCounter+" out of "+_cityGMLFiles.size()+" models loaded (0 %)","LoadingDetails");

                cityGMLRenderer.addBuildingsFromURL(new URL(_cityGMLFiles.get(i)._fileName),
                        _cityGMLFiles.get(i)._needsToBeFixedOnGround,
                        new MyCityGMLRendererListener(this),
                        true);
            }

            addPipeMeshes();
        }
    }

    public void addPipeMeshes()
    {
        UtilityNetworkParser.initialize(widget.getG3MContext(),elevData,pipeMeshRenderer,-4.0);
        UtilityNetworkParser.parseFromURL(new URL("file:///jochen_underground.gml"));


        PipesModel.addMeshes("file:///pipesCoords.csv", widget.getG3MContext().getPlanet(), pipeMeshRenderer, elevData, -4.0,
                widget.getG3MContext().getDownloader());
        PipesModel.addMeshes("file:///pipesCoordsMetzt.csv", widget.getG3MContext().getPlanet(), pipeMeshRenderer, null, -4.0,
                widget.getG3MContext().getDownloader());

    }

    public void setElevationData(ElevationData ed){
        elevData = ed;
    }

    public void setHoleElevationData(ElevationData ed){
        holeElevData = ed;
    }

    public boolean areAllPipesLoaded(){
        return (PipesModel.loadedFiles == 2);
    }

    public void onCityModelLoaded(){
        _modelsLoadedCounter++;
        if (_modelsLoadedCounter == _cityGMLFiles.size()){
            cityGMLRenderer.addBuildingDataFromURL(new URL("file:///karlsruhe_data.geojson"));
        }
        onProgress();
    }

    public void onProgress() {
        float p = (_cityGMLFiles.size() == 0) ? 1: (float)(_modelsLoadedCounter) / ((float)_cityGMLFiles.size());
        //NativeUtils.assignHtmlToElementId(_modelsLoadedCounter+" out of "+_cityGMLFiles.size()+" models loaded ("+p*100+" %)","LoadingDetails");
        MainActivity activity = (MainActivity) getActivity();
        activity.updateDialog();

        if (p == 1){
            onAllDataLoaded();
        }
    }

    public void onAllDataLoaded(){
        ILogger.instance().logInfo("City Model Loaded");

        final boolean includeCastleModel = true;
        if (includeCastleModel){
            shapesRenderer.loadJSONSceneJS(new URL("file:///k_schloss.json"),
                    "file:///k_s/",
                    false, // isTransparent
                    new Geodetic3D(Geodetic3D.fromDegrees(49.013500, 8.404249, 117.82)), //
                    AltitudeMode.ABSOLUTE,
                    new SchlossListener());

            MarkTouchListener randomListener = new MarkTouchListener() {
                @Override
                public boolean touchedMark(Mark mark) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity activity = (MainActivity) getActivity();
                            activity.openGraphDialog();
                        }
                    });
                    return false;
                }
            };

            shapesRenderer.loadJSONSceneJS(new URL("file:///boyas.json"),
                    "file:///k_s/",
                    false,
                    Geodetic3D.fromDegrees(49.01098659,8.40410182,121.91),
                    AltitudeMode.ABSOLUTE,
                    new MeteoListener());
            falseMarksRenderer.addMark(new Mark(
                    new URL("file:///transparent2.png",false),
                    Geodetic3D.fromDegrees(49.01098659,8.40410182,0),
                    AltitudeMode.RELATIVE_TO_GROUND,
                    1000,
                    null,
                    true,
                    randomListener,
                    true));

            String v[] = {"91214493", "23638639", "15526553", "15526562", "15526550", "13956101", "156061723", "15526578"};

            for (int i = 0; i < 8; i++){
                CityGMLBuilding b = cityGMLRenderer.getBuildingWithName(v[i]);
                if (b != null){
                    CityGMLBuildingTessellator.changeColorOfBuildingInBoundedMesh(b, Color.transparent());
                }
            }


        }

        //Whole city!
        widget.getG3MContext().getThreadUtils().invokeInRendererThread(new GTask(){

            @Override
            public void run(G3MContext context) {
                // TODO Auto-generated method stub
                /*widget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(5),
                        Geodetic3D.fromDegrees(49.07139214735035182, 8.134019638291379195, 22423.46165080198989),
                        Angle.fromDegrees(-109.452892),
                        Angle.fromDegrees(-44.938813)
                );*/
                widget.setCameraPosition(Geodetic3D.fromDegrees(49.07139214735035182, 8.134019638291379195, 22423.46165080198989));
                widget.setCameraHeading(Angle.fromDegrees(-109.452892));
                widget.setCameraPitch(Angle.fromDegrees(-44.938813));
                camConstrainer.shouldCaptureMotion(true, pipeMeshRenderer, PipesModel.cylinderInfo);
            }

        }, true);


	 /*
	 //METZ
	   [G3MWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
	                                                 Geodetic3D::fromDegrees(49.1034419341, 6.2225732157,  100.0),//1000
	                                                 Angle::fromDegrees(0.0),//0
	                                                 Angle::fromDegrees(-15.0) //-90
	                                                 );
	  */


    }

    public boolean areBuildingsEnabled(){
        return meshRenderer.isEnable();
    }

    public boolean arePipesEnabled(){
        return pipeMeshRenderer.isEnable();
    }

    public void setBuildingsEnabled(boolean enabled){
        cityGMLRenderer.setEnable(enabled);
        meshRenderer.setEnable(enabled);
    }

    public void setPipesEnabled(boolean enabled){
        pipeMeshRenderer.setEnable(enabled);
    }

    public int getAlphaMethod(){
        return Cylinder.getDistanceMethod() - 1;
    }

    public void setAlphaMethod(int method){
        Cylinder.setDistanceMethod(method+1);
    }

    public boolean isHole(){
        return Cylinder.isDepthEnabled();
    }

    public void setHole(final boolean enable){
        if (enable != Cylinder.isDepthEnabled()){
            pipesRenderer.setHoleMode(enable);
            holeRenderer.setEnable(enable);
            Cylinder.setDepthEnabled(enable);
            widget.getG3MContext().getThreadUtils().invokeInRendererThread(new GTask(){
                @Override
                public void run(G3MContext context){
                    if (!enable){
                        changeHole(Geodetic3D.fromDegrees(0,0,0));
                    }
                    pipeMeshRenderer.clearMeshes();
                    PipesModel.reset();
                    addPipeMeshes();

                }

            },true);
        }
    }

    public int getBuildingColor(){
        return buildingColor;
    }

    public int getMapMode(){
        return mapMode;
    }

    public void setMapMode(int mapMode){
        if (this.mapMode != mapMode){
            switch (mapMode){
                case 0:
                    activateMapMode();
                    this.mapMode = mapMode;
                    break;
                case 1:
                    if (canAccessLocation())
                        setMonoVR(mapMode);
                    else{
                        this.mapMode = mapMode;
                        askForLocationPermissions();
                    }
                    break;
                case 2:
                    if (canAccessLocation())
                        setStereoVR(mapMode);
                    else {
                        this.mapMode = mapMode;
                        askForLocationPermissions();
                    }
                    break;
                case 3:
                    if (canAccessCamera() && canAccessLocation()){
                        this.mapMode = mapMode;
                        setAR();
                    }
                    else {
                        askForCameraPermissions();
                    }

                    break;
            }
        }

    }

    public boolean getCorrection(){
        return correction;
    }

    public void setCorrection(boolean enable){
        correction = enable;
        changeLocationMode(_locationUsesRealGPS);
    }

    private void setAR(){
        MainActivity activity = (MainActivity) this.getActivity();
        activity.setCameraPreview();
        activateARMode();
    }

    private void setMonoVR(int mapMode){
        MainActivity activity = (MainActivity) this.getActivity();
        activity.unsetCameraPreviewIfNeeded();
        activateMonoVRMode();
        this.mapMode = mapMode;
    }

    private void setStereoVR(int mapMode){
        MainActivity activity = (MainActivity) this.getActivity();
        activity.unsetCameraPreviewIfNeeded();
        activateStereoVRMode();
        this.mapMode = mapMode;
    }

    public void setBuildingColor(int color){
        if (buildingColor != color){
            switch(color) {
                case 0:
                    RandomBuildingColorPicker rcp = new RandomBuildingColorPicker();
                    cityGMLRenderer.colorBuildings(rcp);
                    break;
                case 1:
                    cityGMLRenderer.colorBuildingsWithColorBrewer("Heat Demand", "Pastel1", 8);
                    break;
                case 2:
                    cityGMLRenderer.colorBuildingsWithColorBrewer("Building Volume", "Pastel1", 8);
                    break;
                case 3:
                    cityGMLRenderer.colorBuildingsWithColorBrewer("GHG Emissions", "Pastel1", 8);
                    break;
                case 4:
                    cityGMLRenderer.colorBuildingsWithColorBrewer("Demographic Clusters (SOM)", "Pastel1", 8);
                    break;
                case 5:
                    cityGMLRenderer.colorBuildingsWithColorBrewer("Demographic Clusters (k-Means)", "Pastel1", 8);
                    break;
            }
        }
        buildingColor = color;
    }

    public void loadSolarRadiationPointCloudForBuilding(CityGMLBuilding building){

        if (_buildingShowingPC != null){
            CityGMLBuildingTessellator.changeColorOfBuildingInBoundedMesh(_buildingShowingPC, Color.blue());
        }

        _buildingShowingPC = building;
        CityGMLBuildingTessellator.changeColorOfBuildingInBoundedMesh(_buildingShowingPC, Color.transparent());

        if (_pointCloudTask != null){
            widget.getG3MWidget().removeAllPeriodicalTasks();
        }
        _pointCloudTask = new PointCloudEvolutionTask(this);

        widget.getG3MWidget().addPeriodicalTask(new PeriodicalTask(TimeInterval.fromSeconds(0.1),
                _pointCloudTask));
    }

    public void removeSolarRadiationPointCloud(){
        widget.getG3MContext().getThreadUtils().invokeInRendererThread(new GTask() {
            @Override
            public void run(G3MContext context) {
                CityGMLBuildingTessellator.changeColorOfBuildingInBoundedMesh(_buildingShowingPC, Color.blue());
                widget.getG3MWidget().removeAllPeriodicalTasks();
                _pointCloudTask = null;
                _buildingShowingPC = null;
            }
        },true);

    }

    public void addPointCloudMesh(Mesh pc){
        meshRendererPointCloud.addMesh(pc);
    }

    public void removePointCloudMesh(){
        meshRendererPointCloud.clearMeshes();
        setPointCloudBar(false,"");
    }

    public void setPointCloudBar(boolean active,String content){
        MainActivity activity = (MainActivity) getActivity();
        activity.setPointCloudBar(active,content);
    }

    private void activateMapMode(){
        MainActivity activity = (MainActivity) this.getActivity();
        activity.unsetCameraPreviewIfNeeded();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        widget.getHolder().setFormat(PixelFormat.OPAQUE);

        widget.getG3MContext().getThreadUtils().invokeInRendererThread(new GTask() {
            @Override
            public void run(G3MContext context) {
                widget.getG3MWidget().getPlanetRenderer().setEnable(true);
                widget.getG3MWidget().setViewMode(ViewMode.MONO);

                CameraRenderer cameraRenderer = widget.getCameraRenderer();
                cameraRenderer.clearHandlers();
                _dac = null; //Clear Handlers has destroyed it

                //Restoring prev cam
                final Camera cam = widget.getG3MWidget().getCurrentCamera();
                widget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(2),
                        cam.getGeodeticPosition(), _prevPos,
                        cam.getHeading(), _prevHeading,
                        cam.getPitch(), _prevPitch);
                _prevPitch = null;
                _prevHeading = null;
                _prevRoll = null;
                _prevPos = null;


                final boolean useInertia = true;
                cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
                cameraRenderer.addHandler(new CameraDoubleDragHandler());
                cameraRenderer.addHandler(new CameraRotationHandler());
                cameraRenderer.addHandler(new CameraDoubleTapHandler());

                widget.getNextCamera().forceZNear(Double.NaN); //NAN
            }
        },true);
    }

    private void activateMonoVRMode(){
        widget.getHolder().setFormat(PixelFormat.OPAQUE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);


        widget.getG3MContext().getThreadUtils().invokeInRendererThread(new GTask() {
             @Override
             public void run(G3MContext context) {
                 widget.getG3MWidget().getPlanetRenderer().setEnable(true);
                 widget.getG3MWidget().setViewMode(ViewMode.MONO);
                 activateDeviceAttitudeTracking();
                 widget.getNextCamera().setFOV(Angle.nan(), Angle.nan());
             }
        },true);
    }

    private void activateDeviceAttitudeTracking(){

        CameraRenderer cameraRenderer = widget.getG3MWidget().getCameraRenderer();
        cameraRenderer.clearHandlers();
        _dac = null; //Clear Handlers has destroyed it

        //Storing prev cam
        if (_prevPos == null){
            final Camera cam = widget.getG3MWidget().getCurrentCamera();
            _prevPos = new Geodetic3D(cam.getGeodeticPosition());
            _prevRoll = new Angle(cam.getRoll());
            _prevPitch = new Angle(cam.getPitch());
            _prevHeading = new Angle(cam.getHeading());
        }

        if (_dac == null){
            ILocationModifier  lm = (_locationUsesRealGPS)?
                    (correction && heading != null) ?
                            new CorrectedAltitudeFixedLM(elevData,positionMark.getPosition().asGeodetic2D()) : new AltitudeFixerLM(elevData) :
                    new KarlsruheVirtualWalkLM(elevData);

            _dac = new DeviceAttitudeCameraHandler(true, lm);
        }
        cameraRenderer.addHandler(_dac);

        widget.getNextCamera().forceZNear(0.1);

        //Theoretical horizontal FOV
        float hFOVDegrees = ((MainActivity) getActivity()).getHorizontalFoV(); // ?
        //widget.getNextCamera().setFOV(Angle.nan(), Angle.fromDegrees(hFOVDegrees));

        //Mientras no resolvamos el "problemilla" de la cámara, FoV == NaN;
        widget.getNextCamera().setFOV(Angle.nan(),Angle.fromDegrees(hFOVDegrees));
    }

    private void activateStereoVRMode(){
        widget.getHolder().setFormat(PixelFormat.OPAQUE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        widget.getG3MContext().getThreadUtils().invokeInRendererThread(new GTask() {
            @Override
            public void run(G3MContext context) {
                widget.getG3MWidget().getPlanetRenderer().setEnable(true);

                widget.getG3MWidget().setViewMode(ViewMode.STEREO);
                widget.getG3MWidget().setInterocularDistanceForStereoView(0.03); //VR distance between eyes

                activateDeviceAttitudeTracking();
            }
        },true);
    }

    private void activateARMode(){
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        widget.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        widget.setZOrderMediaOverlay(true);
        //widget.setZOrderOnTop(true);
        widget.getG3MContext().getThreadUtils().invokeInRendererThread(new GTask() {
            @Override
            public void run(G3MContext context) {
                widget.getG3MWidget().getPlanetRenderer().setEnable(false);
                widget.getG3MWidget().setViewMode(ViewMode.MONO);
                activateDeviceAttitudeTracking();
                changeLocationMode(true);

            }
        },true);
        //Bad attempt to get what I want ...
        //widget.bringToFront();
        //widget.setZOrderOnTop(true);

        //MainActivity activity = (MainActivity) getActivity();
        //activity.setZ();


    }

    public boolean getUsesGPS(){
        return _locationUsesRealGPS;
    }

    public void setUsesGPS(boolean v){
        _locationUsesRealGPS = v;
        int map = getMapMode();
        if (map == 1 || map == 2)
            changeLocationMode(v);
    }

    private void changeLocationMode(boolean v){
        _locationUsesRealGPS = v;

        if (_dac != null){
            ILocationModifier lm = (_locationUsesRealGPS)?
                    (correction && heading != null) ?
                            new CorrectedAltitudeFixedLM(elevData,positionMark.getPosition().asGeodetic2D()) : new AltitudeFixerLM(elevData) :
             new KarlsruheVirtualWalkLM(elevData);

            _dac.setLocationModifier(lm);
        }
    }



    //------------------------------
    //  Normal stuff !
    //------------------------------

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return (LinearLayout) inflater.inflate(R.layout.globo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initActivity(savedInstanceState);
    }

    @Override
    public void onDestroyView (){
        super.onDestroyView();
    }

    public void setTitle(CharSequence title){
        /*mTitle = title;
        this.getActivity().getActionBar().setTitle(mTitle);*/
    }

    public void onPostCreate() {
        //mDrawerToggle.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

       /* if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_salir:
                salir();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final String[] CAMERA_PERMS={
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int CAMERA_REQUEST=INITIAL_REQUEST+1;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode) {
            case CAMERA_REQUEST:
                if (canAccessCamera() && canAccessLocation()) {
                    this.mapMode = 3;
                    setAR();
                }
                else {
                    Toast.makeText(getContext(),R.string.camera_req_failure,Toast.LENGTH_SHORT).show();
                }
                break;

            case LOCATION_REQUEST:
                if (!canAccessLocation()) {
                    Toast.makeText(getContext(),R.string.location_req_failure,Toast.LENGTH_SHORT).show();
                    mapMode = 0; //Since it could only come from Map mode.
                }
                else {
                    if (mapMode == 1)
                        setMonoVR(1);
                    else if (mapMode == 2)
                        setStereoVR(2);

                }
                break;
        }
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessCamera() {
        return(hasPermission(Manifest.permission.CAMERA));
    }

    private void askForLocationPermissions(){
        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
    }

    private void askForCameraPermissions(){
        requestPermissions(CAMERA_PERMS, CAMERA_REQUEST);
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= 23)
            return(PackageManager.PERMISSION_GRANTED==getActivity().checkSelfPermission(perm));
        else
            return true;
    }


}
