package ru.practicum.ewmserv.event.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class Location {

    @Column(nullable = false, name = "location_lat")
    private float lat;

    @Column(nullable = false, name = "location_lon")
    private float lon;
}
