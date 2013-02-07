

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
      return (_webGLTexture.equals(((GLTextureId_WebGL) that).getWebGLTexture()));
   }


   public JavaScriptObject getWebGLTexture() {
      return _webGLTexture;
   }


   @Override
   public String description() {
      return "GLTextureId_WebGL " + _webGLTexture.hashCode();
   }


   @Override
   public void dispose() {

   }

}
