/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mirko
 */
@Entity
@Table(name = "TRAVELMEAN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Travelmean.findAll", query = "SELECT t FROM Travelmean t")
    , @NamedQuery(name = "Travelmean.findByUid", query = "SELECT t FROM Travelmean t WHERE t.uid = :uid")
    , @NamedQuery(name = "Travelmean.findByOwnedcar", query = "SELECT t FROM Travelmean t WHERE t.ownedcar = :ownedcar")
    , @NamedQuery(name = "Travelmean.findBySharedcar", query = "SELECT t FROM Travelmean t WHERE t.sharedcar = :sharedcar")
    , @NamedQuery(name = "Travelmean.findByOwnedbike", query = "SELECT t FROM Travelmean t WHERE t.ownedbike = :ownedbike")
    , @NamedQuery(name = "Travelmean.findBySharedbike", query = "SELECT t FROM Travelmean t WHERE t.sharedbike = :sharedbike")
    , @NamedQuery(name = "Travelmean.findByWalking", query = "SELECT t FROM Travelmean t WHERE t.walking = :walking")
    , @NamedQuery(name = "Travelmean.findByPublictrasport", query = "SELECT t FROM Travelmean t WHERE t.publictrasport = :publictrasport")})
public class Travelmean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UID")
    private Integer uid;
    @Column(name = "OWNEDCAR")
    private Boolean ownedcar;
    @Column(name = "SHAREDCAR")
    private Boolean sharedcar;
    @Column(name = "OWNEDBIKE")
    private Boolean ownedbike;
    @Column(name = "SHAREDBIKE")
    private Boolean sharedbike;
    @Column(name = "WALKING")
    private Boolean walking;
    @Column(name = "PUBLICTRASPORT")
    private Boolean publictrasport;
    @JoinColumn(name = "UID", referencedColumnName = "UID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Preferences preferences;

    public Travelmean() {
    }

    public Travelmean(Integer uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Boolean getOwnedcar() {
        return ownedcar;
    }

    public void setOwnedcar(Boolean ownedcar) {
        this.ownedcar = ownedcar;
    }

    public Boolean getSharedcar() {
        return sharedcar;
    }

    public void setSharedcar(Boolean sharedcar) {
        this.sharedcar = sharedcar;
    }

    public Boolean getOwnedbike() {
        return ownedbike;
    }

    public void setOwnedbike(Boolean ownedbike) {
        this.ownedbike = ownedbike;
    }

    public Boolean getSharedbike() {
        return sharedbike;
    }

    public void setSharedbike(Boolean sharedbike) {
        this.sharedbike = sharedbike;
    }

    public Boolean getWalking() {
        return walking;
    }

    public void setWalking(Boolean walking) {
        this.walking = walking;
    }

    public Boolean getPublictrasport() {
        return publictrasport;
    }

    public void setPublictrasport(Boolean publictrasport) {
        this.publictrasport = publictrasport;
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
        if (!(object instanceof Travelmean)) {
            return false;
        }
        Travelmean other = (Travelmean) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Travelmean[ uid=" + uid + " ]";
    }
    
}
