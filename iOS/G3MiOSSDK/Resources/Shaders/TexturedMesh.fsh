//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec2 TextureCoordOut;
varying mediump vec4 VertexColor;

uniform sampler2D Sampler;

#define FOG_THRESHOLD 0.999

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut);


  if (gl_FragCoord.z > FOG_THRESHOLD){
    highp float fog = 1.0 - gl_FragCoord.z / (1.0 - FOG_THRESHOLD);

    gl_FragColor *= fog;
    
  }

}