//
//  GLConstants.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "GLConstants.hpp"

int GLCullFace::_front = 0;
int GLCullFace::_back = 0;
int GLCullFace::_frontAndBack = 0;

int GLBufferType::_colorBuffer = 0;
int GLBufferType::_depthBuffer = 0;

int GLStage::_polygonOffsetFill = 0;
int GLStage::_depthTest = 0;
int GLStage::_blend = 0;
int GLStage::_cullFace = 0;

int GLType::_float = 0;
int GLType::_unsignedByte = 0;
int GLType::_unsignedInt = 0;
int GLType::_int = 0;
int GLType::_vec2Float = 0;
int GLType::_vec3Float = 0;
int GLType::_vec4Float = 0;
int GLType::_bool = 0;
int GLType::_matrix4Float = 0;


int GLPrimitive::_triangles     = 0;
int GLPrimitive::_triangleStrip = 0;
int GLPrimitive::_triangleFan   = 0;

int GLPrimitive::_lines     = 0;
int GLPrimitive::_lineStrip = 0;
int GLPrimitive::_lineLoop  = 0;

int GLPrimitive::_points = 0;


int GLBlendFactor::_srcAlpha = 0;
int GLBlendFactor::_oneMinusSrcAlpha = 0;
int GLBlendFactor::_one = 0;
int GLBlendFactor::_zero = 0;

int GLTextureType::_texture2D = 0;

int GLTextureParameter::_minFilter = 0;
int GLTextureParameter::_magFilter = 0;
int GLTextureParameter::_wrapS = 0;
int GLTextureParameter::_wrapT = 0;

int GLTextureParameterValue::_nearest              = 0;
int GLTextureParameterValue::_linear               = 0;
int GLTextureParameterValue::_nearestMipmapNearest = 0;
int GLTextureParameterValue::_nearestMipmapLinear  = 0;
int GLTextureParameterValue::_linearMipmapNearest  = 0;
int GLTextureParameterValue::_linearMipmapLinear   = 0;

int GLTextureParameterValue::_clampToEdge = 0;

int GLAlignment::_pack = 0;
int GLAlignment::_unpack = 0;

int GLFormat::_rgba = 0;

int GLVariable::_viewport = 0;
int GLVariable::_activeUniforms = 0;
int GLVariable::_activeAttributes = 0;

int GLError::_noError = 0;
