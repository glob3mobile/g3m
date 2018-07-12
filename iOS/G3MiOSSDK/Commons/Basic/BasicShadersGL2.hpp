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

    GPUProgramSources sourcesParametricColorRangeMesh("ParametricColorRangeMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"attribute float aColorValue;\n" +
"varying highp float colorValue;\n" +
"uniform lowp vec4 uColorAt0;\n" +
"uniform lowp vec4 uColorAt1;\n" +
"varying highp float colorValue;\n" +
"varying lowp vec4 vertexColor;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"colorValue = aColorValue;\n" +
"highp float currentValue = mix(aColorValue, aColorValueNext, uTime);\n" +
"vertexColor = mix(uColorAt0, uColorAt1, aColorValue);\n" +
"}\n",
 emptyString +  
"varying lowp vec4 vertexColor;\n" +
"void main() {\n" +
"gl_FragColor = mix(uColorAt0, uColorAt1, colorValue);\n" +
"}\n");
    this->add(sourcesParametricColorRangeMesh);

    GPUProgramSources sourcesColorMesh("ColorMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec4 aColor;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"varying vec4 VertexColor;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"VertexColor = aColor;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec4 VertexColor;\n" +
"void main() {\n" +
"if (VertexColor.a > 0.0){\n" +
"gl_FragColor = VertexColor;\n" +
"} else{\n" +
"discard;\n" +
"}\n" +
"}\n");
    this->add(sourcesColorMesh);

    GPUProgramSources sourcesFullTransformedTexCoorMultiTexturedMesh("FullTransformedTexCoorMultiTexturedMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"attribute vec2 aTextureCoord2;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"varying vec2 TextureCoordOut;\n" +
"varying vec2 TextureCoordOut2;\n" +
"uniform mediump vec2 uTranslationTexCoord;\n" +
"uniform mediump vec2 uScaleTexCoord;\n" +
"uniform float uRotationAngleTexCoord;\n" +
"uniform vec2 uRotationCenterTexCoord;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"float s = sin( uRotationAngleTexCoord );\n" +
"float c = cos( uRotationAngleTexCoord );\n" +
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
"TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;\n" +
"TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),\n" +
"(-TextureCoordOut.x * s) + (TextureCoordOut.y * c));\n" +
"TextureCoordOut += uRotationCenterTexCoord;\n" +
"TextureCoordOut2 = aTextureCoord2;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"varying mediump vec2 TextureCoordOut2;\n" +
"uniform sampler2D Sampler;\n" +
"uniform sampler2D Sampler2;\n" +
"void main() {\n" +
"mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
"mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
"gl_FragColor = tex1 * tex2;\n" +
"}\n");
    this->add(sourcesFullTransformedTexCoorMultiTexturedMesh);

    GPUProgramSources sourcesTransformedTexCoorTexturedMesh("TransformedTexCoorTexturedMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"uniform mediump vec2 uTranslationTexCoord;\n" +
"uniform mediump vec2 uScaleTexCoord;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"varying vec4 VertexColor;\n" +
"varying vec2 TextureCoordOut;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"varying mediump vec4 VertexColor;\n" +
"uniform sampler2D Sampler;\n" +
"void main() {\n" +
"gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
"}\n");
    this->add(sourcesTransformedTexCoorTexturedMesh);

    GPUProgramSources sourcesFlatColorMesh_DirectionLight("FlatColorMesh_DirectionLight",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec3 aNormal;\n" +
"uniform mat4 uModelview;\n" +
"uniform mat4 uModel;\n" +
"uniform float uPointSize;\n" +
"uniform vec3 uAmbientLightColor;\n" +
"uniform vec3 uDiffuseLightColor;\n" +
"uniform vec3 uDiffuseLightDirection; //We must normalize\n" +
"varying vec3 lightColor;\n" +
"void main() {\n" +
"vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
"vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
"float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
"lightColor.x = min(lightColor.x, 1.0);\n" +
"lightColor.y = min(lightColor.y, 1.0);\n" +
"lightColor.z = min(lightColor.z, 1.0);\n" +
"}\n",
 emptyString +  
"#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
"precision highp float;\n" +
"#else\n" +
"precision mediump float;\n" +
"#endif\n" +
"uniform lowp vec4 uFlatColor;\n" +
"varying vec3 lightColor;\n" +
"void main() {\n" +
"gl_FragColor.r = uFlatColor.r * lightColor.r;\n" +
"gl_FragColor.g = uFlatColor.g * lightColor.r;\n" +
"gl_FragColor.b = uFlatColor.b * lightColor.r;\n" +
"gl_FragColor.a = uFlatColor.a;\n" +
"}\n");
    this->add(sourcesFlatColorMesh_DirectionLight);

    GPUProgramSources sourcesTransformedTexCoorMultiTexturedMesh("TransformedTexCoorMultiTexturedMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"attribute vec2 aTextureCoord2;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"varying vec2 TextureCoordOut;\n" +
"varying vec2 TextureCoordOut2;\n" +
"uniform mediump vec2 uTranslationTexCoord;\n" +
"uniform mediump vec2 uScaleTexCoord;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
"TextureCoordOut2 = aTextureCoord2;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"varying mediump vec2 TextureCoordOut2;\n" +
"uniform sampler2D Sampler;\n" +
"uniform sampler2D Sampler2;\n" +
"void main() {\n" +
"mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
"mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
"gl_FragColor = tex1 * tex2;\n" +
"}\n");
    this->add(sourcesTransformedTexCoorMultiTexturedMesh);

    GPUProgramSources sourcesTexturedMesh("TexturedMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"varying vec2 TextureCoordOut;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"TextureCoordOut = aTextureCoord;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"uniform sampler2D Sampler;\n" +
"void main() {\n" +
"lowp vec4 color = texture2D(Sampler, TextureCoordOut);\n" +
"if (color.a > 0.0){\n" +
"gl_FragColor = color;\n" +
"} else{\n" +
"discard;\n" +
"}\n" +
"}\n");
    this->add(sourcesTexturedMesh);

    GPUProgramSources sourcesDynamicParametricColorRange3Mesh("DynamicParametricColorRange3Mesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"attribute float aColorValue; //Between 0..1\n" +
"attribute float aColorValueNext; //Between 0..1\n" +
"varying highp float currentColorValue;\n" +
"uniform highp float uTime; //Between 0..1\n" +
"uniform lowp vec4 uColorAt0;\n" +
"uniform lowp vec4 uColorAt1;\n" +
"uniform lowp vec4 uColorAt0_5;\n" +
"varying lowp vec4 vertexColor;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"highp float currentValue = mix(aColorValue, aColorValueNext, uTime);\n" +
"if (currentValue < 0.5){\n" +
"highp float v = currentValue * 2.0;\n" +
"vertexColor = mix(uColorAt0, uColorAt0_5, v);\n" +
"} else{\n" +
"highp float v = (currentValue - 0.5) * 2.0;\n" +
"vertexColor = mix(uColorAt0_5, uColorAt1, v);\n" +
"}\n" +
"}\n",
 emptyString +  
"varying lowp vec4 vertexColor;\n" +
"void main() {\n" +
"gl_FragColor = vertexColor;\n" +
"}\n");
    this->add(sourcesDynamicParametricColorRange3Mesh);

    GPUProgramSources sourcesDynamicParametricColorRangeMesh("DynamicParametricColorRangeMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"attribute float aColorValue; //Between 0..1\n" +
"attribute float aColorValueNext; //Between 0..1\n" +
"varying highp float currentColorValue;\n" +
"uniform highp float uTime; //Between 0..1\n" +
"uniform lowp vec4 uColorAt0;\n" +
"uniform lowp vec4 uColorAt1;\n" +
"varying highp vec4 vertexColor;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"highp float v = mix(aColorValue, aColorValueNext, uTime);\n" +
"vertexColor = mix(uColorAt0, uColorAt1, v);\n" +
"}\n",
 emptyString +  
"varying highp vec4 vertexColor;\n" +
"void main() {\n" +
"gl_FragColor = vertexColor;\n" +
"}\n");
    this->add(sourcesDynamicParametricColorRangeMesh);

    GPUProgramSources sourcesBillboard_TransformedTexCoor("Billboard_TransformedTexCoor",
 emptyString +  
"attribute vec2 aTextureCoord;\n" +
"uniform mat4 uModelview;\n" +
"uniform vec4 uBillboardPosition;\n" +
"uniform vec2 uBillboardAnchor; //Anchor in UV (texture-like) coordinates\n" +
"uniform vec2 uTextureExtent;\n" +
"uniform vec2 uViewPortExtent;\n" +
"uniform mediump vec2 uTranslationTexCoord;\n" +
"uniform mediump vec2 uScaleTexCoord;\n" +
"varying vec2 TextureCoordOut;\n" +
"void main() {\n" +
"gl_Position = uModelview * uBillboardPosition;\n" +
"float fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;\n" +
"float fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;\n" +
"gl_Position.x += ((aTextureCoord.x - 0.5) - (uBillboardAnchor.x - 0.5)) * fx;\n" +
"gl_Position.y -= ((aTextureCoord.y - 0.5) - (uBillboardAnchor.y - 0.5)) * fy;\n" +
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"uniform sampler2D Sampler;\n" +
"void main() {\n" +
"gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
"}\n");
    this->add(sourcesBillboard_TransformedTexCoor);

    GPUProgramSources sourcesFlatColor2DMesh("FlatColor2DMesh",
 emptyString +  
"attribute vec2 aPosition2D;\n" +
"uniform float uPointSize;\n" +
"uniform vec2 uTranslation2D;\n" +
"uniform vec2 uViewPortExtent;\n" +
"void main() {\n" +
"vec2 pixel = aPosition2D;\n" +
"pixel.x -= uViewPortExtent.x / 2.0;\n" +
"pixel.y += uViewPortExtent.y / 2.0;\n" +
"gl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),\n" +
"(pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),\n" +
"0, 1);\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"uniform lowp vec4 uFlatColor;\n" +
"void main() {\n" +
"gl_FragColor = uFlatColor;\n" +
"}\n");
    this->add(sourcesFlatColor2DMesh);

    GPUProgramSources sourcesMultiTexturedMesh("MultiTexturedMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"attribute vec2 aTextureCoord2;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"varying vec2 TextureCoordOut;\n" +
"varying vec2 TextureCoordOut2;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"TextureCoordOut = aTextureCoord;\n" +
"TextureCoordOut2 = aTextureCoord2;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"varying mediump vec2 TextureCoordOut2;\n" +
"uniform sampler2D Sampler;\n" +
"uniform sampler2D Sampler2;\n" +
"void main() {\n" +
"mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
"mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
"gl_FragColor = tex1 * tex2;\n" +
"}\n");
    this->add(sourcesMultiTexturedMesh);

    GPUProgramSources sourcesRoundedColoredPoints_DynamicParametricColorRange3("RoundedColoredPoints_DynamicParametricColorRange3",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec4 aColor;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"attribute float aColorValue; //Between 0..1\n" +
"attribute float aColorValueNext; //Between 0..1\n" +
"uniform highp float uTime; //Between 0..1\n" +
"uniform lowp vec4 uColorAt0;\n" +
"uniform lowp vec4 uColorAt1;\n" +
"uniform lowp vec4 uColorAt0_5;\n" +
"varying lowp vec4 vertexColor;\n" +
"varying highp float currentValue;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"currentValue = mix(aColorValue, aColorValueNext, uTime);\n" +
"if (currentValue < 0.5){\n" +
"highp float v = currentValue * 2.0;\n" +
"vertexColor = mix(uColorAt0, uColorAt0_5, v);\n" +
"} else{\n" +
"highp float v = (currentValue - 0.5) * 2.0;\n" +
"vertexColor = mix(uColorAt0_5, uColorAt1, v);\n" +
"}\n" +
"}\n",
 emptyString +  
"uniform lowp vec4 uRoundedPointBorderColor;\n" +
"varying lowp vec4 vertexColor;\n" +
"varying highp float currentValue;\n" +
"void main() {\n" +
"highp float circleRadius = 0.8 *(1.0 - currentValue)+ 0.2;\n" +
"highp vec2 circCoord = 2.0 * gl_PointCoord - 1.0;\n" +
"highp float dist = dot(circCoord, circCoord);\n" +
"if (dist > circleRadius) {\n" +
"discard;\n" +
"}\n" +
"if (dist < 0.8 * circleRadius){\n" +
"gl_FragColor = vertexColor;\n" +
"} else{\n" +
"gl_FragColor = uRoundedPointBorderColor;\n" +
"}\n" +
"}\n");
    this->add(sourcesRoundedColoredPoints_DynamicParametricColorRange3);

    GPUProgramSources sourcesFullTransformedTexCoorTexturedMesh("FullTransformedTexCoorTexturedMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"uniform mediump vec2 uTranslationTexCoord;\n" +
"uniform mediump vec2 uScaleTexCoord;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"uniform float uRotationAngleTexCoord;\n" +
"uniform vec2 uRotationCenterTexCoord;\n" +
"varying vec4 VertexColor;\n" +
"varying vec2 TextureCoordOut;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"float s = sin( uRotationAngleTexCoord );\n" +
"float c = cos( uRotationAngleTexCoord );\n" +
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
"TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;\n" +
"TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),\n" +
"(-TextureCoordOut.x * s) + (TextureCoordOut.y * c));\n" +
"TextureCoordOut += uRotationCenterTexCoord;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"varying mediump vec4 VertexColor;\n" +
"uniform sampler2D Sampler;\n" +
"void main() {\n" +
"gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
"}\n");
    this->add(sourcesFullTransformedTexCoorTexturedMesh);

    GPUProgramSources sourcesTransformedTexCoorTexturedMesh_DirectionLight("TransformedTexCoorTexturedMesh_DirectionLight",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"attribute vec3 aNormal;\n" +
"uniform mat4 uModelview;\n" +
"uniform mat4 uModel;\n" +
"uniform float uPointSize;\n" +
"varying vec2 TextureCoordOut;\n" +
"uniform mediump vec2 uTranslationTexCoord;\n" +
"uniform mediump vec2 uScaleTexCoord;\n" +
"uniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED\n" +
"varying float diffuseLightIntensity;\n" +
"uniform vec3 uAmbientLightColor;\n" +
"uniform vec3 uDiffuseLightColor;\n" +
"varying vec3 lightColor;\n" +
"void main() {\n" +
"vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
"vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
"float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
"gl_Position = uModelview * aPosition;\n" +
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
"gl_PointSize = uPointSize;\n" +
"lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
"lightColor.x = min(lightColor.x, 1.0);\n" +
"lightColor.y = min(lightColor.y, 1.0);\n" +
"lightColor.z = min(lightColor.z, 1.0);\n" +
"}\n",
 emptyString +  
"#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
"precision highp float;\n" +
"#else\n" +
"precision mediump float;\n" +
"#endif\n" +
"varying mediump vec2 TextureCoordOut;\n" +
"uniform sampler2D Sampler;\n" +
"varying vec3 lightColor;\n" +
"void main() {\n" +
"vec4 texColor = texture2D(Sampler, TextureCoordOut);\n" +
"gl_FragColor.r = texColor.r * lightColor.r;\n" +
"gl_FragColor.g = texColor.g * lightColor.r;\n" +
"gl_FragColor.b = texColor.b * lightColor.r;\n" +
"gl_FragColor.a = texColor.a;\n" +
"}\n");
    this->add(sourcesTransformedTexCoorTexturedMesh_DirectionLight);

    GPUProgramSources sourcesTexturedMesh_DirectionLight("TexturedMesh_DirectionLight",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"attribute vec3 aNormal;\n" +
"uniform mat4 uModelview;\n" +
"uniform mat4 uModel;\n" +
"uniform float uPointSize;\n" +
"varying vec2 TextureCoordOut;\n" +
"uniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED IN SHADER\n" +
"varying float diffuseLightIntensity;\n" +
"uniform vec3 uAmbientLightColor;\n" +
"uniform vec3 uDiffuseLightColor;\n" +
"varying vec3 lightColor;\n" +
"void main() {\n" +
"vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
"vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
"float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
"gl_Position = uModelview * aPosition;\n" +
"TextureCoordOut = aTextureCoord;\n" +
"gl_PointSize = uPointSize;\n" +
"lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
"lightColor.x = min(lightColor.x, 1.0);\n" +
"lightColor.y = min(lightColor.y, 1.0);\n" +
"lightColor.z = min(lightColor.z, 1.0);\n" +
"}\n",
 emptyString +  
"#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
"precision highp float;\n" +
"#else\n" +
"precision mediump float;\n" +
"#endif\n" +
"varying mediump vec2 TextureCoordOut;\n" +
"uniform sampler2D Sampler;\n" +
"varying vec3 lightColor;\n" +
"void main() {\n" +
"vec4 texColor = texture2D(Sampler, TextureCoordOut);\n" +
"gl_FragColor.r = texColor.r * lightColor.r;\n" +
"gl_FragColor.g = texColor.g * lightColor.r;\n" +
"gl_FragColor.b = texColor.b * lightColor.r;\n" +
"gl_FragColor.a = texColor.a;\n" +
"}\n");
    this->add(sourcesTexturedMesh_DirectionLight);

    GPUProgramSources sourcesShader("Shader",
 emptyString +  
"attribute vec4 Position;\n" +
"attribute vec2 TextureCoord;\n" +
"attribute vec4 Color;\n" +
"uniform mediump vec2 TranslationTexCoord;\n" +
"uniform mediump vec2 ScaleTexCoord;\n" +
"uniform mat4 Projection;\n" +
"uniform mat4 Modelview;\n" +
"uniform bool BillBoard;\n" +
"uniform vec2 TextureExtent;\n" +
"uniform vec2 ViewPortExtent;\n" +
"uniform float PointSize;\n" +
"varying vec4 VertexColor;\n" +
"varying vec2 TextureCoordOut;\n" +
"void main() {\n" +
"gl_Position = Projection * Modelview * Position;\n" +
"if (BillBoard) {\n" +
"gl_Position.x += ((TextureCoord.x - 0.5) * 2.0 * TextureExtent.x / ViewPortExtent.x) * gl_Position.w;\n" +
"gl_Position.y -= ((TextureCoord.y - 0.5) * 2.0 * TextureExtent.y / ViewPortExtent.y) * gl_Position.w;\n" +
"}\n" +
"TextureCoordOut = (TextureCoord * ScaleTexCoord) + TranslationTexCoord;\n" +
"VertexColor = Color;\n" +
"gl_PointSize = PointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"varying mediump vec4 VertexColor;\n" +
"uniform sampler2D Sampler;\n" +
"uniform bool EnableTexture;\n" +
"uniform lowp vec4 FlatColor;\n" +
"uniform bool EnableColorPerVertex;\n" +
"uniform bool EnableFlatColor;\n" +
"uniform mediump float FlatColorIntensity;\n" +
"uniform mediump float ColorPerVertexIntensity;\n" +
"void main() {\n" +
"if (EnableTexture) {\n" +
"gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
"if (EnableFlatColor || EnableColorPerVertex) {\n" +
"lowp vec4 color;\n" +
"if (EnableFlatColor) {\n" +
"color = FlatColor;\n" +
"if (EnableColorPerVertex) {\n" +
"color = color * VertexColor;\n" +
"}\n" +
"}\n" +
"else {\n" +
"color = VertexColor;\n" +
"}\n" +
"lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\n" +
"gl_FragColor = mix(gl_FragColor,\n" +
"VertexColor,\n" +
"intensity);\n" +
"}\n" +
"}\n" +
"else {\n" +
"if (EnableColorPerVertex) {\n" +
"gl_FragColor = VertexColor;\n" +
"if (EnableFlatColor) {\n" +
"gl_FragColor = gl_FragColor * FlatColor;\n" +
"}\n" +
"}\n" +
"else {\n" +
"gl_FragColor = FlatColor;\n" +
"}\n" +
"}\n" +
"}\n");
    this->add(sourcesShader);

    GPUProgramSources sourcesParametricColorRange3Mesh("ParametricColorRange3Mesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"attribute float aColorValue;\n" +
"varying highp float colorValue;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"colorValue = aColorValue;\n" +
"if (aColorValue < 0.5){\n" +
"highp float v = aColorValue * 2.0;\n" +
"vertexColor = mix(uColorAt0, uColorAt0_5, v);\n" +
"} else{\n" +
"highp float v = (aColorValue - 0.5) * 2.0;\n" +
"vertexColor = mix(uColorAt0_5, uColorAt1, v);\n" +
"}\n" +
"}\n",
 emptyString +  
"varying lowp vec4 vertexColor;\n" +
"void main() {\n" +
"gl_FragColor = vertexColor;\n" +
"}\n");
    this->add(sourcesParametricColorRange3Mesh);

    GPUProgramSources sourcesColorMesh_DirectionLight("ColorMesh_DirectionLight",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec3 aNormal;\n" +
"uniform mat4 uModelview;\n" +
"uniform mat4 uModel;\n" +
"uniform float uPointSize;\n" +
"uniform vec3 uAmbientLightColor;\n" +
"uniform vec3 uDiffuseLightColor;\n" +
"uniform vec3 uDiffuseLightDirection; //We must normalize\n" +
"varying vec3 lightColor;\n" +
"attribute vec4 aColor;\n" +
"varying vec4 VertexColor;\n" +
"void main() {\n" +
"vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
"vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
"float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
"lightColor.x = min(lightColor.x, 1.0);\n" +
"lightColor.y = min(lightColor.y, 1.0);\n" +
"lightColor.z = min(lightColor.z, 1.0);\n" +
"VertexColor = aColor;\n" +
"}\n",
 emptyString +  
"precision highp float;\n" +
"varying vec4 VertexColor;\n" +
"varying vec3 lightColor;\n" +
"void main() {\n" +
"if (VertexColor.a <= 0.0){\n" +
"discard;\n" +
"} else{\n" +
"gl_FragColor.r = VertexColor.r * lightColor.r;\n" +
"gl_FragColor.g = VertexColor.g * lightColor.r;\n" +
"gl_FragColor.b = VertexColor.b * lightColor.r;\n" +
"gl_FragColor.a = VertexColor.a;\n" +
"}\n" +
"}\n");
    this->add(sourcesColorMesh_DirectionLight);

    GPUProgramSources sourcesColorMesh_DirectionLight_DistanceTransparency("ColorMesh_DirectionLight_DistanceTransparency",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec3 aNormal;\n" +
"uniform mat4 uModelview;\n" +
"uniform mat4 uModel;\n" +
"uniform float uPointSize;\n" +
"uniform vec3 uAmbientLightColor;\n" +
"uniform vec3 uDiffuseLightColor;\n" +
"uniform vec3 uDiffuseLightDirection; //We must normalize\n" +
"varying vec3 lightColor;\n" +
"attribute vec4 aColor;\n" +
"varying vec4 VertexColor;\n" +
"varying highp float alpha;\n" +
"uniform highp float uTransparencyDistanceThreshold;\n" +
"#define HALF_PI 1.5707963268\n" +
"#define START_FADING_AT 0.75\n" +
"void main() {\n" +
"vec3 vertexInModel = (uModel * aPosition).xyz;\n" +
"highp float vertexDist = -vertexInModel.z;\n" +
"highp float d = vertexDist / uTransparencyDistanceThreshold;\n" +
"if (d < START_FADING_AT){\n" +
"alpha = 1.0;\n" +
"} else{\n" +
"if (d > 1.0){\n" +
"alpha = 0.0;\n" +
"} else{\n" +
"d = (d - START_FADING_AT) / (1.0 - START_FADING_AT);\n" +
"d = (1.0 + d) * (HALF_PI);\n" +
"alpha = sin(d);\n" +
"}\n" +
"}\n" +
"vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
"vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
"float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
"lightColor.x = min(lightColor.x, 1.0);\n" +
"lightColor.y = min(lightColor.y, 1.0);\n" +
"lightColor.z = min(lightColor.z, 1.0);\n" +
"VertexColor = aColor;\n" +
"}\n",
 emptyString +  
"precision highp float;\n" +
"varying vec4 VertexColor;\n" +
"varying vec3 lightColor;\n" +
"varying highp float alpha;\n" +
"void main() {\n" +
"if (alpha <= 0.0){\n" +
"discard;\n" +
"}\n" +
"gl_FragColor.r = VertexColor.r * lightColor.r;\n" +
"gl_FragColor.g = VertexColor.g * lightColor.g;\n" +
"gl_FragColor.b = VertexColor.b * lightColor.b;\n" +
"gl_FragColor.a = VertexColor.a * alpha;\n" +
"}\n");
    this->add(sourcesColorMesh_DirectionLight_DistanceTransparency);

    GPUProgramSources sourcesNoColorMesh("NoColorMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"void main() {\n" +
"gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); //RED\n" +
"}\n");
    this->add(sourcesNoColorMesh);

    GPUProgramSources sourcesRoundedColoredPoints("RoundedColoredPoints",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec4 aColor;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"varying vec4 VertexColor;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"VertexColor = aColor;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec4 VertexColor;\n" +
"uniform lowp vec4 uRoundedPointBorderColor;\n" +
"void main() {\n" +
"highp vec2 circCoord = 2.0 * gl_PointCoord - 1.0;\n" +
"highp float dist = dot(circCoord, circCoord);\n" +
"if (dist > 1.0) {\n" +
"discard;\n" +
"}\n" +
"if (dist < 0.8){\n" +
"gl_FragColor = VertexColor;\n" +
"} else{\n" +
"gl_FragColor = uRoundedPointBorderColor;\n" +
"}\n" +
"}\n");
    this->add(sourcesRoundedColoredPoints);

    GPUProgramSources sourcesTextured2DMesh("Textured2DMesh",
 emptyString +  
"attribute vec2 aPosition2D;\n" +
"attribute vec2 aTextureCoord;\n" +
"uniform float uPointSize;\n" +
"varying vec2 TextureCoordOut;\n" +
"uniform vec2 uTranslation2D;\n" +
"uniform vec2 uViewPortExtent;\n" +
"void main() {\n" +
"vec2 pixel = aPosition2D;\n" +
"pixel.x -= uViewPortExtent.x / 2.0;\n" +
"pixel.y += uViewPortExtent.y / 2.0;\n" +
"gl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),\n" +
"(pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),\n" +
"0, 1);\n" +
"TextureCoordOut = aTextureCoord;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"uniform sampler2D Sampler;\n" +
"void main() {\n" +
"gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
"}\n");
    this->add(sourcesTextured2DMesh);

    GPUProgramSources sourcesRoundedColoredPoints_DynamicParametricColorRange("RoundedColoredPoints_DynamicParametricColorRange",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec4 aColor;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"attribute float aColorValue; //Between 0..1\n" +
"attribute float aColorValueNext; //Between 0..1\n" +
"uniform highp float uTime; //Between 0..1\n" +
"uniform lowp vec4 uColorAt0;\n" +
"uniform lowp vec4 uColorAt1;\n" +
"varying lowp vec4 vertexColor;\n" +
"varying lowp float currentValue;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"currentValue = mix(aColorValue, aColorValueNext, uTime);\n" +
"vertexColor = mix(uColorAt0, uColorAt1, currentValue);\n" +
"}\n",
 emptyString +  
"uniform lowp vec4 uRoundedPointBorderColor;\n" +
"varying lowp vec4 vertexColor;\n" +
"varying lowp float currentValue;\n" +
"void main() {\n" +
"highp float circleRadius = 0.8 *(1.0 - currentValue)+ 0.2;\n" +
"highp vec2 circCoord = 2.0 * gl_PointCoord - 1.0;\n" +
"highp float dist = dot(circCoord, circCoord);\n" +
"if (dist > circleRadius) {\n" +
"discard;\n" +
"}\n" +
"if (dist < 0.8 * circleRadius){\n" +
"gl_FragColor = vertexColor;\n" +
"} else{\n" +
"gl_FragColor = uRoundedPointBorderColor;\n" +
"}\n" +
"}\n");
    this->add(sourcesRoundedColoredPoints_DynamicParametricColorRange);

    GPUProgramSources sourcesFlatColorMesh("FlatColorMesh",
 emptyString +  
"attribute vec4 aPosition;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"uniform lowp vec4 uFlatColor;\n" +
"void main() {\n" +
"gl_FragColor = uFlatColor;\n" +
"}\n");
    this->add(sourcesFlatColorMesh);

    GPUProgramSources sourcesDefault("Default",
 emptyString +  
"attribute vec4 aPosition;\n" +
"attribute vec2 aTextureCoord;\n" +
"attribute vec4 aColor;\n" +
"uniform mediump vec2 uTranslationTexCoord;\n" +
"uniform mediump vec2 uScaleTexCoord;\n" +
"uniform mat4 uModelview;\n" +
"uniform float uPointSize;\n" +
"varying vec4 VertexColor;\n" +
"varying vec2 TextureCoordOut;\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
"VertexColor = aColor;\n" +
"gl_PointSize = uPointSize;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"varying mediump vec4 VertexColor;\n" +
"uniform sampler2D Sampler;\n" +
"uniform bool EnableTexture;\n" +
"uniform lowp vec4 uFlatColor;\n" +
"uniform bool EnableColorPerVertex;\n" +
"uniform bool EnableFlatColor;\n" +
"uniform mediump float FlatColorIntensity;\n" +
"uniform mediump float ColorPerVertexIntensity;\n" +
"void main() {\n" +
"if (EnableTexture) {\n" +
"gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
"if (EnableFlatColor || EnableColorPerVertex) {\n" +
"lowp vec4 color;\n" +
"if (EnableFlatColor) {\n" +
"color = uFlatColor;\n" +
"if (EnableColorPerVertex) {\n" +
"color = color * VertexColor;\n" +
"}\n" +
"}\n" +
"else {\n" +
"color = VertexColor;\n" +
"}\n" +
"lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\n" +
"gl_FragColor = mix(gl_FragColor,\n" +
"VertexColor,\n" +
"intensity);\n" +
"}\n" +
"}\n" +
"else {\n" +
"if (EnableColorPerVertex) {\n" +
"gl_FragColor = VertexColor;\n" +
"if (EnableFlatColor) {\n" +
"gl_FragColor = gl_FragColor * uFlatColor;\n" +
"}\n" +
"}\n" +
"else {\n" +
"gl_FragColor = uFlatColor;\n" +
"}\n" +
"}\n" +
"}\n");
    this->add(sourcesDefault);

    GPUProgramSources sourcesBillboard("Billboard",
 emptyString +  
"attribute vec2 aTextureCoord;\n" +
"uniform mat4 uModelview;\n" +
"uniform vec4 uBillboardPosition;\n" +
"uniform vec2 uBillboardAnchor; //Anchor in UV (texture-like) coordinates\n" +
"uniform vec2 uTextureExtent;\n" +
"uniform vec2 uViewPortExtent;\n" +
"varying vec2 TextureCoordOut;\n" +
"void main() {\n" +
"gl_Position = uModelview * uBillboardPosition;\n" +
"float fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;\n" +
"float fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;\n" +
"gl_Position.x += ((aTextureCoord.x - 0.5) - (uBillboardAnchor.x - 0.5)) * fx;\n" +
"gl_Position.y -= ((aTextureCoord.y - 0.5) - (uBillboardAnchor.y - 0.5)) * fy;\n" +
"TextureCoordOut = aTextureCoord;\n" +
"}\n",
 emptyString +  
"varying mediump vec2 TextureCoordOut;\n" +
"uniform sampler2D Sampler;\n" +
"void main() {\n" +
"gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
"}\n");
    this->add(sourcesBillboard);

    GPUProgramSources sourcesFlatColorMesh_DistanceTransparency("FlatColorMesh_DistanceTransparency",
 emptyString +  
"attribute vec4 aPosition;\n" +
"uniform mat4 uModelview;\n" +
"uniform mat4 uModel;\n" +
"uniform float uPointSize;\n" +
"varying highp float alpha;\n" +
"uniform highp float uTransparencyDistanceThreshold;\n" +
"#define HALF_PI 1.5707963268\n" +
"#define START_FADING_AT 0.75\n" +
"void main() {\n" +
"gl_Position = uModelview * aPosition;\n" +
"gl_PointSize = uPointSize;\n" +
"vec3 vertexInModel = (uModel * aPosition).xyz;\n" +
"highp float vertexDist = -vertexInModel.z;\n" +
"highp float d = vertexDist / uTransparencyDistanceThreshold;\n" +
"if (d < START_FADING_AT){\n" +
"alpha = 1.0;\n" +
"} else{\n" +
"if (d > 1.0){\n" +
"alpha = 0.0;\n" +
"} else{\n" +
"d = (d - START_FADING_AT) / (1.0 - START_FADING_AT);\n" +
"d = (1.0 + d) * (HALF_PI);\n" +
"alpha = sin(d);\n" +
"}\n" +
"}\n" +
"}\n",
 emptyString +  
"uniform lowp vec4 uFlatColor;\n" +
"varying highp float alpha;\n" +
"void main() {\n" +
"if (alpha <= 0.0){\n" +
"discard;\n" +
"}\n" +
"gl_FragColor.r = uFlatColor.r;\n" +
"gl_FragColor.g = uFlatColor.g;\n" +
"gl_FragColor.b = uFlatColor.b;\n" +
"gl_FragColor.a = uFlatColor.a * alpha;\n" +
"}\n");
    this->add(sourcesFlatColorMesh_DistanceTransparency);

  }

};
#endif
