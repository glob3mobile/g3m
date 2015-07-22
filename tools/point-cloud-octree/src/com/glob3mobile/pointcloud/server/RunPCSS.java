

package com.glob3mobile.pointcloud.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;


public class RunPCSS {


   public static void main(final String[] args) throws Exception {
      System.out.println("PCSS 0.1");
      System.out.println("--------\n");


      final int port = 8082;
      final Server server = new Server(port);

      final ServletHandler handler = new ServletHandler();
      server.setHandler(handler);

      handler.addServletWithMapping(PCSSServlet.class, "/*");


      server.start();
      server.join();

   }


}
