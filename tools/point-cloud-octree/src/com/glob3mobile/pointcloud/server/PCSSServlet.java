

package com.glob3mobile.pointcloud.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PCSSServlet
         extends
            HttpServlet {

   private static final long serialVersionUID = 1L;


   @Override
   public void init(final ServletConfig config) throws ServletException {
      super.init(config);
      log("initialization of " + getClass());
   }


   @Override
   protected void doGet(final HttpServletRequest request,
                        final HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println("<h1>Hello SimpleServlet, áéíóúñÑ</h1>");
   }


   @Override
   public void destroy() {
      super.destroy();
      log("destroying " + getClass());
   }

}
