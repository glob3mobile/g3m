/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustín Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */

#ifndef CAMERA
#define CAMERA

#include "Vector3D.hpp"

#include "Context.h"
#include "IFactory.h"

#include "Geodetic3D.hpp"


#define AUTO_DRAG_FRICTION              0.985
#define AUTO_ZOOM_FRICTION              0.850
#define AUTO_ROTATE_FRICTION            0.900
#define DOUBLE_TAP_MOVEMENT_PERCENTAGE  0.500

#define NUM_FRAMES_DOUBLE_TAP           20
#define NUM_FRAMES_GO_TO_POS            100

#define MIN_CAMERA_HEIGHT               65    // minimum camera height from ground (in m)


struct ProjectedPixel {
    double x, y;    // pixel coordinates
    double incl;    // inclination cosinus
};


/**
* Class to control the camera.
*/
class Camera{
    
public:

    Camera(const Camera &c);

    /*
     * Constructor of Camera.
     */
    Camera(int width, int height);

    void ResizeViewport(int width, int height);

    float GetViewPortRatio() {
        return ((float) viewport[2]) / viewport[3];
    }

    /*
    * Sets camera params at the beginning of every frame.
	*/
    void Draw(RenderContext &rc);

    void ZoomCamera(Vector3D p0, double factor, double angle, const Camera &camera0);

    void DragCamera(Vector3D p0, double px, double py, double modelView[16], const Camera &camera0);

    bool ZoomCameraAuto(Vector3D p0, const Camera &camera0);

    bool DragCameraAuto(const Camera &camera0);

    bool MoveCameraAuto(Vector3D p0, Vector3D p1, const Camera &camera0);

    void RotateCamera(Vector3D p0, float px, float py, float px0, float py0, const Camera &camera0);

    bool GoToPosAuto(Vector3D p0, Vector3D p1, const Camera &camera0);

    /* 
	* Computes point of the globe starting from a pixel.
	*/
    bool Pixel2GlobeSurface(double px, double py, double modelView[16], Vector3D &p);

    /*
     * Computes pixel starting from a globe point (return pixel (px,py) and inclination cosinus.
     */
    bool GlobeSurface2Pixel(Geodetic3D g3, double modelView[16], ProjectedPixel &p);

    /* 
	* Computes intersection point of view direction with the globe.
	*/
    bool IntersectionViewWithGlobe(Vector3D &p);

//    inline Geodetic2D GetIntersectionViewWithGlobe() {
//        return pvl;
//    }
//
//    inline double GetDistanceIntersectionViewWithGlobe() {
//        return distv;
//    }

//    /** 
//	* Rotates the view when user change screen orientation.
//	* @param angle The angle that the camera must be rotated.
//	*/
//    void RotateScreen(int angle);

    /* 
	* Moves the camera to a specified point.
	*/
    //const void SetPosLatLon (const LatLon l, const double R);

    /**
     * Sets the camera to a specified position.
     * @param g3 A Geodetic3D object specifying the latitude, longitude and altitude.
     */
    void SetPosGeo3D(Geodetic3D g3);

    Geodetic3D GetPosGeo3D();

    //inline double GetLat() { return posl.latitude(); }
    //inline double GetLon() { return posl.longitude(); }

    /** 
     * Gets the radius (distance to globe center).
     * @return The distance to globe center in meters.
     */
    //inline double GetRadius() { return radius; }

    /* 
     * Returns opengl lookat matrix.
     * @param m The array where the data will be stored.
     */
    inline void CopyLookAtMatrix(double m[]) const {
        for (int i = 0; i < 16; i++) m[i] = lookAt[i];
    }
//inline double* GetLookAtMatrix() {  return lookAt;}

    //double GetDistanceToPoint (double x, double y, double z);
    double GetDistanceToPoint(Vector3D p);

    double GetDistanceToPoint(Geodetic3D);

    Vector3D GetCenter() {
        return center;
    }

    Vector3D GetPos() {
        return pos;
    }

    double GetTilt();

private:
    //double frustumFactor;

    int viewport[4];
    double lookAt[16];            // gluLookAt matrix, computed in CPU in double precision
    float projection[16];        // opengl projection matrix

    float lastYValid;            // used when horizontal rotation

    //double dragWx, dragWy, dragWz;		// rotation axe for dragging
    Vector3D dragW;                     // rotation axe for dragging
    double dragAngle, dragStep;            // for automatic drag
    double rotAngle, rotStep;            // for automatic zoom & rotate
    double zoomDesp, zoomStep;            // for automatic zoom & rotate
    float moveDesp, moveStep;            // for double tapping movement
    float gotoposDesp, gotoposStep;     // for GoToPos function

    int gotoposIter;        

	void SetPosXYZ(const Vector3D p, double lon);
    void ApplyTransform (const Camera &camera0, double M[16]);	
	Vector3D GetHorizVector () const;
    Vector3D Pixel2Vector (double px, double py, double modelView[16]) const;
    bool IntersectionRayWithGlobe(Vector3D ray, Vector3D &p);   
	
    Vector3D pos;                        // position
    Vector3D center;                     // center of view
    Vector3D up;                        // vertical vector
    //Geodetic2D posl;                       // latlon value of position	
    //Vector3D pv;                         // intersection between view direction and globe 
    //Geodetic2D pvl;                         // same point in latlon
	//double distv;						// distance from that point to the camera
	//bool viewIntersectGlobe;
	
	double pitch, roll;					// ángulos de inclinación
    
    //std::vector <ICameraStopEventListener *> listeners;

};

#endif
