package com.viktority.pathoptimizer.algo;

import com.viktority.pathoptimizer.pojo.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ShortestPath {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShortestPath.class);

    /**
     * This method takes a reference address, a list of addresses, and a new list. It recursively adds addresses to the new list
     * <p>
     * by finding the nearest neighbor of the reference address until the input list is empty.
     *
     * @param ref     The reference address to calculate distances from
     * @param list    The input list of addresses
     * @param newList The list to add the nearest neighbor addresses to
     * @return The new list containing the nearest neighbor addresses
     */
    public List<Address> findOptimalRoute(Address ref, List<Address> list, List<Address> newList) {
        LOGGER.info("Finding optimal route from {}", ref.getPostcode());

        // Add the reference address to the new list
        newList.add(ref);

        // If the input list is empty, return the new list
        if (list.isEmpty()) {
            LOGGER.info("No addresses found, returning route with only reference address");
            return newList;
        }

        // Otherwise, find the nearest neighbor address to the reference address
        else {
            LOGGER.info("Finding nearest neighbor address to {}", ref.getPostcode());

            // Calculate distances from the reference address to each address in the input list
            List<Double> distances = list.stream()
                    .mapToDouble(address -> calculateDistance(address, ref))
                    .boxed()
                    .collect(Collectors.toList());

            // Find the minimum distance
            Double min = Collections.min(distances);

            // Get the index of the address with the minimum distance
            int minIndex = distances.indexOf(min);

            // Get the address with the minimum distance
            Address address = list.get(minIndex);

            LOGGER.info("Shortest distance from {} -- {}", ref.getPostcode(), address.getPostcode());

            // Remove the nearest neighbor address from the input list
            list.remove(address);

            // Recursively call the method with the nearest neighbor address
            findOptimalRoute(address, list, newList);
        }

        // Return the new list containing all the nearest neighbor addresses
        LOGGER.info("Found optimal route: {}", newList.stream().map(Address::getPostcode).collect(Collectors.toList()));
        return newList;
    }


    /**
     * Finds the optimal route given a reference address and a list of addresses.
     *
     * @param ref       The reference address to start the route.
     * @param addresses The list of addresses to visit.
     * @return The optimal route as a list of addresses.
     */

    public List<Address> findOptimalRoute(Address ref, List<Address> addresses) {
        LOGGER.info("Finding optimal route from {}", ref.getPostcode());

        // Create a new list to store the optimal route
        List<Address> route = new ArrayList<>();

        // Add the reference address as the starting point of the route
        route.add(ref);

        // If the addresses list is null or empty, return the route with only the reference address
        if (addresses == null || addresses.isEmpty()) {
            LOGGER.info("No addresses found, returning route with only reference address");
            return route;
        }

        // Create a priority queue to store the addresses based on their distances to the reference address
        PriorityQueue<Address> queue = new PriorityQueue<>(Comparator.comparingDouble(address -> calculateDistance(address, ref)));

        // Add all the addresses to the priority queue
        queue.addAll(addresses);

        // Initialize the previous address as the reference address
        Address prev = ref;

        // Iterate until the queue is empty
        while (!queue.isEmpty()) {
            // Get the next address with the minimum distance from the queue
            Address next = queue.poll();

            // Check if the next address is null (in case of unexpected input)
            if (next == null) {
                LOGGER.info("No more addresses found, breaking");
                break;
            }

            // Add the next address to the route if it's different from the previous address
            if (!next.equals(prev)) {
                route.add(next);
            }

            // Update the previous address to the next address
            prev = next;
        }

        // Return the optimal route
        LOGGER.info("Found optimal route: {}", route.stream().map(Address::getPostcode).collect(Collectors.toList()));
        return route;
    }


    private double calculateDistance(Address a, Address b) {
        double R = 6371; // Earth's radius in kilometers
        double lat1 = Math.toRadians(a.getLatitude());
        double lon1 = Math.toRadians(a.getLongitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double lon2 = Math.toRadians(b.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a1), Math.sqrt(1 - a1));

        return R * c;
    }
}
