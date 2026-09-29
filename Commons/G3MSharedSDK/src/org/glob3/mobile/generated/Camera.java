package org.glob3.mobile.generated;
public class Camera
{

  public Camera(long timestamp, FrustumPolicy frustumPolicy)
  {
     _frustumPolicy = frustumPolicy;
     _planet = null;
     _position = new MutableVector3D(0, 0, 0);
     _center = new MutableVector3D(0, 0, 0);
     _up = new MutableVector3D(0, 0, 1);
     _dirtyFlags = new CameraDirtyFlags();
     _frustumData = null;
     _projectionMatrix = new MutableMatrix44D();
     _modelMatrix = new MutableMatrix44D();
     _modelViewMatrix = new MutableMatrix44D();
     _cartesianCenterOfView = new MutableVector3D(0,0,0);
     _geodeticCenterOfView = null;
     _frustum = null;
     _frustumInModelCoordinates = null;
     _camEffectTarget = new CameraEffectTarget();
     _geodeticPosition = null;
     _angle2Horizon = -99;
     _normalizedPosition = new MutableVector3D(0, 0, 0);
     _tanHalfVerticalFOV = Double.NaN;
     _tanHalfHorizontalFOV = Double.NaN;
     _timestamp = timestamp;
     _viewPortWidth = -1;
     _viewPortHeight = -1;
     _fixedZNear = Double.NaN;
     _fixedZFar = Double.NaN;
    resizeViewport(0, 0);
    _dirtyFlags.setAllDirty();
  }

  public void dispose()
  {
    if (_camEffectTarget != null)
       _camEffectTarget.dispose();
    if (_frustum != null)
       _frustum.dispose();
    if (_frustumInModelCoordinates != null)
       _frustumInModelCoordinates.dispose();
    if (_geodeticCenterOfView != null)
       _geodeticCenterOfView.dispose();
    if (_geodeticPosition != null)
       _geodeticPosition.dispose();
    if (_frustumPolicy != null)
       _frustumPolicy.dispose();
    if (_frustumData != null)
       _frustumData.dispose();
  }

  public final void copyFrom(Camera that, boolean ignoreTimestamp)
  {
  
    if (ignoreTimestamp || (_timestamp != that._timestamp))
    {
  
      that.forceMatrixCreation();
  
      _timestamp = that._timestamp;
  
      _viewPortWidth = that._viewPortWidth;
      _viewPortHeight = that._viewPortHeight;
  
      _planet = that._planet;
  
      _position.set(that._position);
      _center.set(that._center);
      _up.set(that._up);
      _normalizedPosition.set(that._normalizedPosition);
  
      _dirtyFlags.copyFrom(that._dirtyFlags);
  
      _frustumData = that._frustumData;
  
      _fixedZNear = that._fixedZNear;
      _fixedZFar = that._fixedZFar;
  
      _projectionMatrix.copyValue(that._projectionMatrix);
      _modelMatrix.copyValue(that._modelMatrix);
      _modelViewMatrix.copyValue(that._modelViewMatrix);
  
      _cartesianCenterOfView.set(that._cartesianCenterOfView);
  
      _geodeticCenterOfView = that._geodeticCenterOfView;
  
      _frustum = that._frustum;
  
      _frustumInModelCoordinates = that._frustumInModelCoordinates;
  
      _geodeticPosition = that._geodeticPosition;
      _angle2Horizon = that._angle2Horizon;
  
      _tanHalfVerticalFOV = that._tanHalfVerticalFOV;
      _tanHalfHorizontalFOV = that._tanHalfHorizontalFOV;
    }
  
  }

  public final void resizeViewport(int width, int height)
  {
    if ((width != _viewPortWidth) || (height != _viewPortHeight))
    {
      _timestamp++;
  
      final int viewPortH = (_viewPortHeight == 0) ? height : _viewPortHeight;
      final int viewPortW = (_viewPortWidth == 0) ? width : _viewPortWidth;
      _tanHalfVerticalFOV = _tanHalfVerticalFOV / width * viewPortW;
      _tanHalfHorizontalFOV = _tanHalfHorizontalFOV / height * viewPortH;
  
      _viewPortWidth = width;
      _viewPortHeight = height;
  
      _dirtyFlags.setAllDirty();
    }
  }

  public final Vector3D pixel2Ray(Vector2I pixel)
  {
    final int px = pixel._x;
    final int py = _viewPortHeight - pixel._y;
    final Vector3D pixel3D = new Vector3D(px, py, 0);
  
    final Vector3D obj = getModelViewMatrix().unproject(pixel3D, 0, 0, _viewPortWidth, _viewPortHeight);
    if (obj.isNan())
    {
      ILogger.instance().logWarning("Pixel to Ray return NaN");
      return obj;
    }
  
    return obj.sub(_position.asVector3D());
  }
  public final Vector3D pixel2Ray(Vector2F pixel)
  {
    final float px = pixel._x;
    final float py = _viewPortHeight - pixel._y;
    final Vector3D pixel3D = new Vector3D(px, py, 0);
  
    final Vector3D obj = getModelViewMatrix().unproject(pixel3D, 0, 0, _viewPortWidth, _viewPortHeight);
    if (obj.isNan())
    {
      ILogger.instance().logWarning("Pixel to Ray return NaN");
      return obj;
    }
  
    return obj.sub(_position.asVector3D());
  }

  public final Vector3D pixel2PlanetPoint(Vector2I pixel)
  {
    return _planet.closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
  }

  public final Vector2F point2Pixel(Vector3D point)
  {
    final Vector2D p = getModelViewMatrix().project(point, 0, 0, _viewPortWidth, _viewPortHeight);
  
    return new Vector2F((float) p._x, (float)(_viewPortHeight - p._y));
  }
  public final Vector2F point2Pixel(Vector3F point)
  {
    final Vector2F p = getModelViewMatrix().project(point, 0, 0, _viewPortWidth, _viewPortHeight);
  
    return new Vector2F(p._x, (_viewPortHeight - p._y));
  }

  public final int getViewPortWidth()
  {
     return _viewPortWidth;
  }
  public final int getViewPortHeight()
  {
     return _viewPortHeight;
  }

  public final float getViewPortRatio()
  {
    return (float) _viewPortWidth / _viewPortHeight;
  }

  public final EffectTarget getEffectTarget()
  {
    return _camEffectTarget;
  }

  public final Vector3D getCartesianPosition()
  {
    return _position.asVector3D();
  }
  public final void getCartesianPositionMutable(MutableVector3D result)
  {
    result.set(_position);
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
  public final void getUpMutable(MutableVector3D result)
  {
    result.set(_up);
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
    // perform the subtraction inline to avoid a temporary MutableVector3D instance
    return new Vector3D(_center.x() - _position.x(), _center.y() - _position.y(), _center.z() - _position.z());
  }

  public final boolean hasValidViewDirection()
  {
    double d = _center.squaredDistanceTo(_position);
    return (d > 0) && !(d != d);
  }

  public final void getViewDirectionInto(MutableVector3D result)
  {
    result.set(_center.x() - _position.x(), _center.y() - _position.y(), _center.z() - _position.z());
  }

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
  public final void moveForward(double distance)
  {
    final Vector3D view = getViewDirection().normalized();
    applyTransform(MutableMatrix44D.createTranslationMatrix(view.times(distance)));
  }
  public final void translateCamera(Vector3D desp)
  {
    applyTransform(MutableMatrix44D.createTranslationMatrix(desp));
  }

  public final void move(Vector3D direction, double distance)
  {
    applyTransform(MutableMatrix44D.createTranslationMatrix(direction.times(distance)));
  }

  public final void pivotOnCenter(Angle a)
  {
    final Vector3D rotationAxis = _position.sub(_center).asVector3D();
    rotateWithAxis(rotationAxis, a);
  }

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
    ILogger.instance().logInfo("Viewport width: %d, height %d\n", _viewPortWidth, _viewPortHeight);
  }

  public final Frustum getFrustumInModelCoordinates()
  {
    if (_dirtyFlags._frustumMCDirty)
    {
      _dirtyFlags._frustumMCDirty = false;
      if (_frustumInModelCoordinates != null)
         _frustumInModelCoordinates.dispose();
      _frustumInModelCoordinates = getFrustum().transformedBy_P(getModelMatrix());
    }
    return _frustumInModelCoordinates;
  }

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
  
    return Vector3D.angleBetween(point0, point1);
  }

  public final void initialize(G3MContext context)
  {
    _planet = context.getPlanet();
    // #warning move this to Planet, and remove isFlat() method (DGD)
    if (_planet.isFlat())
    {
      setCartesianPosition(new MutableVector3D(0, 0, _planet.getRadii()._y * 5));
      setUp(new MutableVector3D(0, 1, 0));
    }
    else
    {
      setCartesianPosition(new MutableVector3D(_planet.getRadii().maxAxis() * 5, 0, 0));
      setUp(new MutableVector3D(0, 0, 1));
    }
    _dirtyFlags.setAllDirty();
  }

  public final void setCartesianPosition(MutableVector3D v)
  {
    if (!v.equalTo(_position))
    {
      _timestamp++;
      _position.set(v);
      if (_geodeticPosition != null)
         _geodeticPosition.dispose();
      _geodeticPosition = null;
      _dirtyFlags.setAllDirty();
      final double distanceToPlanetCenter = _position.length();
      final double planetRadius = distanceToPlanetCenter - getGeodeticHeight();
      _angle2Horizon = Math.acos(planetRadius/distanceToPlanetCenter);
      _normalizedPosition.set(_position);
      _normalizedPosition.normalize();
    }
  }

  public final void setCartesianPosition(Vector3D v)
  {
    setCartesianPosition(v.asMutableVector3D());
  }

  public final Angle getHeading()
  {
    return getHeadingPitchRoll()._heading;
  }
  public final void setHeading(Angle angle)
  {
    final TaitBryanAngles angles = getHeadingPitchRoll();
    final CoordinateSystem localRS = getLocalCoordinateSystem();
    final CoordinateSystem cameraRS = localRS.applyTaitBryanAngles(angle, angles._pitch, angles._roll);
    setCameraCoordinateSystem(cameraRS);
  }
  public final Angle getPitch()
  {
    return getHeadingPitchRoll()._pitch;
  }
  public final void setPitch(Angle angle)
  {
    final TaitBryanAngles angles = getHeadingPitchRoll();
    final CoordinateSystem localRS = getLocalCoordinateSystem();
    final CoordinateSystem cameraRS = localRS.applyTaitBryanAngles(angles._heading, angle, angles._roll);
    setCameraCoordinateSystem(cameraRS);
  }

  public final Geodetic3D getGeodeticPosition()
  {
    if (_geodeticPosition == null)
    {
      _geodeticPosition = new Geodetic3D(_planet.toGeodetic3D(getCartesianPosition()));
      if (_geodeticPosition.isNan())
      {
        throw new RuntimeException("Camera logic error, invalid _geodeticPosition");
      }
    }
    return _geodeticPosition;
  }
  public final double getGeodeticHeight()
  {
    return getGeodeticPosition()._height;
  }

  public final void setGeodeticPosition(Geodetic3D g3d)
  {
    final TaitBryanAngles angles = getHeadingPitchRoll();
    setPitch(Angle._MINUS_HALF_PI);
    final MutableMatrix44D dragMatrix = _planet.drag(getGeodeticPosition(), g3d);
    if (dragMatrix.isValid())
    {
      applyTransform(dragMatrix);
    }
    setHeadingPitchRoll(angles._heading, angles._pitch, angles._roll);
  }

  public final void setGeodeticPositionStablePitch(Geodetic3D g3d)
  {
    MutableMatrix44D dragMatrix = _planet.drag(getGeodeticPosition(), g3d);
    if (dragMatrix.isValid())
       applyTransform(dragMatrix);
  }

  public final void setGeodeticPosition(Angle latitude, Angle longitude, double height)
  {
    setGeodeticPosition(new Geodetic3D(latitude, longitude, height));
  }

  public final void setGeodeticPosition(Geodetic2D g2d, double height)
  {
    setGeodeticPosition(new Geodetic3D(g2d, height));
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
    final Vector3D north2D = _planet.getNorth().projectionInPlane(normal);
    final Vector3D orientedVector = north2D.rotateAroundAxis(normal, azimuth.times(-1));
    final Vector3D axis = orientedVector.cross(normal);
    final Vector3D finalVector = orientedVector.rotateAroundAxis(axis, altitude);
    final Vector3D position = cartesianCenter.add(finalVector.normalized().times(distance));
    final Vector3D finalUp = finalVector.rotateAroundAxis(axis, Angle._HALF_PI);
    setCartesianPosition(position.asMutableVector3D());
    setCenter(cartesianCenter.asMutableVector3D());
    setUp(finalUp.asMutableVector3D());
    //  _dirtyFlags.setAllDirty();
  }

  /**
   Finds where a camera with the given pitch must stand so that position1 shows up at screenPosition1
   and position2 at screenPosition2. Screen positions are viewport fractions: (0,0) top-left,
   (1,1) bottom-right, the same frame point2Pixel uses. Does not move this camera.

   Returns CameraPose::nan() when no such camera exists: same screen spot for both, points too far
   apart for a curved planet, or a target that would fall above the horizon.
   */
  public final CameraPose computeCameraPose(Geodetic3D position1, Vector2F screenPosition1, Geodetic3D position2, Vector2F screenPosition2, Angle pitch)
  {
    if (position1.isNan() || position2.isNan() || screenPosition1.isNan() || screenPosition2.isNan() || pitch.isNan())
    {
      return CameraPose.nan();
    }
  
    final Vector2F target1 = new Vector2F(screenPosition1._x * _viewPortWidth, screenPosition1._y * _viewPortHeight);
    final Vector2F target2 = new Vector2F(screenPosition2._x * _viewPortWidth, screenPosition2._y * _viewPortHeight);
    if (target2.sub(target1).length() < 1)
    {
      return CameraPose.nan();
    }
  
    final Vector3D cartesian1 = _planet.toCartesian(position1);
    final Vector3D cartesian2 = _planet.toCartesian(position2);
    final double chord = cartesian2.sub(cartesian1).length();
    if (chord < 1)
    {
      return CameraPose.nan();
    }
  
    final Geodetic3D initialCenter = new Geodetic3D(_planet.getMidPoint(position1.asGeodetic2D(), position2.asGeodetic2D()), (position1._height + position2._height) / 2);
  
    Camera workingCamera = new Camera(-1, _frustumPolicy.copy());
    workingCamera.copyFrom(this, true);
  
    final boolean solved = workingCamera.solvePointOfView(cartesian1, target1, cartesian2, target2, initialCenter, chord * 2, pitch);
  
    final CameraPose pose = solved ? new CameraPose(workingCamera.getGeodeticPosition(), workingCamera.getHeading(), workingCamera.getPitch()) : CameraPose.nan();
  
    if (workingCamera != null)
       workingCamera.dispose();
  
    return pose;
  }

  public final void forceMatrixCreation()
  {
    getGeodeticCenterOfView();
    //getXYZCenterOfView();
    _getCartesianCenterOfView();
    getFrustumInModelCoordinates();
    getProjectionMatrix44D();
    getModelMatrix44D();
    getModelViewMatrix().asMatrix44D();
  }

  public final Matrix44D getModelMatrix44D()
  {
    return getModelMatrix().asMatrix44D();
  }

  public final Matrix44D getProjectionMatrix44D()
  {
    return getProjectionMatrix().asMatrix44D();
  }

  public final Matrix44D getModelViewMatrix44D()
  {
    return getModelViewMatrix().asMatrix44D();
  }

  public final double getAngle2HorizonInRadians()
  {
     return _angle2Horizon;
  }

  public final double getProjectedSphereArea(Sphere sphere)
  {
    // this implementation is not right exact, but it's faster.
    final double z = sphere._center.distanceTo(getCartesianPosition());
    final double rWorld = sphere._radius * _frustumData._zNear / z;
    final double rScreen = rWorld * _viewPortHeight / (_frustumData._top - _frustumData._bottom);
    return DefineConstants.PI * rScreen * rScreen;
  }

  public final void applyTransform(MutableMatrix44D M)
  {
    setCartesianPosition(_position.transformedBy(M, 1.0));
    setCenter(_center.transformedBy(M, 1.0));
    setUp(_up.transformedBy(M, 0.0));
    //_dirtyFlags.setAllDirty();
  }

  public final boolean isPositionWithin(Sector sector, double height)
  {
    final Geodetic3D position = getGeodeticPosition();
    return sector.contains(position._latitude, position._longitude) && height >= position._height;
  }
  public final boolean isCenterOfViewWithin(Sector sector, double height)
  {
    final Geodetic3D position = getGeodeticCenterOfView();
    return sector.contains(position._latitude, position._longitude) && height >= position._height;
  }

  //In case any of the angles is NAN it would be inferred considering the vieport ratio
  public final void setFOV(Angle vertical, Angle horizontal)
  {
    final Angle halfHFOV = horizontal.div(2.0);
    final Angle halfVFOV = vertical.div(2.0);
    final double newH = halfHFOV.tangent();
    final double newV = halfVFOV.tangent();
    if ((newH != _tanHalfHorizontalFOV) || (newV != _tanHalfVerticalFOV))
    {
      _timestamp++;
      _tanHalfHorizontalFOV = newH;
      _tanHalfVerticalFOV = newV;
  
      _dirtyFlags._frustumDataDirty = true;
      _dirtyFlags._projectionMatrixDirty = true;
      _dirtyFlags._modelViewMatrixDirty = true;
      _dirtyFlags._frustumDirty = true;
      _dirtyFlags._frustumMCDirty = true;
    }
  }

  public final Angle getRoll()
  {
    return getHeadingPitchRoll()._roll;
  }
  public final void setRoll(Angle angle)
  {
    final TaitBryanAngles angles = getHeadingPitchRoll();
  
    final CoordinateSystem localRS = getLocalCoordinateSystem();
    final CoordinateSystem cameraRS = localRS.applyTaitBryanAngles(angles._heading, angles._pitch, angle);
    setCameraCoordinateSystem(cameraRS);
  }

  public final CoordinateSystem getLocalCoordinateSystem()
  {
    return _planet.getCoordinateSystemAt(getGeodeticPosition());
  }
  public final CoordinateSystem getCameraCoordinateSystem()
  {
    return CoordinateSystem.fromCamera(this);
  }
  public final TaitBryanAngles getHeadingPitchRoll()
  {
    final CoordinateSystem localRS = getLocalCoordinateSystem();
    final CoordinateSystem cameraRS = getCameraCoordinateSystem();
    return cameraRS.getTaitBryanAngles(localRS);
  }
  public final void setHeadingPitchRoll(Angle heading, Angle pitch, Angle roll)
  {
    final CoordinateSystem localRS = getLocalCoordinateSystem();
    final CoordinateSystem newCameraRS = localRS.applyTaitBryanAngles(heading, pitch, roll);
    setCameraCoordinateSystem(newCameraRS);
  }

  public final double getEstimatedPixelDistance(Vector3D point0, Vector3D point1)
  {
    _ray0.putSub(_position, point0);
    _ray1.putSub(_position, point1);
    final double angleInRadians = MutableVector3D.angleInRadiansBetween(_ray1, _ray0);
    final FrustumData frustumData = getFrustumData();
    final double distanceInMeters = frustumData._zNear * IMathUtils.instance().tan(angleInRadians/2);
    return distanceInMeters * _viewPortHeight / frustumData._top;
  }

  public final long getTimestamp()
  {
    return _timestamp;
  }

  public final void setLookAtParams(MutableVector3D position, MutableVector3D center, MutableVector3D up)
  {
    setCartesianPosition(position);
    setCenter(center);
    setUp(up);
  }

  public final void getLookAtParamsInto(MutableVector3D position, MutableVector3D center, MutableVector3D up)
  {
    position.set(_position);
    center.set(_center);
    up.set(_up);
  }

  public final void getModelViewMatrixInto(MutableMatrix44D matrix)
  {
    matrix.copyValue(getModelViewMatrix());
  }

  public final void getViewPortInto(MutableVector2I viewport)
  {
    viewport.set(_viewPortWidth, _viewPortHeight);
  }

  public static void pixel2RayInto(MutableVector3D position, Vector2F pixel, MutableVector2I viewport, MutableMatrix44D modelViewMatrix, MutableVector3D ray)
  {
    final float px = pixel._x;
    final float py = viewport.y() - pixel._y;
    final Vector3D pixel3D = new Vector3D(px, py, 0);
    final Vector3D obj = modelViewMatrix.unproject(pixel3D, 0, 0, viewport.x(), viewport.y());
    if (obj.isNan())
    {
      ray.set(obj);
    }
    else
    {
      ray.set(obj._x-position.x(), obj._y-position.y(), obj._z-position.z());
    }
  }

  public static Vector3D pixel2Ray(MutableVector3D position, Vector2F pixel, MutableVector2I viewport, MutableMatrix44D modelViewMatrix)
  {
    final float px = pixel._x;
    final float py = viewport.y() - pixel._y;
    final Vector3D pixel3D = new Vector3D(px, py, 0);
    final Vector3D obj = modelViewMatrix.unproject(pixel3D, 0, 0, viewport.x(), viewport.y());
    if (obj.isNan())
    {
      return obj;
    }
    return obj.sub(position.asVector3D());
  }

  public final Angle getHorizontalFOV()
  {
    return Angle.fromRadians(IMathUtils.instance().atan(_tanHalfHorizontalFOV) * 2);
  }

  public final Angle getVerticalFOV()
  {
    return Angle.fromRadians(IMathUtils.instance().atan(_tanHalfVerticalFOV) * 2);
  }

  public final void setCameraCoordinateSystem(CoordinateSystem rs)
  {
    _timestamp++;
    _center.set(_position);
    _center.addInPlace(rs._y);
    _up.set(rs._z);
    _dirtyFlags.setAllDirty();
  }

  public final void getVerticesOfZNearPlane(IFloatBuffer vertices)
  {
    final Plane zNearPlane = getFrustumInModelCoordinates().getNearPlane();
  
    final Vector3D pos = getCartesianPosition();
    final Vector3D vd = getViewDirection();
  
    final Vector3D c = zNearPlane.intersectionWithRay(pos, vd);
    final Vector3D up = getUp().normalized().times(getFrustumData()._top * 2.0);
    final Vector3D right = vd.cross(up).normalized().times(getFrustumData()._right * 2.0);
  
    vertices.putVector3D(0, c.sub(up).sub(right));
    vertices.putVector3D(1, c.add(up).sub(right));
    vertices.putVector3D(2, c.sub(up).add(right));
    vertices.putVector3D(3, c.add(right).add(up));
  }

  public final FrustumData getFrustumData()
  {
    if (_dirtyFlags._frustumDataDirty)
    {
      _dirtyFlags._frustumDataDirty = false;
      if (_frustumData != null)
         _frustumData.dispose();
      _frustumData = calculateFrustumData();
    }
    return _frustumData;
  }

  public final Planet getPlanet()
  {
    return _planet;
  }

  public final void setFixedFrustum(double zNear, double zFar)
  {
    _timestamp++;
    _fixedZNear = zNear;
    _fixedZFar = zFar;
    _dirtyFlags.setAllDirty();
  }

  public final void resetFrustumPolicy()
  {
    _timestamp++;
    _fixedZNear = Double.NaN;
    _fixedZFar = Double.NaN;
    _dirtyFlags.setAllDirty();
  }


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Camera(Camera that);

  //  Camera(const Camera &that):
  //  _viewPortWidth(that._viewPortWidth),
  //  _viewPortHeight(that._viewPortHeight),
  //  _planet(that._planet),
  //  _position(that._position),
  //  _center(that._center),
  //  _up(that._up),
  //  _dirtyFlags(that._dirtyFlags),
  //  _frustumData(that._frustumData),
  //  _projectionMatrix(that._projectionMatrix),
  //  _modelMatrix(that._modelMatrix),
  //  _modelViewMatrix(that._modelViewMatrix),
  //  _cartesianCenterOfView(that._cartesianCenterOfView),
  //  _geodeticCenterOfView((that._geodeticCenterOfView == NULL) ? NULL : new Geodetic3D(*that._geodeticCenterOfView)),
  //  _frustum((that._frustum == NULL) ? NULL : new Frustum(*that._frustum)),
  //  _frustumInModelCoordinates((that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates)),
  //  _camEffectTarget(new CameraEffectTarget()),
  //  _geodeticPosition((that._geodeticPosition == NULL) ? NULL: new Geodetic3D(*that._geodeticPosition)),
  //  _angle2Horizon(that._angle2Horizon),
  //  _normalizedPosition(that._normalizedPosition),
  //  _tanHalfVerticalFieldOfView(NAND),
  //  _tanHalfHorizontalFieldOfView(NAND),
  //  _timestamp(that._timestamp)
  //  {
  //  }

  private final FrustumPolicy _frustumPolicy;

  private long _timestamp;

  private MutableVector3D _ray0 = new MutableVector3D();
  private MutableVector3D _ray1 = new MutableVector3D();


  //IF A NEW ATTRIBUTE IS ADDED CHECK CONSTRUCTORS AND RESET() !!!!
  private int _viewPortWidth;
  private int _viewPortHeight;
  private Planet _planet;
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
  private FrustumData _frustumData;
  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();
  private MutableMatrix44D _modelMatrix = new MutableMatrix44D();
  private MutableMatrix44D _modelViewMatrix = new MutableMatrix44D();
  private MutableVector3D _cartesianCenterOfView = new MutableVector3D();
  private Geodetic3D _geodeticCenterOfView;
  private Frustum _frustum;
  private Frustum _frustumInModelCoordinates;
  private double _tanHalfVerticalFOV;
  private double _tanHalfHorizontalFOV;

  private double _fixedZNear;
  private double _fixedZFar;

  //The Camera Effect Target
  private static class CameraEffectTarget implements EffectTarget
  {
    public void dispose()
    {
    }
  }

  private CameraEffectTarget _camEffectTarget;

  private Vector3D centerOfViewOnPlanet()
  {
    return _planet.closestIntersection(_position.asVector3D(), getViewDirection());
  }

  // computeCameraPose() runs this on a working copy; on success the copy is left at the solution
  private boolean solvePointOfView(Vector3D cartesian1, Vector2F target1, Vector3D cartesian2, Vector2F target2, Geodetic3D initialCenter, double initialDistance, Angle pitch)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final Vector2F targetDelta = target2.sub(target1);
    final double targetAngle = mu.atan2((double) targetDelta._y, (double) targetDelta._x);
    final double targetLength = targetDelta.length();
    final Vector2F targetMiddle = target1.add(target2).div(2);
    final Vector2F viewportCenter = new Vector2F(_viewPortWidth / 2.0f, _viewPortHeight / 2.0f);
  
    final double centerHeight = initialCenter._height;
    double centerLatitudeInRadians = initialCenter._latitude._radians;
    double centerLongitudeInRadians = initialCenter._longitude._radians;
    double distance = initialDistance;
    double azimuthInRadians = 0;
    // setPointOfView's altitude is the elevation over the horizon at the center (90 = straight down).
    // On a curved planet that is not the camera's own pitch, so it gets corrected below.
    double altitudeInRadians = -pitch._radians;
  
    // the azimuth turns the on-screen segment one way or the other; measure which instead of assuming
    setPointOfView(initialCenter, distance, Angle.fromRadians(0), Angle.fromRadians(altitudeInRadians));
    final double angleAtZero = screenAngleOfSegment(cartesian1, cartesian2);
    setPointOfView(initialCenter, distance, Angle.fromRadians(0.05), Angle.fromRadians(altitudeInRadians));
    final double angleAtProbe = screenAngleOfSegment(cartesian1, cartesian2);
    final double azimuthSign = (signedAngleInRadians(angleAtProbe - angleAtZero) > 0) ? 1 : -1;
  
    final int maxIterations = 50;
    double previousError = 0;
    int growingErrorCount = 0;
    for (int i = 0; i < maxIterations; i++)
    {
      final Geodetic3D center = new Geodetic3D(Angle.fromRadians(centerLatitudeInRadians), Angle.fromRadians(centerLongitudeInRadians), centerHeight);
  
      // 1. pitch: the camera's own pitch must match the requested one
      setPointOfView(center, distance, Angle.fromRadians(azimuthInRadians), Angle.fromRadians(altitudeInRadians));
      final double pitchErrorInRadians = signedAngleInRadians(getPitch()._radians - pitch._radians);
      altitudeInRadians += pitchErrorInRadians;
      final Angle altitude = Angle.fromRadians(altitudeInRadians);
  
      // 2. heading: turn until the segment has the target direction
      setPointOfView(center, distance, Angle.fromRadians(azimuthInRadians), altitude);
      final Vector2F pixel1 = point2Pixel(cartesian1);
      final Vector2F pixel2 = point2Pixel(cartesian2);
      if (pixel1.isNan() || pixel2.isNan())
      {
        return false;
      }
      final double error = mu.max(pixel1.sub(target1).length(), pixel2.sub(target2).length());
      if ((error < 0.5) && (mu.abs(pitchErrorInRadians) < 0.0001))
      {
        return isAboveHorizonOf(cartesian1) && isAboveHorizonOf(cartesian2);
      }
      growingErrorCount = (error > previousError) ? growingErrorCount + 1 : 0;
      previousError = error;
      if (growingErrorCount >= 5)
      {
        return false;
      }
      final Vector2F delta = pixel2.sub(pixel1);
      if (delta.length() < 0.001)
      {
        return false;
      }
      final double angleError = signedAngleInRadians(targetAngle - mu.atan2((double) delta._y, (double) delta._x));
      azimuthInRadians += azimuthSign * angleError;
  
      // 3. distance: move away or closer until the segment has the target length
      setPointOfView(center, distance, Angle.fromRadians(azimuthInRadians), altitude);
      final double currentLength = point2Pixel(cartesian2).sub(point2Pixel(cartesian1)).length();
      if (currentLength < 0.001)
      {
        return false;
      }
      distance *= currentLength / targetLength;
  
      // 4. center: look at whatever is now under the pixel that must end up at the viewport center
      setPointOfView(center, distance, Angle.fromRadians(azimuthInRadians), altitude);
      final Vector2F currentMiddle = point2Pixel(cartesian1).add(point2Pixel(cartesian2)).div(2);
      final Vector2F pixelForCenter = viewportCenter.add(currentMiddle).sub(targetMiddle);
      final Vector3D newCenter = _planet.closestIntersection(_position.asVector3D(), pixel2Ray(pixelForCenter));
      if (newCenter.isNan())
      {
        return false;
      }
      final Geodetic2D newCenter2D = _planet.toGeodetic2D(newCenter);
      centerLatitudeInRadians = newCenter2D._latitude._radians;
      centerLongitudeInRadians = newCenter2D._longitude._radians;
    }
  
    return false;
  }

  private double screenAngleOfSegment(Vector3D cartesian1, Vector3D cartesian2)
  {
    final Vector2F delta = point2Pixel(cartesian2).sub(point2Pixel(cartesian1));
    return IMathUtils.instance().atan2((double) delta._y, (double) delta._x);
  }

  // point2Pixel projects points hidden behind the planet too; this tells them apart
  private boolean isAboveHorizonOf(Vector3D point)
  {
    final Vector3D toCamera = _position.asVector3D().sub(point);
    return _planet.geodeticSurfaceNormal(point).dot(toCamera) > 0;
  }

  private static double signedAngleInRadians(double radians)
  {
    while (radians > DefineConstants.PI)
    {
      radians -= 2 * DefineConstants.PI;
    }
    while (radians < -DefineConstants.PI)
    {
      radians += 2 * DefineConstants.PI;
    }
    return radians;
  }

  private void setCenter(MutableVector3D v)
  {
    if (!v.equalTo(_center))
    {
      _timestamp++;
      _center.set(v);
      _dirtyFlags.setAllDirty();
    }
  }

  private void setUp(MutableVector3D v)
  {
    if (!v.equalTo(_up))
    {
      _timestamp++;
      _up.set(v);
      _dirtyFlags.setAllDirty();
    }
  }

  // intersection of view direction with globe in(x,y,z)
  private MutableVector3D _getCartesianCenterOfView()
  {
    if (_dirtyFlags._cartesianCenterOfViewDirty)
    {
      _dirtyFlags._cartesianCenterOfViewDirty = false;
      _cartesianCenterOfView.set(centerOfViewOnPlanet());
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

  private FrustumData calculateFrustumData()
  {
    double zNear;
    double zFar;
  
    if ((_fixedZNear != _fixedZNear) || (_fixedZFar != _fixedZFar))
    {
      final Vector2D zNearAndZFar = _frustumPolicy.calculateFrustumZNearAndZFar(this);
      zNear = (_fixedZNear != _fixedZNear) ? zNearAndZFar._x : _fixedZNear;
      zFar = (_fixedZFar != _fixedZFar) ? zNearAndZFar._y : _fixedZFar;
    }
    else
    {
      zNear = _fixedZNear;
      zFar = _fixedZFar;
    }
  
    return calculateFrustumData(zNear, zFar);
  }
  private FrustumData calculateFrustumData(double zNear, double zFar)
  {
    if ((_tanHalfHorizontalFOV != _tanHalfHorizontalFOV) || (_tanHalfVerticalFOV != _tanHalfVerticalFOV))
    {
      final double ratioScreen = (double) _viewPortHeight / _viewPortWidth;
  
      if ((_tanHalfHorizontalFOV != _tanHalfHorizontalFOV) && (_tanHalfVerticalFOV != _tanHalfVerticalFOV))
      {
        //Default behaviour _tanHalfFieldOfView = 0.3  =>  aprox tan(34 degrees / 2)
        _tanHalfVerticalFOV = 0.3;
        _tanHalfHorizontalFOV = _tanHalfVerticalFOV / ratioScreen;
      }
      else
      {
        if ((_tanHalfHorizontalFOV != _tanHalfHorizontalFOV))
        {
          _tanHalfHorizontalFOV = _tanHalfVerticalFOV / ratioScreen;
        }
        else if (_tanHalfVerticalFOV != _tanHalfVerticalFOV)
        {
          _tanHalfVerticalFOV = _tanHalfHorizontalFOV * ratioScreen;
        }
      }
    }
  
    final double right = _tanHalfHorizontalFOV * zNear;
    final double left = -right;
    final double top = _tanHalfVerticalFOV * zNear;
    final double bottom = -top;
    return new FrustumData(left, right, bottom, top, zNear, zFar);
  }

  // opengl projection matrix
  private MutableMatrix44D getProjectionMatrix()
  {
    if (_dirtyFlags._projectionMatrixDirty)
    {
      _dirtyFlags._projectionMatrixDirty = false;
      _projectionMatrix.copyValue(MutableMatrix44D.createProjectionMatrix(getFrustumData()));
    }
    return _projectionMatrix;
  }

  // Model matrix, computed in CPU in double precision
  private MutableMatrix44D getModelMatrix()
  {
    if (_dirtyFlags._modelMatrixDirty)
    {
      _dirtyFlags._modelMatrixDirty = false;
      _modelMatrix.copyValue(MutableMatrix44D.createModelMatrix(_position, _center, _up));
    }
    return _modelMatrix;
  }

  // multiplication of model * projection
  private MutableMatrix44D getModelViewMatrix()
  {
    if (_dirtyFlags._modelViewMatrixDirty)
    {
      _dirtyFlags._modelViewMatrixDirty = false;
      _modelViewMatrix.copyValueOfMultiplication(getProjectionMatrix(), getModelMatrix());
    }
    return _modelViewMatrix;
  }

}