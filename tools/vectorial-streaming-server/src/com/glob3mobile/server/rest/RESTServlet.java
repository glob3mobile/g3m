

package com.glob3mobile.server.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RESTServlet
   extends
      HttpServlet {

   private static final long serialVersionUID = 1L;


   private RESTProcessor     _processor;


   @Override
   public void init() throws ServletException {
      final String processorClassName = getInitParameter("processor");
      if (processorClassName == null) {
         throw new ServletException("init parameter 'processor' is required.");
      }

      _processor = createProcessor(processorClassName);
   }


   @Override
   public void destroy() {
      if (_processor != null) {
         _processor.destroy();
         _processor = null;
      }
   }


   private RESTProcessor createProcessor(final String className) throws ServletException {
      try {
         final Class<?> klass = Class.forName(className);

         final Object instance = klass.newInstance();

         if (instance instanceof RESTProcessor) {
            final RESTProcessor processor = (RESTProcessor) instance;
            processor.initialize(getServletConfig());
            return processor;
         }

         throw new ServletException("Processor (class " + klass + ") must implements " + RESTProcessor.class);
      }
      catch (final ClassNotFoundException e) {
         throw new ServletException(e);
      }
      catch (final InstantiationException e) {
         throw new ServletException(e);
      }
      catch (final IllegalAccessException e) {
         throw new ServletException(e);
      }
   }


   private boolean tryAlternateRequestType(final RESTPath path,
                                           final HttpServletRequest request,
                                           final HttpServletResponse response) throws IOException, RESTException {
      if ((path != null) && !path.isEmpty()) {
         final String requestType = path.getLast();
         if (requestType.equals("_GET_")) {
            _processor.doGet(path.removeLast(), request).sendTo(response);
            return true;
         }
         else if (requestType.equals("_POST_")) {
            _processor.doPost(path.removeLast(), request).sendTo(response);
            return true;
         }
         else if (requestType.equals("_PUT_")) {
            _processor.doPut(path.removeLast(), request).sendTo(response);
            return true;
         }
         else if (requestType.equals("_DELETE_")) {
            _processor.doDelete(path.removeLast(), request).sendTo(response);
            return true;
         }
      }
      return false;
   }


   @Override
   protected void doGet(final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {
      response.addHeader("Access-Control-Allow-Origin", "*");
      final RESTPath path = RESTPath.create(request);
      try {
         final boolean handled = tryAlternateRequestType(path, request, response);
         if (!handled) {
            _processor.doGet(path, request).sendTo(response);
         }
      }
      catch (final RESTException e) {
         e.sendTo(response);
      }
   }


   @Override
   protected void doPost(final HttpServletRequest request,
                         final HttpServletResponse response) throws IOException {
      response.addHeader("Access-Control-Allow-Origin", "*");
      final RESTPath path = RESTPath.create(request);
      try {
         _processor.doPost(path, request).sendTo(response);
      }
      catch (final RESTException e) {
         e.sendTo(response);
      }
   }


   @Override
   protected void doPut(final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {
      response.addHeader("Access-Control-Allow-Origin", "*");
      final RESTPath path = RESTPath.create(request);
      try {
         _processor.doPut(path, request).sendTo(response);
      }
      catch (final RESTException e) {
         e.sendTo(response);
      }
   }


   @Override
   protected void doDelete(final HttpServletRequest request,
                           final HttpServletResponse response) throws IOException {
      response.addHeader("Access-Control-Allow-Origin", "*");
      final RESTPath path = RESTPath.create(request);
      try {
         _processor.doDelete(path, request).sendTo(response);
      }
      catch (final RESTException e) {
         e.sendTo(response);
      }
   }


}
