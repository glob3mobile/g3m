//
//  Shader.fsh
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

varying mediump vec2 TextureCoordOut;
uniform mediump vec2 TranslationTexCoord;
uniform mediump vec2 ScaleTexCoord;

varying mediump vec4 VertexColor;

uniform sampler2D Sampler;
uniform bool EnableTexture;
uniform lowp vec4 FlatColor;

uniform bool EnableColorPerVertex;
uniform bool EnableFlatColor;
uniform mediump float FlatColorIntensity;
uniform mediump float ColorPerVertexIntensity;

varying mediump float z_glob;

void main() {
  if (EnableTexture) {
    gl_FragColor = texture2D(Sampler, TextureCoordOut * ScaleTexCoord + TranslationTexCoord);

    if (EnableFlatColor || EnableColorPerVertex){
      lowp vec4 color;
      if (EnableFlatColor) {
        color = FlatColor;
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
        gl_FragColor = gl_FragColor * FlatColor;
      }
    }
    else {
      gl_FragColor = FlatColor;
    }
    
  }
  
  if (z_glob>0.0)
    gl_FragColor = vec4(1.0,1.0,0.0,1.0);
  else
    gl_FragColor = vec4(0.0,0.0,1.0,1.0);
}
