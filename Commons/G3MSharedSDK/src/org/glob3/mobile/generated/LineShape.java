package org.glob3.mobile.generated; 
//
//  LineShape.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 26/11/13.
//
//

//
//  LineShape.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 26/11/13.
//
//




//class OrientedBox;


public class LineShape extends AbstractMeshShape
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
    final int pixelWidth = 10;
    double scale = 2 * pixelWidth * distanceToCamera * frustum._top / camera.getHeight() / frustum._znear;
    final Vector3D upper = new Vector3D(scale, scale, 1);
    final Vector3D lower = new Vector3D(-scale, -scale, 0);
    return new OrientedBox(lower, upper, getTransformMatrix(planet));
  }

  private Vector3D _cartesianStartPos;
  private Geodetic3D _geodeticEndPos;

  private void computeOrientationParams(Planet planet)
  {
    _cartesianStartPos = new Vector3D(planet.toCartesian(getPosition()));
    final Vector3D cartesianEndPos = new Vector3D(planet.toCartesian(_geodeticEndPos));
    final Vector3D line = cartesianEndPos.sub(_cartesianStartPos);
    setScale(1, 1, line.length());
    final Vector3D normal = planet.geodeticSurfaceNormal(_cartesianStartPos);
    setPitch(line.angleBetween(normal).times(-1));
    final Vector3D north2D = planet.getNorth().projectionInPlane(normal);
    final Vector3D projectedLine = line.projectionInPlane(normal);
    setHeading(projectedLine.signedAngleBetween(north2D, normal));
  }


  protected final Mesh createMesh(G3MRenderContext rc)
  {
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
    vertices.add(0.0f, 0.0f, 0.0f);
    vertices.add(0.0f, 0.0f, 1.0f);
  
    if (_cartesianStartPos == null)
      computeOrientationParams(rc.getPlanet());
  
    Color color = (_color == null) ? null : new Color(_color);
  
    Mesh mesh = new DirectMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), _width, 1, color);
  
    if (vertices != null)
       vertices.dispose();
    return mesh;
  }
  protected final BoundingVolume getBoundingVolume(G3MRenderContext rc)
  {
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
    _boundingVolume = computeOrientedBox(rc.getPlanet(), rc.getCurrentCamera());
    return _boundingVolume;
  }

  public LineShape(Geodetic3D startPosition, Geodetic3D endPosition, AltitudeMode altitudeMode, float width, Color color)
  {
     super(startPosition, altitudeMode);
     _cartesianStartPos = null;
     _geodeticEndPos = new Geodetic3D(endPosition);
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
    if (_cartesianStartPos != null)
      if (_cartesianStartPos != null)
         _cartesianStartPos.dispose();
    if (_geodeticEndPos != null)
       _geodeticEndPos.dispose();
  
    super.dispose();
  
  }


  public final void setColor(Color color)
  {
    if (_color != null)
       _color.dispose();
    _color = color;
    cleanMesh();
  }

  public final void setWidth(float width)
  {
    if (_width != width)
    {
      _width = width;
      cleanMesh();
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


}