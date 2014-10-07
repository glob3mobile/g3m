


#ifndef __G3MWindowsSDK_G3MWidget_win8__
#define __G3MWindowsSDK_G3MWidget_win8__

class G3MWidget;
class D3DRenderer;
class NativeGL_win8;


#include "GL.hpp"

class G3MWidget_win8{
private:
	G3MWidget* _genericWidget;
	D3DRenderer* _renderer;
	

public:
	G3MWidget_win8();
	GL* getGL();
	D3DRenderer* getRenderer();
	void initSingletons(NativeGL_win8* ngl) const;
	void setWidget(G3MWidget* genericWidget);
};


#endif