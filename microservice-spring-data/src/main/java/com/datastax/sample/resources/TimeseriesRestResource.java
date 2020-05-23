package com.datastax.sample.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.DriverException;
import com.datastax.sample.entity.TimeserieDaily;
import com.datastax.sample.repository.TimeseriesRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Resources working with {@link TimeserieDaily}.
 * This CRUD resource leverages on standard HTTP Codes and patterns.
 * 
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
     * List all tick in table. Please not there is no implementation of paging. 
     * As such result can be really large. If you query tables with large number 
     * of rows, please use Paging.
     *  
     * @return
     *      list all {@link TimeserieDaily} available in the table 
     */
    @Operation(
            summary = "Retrieve all values from table", 
            description = "Name search by %name% format", 
            tags = { "Timeseries" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "successful operation", 
                         content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeserieDaily.class)))) })  
    @RequestMapping(method = GET, value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimeserieDaily>> findAll() {
        logger.debug("Retrieving all values for the time seire");
        return ResponseEntity.ok(
                StreamSupport.stream(timeseriesRepository.findAll().spliterator(), false)
                             .collect(Collectors.toList()));
    }
    
    /**
     * Retrieve TickData list for a symbol
     *
     * @param symbol
     *      unique symbol
     * @return
     *      list of tickData
     */
    @RequestMapping(
            value = "/{symbol}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimeserieDaily>>  findBySymbol(
            @PathVariable(value = "symbol") String symbol) {
        logger.debug("Retrieving TickData with symbol {}", symbol);
        return ResponseEntity.ok(timeseriesRepository.findByTimeserieDailyKeySourceAndTimeserieDailyKeyYyyymmdd(symbol, "20200520"));
    }
    
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String _errorBadRequestHandler(IllegalArgumentException ex) {
        return "Invalid Parameter: " + ex.getMessage();
    }
    
    /**
     * Converts {@link DriverException}s into HTTP 500 error codes and outputs the error message as
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