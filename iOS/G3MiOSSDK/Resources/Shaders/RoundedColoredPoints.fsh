//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec4 VertexColor;

uniform lowp vec4 uRoundedPointBorderColor;

void main() {
  
  highp vec2 circCoord = 2.0 * gl_PointCoord - 1.0;
  highp float dist = dot(circCoord, circCoord);
  if (dist > 1.0) {
    discard;
  }
  
  if (dist < 0.8){
    gl_FragColor = VertexColor;
  } else{
    //Border
    //TODO
    gl_FragColor = uRoundedPointBorderColor;
  }
  
}