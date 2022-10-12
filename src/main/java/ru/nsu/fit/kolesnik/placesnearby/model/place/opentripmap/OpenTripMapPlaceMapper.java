package ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap;

import ru.nsu.fit.kolesnik.placesnearby.model.place.Place;
import ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.payload.OpenTripMapPlaceInfoResponse;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Coordinates;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Mapper;

public class OpenTripMapPlaceMapper implements Mapper<OpenTripMapPlaceInfoResponse, Place> {

    @Override
    public Place map(OpenTripMapPlaceInfoResponse openTripMapPlaceInfoResponse) {
        Coordinates coordinates = null;
        if (openTripMapPlaceInfoResponse.getCoordinates() != null) {
            coordinates = new Coordinates(openTripMapPlaceInfoResponse.getCoordinates().getLatitude(),
                    openTripMapPlaceInfoResponse.getCoordinates().getLongitude());
        }
        return new Place(openTripMapPlaceInfoResponse.getName(), coordinates, openTripMapPlaceInfoResponse.getKinds(),
                openTripMapPlaceInfoResponse.getImageUrl(), openTripMapPlaceInfoResponse.getWikipediaUrl());
    }

}
