/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mirko
 */
@Entity
@Table(name = "REMINDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reminder.findAll", query = "SELECT r FROM Reminder r")
    , @NamedQuery(name = "Reminder.findByUid", query = "SELECT r FROM Reminder r WHERE r.reminderPK.uid = :uid")
    , @NamedQuery(name = "Reminder.findByMeetingid", query = "SELECT r FROM Reminder r WHERE r.reminderPK.meetingid = :meetingid")
    , @NamedQuery(name = "Reminder.findByTime", query = "SELECT r FROM Reminder r WHERE r.time = :time")})
public class Reminder implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReminderPK reminderPK;
    @Column(name = "TIME")
    @Temporal(TemporalType.TIME)
    private Date time;
    @JoinColumns({
        @JoinColumn(name = "UID", referencedColumnName = "UID", insertable = false, updatable = false)
        , @JoinColumn(name = "MEETINGID", referencedColumnName = "MEETINGID", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Meeting meeting;

    public Reminder() {
    }

    public Reminder(ReminderPK reminderPK) {
        this.reminderPK = reminderPK;
    }

    public Reminder(int uid, int meetingid) {
        this.reminderPK = new ReminderPK(uid, meetingid);
    }

    public ReminderPK getReminderPK() {
        return reminderPK;
    }

    public void setReminderPK(ReminderPK reminderPK) {
        this.reminderPK = reminderPK;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reminderPK != null ? reminderPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reminder)) {
            return false;
        }
        Reminder other = (Reminder) object;
        if ((this.reminderPK == null && other.reminderPK != null) || (this.reminderPK != null && !this.reminderPK.equals(other.reminderPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Reminder[ reminderPK=" + reminderPK + " ]";
    }
    
}
