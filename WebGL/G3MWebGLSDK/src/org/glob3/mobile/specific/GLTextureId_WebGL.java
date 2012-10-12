

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IGLTextureId;

import com.google.gwt.core.client.JavaScriptObject;


public final class GLTextureId_WebGL
         implements
            IGLTextureId {

   private final JavaScriptObject _webGLTexture; //WebGLTexture


   public GLTextureId_WebGL(final JavaScriptObject webGLTexture) {
      _webGLTexture = webGLTexture;
   }


   @Override
   public boolean isEqualsTo(final IGLTextureId that) {
      return jsIsEqualsTo(((GLTextureId_WebGL) that).getWebGLTexture());
   }


   private native boolean jsIsEqualsTo(final JavaScriptObject that) /*-{
		return (this.@org.glob3.mobile.specific.GLTextureId_WebGL::_webGLTexture.id == that.id);
   }-*/;


   public JavaScriptObject getWebGLTexture() {
      return _webGLTexture;
   }


   @Override
   public String description() {
      return "GLTextureId_WebGL " + jsGetWebGLTextureId();
   }


   private native int jsGetWebGLTextureId() /*-{
		return this.@org.glob3.mobile.specific.GLTextureId_WebGL::_webGLTexture.id;
   }-*/;

}
