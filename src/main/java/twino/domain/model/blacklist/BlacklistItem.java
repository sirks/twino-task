package twino.domain.model.blacklist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "blacklist")
public class BlacklistItem {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String personalId;

    public BlacklistItem() {
    }

    public BlacklistItem(String personalId) {
        this.personalId = personalId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
