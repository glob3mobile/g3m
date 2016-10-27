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
  //http://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
  
  highp float a = dot(d,d);
  highp float b = 2.0 * dot(o,d);
  highp float r = earthRadius - atmUndergroundOffset; //Earth radius
  highp float c = dot(o,o) - (r*r);
  
  highp float q1 = (b*b) - 4.0 * a * c;
  
  r = earthRadius + stratoHeight; //Atm. radius
  c = dot(o,o) - (r*r);
  
  highp float q2 = (b*b) - 4.0 * a * c;
  bool valid = (q1 < 0.0) && (q2 > 0.0);
  
  if (valid){
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
  
  //Ray density calculations explained in: https://github.com/amazingsmash/AtmosphericShaders
  
  //Scaling the scene down to improve floating point calculations
  d /= 1000.0;
  o /= 1000.0;
  highp float er = earthRadius / 1000.0;
  highp float sh = (stratoHeight + earthRadius) / 1000.0;
  
  highp float ld = dot(d,d);
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
  
  highp float s = (((dx*(dx + ox) + dy*(dy + oy) + dz*(dz + oz))*
    sqrt(dox2 + doy2 + doz2))/ld -
   (sqrt(ox2 + oy2 + oz2)*pdo)/ld - 2.*sh +
   ((dz2*(ox2 + oy2) - 2.0*dx*dz*ox*oz - 2.0*dy*oy*(dx*ox + dz*oz) +
     dy2*(ox2 + oz2) + dx2*(oy2 + oz2))*
    log(dx*(dx + ox) + dy*(dy + oy) + dz*(dz + oz) +
        sqrt(ld)*sqrt(dox2 + doy2 + doz2)))/pow(ld,1.5) -
   ((dz2*(ox2 + oy2) - 2.0*dx*dz*ox*oz - 2.0*dy*oy*(dx*ox + dz*oz) +
     dy2*(ox2 + oz2) + dx2*(oy2 + oz2))*
    log(sqrt(ld)*sqrt(ox2 + oy2 + oz2) + pdo))/pow(ld,1.5))/
  (2.*(er - 1.*sh));
  
  return s;
}

void main() {
  
  //Ray [O + tD = X]
  highp vec3 sp1, sp2;
  bool valid = intersectionsWithAtmosphere(uCameraPosition, rayDirection, sp1, sp2);
  if (!valid){
    gl_FragColor = darkSpace;
  } else{
    //Calculating color
    highp float f = getRayFactor(sp1, sp2 - sp1) * 1.3;
  
    highp vec4 color = mix(darkSpace, blueSky, smoothstep(0.0, 1.0, f));
    color = mix(color, whiteSky, smoothstep(0.7, 1.0, f));
    gl_FragColor = color;

    //Calculating camera Height (for precision problems)
    //Below a certain threshold float precision is not enough for calculations
    highp float camHeight = length(uCameraPosition) - earthRadius;
    gl_FragColor = mix(gl_FragColor, groundSkyColor, smoothstep(minHeigth, minHeigth / 4.0, camHeight));
  }
}

