package uk.ac.ebi.pwp.structures.test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import uk.ac.ebi.pwp.structures.quadtree.client.Box;
import uk.ac.ebi.pwp.structures.quadtree.client.QuadTree;
import uk.ac.ebi.pwp.structures.quadtree.client.QuadTreeBox;

import java.util.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WidgetTest implements EntryPoint {
    // IMPORTANT! ATTENTION!
    // Do NOT use the class name for the place holder ( but it is case sensitive :D )
    @SuppressWarnings("unused")
    private static final String PLACE_HOLDER = "widgettest";

    private List<Box> list = new LinkedList<>();
    private QuadTree<Box> tree;

    @Override
    public void onModuleLoad() {
        test1();
        test2();
        test3();
    }

    private void test3() {
        this.tree.clear();
        GWT.log(this.tree.getItems().size() + " <-- should be 0");
    }

    private void test2() {
        this.tree = new QuadTree<>(100, 100, 3, 25);
        Box a = new Box(80, 90, 80, 90);
        Box b = new Box(80, 30, 90, 40);
        Box c = new Box(30, 80, 40, 90);

        Box d = new Box(30, 10, 40, 20);
        Box e = new Box(30, 30, 40, 40);
        Box f = new Box(10, 30, 20, 40);

        Box g = new Box(10, 20, 10, 20);

        this.tree.add(a);
        this.tree.add(b);
        this.tree.add(c);
        this.tree.add(d);
        this.tree.add(e);
        this.tree.add(f);
        this.tree.add(g);

        this.tree.remove(g);

        this.tree.add(g);

        Box h = new Box(40, 0, 50, 10);
        Box i = new Box(40, 20, 50, 30);
        Box j = new Box(20, 0, 30, 10);

        this.tree.add(h);
        this.tree.add(i);
        this.tree.add(j);

        this.tree.remove(j);
    }

    private void test1() {
        this.tree = new QuadTree<>(15000, 15000);
        GWT.log("ELEMENTS: 20,000");
        for (int i = 0; i < 20000; i++) {
            Box x = new Box(i, i, i + 2, i + 2);
            this.list.add(x);
            this.tree.add(x);
        }

        Set<Box> set = new HashSet<>();

        GWT.log("\tGET ITEMS BY POINT");
        long start = new Date().getTime();
        for (int i = 0; i < 1000; i++) {
            set = tree.getItems(15, 15);
        }
        long end = new Date().getTime();
        report("QuadTree", set.size(), (end - start) / 1000.0);

        set.clear();
        start = new Date().getTime();

        for (Box box : list) {
            if (box.contains(15, 15)) {
                set.add(box);
            }
        }
        end = new Date().getTime();
        report("List    ", set.size(), (end - start));


        GWT.log("\tGET ITEMS BY AREA");
        QuadTreeBox area = new Box(10, 10, 200, 200);
        start = new Date().getTime();
        for (int i = 0; i < 1000; i++) {
            set = tree.getItems(area);
        }
        end = new Date().getTime();
        report("QuadTree", set.size(), (end - start) / 1000.0);

        set.clear();
        start = new Date().getTime();

        for (Box box : list) {
            if (box.touches(area)) {
                set.add(box);
            }
        }
        end = new Date().getTime();
        report("List    ", set.size(), (end - start));
    }

    private void report(String dataStructure, int targetNodes, double milliseconds) {
        GWT.log("\t" + dataStructure + " -> Target nodes: " + targetNodes + " || Time: " + milliseconds + "ms");
    }
}
