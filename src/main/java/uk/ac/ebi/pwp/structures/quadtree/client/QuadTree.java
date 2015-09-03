package uk.ac.ebi.pwp.structures.quadtree.client;

import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class QuadTree<T extends QuadTreeBox> {

    private QuadNode<T> root = null;

    public QuadTree(double minX, double minY, double maxX, double maxY){
        this.root = new QuadNode<>(minX, minY, maxX, maxY, 5);
    }

    public QuadTree(double minX, double minY, double maxX, double maxY, int numberOfItems){
        this.root = new QuadNode<>(minX, minY, maxX, maxY, numberOfItems);
    }

    public QuadTree(double width, double height) {
        this(0, 0, width, height);
    }

    public QuadTree(double width, double height, int numberOfItems) {
        this(0, 0, width, height, numberOfItems);
    }

    public boolean add(T item){
        return this.root.add(new QuadItem<>(item));
    }

    public boolean remove(T item){
        return this.root.remove(item);
    }

    public Set<T> getItems(double x, double y){
        return this.root.getItems(x,y);
    }

    public Set<T> getItems(QuadTreeBox area){
        return this.root.getItems(area);
    }

}
