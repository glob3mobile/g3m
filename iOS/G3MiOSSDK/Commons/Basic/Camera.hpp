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

#include "Context.hpp"
#include "IFactory.hpp"

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
        return ((float) _viewport[2]) / _viewport[3];
    }

    /*
    * Sets camera params at the beginning of every frame.
	*/
    void Draw(const RenderContext &rc);
    /* 
     * Returns opengl lookat matrix.
     * @param m The array where the data will be stored.
     */
    inline void CopyLookAtMatrix(double m[]) const {
        for (int i = 0; i < 16; i++) m[i] = _lookAt[i];
    }

private:
    //double frustumFactor;

    int _viewport[4];
    double _lookAt[16];            // gluLookAt matrix, computed in CPU in double precision
    float _projection[16];        // opengl projection matrix

    //void ApplyTransform (double M[16]);	
	
    Vector3D _pos;                        // position
    Vector3D _center;                     // center of view
    Vector3D _up;                        // vertical vector
    
    //std::vector <ICameraStopEventListener *> listeners;

};

#endif
