package com.egor.familyTree.view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.FamilyTree;
import com.egor.familyTree.model.Person;
import com.egor.familyTree.utils.TreeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.awt.*;

public class PersonInfo extends JSplitPane {

    private Person person;
    private FamilyTree tree;
    private volatile Image image;
    private TreeListener listener;
    private PersonInfo curPanel;


    private JLabel name;
    private JLabel birthInfo;
    private JLabel antropometriya;
    private JLabel bio;
    private JLabel imageLabel;

    public PersonInfo(int newOrientation, PersonComponent source) {
        super(newOrientation);

        this.curPanel = this;
        this.person = source.getPerson();
        this.image = source.getImage();
        this.tree = source.getTree();
        this.listener = source.getTreeListener();
        
        
        this.setLeftComponent(createLeftPanel());
        this.setRightComponent(createRightPanel());
    }

    private JPanel createLeftPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());        
        imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(200,200, Image.SCALE_SMOOTH)));
                
        JPanel buttoPanel = new JPanel(new FlowLayout());
        buttoPanel.add(createDeleteButton());
        buttoPanel.add(createChangeButton());
        
        panel.add("South",buttoPanel);
        panel.add("Center", imageLabel);

        return panel;
    }

    private JButton createChangeButton() {
        JButton changeButton = new JButton("Изменить");

        changeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                NewRelativeView relativeView = new NewRelativeView(tree);
                relativeView.setPerson(person);
                int result = relativeView.openDialog("Изменение данных");
                try {
                    if (result == JOptionPane.OK_OPTION) {
                        Person copyPerson = relativeView.getPerson();
                        
                        checkSpouse(person, copyPerson, tree);
                        if (person.isSameParent(copyPerson) == false) {
                            tree.fixFamilyLinks(person, copyPerson);
                        }
                        person.setPerson(copyPerson);
                        
                        listener.vetoableChange(new PropertyChangeEvent(this, "", 
                                tree, tree));

                        repaint();
                    }
                } catch (RuntimeException | PropertyVetoException exception) { 
                    if (exception instanceof RuntimeException) {
                        JOptionPane.showMessageDialog(null, 
                            "Вы ввели некорректные данные");

                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "Произошла ошибка при обновлении дерева");
                    }
                } 
                updateWindow();
            }     
        });

        changeButton.setSize(100, 50);

        return changeButton;
    }

    private JButton createDeleteButton() {
        JButton deleteButton = new JButton("Удалить");
    
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                    tree.remove(person);
                    try {
                        listener.vetoableChange(new PropertyChangeEvent(this, "", 
                            tree, tree));

                    } catch (PropertyVetoException e1) {
                        e1.printStackTrace();
                    }
                    
                    curPanel.setVisible(false);
                }     
        });

        deleteButton.setSize(100, 50);

        return deleteButton;
    }

    private void updateWindow() {
        name.setText(person.getFullName());
        birthInfo.setText(person.getBirthInfo());
        antropometriya.setText(person.getBodyParaments());
        bio.setText(person.getHtmlBio());
        ImageIcon imageIcon = new ImageIcon(person.getPhoto());
        Image image = imageIcon.getImage();
        image = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));
    }

    private void checkSpouse(Person person, Person copyPerson, FamilyTree tree) {
        if (copyPerson.getSpouse() != -1) {
            Person temp = tree.findById(copyPerson.getSpouse());
            if (temp.getSpouse() != -1 && copyPerson.getSpouse() != person.getSpouse()) {
                int resultChange =  JOptionPane.showConfirmDialog(null, temp + " имеет другого супруга/супругу. Внести изменения?");
                if (resultChange == JOptionPane.OK_OPTION) {
                    tree.findById(temp.getSpouse()).setSpouse(-1);
                    if (person.getSpouse() != -1) {
                        tree.findById(person.getSpouse()).setSpouse(-1);
                    }
                    
                    temp.setSpouse(person.getId());
                } else {
                    person.setSpouse(-1);
                }
            } else {
                temp.setSpouse(person.getId());
            }
        } 
    }

    private JPanel createRightPanel() {

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        name = new JLabel(person.getFullName());
        name.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        name.setOpaque(true);
        name.setBackground(Color.WHITE);
        
        birthInfo = new JLabel(person.getBirthInfo());
        birthInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        birthInfo.setOpaque(true);
        birthInfo.setBackground(Color.WHITE);

        antropometriya = new JLabel(person.getBodyParaments());
        antropometriya.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        antropometriya.setOpaque(true);
        antropometriya.setBackground(Color.WHITE);

        bio = new JLabel(person.getHtmlBio());
        bio.setAlignmentY(TOP_ALIGNMENT);
        bio.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bio.setOpaque(true);
        bio.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(bio);
        scrollPane.setPreferredSize(new Dimension(Constants.BIO_WINDOW_WIDTH, 
            Constants.BIO_WINDOW_HEIGHT));
            
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel shortInfo = new JPanel(new GridLayout(4,2));
        shortInfo.add(new JLabel("Имя: "));
        shortInfo.add(name);
        shortInfo.add(new JLabel("Дата рождения/Место: "));
        shortInfo.add(birthInfo);
        
        shortInfo.add(new JLabel("Вес/Рост: "));
        shortInfo.add(antropometriya);
        shortInfo.add(new JLabel("Биография:"));  
        rightPanel.add(shortInfo);
        rightPanel.add(scrollPane);

        return rightPanel;
    }
}
