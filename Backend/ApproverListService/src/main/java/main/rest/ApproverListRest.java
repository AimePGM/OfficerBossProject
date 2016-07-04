package main.rest;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.client.RestTemplate;

import main.model.ApproverList;
import mongodb.dao.ApproverListDAO;

@Named
@Path("/")
public class ApproverListRest {
	
	private ApproverList approverList;
	private List<ApproverList> approverLists;
	
	private ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
	private ApproverListDAO approverListDAO = ctx.getBean("approverListDAO", ApproverListDAO.class);
	
	@Inject
	private RestTemplate restTemplate;
	
	@GET
	@Path("approverLists")
	@Produces(MediaType.APPLICATION_JSON)
	public Response approverLists(){
		approverLists = approverListDAO.getApproverLists();
		return Response.status(200).entity(approverList).build();
	}
	
	@GET
	@Path("approverList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response approverListById(@QueryParam("approverListId") String approverListId){

		approverList = approverListDAO.readById(approverListId);
		if(approverList == null) return Response.status(404).entity("404 publish document not found").build();
		return Response.status(200).entity(approverList).build();
	}
	
	@GET
	@Path("approverListByDocumentId")
	@Produces(MediaType.APPLICATION_JSON)
	public Response approverListByDocumentId(@QueryParam("approverListId") String documentId){

		approverList = approverListDAO.readByDocumentId(documentId);
		if(approverList == null) return Response.status(404).entity("404 publish document not found").build();
		return Response.status(200).entity(approverList).build();
	}
	
	@GET
	@Path("addApproverList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addApproverList(@QueryParam("documentId") String documentId, @QueryParam("approverIdList") List<String> approverIdList){
		approverList = new ApproverList(documentId, approverIdList);
		approverListDAO.create(approverList);
		return Response.status(200).entity(approverList).build();
	}
	
	@GET
	@Path("updateApproverList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateApproverList(@QueryParam("documentId") String documentId, @QueryParam("approverIdList") List<String> approverIdList){
		approverList = new ApproverList(documentId, approverIdList);
		approverListDAO.deleteByDocumentId(documentId);
		approverListDAO.create(approverList);
		return Response.status(200).entity(approverList).build();
	}
	

}
