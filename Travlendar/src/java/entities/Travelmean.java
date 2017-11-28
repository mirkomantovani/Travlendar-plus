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
import javax.validation.constraints.Size;
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
    , @NamedQuery(name = "Travelmean.findByName", query = "SELECT t FROM Travelmean t WHERE t.name = :name")
    , @NamedQuery(name = "Travelmean.findBySelected", query = "SELECT t FROM Travelmean t WHERE t.selected = :selected")})
public class Travelmean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UID")
    private Integer uid;
    @Size(max = 100)
    @Column(name = "NAME")
    private String name;
    @Column(name = "SELECTED")
    private Boolean selected;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
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
