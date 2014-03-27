//
//  BasicShadersGL2.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/11/13.
//
//

#ifndef G3MiOSSDK_BasicShadersGL2_h
#define G3MiOSSDK_BasicShadersGL2_h

#include "GPUProgramFactory.hpp"

class BasicShadersGL2: public GPUProgramFactory{

public:
  BasicShadersGL2(){
#ifdef C_CODE
    std::string emptyString = "";
#endif
#ifdef JAVA_CODE
    String emptyString = "";
#endif

    GPUProgramSources sourcesBillboard("Billboard",
 emptyString +  
"attribute vec2 aTextureCoord; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform vec4 uBillboardPosition; \n " + 
"uniform vec2 uTextureExtent; \n " + 
"uniform vec2 uViewPortExtent; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * uBillboardPosition; \n " + 
"gl_Position.x += ((aTextureCoord.x - 0.5) * 2.0 * uTextureExtent.x / uViewPortExtent.x) * gl_Position.w; \n " + 
"gl_Position.y -= ((aTextureCoord.y - 0.5) * 2.0 * uTextureExtent.y / uViewPortExtent.y) * gl_Position.w; \n " + 
"TextureCoordOut = aTextureCoord; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"uniform sampler2D Sampler; \n " + 
"void main() { \n " + 
"gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
"} \n ");
    this->add(sourcesBillboard);

    GPUProgramSources sourcesColorMesh("ColorMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec4 aColor; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"varying vec4 VertexColor; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"VertexColor = aColor; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec4 VertexColor; \n " + 
"void main() { \n " + 
"gl_FragColor = VertexColor; \n " + 
"} \n ");
    this->add(sourcesColorMesh);

    GPUProgramSources sourcesDefault("Default",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"attribute vec4 aColor; \n " + 
"uniform mediump vec2 uTranslationTexCoord; \n " + 
"uniform mediump vec2 uScaleTexCoord; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"varying vec4 VertexColor; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord; \n " + 
"VertexColor = aColor; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"varying mediump vec4 VertexColor; \n " + 
"uniform sampler2D Sampler; \n " + 
"uniform bool EnableTexture; \n " + 
"uniform lowp vec4 uFlatColor; \n " + 
"uniform bool EnableColorPerVertex; \n " + 
"uniform bool EnableFlatColor; \n " + 
"uniform mediump float FlatColorIntensity; \n " + 
"uniform mediump float ColorPerVertexIntensity; \n " + 
"void main() { \n " + 
"if (EnableTexture) { \n " + 
"gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
"if (EnableFlatColor || EnableColorPerVertex) { \n " + 
"lowp vec4 color; \n " + 
"if (EnableFlatColor) { \n " + 
"color = uFlatColor; \n " + 
"if (EnableColorPerVertex) { \n " + 
"color = color * VertexColor; \n " + 
"} \n " + 
"} \n " + 
"else { \n " + 
"color = VertexColor; \n " + 
"} \n " + 
"lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0; \n " + 
"gl_FragColor = mix(gl_FragColor, \n " + 
"VertexColor, \n " + 
"intensity); \n " + 
"} \n " + 
"} \n " + 
"else { \n " + 
"if (EnableColorPerVertex) { \n " + 
"gl_FragColor = VertexColor; \n " + 
"if (EnableFlatColor) { \n " + 
"gl_FragColor = gl_FragColor * uFlatColor; \n " + 
"} \n " + 
"} \n " + 
"else { \n " + 
"gl_FragColor = uFlatColor; \n " + 
"} \n " + 
"} \n " + 
"} \n ");
    this->add(sourcesDefault);

    GPUProgramSources sourcesFlatColorMesh("FlatColorMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"uniform lowp vec4 uFlatColor; \n " + 
"void main() { \n " + 
"gl_FragColor = uFlatColor; \n " + 
"} \n ");
    this->add(sourcesFlatColorMesh);

    GPUProgramSources sourcesFlatColorMesh_DirectionLight("FlatColorMesh_DirectionLight",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec3 aNormal; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform mat4 uModel; \n " + 
"uniform float uPointSize; \n " + 
"uniform vec3 uAmbientLightColor; \n " + 
"uniform vec3 uDiffuseLightColor; \n " + 
"uniform vec3 uDiffuseLightDirection; //We must normalize \n " + 
"varying vec3 lightColor; \n " + 
"void main() { \n " + 
"vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) )); \n " + 
"vec3 lightDirNormalized = normalize( uDiffuseLightDirection ); \n " + 
"float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0); \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"gl_PointSize = uPointSize; \n " + 
"lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity; \n " + 
"lightColor.x = min(lightColor.x, 1.0); \n " + 
"lightColor.y = min(lightColor.y, 1.0); \n " + 
"lightColor.z = min(lightColor.z, 1.0); \n " + 
"} \n ",
 emptyString +  
"precision highp float; \n " + 
"uniform lowp vec4 uFlatColor; \n " + 
"varying vec3 lightColor; \n " + 
"void main() { \n " + 
"gl_FragColor.r = uFlatColor.r * lightColor.r; \n " + 
"gl_FragColor.g = uFlatColor.g * lightColor.r; \n " + 
"gl_FragColor.b = uFlatColor.b * lightColor.r; \n " + 
"gl_FragColor.a = uFlatColor.a; \n " + 
"} \n ");
    this->add(sourcesFlatColorMesh_DirectionLight);

    GPUProgramSources sourcesFullTransformedTexCoorMultiTexturedMesh("FullTransformedTexCoorMultiTexturedMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"attribute vec2 aTextureCoord2; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"varying vec2 TextureCoordOut2; \n " + 
"uniform mediump vec2 uTranslationTexCoord; \n " + 
"uniform mediump vec2 uScaleTexCoord; \n " + 
"uniform float uRotationAngleTexCoord; \n " + 
"uniform vec2 uRotationCenterTexCoord; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"float s = sin( uRotationAngleTexCoord ); \n " + 
"float c = cos( uRotationAngleTexCoord ); \n " + 
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord; \n " + 
"TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord; \n " + 
"TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s), \n " + 
"(-TextureCoordOut.x * s) + (TextureCoordOut.y * c)); \n " + 
"TextureCoordOut += uRotationCenterTexCoord; \n " + 
"TextureCoordOut2 = aTextureCoord2; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"varying mediump vec2 TextureCoordOut2; \n " + 
"uniform sampler2D Sampler; \n " + 
"uniform sampler2D Sampler2; \n " + 
"void main() { \n " + 
"mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut); \n " + 
"mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2); \n " + 
"gl_FragColor = tex1 * tex2; \n " + 
"} \n ");
    this->add(sourcesFullTransformedTexCoorMultiTexturedMesh);

    GPUProgramSources sourcesFullTransformedTexCoorTexturedMesh("FullTransformedTexCoorTexturedMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"uniform mediump vec2 uTranslationTexCoord; \n " + 
"uniform mediump vec2 uScaleTexCoord; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"uniform float uRotationAngleTexCoord; \n " + 
"uniform vec2 uRotationCenterTexCoord; \n " + 
"varying vec4 VertexColor; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"float s = sin( uRotationAngleTexCoord ); \n " + 
"float c = cos( uRotationAngleTexCoord ); \n " + 
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord; \n " + 
"TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord; \n " + 
"TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s), \n " + 
"(-TextureCoordOut.x * s) + (TextureCoordOut.y * c)); \n " + 
"TextureCoordOut += uRotationCenterTexCoord; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"varying mediump vec4 VertexColor; \n " + 
"uniform sampler2D Sampler; \n " + 
"void main() { \n " + 
"gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
"} \n ");
    this->add(sourcesFullTransformedTexCoorTexturedMesh);

    GPUProgramSources sourcesMultiTexturedMesh("MultiTexturedMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"attribute vec2 aTextureCoord2; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"varying vec2 TextureCoordOut2; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"TextureCoordOut = aTextureCoord; \n " + 
"TextureCoordOut2 = aTextureCoord2; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"varying mediump vec2 TextureCoordOut2; \n " + 
"uniform sampler2D Sampler; \n " + 
"uniform sampler2D Sampler2; \n " + 
"void main() { \n " + 
"mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut); \n " + 
"mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2); \n " + 
"gl_FragColor = tex1 * tex2; \n " + 
"} \n ");
    this->add(sourcesMultiTexturedMesh);

    GPUProgramSources sourcesNoColorMesh("NoColorMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"void main() { \n " + 
"gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); //RED \n " + 
"} \n ");
    this->add(sourcesNoColorMesh);

    GPUProgramSources sourcesShader("Shader",
 emptyString +  
"attribute vec4 Position; \n " + 
"attribute vec2 TextureCoord; \n " + 
"attribute vec4 Color; \n " + 
"uniform mediump vec2 TranslationTexCoord; \n " + 
"uniform mediump vec2 ScaleTexCoord; \n " + 
"uniform mat4 Projection; \n " + 
"uniform mat4 Modelview; \n " + 
"uniform bool BillBoard; \n " + 
"uniform vec2 TextureExtent; \n " + 
"uniform vec2 ViewPortExtent; \n " + 
"uniform float PointSize; \n " + 
"varying vec4 VertexColor; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"void main() { \n " + 
"gl_Position = Projection * Modelview * Position; \n " + 
"if (BillBoard) { \n " + 
"gl_Position.x += ((TextureCoord.x - 0.5) * 2.0 * TextureExtent.x / ViewPortExtent.x) * gl_Position.w; \n " + 
"gl_Position.y -= ((TextureCoord.y - 0.5) * 2.0 * TextureExtent.y / ViewPortExtent.y) * gl_Position.w; \n " + 
"} \n " + 
"TextureCoordOut = (TextureCoord * ScaleTexCoord) + TranslationTexCoord; \n " + 
"VertexColor = Color; \n " + 
"gl_PointSize = PointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"varying mediump vec4 VertexColor; \n " + 
"uniform sampler2D Sampler; \n " + 
"uniform bool EnableTexture; \n " + 
"uniform lowp vec4 FlatColor; \n " + 
"uniform bool EnableColorPerVertex; \n " + 
"uniform bool EnableFlatColor; \n " + 
"uniform mediump float FlatColorIntensity; \n " + 
"uniform mediump float ColorPerVertexIntensity; \n " + 
"void main() { \n " + 
"if (EnableTexture) { \n " + 
"gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
"if (EnableFlatColor || EnableColorPerVertex) { \n " + 
"lowp vec4 color; \n " + 
"if (EnableFlatColor) { \n " + 
"color = FlatColor; \n " + 
"if (EnableColorPerVertex) { \n " + 
"color = color * VertexColor; \n " + 
"} \n " + 
"} \n " + 
"else { \n " + 
"color = VertexColor; \n " + 
"} \n " + 
"lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0; \n " + 
"gl_FragColor = mix(gl_FragColor, \n " + 
"VertexColor, \n " + 
"intensity); \n " + 
"} \n " + 
"} \n " + 
"else { \n " + 
"if (EnableColorPerVertex) { \n " + 
"gl_FragColor = VertexColor; \n " + 
"if (EnableFlatColor) { \n " + 
"gl_FragColor = gl_FragColor * FlatColor; \n " + 
"} \n " + 
"} \n " + 
"else { \n " + 
"gl_FragColor = FlatColor; \n " + 
"} \n " + 
"} \n " + 
"} \n ");
    this->add(sourcesShader);

    GPUProgramSources sourcesTexturedMesh("TexturedMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"TextureCoordOut = aTextureCoord; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"uniform sampler2D Sampler; \n " + 
"void main() { \n " + 
"gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
"} \n ");
    this->add(sourcesTexturedMesh);

    GPUProgramSources sourcesTexturedMesh_DirectionLight("TexturedMesh_DirectionLight",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"attribute vec3 aNormal; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform mat4 uModel; \n " + 
"uniform float uPointSize; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"uniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED IN SHADER \n " + 
"varying float diffuseLightIntensity; \n " + 
"uniform vec3 uAmbientLightColor; \n " + 
"uniform vec3 uDiffuseLightColor; \n " + 
"varying vec3 lightColor; \n " + 
"void main() { \n " + 
"vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) )); \n " + 
"vec3 lightDirNormalized = normalize( uDiffuseLightDirection ); \n " + 
"float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0); \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"TextureCoordOut = aTextureCoord; \n " + 
"gl_PointSize = uPointSize; \n " + 
"lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity; \n " + 
"lightColor.x = min(lightColor.x, 1.0); \n " + 
"lightColor.y = min(lightColor.y, 1.0); \n " + 
"lightColor.z = min(lightColor.z, 1.0); \n " + 
"} \n ",
 emptyString +  
"precision highp float; \n " + 
"varying mediump vec2 TextureCoordOut; \n " + 
"uniform sampler2D Sampler; \n " + 
"varying vec3 lightColor; \n " + 
"void main() { \n " + 
"vec4 texColor = texture2D(Sampler, TextureCoordOut); \n " + 
"gl_FragColor.r = texColor.r * lightColor.r; \n " + 
"gl_FragColor.g = texColor.g * lightColor.r; \n " + 
"gl_FragColor.b = texColor.b * lightColor.r; \n " + 
"gl_FragColor.a = texColor.a; \n " + 
"} \n ");
    this->add(sourcesTexturedMesh_DirectionLight);

    GPUProgramSources sourcesTransformedTexCoorMultiTexturedMesh("TransformedTexCoorMultiTexturedMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"attribute vec2 aTextureCoord2; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"varying vec2 TextureCoordOut2; \n " + 
"uniform mediump vec2 uTranslationTexCoord; \n " + 
"uniform mediump vec2 uScaleTexCoord; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord; \n " + 
"TextureCoordOut2 = aTextureCoord2; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"varying mediump vec2 TextureCoordOut2; \n " + 
"uniform sampler2D Sampler; \n " + 
"uniform sampler2D Sampler2; \n " + 
"void main() { \n " + 
"mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut); \n " + 
"mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2); \n " + 
"gl_FragColor = tex1 * tex2; \n " + 
"} \n ");
    this->add(sourcesTransformedTexCoorMultiTexturedMesh);

    GPUProgramSources sourcesTransformedTexCoorTexturedMesh("TransformedTexCoorTexturedMesh",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"uniform mediump vec2 uTranslationTexCoord; \n " + 
"uniform mediump vec2 uScaleTexCoord; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform float uPointSize; \n " + 
"varying vec4 VertexColor; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"void main() { \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord; \n " + 
"gl_PointSize = uPointSize; \n " + 
"} \n ",
 emptyString +  
"varying mediump vec2 TextureCoordOut; \n " + 
"varying mediump vec4 VertexColor; \n " + 
"uniform sampler2D Sampler; \n " + 
"void main() { \n " + 
"gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
"} \n ");
    this->add(sourcesTransformedTexCoorTexturedMesh);

    GPUProgramSources sourcesTransformedTexCoorTexturedMesh_DirectionLight("TransformedTexCoorTexturedMesh_DirectionLight",
 emptyString +  
"attribute vec4 aPosition; \n " + 
"attribute vec2 aTextureCoord; \n " + 
"attribute vec3 aNormal; \n " + 
"uniform mat4 uModelview; \n " + 
"uniform mat4 uModel; \n " + 
"uniform float uPointSize; \n " + 
"varying vec2 TextureCoordOut; \n " + 
"uniform mediump vec2 uTranslationTexCoord; \n " + 
"uniform mediump vec2 uScaleTexCoord; \n " + 
"uniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED \n " + 
"varying float diffuseLightIntensity; \n " + 
"uniform vec3 uAmbientLightColor; \n " + 
"uniform vec3 uDiffuseLightColor; \n " + 
"varying vec3 lightColor; \n " + 
"void main() { \n " + 
"vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) )); \n " + 
"vec3 lightDirNormalized = normalize( uDiffuseLightDirection ); \n " + 
"float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0); \n " + 
"gl_Position = uModelview * aPosition; \n " + 
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord; \n " + 
"gl_PointSize = uPointSize; \n " + 
"lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity; \n " + 
"lightColor.x = min(lightColor.x, 1.0); \n " + 
"lightColor.y = min(lightColor.y, 1.0); \n " + 
"lightColor.z = min(lightColor.z, 1.0); \n " + 
"} \n ",
 emptyString +  
"precision highp float; \n " + 
"varying mediump vec2 TextureCoordOut; \n " + 
"uniform sampler2D Sampler; \n " + 
"varying vec3 lightColor; \n " + 
"void main() { \n " + 
"vec4 texColor = texture2D(Sampler, TextureCoordOut); \n " + 
"gl_FragColor.r = texColor.r * lightColor.r; \n " + 
"gl_FragColor.g = texColor.g * lightColor.r; \n " + 
"gl_FragColor.b = texColor.b * lightColor.r; \n " + 
"gl_FragColor.a = texColor.a; \n " + 
"} \n ");
    this->add(sourcesTransformedTexCoorTexturedMesh_DirectionLight);

  }

};
#endif