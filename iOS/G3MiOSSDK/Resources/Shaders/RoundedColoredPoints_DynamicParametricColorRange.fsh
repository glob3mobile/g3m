//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform lowp vec4 uRoundedPointBorderColor;

varying lowp vec4 vertexColor;
varying lowp float currentValue;


void main() {
    
    highp float circleRadius = 0.8 *(1.0 - currentValue)+ 0.2;
    
    highp vec2 circCoord = 2.0 * gl_PointCoord - 1.0;
    highp float dist = dot(circCoord, circCoord);
    if (dist > circleRadius) {
        discard;
    }
    
    if (dist < 0.8 * circleRadius){
        gl_FragColor = vertexColor;
    } else{
        //Border
        gl_FragColor = uRoundedPointBorderColor;
    }
    
}
