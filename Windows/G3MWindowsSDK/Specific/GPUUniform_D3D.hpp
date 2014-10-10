//
//  GPUUniform_D3D.hpp
//  G3MWindowsSDK
//
//
//

#ifndef __G3MWindowsSDK_GPUUniform_D3D__
#define __G3MWindowsSDK_GPUUniform_D3D__

// For aligning to float4 boundaries
#define Float4Align __declspec(align(16))

#include "GPUUniform.hpp"
#include "NativeGL_win8.hpp"

class GPUUniform_D3D :public GPUUniform{
protected:
	NativeGL_win8* _ngl;
	ID3D11Buffer* _buffer;

public:
	GPUUniform_D3D(const std::string& name, IGLUniformID* id, int type, NativeGL_win8* ngl) :GPUUniform(name, id, type) {
		_ngl = ngl;		
	}

	virtual void createD3D11Buffer() = 0;
	virtual void applyChanges(GL* gl) = 0;
	void unset();
	virtual ~GPUUniform_D3D(){
		_buffer->Release();
		delete _buffer;
#ifdef JAVA_CODE
		super.dispose();
#endif
	}

};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformBool_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class GPUUniformBool_D3D : public GPUUniform_D3D {
	struct cbBool
	{
		Float4Align int value;
	};

private:
	cbBool _valueStruct;

public:
	GPUUniformBool_D3D(const std::string&name, IGLUniformID* id, NativeGL_win8* ngl) :GPUUniform_D3D(name, id, GLType::glBool(), ngl) {
		createD3D11Buffer();
	}
	~GPUUniformBool_D3D() {}
	void createD3D11Buffer();
	void applyChanges(GL* gl);
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformMatrix4f_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class GPUUniformMatrix4f_D3D : public GPUUniform_D3D{

public:
	GPUUniformMatrix4f_D3D(const std::string&name, IGLUniformID* id, NativeGL_win8* ngl) :GPUUniform_D3D(name, id, GLType::glMatrix4Float(), ngl) {
		createD3D11Buffer();
	}
	~GPUUniformMatrix4f_D3D(){}
	void createD3D11Buffer();
	void applyChanges(GL* gl);
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformVec4Float_D3D
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class GPUUniformVec4Float_D3D : public GPUUniform_D3D{
	struct cbFloat4
	{
		float _x;
		float _y;
		float _z;
		float _w;
	};
private:
	cbFloat4 _valueStruct;

public:
	GPUUniformVec4Float_D3D(const std::string&name, IGLUniformID* id, NativeGL_win8* ngl) :GPUUniform_D3D(name, id, GLType::glVec4Float(), ngl){
		createD3D11Buffer();
	}
	~GPUUniformVec4Float_D3D(){}
	void createD3D11Buffer();
	void applyChanges(GL* gl);
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformVec3Float_D3D
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class GPUUniformVec3Float_D3D : public GPUUniform_D3D{
	struct cbFloat3
	{
		Float4Align float _x;
		Float4Align float _y;
		Float4Align float _z;
	};
private:
	cbFloat3 _valueStruct;

public:
	GPUUniformVec3Float_D3D(const std::string&name, IGLUniformID* id, NativeGL_win8* ngl) :GPUUniform_D3D(name, id, GLType::glVec3Float(), ngl){
		createD3D11Buffer();
	}
	~GPUUniformVec3Float_D3D(){}
	void createD3D11Buffer();
	void applyChanges(GL* gl);
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformVec2Float_D3D
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class GPUUniformVec2Float_D3D : public GPUUniform_D3D{
	struct cbFloat2
	{
		Float4Align float _x;
		Float4Align float _y;
	};
private:
	cbFloat2 _valueStruct;

public:
	GPUUniformVec2Float_D3D(const std::string&name, IGLUniformID* id, NativeGL_win8* ngl) :GPUUniform_D3D(name, id, GLType::glVec2Float(), ngl){
		createD3D11Buffer();
	}
	~GPUUniformVec2Float_D3D(){}
	void createD3D11Buffer();
	void applyChanges(GL* gl);
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//GPUUniformFloat_D3D
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class GPUUniformFloat_D3D : public GPUUniform_D3D{
	struct cbFloat
	{
		Float4Align float value;
	};

private:
	cbFloat _valueStruct;

public:
	GPUUniformFloat_D3D(const std::string&name, IGLUniformID* id, NativeGL_win8* ngl) :GPUUniform_D3D(name, id, GLType::glFloat(), ngl) {
		createD3D11Buffer();
	}
	~GPUUniformFloat_D3D(){}
	void createD3D11Buffer();
	void applyChanges(GL* gl);
};

#endif