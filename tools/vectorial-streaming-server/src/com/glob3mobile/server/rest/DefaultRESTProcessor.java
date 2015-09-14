

package com.glob3mobile.server.rest;

import static com.glob3mobile.server.rest.ServerErrorCodes.DELETE_NOT_SUPPORTED;
import static com.glob3mobile.server.rest.ServerErrorCodes.GET_NOT_SUPPORTED;
import static com.glob3mobile.server.rest.ServerErrorCodes.PARAMETER_INVALID_RANGE;
import static com.glob3mobile.server.rest.ServerErrorCodes.POST_NOT_SUPPORTED;
import static com.glob3mobile.server.rest.ServerErrorCodes.PUT_NOT_SUPPORTED;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


public abstract class DefaultRESTProcessor
   implements
      RESTProcessor {


   private final int _notSupportedResponseStatus;


   protected DefaultRESTProcessor(final int notSupportedResponseStatus) {
      _notSupportedResponseStatus = notSupportedResponseStatus;
   }


   @SuppressWarnings("unused")
   @Override
   public void initialize(final ServletConfig servletConfig) throws ServletException {
   }


   @Override
   public void destroy() {
   }


   @Override
   public RESTResponse doGet(final RESTPath path,
                             final HttpServletRequest request) throws RESTException {

      throw new RESTJSONErrorException(_notSupportedResponseStatus, GET_NOT_SUPPORTED);
   }


   @Override
   public RESTResponse doPost(final RESTPath path,
                              final HttpServletRequest request) throws RESTException {
      throw new RESTJSONErrorException(_notSupportedResponseStatus, POST_NOT_SUPPORTED);
   }


   @Override
   public RESTResponse doPut(final RESTPath path,
                             final HttpServletRequest request) throws RESTException {
      throw new RESTJSONErrorException(_notSupportedResponseStatus, PUT_NOT_SUPPORTED);
   }


   @Override
   public RESTResponse doDelete(final RESTPath path,
                                final HttpServletRequest request) throws RESTException {
      throw new RESTJSONErrorException(_notSupportedResponseStatus, DELETE_NOT_SUPPORTED);
   }


   static protected String getFileName(final Part part) {
      final String contentDisposition = part.getHeader("content-disposition");
      for (final String content : contentDisposition.split(";")) {
         if (content.trim().startsWith("filename")) {
            return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
         }
      }
      return null;
   }


   protected static int toInt(final long size) throws RESTException {
      if ((size < Integer.MIN_VALUE) || (size > Integer.MAX_VALUE)) {
         throw new RESTJSONErrorException( //
                  HttpServletResponse.SC_OK, //
                  PARAMETER_INVALID_RANGE, //
                  "size is out of Integer range");
      }
      return (int) size;
   }


}
