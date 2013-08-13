package org.glob3.mobile.generated; 
/**
 * Class to control the camera.
 */
public class Camera
{
  public Camera(Camera that)
  {
     _width = that._width;
     _height = that._height;
     _planet = that._planet;
     _position = new MutableVector3D(that._position);
     _center = new MutableVector3D(that._center);
     _up = new MutableVector3D(that._up);
     _dirtyFlags = new CameraDirtyFlags(that._dirtyFlags);
     _frustumData = new FrustumData(that._frustumData);
     _projectionMatrix = new MutableMatrix44D(that._projectionMatrix);
     _modelMatrix = new MutableMatrix44D(that._modelMatrix);
     _modelViewMatrix = new MutableMatrix44D(that._modelViewMatrix);
     _cartesianCenterOfView = new MutableVector3D(that._cartesianCenterOfView);
     _geodeticCenterOfView = (that._geodeticCenterOfView == null) ? null : new Geodetic3D(that._geodeticCenterOfView);
     _frustum = (that._frustum == null) ? null : new Frustum(that._frustum);
     _frustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
     _halfFrustum = (that._halfFrustum == null) ? null : new Frustum(that._halfFrustum);
     _halfFrustumInModelCoordinates = (that._halfFrustumInModelCoordinates == null) ? null : new Frustum(that._halfFrustumInModelCoordinates);
     _camEffectTarget = new CameraEffectTarget();
     _geodeticPosition = (that._geodeticPosition == null) ? null: new Geodetic3D(that._geodeticPosition);
     _angle2Horizon = that._angle2Horizon;
     _normalizedPosition = new MutableVector3D(that._normalizedPosition);
  }

  public Camera(int width, int height)
  {
     _width = 0;
     _height = 0;
     _planet = null;
     _position = new MutableVector3D(0, 0, 0);
     _center = new MutableVector3D(0, 0, 0);
     _up = new MutableVector3D(0, 0, 1);
     _dirtyFlags = new CameraDirtyFlags();
     _frustumData = new FrustumData();
     _projectionMatrix = new MutableMatrix44D();
     _modelMatrix = new MutableMatrix44D();
     _modelViewMatrix = new MutableMatrix44D();
     _cartesianCenterOfView = new MutableVector3D(0,0,0);
     _geodeticCenterOfView = null;
     _frustum = null;
     _frustumInModelCoordinates = null;
     _halfFrustumInModelCoordinates = null;
     _halfFrustum = null;
     _camEffectTarget = new CameraEffectTarget();
     _geodeticPosition = null;
     _angle2Horizon = -99;
     _normalizedPosition = new MutableVector3D(0, 0, 0);
    resizeViewport(width, height);
    _dirtyFlags.setAll(true);
  }

  public void dispose()
  {
    if (_camEffectTarget != null)
       _camEffectTarget.dispose();
    if (_frustum != null)
       _frustum.dispose();
    if (_frustumInModelCoordinates != null)
       _frustumInModelCoordinates.dispose();
    if (_halfFrustum != null)
       _halfFrustum.dispose();
    if (_halfFrustumInModelCoordinates != null)
       _halfFrustumInModelCoordinates.dispose();
    if (_geodeticCenterOfView != null)
       _geodeticCenterOfView.dispose();
    if (_geodeticPosition != null)
       _geodeticPosition.dispose();
    JAVA_POST_DISPOSE
  }

  public final void copyFrom(Camera that)
  {
    //TODO: IMPROVE PERFORMANCE
    _width = that._width;
    _height = that._height;
  
    _planet = that._planet;
  
    _position = new MutableVector3D(that._position);
    _center = new MutableVector3D(that._center);
    _up = new MutableVector3D(that._up);
    _normalizedPosition = new MutableVector3D(that._normalizedPosition);
  
    _dirtyFlags.copyFrom(that._dirtyFlags);
  
    _frustumData = new FrustumData(that._frustumData);
  
  //  _projectionMatrix = MutableMatrix44D(that._projectionMatrix);
  //  _modelMatrix      = MutableMatrix44D(that._modelMatrix);
  //  _modelViewMatrix  = MutableMatrix44D(that._modelViewMatrix);
  
    _projectionMatrix.copyValue(that._projectionMatrix);
    _modelMatrix.copyValue(that._modelMatrix);
    _modelViewMatrix.copyValue(that._modelViewMatrix);
  
    _cartesianCenterOfView = new MutableVector3D(that._cartesianCenterOfView);
  
    if (_geodeticCenterOfView != null)
       _geodeticCenterOfView.dispose();
    _geodeticCenterOfView = (that._geodeticCenterOfView == null) ? null : new Geodetic3D(that._geodeticCenterOfView);
  
    if (_frustum != null)
       _frustum.dispose();
    _frustum = (that._frustum == null) ? null : new Frustum(that._frustum);
  
    if (_frustumInModelCoordinates != null)
       _frustumInModelCoordinates.dispose();
    _frustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
  
    if (_halfFrustum != null)
       _halfFrustum.dispose();
    _halfFrustum = (that._frustum == null) ? null : new Frustum(that._frustum);
  
    if (_halfFrustumInModelCoordinates != null)
       _halfFrustumInModelCoordinates.dispose();
    _halfFrustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
  
    if (_geodeticPosition != null)
       _geodeticPosition.dispose();
    _geodeticPosition = ((that._geodeticPosition == null) ? null : new Geodetic3D(that._geodeticPosition));
    _angle2Horizon = that._angle2Horizon;
  }

  public final void copyFromForcingMatrixCreation(Camera c)
  {
    c.forceMatrixCreation();
    copyFrom(c);
  }


  //void Camera::resetPosition() {
  //  _position = MutableVector3D(0, 0, 0);
  //  _center = MutableVector3D(0, 0, 0);
  //  _up = MutableVector3D(0, 0, 1);
  //
  //  _dirtyFlags.setAll(true);
  //
  //  _frustumData = FrustumData();
  //  _projectionMatrix = MutableMatrix44D();
  //  _modelMatrix = MutableMatrix44D();
  //  _modelViewMatrix = MutableMatrix44D();
  //  _cartesianCenterOfView = MutableVector3D();
  //
  //  delete _geodeticCenterOfView;
  //  _geodeticCenterOfView = NULL;
  //
  //  delete _frustum;
  //  _frustum = NULL;
  //
  //  delete _frustumInModelCoordinates;
  //  _frustumInModelCoordinates = NULL;
  //
  //  delete _halfFrustumInModelCoordinates;
  //  _halfFrustumInModelCoordinates = NULL;
  //
  //  delete _halfFrustum;
  //  _halfFrustum = NULL;
  //}
  
  public final void resizeViewport(int width, int height)
  {
    _width = width;
    _height = height;
  
    _dirtyFlags._projectionMatrixDirty = true;
  
    _dirtyFlags.setAll(true);
  
    //cleanCachedValues();
  }

//  void render(const G3MRenderContext* rc,
//              const GLGlobalState& parentState) const;


  //void Camera::render(const G3MRenderContext* rc,
  //                    const GLGlobalState& parentState) const {
  //  //TODO: NO LONGER NEEDED!!!
  //}
  
  public final Vector3D pixel2Ray(Vector2I pixel)
  {
    final int px = pixel._x;
    final int py = _height - pixel._y;
    final Vector3D pixel3D = new Vector3D(px, py, 0);
  
    final Vector3D obj = getModelViewMatrix().unproject(pixel3D, 0, 0, _width, _height);
    if (obj.isNan())
    {
      return obj;
    }
  
    return obj.sub(_position.asVector3D());
  }

  public final Vector3D pixel2PlanetPoint(Vector2I pixel)
  {
    return _planet.closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
  }

//  const Vector2I point2Pixel(const Vector3D& point) const;
//  const Vector2I point2Pixel(const Vector3F& point) const;
  public final Vector2F point2Pixel(Vector3D point)
  {
    final Vector2D p = getModelViewMatrix().project(point, 0, 0, _width, _height);
  
    return new Vector2F((float) p._x, (float)(_height - p._y));
  }
  public final Vector2F point2Pixel(Vector3F point)
  {
    final Vector2F p = getModelViewMatrix().project(point, 0, 0, _width, _height);
  
    return new Vector2F(p._x, (_height - p._y));
  }

  public final int getWidth()
  {
     return _width;
  }
  public final int getHeight()
  {
     return _height;
  }

  public final float getViewPortRatio()
  {
    return (float) _width / _height;
  }

  public final EffectTarget getEffectTarget()
  {
    return _camEffectTarget;
  }

  public final Vector3D getCartesianPosition()
  {
     return _position.asVector3D();
  }
  public final Vector3D getNormalizedPosition()
  {
     return _normalizedPosition.asVector3D();
  }
  public final Vector3D getCenter()
  {
     return _center.asVector3D();
  }
  public final Vector3D getUp()
  {
     return _up.asVector3D();
  }
  public final Geodetic3D getGeodeticCenterOfView()
  {
     return _getGeodeticCenterOfView();
  }
  public final Vector3D getXYZCenterOfView()
  {
     return _getCartesianCenterOfView().asVector3D();
  }
  public final Vector3D getViewDirection()
  {
     return _center.sub(_position).asVector3D();
  }


  //Dragging camera
  public final void dragCamera(Vector3D p0, Vector3D p1)
  {
    // compute the rotation axe
    final Vector3D rotationAxis = p0.cross(p1);
  
    // compute the angle
    //const Angle rotationDelta = Angle::fromRadians( - acos(p0.normalized().dot(p1.normalized())) );
    final Angle rotationDelta = Angle.fromRadians(-IMathUtils.instance().asin(rotationAxis.length()/p0.length()/p1.length()));
  
    if (rotationDelta.isNan())
    {
      return;
    }
  
    rotateWithAxis(rotationAxis, rotationDelta);
  }
  public final void rotateWithAxis(Vector3D axis, Angle delta)
  {
    applyTransform(MutableMatrix44D.createRotationMatrix(delta, axis));
  }
  public final void moveForward(double d)
  {
    final Vector3D view = _center.sub(_position).normalized().asVector3D();
    applyTransform(MutableMatrix44D.createTranslationMatrix(view.times(d)));
  }

  //Pivot
  public final void pivotOnCenter(Angle a)
  {
    final Vector3D rotationAxis = _position.sub(_center).asVector3D();
    rotateWithAxis(rotationAxis, a);
  }

  //Rotate
  public final void rotateWithAxisAndPoint(Vector3D axis, Vector3D point, Angle delta)
  {
    final MutableMatrix44D m = MutableMatrix44D.createGeneralRotationMatrix(delta, axis, point);
    applyTransform(m);
  }

  public final void print()
  {
    getModelMatrix().print("Model Matrix", ILogger.instance());
    getProjectionMatrix().print("Projection Matrix", ILogger.instance());
    getModelViewMatrix().print("ModelView Matrix", ILogger.instance());
    ILogger.instance().logInfo("Width: %d, Height %d\n", _width, _height);
  }

  public final Frustum getFrustumInModelCoordinates()
  {
    //    return getFrustumMC();
    if (_dirtyFlags._frustumMCDirty)
    {
      _dirtyFlags._frustumMCDirty = false;
      if (_frustumInModelCoordinates != null)
         _frustumInModelCoordinates.dispose();
      _frustumInModelCoordinates = getFrustum().transformedBy_P(getModelMatrix());
    }
    return _frustumInModelCoordinates;
  }

  public final Frustum getHalfFrustuminModelCoordinates()
  {
    return getHalfFrustumMC();
  }

  //  void setPosition(const Geodetic3D& position);

  public final Vector3D getHorizontalVector()
  {
    //int todo_remove_get_in_matrix;
    final MutableMatrix44D M = getModelMatrix();
    return new Vector3D(M.get0(), M.get4(), M.get8());
  }

  public final Angle compute3DAngularDistance(Vector2I pixel0, Vector2I pixel1)
  {
    final Vector3D point0 = pixel2PlanetPoint(pixel0);
    if (point0.isNan())
    {
      return Angle.nan();
    }
  
    final Vector3D point1 = pixel2PlanetPoint(pixel1);
    if (point1.isNan())
    {
      return Angle.nan();
    }
  
    return point0.angleBetween(point1);
  }


  ///#include "GPUProgramState.hpp"
  
  public final void initialize(G3MContext context)
  {
    _planet = context.getPlanet();
    setCartesianPosition(new MutableVector3D(_planet.getRadii().maxAxis() * 5, 0, 0));
    _dirtyFlags.setAll(true);
  }

  //  void resetPosition();

  public final void setCartesianPosition(MutableVector3D v)
  {
    if (!v.equalTo(_position))
    {
      _position = new MutableVector3D(v);
      if (_geodeticPosition != null)
         _geodeticPosition.dispose();
      _geodeticPosition = null;
      _dirtyFlags.setAll(true);
      final double distanceToPlanetCenter = _position.length();
      final double planetRadius = distanceToPlanetCenter - getGeodeticPosition()._height;
      _angle2Horizon = Math.acos(planetRadius/distanceToPlanetCenter);
      _normalizedPosition = _position.normalized();
    }
  }

  public final void setCartesianPosition(Vector3D v)
  {
    setCartesianPosition(v.asMutableVector3D());
  }

  public final Angle getHeading()
  {
    final Vector3D normal = _planet.geodeticSurfaceNormal(_position);
    return getHeading(normal);
  }
  public final void setHeading(Angle angle)
  {
    final Vector3D normal = _planet.geodeticSurfaceNormal(_position);
    final Angle currentHeading = getHeading(normal);
    final Angle delta = currentHeading.sub(angle);
    rotateWithAxisAndPoint(normal, _position.asVector3D(), delta);
    //printf ("previous heading=%f   current heading=%f\n", currentHeading._degrees, getHeading()._degrees);
  }
  public final Angle getPitch()
  {
    final Vector3D normal = _planet.geodeticSurfaceNormal(_position);
    final Angle angle = _up.asVector3D().angleBetween(normal);
    return Angle.fromDegrees(90).sub(angle);
  }
  public final void setPitch(Angle angle)
  {
    final Angle currentPitch = getPitch();
    final Vector3D u = getHorizontalVector();
    rotateWithAxisAndPoint(u, _position.asVector3D(), angle.sub(currentPitch));
    //printf ("previous pitch=%f   current pitch=%f\n", currentPitch._degrees, getPitch()._degrees);
  }

  public final Geodetic3D getGeodeticPosition()
  {
    if (_geodeticPosition == null)
    {
      _geodeticPosition = new Geodetic3D(_planet.toGeodetic3D(getCartesianPosition()));
    }
    return _geodeticPosition;
  }

  public final void setGeodeticPosition(Geodetic3D g3d)
  {
    _setGeodeticPosition(_planet.toCartesian(g3d));
  }

  public final void setGeodeticPosition(Angle latitude, Angle longitude, double height)
  {
    _setGeodeticPosition(_planet.toCartesian(latitude, longitude, height));
  }

  public final void setGeodeticPosition(Geodetic2D g2d, double height)
  {
    _setGeodeticPosition(_planet.toCartesian(g2d._latitude, g2d._longitude, height));
  }

  /**
   This method put the camera pointing to given center, at the given distance, using the given angles.

   The situation is like in the figure of this url:
   http: //en.wikipedia.org/wiki/Azimuth

   At the end, camera will be in the 'Star' point, looking at the 'Observer' point.
   */
  public final void setPointOfView(Geodetic3D center, double distance, Angle azimuth, Angle altitude)
  {
    // TODO_deal_with_cases_when_center_in_poles
    final Vector3D cartesianCenter = _planet.toCartesian(center);
    final Vector3D normal = _planet.geodeticSurfaceNormal(center);
    final Vector3D north2D = Vector3D.upZ().projectionInPlane(normal);
    final Vector3D orientedVector = north2D.rotateAroundAxis(normal, azimuth.times(-1));
    final Vector3D axis = orientedVector.cross(normal);
    final Vector3D finalVector = orientedVector.rotateAroundAxis(axis, altitude);
    final Vector3D position = cartesianCenter.add(finalVector.normalized().times(distance));
    final Vector3D finalUp = finalVector.rotateAroundAxis(axis, Angle.fromDegrees(90.0f));
    setCartesianPosition(position.asMutableVector3D());
    setCenter(cartesianCenter.asMutableVector3D());
    setUp(finalUp.asMutableVector3D());
  //  _dirtyFlags.setAll(true);
  }

  public final void forceMatrixCreation()
  {
    //MutableMatrix44D projectionMatrix = MutableMatrix44D::createProjectionMatrix(_frustumData);
    //getFrustumData();
    getProjectionMatrix44D();
    getModelMatrix44D();
    getModelViewMatrix().asMatrix44D();
  }



//  void addProjectionAndModelGLFeatures(GLState& glState) const{
//    glState.clearGLFeatureGroup(CAMERA_GROUP);
//    ProjectionGLFeature* p = new ProjectionGLFeature(getProjectionMatrix().asMatrix44D());
//    glState.addGLFeature(p, false);
//    ModelGLFeature* m = new ModelGLFeature(getModelMatrix44D());
//    glState.addGLFeature(m, false);
//  }

  public final Matrix44D getModelMatrix44D()
  {
    return getModelMatrix().asMatrix44D();
  }

  public final Matrix44D getProjectionMatrix44D()
  {
    return getProjectionMatrix().asMatrix44D();
  }

  public final double getAngle2HorizonInRadians()
  {
     return _angle2Horizon;
  }

  public final double getProjectedSphereArea(Sphere sphere)
  {
    // this implementation is not right exact, but it's faster.
    final double z = sphere._center.distanceTo(getCartesianPosition());
    final double rWorld = sphere._radius * _frustumData._znear / z;
    final double rScreen = rWorld * _height / (_frustumData._top - _frustumData._bottom);
    return DefineConstants.PI * rScreen * rScreen;
  }

  private Angle getHeading(Vector3D normal)
  {
    final Vector3D north2D = Vector3D.upZ().projectionInPlane(normal);
    final Vector3D up2D = _up.asVector3D().projectionInPlane(normal);
    return up2D.signedAngleBetween(north2D, normal);
  }

  //IF A NEW ATTRIBUTE IS ADDED CHECK CONSTRUCTORS AND RESET() !!!!
  private int _width;
  private int _height;

  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE

  private MutableVector3D _position = new MutableVector3D(); // position
  private MutableVector3D _center = new MutableVector3D(); // point where camera is looking at
  private MutableVector3D _up = new MutableVector3D(); // vertical vector

  private Geodetic3D _geodeticPosition; //Must be updated when changing position

  // this value is only used in the method Sector::isBackOriented
  // it's stored in double instead of Angle class to optimize performance in android
  // Must be updated when changing position
  private double _angle2Horizon;
  private MutableVector3D _normalizedPosition = new MutableVector3D();


  private CameraDirtyFlags _dirtyFlags = new CameraDirtyFlags();
  private FrustumData _frustumData = new FrustumData();
  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();
  private MutableMatrix44D _modelMatrix = new MutableMatrix44D();
  private MutableMatrix44D _modelViewMatrix = new MutableMatrix44D();
  private MutableVector3D _cartesianCenterOfView = new MutableVector3D();
  private Geodetic3D _geodeticCenterOfView;
  private Frustum _frustum;
  private Frustum _frustumInModelCoordinates;
  private Frustum _halfFrustum; // ONLY FOR DEBUG
  private Frustum _halfFrustumInModelCoordinates; // ONLY FOR DEBUG

  //The Camera Effect Target
  private static class CameraEffectTarget implements EffectTarget
  {
    public void dispose()
    {
      JAVA_POST_DISPOSE
    }
  }

  private CameraEffectTarget _camEffectTarget;


  //const Vector2I Camera::point2Pixel(const Vector3D& point) const {
  //  const Vector2D p = getModelViewMatrix().project(point,
  //                                                  0, 0, _width, _height);
  //
  ////  const IMathUtils* mu = IMathUtils::instance();
  ////
  ////  return Vector2I(mu->round( (float) p._x ),
  ////                  mu->round( (float) ((double) _height - p._y) ) );
  ////
  //  return Vector2I((int) p._x,
  //                  (int) (_height - p._y) );
  //}
  
  //const Vector2I Camera::point2Pixel(const Vector3F& point) const {
  //  const Vector2F p = getModelViewMatrix().project(point,
  //                                                  0, 0, _width, _height);
  //
  ////  const IMathUtils* mu = IMathUtils::instance();
  ////
  ////  return Vector2I(mu->round( p._x ),
  ////                  mu->round( (float) _height - p._y ) );
  //  return Vector2I((int) p._x ,
  //                  (int) (_height - p._y ) );
  //}
  
  private void applyTransform(MutableMatrix44D M)
  {
    setCartesianPosition(_position.transformedBy(M, 1.0));
    setCenter(_center.transformedBy(M, 1.0));
  
    setUp(_up.transformedBy(M, 0.0));
  
    //_dirtyFlags.setAll(true);
  }


  //void Camera::setPosition(const Geodetic3D& g3d) {
  //  setCartesianPosition( _planet->toCartesian(g3d).asMutableVector3D() );
  //}
  
  private Vector3D centerOfViewOnPlanet()
  {
    final Vector3D ray = _center.sub(_position).asVector3D();
    return _planet.closestIntersection(_position.asVector3D(), ray);
  }

  private void setCenter(MutableVector3D v)
  {
    if (!v.equalTo(_center))
    {
      _center = new MutableVector3D(v);
      _dirtyFlags.setAll(true);
    }
  }

  private void setUp(MutableVector3D v)
  {
    if (!v.equalTo(_up))
    {
      _up = new MutableVector3D(v);
      _dirtyFlags.setAll(true);
    }
  }

  // data to compute frustum
  private FrustumData getFrustumData()
  {
    if (_dirtyFlags._frustumDataDirty)
    {
      _dirtyFlags._frustumDataDirty = false;
      _frustumData = calculateFrustumData();
    }
    return _frustumData;
  }

  // intersection of view direction with globe in(x,y,z)
  private MutableVector3D _getCartesianCenterOfView()
  {
    if (_dirtyFlags._cartesianCenterOfViewDirty)
    {
      _dirtyFlags._cartesianCenterOfViewDirty = false;
      _cartesianCenterOfView = centerOfViewOnPlanet().asMutableVector3D();
    }
    return _cartesianCenterOfView;
  }

  // intersection of view direction with globe in geodetic
  private Geodetic3D _getGeodeticCenterOfView()
  {
    if (_dirtyFlags._geodeticCenterOfViewDirty)
    {
      _dirtyFlags._geodeticCenterOfViewDirty = false;
      if (_geodeticCenterOfView != null)
         _geodeticCenterOfView.dispose();
      _geodeticCenterOfView = new Geodetic3D(_planet.toGeodetic3D(getXYZCenterOfView()));
    }
    return _geodeticCenterOfView;
  }

  // camera frustum
  private Frustum getFrustum()
  {
    if (_dirtyFlags._frustumDirty)
    {
      _dirtyFlags._frustumDirty = false;
      if (_frustum != null)
         _frustum.dispose();
      _frustum = new Frustum(getFrustumData());
    }
    return _frustum;
  }

  private Frustum getHalfFrustum()
  {
    // __temporal_test_for_clipping;
    if (_dirtyFlags._halfFrustumDirty)
    {
      _dirtyFlags._halfFrustumDirty = false;
      if (_halfFrustum != null)
         _halfFrustum.dispose();
      FrustumData data = getFrustumData();
      _halfFrustum = new Frustum(data._left/4, data._right/4, data._bottom/4, data._top/4, data._znear, data._zfar);
    }
    return _halfFrustum;
  }

  private Frustum getHalfFrustumMC()
  {
    if (_dirtyFlags._halfFrustumMCDirty)
    {
      _dirtyFlags._halfFrustumMCDirty = false;
      if (_halfFrustumInModelCoordinates != null)
         _halfFrustumInModelCoordinates.dispose();
      _halfFrustumInModelCoordinates = getHalfFrustum().transformedBy_P(getModelMatrix());
    }
    return _halfFrustumInModelCoordinates;
  }

  private FrustumData calculateFrustumData()
  {
    //    // compute znear value
    //    const double maxRadius = _planet->getRadii().maxAxis();
    //    const double distanceToPlanetCenter = _position.length();
    //    const double distanceToSurface = distanceToPlanetCenter - maxRadius;
    //
    //    double znear;
    //    if (distanceToSurface > maxRadius/5) {
    //      znear = maxRadius / 10;
    //    }
    //    else if (distanceToSurface > maxRadius/500) {
    //      znear = maxRadius / 1e4;
    //    }
    //    else if (distanceToSurface > maxRadius/2000) {
    //      znear = maxRadius / 1e5;
    //    }
    //    else {
    //      znear = maxRadius / 1e6 * 3;
    //    }
    //
    //    // compute zfar value
    //    double zfar = 10000 * znear;
    //    if (zfar > distanceToPlanetCenter) {
    //      zfar = distanceToPlanetCenter;
    //    }
    //
    //    // compute rest of frustum numbers
    //    const double ratioScreen = (double) _height / _width;
    //    const double right = 0.3 / ratioScreen * znear;
    //    const double left = -right;
    //    const double top = 0.3 * znear;
    //    const double bottom = -top;
    //
    //    return FrustumData(left, right,
    //                       bottom, top,
    //                       znear, zfar);
  
    final double height = getGeodeticPosition()._height;
    double zNear = height * 0.1;
  
    /*
    // compute zfar value using distance to horizon (Agustin version)
    const double distanceToPlanetCenter = _position.length();
    const double planetRadius = distanceToPlanetCenter - height;
    const double distanceToHorizon = sqrt(distanceToPlanetCenter*distanceToPlanetCenter-planetRadius*planetRadius);
    const double zfar = distanceToHorizon * 2.0;
    printf ("ratio z = %f\n", zfar/znear);
     */
  
    double zFar = 10000 * zNear;
    final double distance2ToPlanetCenter = _position.squaredLength();
    if ((zFar * zFar) > distance2ToPlanetCenter)
    {
      zFar = IMathUtils.instance().sqrt(distance2ToPlanetCenter);
    }
  
    final double goalRatio = 1000;
    final double ratio = zFar / zNear;
    if (ratio < goalRatio)
    {
      zNear = zFar / goalRatio;
      //ratio = zFar / zNear;
    }
  
  //  int __TODO_remove_debug_code;
  //  printf(">>> height=%f zNear=%f zFar=%f ratio=%f\n",
  //         height,
  //         zNear,
  //         zFar,
  //         ratio);
  
    // compute rest of frustum numbers
    final double _tanHalfFieldOfView = 0.3; // aprox tan(34 degrees / 2)
  
    final double ratioScreen = (double) _height / _width;
    final double right = _tanHalfFieldOfView / ratioScreen * zNear;
    final double left = -right;
    final double top = _tanHalfFieldOfView * zNear;
    final double bottom = -top;
  
  
    return new FrustumData(left, right, bottom, top, zNear, zFar);
  }

  private void _setGeodeticPosition(Vector3D pos)
  {
    final Angle heading = getHeading();
    final Angle pitch = getPitch();
  
    setPitch(Angle.zero());
  
    final MutableVector3D finalPos = pos.asMutableVector3D();
    final Vector3D axis = _position.cross(finalPos).asVector3D();
    if (axis.length()<1e-3)
    {
      return;
    }
    final Angle angle = _position.angleBetween(finalPos);
    rotateWithAxis(axis, angle);
  
    final double dist = _position.length() - pos.length();
    moveForward(dist);
  
    setHeading(heading);
    setPitch(pitch);
  }

  // opengl projection matrix
  private MutableMatrix44D getProjectionMatrix()
  {
    if (_dirtyFlags._projectionMatrixDirty)
    {
      _dirtyFlags._projectionMatrixDirty = false;
      _projectionMatrix = MutableMatrix44D.createProjectionMatrix(getFrustumData());
    }
    return _projectionMatrix;
  }

  // Model matrix, computed in CPU in double precision
  private MutableMatrix44D getModelMatrix()
  {
    if (_dirtyFlags._modelMatrixDirty)
    {
      _dirtyFlags._modelMatrixDirty = false;
      _modelMatrix = MutableMatrix44D.createModelMatrix(_position, _center, _up);
    }
    return _modelMatrix;
  }

  // multiplication of model * projection
  private MutableMatrix44D getModelViewMatrix()
  {
    if (_dirtyFlags._modelViewMatrixDirty)
    {
      _dirtyFlags._modelViewMatrixDirty = false;
      _modelViewMatrix = getProjectionMatrix().multiply(getModelMatrix());
    }
    return _modelViewMatrix;
  }

}