package com.viktority.pathoptimizer.services;

import com.viktority.pathoptimizer.algo.ShortestPath;
import com.viktority.pathoptimizer.pojo.Address;
import com.viktority.pathoptimizer.pojo.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OptimizedPathService {

    private static final Logger logger = LoggerFactory.getLogger(OptimizedPathService.class);
    private static final String apiURI = "https://api.postcodes.io/postcodes/";

    public List<String> findOptimizedPath(List<String> locations, String start, String dest) {
        logger.info("Finding optimized path from {} to {}", start, dest);

        // Remove the destination and starting locations from the list of locations.
        List<String> list = new ArrayList<>(locations);
        list.remove(dest);
        list.remove(start);

        // Get the addresses for the starting and destination locations.
        Optional<Address> addressDataFromPostCode = getAddressFromPostCode(start);
        Optional<Address> addressDataFromPostCode1 = getAddressFromPostCode(dest);
        if(addressDataFromPostCode.isEmpty()){
            logger.info("Invalid Start postcode - {}", start);
            return new ArrayList<>();
        }

        if(addressDataFromPostCode1.isEmpty()){
            logger.info("Invalid Destination postcode - {}", dest);
            return new ArrayList<>();
        }

        Address startAddress = addressDataFromPostCode.get();
        Address stopAddress = addressDataFromPostCode1.get();

        // Get the addresses for all the intermediate locations.
        List<Address> addresses = list.stream()
                .map(this::getAddressFromPostCode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // Create a new instance of the ShortestPath class.
        ShortestPath shortestPath = new ShortestPath();

        // Find the optimal route from the starting address to the destination address.
        List<Address> xx = shortestPath.findOptimalRoute(startAddress, addresses);

        // Add the destination address to the list of addresses.
        xx.add(stopAddress);

        // Convert the list of addresses to a list of strings.
        List<String> path = xx.stream().map(Address::getPostcode).collect(Collectors.toList());

        logger.info("Found optimized path: {}", path);

        return path;
    }

    private Optional<Address> getAddressFromPostCode(String postCode) {
        //External Service
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Root> response = restTemplate.getForEntity(apiURI + postCode, Root.class);
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            Root body = response.getBody();
            Root root = Objects.requireNonNull(body);

            Address result = root.getResult();
            return Optional.of(result);
        }
        return Optional.empty();
    }

}

