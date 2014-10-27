//
//  GPUProgram_D3D.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//
#include <fstream>
#include <D3D11Shader.h>
#include <D3Dcompiler.h>

#include "GPUProgram_D3D.hpp"
#include "GLUniformID_win8.hpp"
#include "ILogger.hpp"
#include "GPUUniform_D3D.hpp"
#include "GPUAttribute_D3D.hpp"


GPUProgram_D3D::GPUProgram_D3D(GL* gl,
	const std::string& name,
	NativeGL_win8* ngl){

	//Map that maps HLSL Semantics to data types -> should be moved to NativeGL_win8
	m["BINORMAL"] = FLOAT4;
	m["BLENDWEIGHT"] = FLOAT1;
	m["COLOR"] = FLOAT4;
	m["NORMAL"] = FLOAT4;
	m["POSITION"] = FLOAT4;
	m["POSITIONT"] = FLOAT4;
	m["PSIZE"] = FLOAT1;
	m["TANGENT"] = FLOAT4;
	m["TEXCOORD"] = FLOAT4;

	_name = name;
	_gl = gl;
	_ngl = ngl;

	HRESULT hr;
	//Load pre-compiled vertex-Shader file
	std::string vertexShaderName = _name + "V.cso";
	_vsData = loadShaderFile(vertexShaderName);
	hr=_ngl->getDevice()->CreateVertexShader(_vsData->Data, _vsData->Length, nullptr, &_vshader);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Vertex shader " + vertexShaderName);
		throw Platform::Exception::CreateException(hr);
	}
	else{
		ILogger::instance()->logInfo("Loaded vertexShader with name " + name);
	}

	//Load pre-compiled pixel shader file
	std::string pixelShaderName = name + "P.cso";
	_psData = loadShaderFile(pixelShaderName);
	hr=_ngl->getDevice()->CreatePixelShader(_psData->Data, _psData->Length, nullptr, &_pshader);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Pixel shader " + pixelShaderName);
		throw Platform::Exception::CreateException(hr);
	}
	else{
		ILogger::instance()->logInfo("Loaded pixelShader with name " + name);
	}

	getVariables(gl);


}

GPUProgram_D3D::~GPUProgram_D3D(){

	_vshader->Release();
	_pshader->Release();
	_inputLayout->Release();
	delete _vsData;
	delete _psData;
}

void GPUProgram_D3D::getVariables(GL* gl){
	getShaderUniforms(gl);
	getShaderAttributes(gl);
}



void GPUProgram_D3D::getShaderUniforms(GL* gl){
	
	for (int i = 0; i < 32; i++) {
		_uniforms[i] = NULL;
	}
	_uniformsCode = 0;
	//Get array of uniforms from both vertex and pixel shader
	GPUUniform_D3D** createdVSUniforms = getVSUniforms();
	GPUUniform_D3D** createdPSUniforms = getPSUniforms();

	//Combine all the uniforms
	_nUniforms = _nvsUniforms + _npsUniforms;
	_createdUniforms = new GPUUniform*[_nUniforms];
	for (int i = 0; i < _nvsUniforms; i++){
		_createdUniforms[i] = createdVSUniforms[i];
	}
	for (int i = 0; i < _npsUniforms; i++){
		_createdUniforms[i + _nvsUniforms] = createdPSUniforms[i];
	}
	delete [] createdPSUniforms;
	delete [] createdVSUniforms;
}


//Read back "Uniforms" (aka constant buffers) from compiled VERTEX shader
GPUUniform_D3D** GPUProgram_D3D::getVSUniforms(){

	HRESULT hr;
	ID3D11ShaderReflection* vsReflector = NULL;
	hr = D3DReflect(_vsData->Data, _vsData->Length, IID_ID3D11ShaderReflection, (void**)&vsReflector);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while calling D3DReflect on Vertex Shader");
		throw Platform::Exception::CreateException(hr);
	}
	D3D11_SHADER_DESC vsdesc;
	hr = vsReflector->GetDesc(&vsdesc);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while getting Vertex Shader Description");
		throw Platform::Exception::CreateException(hr);
	}

	_nvsUniforms = vsdesc.ConstantBuffers;
	int counter = 0;
	//Create array of uniforms
	GPUUniform_D3D** createdVSUniforms = new GPUUniform_D3D*[_nvsUniforms];
	for (int i = 0; i < _nvsUniforms; i++) {

		//get the cbuffer (->Uniform) i
		ID3D11ShaderReflectionConstantBuffer* constBuffer = vsReflector->GetConstantBufferByIndex(i);

		//get its description
		D3D11_SHADER_BUFFER_DESC cbufferdesc;
		hr = constBuffer->GetDesc(&cbufferdesc);
		if (FAILED(hr)){
			ILogger::instance()->logError("Error while getting Description of Vertex Shader Constant Buffer %i", i);
			throw Platform::Exception::CreateException(hr);
		}

		//Since we are doing one cBuffer per uniform, this loop should only run once...
		for (UINT j = 0; j < cbufferdesc.Variables; j++){

			//Extract variable (and corresponding _desc) from cBuffer
			ID3D11ShaderReflectionVariable* cbufferVariable = constBuffer->GetVariableByIndex(j);
			D3D11_SHADER_VARIABLE_DESC cbufferVariable_desc;
			hr = cbufferVariable->GetDesc(&cbufferVariable_desc);
			if (FAILED(hr)){
				ILogger::instance()->logError("Error while getting Decription of Vertex Shader Constant Buffer variable description");
				throw Platform::Exception::CreateException(hr);
			}

			//Extract Type description
			ID3D11ShaderReflectionType* pType = cbufferVariable->GetType();
			D3D11_SHADER_TYPE_DESC type_desc;
			hr = pType->GetDesc(&type_desc);
			if (FAILED(hr)){
				ILogger::instance()->logError("Error while getting Decription of Vertex Shader cBuffer type description");
				throw Platform::Exception::CreateException(hr);
			}

			//get the name from the variableDesc
			std::string vname = cbufferVariable_desc.Name;

			int vsize = cbufferVariable_desc.Size;

			GPUUniform_D3D* u = createUniform(vname, vsize, type_desc.Class, type_desc.Type, i);
			if (u != NULL) {
				_uniforms[u->getIndex()] = u;

				const int code = GPUVariable::getUniformCode(u->_key);
				_uniformsCode = _uniformsCode | code;
			}
			createdVSUniforms[counter++] = u; //Adding to created uniforms array

		}
	}
	return createdVSUniforms;
}

//Read back "Uniforms" (aka constant buffers) from compiled PIXEL shader
GPUUniform_D3D** GPUProgram_D3D::getPSUniforms(){
	HRESULT hr;
	ID3D11ShaderReflection* psReflector = NULL;
	hr = D3DReflect(_psData->Data, _psData->Length, IID_ID3D11ShaderReflection, (void**)&psReflector);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while calling D3DReflect");
		throw Platform::Exception::CreateException(hr);
	}

	D3D11_SHADER_DESC psdesc;
	hr = psReflector->GetDesc(&psdesc);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while getting Pixel Shader Description");
		throw Platform::Exception::CreateException(hr);
	}

	//find out if/how many samplers are present, and their names
	int boundres = psdesc.BoundResources;
	ILogger::instance()->logInfo("pixel shader bound resources: %i", boundres);
	int numSamplers = 0;
	std::string samplername;  //TODO: more than 1 sampler
	for (int i = 0; i < boundres; i++){
		D3D11_SHADER_INPUT_BIND_DESC sibd;
		hr = psReflector->GetResourceBindingDesc(i, &sibd);
		if (FAILED(hr)){
			ILogger::instance()->logError("Error getResourceBindingDesc slot %i ", i);
			throw Platform::Exception::CreateException(hr);
		}
		if (sibd.Type == D3D_SIT_SAMPLER){
			numSamplers++;
			samplername = sibd.Name;
			ILogger::instance()->logInfo("Sampler at resource slot %i with name: " + samplername, i);
		}		
	}
	
	_npsUniforms = psdesc.ConstantBuffers;
	int counter = 0;
	GPUUniform_D3D** createdPSUniforms = new GPUUniform_D3D*[_npsUniforms + numSamplers];
	for (int i = 0; i < _npsUniforms; i++) {
		//get the cbuffer i
		ID3D11ShaderReflectionConstantBuffer* constBuffer = psReflector->GetConstantBufferByIndex(i);
		//get its description
		D3D11_SHADER_BUFFER_DESC cbufferdesc;
		hr = constBuffer->GetDesc(&cbufferdesc);
		if (FAILED(hr)){
			ILogger::instance()->logError("Error while getting Description of Pixel Shader Constant Buffer %i", i);
			throw Platform::Exception::CreateException(hr);
		}
		std::string name = cbufferdesc.Name;
		for (UINT j = 0; j < cbufferdesc.Variables; j++){

			//Extract variable (and corresponding _desc) from cBuffer
			ID3D11ShaderReflectionVariable* cbufferVariable = constBuffer->GetVariableByIndex(j);
			D3D11_SHADER_VARIABLE_DESC cbufferVariable_desc;
			hr = cbufferVariable->GetDesc(&cbufferVariable_desc);
			if (FAILED(hr)){
				ILogger::instance()->logError("Error while getting Decription of Pixel Shader Constant Buffer variable description");
				throw Platform::Exception::CreateException(hr);
			}

			//Extract Type description
			ID3D11ShaderReflectionType* pType = cbufferVariable->GetType();
			D3D11_SHADER_TYPE_DESC type_desc;
			hr = pType->GetDesc(&type_desc);
			if (FAILED(hr)){
				ILogger::instance()->logError("Error while getting Decription of Pixel Shader cBuffer type description");
				throw Platform::Exception::CreateException(hr);
			}

			//get the name from the variableDesc
			std::string vname = cbufferVariable_desc.Name;
			int vsize = cbufferVariable_desc.Size;

			//Create the uniform and add to list
			GPUUniform_D3D* u = createUniform(vname, vsize, type_desc.Class, type_desc.Type, i);
			if (u != NULL) {
				_uniforms[u->getIndex()] = u;

				const int code = GPUVariable::getUniformCode(u->_key);
				_uniformsCode = _uniformsCode | code;
			}
			createdPSUniforms[counter++] = u; //Adding to created uniforms array
		}
	}
	//Add sampler uniforms (if any)
	for (int i = 0; i < numSamplers; i++){
		GPUUniform_D3D* samplerUniform = new GPUUniformSampler2D_D3D(samplername, new GLUniformID_win8(666), _ngl);
		_uniforms[samplerUniform->getIndex()] = samplerUniform;
		const int code = GPUVariable::getUniformCode(samplerUniform->_key);
		_uniformsCode = _uniformsCode | code;
		createdPSUniforms[counter++] = samplerUniform;
	}
	_npsUniforms += numSamplers;

	return createdPSUniforms;
}


//Currently only checks for vertex shader Attributes, as all pixel shaders used so far do not have any attributes
void GPUProgram_D3D::getShaderAttributes(GL* gl){
	for (int i = 0; i < 32; i++) {
		_attributes[i] = NULL;
	}

	//Vertex shader attributes
	_attributesCode = 0;
	HRESULT hr;
	ID3D11ShaderReflection* vsReflector = NULL;
	hr = D3DReflect(_vsData->Data, _vsData->Length, IID_ID3D11ShaderReflection, (void**)&vsReflector);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while calling D3DReflect");
		throw Platform::Exception::CreateException(hr);
	}
	D3D11_SHADER_DESC vsdesc;
	hr = vsReflector->GetDesc(&vsdesc);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while getting Vertex Shader Description (getShaderAttributes)");
		throw Platform::Exception::CreateException(hr);
	}

	_nAttributes = vsdesc.InputParameters;
	int counter = 0;
	//Create array of attributes
	_createdAttributes = new GPUAttribute*[_nAttributes];
	D3D11_INPUT_ELEMENT_DESC* _ieds = new D3D11_INPUT_ELEMENT_DESC[_nAttributes];
	for (int i = 0; i < _nAttributes; i++) {
		D3D11_SIGNATURE_PARAMETER_DESC input_desc;
		hr = vsReflector->GetInputParameterDesc(i, &input_desc);
		if (FAILED(hr)){
			ILogger::instance()->logError("Error while getting Vertex Shader Input Paramter Desc (getShaderAttributes)");
			throw Platform::Exception::CreateException(hr);
		}
		//Get name and semantic index
		std::string iname = input_desc.SemanticName;
		int semanticIndex = input_desc.SemanticIndex;

		GPUAttribute_D3D* a = createAttribute(iname, i, semanticIndex);
		if (a != NULL) {
			_attributes[a->getIndex()] = a;
			const int code = GPUVariable::getAttributeCode(a->_key);
			_attributesCode = _attributesCode | code;
			//get the input element description from each created Attribute
			_ieds[i] = a->getIED();		
		}

		_createdAttributes[counter++] = a;
	}


	//Create and set Input Layout
	hr = _ngl->getDevice()->CreateInputLayout(_ieds, _nAttributes, _vsData->Data, _vsData->Length, &_inputLayout);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error While creating Input Layout for Attributes");
		throw Platform::Exception::CreateException(hr);
	}
	_ngl->getDeviceContext()->IASetInputLayout(_inputLayout);
}


void GPUProgram_D3D::applyChanges(GL* gl){

	//Apply uniforms
	for (int i = 0; i < _nUniforms; i++) {
		GPUUniform* uniform = _createdUniforms[i];
		if (uniform != NULL) { //Texture Samplers return null
			uniform->applyChanges(gl);
		}
	}

	//Apply attributes
	ID3D11Buffer** buffers = new ID3D11Buffer*[_nAttributes];
	unsigned int* strides = new unsigned int[_nAttributes];
	unsigned int* offsets = new unsigned int[_nAttributes];
	for (int j = 0; j < _nAttributes; j++) {
		GPUAttribute_D3D* attribute = (GPUAttribute_D3D*)_createdAttributes[j];
		buffers[j] = attribute->getBuffer();
		strides[j] = attribute->getStride();
		offsets[j] = attribute->getOffset();
		/*if (attribute != NULL) {
			attribute->applyChanges(gl);
		}*/
		attribute->setDirty(false);
	}
	_ngl->getDeviceContext()->IASetInputLayout(_inputLayout);
	_ngl->getDeviceContext()->IASetVertexBuffers(0, _nAttributes, buffers, strides, offsets);
	delete[] buffers;
	delete[] strides;
	delete[] offsets;
}

void GPUProgram_D3D::deleteShader(GL* gl, int shader) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void GPUProgram_D3D::deleteProgram(GL* gl, const IGPUProgram* p){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

GPUUniform_D3D* GPUProgram_D3D::createUniform(std::string name, int size, int klasse, int type, int id){
	if (((klasse == 3) | (klasse == 4)) && type == 3){
		return new GPUUniformMatrix4f_D3D(name, new GLUniformID_win8(id), _ngl);
	}
	else if (klasse == 1 && type == 3 && size == 16){
		return new GPUUniformVec4Float_D3D(name, new GLUniformID_win8(id), _ngl);
	}
	else if (klasse == 0 && type == 3){
		return new GPUUniformFloat_D3D(name, new GLUniformID_win8(id), _ngl);
	}

	else if (klasse == 1 && type == 3 && size == 8){
		return new GPUUniformVec2Float_D3D(name, new GLUniformID_win8(id), _ngl);
	}
	else if (klasse == 1 && type == 3 && size == 12){
		return new GPUUniformVec3Float_D3D(name, new GLUniformID_win8(id), _ngl);
	}
	else if (klasse == 0 && type == 1){
		return new GPUUniformBool_D3D(name, new GLUniformID_win8(id), _ngl);
	}
	/*
	else if (){
	return new GPUUniformSampler2D(name, new GLUniformID_win8(id));
	}*/
	else{
		ILogger::instance()->logError("Unknown uniform type : " + name);
		return NULL;
	}
}

GPUAttribute_D3D* GPUProgram_D3D::createAttribute(std::string name, int id, int semanticIndex){
	int type;
	type = m.at(name);     // TODO need to catch it if type does not exist!!



	if (type == -1 || type == 0){
		return NULL;
	}
	else if (type == 1){
		return new GPUAttributeVec1Float_D3D(name, id, semanticIndex, _ngl);
	}
	else if (type == 2){
		return new GPUAttributeVec2Float_D3D(name, id, semanticIndex, _ngl);
	}
	else if (type == 3){
		return new GPUAttributeVec3Float_D3D(name, id, semanticIndex, _ngl);
	}
	else if (type == 4){
		return new GPUAttributeVec4Float_D3D(name, id, semanticIndex, _ngl);
	}
	else{
		return NULL;
	}
}

//Load pre-compiled shader (*.cso) file from disk 
Platform::Array<byte>^ GPUProgram_D3D::loadShaderFile(std::string File)
{
	Platform::Array<byte>^ FileData = nullptr;

	// open the file
	std::ifstream VertexFile(File, std::ios::in | std::ios::binary | std::ios::ate);

	// if open was successful
	if (VertexFile.is_open())
	{
		// find the length of the file
		int Length = (int)VertexFile.tellg();

		// collect the file data
		FileData = ref new Platform::Array<byte>(Length);
		VertexFile.seekg(0, std::ios::beg);
		VertexFile.read(reinterpret_cast<char*>(FileData->Data), Length);
		VertexFile.close();
	}
	else{
		ILogger::instance()->logError("Could not load shader file " + File);
		std::string errMsg("Error while loading shader file " + File);
		throw std::exception(errMsg.c_str());
	}
	return FileData;
}