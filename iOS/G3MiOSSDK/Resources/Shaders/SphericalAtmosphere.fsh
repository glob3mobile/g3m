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

highp vec2 intersectionsWithSphere(highp vec3 o,
                                   highp vec3 d,
                                   highp float r){
  //http://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
  
  highp float a = dot(d,d);
  highp float b = 2.0 * dot(o,d);
  highp float c = dot(o,o) - pow(r, 2.0);
  
  highp float q = pow(b,2.0) - 4.0 * a * c;
  if (q < 0.0){
    return vec2(-1.0, -1.0); //No idea how to write NAN in GLSL
  }
  
  highp float sq = sqrt(q);
  highp float t1 = (-b - sq) / (2.0*a);
  highp float t2 = (-b + sq) / (2.0*a);
  
  return vec2(t1,t2);
}


void main() {
  
  //ATM parameters
  const highp float earthRadius = 6.36744e6;
  const highp float atmThickness = 1000e3;
  const highp float atmUndergroundOffset = 100e3;
  
  //Max distance on atmosphere (as in: http://www.mathopenref.com/chord.html )
  const highp float maxDistAtm = 2.0 * sqrt(pow(earthRadius + atmThickness, 2.0) - pow(earthRadius, 2.0));
  
  //Ray [O + tD = X]
  highp vec3 o = planePos;
  highp vec3 d = planePos - uCameraPosition;
  
//  highp vec3 intersection = closestIntersectionWithSphere(o, d, earthRadius - atmUndergroundOffset);
//  highp vec3 intersectionUpper = closestIntersectionWithSphere(o, d, earthRadius + atmThickness);
//  
//  if (intersection.x == 0.0 && intersectionUpper.x != 0.0){
//    gl_FragColor = vec4(39.0 / 256.0, 227.0 / 256.0, 244.0 / 256.0, 1.0); //RED
//  } else{
//    discard;
//  }

  highp vec2 interEarth = intersectionsWithSphere(o,d, earthRadius - atmUndergroundOffset);
  if (interEarth.x != -1.0 || interEarth.y != -1.0){
    discard; 
  }
  
  highp vec2 interAtm = intersectionsWithSphere(o,d, earthRadius + atmThickness);
  if (interAtm.x == -1.0 || interAtm.y == -1.0){
    discard;
  }
  
  if (interAtm.x < 0.0){
    interAtm.x = 0.0;
  }
  
  highp vec3 p1 = o + interAtm.x * d;
  highp vec3 p2 = o + interAtm.y * d;
  highp float dist = distance(p1,p2);
  

  highp vec4 whiteSky = vec4(1.0, 1.0, 1.0, 1.0);
  highp vec4 blueSky = vec4(39.0 / 256.0, 227.0 / 256.0, 244.0 / 256.0, 1.0);
  highp vec4 darkSpace = vec4(0.0, 0.0, 0.0, 0.0);
  
  
  highp float factor = dist / maxDistAtm; //Reflection factor
  if (factor > 1.0){
    factor = 1.0;
  }
  
  gl_FragColor = blueSky * factor + darkSpace * (factor - 1.0);
  
}
