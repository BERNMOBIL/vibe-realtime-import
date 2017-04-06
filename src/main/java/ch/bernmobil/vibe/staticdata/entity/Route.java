package ch.bernmobil.vibe.staticdata.entity;

import ch.bernmobil.vibe.staticdata.entity.sync.RouteMapper;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Route {

  private static long idCounter = 0;

  @Id
  private Long id;
  private Integer type;

  public Route(int type, String gtfs_id) {
    this.id = ++idCounter;
    RouteMapper.addMapping(gtfs_id, id);

    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
