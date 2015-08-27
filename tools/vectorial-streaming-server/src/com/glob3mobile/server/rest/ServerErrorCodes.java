

package com.glob3mobile.server.rest;


public class ServerErrorCodes {

   // no need to translate, there are development-only messages
   public static final String GET_NOT_SUPPORTED       = "GET_NOT_SUPPORTED";
   public static final String POST_NOT_SUPPORTED      = "POST_NOT_SUPPORTED";
   public static final String PUT_NOT_SUPPORTED       = "PUT_NOT_SUPPORTED";
   public static final String DELETE_NOT_SUPPORTED    = "DELETE_NOT_SUPPORTED";


   public static final String PARAMETER_INVALID_RANGE = "PARAMETER_INVALID_RANGE";
   //   public static final String PARAMETER_FORMAT_ERROR  = "PARAMETER_FORMAT_ERROR";

   public static final String INVALID_OPERATION       = "INVALID_OPERATION";
   //   public static final String PARAMETER_MISSING       = "PARAMETER_MISSING";
   //
   //
   //   // translatable errors
   //   public static final String LOGIN_ERROR             = "LOGIN_ERROR";
   //   public static final String UNAUTHORIZED            = "UNAUTHORIZED";
   //
   //   public static final String CREATION_ERROR          = "CREATION_ERROR";
   //
   //   public static final String PERSISTENCY_ERROR       = "PERSISTENCY_ERROR";
   //
   //   public static final String DUPLICATION             = "DUPLICATION";
   //   public static final String LOGIC_ERROR             = "LOGIC_ERROR";
   public static final String NOT_FOUND               = "NOT_FOUND";


   private ServerErrorCodes() {
   }


}
