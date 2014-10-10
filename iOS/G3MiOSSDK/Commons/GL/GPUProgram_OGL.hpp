//
//  GPUProgram_OGL.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//

#ifndef __G3MiOSSDK__GPUProgram_OGL__
#define __G3MiOSSDK__GPUProgram_OGL__

#include "IGPUProgram.hpp"

class GPUProgram_OGL : public IGPUProgram{

public:
	static GPUProgram_OGL* createProgram(GL* gl,
		const std::string& name,
		const std::string& vertexSource,
		const std::string& fragmentSource);

	//void onUnused(GL* gl);
	void applyChanges(GL* gl);
	~GPUProgram_OGL();

protected:
	void getVariables(GL* gl);

private:
	bool compileShader(GL* gl, int shader, const std::string& source) const;
	bool linkProgram(GL* gl) const;
	void deleteShader(GL* gl, int shader) const;
	void deleteProgram(GL* gl, const IGPUProgram* p);

	

};

#endif