

package com.glob3mobile.vectorial.server;


import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;


public class VectorialStreamingRESTProcessor
   extends
      AbstractVectorialStreamingRESTProcessor {


   private File _lodStorageDirectory;


   public VectorialStreamingRESTProcessor() {
      super(SC_OK);
   }


   @Override
   public void initialize(final ServletConfig servletConfig) throws ServletException {
      super.initialize(servletConfig);

      final String lodStorageDirectoryName = servletConfig.getInitParameter("lod_storage_directory");
      if ((lodStorageDirectoryName == null) || lodStorageDirectoryName.isEmpty()) {
         throw new ServletException("Init parameter 'lod_storage_directory' is required.");
      }

      _lodStorageDirectory = new File(lodStorageDirectoryName);
      validateDirectory(_lodStorageDirectory);
   }


   @Override
   public void destroy() {
      _lodStorageDirectory = null;

      super.destroy();
   }


   @Override
   protected File getDirectoryFor(final String lodName) {
      return _lodStorageDirectory;
   }


}
