package com.egor.familyTree.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.Node;
import com.egor.familyTree.model.Person;


public class FamilyWriter {
    
    String filename;

    public FamilyWriter(String filename) {
        this.filename = filename;
    }

    public void save(LinkedList<Person> persons) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                Constants.SOURCE + filename))) {

            for (Person person : persons) {
                writer.write(person.getCSV() + "\n");
                saveBiography(person);
            }
            writer.close();
        }
    }


    public void saveNodes(LinkedList<Node> nodes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
            Constants.SOURCE + filename + "_" + "nodes"))) {
            
            for (Node node : nodes) {
                writer.write(node.getCSV() + "\n");
            }

            writer.close();
        } 
    }

    private void saveBiography(Person person) {
        File dir = new File(Constants.BIO_DIR + filename);
        dir.mkdirs();
        
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(dir.getCanonicalPath() + "/" + person.getId()))) {

            writer.write(person.getBio());
            writer.close();
            
        } catch (IOException e) {
           System.out.println("Cannot save biography of " + person.getId());
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
