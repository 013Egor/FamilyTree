package com.egor.familyTree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.egor.familyTree.model.FamilyTree;
import com.egor.familyTree.model.Person;

public class FamilyTreeTest {

    FamilyTree tree;
    Person p1;
    Person p2;
    Person c1;
    Person c2;

    @BeforeEach
    public void init() {

        tree = new FamilyTree();
        p1 = new Person();
        p2 = new Person();
        c1 = new Person();
        c2 = new Person();

        tree.addNew(p1);
        tree.addNew(p2);
        
        c1.setParent1(p1.getId());
        c1.setParent2(p2.getId());

        c2.setParent1(p1.getId());
        c2.setParent2(p2.getId());

        tree.addNew(c1);
        tree.addNew(c2);
    }

    @Test
    public void testAddNew() {
        

        assertEquals(4, tree.getPersons().size());
        assertEquals(3, tree.getNodes().size());
    }

    @Test
    public void testClear() {

        assertEquals(4, tree.getPersons().size());
        assertEquals(3, tree.getNodes().size());

        tree.clear();
    
        assertEquals(0, tree.getPersons().size());
        assertEquals(0, tree.getNodes().size());

    }

    @Test
    public void testFindById() {
        assertEquals(c1, tree.findById(c1.getId()));

        assertThrows(NoSuchElementException.class, () -> tree.findById(100));
    }

    @Test
    public void testFixNodes() {
        assertEquals(4, tree.getPersons().size());
        assertEquals(3, tree.getNodes().size());

        c1.setParent1(-1);
        c1.setParent2(-1);
        tree.fixNodes();
    
        assertEquals(4, tree.getPersons().size());
        assertEquals(4, tree.getNodes().size());
    }

    @Test
    public void testGetFurtherPoint() {
        p1.curX = 12;
        p1.curY = 12;

        p2.curX = 0;
        p2.curY = 13;

        c1.curX = 2;
        c1.curY = 22;
        
        c2.curX = 32;
        c2.curY = 12;

        int[] result = tree.getFurtherPoint();

        assertEquals(32, result[0]);
        assertEquals(22, result[1]);
    }

    @Test
    public void testGetPersonsArray() {
        Person[] array = tree.getPersonsArray();

        assertEquals(array.length, tree.getPersons().size() + 1);
    }

    @Test
    public void testRemove() {
        assertEquals(p1, tree.findById(p1.getId()));
        tree.remove(p1);

        assertThrows(NoSuchElementException.class, () -> tree.findById(p1.getId()));
    }

    @Test
    public void testFixFamilyLinks() {
        Person temp = new Person();
        tree.addNew(temp);

        assertEquals(4, tree.getNodes().size());
        assertEquals(5, tree.getPersons().size());

        Person change = new Person(c2);
        change.setParent2(temp.getId());

        tree.fixFamilyLinks(c2, change);

        assertEquals(5, tree.getNodes().size());
    }
}
