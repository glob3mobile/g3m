package org.glob3.mobile.generated; 
/**
* Class to control the camera.
*/
public class Camera
{


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	Camera(Camera c);

	/*
	 * Constructor of Camera.
	 */
	public Camera(int width, int height)
	{
		_pos = new Vector3D(63650000.0, 0.0, 0.0);
		_center = new Vector3D(0.0, 0.0, 0.0);
		_up = new Vector3D(0.0, 0.0, 1.0);
		ResizeViewport(width, height);
	}

	public final void ResizeViewport(int width, int height)
	{
		_viewport[0] = _viewport[1] = 0;
		_viewport[2] = width;
		_viewport[3] = height;
	}

	public final float GetViewPortRatio()
	{
		return ((float) _viewport[2]) / _viewport[3];
	}

	/*
	* Sets camera params at the beginning of every frame.
	*/
	public final void Draw(RenderContext rc)
	{
		double znear;
    
		// update znear
		//double height = GetPosGeo3D().height();
	  double height = _pos.length();
    
    
		if (height > 1273000.0)
			znear = 636500.0;
		else if (height > 12730.0)
			znear = 6365.0;
		else if (height > 3182.5)
			znear = 63.65;
		else
			znear = 19.095;
    
		// compute projection matrix
		double ratioScreen = (double) _viewport[3] / _viewport[2];
		Glu.ComputeProjectionMatrix(-0.3 / ratioScreen * znear, 0.3 / ratioScreen * znear, -0.3 * znear, 0.3 * znear, znear, 10000 * znear, _projection);
    
		// obtaing gl object reference
		IGL gl = rc.getGL();
		gl.SetProjection(_projection);
    
		// make the lookat
		//glMatrixMode(GL_MODELVIEW);
		Glu.ComputeLookAtMatrix(_pos, _center, _up, _lookAt);
		float[] L = new float[16];
		for (int k = 0; k < 16; k++)
			L[k] = (float) _lookAt[k];
		gl.LoadMatrixf(L);
	}
	/* 
	 * Returns opengl lookat matrix.
	 * @param m The array where the data will be stored.
	 */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: inline void CopyLookAtMatrix(double m[]) const
	public final void CopyLookAtMatrix(double[] m)
	{
		for (int i = 0; i < 16; i++)
			m[i] = _lookAt[i];
	}

	//double frustumFactor;

	private int[] _viewport = new int[4];
	private double[] _lookAt = new double[16]; // gluLookAt matrix, computed in CPU in double precision
	private float[] _projection = new float[16]; // opengl projection matrix

	//void ApplyTransform (double M[16]);	

	private Vector3D _pos ; // position
	private Vector3D _center ; // center of view
	private Vector3D _up ; // vertical vector

	//std::vector <ICameraStopEventListener *> listeners;

}
/*
// THIS COPY CONSTRUCTOR IS ONLY FOR THE JAVA CONVERTER

Camera::Camera(const Camera &c):
//frustumFactor(c.frustumFactor),
lastYValid(c.lastYValid),
//dragWx(c.dragWx), dragWy(c.dragWy), dragWz(c.dragWz),
dragAngle(c.dragAngle), dragStep(c.dragStep),
rotAngle(c.rotAngle), rotStep(c.rotStep),
zoomDesp(c.z()oomDesp), zoomStep(c.z()oomStep),
moveDesp(c.moveDesp), moveStep(c.moveStep),
gotoposDesp(c.gotoposDesp), gotoposStep(c.gotoposStep),
//upx(c.upx), upy(c.upy), upz(c.upz),
//radius(c.radius),
distv(c.distv),
viewIntersectGlobe(c.viewIntersectGlobe),
pitch(c.pitch), roll(c.roll) {
	up.copyFrom(c.up);
	dragW.copyFrom(c.dragW);
	pv.x() = c.pv.x();
	pv.y() = c.pv.y();
	pv.z() = c.pv.z();
	pos.x() = c.pos.x();
	pos.y() = c.pos.y();
	pos.z() = c.pos.z();
	center.x() = c.center.x();
	center.y() = c.center.y();
	center.z() = c.center.z();
	//posl.lat=c.posl.lat;  posl.lon=c.posl.lon;
	posl.copyFrom(c.posl);
	//pvl.lat=c.pvl.lat;  pvl.lon=c.pvl.lon;
	pvl.copyFrom(c.pvl);
	for (int i = 0; i < 4; i++) {
		viewport[i] = c.viewport[i];
	}
	for (int i = 0; i < 16; i++) {
		lookAt[i] = c.lookAt[i];
	}
	for (int i = 0; i < 16; i++) {
		projection[i] = c.projection[i];
	}

}

 */

/*

void Camera::ApplyTransform(double M[16]) {
	_pos.x() = pos.x() * M[0] + _pos.y() * M[4] + _pos.z() * M[8] + M[12];
	_pos.y() = _pos.x() * M[1] + _pos.y() * M[5] + _pos.z() * M[9] + M[13];
	_pos.z() = _pos.x() * M[2] + _pos.y() * M[6] + _pos.z() * M[10] + M[14];
	center.x() = camera0.center.x() * M[0] + camera0.center.y() * M[4] + camera0.center.z() * M[8] + M[12];
	center.y() = camera0.center.x() * M[1] + camera0.center.y() * M[5] + camera0.center.z() * M[9] + M[13];
	center.z() = camera0.center.x() * M[2] + camera0.center.y() * M[6] + camera0.center.z() * M[10] + M[14];
	up.x() = camera0.up.x() * M[0] + camera0.up.y() * M[4] + camera0.up.z() * M[8];
	up.y() = camera0.up.x() * M[1] + camera0.up.y() * M[5] + camera0.up.z() * M[9];
	up.z() = camera0.up.x() * M[2] + camera0.up.y() * M[6] + camera0.up.z() * M[10];


}
*/