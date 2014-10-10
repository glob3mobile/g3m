//
//  NativeGL_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 04/06/14.
// 
//
#include <d3d11_2.h>
#include "NativeGL_win8.hpp"
#include "GPUProgram_D3D.hpp"
#include "Matrix44D.hpp"
#include "ShortBuffer_win8.hpp"
#include "GLTextureId_win8.hpp"


void NativeGL_win8::initializeRenderStates() const{
	_rasterizerDesc.FillMode = D3D11_FILL_WIREFRAME;

	_rtblendDesc.BlendOp = D3D11_BLEND_OP_ADD;
	_rtblendDesc.BlendOpAlpha = D3D11_BLEND_OP_ADD;
	_rtblendDesc.SrcBlendAlpha = D3D11_BLEND_ONE;
	_rtblendDesc.DestBlendAlpha = D3D11_BLEND_ZERO;
	_rtblendDesc.RenderTargetWriteMask = D3D11_COLOR_WRITE_ENABLE_ALL;

}

//Method should be called by any Draw-method
void NativeGL_win8::setRenderState() const{
	if (_rasterizerStateChanged){
		_device->CreateRasterizerState1(&_rasterizerDesc, &_rasterizerState);
		_deviceContext->RSSetState(_rasterizerState);
		_rasterizerStateChanged = false;
	}

	if (_blendDescChanged){

		_blendDesc.RenderTarget[0] = _rtblendDesc;
		_device->CreateBlendState(&_blendDesc, &_blendState);
		_deviceContext->OMSetBlendState(_blendState, NULL, 0xffffffff);
		_blendDescChanged = false;
	}
	
}


void NativeGL_win8::clearColor(float red, float green, float blue, float alpha) const {
	_clearColor[0] = red;
	_clearColor[1] = green;
	_clearColor[2] = blue;
	_clearColor[3] = alpha;
}

void NativeGL_win8::useProgram(IGPUProgram* program) const{

	//Cast to GPUProgram_D3D and set Shaders
	GPUProgram_D3D* p = (GPUProgram_D3D*) program;
	_deviceContext->VSSetShader(p->getVertexShader(), nullptr, 0);
	_deviceContext->PSSetShader(p->getPixelShader(), nullptr, 0);
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
		_depthStencilDesc.DepthEnable = true;
		_depthStencilStateChanged = true;
	}
	else if (feature == FEATURE_BLEND){
		_rtblendDesc.BlendEnable = true;
		_blendDescChanged = true;
	}
}

void NativeGL_win8::disable(int feature) const{
	if (feature == FEATURE_DEPTHTEST){
		_depthStencilDesc.DepthEnable = false;
		_depthStencilStateChanged = true;
	}
	else if (feature == FEATURE_BLEND){
		_rtblendDesc.BlendEnable = false;
		_blendDescChanged = true;
	}
	else if (feature == FEATURE_CULLFACE){
		_rasterizerDesc.CullMode = D3D11_CULL_MODE::D3D11_CULL_NONE;
		_rasterizerStateChanged = true;
	}
	else if (feature == FEATURE_POLYGONOFFSETFILL){
		_rasterizerDesc.DepthBias = 0;
		_rasterizerDesc.DepthBiasClamp = 0.0f;
		_rasterizerDesc.SlopeScaledDepthBias = 0.0f;
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


	//get pointer to underlying array
	short* values = ((ShortBuffer_win8*)indices)->getPointer();

	//buffer does not exist or has changed size -> create new
	if ((_indexBuffer == NULL) || (_indexBufferSize != sizeof(unsigned short)*count)){
		ILogger::instance()->logInfo("creating index buffer");

		if (_indexBuffer != NULL){
			_indexBuffer->Release();
		}

		_indexBufferSize = sizeof(unsigned short)*count;
		D3D11_BUFFER_DESC _indexBufferDesc;
		_indexBufferDesc.Usage = D3D11_USAGE_DEFAULT;
		_indexBufferDesc.ByteWidth = _indexBufferSize;
		_indexBufferDesc.BindFlags = D3D11_BIND_INDEX_BUFFER;
		_indexBufferDesc.CPUAccessFlags = 0;
		_indexBufferDesc.MiscFlags = 0;

		D3D11_SUBRESOURCE_DATA InitData;
		InitData.pSysMem = values;
		InitData.SysMemPitch = 0;
		InitData.SysMemSlicePitch = 0;

		HRESULT hr = _device->CreateBuffer(&_indexBufferDesc, &InitData, &_indexBuffer);
		if (FAILED(hr)){
			ILogger::instance()->logError("Error while creating index buffer");
			std::string errMsg("Error while creating index buffer");
			throw std::exception(errMsg.c_str());
		}	
	}

	//size has not changed, just update with new values
	// TODO: Only update if necessary
	else{
		_deviceContext->UpdateSubresource(_indexBuffer, 0, NULL, values, 0, 0);
	}

	// set RenderStates
	setRenderState();
	// Set the buffer.
	_deviceContext->IASetIndexBuffer(_indexBuffer, DXGI_FORMAT_R16_UINT, 0);
	// Set Toppology (tringles, triangleStrip, lines, etc...)
	_deviceContext->IASetPrimitiveTopology((D3D11_PRIMITIVE_TOPOLOGY)mode);
	//Draw
	_deviceContext->DrawIndexed(count, 0, 0);
	
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

	if (sfactor != _rtblendDesc.SrcBlend){
		_rtblendDesc.SrcBlend = D3D11_BLEND(sfactor);
		_blendDescChanged = true;
	}
	
	if (dfactor != _rtblendDesc.DestBlend){
		_rtblendDesc.DestBlend = D3D11_BLEND(dfactor);
		_blendDescChanged = true;
	}
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

	std::vector<IGLTextureId*> ts;

	for (int i = 0; i < n; i++) {
		unsigned int textureId = i+1;
		if (textureId == 0) {
			ILogger::instance()->logError("Can't create a textureId");
		}
		else {
			ts.push_back(new GLTextureID_win8(textureId));
		}
	}
	return ts;

	//std::string errMsg("TODO: Implementation");
	//throw std::exception(errMsg.c_str());
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
	_rasterizerDesc.CullMode = D3D11_CULL_MODE(c);
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
