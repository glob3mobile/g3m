#include "GPUUniform_D3D.hpp"
#include "GLUniformID_win8.hpp"



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Generic
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformBool_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void GPUUniformBool_D3D::createD3D11Buffer(){


	D3D11_BUFFER_DESC bdesc;
	ZeroMemory(&bdesc, sizeof(D3D11_BUFFER_DESC));
	bdesc.ByteWidth = sizeof(_valueStruct);
	bdesc.BindFlags = D3D11_BIND_CONSTANT_BUFFER;
	bdesc.Usage = D3D11_USAGE_DYNAMIC;// D3D11_USAGE_DEFAULT;
	bdesc.CPUAccessFlags = D3D11_CPU_ACCESS_WRITE; //NULL
	bdesc.MiscFlags = NULL;
	bdesc.StructureByteStride = NULL;

	if (FAILED(_ngl->getDevice()->CreateBuffer(&bdesc, nullptr,
		&_buffer))){
		ILogger::instance()->logError("Error while creating Bool constant buffer");
		std::string errMsg("Error while creating Bool constant buffer");
		throw std::exception(errMsg.c_str());
	}

}

void GPUUniformBool_D3D::applyChanges(GL* gl){


	GPUUniformValueBool* boolValue = (GPUUniformValueBool*)_value;
	if (_value) {
		_valueStruct.value = 1;
	}
	else{
		_valueStruct.value = 0;
	}

	D3D11_MAPPED_SUBRESOURCE MSData;
	if (SUCCEEDED(_ngl->getDeviceContext()->Map(_buffer, 0, D3D11_MAP_WRITE_DISCARD, 0, &MSData)))
	{
		// copy data over to constant buffer
		memcpy_s(MSData.pData, MSData.RowPitch, &_valueStruct, sizeof(_valueStruct));
		_ngl->getDeviceContext()->Unmap(_buffer, 0);
	}
	else{
		ILogger::instance()->logError("Uniform " + _name + " was not set.");
		std::string errMsg("Error while mapping constant buffer (GPUUniformBool_D3D)");
		throw std::exception(errMsg.c_str());
	}

	GLUniformID_win8* id8 = (GLUniformID_win8*)getID();
	_ngl->getDeviceContext()->VSSetConstantBuffers(id8->getID(), 1, &_buffer);
	_dirty = false;

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformMatrix4f_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUUniformMatrix4f_D3D::createD3D11Buffer(){

	D3D11_BUFFER_DESC bdesc;
	ZeroMemory(&bdesc, sizeof(D3D11_BUFFER_DESC));
	bdesc.ByteWidth = sizeof(float)*16;
	bdesc.BindFlags = D3D11_BIND_CONSTANT_BUFFER;
	bdesc.Usage = D3D11_USAGE_DYNAMIC; //D3D11_USAGE_DEFAULT; 
	bdesc.CPUAccessFlags = D3D11_CPU_ACCESS_WRITE; //NULL
	bdesc.MiscFlags = NULL;
	bdesc.StructureByteStride = NULL;

	if (FAILED(_ngl->getDevice()->CreateBuffer(&bdesc, nullptr,
		&_buffer))){
		ILogger::instance()->logError("Error while creating Matrix44F constant buffer");
		std::string errMsg("Error while creating Matrix44F constant buffer");
		throw std::exception(errMsg.c_str());
	}
}

void GPUUniformMatrix4f_D3D::applyChanges(GL* gl){


	GPUUniformValueMatrix4* matrixValue = (GPUUniformValueMatrix4*)_value;

	/*float* mvp = matrixValue->getMatrixValue()->getRowMajorFloatArray();
	ILogger::instance()->logInfo("Applying Changes to matrix: ");
	for (int i = 0; i < 4; i++){
		ILogger::instance()->logInfo("%f  %f  %f  %f", mvp[i * 4], mvp[i * 4 + 1], mvp[i * 4 + 2], mvp[i * 4+3]);
	}*/

	//_ngl->getDeviceContext()->UpdateSubresource(_buffer, 0, NULL, matrixValue->getMatrixValue()->getRowMajorFloatArray(), 0, 0);

	D3D11_MAPPED_SUBRESOURCE MSData;
	if (SUCCEEDED(_ngl->getDeviceContext()->Map(_buffer, 0, D3D11_MAP_WRITE_DISCARD, 0, &MSData)))
	{
		// copy data over to constant buffer
		memcpy_s(MSData.pData, MSData.RowPitch, matrixValue->getMatrixValue()->getRowMajorFloatArray(), sizeof(float)*16);
		_ngl->getDeviceContext()->Unmap(_buffer, 0);
	}
	else{
		ILogger::instance()->logError("Uniform " + _name + " was not set.");
		std::string errMsg("Error while mapping constant buffer (GPUUniformMatrix4f_D3D)");
		throw std::exception(errMsg.c_str());
	}

	GLUniformID_win8* id8 = (GLUniformID_win8*)getID();
	_ngl->getDeviceContext()->VSSetConstantBuffers(id8->getID(), 1, &_buffer);
	_dirty = false;

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformVec4Float_D3D
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUUniformVec4Float_D3D::createD3D11Buffer(){

	HRESULT hr;

	D3D11_BUFFER_DESC bdesc;
	ZeroMemory(&bdesc, sizeof(D3D11_BUFFER_DESC));
	bdesc.ByteWidth = sizeof(_valueStruct);
	bdesc.BindFlags = D3D11_BIND_CONSTANT_BUFFER;
	bdesc.Usage = D3D11_USAGE_DYNAMIC;// D3D11_USAGE_DEFAULT;
	bdesc.CPUAccessFlags = D3D11_CPU_ACCESS_WRITE; //NULL
	bdesc.MiscFlags = NULL;
	bdesc.StructureByteStride = NULL;
	hr = _ngl->getDevice()->CreateBuffer(&bdesc, nullptr,
		&_buffer);

	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Vec4F constant buffer");
		std::string errMsg("Error while creating Vec4F constant buffer");
		throw std::exception(errMsg.c_str());
	}
}
void GPUUniformVec4Float_D3D::applyChanges(GL* gl){

	GPUUniformValueVec4Float* floatValue = (GPUUniformValueVec4Float*)_value;
	_valueStruct._x = floatValue->getXValue();
	_valueStruct._y = floatValue->getYValue();
	_valueStruct._z = floatValue->getZValue();
	_valueStruct._w = floatValue->getWValue();

	D3D11_MAPPED_SUBRESOURCE MSData;
	if (SUCCEEDED(_ngl->getDeviceContext()->Map(_buffer, 0, D3D11_MAP_WRITE_DISCARD, 0, &MSData)))
	{
		// copy data over to constant buffer
		memcpy_s(MSData.pData, MSData.RowPitch, &_valueStruct, sizeof(_valueStruct));

		_ngl->getDeviceContext()->Unmap(_buffer, 0);
		_dirty = false;
	}
	else{
		ILogger::instance()->logError("Uniform " + _name + " was not set.");
		std::string errMsg("Error while mapping constant buffer (GPUUniformVec4Float_D3D)");
		throw std::exception(errMsg.c_str());
	}

	GLUniformID_win8* id8 = (GLUniformID_win8*)getID();
	_ngl->getDeviceContext()->VSSetConstantBuffers(id8->getID(), 1, &_buffer);
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformVec3Float_D3D
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUUniformVec3Float_D3D::createD3D11Buffer(){

	HRESULT hr;

	D3D11_BUFFER_DESC bdesc;
	ZeroMemory(&bdesc, sizeof(D3D11_BUFFER_DESC));
	bdesc.ByteWidth = sizeof(_valueStruct);
	bdesc.BindFlags = D3D11_BIND_CONSTANT_BUFFER;
	bdesc.Usage = D3D11_USAGE_DYNAMIC;// D3D11_USAGE_DEFAULT;
	bdesc.CPUAccessFlags = D3D11_CPU_ACCESS_WRITE; //NULL
	bdesc.MiscFlags = NULL;
	bdesc.StructureByteStride = NULL;
	hr = _ngl->getDevice()->CreateBuffer(&bdesc, nullptr,
		&_buffer);

	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Vec3F constant buffer");
		std::string errMsg("Error while creating Vec3F constant buffer");
		throw std::exception(errMsg.c_str());
	}
}
void GPUUniformVec3Float_D3D::applyChanges(GL* gl){

	GPUUniformValueVec3Float* floatValue = (GPUUniformValueVec3Float*)_value;
	_valueStruct._x = floatValue->getXValue();
	_valueStruct._y = floatValue->getYValue();
	_valueStruct._z = floatValue->getZValue();

	D3D11_MAPPED_SUBRESOURCE MSData;
	if (SUCCEEDED(_ngl->getDeviceContext()->Map(_buffer, 0, D3D11_MAP_WRITE_DISCARD, 0, &MSData)))
	{
		// copy data over to constant buffer
		memcpy_s(MSData.pData, MSData.RowPitch, &_valueStruct, sizeof(_valueStruct));
		_ngl->getDeviceContext()->Unmap(_buffer, 0);
		_dirty = false;
	}
	else{
		ILogger::instance()->logError("Uniform " + _name + " was not set.");
		std::string errMsg("Error while mapping constant buffer (GPUUniformVec3Float_D3D)");
		throw std::exception(errMsg.c_str());
	}

	GLUniformID_win8* id8 = (GLUniformID_win8*)getID();
	_ngl->getDeviceContext()->VSSetConstantBuffers(id8->getID(), 1, &_buffer);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformVec2Float_D3D
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUUniformVec2Float_D3D::createD3D11Buffer(){

	HRESULT hr;

	D3D11_BUFFER_DESC bdesc;
	ZeroMemory(&bdesc, sizeof(D3D11_BUFFER_DESC));
	bdesc.ByteWidth = sizeof(_valueStruct);
	bdesc.BindFlags = D3D11_BIND_CONSTANT_BUFFER;
	bdesc.Usage = D3D11_USAGE_DYNAMIC;// D3D11_USAGE_DEFAULT;
	bdesc.CPUAccessFlags = D3D11_CPU_ACCESS_WRITE; //NULL
	bdesc.MiscFlags = NULL;
	bdesc.StructureByteStride = NULL;
	hr = _ngl->getDevice()->CreateBuffer(&bdesc, nullptr,
		&_buffer);

	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Vec2F constant buffer");
		std::string errMsg("Error while creating Vec2F constant buffer");
		throw std::exception(errMsg.c_str());
	}
}
void GPUUniformVec2Float_D3D::applyChanges(GL* gl){

	GPUUniformValueVec2Float* floatValue = (GPUUniformValueVec2Float*)_value;
	_valueStruct._x = floatValue->getXValue();
	_valueStruct._y = floatValue->getYValue();

	D3D11_MAPPED_SUBRESOURCE MSData;
	if (SUCCEEDED(_ngl->getDeviceContext()->Map(_buffer, 0, D3D11_MAP_WRITE_DISCARD, 0, &MSData)))
	{
		// copy data over to constant buffer
		memcpy_s(MSData.pData, MSData.RowPitch, &_valueStruct, sizeof(_valueStruct));
		_ngl->getDeviceContext()->Unmap(_buffer, 0);
		_dirty = false;
	}
	else{
		ILogger::instance()->logError("Uniform " + _name + " was not set.");
		std::string errMsg("Error while mapping constant buffer (GPUUniformVec2Float_D3D)");
		throw std::exception(errMsg.c_str());
	}

	GLUniformID_win8* id8 = (GLUniformID_win8*)getID();
	_ngl->getDeviceContext()->VSSetConstantBuffers(id8->getID(), 1, &_buffer);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformFloat_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void GPUUniformFloat_D3D::createD3D11Buffer(){

	HRESULT hr;

	D3D11_BUFFER_DESC bdesc;
	ZeroMemory(&bdesc, sizeof(D3D11_BUFFER_DESC));
	bdesc.ByteWidth = sizeof(_valueStruct);
	bdesc.BindFlags = D3D11_BIND_CONSTANT_BUFFER;
	bdesc.Usage = D3D11_USAGE_DYNAMIC;// D3D11_USAGE_DEFAULT;
	bdesc.CPUAccessFlags = D3D11_CPU_ACCESS_WRITE; //NULL
	bdesc.MiscFlags = NULL;
	bdesc.StructureByteStride = NULL;
	hr = _ngl->getDevice()->CreateBuffer(&bdesc, nullptr,
		&_buffer);

	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Float1 constant buffer");
		std::string errMsg("Error while creating Float1 constant buffer");
		throw std::exception(errMsg.c_str());
	}
}

void GPUUniformFloat_D3D::applyChanges(GL* gl){

	GPUUniformValueFloat* floatValue = (GPUUniformValueFloat*)_value;
	_valueStruct.value = floatValue->getFloatValue();


	D3D11_MAPPED_SUBRESOURCE MSData;
	if (SUCCEEDED(_ngl->getDeviceContext()->Map(_buffer, 0, D3D11_MAP_WRITE_DISCARD, 0, &MSData)))
	{
		// copy data over to constant buffer
		memcpy_s(MSData.pData, MSData.RowPitch, &_valueStruct, sizeof(_valueStruct));
		_ngl->getDeviceContext()->Unmap(_buffer, 0);
		_dirty = false;
	}
	else{
		ILogger::instance()->logError("Uniform " + _name + " was not set.");
		std::string errMsg("Error while mapping constant buffer (GPUUniformFloat_D3D)");
		throw std::exception(errMsg.c_str());
	}
	GLUniformID_win8* id8 = (GLUniformID_win8*)getID();
	_ngl->getDeviceContext()->VSSetConstantBuffers(id8->getID(), 1, &_buffer);
}