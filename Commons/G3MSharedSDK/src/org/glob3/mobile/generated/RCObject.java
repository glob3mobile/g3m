

package org.glob3.mobile.generated;

import java.util.ArrayList;
import java.util.List;


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
   private int                             _referenceCounter;
   private boolean                         _suicided          = false;
   private boolean                         _disposed          = false;


   private final StackTraceElement[]       _instanciationCallStack;
   private final List<StackTraceElement[]> _retainCallstacks  = new ArrayList<StackTraceElement[]>();
   private final List<StackTraceElement[]> _releaseCallstacks = new ArrayList<StackTraceElement[]>();


   private void _suicide() {
      _suicided = true;
      this.dispose();
   }


   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //  RCObject(RCObject that);

   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
   //  RCObject operator =(RCObject that);


   public void dispose() {
      _disposed = true;
      if (_referenceCounter != 0) {
         ILogger.instance().logError("DELETING RCOBJECT WITH UNRELEASED REFERENCES!");
      }
   }


   protected RCObject() // the object starts retained
   {
      _referenceCounter = 1;
      if (this instanceof TileImageContribution) {
         _instanciationCallStack = new Throwable().getStackTrace();
      }
      else {
         _instanciationCallStack = null;
      }
   }


   public final void _retain() {
      _referenceCounter++;
      if (this instanceof TileImageContribution) {
         _retainCallstacks.add(new Throwable().getStackTrace());
      }
   }


   public final void _release() {
      if (this instanceof TileImageContribution) {
         _releaseCallstacks.add(new Throwable().getStackTrace());
      }

      if (--_referenceCounter == 0) {
         _suicide();
      }
      if (_referenceCounter < 0) {
         showLogicError("Invalid _release()");
      }
   }


   @Override
   protected void finalize() throws Throwable {
      if (_referenceCounter != 0) {
         showLogicError("Finalized with invalid _referenceCounter");
      }
      super.finalize();
   }


   private void showLogicError(final String string) {
      if (this instanceof TileImageContribution) {
         final ILogger logger = ILogger.instance();

         //      logger.logError("RCObject: \"" + this + "\" " + string + ", _referenceCounter=" + _referenceCounter + ", _suicided="
         //                      + _suicided + ", _disposed=" + _disposed);

         logger.logError("=================================================");
         logger.logError("RCObject: \"" + this + "\" " + string + ", _referenceCounter=" + _referenceCounter + ", _suicided="
                         + _suicided + ", _disposed=" + _disposed);
         logger.logError("Instanciated at: ");
         for (final StackTraceElement e : _instanciationCallStack) {
            logger.logError("  " + e.toString());
         }
         logger.logError("Retained at: ");
         for (final StackTraceElement[] each : _retainCallstacks) {
            for (final StackTraceElement e : each) {
               logger.logError("  " + e.toString());
            }
            logger.logError("---");
         }
         logger.logError("Released at: ");
         for (final StackTraceElement[] each : _releaseCallstacks) {
            for (final StackTraceElement e : each) {
               logger.logError("  " + e.toString());
            }
            logger.logError("---");
         }
         logger.logError("=================================================");
      }

   }

}
