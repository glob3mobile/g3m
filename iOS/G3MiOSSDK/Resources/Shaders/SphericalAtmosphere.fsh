//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform highp vec3 uCameraPosition;

varying highp vec3 planePos;

//ATM parameters
const highp float earthRadius = 6.36744e6;

const highp float atmosphereScale = 15.0;
const highp float tropoHeight = 10e3 * atmosphereScale;
const highp float stratoHeight = 50e3 * atmosphereScale;

//const highp float atmThickness = 50e3;
const highp float atmUndergroundOffset = 100e3;

//Height at which the effect is replaced by a blue background
const highp float minHeigth = 20000.0;

//Max distance on atmosphere (as in: http://www.mathopenref.com/chord.html )
const highp float maxDistTropo = 2.0 * sqrt(pow(earthRadius + tropoHeight, 2.0) - pow(earthRadius, 2.0));
const highp float maxDistStrato = 2.0 * sqrt(pow(earthRadius + stratoHeight, 2.0) - pow(earthRadius + tropoHeight, 2.0));

//Multicolor gradient
highp vec4 whiteSky = vec4(1.0, 1.0, 1.0, 1.0);
highp vec4 blueSky = vec4(32.0 / 256.0, 173.0 / 256.0, 249.0 / 256.0, 1.0);
highp vec4 darkSpace = vec4(0.0, 0.0, 0.0, 0.0);


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
  
  if (t1 < t2){
    return vec2(t1,t2);
  } else{
    return vec2(t2, t1);
  }
}

highp float rayLenghtInSphere(highp vec3 o,
                              highp vec3 d,
                              highp float r,
                              out highp vec3 p1,
                              out highp vec3 p2){
  highp vec2 t = intersectionsWithSphere(o,d,r);
  
  if (t.x < 0.0){
    if (t.y < 0.0){
      return 0.0;
    } else{
      t.x = 0.0;
    }
  }
  
  if (t.x < 1.0){ //Eliminating distance to Znear plane
    t.x = 1.0;
  }
  
  p1 = o + d * t.x;
  p2 = o + d * t.y;
  
  return length(p2-p1);
  
}

highp float getRayFactor(highp vec3 o, highp vec3 d){
  
  highp float ld = dot(d,d);
  highp float pdo = dot(d,o);
  
  highp float dx = d.x;
  highp float dy = d.y;
  highp float dz = d.z;
  
  highp float ox = o.x;
  highp float oy = o.y;
  highp float oz = o.z;
  
  highp float s = (-12000. + 2.*earthRadius + ((dx*(dx + ox) + dy*(dy + oy) + dz*(dz + oz))*
                                               sqrt(pow(dx + ox,2.0) + pow(dy + oy,2.0) + pow(dz + oz,2.0)))/ld -
                   (sqrt(pow(ox,2.0) + pow(oy,2.0) + pow(oz,2.0))*pdo)/ld - 2.*stratoHeight +
                   ((pow(dz,2.0)*(pow(ox,2.0) + pow(oy,2.0)) - 2.*dx*dz*ox*oz - 2.*dy*oy*(dx*ox + dz*oz) +
                     pow(dy,2.0)*(pow(ox,2.0) + pow(oz,2.0)) + pow(dx,2.0)*(pow(oy,2.0) + pow(oz,2.0)))*
                    log(dx*(dx + ox) + dy*(dy + oy) + dz*(dz + oz) +
                        sqrt(ld)*sqrt(pow(dx + ox,2.0) + pow(dy + oy,2.0) + pow(dz + oz,2.0))))/pow(ld,1.5) -
                   ((pow(dz,2.0)*(pow(ox,2.0) + pow(oy,2.0)) - 2.*dx*dz*ox*oz - 2.*dy*oy*(dx*ox + dz*oz) +
                     pow(dy,2.0)*(pow(ox,2.0) + pow(oz,2.0)) + pow(dx,2.0)*(pow(oy,2.0) + pow(oz,2.0)))*
                    log(sqrt(ld)*sqrt(pow(ox,2.0) + pow(oy,2.0) + pow(oz,2.0)) + pdo))/pow(ld,1.5))/
  (2.*(earthRadius - 1.*stratoHeight));
  
  //highp float maxLength = 2.0 * sqrt(pow(earthRadius + stratoHeight, 2.0) - pow(earthRadius, 2.0));
  
  return s;
}

void main() {
  
  
  //Ray [O + tD = X]
  highp vec3 o = planePos;
  highp vec3 d = planePos - uCameraPosition;
  
  //Discarding pixels on Earth
  highp vec2 interEarth = intersectionsWithSphere(o,d, earthRadius - atmUndergroundOffset);
  if (interEarth.x != -1.0 || interEarth.y != -1.0){
    discard;
  }
  
  //Ray length in stratosphere
  highp vec3 sp1, sp2;
  highp float stratoLength = rayLenghtInSphere(o,d, earthRadius + stratoHeight, sp1, sp2);
  if (stratoLength <= 0.0){
    discard;
  }

  highp float f = getRayFactor(sp1, sp2 - sp1);
//  if (f <= 0.0 || f >= 1.0){
//    gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
//  } else{
  if (f > 2.5){
    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
  } else{
    gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);
  }
//    gl_FragColor = vec4(f / 10.0, 0.0, 0.1, 1.0);
//  }
  
  
  //Calculating camera Height (for precision problems)
  //Below a certain threshold float precision is not enough for calculations
  const highp float minHeigth = 20000.0;
  highp float camHeight = length(uCameraPosition) - earthRadius;
  gl_FragColor = mix(gl_FragColor, blueSky, smoothstep(minHeigth, minHeigth / 2.0, camHeight));
  
}

