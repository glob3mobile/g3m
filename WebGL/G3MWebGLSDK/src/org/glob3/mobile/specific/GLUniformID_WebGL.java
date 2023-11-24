
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;
import com.google.gwt.core.client.*;

public final class GLUniformID_WebGL implements IGLUniformID {

   private final JavaScriptObject _id;

   GLUniformID_WebGL(final JavaScriptObject id) {
      _id = id;
   }

   public JavaScriptObject getId() {
      return _id;
   }

   @Override
   public boolean isValid() {
      return _id != null;
   }

   @Override
   public void dispose() {

   }

}
