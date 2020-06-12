package com.databorough.utils;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import acdemxaMvcprocess.logic.data.ProgramControlData;

import static com.databorough.utils.AS400Utils.getJobUser;
import static com.databorough.utils.ServletContextProvider.getAttribute;
import static com.databorough.utils.ServletContextProvider.removeAttribute;

/**
 * Serves common application resources to REST clients.
 *
 * @author Robin Rizvi
 * @since (2014-05-29.18:26:08)
 */
@Path("/application")
public class ServiceUtils {
	@GET
	@Path("/checkPdfReport")
	public Response checkPdfReport() {
		String pdfFile = (String)getAttribute("report" + getJobUser());

		if (pdfFile == null) {
			return Response.serverError().build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("/pdfReport")
	@Produces("application/pdf")
	public Response getPdfReport() {
		String attr = "report" + getJobUser();
		String pdfFile = (String)getAttribute(attr);
		removeAttribute(attr);

		File file = new File(pdfFile);

		ResponseBuilder response = Response.ok((Object)file);
		response.header("Content-Disposition", "inline; filename=report.pdf");

		return response.build();
	}

	@GET
	@Path("/programControlData")
	@Produces(MediaType.APPLICATION_JSON)
	public ProgramControlData getProgramControlData() {
		return new ProgramControlData();
	}
}
