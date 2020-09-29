/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.educationproject.api;

import com.test.educationproject.ejb.RecordsBean;
import com.test.educationproject.utils.JsonResponse;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author litem
 */
@Stateless
@Path("records")
@Produces({MediaType.APPLICATION_JSON})
public class Records {

    @EJB
    private RecordsBean rBean;

    @GET
    @Path("institutions")
    public Response getAllInstitutions() {
        return Response.ok(rBean.getAllInstitutions()).build();
    }

    @GET
    @Path("institution")
    public Response getInstitutionById(@QueryParam("id") Integer id) {
        return Response.ok(rBean.getInstitution_ById(id)).build();
    }

    @POST
    @Path("institution")
    public Response addInstitution(HashMap data_hm) {
        JsonResponse jr = rBean.addInstitution(data_hm);

        return Response.status(jr.getResponse_code()).entity(jr).build();
    }
    
    @PUT
    @Path("institution")
    public Response updateInstitution(HashMap data_hm, @QueryParam("institutionId") Integer institutionId){
        JsonResponse jr = rBean.updateInstitution(data_hm, institutionId);
        return Response.ok(jr.getResponse_code()).entity(jr).build();
    }
    
    @DELETE
    @Path("institution")
    public  Response deleteInstitution(@QueryParam("institutionId") Integer institutionId){
        JsonResponse jr = rBean.deleteInstitution(institutionId);
        
        return Response.ok(jr.getResponse_code()).entity(jr).build();
    }
    
    @GET
    @Path("institution/courses")
    public Response getCoursesByInstitutionId(@QueryParam("id") Integer id) {
        return Response.ok(rBean.getCourses_ByInstitutionId(id)).build();
    }
    
    @GET
    @Path("courses")
    public Response getAllCourses(){
        return Response.ok(rBean.getAllCourses()).build();
    }
    
    @GET
    @Path("course")
    public Response getCourseById(@QueryParam("courseId") Integer courseId){
        return Response.ok(rBean.getCourse_ByCourseId(courseId)).build();
    }
    
    @POST
    @Path("course")
    public Response addCourse(HashMap data_hm, @QueryParam("institutionId") Integer InstitutionId){
        JsonResponse  jr = rBean.addCourse(data_hm, InstitutionId);
        
        return Response.ok(jr.getResponse_code()).entity(jr).build();
    }
    
    @PUT
    @Path("course")
    public Response updateCourse(HashMap data_hm, @QueryParam("courseId") Integer courseId){
        JsonResponse jr = rBean.updateCourse(data_hm, courseId);
        
        return Response.ok(jr.getResponse_code()).entity(jr).build();
    }
    
    @DELETE
    @Path("course")
    public Response deleteCourse(@QueryParam("courseId") Integer courseId){
        JsonResponse jr= rBean.deleteCourse(courseId);
        
        return Response.ok(jr.getResponse_code()).entity(jr).build();
    }
    
    @GET
    @Path("students")
    public Response getAllStudents(){
        return Response.ok(rBean.getAllStudents()).build();
    }
    
    @GET
    @Path("student")
    public Response getStudentById(@QueryParam("studentId") Integer studentId){
        return  Response.ok(rBean.getStudent_ById(studentId)).build();
    }
    
    @POST
    @Path("student")
    public Response addStudent(HashMap data_hm){
        JsonResponse jr = rBean.addStudent(data_hm);
        
        return Response.ok(jr.getResponse_code()).entity(jr).build();
    }
    
    @PUT
    @Path("student")
    public Response updateStudent(HashMap data_hm, @QueryParam("studentId") Integer studentId){
        JsonResponse jr = rBean.updateStudent(data_hm, studentId);
        
        return Response.ok(jr.getResponse_code()).entity(jr).build();
    }
    
    @DELETE
    @Path("student")
    public Response deleteStudent(@QueryParam("studentId") Integer studentId){
        JsonResponse jr = rBean.deleteStudent(studentId);
        return Response.ok(jr.getResponse_code()).entity(jr).build();
    }
  
}
