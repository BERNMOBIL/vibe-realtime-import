package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Journey;
import ch.bernmobil.vibe.staticdata.entity.Schedule;
import ch.bernmobil.vibe.staticdata.entity.sync.JourneyMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.StopMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import java.sql.Time;
import java.text.SimpleDateFormat;
import org.springframework.batch.item.ItemProcessor;

public class ScheduleProcessor implements ItemProcessor<GtfsStopTime, Schedule> {
    @Override
    public Schedule process(GtfsStopTime item) throws Exception {
        String plattform = "Plattform";

        Time plannedArrival = new Time(new SimpleDateFormat("hh:mm:ss").parse(item.getArrivalTime()).getTime());
        Time plannedDeparture = new Time(new SimpleDateFormat("hh:mm:ss").parse(item.getDepartureTime()).getTime());
        Long stop = StopMapper.getMappingByStopId(item.getStopId()).id;
        Long journey = JourneyMapper.getMappingByTripId(item.getTripId()).id;

        return new Schedule(plattform, plannedArrival, plannedDeparture, stop, journey);
    }
}