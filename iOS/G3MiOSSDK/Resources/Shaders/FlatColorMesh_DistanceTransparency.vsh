//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
uniform mat4 uModelview;
uniform mat4 uModel;

uniform float uPointSize;

varying highp float alpha;
uniform highp float uTransparencyDistanceThreshold;

#define HALF_PI 1.5707963268
#define START_FADING_AT 0.75

void main() {
  gl_Position = uModelview * aPosition;
  gl_PointSize = uPointSize;
    
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
}
