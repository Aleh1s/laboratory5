package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Leaf {

    private List<Place> data;
    private Rectangle mbr;

    public Leaf() {
        this.data = new LinkedList<>();
        this.mbr = new Rectangle();
    }

    public boolean addPlace(Place place) {
        Point coordinates = place.getPoint();

        if (data.isEmpty()) {
            mbr.initMBR(coordinates);
        }

        mbr.increaseMBR(coordinates);
        data.add(place);
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                """
                        data: [
                            %s
                        ],
                        mbr: %s
                        
                        """
        , data, mbr);
    }

    //"Leaf{" +
    //                "data=" + data +
    //                ", rectangle=" + rectangle +
    //                '}';
}
