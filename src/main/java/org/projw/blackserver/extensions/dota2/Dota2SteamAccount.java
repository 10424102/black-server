package org.projw.blackserver.extensions.dota2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_dota2_steam_account")
public class Dota2SteamAccount {
    @Id
    @GeneratedValue
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
