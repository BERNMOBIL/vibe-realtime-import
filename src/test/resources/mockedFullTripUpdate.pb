header {
  gtfs_realtime_version: "1.0"
  incrementality: FULL_DATASET
  timestamp: 1284457468
}


entity {
  id: "first_entity"
  trip_update {
    trip {
      trip_id: "101_000827_10_95932_76646_07:26_07:45"
    }
    stop_time_update {
      stop_id: "77969_0"
      arrival {
        delay: 5
        time: 1496740530
      }
    }
    stop_time_update {
      stop_sequence: 8
      stop_id: "stop_id_2"
      arrival {
        delay: 1
      }
    }
  }
}
entity {
  id: "second_entity"
  trip_update {
    trip {
      trip_id: "180_000827_10_95932_90052_18:16_18:53"
      start_time: "11:15:35"
    }
    stop_time_update {
      stop_sequence: 1
      stop_id: "76313_0"
      arrival {
        time: 1496745301
        delay: -2
      }
    }
  }
}
entity {
  id: "third_entity"
  trip_update {
    trip {
      trip_id: "230_000827_6_7110_7062_05:20_05:30"
      start_time: "11:15:35"
    }
    stop_time_update {
      stop_sequence: 1
      stop_id: "88423_0"
      arrival {
        time: 1496747943
        delay: -2
      }
    }
  }
}