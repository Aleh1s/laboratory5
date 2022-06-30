package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Node {

    private List<Leaf> leaves;
    private List<Node> children;
    private Rectangle mbr;

    public Node() {
        this.leaves = new LinkedList<>();
        this.children = new LinkedList<>();
        this.mbr = new Rectangle();
    }

    public void addLeaf(Leaf leaf) {
        if (leaves.size() == 0) {
            mbr.initMBR(leaf.getMbr());
        }

        mbr.increaseMBR(leaf.getMbr());
        leaves.add(leaf);
    }

    public void addChild(Node child) {
        if (children.size() == 0) {
            mbr.initMBR(child.getMbr());
        }

        mbr.increaseMBR(child.mbr);
        children.add(child);
    }

    private void initMBR(Rectangle mbr) {
        this.mbr.setMinLatitude(mbr.getMinLatitude());
        this.mbr.setMaxLatitude(mbr.getMaxLatitude());
        this.mbr.setMinLongitude(mbr.getMinLongitude());
        this.mbr.setMaxLongitude(mbr.getMaxLongitude());
    }

    private void increaseMBR(Rectangle mbr) {
        double maxLatitude = mbr.getMaxLatitude();
        if (mbr.getMaxLatitude() < maxLatitude) mbr.setMaxLatitude(maxLatitude);

        double minLatitude = mbr.getMinLatitude();
        if (mbr.getMinLatitude() > minLatitude) mbr.setMinLatitude(minLatitude);

        double maxLongitude = mbr.getMaxLongitude();
        if (mbr.getMaxLongitude() < maxLongitude) mbr.setMaxLongitude(maxLongitude);

        double minLongitude = mbr.getMinLongitude();
        if (mbr.getMinLongitude() > minLongitude) mbr.setMinLongitude(minLongitude);
    }

    @Override
    public String toString() {
        return String.format(
                """
                        Node: {
                            leaves: [
                                %s
                            ],
                            children: [
                                %s
                            ],
                            mbr: %s
                        }
                        """
        , leaves, children, mbr);
    }

    //"Node{" +
    //                "leaves=" + leaves +
    //                ", children=" + children +
    //                ", mbr=" + mbr +
    //                '}';
}
