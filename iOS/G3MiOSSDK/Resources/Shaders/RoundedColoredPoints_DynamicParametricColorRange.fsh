//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform lowp vec4 uRoundedPointBorderColor;

varying highp float currentColorValue;

uniform lowp vec4 uColorAt0;
uniform lowp vec4 uColorAt1;

void main() {
    
    highp float circleRadius = 1.0 * (0.8 * currentColorValue + 0.2);
  
  highp vec2 circCoord = 2.0 * gl_PointCoord - 1.0;
  highp float dist = dot(circCoord, circCoord);
  if (dist > circleRadius) {
    discard;
  }
  
  if (dist < 0.8 * circleRadius){
    gl_FragColor = mix(uColorAt0, uColorAt1, currentColorValue);
  } else{
    //Border
    //TODO
    gl_FragColor = uRoundedPointBorderColor;
  }
  
}
