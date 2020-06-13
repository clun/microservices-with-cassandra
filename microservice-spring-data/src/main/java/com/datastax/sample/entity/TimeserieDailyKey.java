package com.datastax.sample.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

/**
 * This is a timeSeries table
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@PrimaryKeyClass
public class TimeserieDailyKey implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = 1142109498800363080L;
    
    /** Key Formatter. */
    public static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    /** Tick Data Partition Key. */
    @PrimaryKeyColumn(name = "source", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = Name.TEXT)
    private String source;

    /** Tick Data Clustering Column. */
    @PrimaryKeyColumn(name = "yyyymmdd", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = Name.TEXT)
    private String yyyymmdd;
    
    /** Tick Data Clustering Column (time order Desc). */
    @PrimaryKeyColumn(name = "tick", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @CassandraType(type = Name.TIMESTAMP)
    private Instant tick;

    /**
     * Constructor.
     *
     * @param source
     * @param yyyymmdd
     * @param tick
     */
    public TimeserieDailyKey(String source, String yyyymmdd, Instant tick) {
        super();
        this.source     = source;
        this.yyyymmdd   = yyyymmdd;
        this.tick       = tick;
    }
    
    /**
     * Getter accessor for attribute 'source'.
     *
     * @return
     *       current value of 'source'
     */
    public String getSource() {
        return source;
    }

    /**
     * Setter accessor for attribute 'source'.
     * @param source
     * 		new value for 'source '
     */
    public void setSource(String source) {
        this.source = source;
    }
    

    /**
     * Getter accessor for attribute 'yyymmdd'.
     *
     * @return
     *       current value of 'yyymmdd'
     */
    public String getYyyymmdd() {
        return yyyymmdd;
    }

    /**
     * Setter accessor for attribute 'yyymmdd'.
     * @param yyymmdd
     * 		new value for 'yyymmdd '
     */
    public void setYyyymmdd(String yyymmdd) {
        this.yyyymmdd = yyymmdd;
    }

    /**
     * Getter accessor for attribute 'serialversionuid'.
     *
     * @return
     *       current value of 'serialversionuid'
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * Getter accessor for attribute 'tick'.
     *
     * @return
     *       current value of 'tick'
     */
    public Instant getTick() {
        return tick;
    }

    /**
     * Setter accessor for attribute 'tick'.
     * @param tick
     * 		new value for 'tick '
     */
    public void setTick(Instant tick) {
        this.tick = tick;
    }
    
}
