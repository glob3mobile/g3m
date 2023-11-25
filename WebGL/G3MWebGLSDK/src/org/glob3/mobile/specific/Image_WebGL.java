
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;
import com.google.gwt.core.client.*;

public final class Image_WebGL extends IImage {

   private JavaScriptObject _imgObject;       //IMAGE JS
   private JsArrayNumber    _imageData = null;

   public Image_WebGL(final JavaScriptObject data) {
      _imgObject = data;
      if ((getWidth() <= 0) || (getHeight() <= 0)) {
         _imgObject = null;
      }
   }

   public JavaScriptObject getImage() {
      return _imgObject;
   }

   @Override
   public native int getWidth() /*-{
    var jsImage = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
    return jsImage ? jsImage.width : 0;
   }-*/;

   @Override
   public native int getHeight() /*-{
    var jsImage = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
    return jsImage ? jsImage.height : 0;
   }-*/;

   @Override
   public Vector2I getExtent() {
      return new Vector2I(getWidth(), getHeight());
   }

   @Override
   public String description() {
      return "Image_WebGL " + getWidth() + " x " + getHeight() + ", _image=(" + _imgObject + ")";
   }

   @Override
   public IImage shallowCopy() {
      return new Image_WebGL(_imgObject);
   }

   @Override
   public boolean isPremultiplied() {
      return false;
   }

   private static native JavaScriptObject createCanvas() /*-{
    return $doc.createElement("canvas");
   }-*/;

   private static native JavaScriptObject getContext2D(final JavaScriptObject canvas) /*-{
    return canvas.getContext("2d");
   }-*/;

   @Override
   public void getPixel(final int x, final int y, final MutableColor255 pixel) {
      if (_imageData == null) {
         final JavaScriptObject domCanvas        = createCanvas();
         final JavaScriptObject domCanvasContext = getContext2D(domCanvas);
         _imageData = createImageData(domCanvas, domCanvasContext);
      }
      final int i = (y * getWidth()) + x;
      pixel._red   = (byte) _imageData.get(i);
      pixel._green = (byte) _imageData.get(i + 1);
      pixel._blue  = (byte) _imageData.get(i + 2);
      pixel._alpha = (byte) _imageData.get(i + 3);
   }

   private native JsArrayNumber createImageData(final JavaScriptObject domCanvas, JavaScriptObject domCanvasContext)/*-{
    var w = this.@org.glob3.mobile.specific.Image_WebGL::getWidth();
    var h = this.@org.glob3.mobile.specific.Image_WebGL::getHeight();
    domCanvas.width = w;
    domCanvas.height = h;

    var imageJS = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
    domCanvasContext.drawImage(imageJS, 0, 0, w, h);

    var imgData = domCanvasContext.getImageData(0, 0, w, h);
    return imgData;
   }-*/;

}
