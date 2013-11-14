

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IGLTextureId;


public final class GLTextureId_Android
         implements
            IGLTextureId {

   private final int _glTextureId;


   //   public GLTextureId_Android() {
   //      _glTextureId = -1;
   //   }


   GLTextureId_Android(final int textureId) {
      _glTextureId = textureId;
   }


   //   public GLTextureId_Android(final GLTextureId_Android that) {
   //      _glTextureId = that.getGLTextureId();
   //   }


   @Override
   public boolean isEquals(final IGLTextureId that) {
      return (_glTextureId == ((GLTextureId_Android) that).getGLTextureId());
   }


   @Override
   public String description() {
      return "GLTextureId_Android " + _glTextureId;
   }


   public int getGLTextureId() {
      return _glTextureId;
   }


   @Override
   public void dispose() {

   }

}
