package main.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.client.RestTemplate;

import main.model.Document;
import main.model.documentstatus.*;
import mongodb.dao.DocumentDAO;

@Named
@Path("/")
public class DocumentRest{
	

	private Document document;
	private static List<Document> documents = new ArrayList<Document>();
	private ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
	private DocumentDAO documentDAO = ctx.getBean("documentDAO", DocumentDAO.class);
	
	@Inject
	private RestTemplate restTemplate;
	
	public Response okStatus(Object obj){
		return Response.status(200).entity(obj).build();
	}
	
	public Response notFoundStatus(Object obj){
		return Response.status(404).entity(obj).build();
	}
	
	
	@GET
	@Path("getalldocuments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDocuments() {
		System.out.println("GET Request: getalldocuments");
		documents = documentDAO.getAllDocuments();
		return okStatus(documents);
	}
	
	@GET
	@Path("getdocument")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getdocument(
			@QueryParam("documentid") String id) {
		System.out.println("GET Request: getdocument");
		document = documentDAO.readById(id);
		if(document == null) return notFoundStatus("404 Document Not Found");
		return okStatus(document);
	}
	
	@GET
	@Path("getalldocumentsbyuserid")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getdocumentbyuserid(
			@QueryParam("userid") String id) {
		System.out.println("GET Request: getalldocumentsbyuserid");
		documents = documentDAO.getAllDocumentsByUserId(id);
		if(documents == null) return notFoundStatus("404 Document Lists not Found");
		return okStatus(documents);
	}
	
	
	@POST
	@Path("newdraft")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createNewDraftdocument(
			@FormParam("documentName") String name, 
			@FormParam("description") String description,
			@FormParam("creator") String creatorId
			) {
		System.out.println("GET Request: newdraft");
		
		document = new Document(name, description, new Date(), new Draft(), creatorId, "56a0d083d4c607b2e7a60a5c",0,1,true);
		documentDAO.create(document);
		
		return okStatus(document);
	}
	
	@GET
	@Path("newdocument")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createNewdocument(
			@QueryParam("documentName") String name, 
			@QueryParam("description") String description,
			@QueryParam("creator") String creatorId) {
		System.out.println("GET Request: newdocument");
		document = new Document(name, description, new Date(), new WaitingForApproval(), creatorId, "56a0d083d4c607b2e7a60a5c", 1,0,false);
		documentDAO.create(document);
		return okStatus(document);
	}
	

	@POST
	@Path("save")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response savedocument(
			@FormParam("documentId") String id, 
			@FormParam("documentName") String name,
			@FormParam("description") String description
		) {
		System.out.println("GET Request: save");
		document = documentDAO.readById(id);
		if (document == null) return notFoundStatus("404 Document not Found");
		
		int minorVersion = document.getMinorVersion();
		document.setDocumentName(name);
		document.setDescription(description);
		document.setLastModifiedDate(new Date());
		document.setVersion(document.getMajorVersion(), minorVersion++);
		documentDAO.update(document);
		return okStatus(document);
	}
	
	@GET
	@Path("submit")
	@Produces(MediaType.APPLICATION_JSON)
	public Response submitdocumentToApprove(
			@QueryParam("documentid") String id) {
		System.out.println("GET Request: submit");
		document = documentDAO.readById(id);
		if (document == null) return notFoundStatus("404 Document not Found");
		
		int majorVersion = document.getMajorVersion();
		document.setDocumentStatus(new WaitingForApproval().getDocumentStatusName());
		document.setLastModifiedDate(new Date());
		document.setVersion(majorVersion++, document.getMinorVersion());
		document.setEditable(false);
		documentDAO.update(document);
		return okStatus(document);
	}
	
	@GET
	@Path("approve")
	@Produces(MediaType.APPLICATION_JSON)
	public Response approveThedocument(
			@QueryParam("documentid") String id) {
		System.out.println("GET Request: approve");
		document = documentDAO.readById(id);
		if (document == null) return notFoundStatus("404 Document not Found");
		document.setDocumentStatus(new Approved().getDocumentStatusName());
		document.setLastModifiedDate(new Date());
		document.setEditable(false);
		documentDAO.update(document);
		return okStatus(document);
	}
	
	@GET
	@Path("publish")
	@Produces(MediaType.APPLICATION_JSON)
	public Response publishThedocument(
			@QueryParam("documentid") String id) {
		System.out.println("GET Request: publish");
		document = documentDAO.readById(id);
		if (document == null) return notFoundStatus("404 Document not Found");
		document.setDocumentStatus(new Publish().getDocumentStatusName());
		document.setEditable(false);
		documentDAO.update(document);
		return okStatus(document);
	}
	
	@GET
	@Path("reject")
	@Produces(MediaType.APPLICATION_JSON)
	public Response rejectDocument(
			@QueryParam("documentid") String id) {
		System.out.println("GET Request: reject");
		document = documentDAO.readById(id);
		if (document == null) return notFoundStatus("404 Document not Found");
		document.setDocumentStatus(new Reject().getDocumentStatusName());
		document.setLastModifiedDate(new Date());
		document.setEditable(false);
		documentDAO.update(document);
		return okStatus(document);
	}
	
	@GET
	@Path("editable")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setEditable(
			@QueryParam("documentId") String id) {
		System.out.println("GET Request: reject");
		document = documentDAO.readById(id);
		if (document == null) return notFoundStatus("404 Document not Found");
		document.setEditable(true);
		documentDAO.update(document);
		return okStatus(document);
	}
	
	@GET
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDocument(
			@QueryParam("documentid") String id) {
		System.out.println("GET Request: delete");
		int temp = documentDAO.deleteById(id);
		System.out.println("deleted "+temp+" document(s)");
		return okStatus("Deleted!");
	}
	

}
