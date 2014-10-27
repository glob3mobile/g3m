#include "Canvas_win8.hpp"


void Canvas_win8::_initialize(int width, int height){
	

	D2D1_SIZE_U size;
	size.width = width;
	size.height = height;

	float dpiX, dpiY;
	_ngl->getD2DFactory()->GetDesktopDpi(&dpiX, &dpiY);

	D2D1_BITMAP_PROPERTIES1 props = D2D1::BitmapProperties1(D2D1_BITMAP_OPTIONS_TARGET,
	D2D1::PixelFormat(DXGI_FORMAT::DXGI_FORMAT_B8G8R8A8_UNORM, D2D1_ALPHA_MODE_PREMULTIPLIED),
		dpiX, dpiY);

	UINT pitch = width * 32;

	if (FAILED(_ngl->getD2DDeviceContext()->CreateBitmap(size, NULL, pitch, props, &_bitmap))){
		std::string errMsg("TODO: Implementation");
		throw std::exception(errMsg.c_str());
	}

	_ngl->getD2DDeviceContext()->SetTarget(_bitmap);
	
}
void Canvas_win8::_setFillColor(const Color& color){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_setLineColor(const Color& color){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_setLineWidth(float width){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_setLineCap(StrokeCap cap){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_setLineJoin(StrokeJoin join){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_setLineMiterLimit(float limit){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_setLineDash(float lengths[], int count, float phase){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_fillRectangle(float left, float top, float width, float height){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_strokeRectangle(float left, float top, float width, float height){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_fillAndStrokeRectangle(float left, float top, float width, float height){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_fillRoundedRectangle(float left, float top, float width, float height, float radius){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_strokeRoundedRectangle(float left, float top, float width, float height, float radius){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_fillAndStrokeRoundedRectangle(float left, float top, float width, float height, float radius){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_setShadow(const Color& color, float blur, float offsetX, float offsetY){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_removeShadow(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_clearRect(float left, float top, float width, float height){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_createImage(IImageListener* listener, bool autodelete){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_setFont(const GFont& font){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
const Vector2F Canvas_win8::_textExtent(const std::string& text){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_fillText(const std::string& text, float left, float top){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_drawImage(const IImage* image, float destLeft, float destTop){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_drawImage(const IImage* image, float destLeft, float destTop, float transparency){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_drawImage(const IImage* image, float destLeft, float destTop, float destWidth, float destHeight){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_drawImage(const IImage* image, float destLeft, float destTop, float destWidth, float destHeight, float transparency){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_drawImage(const IImage* image,
	float srcLeft, float srcTop, float srcWidth, float srcHeight,
	float destLeft, float destTop, float destWidth, float destHeight){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_drawImage(const IImage* image,
	float srcLeft, float srcTop, float srcWidth, float srcHeight,
	float destLeft, float destTop, float destWidth, float destHeight,
	float transparency){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_beginPath(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_closePath(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_stroke(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_fill(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_fillAndStroke(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_moveTo(float x, float y){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}
void Canvas_win8::_lineTo(float x, float y){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}