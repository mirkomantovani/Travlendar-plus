/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mirko
 */
@Entity
@Table(name = "WARNING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Warning.findAll", query = "SELECT w FROM Warning w")
    , @NamedQuery(name = "Warning.findByUid", query = "SELECT w FROM Warning w WHERE w.warningPK.uid = :uid")
    , @NamedQuery(name = "Warning.findByWarningid", query = "SELECT w FROM Warning w WHERE w.warningPK.warningid = :warningid")
    , @NamedQuery(name = "Warning.findByMeetings", query = "SELECT w FROM Warning w WHERE w.meetings = :meetings")
    , @NamedQuery(name = "Warning.findByBreaks", query = "SELECT w FROM Warning w WHERE w.breaks = :breaks")})
public class Warning implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WarningPK warningPK;
    @Size(max = 255)
    @Column(name = "MEETINGS")
    private String meetings;
    @Size(max = 255)
    @Column(name = "BREAKS")
    private String breaks;
    @JoinColumn(name = "UID", referencedColumnName = "UID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usertable usertable;

    public Warning() {
    }

    public Warning(WarningPK warningPK) {
        this.warningPK = warningPK;
    }

    public Warning(int uid, int warningid) {
        this.warningPK = new WarningPK(uid, warningid);
    }

    public WarningPK getWarningPK() {
        return warningPK;
    }

    public void setWarningPK(WarningPK warningPK) {
        this.warningPK = warningPK;
    }

    public String getMeetings() {
        return meetings;
    }

    public void setMeetings(String meetings) {
        this.meetings = meetings;
    }

    public String getBreaks() {
        return breaks;
    }

    public void setBreaks(String breaks) {
        this.breaks = breaks;
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
        hash += (warningPK != null ? warningPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Warning)) {
            return false;
        }
        Warning other = (Warning) object;
        if ((this.warningPK == null && other.warningPK != null) || (this.warningPK != null && !this.warningPK.equals(other.warningPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Warning[ warningPK=" + warningPK + " ]";
    }
    
}
