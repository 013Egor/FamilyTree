package com.egor.familyTree.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;

import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.FamilyTree;
import com.egor.familyTree.model.Person;
import com.egor.familyTree.utils.ImageFilter;
import com.egor.familyTree.utils.Utils;



public class NewRelativeView extends JPanel {
    
    private JPanel panel = new JPanel(new GridLayout(12,2));
    private Person person = new Person();
    private JTextField firsName = new JTextField();
    private JTextField lastName = new JTextField();
    private JTextField middleName = new JTextField();
    private JTextField birthDate = new JTextField();
    private JTextField birthPlace = new JTextField();
    private JTextField weight = new JTextField();
    private JTextField height = new JTextField();


    private JComboBox<Person> firstParent;
    private JComboBox<Person> secondParent;
    private JComboBox<Person> spouse;
    private JButton button = new JButton("Выбрать фото");
    private JPanel biographyPanel = new JPanel(new FlowLayout());
    private JTextArea biography = new JTextArea();

    private FamilyTree tree;
    
    private Person[] persons;

    public NewRelativeView(FamilyTree tree) {
        this.tree = tree;
        this.persons = tree.getPersonsArray();
        
        panel.add(new JLabel("Имя: "));
        panel.add(firsName);

        panel.add(new JLabel("Фамилия: "));
        panel.add(lastName);

        panel.add(new JLabel("Отчество: "));
        panel.add(middleName);

        panel.add(new JLabel("Дата рождения: "));
        panel.add(birthDate);

        panel.add(new JLabel("Место рождения: "));
        panel.add(birthPlace);

        panel.add(new JLabel("Вес: "));
        panel.add(weight);

        panel.add(new JLabel("Рост: "));
        panel.add(height);

        panel.add(new JLabel("Родитель 1: "));
        firstParent = new JComboBox<Person>(persons);
        panel.add(firstParent);

        panel.add(new JLabel("Родитель 2: "));
        secondParent = new JComboBox<Person>(persons);
        panel.add(secondParent);

        panel.add(new JLabel("Супруг/супруга: "));
        spouse = new JComboBox<Person>(persons);
        panel.add(spouse);

        panel.add(new JLabel("Фото: "));
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser file = new JFileChooser();
                file.addChoosableFileFilter(new ImageFilter());
                file.setAcceptAllFileFilterUsed(false);
                int result = file.showOpenDialog(new JDialog());
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = file.getSelectedFile();
                    Utils.savePhoto(selectedFile, tree.getSaveFilename());
                    person.setPhoto(selectedFile.getAbsolutePath());
                }
            }
        });

        panel.add(button);
        
        panel.add(new JLabel("Биография: "));

        biography.setPreferredSize(new Dimension(Constants.BIO_WINDOW_WIDTH, 
                Constants.BIO_WINDOW_HEIGHT));

        biography.setBorder(new LineBorder(Color.BLACK, 1));

        biographyPanel.add(biography);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(panel);
        this.add(biographyPanel);
    }
    

    public int openDialog(String title) {

        int result = JOptionPane.showConfirmDialog(null, this, title, 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        return result;
    }

    public void setPerson(Person person) {
        
        this.person = person;

        firsName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        middleName.setText(person.getMiddleName());
        if (person.hasBirthDay()) {
            birthDate.setText(person.getBirthDay());
        }
        birthPlace.setText(person.getBirthPlace());
        if (person.getWeight() != Constants.EMPTY) {
            weight.setText(person.getWeight() + "");
        }
        if (person.getHeight() != Constants.EMPTY) {
            height.setText(person.getHeight() + "");
        }
        if (person.getParent1() == Constants.EMPTY) {
            firstParent.setSelectedItem(persons[0]);
        } else {
            firstParent.setSelectedItem(tree.findById(person.getParent1()));
        }
        if (person.getParent2() == Constants.EMPTY) {
            secondParent.setSelectedItem(persons[0]);
        } else {
            secondParent.setSelectedItem(tree.findById(person.getParent2()));
        }
        if (person.getSpouse() == Constants.EMPTY) {
            spouse.setSelectedItem(persons[0]);
        } else {
            spouse.setSelectedItem(tree.findById(person.getSpouse()));
        }
        biography.setText(person.getBio());
    }

    public Person getPerson() {

        Person temp;
        Person copyPerson = new Person(person);

        copyPerson.setFirstName(firsName.getText());
        copyPerson.setLastName(lastName.getText());
        copyPerson.setMiddleName(middleName.getText());
        if (birthDate.getText().equals("")) {
            copyPerson.setBirthDay("-1.-1.-1");    
        } else {
            copyPerson.setBirthDay(birthDate.getText());
        }
        copyPerson.setBirthPlace(birthPlace.getText());
        if (weight.getText().equals("")) {
            copyPerson.setWeight(Constants.EMPTY);    
        } else {
            copyPerson.setWeight(Double.parseDouble(weight.getText()));
        }
        if (height.getText().equals("")) {
            copyPerson.setHeight(Constants.EMPTY);
        } else {
            copyPerson.setHeight(Double.parseDouble(height.getText()));
        }
        temp = (Person) firstParent.getSelectedItem();
        copyPerson.setParent1(temp.getId());
        temp = (Person) secondParent.getSelectedItem();
        copyPerson.setParent2(temp.getId());
        temp = (Person) spouse.getSelectedItem();
        copyPerson.setSpouse(temp.getId());
        copyPerson.setBio(biography.getText());
        if (copyPerson.getParent1() == copyPerson.getParent2() && 
                copyPerson.hasParent()) {
                    
            throw new RuntimeException();
        }
        
        return copyPerson;
    }
}
