package uk.ac.ebi.pwp.structures.quadtree.client;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
class QuadNode<T extends QuadTreeBox> {
    protected int numberOfItems;
    protected Box box;

    private boolean isLeaf = true;
    private Set<QuadItem<T>> values = new HashSet<>();

    //Children when this node is not a leaf (ORDER: NW, NE, SE, SW)
    private QuadNode<T> NW;
    private QuadNode<T> NE;
    private QuadNode<T> SE;
    private QuadNode<T> SW;

    public QuadNode(Box dimensions, int numberOfItems) {
        this.box = dimensions;
        this.numberOfItems = numberOfItems;
    }

    public QuadNode(double minX, double minY, double maxX, double maxY, int numberOfItems) {
        this(new Box(minX, minY, maxX, maxY), numberOfItems);
    }

    boolean add(QuadItem<T> item) {
        if (this.box == null || !item.touches(this.box)) {
            //If the item is completely outside of the box, we can not add it here
            return false;
        }
        //At this point the item can be added into this node
        if (this.isLeaf) {
            if (this.values.size() < numberOfItems) {
                //There is still space available for a new item
                return values.add(item);
            } else {
                //There is not space so the node has to be broken down into nodes
                this.createChildren();
                //The current values have to be moved a level down
                this.moveValuesDown();
            }
        }
        //Time to add the new item into the corresponding nodes
        return this.addToAllNodes(item);
    }

    boolean remove(T item) {
        QuadItem<T> aux = new QuadItem<>(item);
        if (this.box == null || !aux.touches(this.box)) {
            //If the item is completely outside of the box, won't be here for sure ;)
            return false;
        }
        boolean removed;
        if (this.isLeaf) {
            //We keep QuadItem2D objects but we look for T objects...
            List<QuadItem<T>> target = new LinkedList<>();
            for (QuadItem<T> value : values) {
                if (value.getValue().equals(item)) target.add(value);
            }
            removed = values.removeAll(target);
        } else {
            removed = this.NW.remove(item);
            removed |= this.NE.remove(item);
            removed |= this.SE.remove(item);
            removed |= this.SW.remove(item);
        }
        mergeChildren();
        return removed;
    }

    private boolean addToAllNodes(QuadItem<T> item) {
        boolean added = this.NW.add(item);
        added |= this.NE.add(item);
        added |= this.SE.add(item);
        added |= this.SW.add(item);
        return added;
    }

    public Set<T> getItems(double x, double y) {
        Set<T> set = new HashSet<>();
        if (this.box.contains(x, y)) {
            if (this.isLeaf) {
                for (QuadItem<T> value : this.values) {
                    if (value.contains(x, y)) {
                        set.add(value.getValue());
                    }
                }
            } else {
                set.addAll(this.NW.getItems(x, y));
                set.addAll(this.NE.getItems(x, y));
                set.addAll(this.SE.getItems(x, y));
                set.addAll(this.SW.getItems(x, y));
            }
        }
        return set;
    }

    public Set<T> getItems(QuadTreeBox area) {
        Set<T> set = new HashSet<>();
        if (this.box.intersects(area)) {
            if (this.isLeaf) {
                for (QuadItem<T> value : this.values) {
                    if (value.touches(area)) {
                        set.add(value.getValue());
                    }
                }
            } else {
                set.addAll(this.NW.getItems(area));
                set.addAll(this.NE.getItems(area));
                set.addAll(this.SE.getItems(area));
                set.addAll(this.SW.getItems(area));
            }
        }
        return set;
    }

    public Set<T> getItems() {
        return getItems(box);
    }

    private void createChildren() {
        if (!this.isLeaf) return;

        double minX = this.box.getMinX();
        double minY = this.box.getMinY();
        double maxX = this.box.getMaxX();
        double maxY = this.box.getMaxY();
        double centreX = this.box.getCentreX();
        double centreY = this.box.getCentreY();

        this.NW = new QuadNode<>(minX, minY, centreX, centreY, this.numberOfItems);
        this.NE = new QuadNode<>(centreX, minY, maxX, centreY, this.numberOfItems);
        this.SE = new QuadNode<>(centreX, centreY, maxX, maxY, this.numberOfItems);
        this.SW = new QuadNode<>(minX, centreY, centreX, maxY, this.numberOfItems);

        this.isLeaf = false;
    }

    private void mergeChildren() {
        if (this.isLeaf) return;

        Set<T> items = this.NW.getItems();
        items.addAll(this.NE.getItems());
        items.addAll(this.SE.getItems());
        items.addAll(this.SW.getItems());

        if (items.size() <= numberOfItems) {
            this.isLeaf = true;
            this.NW = this.NE = this.SE = this.SW = null;
            this.values = new HashSet<>();
            for (T item : items) {
                this.values.add(new QuadItem<>(item));
            }
        }
    }

    private void moveValuesDown() {
        if (this.isLeaf) return;
        for (QuadItem<T> value : this.values) {
            this.addToAllNodes(value);
        }
        this.values.clear();
    }
}
