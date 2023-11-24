
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;
import com.google.gwt.core.client.*;

public final class GLTextureID_WebGL implements IGLTextureID {

   private final JavaScriptObject _webGLTexture; //WebGLTexture

   public GLTextureID_WebGL(final JavaScriptObject webGLTexture) {
      _webGLTexture = webGLTexture;
   }

   @Override
   public boolean isEquals(final IGLTextureID that) {
      return (_webGLTexture.equals(((GLTextureID_WebGL) that).getWebGLTexture()));
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
