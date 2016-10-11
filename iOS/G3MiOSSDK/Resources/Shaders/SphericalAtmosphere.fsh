//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform highp vec3 uCameraPosition;

varying highp vec3 planePos;

highp vec3 closestIntersectionWithSphere(highp vec3 o,
                                         highp vec3 d,
                                         highp float r){
  //http://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
  
  highp float a = dot(d,d);
  highp float b = 2.0 * dot(o,d);
  highp float c = dot(o,o) - pow(r, 2.0);
  
  highp float q = pow(b,2.0) - 4.0 * a * c;
  if (q < 0.0){
    return vec3(0, 0, 0);
  }
  
  
  highp float x = -b - sqrt(q) / (2.0*a);
  
  return o + d * x;
  
}

void main() {
  highp vec3 intersection = closestIntersectionWithSphere(planePos, planePos - uCameraPosition, 6.36744e6 - 200e3);
  highp vec3 intersectionUpper = closestIntersectionWithSphere(planePos, planePos - uCameraPosition, 6.36744e6 + 1000e3);
  
  if (intersection.x == 0.0 && intersectionUpper.x != 0.0){
    gl_FragColor = vec4(39.0 / 256.0, 227.0 / 256.0, 244.0 / 256.0, 1.0); //RED
  } else{
    discard;
  }
}
