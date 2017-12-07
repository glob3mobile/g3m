package org.glob3.mobile.generated; 
public class CityGMLRenderer extends DefaultRenderer
{

  private java.util.ArrayList<CityGMLBuilding> _buildings = new java.util.ArrayList<CityGMLBuilding>();

  private ElevationData _elevationData;
  private MeshRenderer _meshRenderer;
  private MarksRenderer _marksRenderer;
  private GEOVectorLayer _vectorLayer;

  private Camera _lastCamera;

  private CityGMLBuildingTouchedListener _touchListener;

  private static class TessellationTask extends GAsyncTask
  {
    private CityGMLRenderer _vc;
    private java.util.ArrayList<CityGMLBuilding> _buildings = new java.util.ArrayList<CityGMLBuilding>();
    private java.util.ArrayList<Mesh> _buildingMeshes = new java.util.ArrayList<Mesh>();

    private CityGMLRendererListener _listener;
    private final boolean _autoDelete;

    private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();
    private java.util.ArrayList<GEORasterSymbol> _geoSymbol = new java.util.ArrayList<GEORasterSymbol>();
    private Mesh _mesh;

    private final boolean _fixOnGround;



    private GEORasterSymbol createBuildingFootprints(CityGMLBuilding b)
    {


      float[] dashLengths = {};
      int dashCount = 0;

      GEO2DLineRasterStyle style = new GEO2DLineRasterStyle(Color.red(), 3, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0);

      for (int j = 0; j < b._surfaces.size(); j++)
      {
        CityGMLBuildingSurface s = b._surfaces.get(j);
        if (s.getType() == CityGMLBuildingSurfaceType.GROUND)
        {

          java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D>();
          for (int k = 0; k < s._geodeticCoordinates.size(); k++)
          {
            final Geodetic2D g2D = s._geodeticCoordinates.get(k).asGeodetic2D();
            coordinates.add(new Geodetic2D(g2D));
          }

          return new GEOLineRasterSymbol(new GEO2DCoordinatesData(coordinates), style);
        }

      }
      return null;
    }

    public TessellationTask(CityGMLRenderer vc, java.util.ArrayList<CityGMLBuilding> buildings, boolean fixOnGround, CityGMLRendererListener listener, boolean autoDelete)
    {
       _vc = vc;
       _buildings = buildings;
       _listener = listener;
       _autoDelete = autoDelete;
       _mesh = null;
       _fixOnGround = fixOnGround;
    }



    public void runInBackground(G3MContext context)
    {
      //Adding marks
      if (_vc._marksRenderer != null)
      {
        for (int i = 0; i < _buildings.size(); i++)
        {
          _marks.add(CityGMLBuildingTessellator.createMark(_buildings.get(i), false));
        }
      }

      if (_vc._vectorLayer!= null)
      {
        for (int i = 0; i < _buildings.size(); i++)
        {
          GEORasterSymbol s = createBuildingFootprints(_buildings.get(i));
          if (s != null)
          {
            _geoSymbol.add(s);
          }
        }
      }

      //Checking walls visibility
      //      int n = CityGMLBuilding::checkWallsVisibility(_buildings);
      //      ILogger::instance()->logInfo("Removed %d invisible walls from the model.", n);


      //Creating mesh model
            final boolean checkSurfacesVisibility = true;
            _mesh = CityGMLBuildingTessellator.createMesh(_buildings, _vc._context.getPlanet(), _fixOnGround, checkSurfacesVisibility, null, _vc._elevationData);
      _buildingMeshes.add(_mesh);

//      for (size_t i = 0; i < _buildings.size(); i++) {
//        std::vector<CityGMLBuilding*> bs;
//        bs.push_back(_buildings[i]);
//        _mesh = CityGMLBuildingTessellator::createMesh(bs,
//                                                       *_vc->_context->getPlanet(),
//                                                       _fixOnGround, checkSurfacesVisibility, NULL,
//                                                       _vc->_elevationData);
//        _buildingMeshes.push_back(_mesh);
//      }

    }

    public void onPostExecute(G3MContext context)
    {

      //Including elements must be done in the rendering thread
      //      _vc->_meshRenderer->addMesh(_mesh);

      for (int i = 0; i < _buildingMeshes.size(); i++)
      {
        _vc._meshRenderer.addMesh(_buildingMeshes.get(i));
      }

      //Uncomment for seeing spheres
      //      for (size_t i = 0; i < _buildings.size(); i++) {
      //      _vc->_meshRenderer->addMesh(CityGMLBuildingTessellator::getSphereOfBuilding(_buildings[i])->createWireframeMesh(Color::red(), 5));
      //      }

      for (int i = 0; i < _marks.size(); i++)
      {
        _vc._marksRenderer.addMark(_marks.get(i));
      }

      for (int i = 0; i < _geoSymbol.size(); i++)
      {
        _vc._vectorLayer.addSymbol(_geoSymbol.get(i));
      }

      _listener.onBuildingsLoaded(_buildings);
      if (_autoDelete)
      {
        if (_listener != null)
           _listener.dispose();
      }
    }
  }


  public CityGMLRenderer(MeshRenderer meshRenderer, MarksRenderer marksRenderer, GEOVectorLayer vectorLayer)
  {
     _elevationData = null;
     _meshRenderer = meshRenderer;
     _marksRenderer = marksRenderer;
     _lastCamera = null;
     _vectorLayer = vectorLayer;
     _touchListener = null;
  }

  public final void setElevationData(ElevationData ed)
  {
    _elevationData = ed;
  }

  public void initialize(G3MContext context)
  {
    super.initialize(context);
    _meshRenderer.initialize(context);
    if (_marksRenderer != null)
    {
      _marksRenderer.initialize(context);
    }
  }

  public final java.util.ArrayList<CityGMLBuilding> getBuildings()
  {
    return _buildings;
  }

  public final void addBuildingsFromURL(URL url, boolean fixBuildingsOnGround, CityGMLRendererListener listener, boolean autoDelete)
  {
  
    CityGMLParser.parseFromURL(url, new CityGMLParsingListener(this, fixBuildingsOnGround, listener, autoDelete), true);
  
  }

  public final void addBuildingDataFromURL(URL url)
  {
    _context.getDownloader().requestBuffer(url, 1000, TimeInterval.forever(), true, new BuildingDataBDL(_buildings, _context), true);
  }

  public final void addBuildings(java.util.ArrayList<CityGMLBuilding> buildings, boolean fixOnGround, CityGMLRendererListener listener, boolean autoDelete)
  {
  
    java.util.ArrayList<CityGMLBuilding> notRepeatedBuildings = new java.util.ArrayList<CityGMLBuilding>();
  
    for (int i = 0; i < buildings.size(); i++)
    {
  
      CityGMLBuilding b = buildings.get(i);
      for (int j = 0; j < _buildings.size(); j++)
      {
        if (_buildings.get(j)._name.equals(b._name))
        {
          if (b != null)
             b.dispose();
          b = null;
          break;
        }
      }
  
      if (b != null)
      {
        notRepeatedBuildings.add(b);
      }
    }
  
    for (int i = 0; i < notRepeatedBuildings.size(); i++)
    {
      _buildings.add(notRepeatedBuildings.get(i));
    }
  
    boolean createCityMeshAndMarks = true;
    if (createCityMeshAndMarks)
    {
      _context.getThreadUtils().invokeAsyncTask(new TessellationTask(this, notRepeatedBuildings, fixOnGround, listener, autoDelete), true);
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public void render(G3MRenderContext rc, GLState glState)
  {
    _lastCamera = rc.getCurrentCamera();
  
    _meshRenderer.render(rc, glState);
    if (_marksRenderer != null)
    {
      _marksRenderer.render(rc, glState);
    }
  
  //  //Rendering Spheres
  //  Color red = Color::red();
  //  for (size_t i = 0; i < _buildings.size(); i++) {
  //    const Sphere* s = CityGMLBuildingTessellator::getSphereOfBuilding(_buildings[i]);
  //    if (s != NULL){
  //      s->render(rc, glState, red);
  //    }
  //  }
  }

  //  virtual bool onTouchEvent(const G3MEventContext* ec,
  //                            const TouchEvent* touchEvent) {
  //    return false;
  //  }

  public final void colorBuildings(CityGMLBuildingColorProvider cp)
  {
    for (int i = 0; i < _buildings.size(); i++)
    {
      CityGMLBuilding b = _buildings.get(i);
      Color c = cp.getColor(b);
      CityGMLBuildingTessellator.changeColorOfBuildingInBoundedMesh(b, c);
    }
  }

  public final java.util.ArrayList<Double> getAllValuesOfProperty(String name)
  {
    java.util.ArrayList<Double> v = new java.util.ArrayList<Double>();
    for (int i = 0; i < _buildings.size(); i++)
    {
      double value = _buildings.get(i).getNumericProperty(name);
      if (!(value != value))
      {
        v.add(value);
      }
    }
    return v;
  }

  public final CityGMLBuilding getBuildingWithName(String name)
  {
    for (int i = 0; i < _buildings.size(); i++)
    {
      if (_buildings.get(i)._name.equals(name))
      {
        return _buildings.get(i);
      }
    }
    return null;
  }

  public final void colorBuildingsWithColorBrewer(String propertyName, String colorScheme, int nClasses)
  {

    java.util.ArrayList<Double> vs = getAllValuesOfProperty(propertyName);

    ColorLegend cl = ColorLegendHelper.createColorBrewLegendWithHomogeneousBreaks(vs, colorScheme, nClasses);

    BuildingDataColorProvider colorProvider = new BuildingDataColorProvider(propertyName, cl);

    colorBuildings(colorProvider);

    if (colorProvider != null)
       colorProvider.dispose();

  }


  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
  
    if (_lastCamera == null || _touchListener == null)
    {
      return false;
    }
  
    if (touchEvent.getType() == TouchEventType.LongPress)
    {
      final Vector2F pixel = touchEvent.getTouch(0).getPos();
      final Vector3D ray = _lastCamera.pixel2Ray(pixel);
      final Vector3D origin = _lastCamera.getCartesianPosition();
  
  //    FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  //    fbb->add(origin);
  //    fbb->add(origin.add(ray.times(10)));
  //    DirectMesh* dm = new DirectMesh(GLPrimitive::lines(),
  //                                    true,
  //                                    fbb->getCenter(),
  //                                    fbb->create(),
  //                                    4.0f,
  //                                    1.0f,
  //                                    new Color(Color::green()));
  //    _meshRenderer->addMesh(dm);
  //
  //    delete fbb;
  //
  //    fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  //    fbb->add(origin);
  //
  //    DirectMesh* dm2 = new DirectMesh(GLPrimitive::lines(),
  //                                     true,
  //                                     fbb->getCenter(),
  //                                     fbb->create(),
  //                                     4.0f,
  //                                     1.0f,
  //                                     new Color(Color::blue()));
  //
  //
  //    delete fbb;
  //    _meshRenderer->addMesh(dm2);
  
      double minDis = 1e20;
      CityGMLBuilding touchedB = null;
  
      for (int i = 0; i < _buildings.size(); i++)
      {
        final Sphere s = CityGMLBuildingTessellator.getSphereOfBuilding(_buildings.get(i));
  
        if (s != null)
        {
          final java.util.ArrayList<Double> dists = s.intersectionsDistances(origin._x, origin._y, origin._z, ray._x, ray._y, ray._z);
          for (int j = 0; j < dists.size(); j++)
          {
            if (dists.get(j) < minDis)
            {
              minDis = dists.get(j);
              touchedB = _buildings.get(i);
            }
          }
  
        }
      }
  
      if (touchedB != null)
      {
        _touchListener.onBuildingTouched(touchedB);
        return false; //return true;
      }
    }
  
    return false;
  }

  public final void setTouchListener(CityGMLBuildingTouchedListener touchListener)
  {
    _touchListener = touchListener;
  }


}