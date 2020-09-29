package com.test.educationproject.ejb_db;

import com.test.educationproject.entities.Course;
import com.test.educationproject.entities.Institution;
import com.test.educationproject.entities.Student;
import com.test.educationproject.jpa.TransactionProvider;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author litem
 */
@Stateless
public class DbBean_Entities {

    @EJB
    private TransactionProvider provider;

    public List<Institution> getInstitutions() {
        List<Institution> list = new ArrayList();
        try {
            EntityManager em = provider.getEM();

            Query q = em.createQuery("SELECT i FROM Institution i");
            list = provider.getManyFromQuery(q);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public Institution getInstitution_ByInstitutionId(Integer institutionid) {
        Institution res = null;
        try {
            if (institutionid != null) {
                EntityManager em = provider.getEM();

                Query q = em.createQuery("SELECT i FROM Institution i WHERE i.institutionid = :institutionid");
                q.setParameter("institutionid", institutionid);
                res = provider.getSingleResult(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
    
    public List<Course> getCourses(){
        List<Course> list = new ArrayList();
        
        try{
            EntityManager em = provider.getEM();
            
            Query coursesq = em.createQuery("SELECT c FROM Course c");
            
            list = provider.getManyFromQuery(coursesq);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }
    
    public Course getCourse_ByCourseId(Integer courseId){
        Course course = null;
        
        try {
            if(courseId != null){
                EntityManager em = provider.getEM();
                
                Query courseq = em.createQuery("SELECT c FROM Course c WHERE c.courseid = :courseId");
                courseq.setParameter("courseId", courseId);
                
                course = provider.getSingleResult(courseq);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return course;
        }
    }
    
    public List<Course> getCourse_ByInstitutionId(Integer institutionId){
        List<Course> course_list = new ArrayList();
        
        try {
            EntityManager em = provider.getEM();
            
            Query coursesq = em.createQuery("SELECT c FROM Course c WHERE c.institution.institutionid = :institutionId");
            coursesq.setParameter("institutionId", institutionId);
            
           course_list = provider.getManyFromQuery(coursesq);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return course_list;
        }
    }
    
    
    public  List<Student> getAllStudents(){
        List<Student> student_list = new ArrayList();
        
        try {
            EntityManager em = provider.getEM();
            
            Query studentq = em.createQuery("SELECT s FROM Student s");
            
            student_list = provider.getManyFromQuery(studentq);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return student_list;
        }
    }
    
    
    public List<Student> getStudent_ByCourseId(Integer courseId){
        List<Student> student_list = new ArrayList();
        
        try {
            EntityManager em = provider.getEM();
            
            Query studentq = em.createQuery("SELECT s FROM Student s WHERE s.course.courseid = :courseId");
            studentq.setParameter("courseId", courseId);
            
           student_list = provider.getManyFromQuery(studentq);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return student_list;
        }
    }
    
    public Student getStudent_ByStudentId(Integer studentId){
        Student student = null;
        
        try {
            if(studentId != null){
                EntityManager em = provider.getEM();
                
                Query studentq = em.createQuery("SELECT s FROM Student s WHERE s.studentid = :studentId");
                studentq.setParameter("studentId", studentId);
                
                student = provider.getSingleResult(studentq);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return student;
        }
    }
    
    public Boolean checkIfNameExists(String tablename, String name, Integer personid, Integer tableid){
        Boolean res = false;
        
        try {
            EntityManager em = provider.getEM();
            
            if(tablename =="institution"){
                    Query institutionq;
                    if(personid != null){
                        institutionq = em.createQuery("SELECT i FROM Institution i WHERE i.institutionid != :personid AND i.name = :name");
                        institutionq.setParameter("personid", personid);
                    }else{
                        institutionq = em.createQuery("SELECT i FROM Institution i WHERE i.name = :name");
                    }
                    
                    institutionq.setParameter("name", name);
                    
                    Institution i = provider.getSingleResult(institutionq);
                    
                    if(i != null){
                        res = true;
                    }
            }
            else if(tablename =="course"){
                    Query courseq;
                    if(personid != null){
                        courseq = em.createQuery("SELECT c FROM Course c WHERE c.institution.institutionid = :tableid AND c.courseid != :personid AND c.name = :name");
                        courseq.setParameter("personid", personid);
                    }else{
                        courseq = em.createQuery("SELECT c FROM Course c WHERE c.institution.institutionid = :tableid AND c.name = :name");
                    }
                    courseq.setParameter("name", name);
                    courseq.setParameter("tableid", tableid);
                    
                    Course c = provider.getSingleResult(courseq);
                    
                    if(c != null){
                        res = true;
                    }
            }
            else if(tablename =="student"){
                    Query studentq;
                    if(personid != null){
                        studentq = em.createQuery("SELECT s FROM Student s WHERE s.course.courseid = :tableid AND s.studentid != :personid AND s.name = :name");
                        studentq.setParameter("personid", personid);
                    }else{
                        studentq = em.createQuery("SELECT s FROM Student s WHERE s.course.courseid = :tableid AND s.name = :name");
                    }  
                    studentq.setParameter("name", name);
                    studentq.setParameter("tableid", tableid);
                    
                    Student s = provider.getSingleResult(studentq);
                    
                    if(s != null){
                        res = true;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
