/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mirko
 */
@Entity
@Table(name = "USERTABLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usertable.findAll", query = "SELECT u FROM Usertable u")
    , @NamedQuery(name = "Usertable.findByUid", query = "SELECT u FROM Usertable u WHERE u.uid = :uid")
    , @NamedQuery(name = "Usertable.findByName", query = "SELECT u FROM Usertable u WHERE u.name = :name")
    , @NamedQuery(name = "Usertable.findBySurname", query = "SELECT u FROM Usertable u WHERE u.surname = :surname")
    , @NamedQuery(name = "Usertable.findByEmail", query = "SELECT u FROM Usertable u WHERE u.email = :email")
    , @NamedQuery(name = "Usertable.findByRegistrationdate", query = "SELECT u FROM Usertable u WHERE u.registrationdate = :registrationdate")
    , @NamedQuery(name = "Usertable.findByHomeaddress", query = "SELECT u FROM Usertable u WHERE u.homeaddress = :homeaddress")
    , @NamedQuery(name = "Usertable.findByHashedpassword", query = "SELECT u FROM Usertable u WHERE u.hashedpassword = :hashedpassword")})
public class Usertable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UID")
    private Integer uid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;
    @Size(max = 100)
    @Column(name = "SURNAME")
    private String surname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "REGISTRATIONDATE")
    @Temporal(TemporalType.DATE)
    private Date registrationdate;
    @Size(max = 100)
    @Column(name = "HOMEADDRESS")
    private String homeaddress;
    @Size(max = 255)
    @Column(name = "HASHEDPASSWORD")
    private String hashedpassword;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usertable")
    private Travelmean travelmean;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usertable")
    private Preferences preferences;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usertable")
    private List<Break> breakList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usertable")
    private List<Meeting> meetingList;

    public Usertable() {
    }

    public Usertable(Integer uid) {
        this.uid = uid;
    }

    public Usertable(Integer uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(Date registrationdate) {
        this.registrationdate = registrationdate;
    }

    public String getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress;
    }

    public String getHashedpassword() {
        return hashedpassword;
    }

    public void setHashedpassword(String hashedpassword) {
        this.hashedpassword = hashedpassword;
    }

    public Travelmean getTravelmean() {
        return travelmean;
    }

    public void setTravelmean(Travelmean travelmean) {
        this.travelmean = travelmean;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    @XmlTransient
    public List<Break> getBreakList() {
        return breakList;
    }

    public void setBreakList(List<Break> breakList) {
        this.breakList = breakList;
    }

    @XmlTransient
    public List<Meeting> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(List<Meeting> meetingList) {
        this.meetingList = meetingList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usertable)) {
            return false;
        }
        Usertable other = (Usertable) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Usertable[ uid=" + uid + " ]";
    }
    
}
