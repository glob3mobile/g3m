package org.glob3.mobile.generated; 
//
//  RasterPolygonShape.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 28/11/13.
//
//

//
//  RasterPolygonShape.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 28/11/13.
//
//




//class OrientedBox;


public class RasterPolygonShape extends Shape
{

  private OrientedBox _boundingVolume;

  private float _borderWidth;

  private Color _borderColor;
  private Color _surfaceColor;

  private OrientedBox computeOrientedBox(Planet planet, Camera camera)
  {
    if (_cartesianStartPos == null)
      computeOrientationParams(planet);
    double distanceToCamera = camera.getCartesianPosition().distanceTo(_cartesianStartPos);
    FrustumData frustum = camera.getFrustumData();
    final int pixelWidth = 10;
    double scale = 2 * pixelWidth * distanceToCamera * frustum._top / camera.getHeight() / frustum._znear;
    double incZ = scale - (_maxZ - _minZ);
    if (incZ < 0)
       incZ = 0;
    final Vector3D upper = new Vector3D(_maxX, _maxY, _maxZ+incZ);
    final Vector3D lower = new Vector3D(_minX, _minY, _minZ-incZ);
    return new OrientedBox(lower, upper, getTransformMatrix(planet));
  }

  private Vector3D _cartesianStartPos;
  private java.util.ArrayList<Geodetic2D> _coordinates;

  private void computeOrientationParams(Planet planet)
  {
    _minX = _minY = _minZ = 0;
    _maxX = _maxY = _maxZ = 0;
    Geodetic2D geodeticStartPos = _coordinates.get(0);
    _cartesianStartPos = new Vector3D(planet.toCartesian(geodeticStartPos));
    final Vector3D normal = planet.geodeticSurfaceNormal(_cartesianStartPos);
    Plane Z0 = Plane.fromPointAndNormal(_cartesianStartPos, normal);
    final Vector3D north2D = planet.getNorth().projectionInPlane(normal);
    Plane Y0 = Plane.fromPointAndNormal(_cartesianStartPos, north2D);
    final Vector3D east2D = north2D.cross(normal);
    Plane X0 = Plane.fromPointAndNormal(_cartesianStartPos, east2D);
    for (int n = 1; n<_coordinates.size(); n++)
    {
      Geodetic2D geodeticVertex = _coordinates.get(n);
      Vector3D vertex = planet.toCartesian(geodeticVertex);
      double x = X0.signedDistance(vertex);
      if (x < _minX)
         _minX = x;
      if (x > _maxX)
         _maxX = x;
      double y = Y0.signedDistance(vertex);
      if (y < _minY)
         _minY = y;
      if (y > _maxY)
         _maxY = y;
      double z = Z0.signedDistance(vertex);
      if (z < _minZ)
         _minZ = z;
      if (z > _maxZ)
         _maxZ = z;
    }
  }

  private double _minX;
  private double _minY;
  private double _minZ;
  private double _maxX;
  private double _maxY;
  private double _maxZ;


  protected final BoundingVolume getBoundingVolume(G3MRenderContext rc)
  {
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
    _boundingVolume = computeOrientedBox(rc.getPlanet(), rc.getCurrentCamera());
    return _boundingVolume;
  }



  public RasterPolygonShape(java.util.ArrayList<Geodetic2D> coordinates, float borderWidth, Color borderColor, Color surfaceColor)
  {
    super(new Geodetic3D(coordinates.get(0), 0), AltitudeMode.RELATIVE_TO_GROUND);
    _coordinates = GEORasterSymbol.copyCoordinates(coordinates);
    _cartesianStartPos = null;
    _boundingVolume = null;
    _borderWidth = borderWidth;
    _borderColor = new Color(borderColor);
    _surfaceColor = new Color(surfaceColor);
  }

  public void dispose()
  {
    if (_borderColor != null)
       _borderColor.dispose();
    if (_surfaceColor != null)
       _surfaceColor.dispose();
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
    if (_cartesianStartPos != null)
      if (_cartesianStartPos != null)
         _cartesianStartPos.dispose();
  
    for (int i = 0; i < _coordinates.size(); i++)
    {
      final Geodetic2D coordinate = _coordinates.get(i);
      if (coordinate != null)
         coordinate.dispose();
    }
    _coordinates = null;
  
  
    super.dispose();
  
  }


  public final void setColor(Color color)
  {
   /* delete _color;
    _color = color;*/
  }

  public final void setWidth(float width)
  {
   /* if (_width != width) {
      _width = width;
    }*/
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Camera camera, Vector3D origin, Vector3D direction)
  {
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
    _boundingVolume = computeOrientedBox(planet, camera);
    return _boundingVolume.intersectionsDistances(origin, direction);
  }

  public final boolean isVisible(G3MRenderContext rc)
  {
    return true;
    //return getBoundingVolume(rc)->touchesFrustum(rc->getCurrentCamera()->getFrustumInModelCoordinates());
  }

  public final void setSelectedDrawMode(boolean mode)
  {
  /*  if (mode) {
      setColor(Color::newFromRGBA(1, 0, 0, 1));
    } else {
      setColor(new Color(*_originalColor));
    }*/
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return false;
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
  }

  public final GEORasterSymbol createRasterSymbolIfNeeded()
  {
    float[] dashLengths = {};
    java.util.ArrayList<Geodetic2D> coordinates = GEORasterSymbol.copyCoordinates(_coordinates);
    GEO2DPolygonData polygonData = new GEO2DPolygonData(coordinates, null);
    GEO2DLineRasterStyle lineStyle = new GEO2DLineRasterStyle(_borderColor, _borderWidth, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, 0, 0);
    GEO2DSurfaceRasterStyle surfaceStyle = new GEO2DSurfaceRasterStyle(_surfaceColor);
    return new GEORasterPolygonSymbol(polygonData, lineStyle, surfaceStyle);
  }

  public final java.util.ArrayList<Geodetic2D> getCopyRasterCoordinates()
  {
    java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D>();
    int size = _coordinates.size();
    for (int n = 0; n<size; n++)
      coordinates.add(new Geodetic2D(*_coordinates.get(n)));
    return coordinates;
  }

  public final boolean isRaster()
  {
    return true;
  }

  public final float getBorderWidth()
  {
    return _borderWidth;
  }

  public final Color getBorderColor()
  {
    return _borderColor;
  }

  public final Color getSurfaceColor()
  {
    return _surfaceColor;
  }

}