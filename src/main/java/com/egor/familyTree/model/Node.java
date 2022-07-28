package com.egor.familyTree.model;

import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Node")
public class Node implements Comparable<Node> {
    
    @GenericGenerator(name = "node_generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "node_generator")
    private int id;

    @Column(name = "childs")
    private LinkedList<Integer> childs;

    @Column(name = "parent1")
    private int parent1;
    
    @Column(name = "parent2")
    private int parent2;

    @Column(name = "age")
    private int age;

    @Column(name = "x")
    public volatile int curX;
    @Column(name = "y")
    public volatile int curY;

    @Transient
    public boolean passed = false;

    @Transient
    private LinkedList<Node> nodes;

    public Node() {
        nodes = new LinkedList<>();
        childs = new LinkedList<>();
    }

    public Node(Person person) {
        childs = new LinkedList<Integer>();
        nodes = new LinkedList<Node>();
        childs.add(person.getId());
        this.parent1 = person.getParent1();
        this.parent2 = person.getParent2();
    }

    public int getParent1() {
        return parent1;
    }

    public void setParent1(int parent1) {
        this.parent1 = parent1;
    }

    public int getParent2() {
        return parent2;
    }

    public void setParent2(int parent2) {
        this.parent2 = parent2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }

    public void add(Person person) {
        childs.add(person.getId());
    }
    
    public void add(Node node) {
        nodes.add(node);
    }

    public LinkedList<Integer> getChilds() {
        return childs;
    }

    public void setChilds(String c) throws NumberFormatException {
        String[] childs = c.split("[;]");
        for (int i = 0; i < childs.length; i++) {
            this.childs.add(Integer.parseInt(childs[i]));
        }
    }

    public void setChilds(LinkedList<Integer> childs) {
        this.childs = childs;
    }
    
    public boolean isSameParents(Person person) {
        if (parent1 == Constants.EMPTY && parent2 == Constants.EMPTY) {
            return false;
        } else {
            return parent1 == person.getParent1() && parent2 == person.getParent2() || parent1 == person.getParent2() && parent2 == person.getParent1();
        }
    }

    public boolean childIsParent(Node node) {
        for (Integer integer : childs) {
            if (node.parent1 == integer || node.parent2 == integer) {
                return true;
            }
        }

        return false;
    }

    public void removeChild(int person) {
        Integer id = -1;
        for (Integer child : childs) {
            if (child == person) {
                id = child;
                break;
            }
        }

        childs.remove(id);
    }

    public boolean isParent(int id) {
        return id == parent1 || id == parent2;
    }

    public void removeParent(Person person) {
        if (person.getId() == parent1) {
            parent1 = Constants.EMPTY;
        } else if (person.getId() == parent2) {
            parent2 = Constants.EMPTY;
        }
    }
    
    public void setCoordinates(int x, int y) {
        passed = true;
        curX = x;
        curY = y;
        int temp = 0;
        for (Node node : nodes) {
            if (node.passed == false) {
                node.curX = curX + temp;
                node.curY = curY + Constants.Y_STEP;
                temp += Constants.X_STEP;
                setCoordinates(node);
            }
        }
    }
    private void setCoordinates(Node n) {
        int temp = 0;
        n.passed = true;
        for (Node node : n.nodes) {
            if (node.passed == false) {
                node.curX = n.curX + temp;
                node.curY = n.curY + Constants.Y_STEP;
                temp += Constants.X_STEP;
                setCoordinates(node);
            }
        }
    }
    
    public String getCSV() {
        StringBuffer result = new StringBuffer();

        for(Integer child : childs) {
            result.append(child + ";");
        }

        return id + "," + age + "," + curX + "," + curY + "," + 
                    parent1 + "," + parent2 + "," + result;
    }
    @Override
    public int compareTo(Node o) {
        return age - o.age;
    }
    @Override
    public String toString() {
        StringBuffer c = new StringBuffer();

        for (Integer i : childs) {
            c.append(i + " - ");
        }

        return "p1: " + parent1 + " p2: " + parent2 + " c: " + c.toString(); 
    }
}
