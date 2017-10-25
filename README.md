[<img src=https://user-images.githubusercontent.com/6883670/31999264-976dfb86-b98a-11e7-9432-0316345a72ea.png height=75 />](https://reactome.org)

# QuadTree
GWT basic implementation of a QuadTree

## How to use it?

First add EBI Nexus repository in your ```pom.xml``` file

```xml
<repositories>
    ...
    <!-- EBI repo -->
    <repository>
        <id>nexus-ebi-repo</id>
        <name>The EBI internal repository</name>
        <url>http://www.ebi.ac.uk/Tools/maven/repos/content/groups/ebi-repo/</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <!-- EBI SNAPSHOT repo -->
    <repository>
        <id>nexus-ebi-snapshot-repo</id>
        <name>The EBI internal snapshot repository</name>
        <url>http://www.ebi.ac.uk/Tools/maven/repos/content/groups/ebi-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

Then add the QuadTree dependency

```xml
<dependencies>
    ...
    <dependency>
        <groupId>uk.ac.ebi.pwp.widgets</groupId>
        <artifactId>quadtree</artifactId>
        <version>1.0.0</version>
    </dependency>
<dependencies>
```    

Let's now define a dummy class called ```Rectangle``` that will be used in the QuadTree example bellow:

```java
public class Rectangle implements QuadTreeBox {
  private double x;
  private double y;
  private double width;
  private double height;
  private String text;

  public MyElement(double x, double y, double width, double height, String text){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.text = text;
  }

  public String getText(){
    return this.text;
  }

  // ####################################
  //  QuadTreeBox methods implementation
  // ####################################

  @Override
  public double getMinX() {
      return this.x;
  }

  @Override
  public double getMinY() {
      return this.y;
  }

  @Override
  public double getMaxX() {
      return this.x + this.width;
  }

  @Override
  public double getMaxY() {
      return this.y + this.height;
  }
}
```  
  
Now that we have the ```Rectangle``` class, the way of creating a ```QuadTree``` to store them would be:

```java
...
//Defining the properties of the QuadTree
int numberOfElements = 5; //Max number of elements to be stored per cuadrant
double minX = 0;
double minY = 0;
double maxX = 1000;
double maxY = 1000;

//Instantition of the QuadTree
QuadTree<Rectangle> quadTree = new QuadTree<>(minX, minY, maxX, maxY, numberOfElements);

Rectangle r1 = new Rectangle(5, 10, 100, 75, "Rectangle 1");
Rectangle r2 = new Rectangle(25, 100, 150, 90, "Rectangle 2");
...

quadTree.add(r1);
quadTree.add(r2);
...
```

Querying the QuadTree:

```java
//Elements for a given point
double x = 150;
doule y = 120;
Set<Rectangle> set = three.getItems(x, y);

//Elements for a given area
QuadTreeBox area = new Box(10, 10, 200, 200);
Set<Rectangle> set = tree.getItems(area);
```
