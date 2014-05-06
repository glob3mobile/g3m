

package org.glob3.mobile.generated;

//
//  RCObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//

//
//  RCObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//


public class RCObject {
   private int     _referenceCounter;
   private boolean _suicided = false;
   private boolean _disposed = false;


   private void _suicide() {
      _suicided = true;
      this.dispose();
   }


   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //  RCObject(RCObject that);

   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //  RCObject operator =(RCObject that);

   protected RCObject() // the object starts retained
   {
      _referenceCounter = 1;

   }


   public void dispose() {
      _disposed = true;
      if (_referenceCounter != 0) {
         ILogger.instance().logError("DELETING RCOBJECT WITH UNRELEASED REFERENCES!");
      }
   }


   public final void _retain() {
      _referenceCounter++;
   }


   public final void _release() {
      if (--_referenceCounter == 0) {
         _suicide();
      }
      if (_referenceCounter < 0) {
         ILogger.instance().logError(
                  "RCObject: \"" + this + "\". Invalid _release(), _referenceCounter=" + _referenceCounter + ", _suicided="
                           + _suicided + ", _disposed=" + _disposed);
      }
   }


   @Override
   protected void finalize() throws Throwable {
      if (_referenceCounter != 0) {
         ILogger.instance().logError(
                  "RCObject: \"" + this + "\" finalized with invalid _referenceCounter=" + _referenceCounter + ", _suicided="
                           + _suicided + ", _disposed=" + _disposed);
      }
      super.finalize();
   }

}
