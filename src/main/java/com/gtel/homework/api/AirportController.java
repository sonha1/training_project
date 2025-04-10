package com.gtel.homework.api;

import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.AirportRequest;
import com.gtel.homework.model.response.AirportResponse;
import com.gtel.homework.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/airports")
public class AirportController {
    @Autowired
    private AirportService airportService;

    @GetMapping
    public List<AirportResponse> getAirports(@RequestParam(required = false) Map<String, String> params) {
        return airportService.getAirportsNo4(params);
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity countAirports() {
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
