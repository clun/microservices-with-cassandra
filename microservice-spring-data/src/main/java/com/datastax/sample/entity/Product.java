package com.datastax.sample.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * Entity for table Products.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Table(value = "products")
public class Product implements Serializable {
    
    /** Serial for the class. */
    private static final long serialVersionUID = -5844442448334944278L;
    
    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = Name.UUID)
    private UUID id;
    
    @Column("name")
    @CassandraType(type = Name.TEXT)
    private String name;
    
    @Column("description")
    @CassandraType(type = Name.TEXT)
    private String description;
    
    @Column("price")
    @CassandraType(type = Name.DECIMAL)
    private Double price;
    
    @Column("created")
    @CassandraType(type = Name.TIMESTAMP)
    private Instant created;

    /**
     * Getter accessor for attribute 'name'.
     *
     * @return
     *       current value of 'name'
     */
    public String getName() {
        return name;
    }

    /**
     * Setter accessor for attribute 'name'.
     * @param name
     * 		new value for 'name '
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter accessor for attribute 'id'.
     *
     * @return
     *       current value of 'id'
     */
    public UUID getId() {
        return id;
    }

    /**
     * Setter accessor for attribute 'id'.
     * @param id
     * 		new value for 'id '
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Getter accessor for attribute 'description'.
     *
     * @return
     *       current value of 'description'
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter accessor for attribute 'description'.
     * @param description
     * 		new value for 'description '
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter accessor for attribute 'price'.
     *
     * @return
     *       current value of 'price'
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Setter accessor for attribute 'price'.
     * @param price
     * 		new value for 'price '
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Getter accessor for attribute 'created'.
     *
     * @return
     *       current value of 'created'
     */
    public Instant getCreated() {
        return created;
    }

    /**
     * Setter accessor for attribute 'created'.
     * @param created
     * 		new value for 'created '
     */
    public void setCreated(Instant created) {
        this.created = created;
    }
    
}
