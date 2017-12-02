/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mirko
 */
@Entity
@Table(name = "PREFERENCES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Preferences.findAll", query = "SELECT p FROM Preferences p")
    , @NamedQuery(name = "Preferences.findByUid", query = "SELECT p FROM Preferences p WHERE p.uid = :uid")
    , @NamedQuery(name = "Preferences.findByMinimizecarbonfootprint", query = "SELECT p FROM Preferences p WHERE p.minimizecarbonfootprint = :minimizecarbonfootprint")
    , @NamedQuery(name = "Preferences.findByMaxwalkingdistance", query = "SELECT p FROM Preferences p WHERE p.maxwalkingdistance = :maxwalkingdistance")
    , @NamedQuery(name = "Preferences.findByMaxcyclingdistance", query = "SELECT p FROM Preferences p WHERE p.maxcyclingdistance = :maxcyclingdistance")
    , @NamedQuery(name = "Preferences.findByNopublictransportationsafter", query = "SELECT p FROM Preferences p WHERE p.nopublictransportationsafter = :nopublictransportationsafter")
    , @NamedQuery(name = "Preferences.findByAvoidtolls", query = "SELECT p FROM Preferences p WHERE p.avoidtolls = :avoidtolls")
    , @NamedQuery(name = "Preferences.findByAvoidmotorways", query = "SELECT p FROM Preferences p WHERE p.avoidmotorways = :avoidmotorways")})
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UID")
    private Integer uid;
    @Column(name = "MINIMIZECARBONFOOTPRINT")
    private Boolean minimizecarbonfootprint;
    @Column(name = "MAXWALKINGDISTANCE")
    private Integer maxwalkingdistance;
    @Column(name = "MAXCYCLINGDISTANCE")
    private Integer maxcyclingdistance;
    @Column(name = "NOPUBLICTRANSPORTATIONSAFTER")
    @Temporal(TemporalType.TIME)
    private Date nopublictransportationsafter;
    @Column(name = "AVOIDTOLLS")
    private Boolean avoidtolls;
    @Column(name = "AVOIDMOTORWAYS")
    private Boolean avoidmotorways;
    @JoinColumn(name = "UID", referencedColumnName = "UID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usertable usertable;

    public Preferences() {
    }

    public Preferences(Integer uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Boolean getMinimizecarbonfootprint() {
        return minimizecarbonfootprint;
    }

    public void setMinimizecarbonfootprint(Boolean minimizecarbonfootprint) {
        this.minimizecarbonfootprint = minimizecarbonfootprint;
    }

    public Integer getMaxwalkingdistance() {
        return maxwalkingdistance;
    }

    public void setMaxwalkingdistance(Integer maxwalkingdistance) {
        this.maxwalkingdistance = maxwalkingdistance;
    }

    public Integer getMaxcyclingdistance() {
        return maxcyclingdistance;
    }

    public void setMaxcyclingdistance(Integer maxcyclingdistance) {
        this.maxcyclingdistance = maxcyclingdistance;
    }

    public Date getNopublictransportationsafter() {
        return nopublictransportationsafter;
    }

    public void setNopublictransportationsafter(Date nopublictransportationsafter) {
        this.nopublictransportationsafter = nopublictransportationsafter;
    }

    public Boolean getAvoidtolls() {
        return avoidtolls;
    }

    public void setAvoidtolls(Boolean avoidtolls) {
        this.avoidtolls = avoidtolls;
    }

    public Boolean getAvoidmotorways() {
        return avoidmotorways;
    }

    public void setAvoidmotorways(Boolean avoidmotorways) {
        this.avoidmotorways = avoidmotorways;
    }

    public Usertable getUsertable() {
        return usertable;
    }

    public void setUsertable(Usertable usertable) {
        this.usertable = usertable;
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
        if (!(object instanceof Preferences)) {
            return false;
        }
        Preferences other = (Preferences) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Preferences[ uid=" + uid + " ]";
    }
    
}
