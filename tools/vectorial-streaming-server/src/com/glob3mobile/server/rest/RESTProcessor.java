

package com.glob3mobile.server.rest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


public interface RESTProcessor {


   public void initialize(final ServletConfig servletConfig) throws ServletException;


   public RESTResponse doGet(final RESTPath path,
                             final HttpServletRequest request) throws RESTException;


   public RESTResponse doPost(final RESTPath path,
                              final HttpServletRequest request) throws RESTException;


   public RESTResponse doPut(final RESTPath path,
                             final HttpServletRequest request) throws RESTException;


   public RESTResponse doDelete(final RESTPath path,
                                final HttpServletRequest request) throws RESTException;


   public void destroy();


}
