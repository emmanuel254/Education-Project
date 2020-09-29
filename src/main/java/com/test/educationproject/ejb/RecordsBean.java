
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.educationproject.ejb;

import com.google.common.base.Strings;
import com.test.educationproject.ejb_db.DbBean_Entities;
import com.test.educationproject.entities.Course;
import com.test.educationproject.entities.Institution;
import com.test.educationproject.entities.Student;
import com.test.educationproject.jpa.TransactionProvider;
import com.test.educationproject.utils.JsonResponse;
import com.test.educationproject.utils.Utils;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author litem
 */
@Stateless
public class RecordsBean {

    @EJB
    private DbBean_Entities entitiesBean;

    @EJB
    private TransactionProvider provider;
    

    public List getAllInstitutions() {
        List res = new ArrayList();

        try {
            List<Institution> alliInstitutions = entitiesBean.getInstitutions();

            for (Institution i : alliInstitutions) {
                HashMap hm = new HashMap();
                
                List courses_hm_list = new ArrayList();
                List<Course> courses = entitiesBean.getCourse_ByInstitutionId(i.getInstitutionid());
                for(Course c : courses){
                    HashMap course_hm = new HashMap();
                    
                    List students_hm_list = new ArrayList();
                    List<Student> students= entitiesBean.getStudent_ByCourseId(c.getCourseid());
                    for(Student s: students){
                        HashMap student_hm = new HashMap();
                        
                        student_hm.put("studentid", s.getStudentid());
                        student_hm.put("name", s.getName());
                        student_hm.put("last_updated", s.getUpdatedOn());
                        
                        students_hm_list.add(student_hm);
                    }
                    
                    course_hm.put("courseid", c.getCourseid());
                    course_hm.put("name", c.getName());
                    course_hm.put("students", students_hm_list);
                    
                    courses_hm_list.add(course_hm);
                }
                hm.put("institutionid", i.getInstitutionid());
                hm.put("name", i.getName());
                hm.put("courses", courses_hm_list);

                res.add(hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public HashMap getInstitution_ById(Integer id) {
        HashMap res = new HashMap();

        try {
            Institution i = entitiesBean.getInstitution_ByInstitutionId(id);

            if (i != null) {
                res.put("institutionid", i.getInstitutionid());
                res.put("name", i.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public JsonResponse addInstitution(HashMap data_hm) {
        JsonResponse jr = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("An error occurred");

        try {
            if (data_hm != null) {
                String name = Utils.getStringValue_FromHashMap(data_hm, "name");

                if (Strings.isNullOrEmpty(name)) {
                    jr.setResponse_msg("Please specify the name of the institution");
                } else {
                    
                    //Check if name already exists in the db
                    if(entitiesBean.checkIfNameExists("institution", name, null, null)){
                        
                        jr.setResponse_code(403);
                        jr.setResponse_msg(name+ " is already taken, kindly enter another name");
                        
                    } else {
                        
                        Institution i = new Institution();
                        i.setName(name);

                        if (provider.createEntity(i)) {
                            jr.setResponse_code(200);
                            jr.setResponse_msg((name + " has been added as a new institution"));
                        }
                    }
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
    
    public JsonResponse updateInstitution(HashMap data_hm, Integer id){
        JsonResponse jr = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("Failed to update institution");
        
        try{
            if(data_hm != null){
                String name = Utils.getStringValue_FromHashMap(data_hm, "name");
                
                if(Strings.isNullOrEmpty(name)){
                    jr.setResponse_code(401);
                    jr.setResponse_msg("Please enter the name of institution");
                } else {
                    
                     //Check if name already exists in the db
                    if(entitiesBean.checkIfNameExists("institution", name, id, null)){
                        
                        jr.setResponse_code(403);
                        jr.setResponse_msg(name+ " is already taken, kindly enter another name");
                        
                    } else {
                        Institution i = entitiesBean.getInstitution_ByInstitutionId(id);
                    
                        i.setName(name);

                        if(provider.updateEntity(i)){
                            jr.setResponse_code(200);
                            jr.setResponse_msg(name+ " has been updated successfully");
                        }
                    }
                  
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
    
    public JsonResponse deleteInstitution(Integer id){
        JsonResponse jr = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("An error occured while deleting the record");
        
        try{
            Institution i = entitiesBean.getInstitution_ByInstitutionId(id);
            
            if(i == null){
                jr.setResponse_code(404);
                jr.setResponse_msg("The specified institution does not exist");
            }else {
                String name = i.getName();
                Integer institutionId = i.getInstitutionid();
                
                List<Course> course_list = entitiesBean.getCourse_ByInstitutionId(institutionId);
                
                if(!course_list.isEmpty()){
                    jr.setResponse_code(403);
                    jr.setResponse_msg("Cannot delete an institution containing courses and students");
                }else{
                    if(provider.deleteEntity(i)){
                        jr.setResponse_code(200);
                        jr.setResponse_msg(name+ "'s record has been deleted");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
    
    public  List getCourses_ByInstitutionId(Integer institutionId){
        List courses_list = new ArrayList();
        
        try {
            List<Course> courses = entitiesBean.getCourse_ByInstitutionId(institutionId);
            
            for(Course c : courses){
                HashMap hm = new HashMap();
                
                List students_hm_list = new ArrayList();
                List<Student> students = entitiesBean.getStudent_ByCourseId(c.getCourseid());
                for(Student s : students){
                    HashMap student_hm = new HashMap();
                    
                    student_hm.put("studentid", s.getStudentid());
                    student_hm.put("name", s.getName());
                    
                    students_hm_list.add(student_hm);
                }
                
                hm.put("courseid", c.getCourseid());
                hm.put("name", c.getName());
                hm.put("students", students_hm_list);
                
                courses_list.add(hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return courses_list;
        }
    }    
    public List getAllCourses(){
        List courses_list = new ArrayList();
        
        try{
            List<Course> allcourses = entitiesBean.getCourses();
            
            for (Course c : allcourses){
                HashMap hm = new HashMap();
                
                List students_hm_list = new ArrayList();
                List<Student> students = entitiesBean.getStudent_ByCourseId(c.getCourseid());
                for(Student s : students){
                    HashMap student_hm = new HashMap();
                    
                    student_hm.put("studentid", s.getStudentid());
                    student_hm.put("name", s.getName());
                    
                    students_hm_list.add(student_hm);
                }
                HashMap institution_hm = new HashMap();
                institution_hm.put("institutionid", c.getInstitution().getInstitutionid());
                institution_hm.put("name", c.getInstitution().getName());
                
                hm.put("courseid", c.getCourseid());
                hm.put("name", c.getName());
                hm.put("institution", institution_hm);
                hm.put("students", students_hm_list);
                
                courses_list.add(hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return courses_list;
        }
    }
    
    public JsonResponse addCourse(HashMap data_hm, Integer institutionId){
        JsonResponse jr = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("An error occured while adding the course");
        
        try{
            if(data_hm != null){
                String name = Utils.getStringValue_FromHashMap(data_hm, "name");
                
                if(Strings.isNullOrEmpty(name)){
                    jr.setResponse_code(401);
                    jr.setResponse_msg("course name is required");
                }else {
                    
                    Institution i = entitiesBean.getInstitution_ByInstitutionId(institutionId);
                    
                    if(i == null){
                        jr.setResponse_code(404);
                        jr.setResponse_msg("The specified institution does not exist");
                    } else {
                        
                        //Check if name already exists in the db
                        if(entitiesBean.checkIfNameExists("course", name, null, institutionId)){

                            jr.setResponse_code(403);
                            jr.setResponse_msg(name+ " is already taken, kindly enter another name");

                        }
                        else {
                            Course c = new Course();
                            c.setInstitution(i);
                            c.setName(name);

                            if(provider.createEntity(c)){
                                jr.setResponse_code(201);
                                jr.setResponse_msg(name+ " has been added to database");
                            }
                        }
                        
                    }
                    
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
    
    public HashMap getCourse_ByCourseId(Integer courseId){
        HashMap res = new HashMap();
        
        try{
            Course c = entitiesBean.getCourse_ByCourseId(courseId);
            
            if(c != null) {
                res.put("courseid", c.getCourseid());
                res.put("name", c.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
    
    public JsonResponse updateCourse(HashMap data_hm, Integer courseId){
        JsonResponse jr = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("An error occured while updating your request");
        
        try{
            if(data_hm != null) {
                String name = Utils.getStringValue_FromHashMap(data_hm, "name");
                Integer institutionId = Utils.getInteger_FromHashMap(data_hm, "institutionId");
                
                Institution i = entitiesBean.getInstitution_ByInstitutionId(institutionId);
                
                if(Strings.isNullOrEmpty(name)){
                    jr.setResponse_code(401);
                    jr.setResponse_msg("Please enter the name of the course");
                } else if( i == null){
                    jr.setResponse_code(401);
                    jr.setResponse_msg("The specified institution does not exist");
                } else {
                    Course c = entitiesBean.getCourse_ByCourseId(courseId);
                    
                    if(c == null){
                        jr.setResponse_code(404);
                        jr.setResponse_msg("The course you want to update does not exist");
                    }else {
                        
                        //Check if name already exists in the db
                        if(entitiesBean.checkIfNameExists("course", name, courseId, institutionId)){

                            jr.setResponse_code(403);
                            jr.setResponse_msg(name+ " is already taken, kindly enter another name");

                        } else {
                            c.setName(name);
                            c.setInstitution(i);

                            if(provider.updateEntity(c)){
                                jr.setResponse_code(200);
                                jr.setResponse_msg(name+ " was updated successfully");
                            }
                        }
                        
                    }
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
    
    public JsonResponse deleteCourse(Integer courseId){
        JsonResponse jr  = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("An error occured while deleting the record");
        
        try {
            Course c = entitiesBean.getCourse_ByCourseId(courseId);
            
            if(c == null) {
                jr.setResponse_code(404);
                jr.setResponse_msg("The specified course does not exist");
            } else {
                String name = c.getName();
                
                List<Student> student = entitiesBean.getStudent_ByCourseId(courseId);
                
                if(!student.isEmpty()){
                    jr.setResponse_code(403);
                    jr.setResponse_msg("You cannot delete a course containing students");
                } else {
                    if(provider.deleteEntity(c)){
                        jr.setResponse_code(200);
                        jr.setResponse_msg(name+ " was deleted successfully");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
    
    
    public List getAllStudents() {
        List res = new ArrayList();

        try {
            List<Student> allstudents = entitiesBean.getAllStudents();

            for (Student s : allstudents) {
                HashMap hm = new HashMap();
                
                HashMap course_hm = new HashMap();
                course_hm.put("name", s.getCourse().getName());
                course_hm.put("courseid", s.getCourse().getCourseid());
                
                HashMap institution_hm = new HashMap();
                institution_hm.put("name", s.getCourse().getInstitution().getName());
                institution_hm.put("institutionid", s.getCourse().getInstitution().getInstitutionid());
                
                hm.put("studentid", s.getStudentid());
                hm.put("name", s.getName());
                hm.put("updated_on", s.getUpdatedOn());
                hm.put("course", course_hm);
                hm.put("institution", institution_hm);

                res.add(hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
    
    public HashMap getStudent_ById(Integer studentId){
        HashMap res = new HashMap();
        
        try {
            Student s = entitiesBean.getStudent_ByStudentId(studentId);
            
            if(s != null){
                HashMap course_hm = new HashMap();
                course_hm.put("name", s.getCourse().getName());
                course_hm.put("courseid", s.getCourse().getCourseid());
                
                HashMap institution_hm = new HashMap();
                institution_hm.put("name", s.getCourse().getInstitution().getName());
                institution_hm.put("institutionid", s.getCourse().getInstitution().getInstitutionid());
                
                res.put("studentId", s.getStudentid());
                res.put("name", s.getName());
                res.put("updated_on", s.getUpdatedOn());
                res.put("course", course_hm);
                res.put("institution", institution_hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
        
    }
    
    
    public JsonResponse addStudent(HashMap data_hm){
        JsonResponse jr = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("An error occured while adding the student");
        
        try{
            if(data_hm != null){
                String name = Utils.getStringValue_FromHashMap(data_hm, "name");
                Integer courseId = Utils.getInteger_FromHashMap(data_hm, "courseId");
                
                if(Strings.isNullOrEmpty(name)){
                    jr.setResponse_code(401);
                    jr.setResponse_msg("student name is required");
                }else {
                    
                    Course c = entitiesBean.getCourse_ByCourseId(courseId);
                    
                    if(c == null){
                        jr.setResponse_code(404);
                        jr.setResponse_msg("The specified course does not exist");
                    } else {
                        
                        //Check if name already exists in the db
                        if(entitiesBean.checkIfNameExists("student", name, null, courseId)){

                            jr.setResponse_code(403);
                            jr.setResponse_msg(name+ " is already taken, kindly enter another name");

                        } else {
                            
                            Date date = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String datetimestr = format.format(date);
                            Date currentdatetime = format.parse(datetimestr);

                            Student s = new Student();
                            s.setCourse(c); 
                            s.setName(name);
                            s.setUpdatedOn(currentdatetime);

                            if(provider.createEntity(s)){
                                jr.setResponse_code(201);
                                jr.setResponse_msg(name+ " has been registered successfully");
                            }
                            
                        }
                        
                    }
                    
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
    
    public JsonResponse updateStudent(HashMap data_hm, Integer studentId){
        JsonResponse jr = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("An error occured while updating student");
        
        try{
            if(data_hm != null){
                String name = Utils.getStringValue_FromHashMap(data_hm, "name");
                Integer courseId = Utils.getInteger_FromHashMap(data_hm, "courseId");
                
                if(Strings.isNullOrEmpty(name)){
                    jr.setResponse_code(401);
                    jr.setResponse_msg("student name is required");
                }else {
                    
                    Course c = entitiesBean.getCourse_ByCourseId(courseId);
                    
                    if(c == null){
                        jr.setResponse_code(404);
                        jr.setResponse_msg("The specified course does not exist");
                    } else {
                        
                        //Check if name already exists in the db
                        if(entitiesBean.checkIfNameExists("student", name, studentId, courseId)){

                            jr.setResponse_code(403);
                            jr.setResponse_msg(name+ " is already taken, kindly enter another name");

                        } else {
                            Date date = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String datetimestr = format.format(date);
                            Date currentdatetime = format.parse(datetimestr);

                            Student s = entitiesBean.getStudent_ByStudentId(studentId);
                            
                            s.setCourse(c); 
                            s.setName(name);
                            s.setUpdatedOn(currentdatetime);

                            if(provider.createEntity(s)){
                                jr.setResponse_code(201);
                                jr.setResponse_msg(name+ "'s details have been updated successfully");
                            }
                        }
                        
                    }
                    
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
    
    public JsonResponse deleteStudent(Integer studentId){
        JsonResponse jr  = new JsonResponse();
        jr.setResponse_code(500);
        jr.setResponse_msg("An error occured while deleting the record");
        
        try {
            Student s = entitiesBean.getStudent_ByStudentId(studentId);
            
            if(s == null) {
                jr.setResponse_code(404);
                jr.setResponse_msg("The specified student does not exist");
            } else {
                String name = s.getName();
                
                if(provider.deleteEntity(s)){
                    jr.setResponse_code(200);
                    jr.setResponse_msg(name+ "'s record was deleted successfully");
                }
   
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jr;
        }
    }
}
