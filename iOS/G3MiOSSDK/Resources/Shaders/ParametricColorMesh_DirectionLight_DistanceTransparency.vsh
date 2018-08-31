//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec3 aNormal;

uniform mat4 uModelview;
uniform mat4 uModel;

uniform float uPointSize;

uniform vec3 uAmbientLightColor;
uniform vec3 uDiffuseLightColor;

uniform vec3 uDiffuseLightDirection; //We must normalize
varying vec3 lightColor;

varying vec4 VertexColor;
varying highp float alpha;
uniform highp float uTransparencyDistanceThreshold;

attribute float aColorValue; //Between 0..1
uniform lowp vec4 uColorAt0;
uniform lowp vec4 uColorAt1;

#define HALF_PI 1.5707963268

#define START_FADING_AT 0.75

void main() {
    
    vec3 vertexInModel = (uModel * aPosition).xyz;
    highp float vertexDist = -vertexInModel.z;
    highp float d = vertexDist / uTransparencyDistanceThreshold;
    
    if (d < START_FADING_AT){
        alpha = 1.0;
    } else{
        if (d > 1.0){
            alpha = 0.0;
        } else{
            d = (d - START_FADING_AT) / (1.0 - START_FADING_AT);
            d = (1.0 + d) * (HALF_PI);
            alpha = sin(d);
        }
    }
    
    vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));
    vec3 lightDirNormalized = normalize( uDiffuseLightDirection );
    
    float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);
    
    gl_Position = uModelview * aPosition;
    
    gl_PointSize = uPointSize;
    
    //Computing Total Light in Vertex
    lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;
    lightColor.x = min(lightColor.x, 1.0);
    lightColor.y = min(lightColor.y, 1.0);
    lightColor.z = min(lightColor.z, 1.0);
    
    //Passing color
    VertexColor = mix(uColorAt0, uColorAt1, aColorValue);
}
