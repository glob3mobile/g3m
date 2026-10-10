#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

uniform vec3 uCameraPosition;
// 1.0 draws the haze in front of the ground (after the planet), 0.0 the sky and the space (before it)
uniform float uGroundHazePass;
// the background colour, painted where the sky is under the planet edge
uniform vec3 uSpaceColor;
// Scattered light tends to this colour instead of white on long paths (horizon, limb); the haze is this colour.
// Set by AtmosphereRenderer (default: the horizon of Google Earth seen from the ground), tinted by its ColorLook
uniform vec3 uHorizonColor;
// Rayleigh scattering of the sky for red, green and blue, in 1e-3 / km; set by AtmosphereRenderer, greyed by its ColorLook
uniform vec3 uSkyRayleighScattering;
varying vec3 rayDirection;

//ATM parameters
const float earthRadius = 6.36744e6;
// WGS84, as EllipsoidalPlanet::createEarth()
const vec3 earthRadii = vec3(6378137.0, 6378137.0, 6356752.314245);

// Air density falls as exp(-height / scaleHeight); the real scale height is 8 km
const float realScaleHeight = 8.0;
// The sky uses air twice as high as the real one, so the halo seen from space is wide;
// the haze over the ground uses the real air, so it does not thicken too fast towards the horizon
const float atmosphereScale = 2.0;
const float skyScaleHeight = realScaleHeight * atmosphereScale;
// the density there is exp(-12): the air above adds nothing visible
const float stratoHeight = 12.0 * skyScaleHeight * 1000.0;
// the sky is drawn under the planet edge too, so no background shows where the tiles fall short of the ellipsoid
const float atmUndergroundOffset = 100e3;

// Rayleigh scattering coefficients at sea level for red, green and blue (680, 550, 440 nm), in 1e-6 / m, that is 1e-3 / km;
// the haze opacity only (the sky gets uSkyRayleighScattering, by default the same values)
const vec3 rayleighScattering = vec3(5.802, 13.558, 33.1) * 1e-3;
// Fitted so the zenith seen from the ground is the one of Google Earth, (59, 89, 138); the sky only
const float skyRayleighScatteringScale = 1.78;

const int opticalDepthSamples = 16;

const vec4 noAir = vec4(0.0, 0.0, 0.0, 0.0);


bool rayIntersectsSphere(vec3 o, vec3 d, float radius,
                         out float tNear,
                         out float tFar) {
  // http://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection

  float a = dot(d,d);
  float b = 2.0 * dot(o,d);
  float c = dot(o,o) - (radius*radius);

  float q = (b*b) - 4.0 * a * c;
  if (q <= 0.0) {
    return false;
  }

  float sq = sqrt(q);
  tNear = (-b - sq) / (2.0*a);
  tFar  = (-b + sq) / (2.0*a);
  return true;
}

bool rayHitsGround(vec3 o, vec3 d, out float tGround) {
  // the ellipsoid is the unit sphere once the space is divided by its radii
  float tFar;
  return rayIntersectsSphere(o / earthRadii, d / earthRadii, 1.0, tGround, tFar) && (tGround > 0.0);
}

// Where the ray hits the ground or, when it misses, where it passes closest to it:
// an antialiased pixel on the horizon can hold some ground although its centre misses it
float groundDistanceAlongRay(vec3 o, vec3 d) {
  float tGround;
  if (rayHitsGround(o, d, tGround)) {
    return tGround;
  }
  vec3 oInUnitSphere = o / earthRadii;
  vec3 dInUnitSphere = d / earthRadii;
  return -dot(oInUnitSphere, dInUnitSphere) / dot(dInUnitSphere, dInUnitSphere);
}

// Height over the ellipsoid along the radius through the point; over the sphere of earthRadius the
// ground would sit 11 km high at the equator and 11 km deep at the poles, and the air with it
float heightOverEllipsoid(vec3 point) {
  float distanceToCenter = length(point);
  float groundRadius = distanceToCenter / length(point / earthRadii);
  return distanceToCenter - groundRadius;
}

float airDensity(vec3 point, float scaleHeight) {
  float heightInKm = heightOverEllipsoid(point) / 1000.0;
  return exp(-heightInKm / scaleHeight);
}

// km of sea-level air along the segment (midpoint rule; no closed form for exponential air)
float opticalDepthInAtmosphere(vec3 p1, vec3 p2, float scaleHeight) {
  vec3 sampleStep = (p2 - p1) / float(opticalDepthSamples);
  float densitySum = 0.0;
  for (int i = 0; i < opticalDepthSamples; i++) {
    densitySum += airDensity(p1 + sampleStep * (float(i) + 0.5), scaleHeight);
  }
  return densitySum * length(sampleStep) / 1000.0;
}

// km of sea-level air in the vertical column from the point to the top
float opticalDepthAbove(vec3 point, float scaleHeight) {
  return scaleHeight * airDensity(point, scaleHeight);
}

vec3 skyExtinction(float opticalDepth) {
  return uSkyRayleighScattering * skyRayleighScatteringScale * opticalDepth;
}

vec3 hazeExtinction(float opticalDepth) {
  return rayleighScattering * opticalDepth;
}

vec3 transmittance(vec3 airExtinction) {
  return exp(-airExtinction);
}

// Same as 1 - transmittance for thin air, but saturates to uHorizonColor
vec3 scatteredLight(vec3 airExtinction) {
  return uHorizonColor * (vec3(1.0) - exp(-airExtinction / uHorizonColor));
}

// Interleaved gradient noise (Jimenez 2014), uniform in [0, 1) and fixed on the screen
float screenNoise() {
  return fract(52.9829189 * fract(dot(gl_FragCoord.xy, vec2(0.06711056, 0.00583715))));
}

// 8 bits have no level between 0 and 1/255: a dark gradient shows as bands with hard edges.
// Half a level of noise mixes the two neighbouring levels so the eye sees the value in between
vec3 dithered(vec3 light) {
  return light + (screenNoise() - 0.5) / 255.0;
}

vec4 sky(vec3 o, vec3 d) {
  float tAtmosphereIn, tAtmosphereOut;
  if (!rayIntersectsSphere(o, d, earthRadius + stratoHeight, tAtmosphereIn, tAtmosphereOut) || (tAtmosphereOut <= 0.0)) {
    return noAir;
  }

  float tUnderground, tUndergroundFar;
  if (rayIntersectsSphere(o, d, earthRadius - atmUndergroundOffset, tUnderground, tUndergroundFar) && (tUnderground > 0.0)) {
    return vec4(uSpaceColor, 1.0);
  }

  float opticalDepth = opticalDepthInAtmosphere(o + d * max(tAtmosphereIn, 0.0), o + d * tAtmosphereOut, skyScaleHeight);
  vec3 airExtinction = skyExtinction(opticalDepth);
  vec3 light = dithered(scatteredLight(airExtinction));

  // opaque where the planet is behind, so no star shows where the tiles fall short of the ellipsoid
  float tGround;
  if (rayHitsGround(o, d, tGround)) {
    return vec4(light, 1.0);
  }
  // blended with one / oneMinusSrcAlpha: light + background * transmittance (one alpha, the transmittance of green)
  return vec4(light, 1.0 - transmittance(airExtinction).g);
}

// Grey fog towards uHorizonColor, blended with srcAlpha / oneMinusSrcAlpha: uHorizonColor * opacity + ground * (1 - opacity).
// One alpha can only attenuate the three channels alike, so the fog uses the transmittance of green (550 nm)
vec4 groundHaze(vec3 o, vec3 d) {
  float tAtmosphereIn, tAtmosphereOut;
  if (!rayIntersectsSphere(o, d, earthRadius + stratoHeight, tAtmosphereIn, tAtmosphereOut)) {
    return noAir;
  }

  float tGround = groundDistanceAlongRay(o, d);
  float tStart = max(tAtmosphereIn, 0.0);
  if (tGround <= tStart) {
    return noAir;
  }

  // only the air beyond the vertical column above the ground point hazes it, so looking down is not veiled
  vec3 groundPoint = o + d * tGround;
  float opticalDepth = max(opticalDepthInAtmosphere(o + d * tStart, groundPoint, realScaleHeight) - opticalDepthAbove(groundPoint, realScaleHeight), 0.0);
  float opacity = 1.0 - transmittance(hazeExtinction(opticalDepth)).g;
  if (opacity <= 0.0) {
    return noAir;
  }
  return vec4(uHorizonColor, opacity);
}

void main() {
  //Ray [O + tD = X]
  vec3 o = uCameraPosition;
  vec3 d = normalize(rayDirection);

  if (uGroundHazePass > 0.5) {
    gl_FragColor = groundHaze(o, d);
  }
  else {
    gl_FragColor = sky(o, d);
  }
}
