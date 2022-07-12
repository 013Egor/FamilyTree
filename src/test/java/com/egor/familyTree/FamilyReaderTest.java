package com.egor.familyTree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.egor.familyTree.io.FamilyReader;
import com.egor.familyTree.model.Node;
import com.egor.familyTree.model.Person;

public class FamilyReaderTest {
    @Test
    public void testGetFilename() {
        FamilyReader reader = new FamilyReader("filename");
        assertEquals("filename", reader.getFilename());
    }

    @Test
    public void testLoad() {
        FamilyReader incorectLength = new FamilyReader(Constants.SOURCE_TEST + "testIncorrect");
        FamilyReader corect = new FamilyReader(Constants.SOURCE_TEST + "testCorrect");
        assertThrows(IOException.class, () -> incorectLength.load());
    

        try {
            LinkedList<Person> list = corect.load();
            assertEquals(3, list.size());
        } catch (NumberFormatException | IOException e) {}
    }

    @Test
    public void testLoadNodes() {
        FamilyReader corect = new FamilyReader(Constants.SOURCE_TEST + "nodesCorrect");
        FamilyReader incorectLength = new FamilyReader(Constants.SOURCE_TEST + "nodesIncorrect");

        assertThrows(IOException.class, () -> incorectLength.load());

        try {
            LinkedList<Node> list = corect.loadNodes();
            assertEquals(9, list.size());
        } catch (NumberFormatException | IOException e) {}
    }

    @Test
    public void testSetFilename() {
        FamilyReader reader = new FamilyReader("filename");
        reader.setFilename("Hello");
        assertEquals("Hello", reader.getFilename());
    }
}
