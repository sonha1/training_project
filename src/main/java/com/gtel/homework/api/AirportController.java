package com.gtel.homework.api;

import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.AirportRequest;
import com.gtel.homework.model.response.AirportResponse;
import com.gtel.homework.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/airports")
@SecurityRequirement(name = "bearerAuth")
public class AirportController {
    @Autowired
    private AirportService airportService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AirportResponse> getAirports(@RequestParam(required = false) Map<String, String> params) {
        return airportService.getAirportsNo4(params);
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<?> countAirports() {
        int count = airportService.countAirports();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(count)).build();
    }

    @GetMapping("/{id}")
    public AirportResponse getAirport(@PathVariable String id) {
        return airportService.getAirport2(id);
    }

    @PostMapping
    public void createAirport(@RequestBody AirportRequest airportRequest) throws ApplicationException {
        airportService.createAirport(airportRequest);
    }

    @PutMapping("/{id}")
    public void updateAirport(@PathVariable String id, @RequestBody AirportRequest airportRequest) {
        airportService.updateAirports(id, airportRequest);
    }

    @PatchMapping("/{id}")
    public void updatePatchAirport(@PathVariable String id, @RequestBody AirportRequest airportRequest) {
        airportService.updatePathAirports(id, airportRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable String id) {
        airportService.deleteAirport(id);
    }
}
