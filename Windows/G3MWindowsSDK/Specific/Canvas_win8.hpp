//
//  Canvas_win8.hpp
//  G3MWindowsSDK
//
//  
//

#ifndef __G3MWindowsSDK_Canvas_win8__
#define __G3MWindowsSDK_Canvas_win8__

#include "ICanvas.hpp"
#include "NativeGL_win8.hpp"

class Canvas_win8 :public ICanvas{
private:
	NativeGL_win8* _ngl;
	ID2D1Bitmap1* _bitmap = NULL;
protected:
	void _initialize(int width, int height);
	void _setFillColor(const Color& color);
	void _setLineColor(const Color& color);
	void _setLineWidth(float width);
	void _setLineCap(StrokeCap cap);
	void _setLineJoin(StrokeJoin join);
	void _setLineMiterLimit(float limit);
	void _setLineDash(float lengths[], int count, float phase);
	void _fillRectangle(float left, float top, float width, float height);
	void _strokeRectangle(float left, float top, float width, float height);
	void _fillAndStrokeRectangle(float left, float top, float width, float height);
	void _fillRoundedRectangle(float left, float top, float width, float height, float radius);
	void _strokeRoundedRectangle(float left, float top, float width, float height, float radius);
	void _fillAndStrokeRoundedRectangle(float left, float top, float width, float height, float radius);
	void _setShadow(const Color& color, float blur, float offsetX, float offsetY);
	void _removeShadow();
	void _clearRect(float left, float top, float width, float height);
	void _createImage(IImageListener* listener, bool autodelete);
	void _setFont(const GFont& font);
	const Vector2F _textExtent(const std::string& text);
	void _fillText(const std::string& text, float left, float top);
	void _drawImage(const IImage* image, float destLeft, float destTop);
	void _drawImage(const IImage* image, float destLeft, float destTop, float transparency);
	void _drawImage(const IImage* image, float destLeft, float destTop, float destWidth, float destHeight);
	void _drawImage(const IImage* image, float destLeft, float destTop, float destWidth, float destHeight, float transparency);
	void _drawImage(const IImage* image,
		float srcLeft, float srcTop, float srcWidth, float srcHeight,
		float destLeft, float destTop, float destWidth, float destHeight);
	void _drawImage(const IImage* image,
		float srcLeft, float srcTop, float srcWidth, float srcHeight,
		float destLeft, float destTop, float destWidth, float destHeight,
		float transparency);
	void _beginPath();
	void _closePath();
	void _stroke();
	void _fill();
	void _fillAndStroke();
	void _moveTo(float x, float y);
	void _lineTo(float x, float y);
public:
	Canvas_win8(NativeGL_win8* ngl) :
		_ngl(ngl){

	}


};

#endif