//
//  Shader.fsh
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

varying mediump vec2 TextureCoordOut;

uniform sampler2D Sampler;
uniform bool EnableTexture;
uniform lowp vec4 FlatColor;

void main() {
  if (EnableTexture) {
    gl_FragColor = texture2D (Sampler, TextureCoordOut);
  }
  else {
    gl_FragColor = FlatColor;
  }
}
