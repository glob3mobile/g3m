#include "GPUAttribute_D3D.hpp"
#include "FloatBuffer_win8.hpp"


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//General
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void GPUAttribute_D3D::unset(GL* gl){
	_buffer->Release();
	_buffer = NULL;
	_bufferSize = -1;

	if (_value != NULL) {
		_value->_release();
		_value = NULL;
	}
	_enabled = false;
	_dirty = false;
}

void GPUAttribute_D3D::set(const GPUAttributeValue* v){
	if (v != _value) {

		if (v->_enabled && _type != v->_type) { //type checking
			ILogger::instance()->logError("Attempting to set attribute " + _name + "with invalid value type.");
			return;
		}
		if (_value == NULL || !_value->isEquals(v)) {
			_dirty = true;

			if (_value != NULL) {
				_value->_release();
			}
			_value = v;
			_value->_retain();

			int newSize = ((FloatBuffer_win8*)((GPUAttributeValueVecFloat*)_value)->getBuffer())->size();
			if ((_buffer == NULL) || (newSize != _bufferSize)){
				if (_buffer != NULL){
					_buffer->Release();
				}
				_bufferSize = newSize;
				createD3D11Buffer();
			}
			else{

				if (_buffer != NULL){
					_buffer->Release();
				}
				_bufferSize = newSize;
				createD3D11Buffer();
				//updateBuffer();
			}		
		}
	}
}

//Generic implementation - only GPUAttributeVec4Float_D3D needs its own implementation (because it may need to expand vec3s to vec4s))
void GPUAttribute_D3D::createD3D11Buffer(){

	FloatBuffer_win8* buffer = (FloatBuffer_win8*)((GPUAttributeValueVecFloat*)_value)->getBuffer();
	float* bufferArray;
	bufferArray = buffer->getPointer();

	D3D11_SUBRESOURCE_DATA vertexBufferData = { 0 };
	vertexBufferData.pSysMem = bufferArray;// _data;
	vertexBufferData.SysMemPitch = 0;
	vertexBufferData.SysMemSlicePitch = 0;

	D3D11_BUFFER_DESC bdesc;
	ZeroMemory(&bdesc, sizeof(D3D11_BUFFER_DESC));
	bdesc.ByteWidth = sizeof(float) * _bufferSize;
	bdesc.BindFlags = D3D11_BIND_VERTEX_BUFFER;
	bdesc.Usage = D3D11_USAGE_DYNAMIC; //D3D11_USAGE_DEFAULT;
	bdesc.CPUAccessFlags = D3D11_CPU_ACCESS_WRITE; //NULL;
	bdesc.MiscFlags = NULL;
	bdesc.StructureByteStride = NULL;

	if (FAILED(_ngl->getDevice()->CreateBuffer(&bdesc, &vertexBufferData,
		&_buffer))){
		ILogger::instance()->logError("Error while creating FloatX vertex buffer");
		std::string errMsg("Error while creating FloatX vertex buffer");
		throw std::exception(errMsg.c_str());
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUAttributeVec1Float_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUAttributeVec1Float_D3D::setInputLayoutDesc(){
	_ied.Format = DXGI_FORMAT_R32_FLOAT;
	_ied.SemanticName = _name.c_str();
	_ied.SemanticIndex = _semanticIndex;
	_ied.InputSlot = _id;
	if (_id == 0){
		_ied.AlignedByteOffset = 0;
	}
	else{
		_ied.AlignedByteOffset = D3D11_APPEND_ALIGNED_ELEMENT;
		//_ied.AlignedByteOffset = 0;
	}
	_ied.InputSlotClass = D3D11_INPUT_PER_VERTEX_DATA;
	_ied.InstanceDataStepRate = 0;
}

void GPUAttributeVec1Float_D3D::updateBuffer(){
	std::string errMsg("Implementation");
	throw std::exception(errMsg.c_str());
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUAttributeVec2Float_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUAttributeVec2Float_D3D::setInputLayoutDesc(){
	_ied.Format = DXGI_FORMAT_R32G32_FLOAT;
	_ied.SemanticName = _name.c_str();
	_ied.SemanticIndex = _semanticIndex;//TODO _ied.SemanticIndex = TODO;
	_ied.InputSlot = _id;
	if (_id == 0){
		_ied.AlignedByteOffset = 0;
	}
	else{
		_ied.AlignedByteOffset = D3D11_APPEND_ALIGNED_ELEMENT;
	}
	_ied.InputSlotClass = D3D11_INPUT_PER_VERTEX_DATA;
	_ied.InstanceDataStepRate = 0;
}

void GPUAttributeVec2Float_D3D::updateBuffer(){
	std::string errMsg("Implementation");
	throw std::exception(errMsg.c_str());
}



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUAttributeVec3Float_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUAttributeVec3Float_D3D::setInputLayoutDesc(){
	_ied.Format = DXGI_FORMAT_R32G32B32_FLOAT;
	_ied.SemanticName = _name.c_str();
	_ied.SemanticIndex = _semanticIndex;
	_ied.InputSlot = _id;
	if (_id == 0){
		_ied.AlignedByteOffset = 0;
	}
	else{
		_ied.AlignedByteOffset = D3D11_APPEND_ALIGNED_ELEMENT;
	}
	_ied.InputSlotClass = D3D11_INPUT_PER_VERTEX_DATA;
	_ied.InstanceDataStepRate = 0;
}

void GPUAttributeVec3Float_D3D::updateBuffer(){
	FloatBuffer_win8* buffer = (FloatBuffer_win8*)((GPUAttributeValueVecFloat*)_value)->getBuffer();

	float* bufferArray;
	bufferArray = buffer->getPointer();

	D3D11_MAPPED_SUBRESOURCE MSData;
	if (SUCCEEDED(_ngl->getDeviceContext()->Map(_buffer, 0, D3D11_MAP_WRITE_DISCARD, 0, &MSData)))
	{
		// copy data over to constant buffer
		memcpy_s(MSData.pData, MSData.RowPitch, &bufferArray, sizeof(float)*_bufferSize);
		_ngl->getDeviceContext()->Unmap(_buffer, 0);
	}
	else{
		ILogger::instance()->logError("Uniform " + _name + " was not set.");
		std::string errMsg("Error while mapping constant buffer (GPUUniformBool_D3D)");
		throw std::exception(errMsg.c_str());
	}


}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUAttributeVec4Float_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUAttributeVec4Float_D3D::setInputLayoutDesc(){
	_ied.Format = DXGI_FORMAT_R32G32B32A32_FLOAT;
	_ied.SemanticName = _name.c_str();
	_ied.SemanticIndex = _semanticIndex;//TODO _ied.SemanticIndex = TODO;
	_ied.InputSlot = _id;
	if (_id == 0){
		_ied.AlignedByteOffset = 0;
	}
	else{
		_ied.AlignedByteOffset = D3D11_APPEND_ALIGNED_ELEMENT;
	}
	_ied.InputSlotClass = D3D11_INPUT_PER_VERTEX_DATA;
	_ied.InstanceDataStepRate = 0;
}

void GPUAttributeVec4Float_D3D::createD3D11Buffer(){

	FloatBuffer_win8* buffer = (FloatBuffer_win8*)((GPUAttributeValueVecFloat*)_value)->getBuffer();
	int buffersize;
	float* bufferArray;
	if (_value->_arrayElementSize == 3){
		bufferArray = buffer->getPaddedTo4Pointer();
		buffersize = buffer->getPaddedSize();
	}
	else{
		bufferArray = buffer->getPointer();
		buffersize = _bufferSize;// buffer->size();
	}

	D3D11_SUBRESOURCE_DATA vertexBufferData = { 0 };
	vertexBufferData.pSysMem = bufferArray;// _data;
	vertexBufferData.SysMemPitch = 0;
	vertexBufferData.SysMemSlicePitch = 0;

	D3D11_BUFFER_DESC bdesc;
	ZeroMemory(&bdesc, sizeof(D3D11_BUFFER_DESC));
	bdesc.ByteWidth = sizeof(float) * buffersize;
	bdesc.BindFlags = D3D11_BIND_VERTEX_BUFFER;
	bdesc.Usage = D3D11_USAGE_DYNAMIC; //D3D11_USAGE_DEFAULT;
	bdesc.CPUAccessFlags = D3D11_CPU_ACCESS_WRITE; //NULL;
	bdesc.MiscFlags = NULL;
	bdesc.StructureByteStride = NULL;

	HRESULT hr = _ngl->getDevice()->CreateBuffer(&bdesc, &vertexBufferData,
		&_buffer);

	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Float4 vertex buffer");
		std::string errMsg("Error while creating Float4 vertex buffer");
		throw std::exception(errMsg.c_str());
	}
}

void GPUAttributeVec4Float_D3D::updateBuffer(){

	FloatBuffer_win8* buffer = (FloatBuffer_win8*)((GPUAttributeValueVecFloat*)_value)->getBuffer();
	int buffersize;
	float* bufferArray;
	if (_value->_arrayElementSize == 3){
		bufferArray = buffer->getPaddedTo4Pointer();
		buffersize = buffer->getPaddedSize();
	}
	else{
		bufferArray = buffer->getPointer();
		buffersize = _bufferSize;// buffer->size();
	}

	//_ngl->getDeviceContext()->UpdateSubresource(_buffer, 0, NULL, bufferArray, 0, 0);
	
	D3D11_MAPPED_SUBRESOURCE MSData;
	if (SUCCEEDED(_ngl->getDeviceContext()->Map(_buffer, _id, D3D11_MAP_WRITE_DISCARD, 0, &MSData)))
	{
		// copy data over to constant buffer
		memcpy_s(MSData.pData, MSData.RowPitch, &bufferArray, sizeof(float)*buffersize);
		_ngl->getDeviceContext()->Unmap(_buffer, _id);
	}
	else{
		ILogger::instance()->logError("Uniform " + _name + " was not set.");
		std::string errMsg("Error while mapping constant buffer (GPUUniformBool_D3D)");
		throw std::exception(errMsg.c_str());
	}

}


