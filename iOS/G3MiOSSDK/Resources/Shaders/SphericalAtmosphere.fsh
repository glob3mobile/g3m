//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform highp vec3 uCameraPosition;
varying highp vec3 rayDirection;

//ATM parameters
const highp float earthRadius = 6.36744e6;

const highp float atmosphereScale = 15.0;
const highp float stratoHeight = 50e3 * atmosphereScale;
const highp float atmUndergroundOffset = 100e3;

//Height at which the effect is replaced by a blue background
const highp float minHeigth = 35000.0;

//Multicolor gradient
highp vec4 whiteSky = vec4(1.0, 1.0, 1.0, 1.0);
highp vec4 blueSky = vec4(32.0 / 256.0, 173.0 / 256.0, 249.0 / 256.0, 1.0);
highp vec4 darkSpace = vec4(0.0, 0.0, 0.0, 0.0);
highp vec4 groundSkyColor = mix(blueSky, whiteSky, smoothstep(0.0, 1.0, 0.5));


bool intersectionsWithAtmosphere(highp vec3 o, highp vec3 d,
                                 out highp vec3 p1,
                                 out highp vec3 p2){
  // http://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection

  highp float a = dot(d,d);
  highp float b = 2.0 * dot(o,d);
  highp float r = earthRadius - atmUndergroundOffset; //Earth radius
  highp float c = dot(o,o) - (r*r);

  highp float q1 = (b*b) - 4.0 * a * c;

  r = earthRadius + stratoHeight; //Atm. radius
  c = dot(o,o) - (r*r);

  highp float q2 = (b*b) - 4.0 * a * c;
  bool valid = (q1 < 0.0) && (q2 > 0.0);

  if (valid) {
    highp float sq = sqrt(q2);
    highp float t1 = (-b - sq) / (2.0*a);
    highp float t2 = (-b + sq) / (2.0*a);

    if (t1 < 0.0 && t2 < 0.0){
      return false;
    }

    p1 = o + d * max(min(t1,t2), 0.0);
    p2 = o + d * max(t1,t2);
  }

  return valid;
}

highp float getRayFactor(highp vec3 o, highp vec3 d){

  // Ray density calculations explained in: https://github.com/amazingsmash/AtmosphericShaders
  
  
  highp float rl = length(d); //Original Length of the Ray
  d = normalize(d); //Integrating from 0 to LD
  
  highp float scaleFactor = 1.0;
  
  o /= scaleFactor;
  d /= scaleFactor;

  highp float er = earthRadius / scaleFactor;
  highp float sh = (stratoHeight + earthRadius) / scaleFactor;
  rl /= scaleFactor;

  highp float pdo = dot(d,o);

  highp float dx = d.x;
  highp float dy = d.y;
  highp float dz = d.z;

  highp float ox = o.x;
  highp float oy = o.y;
  highp float oz = o.z;

  highp float dox2 = (dx + ox) * (dx + ox);
  highp float doy2 = (dy + oy) * (dy + oy);
  highp float doz2 = (dz + oz) * (dz + oz);

  highp float ox2 = ox * ox;
  highp float oy2 = oy * oy;
  highp float oz2 = oz * oz;

  highp float dx2 = dx * dx;
  highp float dy2 = dy * dy;
  highp float dz2 = dz * dz;
  
  highp float ol2 = ox2 + oy2 + oz2;
  highp float rl2 = rl*rl;
  
  highp float maxRayLength = sqrt((sh*sh)-(er*er));
  highp float er2 = er*er;
  highp float sh2 = sh*sh;
  
  highp float s = (-((sqrt(ol2)*pdo)/(dx2 + dy2 + dz2)) + (pdo/(dx2 + dy2 + dz2) + rl)*sqrt(ol2 + 2.0*pdo*rl + (dx2 + dy2 + dz2)*rl2) - 2.*rl*sh - ((dz2*(ox2 + oy2) - 2.0*dx*dz*ox*oz - 2.0*dy*oy*(dx*ox + dz*oz) + dy2*(ox2 + oz2) + dx2*(oy2 + oz2))*log(sqrt(dx2 + dy2 + dz2)*sqrt(ol2) + pdo))/pow(dx2 + dy2 + dz2,1.5) + ((dz2*(ox2 + oy2) - 2.0*dx*dz*ox*oz - 2.0*dy*oy*(dx*ox + dz*oz) + dy2*(ox2 + oz2) + dx2*(oy2 + oz2))*log(pdo + (dx2 + dy2 + dz2)*rl + sqrt(dx2 + dy2 + dz2)*sqrt(ol2 + 2.0*(dx*ox + dy*oy + dz*oz)*rl + (dx2 + dy2 + dz2)*rl2)))/pow(dx2 + dy2 + dz2,1.5))/(2.*(-1.*maxRayLength*sh + er2*(-0.5*log(-maxRayLength + sh) + 0.5*log(maxRayLength + sh))));

  return s;
}

void main() {
  //Ray [O + tD = X]
  highp vec3 sp1, sp2;
  bool valid = intersectionsWithAtmosphere(uCameraPosition, rayDirection, sp1, sp2);
  if (valid){
    //Calculating color
    highp float f = getRayFactor(sp1, sp2 - sp1);
    
//    highp vec3 scatteringRatios = normalize(vec3(pow(640.0,4.0), pow(530.0,4.0), pow(450.0,4.0)));
//    
//    highp float r = f;
//    gl_FragColor = vec4(r/scatteringRatios.x,
//                        r/scatteringRatios.y,
//                        r/scatteringRatios.z,
//                        1.0);
//    
//    gl_FragColor = clamp(gl_FragColor, 0.0, 1.0);
    
//    if (f > 0.5){
//      gl_FragColor = blueSky;
//    } else{
//      gl_FragColor = darkSpace;
//    }

    highp vec4 color = mix(darkSpace, blueSky, smoothstep(0.0, 1.0, f));
    color = mix(color, whiteSky, smoothstep(0.8, 1.0, f));
    gl_FragColor = color;
    
    //highp vec4 color = blueSky * 2.0 * f;

    //Calculating camera Height (for precision problems)
    //Below a certain threshold float precision is not enough for calculations
    highp float camHeight = length(uCameraPosition) - earthRadius;
    gl_FragColor = mix(color, groundSkyColor, smoothstep(minHeigth, minHeigth / 4.0, camHeight));
  }
  else {
    gl_FragColor = darkSpace;
  }
}
