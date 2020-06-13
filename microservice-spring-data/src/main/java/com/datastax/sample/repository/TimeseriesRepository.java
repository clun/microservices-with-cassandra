package com.datastax.sample.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.datastax.sample.entity.TimeserieDaily;
import com.datastax.sample.entity.TimeserieDailyKey;

/**
 * Find by Symbol.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public interface TimeseriesRepository extends CassandraRepository<TimeserieDaily, TimeserieDailyKey> { 
    
    List<TimeserieDaily> findByTimeserieDailyKeySourceAndTimeserieDailyKeyYyyymmdd(String symbol, String valueDate);
    
    /**
     * Get the serie for today, no paging.
     */
    default List<TimeserieDaily> findTimeSeriesToday(String source) {
        return findByTimeserieDailyKeySourceAndTimeserieDailyKeyYyyymmdd(source, 
                TimeserieDailyKey.YYYYMMDD.format(LocalDateTime.now()));
    }
}
