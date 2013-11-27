package org.glob3.mobile.generated; 
//
//  RasterLineShape.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 27/11/13.
//
//

//
//  RasterLineShape.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 27/11/13.
//
//




//class OrientedBox;


public class RasterLineShape extends Shape
{

  private OrientedBox _boundingVolume;

  private float _width;

  private Color _color;
  private Color _originalColor;

  private OrientedBox computeOrientedBox(Planet planet, Camera camera)
  {
    if (_cartesianStartPos == null)
      computeOrientationParams(planet);
    double distanceToCamera = camera.getCartesianPosition().distanceTo(_cartesianStartPos);
    FrustumData frustum = camera.getFrustumData();
    final int pixelWidth = 6;
    double scale = 2 * pixelWidth * distanceToCamera * frustum._top / camera.getHeight() / frustum._znear;
    final Vector3D upper = new Vector3D(scale, scale, 1);
    final Vector3D lower = new Vector3D(-scale, -scale, 0);
    return new OrientedBox(lower, upper, getTransformMatrix(planet));
  }

  private Geodetic2D _geodeticStartPos;
  private Geodetic2D _geodeticEndPos;
  private Vector3D _cartesianStartPos;

  private void computeOrientationParams(Planet planet)
  {
    _cartesianStartPos = new Vector3D(planet.toCartesian(_geodeticStartPos));
    final Vector3D cartesianEndPos = new Vector3D(planet.toCartesian(_geodeticEndPos));
    final Vector3D line = cartesianEndPos.sub(_cartesianStartPos);
    setScale(1, 1, line.length());
    final Vector3D normal = planet.geodeticSurfaceNormal(_cartesianStartPos);
    setPitch(line.angleBetween(normal).times(-1));
    final Vector3D north2D = planet.getNorth().projectionInPlane(normal);
    final Vector3D projectedLine = line.projectionInPlane(normal);
    setHeading(projectedLine.signedAngleBetween(north2D, normal));
  }


  protected final BoundingVolume getBoundingVolume(G3MRenderContext rc)
  {
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
    _boundingVolume = computeOrientedBox(rc.getPlanet(), rc.getCurrentCamera());
    return _boundingVolume;
  }

  public RasterLineShape(Geodetic2D startPosition, Geodetic2D endPosition, float width, Color color)
  {
     _geodeticStartPos = startPosition;
     _geodeticEndPos = endPosition;
     super(new Geodetic3D(startPosition, 0), AltitudeMode.RELATIVE_TO_GROUND);
     _cartesianStartPos = null;
     _boundingVolume = null;
     _width = width;
     _color = new Color(color);
     _originalColor = new Color(color);
  }

  public void dispose()
  {
    if (_color != null)
       _color.dispose();
    if (_originalColor != null)
       _originalColor.dispose();
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
    if (_geodeticStartPos != null)
       _geodeticStartPos.dispose();
    if (_geodeticEndPos != null)
       _geodeticEndPos.dispose();
    if (_cartesianStartPos != null)
      if (_cartesianStartPos != null)
         _cartesianStartPos.dispose();
  
    super.dispose();
  
  }


  public final void setColor(Color color)
  {
    if (_color != null)
       _color.dispose();
    _color = color;
  }

  public final void setWidth(float width)
  {
    if (_width != width)
    {
      _width = width;
    }
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
    if (mode)
    {
      setColor(Color.newFromRGBA(1, 0, 0, 1));
    }
    else
    {
      setColor(new Color(_originalColor));
    }
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
    java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D>();
    coordinates.add(_geodeticStartPos);
    coordinates.add(_geodeticEndPos);
    float[] dashLengths = {};
    GEO2DLineRasterStyle lineStyle = new GEO2DLineRasterStyle(_color, _width, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, 0, 0);
    return new GEORasterLineSymbol(coordinates, lineStyle);
  }

}