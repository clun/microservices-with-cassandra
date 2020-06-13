package com.datastax.sample.entity;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * Data into  Cassandra.
 *
 * @author DataStax Evangelist Team
 */
@Table(value = "timeseries_daily")
public class TimeserieDaily implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 6761984069893402714L;
    
    @PrimaryKey
    private TimeserieDailyKey timeserieDailyKey; 
    
    /** value. */
    @Column
    private double value;
     
    /** Keep default. */
    public TimeserieDaily() {}

    /**
     * Common constructor
     */
    public TimeserieDaily(String source, double value) {
        Instant now = Instant.now();
        this.timeserieDailyKey = new TimeserieDailyKey(source, TimeserieDailyKey.YYYYMMDD.format(now), now);
        this.value             = value;
    }
    
    /**
     * Common constructor
     */
    public TimeserieDaily(TimeserieDailyKey tickDataKey, double value) {
        this.timeserieDailyKey = tickDataKey;
        this.value    = value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "TickData [" +
                "symbol=" + timeserieDailyKey + ", " +
                "value=" + value + "]";
    }

    /**
     * Getter accessor for attribute 'value'.
     *
     * @return
     *       current value of 'value'
     */
    public double getValue() {
        return value;
    }

    /**
     * Setter accessor for attribute 'value'.
     * @param value
     * 		new value for 'value '
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Getter accessor for attribute 'timeserieDailyKey'.
     *
     * @return
     *       current value of 'timeserieDailyKey'
     */
    public TimeserieDailyKey getTimeserieDailyKey() {
        return timeserieDailyKey;
    }

    /**
     * Setter accessor for attribute 'timeserieDailyKey'.
     * @param timeserieDailyKey
     * 		new value for 'timeserieDailyKey '
     */
    public void setTimeserieDailyKey(TimeserieDailyKey timeserieDailyKey) {
        this.timeserieDailyKey = timeserieDailyKey;
    }

}
