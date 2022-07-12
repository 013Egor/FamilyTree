package com.egor.familyTree.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.Node;
import com.egor.familyTree.model.Person;

public class FamilyReader {

    private String filename;

    public FamilyReader(String filename) {
        this.filename = filename;
    }

    private Person getPerson(String[] info) {
        Person person = new Person(info[1], info[2], info[3]);
        person.setId(Integer.parseInt(info[0]));
        if (info[4].equals("") == false) {
            person.setBirthDay(info[4]);
        }
        person.setBirthPlace(info[5]);
        person.setWeight(Double.parseDouble(info[6]));
        person.setHeight(Double.parseDouble(info[7]));
        person.setParent1(Integer.parseInt(info[8]));
        person.setParent2(Integer.parseInt(info[9]));
        person.setSpouse(Integer.parseInt(info[10]));
        person.setPhoto(Constants.PHOTO_DIR + filename + "/" + info[11]);
        person.curX = Integer.parseInt(info[12]);
        person.curY = Integer.parseInt(info[13]);

        return person;
    }

    public LinkedList<Person> load() throws IOException {

        LinkedList<Person> persons = new LinkedList<Person>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.SOURCE + filename))) {
            String line = reader.readLine();
            while (line != null) {
                String[] info = line.split("[,]");
                if (info.length != 14) {
                    throw new IOException();
                }
                Person person = getPerson(info);
                person.setBio(loadBiography(person.getId()));
                persons.add(person);
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new IOException(filename);
        }
        
        return persons;
    }

    private Node getNode(String[] info) throws NumberFormatException {
        Node node = new Node();

        node.setId(Integer.parseInt(info[0]));
        node.setAge(Integer.parseInt(info[1]));
        node.curX = Integer.parseInt(info[2]);
        node.curY = Integer.parseInt(info[3]);
        node.setParent1(Integer.parseInt(info[4]));
        node.setParent2(Integer.parseInt(info[5]));
        node.setChilds(info[6]);

        return node;
    }

    public LinkedList<Node> loadNodes() throws IOException {
        LinkedList<Node> nodes = new LinkedList<Node>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.SOURCE + filename + "_nodes"))) {
            String line;

            while((line = reader.readLine()) != null) {
                String[] info = line.split("[,]");
                if (info.length != 7) {
                    throw new IOException();
                }
                Node node = getNode(info);

                nodes.add(node);
            }
        } catch (IOException e) {
            throw new IOException(filename + "_node");
        }

        return nodes;
    }
    private String loadBiography(int id) {

        StringBuffer text = new StringBuffer();

        File dir = new File(Constants.BIO_DIR + filename);
        dir.mkdirs();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(dir.getCanonicalPath() + "/" + id))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line + "\n");
            }
        }  catch (IOException e) {
            System.out.println("Cannot load biography of " + id);
        }
        return text.toString();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
