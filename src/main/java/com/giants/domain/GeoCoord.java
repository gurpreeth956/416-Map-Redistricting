package com.giants.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "GeoCoords")
public class GeoCoord {

    @Embeddable
    public static class GeoCoordsKey implements Serializable {

        private int geoJsonId;
        private double latitude;
        private double longitude;

        public GeoCoordsKey(int geoJsonId, double latitude, double longitude) {
            this.geoJsonId = geoJsonId;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Column(name = "geoJsonId")
        public int getGeoJsonId() {
            return geoJsonId;
        }

        public void setGeoJsonId(int geoJsonId) {
            this.geoJsonId = geoJsonId;
        }

        @Column(name = "latitude")
        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        @Column(name = "longitude")
        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoCoordsKey that = (GeoCoordsKey) o;
            return geoJsonId == that.geoJsonId &&
                    Double.compare(that.latitude, latitude) == 0 &&
                    Double.compare(that.longitude, longitude) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(geoJsonId, latitude, longitude);
        }
    }

    private GeoCoordsKey geoCoordsKey;
    private GeoJSON geoJson;

    public GeoCoord() {

    }

    public GeoCoord(GeoJSON geoJson, double latitude, double longitude) {
        this.geoCoordsKey = new GeoCoordsKey(geoJson.getId(), latitude, longitude);
        this.geoJson = geoJson;
    }

    @EmbeddedId
    public GeoCoordsKey getGeoCoordsKey() {
        return geoCoordsKey;
    }

    public void setGeoCoordsKey(GeoCoordsKey geoCoordsKey) {
        this.geoCoordsKey = geoCoordsKey;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("geoJsonId")
    @JoinColumn(name="geoJsonId", referencedColumnName = "id", insertable = false, updatable = false)
    public GeoJSON getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(GeoJSON geoJson) {
        this.geoJson = geoJson;
    }
}
