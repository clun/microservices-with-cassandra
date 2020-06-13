package com.datastax.sample.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datastax.oss.driver.api.core.DriverException;
import com.datastax.sample.entity.TimeserieDaily;
import com.datastax.sample.repository.TimeseriesRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Resources working with {@link TimeserieDaily}.
 * This CRUD resource leverages on standard HTTP Codes and patterns.
 */
@RestController
@RequestMapping("/api/v1/timeseries")
@Tag(name = "Timeseries", description = "Save ticks and search by source and day (yyyymmdd)")
public class TimeseriesRestResource {

    /** Logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(TimeseriesRestResource.class);
    
    /** Service implementation Injection. */
    @Autowired
    private TimeseriesRepository timeseriesRepository;

    /**
     * Best practice : Inversion of Control through constructor and no More @Inject nor @Autowired
     * 
     * @param tickRepo
     *      repository implementation
     */
    public TimeseriesRestResource(TimeseriesRepository tickRepo) {
        this.timeseriesRepository = tickRepo;
    }
    
    
    /**
     * Retrieve the timeseries for current day
     *
     * @param source
     *      unique source
     * @return
     *      list of tickData
     */
    @Operation(
            summary = "Retrieve a serie for today", 
            description = "Retrieve a serie based on source for today", 
            tags = { "Timeseries" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "successful operation", 
                         content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeserieDaily.class)))) })  
    @RequestMapping(
            value = "/{source}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimeserieDaily>>  findSerieToday(
            @PathVariable(value = "source") 
            @Parameter(allowEmptyValue = false, description = "Source for events", 
                       name = "source", example = "sensor_X")
            String source) {
        logger.info("Search serie for today and source '{}'", source);
        return ResponseEntity.ok(timeseriesRepository.findTimeSeriesToday(source));
    }
    
    /**
     * Retrieve the timeseries for defined day by yyyymmdd
     *
     * @param symbol
     *      unique symbol
     * @return
     *      list of tickData
     */
    @Operation(
            summary = "Retrieve a serie for a defined day", 
            description = "Retrieve a serie from %source% and day %yyyymmdd%", 
            tags = { "Timeseries" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "successful operation", 
                         content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeserieDaily.class)))) })  
    @RequestMapping(
            value = "/{source}/{yyyymmdd}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimeserieDaily>>  findSerieADay(
            @PathVariable(value = "source") 
            @Parameter(allowEmptyValue = false, description = "Source for events", 
            name = "source", example = "sensor_X")
            String source, 
            
            @PathVariable(value = "yyyymmdd") 
            @Parameter(allowEmptyValue = false, description = "Filter by day for yyyymmdd", 
            name = "yyyymmdd", example = "20200501")
            String yyyymmdd) {
        logger.info("Retrieving timeserie with symbol {}", source);
        return ResponseEntity.ok(timeseriesRepository.findByTimeserieDailyKeySourceAndTimeserieDailyKeyYyyymmdd(source, yyyymmdd));
    }
    
    /**
     * Insert a new value for a time series. Preferred to 
     * PUT as the full URL is not known
     *
     * @param symbol
     *      unique symbol
     * @return
     *      list of tickData
     */
    @Operation(
            summary = "Save a new value for on source", 
            description = "tick and yyyymmdd are computed at insertion", 
            tags = { "Timeseries" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" ,description = "successful operation", 
                         content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeserieDaily.class)))) })  
    @RequestMapping(
            method = POST,
            value = "/", 
            consumes = APPLICATION_JSON_VALUE,
            produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<Instant>  save(HttpServletRequest request,
            
            @RequestParam("source") 
            @Parameter(allowEmptyValue = false, description = "Source for events", 
            name = "source", example = "sensor_X")
            String source,
            
            @RequestParam("value") 
            @Parameter(allowEmptyValue = false, description = "Metric registered", 
            name = "value", example = "0.1")
            Double value) {
        
        TimeserieDaily record = new TimeserieDaily(source, value);
        timeseriesRepository.save(record);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/v1/timeseries/{source}/{yyyymmdd}")
                .buildAndExpand(
                        record.getTimeserieDailyKey().getSource(), 
                        record.getTimeserieDailyKey().getYyyymmdd())
                .toUri();
        // HTTP 201 with confirmation number
        return ResponseEntity.created(location).body(record.getTimeserieDailyKey().getTick());
    }
    
    /**
     * Converts {@link IllegalArgumentException} into HTTP 400 bad parameter
     * the response body.
     *
     * @param e The {@link DriverException}.
     * @return The error message to be used as response body.
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String _errorBadRequestHandler(IllegalArgumentException ex) {
        return "Invalid Parameter: " + ex.getMessage();
    }
    
    /**
     * Converts {@link DriverException} into HTTP 500 error codes and outputs the error message as
     * the response body.
     *
     * @param e The {@link DriverException}.
     * @return The error message to be used as response body.
     */
    @ExceptionHandler(DriverException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String _errorDriverHandler(DriverException e) {
      return e.getMessage();
    }

}
