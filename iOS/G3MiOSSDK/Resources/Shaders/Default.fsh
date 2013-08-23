//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec2 TextureCoordOut;

varying mediump vec4 VertexColor;

uniform sampler2D Sampler;
uniform bool EnableTexture;
uniform lowp vec4 uFlatColor;

uniform bool EnableColorPerVertex;
uniform bool EnableFlatColor;
uniform mediump float FlatColorIntensity;
uniform mediump float ColorPerVertexIntensity;

void main() {
  
  if (EnableTexture) {
    gl_FragColor = texture2D(Sampler, TextureCoordOut);
    
    if (EnableFlatColor || EnableColorPerVertex) {
      lowp vec4 color;
      if (EnableFlatColor) {
        color = uFlatColor;
        if (EnableColorPerVertex) {
          color = color * VertexColor;
        }
      }
      else {
        color = VertexColor;
      }
      
      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;
      gl_FragColor = mix(gl_FragColor,
                         VertexColor,
                         intensity);
    }
  }
  else {
    
    if (EnableColorPerVertex) {
      gl_FragColor = VertexColor;
      if (EnableFlatColor) {
        gl_FragColor = gl_FragColor * uFlatColor;
      }
    }
    else {
      gl_FragColor = uFlatColor;
    }
    
  }
  
}