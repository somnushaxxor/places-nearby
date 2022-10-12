package ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper;

import ru.nsu.fit.kolesnik.placesnearby.model.location.Location;
import ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.payload.GraphHopperResponseItem;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Coordinates;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Mapper;

public class GraphHopperLocationMapper implements Mapper<GraphHopperResponseItem, Location> {

    @Override
    public Location map(GraphHopperResponseItem graphHopperResponseItem) {
        Coordinates coordinates = new Coordinates(graphHopperResponseItem.getCoordinates().getLatitude(),
                graphHopperResponseItem.getCoordinates().getLongitude());
        return new Location(graphHopperResponseItem.getName(), coordinates,
                graphHopperResponseItem.getCountry(), graphHopperResponseItem.getState(),
                graphHopperResponseItem.getCity(), graphHopperResponseItem.getStreet(),
                graphHopperResponseItem.getHouseNumber(), graphHopperResponseItem.getPostcode());
    }

}
