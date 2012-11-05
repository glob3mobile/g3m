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
	resizeViewport(width, height);
  }

  public void dispose()
  {

  }

  public final void copyFrom(Camera that)
  {
	_width = that._width;
	_height = that._height;
  
	_planet = that._planet;
  
	_position = new MutableVector3D(that._position);
	_center = new MutableVector3D(that._center);
	_up = new MutableVector3D(that._up);
  
	_dirtyFlags.copyFrom(that._dirtyFlags);
  
	_frustumData = new FrustumData(that._frustumData);
  
	_projectionMatrix = new MutableMatrix44D(that._projectionMatrix);
	_modelMatrix = new MutableMatrix44D(that._modelMatrix);
	_modelViewMatrix = new MutableMatrix44D(that._modelViewMatrix);
  
	_cartesianCenterOfView = new MutableVector3D(that._cartesianCenterOfView);
  
	_geodeticCenterOfView = (that._geodeticCenterOfView == null) ? null : new Geodetic3D(that._geodeticCenterOfView);
  
  
	_camEffectTarget = new CameraEffectTarget();
  
	_frustum = (that._frustum == null) ? null : new Frustum(that._frustum);
  
	_frustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
  
	_halfFrustum = (that._frustum == null) ? null : new Frustum(that._frustum);
  
	_halfFrustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
  }

  public final void resizeViewport(int width, int height)
  {
	_width = width;
	_height = height;
  
	_dirtyFlags._projectionMatrix = true;
  
	//cleanCachedValues();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void render(const RenderContext* rc) const
  public final void render(RenderContext rc)
  {
	GL gl = rc.getGL();
	gl.setProjection(getProjectionMatrix());
	gl.loadMatrixf(getModelMatrix());
  
	// TEMP: TEST TO SEE HALF SIZE FRUSTUM CLIPPING
	if (false)
	{
	  final MutableMatrix44D inversed = getModelMatrix().inversed();
  
	  final FrustumData data = calculateFrustumData();
	  final Vector3D p0 = new Vector3D(new Vector3D(data._left/4, data._top/4, -data._znear-10).transformedBy(inversed, 1));
	  final Vector3D p1 = new Vector3D(new Vector3D(data._left/4, data._bottom/4, -data._znear-10).transformedBy(inversed, 1));
	  final Vector3D p2 = new Vector3D(new Vector3D(data._right/4, data._bottom/4, -data._znear-10).transformedBy(inversed, 1));
	  final Vector3D p3 = new Vector3D(new Vector3D(data._right/4, data._top/4, -data._znear-10).transformedBy(inversed, 1));
  
	  float[] v = { (float) p0._x, (float) p0._y, (float) p0._z, (float) p1._x, (float) p1._y, (float) p1._z, (float) p2._x, (float) p2._y, (float) p2._z, (float) p3._x, (float) p3._y, (float) p3._z };
	  int[] i = {0, 1, 2, 3};
  
	  FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
	  IntBufferBuilder index = new IntBufferBuilder();
  
	  for (int n = 0; n<4; n++)
		vertices.add(v[3 *n], v[3 *n+1], v[3 *n+2]);
  
	  for (int n = 0; n<4; n++)
		index.add(i[n]);
  
	  IIntBuffer _indices = index.create();
	  IFloatBuffer _vertices = vertices.create();
  
	  gl.enableVerticesPosition();
	  gl.vertexPointer(3, 0, _vertices);
	  gl.lineWidth(2);
	  gl.color(1, 0, 1, 1);
	  gl.drawLineLoop(_indices);
  
	  gl.lineWidth(1);
	  gl.color(1, 1, 1, 1);
	}
  
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D pixel2Ray(const Vector2I& pixel) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D pixel2PlanetPoint(const Vector2I& pixel) const
  public final Vector3D pixel2PlanetPoint(Vector2I pixel)
  {
	return _planet.closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2I point2Pixel(const Vector3D& point) const
  public final Vector2I point2Pixel(Vector3D point)
  {
	final Vector2D p = getModelViewMatrix().project(point, 0, 0, _width, _height);
  
	//  int __TODO_check_isNan_is_needed;
	//  if (p.isNan()) {
	//    return p;
	//  }
  
	IMathUtils math = IMathUtils.instance();
  
	return new Vector2I(math.toInt(p._x), math.toInt(_height-p._y));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getWidth() const
  public final int getWidth()
  {
	  return _width;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getHeight() const
  public final int getHeight()
  {
	  return _height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getViewPortRatio() const
  public final float getViewPortRatio()
  {
	return (float) _width / _height;
  }

  public final EffectTarget getEffectTarget()
  {
	return _camEffectTarget;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getCartesianPosition() const
  public final Vector3D getCartesianPosition()
  {
	  return _position.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getCenter() const
  public final Vector3D getCenter()
  {
	  return _center.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getUp() const
  public final Vector3D getUp()
  {
	  return _up.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D getGeodeticCenterOfView() const
  public final Geodetic3D getGeodeticCenterOfView()
  {
	  return _getGeodeticCenterOfView();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getXYZCenterOfView() const
  public final Vector3D getXYZCenterOfView()
  {
	  return _getCartesianCenterOfView().asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getViewDirection() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Frustum* const getFrustumInModelCoordinates() const
  public final Frustum getFrustumInModelCoordinates()
  {
//    return getFrustumMC();
	if (_dirtyFlags._frustumMC)
	{
	  _dirtyFlags._frustumMC = false;
	  _frustumInModelCoordinates = getFrustum().transformedBy_P(getModelMatrix());
	}
	return _frustumInModelCoordinates;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Frustum* const getHalfFrustuminModelCoordinates() const
  public final Frustum getHalfFrustuminModelCoordinates()
  {
	return getHalfFrustumMC();
  }

  public final void setPosition(Geodetic3D g3d)
  {
	setCartesianPosition(_planet.toCartesian(g3d).asMutableVector3D());
  }

  public final Vector3D getHorizontalVector()
  {
	int todo_remove_get_in_matrix;
	final MutableMatrix44D M = getModelMatrix();
	return new Vector3D(M.get(0), M.get(4), M.get(8));
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

  public final void initialize(InitializationContext ic)
  {
	_planet = ic.getPlanet();
	setCartesianPosition(new MutableVector3D(_planet.getRadii().maxAxis() * 5, 0, 0));
	_dirtyFlags.setAll(true);
  }

  public final void resetPosition()
  {
	_position = new MutableVector3D(0, 0, 0);
	_center = new MutableVector3D(0, 0, 0);
	_up = new MutableVector3D(0, 0, 1);
  
	_dirtyFlags.setAll(true);
  
	_frustumData = new FrustumData();
	_projectionMatrix = new MutableMatrix44D();
	_modelMatrix = new MutableMatrix44D();
	_modelViewMatrix = new MutableMatrix44D();
	_cartesianCenterOfView = new MutableVector3D();
  
	if (_geodeticCenterOfView != null)
		_geodeticCenterOfView.dispose();
	_geodeticCenterOfView = null;
  
	if (_frustum != null)
		_frustum.dispose();
	_frustum = null;
  
	if (_frustumInModelCoordinates != null)
		_frustumInModelCoordinates.dispose();
	_frustumInModelCoordinates = null;
  
	if (_halfFrustumInModelCoordinates != null)
		_halfFrustumInModelCoordinates.dispose();
	_halfFrustumInModelCoordinates = null;
  
	if (_halfFrustum != null)
		_halfFrustum.dispose();
	_halfFrustum = null;
  }

  public final void setCartesianPosition(MutableVector3D v)
  {
	if (!v.equalTo(_position))
	{
	  _position = new MutableVector3D(v);
	  _dirtyFlags.setAll(true);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getHeading() const
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
	//printf ("previous heading=%f   current heading=%f\n", currentHeading.degrees(), getHeading().degrees());
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getPitch() const
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
	//printf ("previous pitch=%f   current pitch=%f\n", currentPitch.degrees(), getPitch().degrees());
  }

  public final void orbitTo(Vector3D pos)
  {
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
  }
  public final void orbitTo(Geodetic3D g3d)
  {
	orbitTo(_planet.toCartesian(g3d));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getHeading(const Vector3D& normal) const
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void unusedMethod() const
	public final void unusedMethod()
	{
	}
  }
  private CameraEffectTarget _camEffectTarget;

  private void applyTransform(MutableMatrix44D M)
  {
	setCartesianPosition(_position.transformedBy(M, 1.0));
	setCenter(_center.transformedBy(M, 1.0));
  
	setUp(_up.transformedBy(M, 0.0));
  
	//_dirtyFlags.setAll(true);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D centerOfViewOnPlanet() const
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: FrustumData getFrustumData() const
  private FrustumData getFrustumData()
  {
	if (_dirtyFlags._frustumData)
	{
	  _dirtyFlags._frustumData = false;
	  _frustumData = calculateFrustumData();
	}
	return _frustumData;
  }

  // opengl projection matrix       
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D getProjectionMatrix() const
  private MutableMatrix44D getProjectionMatrix()
  {
	if (_dirtyFlags._projectionMatrix)
	{
	  _dirtyFlags._projectionMatrix = false;
	  _projectionMatrix = MutableMatrix44D.createProjectionMatrix(getFrustumData());
	}
	return _projectionMatrix;
  }

  // Model matrix, computed in CPU in double precision
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D getModelMatrix() const
  private MutableMatrix44D getModelMatrix()
  {
	if (_dirtyFlags._modelMatrix)
	{
	  _dirtyFlags._modelMatrix = false;
	  _modelMatrix = MutableMatrix44D.createModelMatrix(_position, _center, _up);
	}
	return _modelMatrix;
  }

  // multiplication of model * projection 
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D getModelViewMatrix() const
  private MutableMatrix44D getModelViewMatrix()
  {
	if (_dirtyFlags._modelViewMatrix)
	{
	  _dirtyFlags._modelViewMatrix = false;
	  _modelViewMatrix = getProjectionMatrix().multiply(getModelMatrix());
	}
	return _modelViewMatrix;
  }

  // intersection of view direction with globe in(x,y,z)
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D _getCartesianCenterOfView() const
  private MutableVector3D _getCartesianCenterOfView()
  {
	if (_dirtyFlags._cartesianCenterOfView)
	{
	  _dirtyFlags._cartesianCenterOfView = false;
	  _cartesianCenterOfView = centerOfViewOnPlanet().asMutableVector3D();
	}
	return _cartesianCenterOfView;
  }

  // intersection of view direction with globe in geodetic
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D* _getGeodeticCenterOfView() const
  private Geodetic3D _getGeodeticCenterOfView()
  {
	if (_dirtyFlags._geodeticCenterOfView)
	{
	  _dirtyFlags._geodeticCenterOfView = false;
	  _geodeticCenterOfView = new Geodetic3D(_planet.toGeodetic3D(getXYZCenterOfView()));
	}
	return _geodeticCenterOfView;
  }

  // camera frustum
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Frustum* getFrustum() const
  private Frustum getFrustum()
  {
	if (_dirtyFlags._frustum)
	{
	  _dirtyFlags._frustum = false;
	  _frustum = new Frustum(getFrustumData());
	}
	return _frustum;
  }


  private int __temporal_test_for_clipping;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Frustum* getHalfFrustum() const
  private Frustum getHalfFrustum()
  {
	if (_dirtyFlags._halfFrustum)
	{
	  _dirtyFlags._halfFrustum = false;
	  FrustumData data = getFrustumData();
	  _halfFrustum = new Frustum(data._left/4, data._right/4, data._bottom/4, data._top/4, data._znear, data._zfar);
	}
	return _halfFrustum;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Frustum* getHalfFrustumMC() const
  private Frustum getHalfFrustumMC()
  {
	if (_dirtyFlags._halfFrustumMC)
	{
	  _dirtyFlags._halfFrustumMC = false;
	  _halfFrustumInModelCoordinates = getHalfFrustum().transformedBy_P(getModelMatrix());
	}
	return _halfFrustumInModelCoordinates;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: FrustumData calculateFrustumData() const
  private FrustumData calculateFrustumData()
  {
	// compute znear value
	final double maxRadius = _planet.getRadii().maxAxis();
	final double distanceToPlanetCenter = _position.length();
	final double distanceToSurface = distanceToPlanetCenter - maxRadius;

	double znear;
	if (distanceToSurface > maxRadius/5)
	{
	  znear = maxRadius / 10;
	}
	else if (distanceToSurface > maxRadius/500)
	{
	  znear = maxRadius / 1e4;
	}
	else if (distanceToSurface > maxRadius/2000)
	{
	  znear = maxRadius / 1e5;
	}
	else
	{
	  znear = maxRadius / 1e6 * 3;
	}

	// compute zfar value
	double zfar = 10000 * znear;
	if (zfar > distanceToPlanetCenter)
	{
	  zfar = distanceToPlanetCenter;
	}

	// compute rest of frustum numbers
	final double ratioScreen = (double) _height / _width;
	final double right = 0.3 / ratioScreen * znear;
	final double left = -right;
	final double top = 0.3 * znear;
	final double bottom = -top;

	return new FrustumData(left, right, bottom, top, znear, zfar);
  }


  //void calculateCachedValues();

  /*void cleanCachedValues() {
	_dirtyCachedValues = true;
	//    if (_frustum != NULL) {
	//      delete _frustum;
	//      _frustum = NULL;
	//    }
	if (_frustumInModelCoordinates != NULL) {
#ifdef C_CODE
	  delete _frustumInModelCoordinates;
#endif
	  _frustumInModelCoordinates = NULL;
	}
  }*/




}