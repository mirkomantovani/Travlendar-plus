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
public class BreakPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "UID")
    private int uid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BREAKID")
    private int breakid;

    public BreakPK() {
    }

    public BreakPK(int uid, int breakid) {
        this.uid = uid;
        this.breakid = breakid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getBreakid() {
        return breakid;
    }

    public void setBreakid(int breakid) {
        this.breakid = breakid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) uid;
        hash += (int) breakid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BreakPK)) {
            return false;
        }
        BreakPK other = (BreakPK) object;
        if (this.uid != other.uid) {
            return false;
        }
        if (this.breakid != other.breakid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.BreakPK[ uid=" + uid + ", breakid=" + breakid + " ]";
    }
    
}
