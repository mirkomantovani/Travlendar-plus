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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mirko
 */
@Entity
@Table(name = "BREAK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Break.findAll", query = "SELECT b FROM Break b")
    , @NamedQuery(name = "Break.findByUid", query = "SELECT b FROM Break b WHERE b.uid = :uid")
    , @NamedQuery(name = "Break.findByName", query = "SELECT b FROM Break b WHERE b.name = :name")
    , @NamedQuery(name = "Break.findByStartingtime", query = "SELECT b FROM Break b WHERE b.startingtime = :startingtime")
    , @NamedQuery(name = "Break.findByEndingtime", query = "SELECT b FROM Break b WHERE b.endingtime = :endingtime")
    , @NamedQuery(name = "Break.findByMinduration", query = "SELECT b FROM Break b WHERE b.minduration = :minduration")
    , @NamedQuery(name = "Break.findByRecurrent", query = "SELECT b FROM Break b WHERE b.recurrent = :recurrent")
    , @NamedQuery(name = "Break.findByLunch", query = "SELECT b FROM Break b WHERE b.lunch = :lunch")
    , @NamedQuery(name = "Break.findByDayofweek", query = "SELECT b FROM Break b WHERE b.dayofweek = :dayofweek")})
public class Break implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UID")
    private Integer uid;
    @Size(max = 10)
    @Column(name = "NAME")
    private String name;
    @Column(name = "STARTINGTIME")
    @Temporal(TemporalType.TIME)
    private Date startingtime;
    @Column(name = "ENDINGTIME")
    @Temporal(TemporalType.TIME)
    private Date endingtime;
    @Column(name = "MINDURATION")
    @Temporal(TemporalType.TIME)
    private Date minduration;
    @Column(name = "RECURRENT")
    private Boolean recurrent;
    @Column(name = "LUNCH")
    private Boolean lunch;
    @Size(max = 20)
    @Column(name = "DAYOFWEEK")
    private String dayofweek;
    @JoinColumn(name = "UID", referencedColumnName = "UID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Preferences preferences;

    public Break() {
    }

    public Break(Integer uid) {
        this.uid = uid;
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

    public Date getStartingtime() {
        return startingtime;
    }

    public void setStartingtime(Date startingtime) {
        this.startingtime = startingtime;
    }

    public Date getEndingtime() {
        return endingtime;
    }

    public void setEndingtime(Date endingtime) {
        this.endingtime = endingtime;
    }

    public Date getMinduration() {
        return minduration;
    }

    public void setMinduration(Date minduration) {
        this.minduration = minduration;
    }

    public Boolean getRecurrent() {
        return recurrent;
    }

    public void setRecurrent(Boolean recurrent) {
        this.recurrent = recurrent;
    }

    public Boolean getLunch() {
        return lunch;
    }

    public void setLunch(Boolean lunch) {
        this.lunch = lunch;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
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
        if (!(object instanceof Break)) {
            return false;
        }
        Break other = (Break) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Break[ uid=" + uid + " ]";
    }
    
}
