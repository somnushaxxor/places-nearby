package ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap;

import ru.nsu.fit.kolesnik.placesnearby.model.place.Place;
import ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.payload.OpenTripMapPlaceInfoResponse;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Coordinates;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Mapper;

public class OpenTripMapPlaceMapper implements Mapper<OpenTripMapPlaceInfoResponse, Place> {

    @Override
    public Place map(OpenTripMapPlaceInfoResponse openTripMapPlaceInfoResponse) {
        Coordinates coordinates = new Coordinates(openTripMapPlaceInfoResponse.getCoordinates().getLatitude(),
                openTripMapPlaceInfoResponse.getCoordinates().getLongitude());
        String name;
        if (openTripMapPlaceInfoResponse.getName().isEmpty()) {
            name = "UNKNOWN";
        } else {
            name = openTripMapPlaceInfoResponse.getName();
        }
        return new Place(name, coordinates, openTripMapPlaceInfoResponse.getKinds(),
                openTripMapPlaceInfoResponse.getImageUrl(), openTripMapPlaceInfoResponse.getWikipediaUrl());
    }

}
