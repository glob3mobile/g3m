//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
uniform mat4 uModelview;

uniform float uPointSize;

attribute float aColorValue; //Between 0..1
attribute float aColorValueNext; //Between 0..1

varying highp float currentColorValue;
uniform highp float uTime; //Between 0..1

uniform lowp vec4 uColorAt0;
uniform lowp vec4 uColorAt1;
uniform lowp vec4 uColorAt0_5;

varying lowp vec4 vertexColor;

void main() {
    gl_Position = uModelview * aPosition;
    gl_PointSize = uPointSize;
    
    highp float currentValue = mix(aColorValue, aColorValueNext, uTime);
    if (currentValue < 0.5){
        highp float v = currentValue * 2.0;
        vertexColor = mix(uColorAt0, uColorAt0_5, v);
    } else{
        highp float v = (currentValue - 0.5) * 2.0;
        vertexColor = mix(uColorAt0_5, uColorAt1, v);
    }
}
