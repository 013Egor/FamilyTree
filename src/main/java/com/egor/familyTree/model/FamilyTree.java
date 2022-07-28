package com.egor.familyTree.model;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.egor.familyTree.io.FamilyReader;
import com.egor.familyTree.io.FamilyWriter;

@Entity
@Table(name = "familytree")
public class FamilyTree {

    @GenericGenerator(name = "tree_generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "tree_generator")
    @Column(name = "id")
    private Integer id;

    @Transient
    private volatile LinkedList<Person> persons;
    @Transient
    private volatile LinkedList<Node> nodes;
    
    @Column(name = "lastid")
    private int lastID;

    @Column(name = "filename")
    private String filename;

    @Transient
    private FamilyReader reader;
    @Transient
    private FamilyWriter writer;

    public FamilyTree() {

        persons = new LinkedList<Person>();
        nodes = new LinkedList<Node>();
        
        reader = new FamilyReader("filename");
        writer = new FamilyWriter("filename");
        filename =  "filename";
        lastID = 0;
    }

    public int addNew(Person person) {

        lastID++;
        person.setId(lastID);
        
        persons.add(person);
        boolean inNode = false;

        for (Node item : nodes) {
            if (item.isSameParents(person)) {
                item.add(person);
                inNode = true;
                int age = item.getAge() > person.getYear() ? item.getAge() : person.getYear();
                
                item.setAge(age);
                
                break;
            }
        }

        if (inNode == false) {
            Node node = new Node(person);
            node.setId(nodes.size());
            node.setAge(person.getYear());
            
            nodes.add(node);
        }

        return persons.size();
    }

    private void setNodes() {
        for (Person person : persons) {

            person.curX = Constants.EMPTY;
            person.curY = Constants.EMPTY;

            boolean inNode = false;
            
            for (Node item : nodes) {
                if (item.isSameParents(person)) {
                    item.add(person);
                    inNode = true;
                    int age = item.getAge() > person.getYear() ? item.getAge() : person.getYear();
                    item.setAge(age);
                    break;
                }
            }
        
            if (inNode == false) {
                Node node = new Node(person);
                node.setId(nodes.size());
                node.setAge(person.getYear());
                nodes.add(node);
            }
        }
    }
    
    private void setGraph() {
        for (Node node : nodes) {
            node.passed = false;
            for (Node subNode : nodes) {
                if (subNode.equals(node) == false) {
                    if (node.childIsParent(subNode)) {
                        node.add(subNode);
                    }
                }
            }
        }
    }
    
    private void setNodesCoordinates() {
        nodes.sort(Node::compareTo);
        int step = 200;
        for (Node item : nodes) {
            if (item.getParent1() == Constants.EMPTY && 
                    item.getParent2() == Constants.EMPTY && item.passed == false) {

                item.setCoordinates(step, 0);
                step += Constants.X_STEP;
            } 
        }
    }
    
    private void setPersonsCoordinates() {
        for (Node node : nodes) {
            if (node.getParent1() != Constants.EMPTY) {
                Person parent = findById(node.getParent1()); 
                
                parent.curX = node.curX - Constants.ICON_WIDTH;
                parent.curY = node.curY - Constants.ICON_HEIGHT;
                
            }
            if (node.getParent2() != Constants.EMPTY) {
                Person parent = findById(node.getParent2());
                
                parent.curX = node.curX + Constants.NODE_WIDTH;
                parent.curY = node.curY - Constants.ICON_HEIGHT;
           }
            
            Person tempPerson;
            int t = (node.getChilds().size() * Constants.ICON_WIDTH) / 2;
            for (Integer id : node.getChilds()) {
                tempPerson = findById(id);
                if (tempPerson.curX == Constants.EMPTY && tempPerson.curY == Constants.EMPTY) {
                    tempPerson.curX = node.curX - t + Constants.NODE_WIDTH / 2;
                    tempPerson.curY = node.curY + Constants.ICON_HEIGHT;  
                } 
                t -= Constants.ICON_WIDTH;
            }
        }
    }
    
    public void fixNodes() {
        
        nodes.clear();

        setNodes();
        setGraph();

        setNodesCoordinates();
        setPersonsCoordinates();
    }

    public boolean fixFamilyLinks(Person curPerson, Person changedPerson) {
        boolean inNodes = false;
        for (Node node : nodes) {
            if (node.isSameParents(curPerson)) {
                node.removeChild(curPerson.getId());
            } else if (node.isSameParents(changedPerson)) {
                node.add(changedPerson);
                inNodes = true;
            }
        }

        if (inNodes == false) {
            Node node = new Node(changedPerson);
            nodes.add(node);
        }

        return inNodes;
    }

    public void remove(Person person) {
        persons.remove(person);

        for (Node item : nodes) {
            if (item.isSameParents(person)) {
                item.removeChild(person.getId());
            } 
            if (item.isParent(person.getId())) {
                item.removeParent(person);
                Person child;
                for (Integer id : item.getChilds()) {
                    child = findById(id);
                    if (child.getParent1() == person.getId()) {
                        child.setParent1(Constants.EMPTY);
                    } else {
                        child.setParent2(Constants.EMPTY);
                    }
                }
            }
        }

        for (Person human : persons) {
            if (human.getSpouse() == person.getId()) {
                human.setSpouse(Constants.EMPTY);
                break;
            }
        }

        File bio = new File(Constants.BIO_DIR + writer.getFilename() + "/" + person.getId());
        if (bio.exists()) {
            bio.delete();
        }

        File photo = new File(person.getPhoto());
        if (photo.exists()) {
            photo.delete();
        }
    }

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(LinkedList<Node> nodes) {
        this.nodes = nodes;
    }

    public LinkedList<Person> getPersons() {
        return persons;
    }

    public Person findById(int id) throws NoSuchElementException {
        for (Person item : persons) {
            if (item.getId() == id) {
                return item;
            }
        }

        throw new NoSuchElementException();
    }

    public int[] getFurtherPoint() {
        int[] point = {0, 0};
        for (Person person : persons) {
            point[0] = point[0] >= person.curX ? point[0] : person.curX;
            point[1] = point[1] >= person.curY ? point[1] : person.curY;
        }

        return point;
    }
    
    public void setSavingFilename(String filename) {
        writer.setFilename(filename);
        this.filename = filename;
    }

    public String getSaveFilename() {
        return writer.getFilename();
    }

    public Person[] getPersonsArray() {
        Person[] list = new Person[persons.size() + 1];
        int i = 0;
        list[i++] = new Person("Нет", "", "");
        list[0].setId(Constants.EMPTY);
        for (Person person : persons) {
            list[i++] = person;
        }
        return list;
    }

    public void clear() {
        persons.clear();
        nodes.clear();
    }

    public void load(String filename) throws IOException {
        persons.clear();
        nodes.clear();

        reader.setFilename(filename);

        persons = reader.load();
        for (Person person : persons) {
            lastID = lastID > person.getId() ? lastID : person.getId();
        }
        nodes = reader.loadNodes();
    }

    public boolean save() {

        try {
            writer.save(persons);
            writer.saveNodes(nodes);
        } catch (IOException e) {
            return false;
        }

        return true;
    }    

    public void setNewId(int id) {
        this.id = id;
        for (Person person : persons) {
            person.setTreeId(id);
        }
        for (Node node : nodes) {
            node.setTreeId(id);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLastID() {
        return lastID;
    }

    public void setLastID(int lastID) {
        this.lastID = lastID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setPersons(LinkedList<Person> persons) {
        this.persons = persons;
    }

    public FamilyReader getReader() {
        return reader;
    }

    public void setReader(FamilyReader reader) {
        this.reader = reader;
    }

    public FamilyWriter getWriter() {
        return writer;
    }

    public void setWriter(FamilyWriter writer) {
        this.writer = writer;
    }
}
