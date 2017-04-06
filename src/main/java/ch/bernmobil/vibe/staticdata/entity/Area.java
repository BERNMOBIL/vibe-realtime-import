package ch.bernmobil.vibe.staticdata.entity;

import ch.bernmobil.vibe.staticdata.entity.sync.AreaMapper;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Area {
    private static long idCounter = 0;

    @Id
    private Long id;
    private String name;

    public Area(String name, String gtfsId) {
        this.id = ++idCounter;
        AreaMapper.addMapping(gtfsId, id);

        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
