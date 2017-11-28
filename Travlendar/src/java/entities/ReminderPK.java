/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Mirko
 */
@Embeddable
public class ReminderPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "UID")
    private int uid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEETINGID")
    private int meetingid;

    public ReminderPK() {
    }

    public ReminderPK(int uid, int meetingid) {
        this.uid = uid;
        this.meetingid = meetingid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMeetingid() {
        return meetingid;
    }

    public void setMeetingid(int meetingid) {
        this.meetingid = meetingid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) uid;
        hash += (int) meetingid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReminderPK)) {
            return false;
        }
        ReminderPK other = (ReminderPK) object;
        if (this.uid != other.uid) {
            return false;
        }
        if (this.meetingid != other.meetingid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ReminderPK[ uid=" + uid + ", meetingid=" + meetingid + " ]";
    }
    
}
