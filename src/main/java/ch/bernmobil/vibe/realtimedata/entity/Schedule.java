package ch.bernmobil.vibe.realtimedata.entity;

import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Schedule {

  private static long idCounter = 0;

  @Id
  private Long id;

  private String platform;
  private Time planned_arrival;
  private Time planned_departure;
  private Long stop;
  private Long journey;
  private Long scheduleUpdate;


  public static long getIdCounter() {
    return idCounter;
  }

  public Long getId() {
    return id;
  }

  public String getPlatform() {
    return platform;
  }

  public Time getPlanned_arrival() {
    return planned_arrival;
  }

  public Time getPlanned_departure() {
    return planned_departure;
  }

  public Long getStop() {
    return stop;
  }

  public Long getJourney() {
    return journey;
  }

  public void setScheduleUpdate(Long scheduleUpdate) {
    this.scheduleUpdate = scheduleUpdate;
  }
}
