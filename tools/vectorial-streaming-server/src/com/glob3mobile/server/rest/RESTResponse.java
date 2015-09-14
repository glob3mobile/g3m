

package com.glob3mobile.server.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface RESTResponse {

   public void sendTo(final HttpServletResponse response) throws IOException;

}
