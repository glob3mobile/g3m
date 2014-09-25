//
//  GLAttribute_D3D.hpp
//  G3MWindowsSDK

#ifndef __G3MWindowsSDK_GLAttribute_D3D__
#define __G3MWindowsSDK_GLAttribute_D3D__



#include <DirectXMath.h>

#include "GPUAttribute.hpp"
#include "NativeGL_win8.hpp"

class GPUAttribute_D3D : public GPUAttribute {

protected:
	NativeGL_win8* _ngl;
	ID3D11Buffer* _buffer = NULL;
	int _bufferSize = -1;
	D3D11_INPUT_ELEMENT_DESC _ied;
	int _semanticIndex;

public:
	GPUAttribute_D3D(const std::string&name, int id, int type, int size, int semanticIndex, NativeGL_win8* ngl) :GPUAttribute(name, id, type, size){
		_semanticIndex = semanticIndex;
		_ngl = ngl;
	}

	virtual ~GPUAttribute_D3D(){
		_buffer->Release();
	}

	D3D11_INPUT_ELEMENT_DESC getIED(){
		return _ied;
	}

	ID3D11Buffer* getBuffer(){
		return _buffer;
	}

	int getOffset(){
		return _value->_index;
	}

	void setDirty(boolean isDirty){
		_dirty = isDirty;
	}

	virtual int getStride() = 0;
	virtual void setInputLayoutDesc() = 0;
	void set(const GPUAttributeValue* v);
	virtual void createD3D11Buffer();
	virtual void updateBuffer() = 0;

};


class GPUAttributeVec1Float_D3D : public GPUAttribute_D3D{

public:
	GPUAttributeVec1Float_D3D(const std::string&name, int id, int semanticIndex, NativeGL_win8* ngl) :GPUAttribute_D3D(name, id, GLType::glFloat(), 1, semanticIndex, ngl) {
		setInputLayoutDesc();
	}

	int getStride(){
		return sizeof(float);
	}

	void setInputLayoutDesc();
	void updateBuffer();
};

class GPUAttributeVec2Float_D3D : public GPUAttribute_D3D{

public:
	GPUAttributeVec2Float_D3D(const std::string&name, int id, int semanticIndex, NativeGL_win8* ngl) :GPUAttribute_D3D(name, id, GLType::glFloat(), 2, semanticIndex, ngl) {
		setInputLayoutDesc();
	}

	int getStride(){
		return sizeof(float)*2;
	}

	void setInputLayoutDesc();
	void updateBuffer();
};

class GPUAttributeVec3Float_D3D : public GPUAttribute_D3D{

public:
	GPUAttributeVec3Float_D3D(const std::string&name, int id, int semanticIndex, NativeGL_win8* ngl) :GPUAttribute_D3D(name, id, GLType::glFloat(), 3, semanticIndex, ngl) {
		setInputLayoutDesc();
	}

	int getStride(){
		return sizeof(float)*3;
	}

	void setInputLayoutDesc();
	void updateBuffer();
};

class GPUAttributeVec4Float_D3D : public GPUAttribute_D3D{

public:
	GPUAttributeVec4Float_D3D(const std::string&name, int id, int semanticIndex, NativeGL_win8* ngl) :GPUAttribute_D3D(name, id, GLType::glFloat(), 4, semanticIndex, ngl) {
		setInputLayoutDesc();
	}

	int getStride(){
		return sizeof(float)*4;
	}

	void setInputLayoutDesc();
	void createD3D11Buffer();
	void updateBuffer();
};

#endif