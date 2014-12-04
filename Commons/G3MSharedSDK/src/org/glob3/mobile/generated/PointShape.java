package org.glob3.mobile.generated; 
//
//  PointShape.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 22/11/13.
//
//

//
//  PointShape.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 22/11/13.
//
//




//class OrientedBox;


public class PointShape extends AbstractMeshShape
{

  private OrientedBox _boundingVolume;

  private float _width;

  private Color _color;
  private Color _originalColor;

  private OrientedBox computeOrientedBox(Planet planet, Camera camera)
  {
    if (_cartesianPosition == null)
      _cartesianPosition = new Vector3D(planet.toCartesian(getPosition()));
    double distanceToCamera = camera.getCartesianPosition().distanceTo(_cartesianPosition);
    FrustumData frustum = camera.getFrustumData();
    final int pixelWidth = 10;
    double scale = 2 * pixelWidth * distanceToCamera * frustum._top / camera.getViewPortHeight() / frustum._znear;
    final Vector3D upper = new Vector3D(scale, scale, scale);
    final Vector3D lower = upper.times(-1);
    return new OrientedBox(lower, upper, getTransformMatrix(planet));
  }

  private Vector3D _cartesianPosition;

  protected final Mesh createMesh(G3MRenderContext rc)
  {
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
    vertices.add(0.0f, 0.0f, 0.0f);
    Color color = (_color == null) ? null : new Color(_color);
  
    Mesh mesh = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), 1, _width, color);
  
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

  public PointShape(Geodetic3D position, AltitudeMode altitudeMode, float width, Color color)
  {
     super(position, altitudeMode);
     _cartesianPosition = null;
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
    if (_cartesianPosition != null)
      if (_cartesianPosition != null)
         _cartesianPosition.dispose();
  
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

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Camera camera, Vector3D origin, Vector3D direction)
  {
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
    _boundingVolume = computeOrientedBox(planet, camera);
    return _boundingVolume.intersectionsDistances(origin, direction);
  }

}