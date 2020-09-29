/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.educationproject.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author litem
 */
@Entity
@Table(name = "institution", catalog = "education", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Institution.findAll", query = "SELECT i FROM Institution i")
    , @NamedQuery(name = "Institution.findByInstitutionid", query = "SELECT i FROM Institution i WHERE i.institutionid = :institutionid")
    , @NamedQuery(name = "Institution.findByName", query = "SELECT i FROM Institution i WHERE i.name = :name")})
public class Institution implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "institutionid", nullable = false)
    private Integer institutionid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "name", nullable = false, length = 250)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institution")
    private List<Course> courseList;

    public Institution() {
    }

    public Institution(Integer institutionid) {
        this.institutionid = institutionid;
    }

    public Institution(Integer institutionid, String name) {
        this.institutionid = institutionid;
        this.name = name;
    }

    public Integer getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(Integer institutionid) {
        this.institutionid = institutionid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (institutionid != null ? institutionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Institution)) {
            return false;
        }
        Institution other = (Institution) object;
        if ((this.institutionid == null && other.institutionid != null) || (this.institutionid != null && !this.institutionid.equals(other.institutionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.test.educationproject.entities.Institution[ institutionid=" + institutionid + " ]";
    }
    
}
