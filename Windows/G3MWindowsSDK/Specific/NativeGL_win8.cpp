//
//  NativeGL_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 04/06/14.
// 
//
#include <d3d11_2.h>
#include "NativeGL_win8.hpp"

void NativeGL_win8::clearColor(float red, float green, float blue, float alpha) const {
	_clearColor[0] = red;
	_clearColor[1] = green;
	_clearColor[2] = blue;
	_clearColor[3] = alpha;
}

void NativeGL_win8::useProgram(IGPUProgram* program) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::uniform2f(const IGLUniformID* loc, float x, float y) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::uniform1f(const IGLUniformID* loc, float x) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::uniform1i(const IGLUniformID* loc, int v) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void NativeGL_win8::uniformMatrix4fv(const IGLUniformID* location, bool transpose, const Matrix44D* matrix) const {
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::uniform4f(const IGLUniformID* location, float v0, float v1, float v2, float v3) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::uniform3f(const IGLUniformID* location, float v0, float v1, float v2) const {
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::clear(int buffers) const{
	if ((buffers &  COLOR_BUFFER_BIT) != 0) {	
		_deviceContext->ClearRenderTargetView(_renderTargetView.Get(), _clearColor);
	}
	if ((buffers & DEPTH_BUFFER_BIT) != 0){
		_deviceContext->ClearDepthStencilView(_depthStencilView.Get(), DEPTH_BUFFER_BIT, 0.f, 0);
	}
}

void NativeGL_win8::enable(int feature) const{
	if (feature == FEATURE_DEPTHTEST){
		_depthStencilDesc->DepthEnable = true;
		_depthStencilStateChanged = true;
	}
	else if (feature == FEATURE_BLEND){
		_blendDesc->BlendEnable = true;
		_blendDescChanged = true;
	}
}

void NativeGL_win8::disable(int feature) const{
	if (feature == FEATURE_DEPTHTEST){
		_depthStencilDesc->DepthEnable = false;
		_depthStencilStateChanged = true;
	}
	else if (feature == FEATURE_BLEND){
		_blendDesc->BlendEnable = false;
		_blendDescChanged = true;
	}
	else if (feature == FEATURE_CULLFACE){
		_rasterizerDesc->CullMode = D3D11_CULL_MODE::D3D11_CULL_NONE;
		_rasterizerStateChanged = true;
	}
	else if (feature == FEATURE_POLYGONOFFSETFILL){
		_rasterizerDesc->DepthBias = 0;
		_rasterizerDesc->DepthBiasClamp = 0.0f;
		_rasterizerDesc->SlopeScaledDepthBias = 0.0f;
		_rasterizerStateChanged = true;
	}
}

void NativeGL_win8::polygonOffset(float factor, float units) const{
	//_rasterizerDesc->SlopeScaledDepthBias = factor;
	//_rasterizerDesc->DepthBias = units / 1ef6;
	//_rasterizerDesc->DepthBias = units * 2.0*4.8e-7;
	std::string errMsg("TODO: Find out how the values (factor, units) from OGL and DepthBias/SlopeScaledDepthBias relate");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::vertexAttribPointer(int index, int size, bool normalized, int stride, const IFloatBuffer* buffer) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::drawElements(int mode, int count, IShortBuffer* indices) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::lineWidth(float width) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

int NativeGL_win8::getError() const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::blendFunc(int sfactor, int dfactor) const{
	
	_blendDesc->SrcBlendAlpha = D3D11_BLEND(sfactor);
	_blendDesc->DestBlendAlpha = D3D11_BLEND(dfactor);

	if (sfactor == D3D11_BLEND::D3D11_BLEND_SRC_ALPHA){
		_blendDesc->SrcBlend = D3D11_BLEND::D3D11_BLEND_SRC_COLOR;
	}
	else if (sfactor == D3D11_BLEND::D3D11_BLEND_INV_SRC_ALPHA){
		_blendDesc->SrcBlend = D3D11_BLEND::D3D11_BLEND_INV_SRC_COLOR;
	}
	else{
		_blendDesc->SrcBlend = D3D11_BLEND(sfactor);
	}


	if (dfactor == D3D11_BLEND::D3D11_BLEND_SRC_ALPHA){
		_blendDesc->DestBlend = D3D11_BLEND::D3D11_BLEND_SRC_COLOR;
	}
	else if (dfactor == D3D11_BLEND::D3D11_BLEND_INV_SRC_ALPHA){
		_blendDesc->DestBlend = D3D11_BLEND::D3D11_BLEND_INV_SRC_COLOR;
	}
	else{
		_blendDesc->DestBlend = D3D11_BLEND(dfactor);
	}

	_blendDescChanged = true;

}

void NativeGL_win8::bindTexture(int target, const IGLTextureId* texture) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

bool NativeGL_win8::deleteTexture(const IGLTextureId* texture) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::enableVertexAttribArray(int location) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::disableVertexAttribArray(int location) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::pixelStorei(int pname, int param) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

std::vector<IGLTextureId*> NativeGL_win8::genTextures(int	n) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::texParameteri(int target, int par, int v) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::texImage2D(const IImage* image, int format) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::generateMipmap(int target) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::drawArrays(int mode, int first, int count) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::cullFace(int c) const{
	_rasterizerDesc->CullMode = D3D11_CULL_MODE(c);
	_rasterizerStateChanged = true;
}

void NativeGL_win8::getIntegerv(int v, int i[]) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

int NativeGL_win8::CullFace_Front() const{
	return D3D11_CULL_MODE::D3D11_CULL_FRONT;
}

int NativeGL_win8::CullFace_Back() const{
	return D3D11_CULL_MODE::D3D11_CULL_BACK;
}

int NativeGL_win8::CullFace_FrontAndBack() const{
	return D3D11_CULL_MODE::D3D11_CULL_NONE;
}

int NativeGL_win8::BufferType_ColorBuffer() const{
	return COLOR_BUFFER_BIT;
}

int NativeGL_win8::BufferType_DepthBuffer() const{
	return DEPTH_BUFFER_BIT;
}

int NativeGL_win8::Feature_PolygonOffsetFill() const{
	return FEATURE_POLYGONOFFSETFILL;
}

int NativeGL_win8::Feature_DepthTest() const{
	return FEATURE_DEPTHTEST;
}

int NativeGL_win8::Feature_Blend() const{
	return FEATURE_BLEND;
}

int NativeGL_win8::Feature_CullFace() const{
	return FEATURE_CULLFACE;
}

int NativeGL_win8::Type_Float() const {
	return TYPE_FLOAT;
}

int NativeGL_win8::Type_UnsignedByte() const{
	return TYPE_UBYTE;
}

int NativeGL_win8::Type_UnsignedInt() const{
	return TYPE_UINT;
}

int NativeGL_win8::Type_Int() const{
	return TYPE_INT;
}

int NativeGL_win8::Type_Vec2Float() const{
	return TYPE_VEC2FLOAT;
}

int NativeGL_win8::Type_Vec3Float() const{
	return TYPE_VEC3FLOAT;
}

int NativeGL_win8::Type_Vec4Float() const{
	return TYPE_VEC4FLOAT;
}

int NativeGL_win8::Type_Bool() const{
	return TYPE_BOOL;
}

int NativeGL_win8::Type_Matrix4Float() const{
	return TYPE_MAT4FLOAT;
}

int NativeGL_win8::Primitive_Triangles() const{
	return D3D_PRIMITIVE_TOPOLOGY_TRIANGLELIST;
}

int NativeGL_win8::Primitive_TriangleStrip() const{
	return D3D_PRIMITIVE_TOPOLOGY_TRIANGLESTRIP;
}

int NativeGL_win8::Primitive_TriangleFan() const{
	return D3D_PRIMITIVE_TOPOLOGY_UNDEFINED;
}

int NativeGL_win8::Primitive_Lines() const{
	return D3D_PRIMITIVE_TOPOLOGY_LINELIST;
}

int NativeGL_win8::Primitive_LineStrip() const{
	return D3D_PRIMITIVE_TOPOLOGY_LINESTRIP;
}

int NativeGL_win8::Primitive_LineLoop() const{
	return D3D_PRIMITIVE_TOPOLOGY_UNDEFINED;
}

int NativeGL_win8::Primitive_Points() const{
	return D3D_PRIMITIVE_TOPOLOGY_POINTLIST;
}

int NativeGL_win8::BlendFactor_One() const{
	return D3D11_BLEND::D3D11_BLEND_ONE;
}

int NativeGL_win8::BlendFactor_Zero() const{
	return D3D11_BLEND::D3D11_BLEND_ZERO;
}

int NativeGL_win8::BlendFactor_SrcAlpha() const{
	return D3D11_BLEND::D3D11_BLEND_SRC_ALPHA;
}

int NativeGL_win8::BlendFactor_OneMinusSrcAlpha() const{
	return D3D11_BLEND::D3D11_BLEND_INV_SRC_ALPHA;
}

int NativeGL_win8::TextureType_Texture2D() const{
	return TEXTURETYPE2D;
}

int NativeGL_win8::TextureParameter_MinFilter() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameter_MagFilter() const{
	return 1;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameter_WrapS() const{
	return D3D11_TEXTURE_ADDRESS_MODE::D3D11_TEXTURE_ADDRESS_WRAP;
}

int NativeGL_win8::TextureParameter_WrapT() const{
	return D3D11_TEXTURE_ADDRESS_MODE::D3D11_TEXTURE_ADDRESS_WRAP;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameterValue_Nearest() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameterValue_Linear() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameterValue_NearestMipmapNearest() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameterValue_NearestMipmapLinear() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameterValue_LinearMipmapNearest() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameterValue_LinearMipmapLinear() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::TextureParameterValue_ClampToEdge() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::Alignment_Pack() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::Alignment_Unpack() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::Format_RGBA() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::Variable_Viewport() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::Variable_ActiveAttributes() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::Variable_ActiveUniforms() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::Error_NoError() const{
	return 0;
	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
}

int NativeGL_win8::createProgram() const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

bool NativeGL_win8::deleteProgram(int program) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::attachShader(int program, int shader) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

int NativeGL_win8::createShader(ShaderType type) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

bool NativeGL_win8::compileShader(int shader, const std::string& source) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

bool NativeGL_win8::deleteShader(int shader) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::printShaderInfoLog(int shader) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

bool NativeGL_win8::linkProgram(int program) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::printProgramInfoLog(int program) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::bindAttribLocation(const IGPUProgram* program, int loc, const std::string& name) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

int NativeGL_win8::getProgramiv(const IGPUProgram* program, int param) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

GPUUniform* NativeGL_win8::getActiveUniform(const IGPUProgram* program, int i) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

GPUAttribute* NativeGL_win8::getActiveAttribute(const IGPUProgram* program, int i) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::depthMask(bool v) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void NativeGL_win8::setActiveTexture(int i) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
