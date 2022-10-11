package ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper;

import ru.nsu.fit.kolesnik.placesnearby.model.location.Location;
import ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.payload.GraphHopperLocation;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Mapper;

public class GraphHopperLocationMapper implements Mapper<GraphHopperLocation, Location> {

    @Override
    public Location map(GraphHopperLocation graphHopperLocation) {
        return new Location(graphHopperLocation.getName(), graphHopperLocation.getPoint().getLatitude(),
                graphHopperLocation.getPoint().getLongitude(), graphHopperLocation.getCountry(),
                graphHopperLocation.getState(), graphHopperLocation.getCity(), graphHopperLocation.getStreet(),
                graphHopperLocation.getHouseNumber(), graphHopperLocation.getPostcode());
    }

}
