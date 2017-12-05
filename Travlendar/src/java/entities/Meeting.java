/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mirko
 */
@Entity
@Table(name = "MEETING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meeting.findAll", query = "SELECT m FROM Meeting m")
    , @NamedQuery(name = "Meeting.findByUid", query = "SELECT m FROM Meeting m WHERE m.meetingPK.uid = :uid")
    , @NamedQuery(name = "Meeting.findByMeetingid", query = "SELECT m FROM Meeting m WHERE m.meetingPK.meetingid = :meetingid")
    , @NamedQuery(name = "Meeting.findByName", query = "SELECT m FROM Meeting m WHERE m.name = :name")
    , @NamedQuery(name = "Meeting.findByStartingdate", query = "SELECT m FROM Meeting m WHERE m.startingdate = :startingdate")
    , @NamedQuery(name = "Meeting.findByDuration", query = "SELECT m FROM Meeting m WHERE m.duration = :duration")
    , @NamedQuery(name = "Meeting.findByLocation", query = "SELECT m FROM Meeting m WHERE m.location = :location")})
public class Meeting implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MeetingPK meetingPK;
    @Size(max = 100)
    @Column(name = "NAME")
    private String name;
    @Column(name = "STARTINGDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startingdate;
    @Column(name = "DURATION")
    private Integer duration;
    @Size(max = 100)
    @Column(name = "LOCATION")
    private String location;
    @JoinColumn(name = "UID", referencedColumnName = "UID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usertable usertable;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "meeting")
    private Reminder reminder;
    
    

    public Meeting() {
    }

    public Meeting(MeetingPK meetingPK) {
        this.meetingPK = meetingPK;
    }

    public Meeting(int uid, int meetingid) {
        this.meetingPK = new MeetingPK(uid, meetingid);
    }

    public MeetingPK getMeetingPK() {
        return meetingPK;
    }

    public void setMeetingPK(MeetingPK meetingPK) {
        this.meetingPK = meetingPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartingdate() {
        return startingdate;
    }

    public void setStartingdate(Date startingdate) {
        this.startingdate = startingdate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Usertable getUsertable() {
        return usertable;
    }

    public void setUsertable(Usertable usertable) {
        this.usertable = usertable;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (meetingPK != null ? meetingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meeting)) {
            return false;
        }
        Meeting other = (Meeting) object;
        if ((this.meetingPK == null && other.meetingPK != null) || (this.meetingPK != null && !this.meetingPK.equals(other.meetingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Meeting[ meetingPK=" + meetingPK + " ]";
    }
    
}
