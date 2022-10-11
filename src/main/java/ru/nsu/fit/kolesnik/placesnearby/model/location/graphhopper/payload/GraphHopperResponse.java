package ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphHopperResponse {

    List<GraphHopperLocation> hits;

}
