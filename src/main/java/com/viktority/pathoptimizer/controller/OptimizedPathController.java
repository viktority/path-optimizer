package com.viktority.pathoptimizer.controller;

import com.viktority.pathoptimizer.services.OptimizedPathService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/optimize-path")
public class OptimizedPathController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizedPathController.class);
    private final OptimizedPathService optimizedPathService;

    @Autowired
    public OptimizedPathController(OptimizedPathService optimizedPathService) {
        this.optimizedPathService = optimizedPathService;
    }

    @ApiOperation(value = "Retrieve optimized path", notes = "Retrieves the optimized path from a starting location to a destination using a list of locations.")
    @GetMapping
    public List<String> findOptimizedPath(
            @ApiParam(value = "List of Postcodes to include in the path optimization", example = "[BH12PE, BH121DF, BH88AQ, BH51BL, BH92BB,  BH88UN, BH125BB, BH76HW, BH89RT, BH14QP ]")
            @RequestParam("locations") List<String> locations,

            @ApiParam(value = "Starting location(postcode) for the path", example = "BH12PE")
            @RequestParam("start_postcode") String start,

            @ApiParam(value = "Destination location(postcode) for the path", example = "BH76HW")
            @RequestParam("destination") String dest) {

        LOGGER.info("Finding optimized path from {} to {}", start, dest);
        List<String> path = optimizedPathService.findOptimizedPath(locations, start, dest);
        LOGGER.info("Found optimized path: {}", path);
        return path;
    }

}


