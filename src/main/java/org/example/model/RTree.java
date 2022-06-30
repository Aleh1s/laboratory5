package org.example.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RTree {

    private Node root;

    private final int MAX_ENTRIES = 84;
    private final int MAX_BUCKET_SIZE = 10_000;
    private final String DEFAULT_PATH = "D:\\Programming\\csv\\ukraine_poi.csv";


    public RTree() throws IOException {
        initTree();
    }

    public List<Place> search(double latitude, double longitude, int size) {
        int halfOfSize = size / 2;
        double distanceLatitude = toLatitude(halfOfSize);
        double distanceLongitude = toLongitude(halfOfSize);

        List<Place> places = new LinkedList<>();
        Rectangle sector = new Rectangle(
                latitude + distanceLatitude,
                latitude - distanceLatitude,
                longitude + distanceLongitude,
                longitude - distanceLongitude

        );

        chooseLeaf(sector, root, places);
        return places;
    }

    private double toLatitude(int size) {
        return size / 111.0;
    }


    private double toLongitude(int size) {
        return size / 111.3;
    }

    private void chooseLeaf (Rectangle sector, Node current, List<Place> includedInSector) {
        if (!current.getLeaves().isEmpty()) {
            List<Leaf> leaves = current.getLeaves();

            for (Leaf leaf : leaves) {
                addIncludedInSectorPlaces(sector, leaf, includedInSector);
            }
        } else {
            for (Node child : current.getChildren()) {
                if (sector.isCrosses(child.getMbr())) {
                    chooseLeaf(sector, child, includedInSector);
                }
            }
        }
    }

    private void addIncludedInSectorPlaces(Rectangle rectangle, Leaf src, List<Place> dest) {
        for (Place place : src.getData()) {
            if (rectangle.liesInPlane(place.getPoint())) dest.add(place);
        }
    }

    public void insertAll(List<Place> places) {
        for (Place place : places) {
            insert(place);
        }
    }

    public void insert(Place place) {
        if (root == null) {
            Leaf leaf = new Leaf();
            root = new Node();
            leaf.addPlace(place);
            root.addLeaf(leaf);
        } else {
            insert(place, root);
        }
    }

    private void insert(Place place, Node parent) {
        Optional<List<Node>> optionalNodes
                = chooseLeaf(place, parent);

        if (optionalNodes.isPresent()) {
            Node newRoot = new Node();
            List<Node> nodes = optionalNodes.get();
            for (Node node : nodes) newRoot.addChild(node);
            root = newRoot;
        }
    }

    public Optional<List<Node>> chooseLeaf(Place place, Node parent) {
        Point point = place.getPoint();
        if (!parent.getLeaves().isEmpty()) {
            Leaf leafWithMinMbrIncreasing = findLeafWithMinMbrIncreasing(parent, point);

            Optional<List<Leaf>> splitLeaves
                    = doInsertPlace(place, leafWithMinMbrIncreasing);
            if (splitLeaves.isPresent()) {
                parent.getLeaves().remove(leafWithMinMbrIncreasing);
                return doInsertLeaves(splitLeaves.get(), parent);
            }

            return Optional.empty();
        } else {
            Node nodeWithMinMbrIncreasing = findNodeWithMinMbrIncreasing(parent, point);

            Optional<List<Node>> nodes
                    = chooseLeaf(place, nodeWithMinMbrIncreasing);
            if (nodes.isPresent()) {
                parent.getChildren().remove(nodeWithMinMbrIncreasing);
                return doInsertNodes(nodes.get(), parent);
            }
        }

        return Optional.empty();
    }

    private Node findNodeWithMinMbrIncreasing(Node parent, Point point) {
        List<Node> children = parent.getChildren();
        Node nodeWithMinIncreasing = children.get(0);
        Rectangle mbrOfNodeWithMinIncreasing = nodeWithMinIncreasing.getMbr();
        double minIncrease = mbrOfNodeWithMinIncreasing.countIncrease(point);

        for (int i = 1; i < children.size(); i++) {
            Node current = children.get(i);
            Rectangle mbr = current.getMbr();
            double increase = mbr.countIncrease(point);

            if (minIncrease > increase) {
                minIncrease = increase;
                nodeWithMinIncreasing = current;
            }
        }

        return nodeWithMinIncreasing;
    }
    private Leaf findLeafWithMinMbrIncreasing(Node parent, Point point) {
        List<Leaf> leaves = parent.getLeaves();
        Leaf leafWithMinMbrIncreasing = leaves.get(0);
        Rectangle mbrOfLeafWithMinIncreasing = leafWithMinMbrIncreasing.getMbr();
        double minIncreasing = mbrOfLeafWithMinIncreasing.countIncrease(point);

        for (int i = 1; i < leaves.size(); i++) {
            Leaf currentLeaf = leaves.get(i);
            Rectangle currentLeafMbr = currentLeaf.getMbr();
            double increasing = currentLeafMbr.countIncrease(point);

            if (minIncreasing > increasing) {
                minIncreasing = increasing;
                leafWithMinMbrIncreasing = currentLeaf;
            }
        }

        return leafWithMinMbrIncreasing;
    }

    public Optional<List<Leaf>> doInsertPlace(Place place, Leaf leaf) {
        leaf.addPlace(place);

        List<Place> data = leaf.getData();
        if (data.size() > MAX_BUCKET_SIZE) {
            return Optional.of(linearSplitData(leaf));
        }

        return Optional.empty();
    }

    public List<Leaf> linearSplitData(Leaf leaf) {
        Leaf l1 = new Leaf();
        Leaf l2 = new Leaf();

        List<Place> data = leaf.getData();
        Point centerPointCoordinates = countCenter(leaf.getMbr());

        for (Place place : data) {
            Point point = place.getPoint();
            boolean isLeft = point.getLatitude() < centerPointCoordinates.getLatitude();

            if (isLeft) l1.addPlace(place);
            else l2.addPlace(place);

        }

        return List.of(l1, l2);
    }

    public Point countCenter(Rectangle coordinates) {
        double height = coordinates.getMaxLongitude() + coordinates.getMinLongitude();
        double width = coordinates.getMaxLatitude() + coordinates.getMinLatitude();

        return new Point(
                height / 2,
                width / 2
        );
    }

    public Optional<List<Node>> doInsertLeaves(List<Leaf> leaves, Node parent) {
        for (Leaf leaf : leaves) {
            parent.addLeaf(leaf);
        }

        List<Leaf> parentLeaves = parent.getLeaves();
        if (parentLeaves.size() > MAX_ENTRIES) {
            return Optional.of(linearSplitLeaves(parent));
        }

        return Optional.empty();
    }


    public Optional<List<Node>> doInsertNodes(List<Node> nodes, Node parent) {
        for (Node node : nodes) {
            parent.addChild(node);
        }

        List<Node> children = parent.getChildren();
        if (children.size() > MAX_ENTRIES) {
            return Optional.of(linearSplitChildren(parent));
        }

        return Optional.empty();
    }

    public List<Node> linearSplitChildren(Node node) {
        Node n1 = new Node();
        Node n2 = new Node();

        List<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            if (i < children.size() / 2) {
                n1.addChild(children.get(i));
            } else {
                n2.addChild(children.get(i));
            }
        }

        return List.of(n1, n2);
    }

    public List<Node> linearSplitLeaves(Node node) {
        Node n1 = new Node();
        Node n2 = new Node();

        List<Leaf> leaves = node.getLeaves();
        for (int i = 0; i < leaves.size(); i++) {
            if (i < leaves.size() / 2) {
                n1.addLeaf(leaves.get(i));
            } else {
                n2.addLeaf(leaves.get(i));
            }
        }

        return List.of(n1, n2);
    }

    public void initTree() throws IOException {
        List<String> data = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DEFAULT_PATH))) {

            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }

            List<Place> places = new LinkedList<>();
            for (String str : data) {
                Place parse = Place.parse(str);
                places.add(parse);
            }

            insertAll(places);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(
                    String.format("File with path [ %s ] does not exists", DEFAULT_PATH)
            );
        } catch (IOException e) {
            throw new IOException(
                    String.format("Can not read from file with path [ %s ]", DEFAULT_PATH), e
            );
        }
    }

    @Override
    public String toString() {
        return String.format("""
                RTree: {
                    root: %s
                }
                """, root);
    }
}
