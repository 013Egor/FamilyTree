package com.egor.familyTree.utils;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.egor.familyTree.database.FamilyTreeDao;
import com.egor.familyTree.database.NodeDao;
import com.egor.familyTree.database.PersonDao;
import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.FamilyTree;
import com.egor.familyTree.model.Person;
import com.egor.familyTree.view.FamilyView;
import com.egor.familyTree.view.NewRelativeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

public class MenuCreator {

    public static JMenuItem createLoadButton(FamilyTree tree, FamilyView view) {
        JMenuItem item = new JMenuItem("Загрузить");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(Constants.SOURCE));
                int result = fileChooser.showOpenDialog(new JDialog());
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    tree.clear();
                    try {
                        tree.load(selectedFile.getName());
                        tree.setSavingFilename(selectedFile.getName());
                    } catch (IOException exc) {
                        JOptionPane.showMessageDialog(null, "<html>Неудалось открыть файл.<br>Проверьте файл " + exc.getMessage() , "Ошибка", JOptionPane.PLAIN_MESSAGE);
                        tree.clear();
                        tree.setSavingFilename("filename");
                    }
                    view.clearComponents();
                    view.setComponents();
                }        
            }
            
        });

        return item;
    }

    public static JMenuItem createSaveButton(FamilyTree tree) {
        JMenuItem item = new JMenuItem("Cохранить");

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = tree.save();
                if (result == true) {
                    JOptionPane.showMessageDialog(null, "Данные успешно сохранены в " + tree.getSaveFilename());
                } else {
                    JOptionPane.showMessageDialog(null, "Произошла ошибка при сохранении");
                }
            }
            
        });

        return item;
    }

    public static JMenuItem createSaveAsButton(FamilyTree tree) {
        JMenuItem item = new JMenuItem("Cохранить как");

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(Constants.SOURCE));
                int result = fileChooser.showOpenDialog(new JDialog());
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Utils.movePhotos(tree.getPersons(), selectedFile.getName());
                    tree.setSavingFilename(selectedFile.getName());
                    boolean savingResult = tree.save();
                    if (savingResult == true) {
                        JOptionPane.showMessageDialog(null, "Данные успешно сохранены в " + tree.getSaveFilename());
                    } else {
                        JOptionPane.showMessageDialog(null, "Произошла ошибка при сохранении");
                    }
                }     
            }
            
        });

        return item;
    }

    public static JMenuItem createSaveToDatabase(FamilyTree tree) {
        JMenuItem item = new JMenuItem("Cохранить в базу данных");

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    FamilyTreeDao treeDao = new FamilyTreeDao();

                    boolean savingResult = treeDao.saveTree(tree);
                    
                    tree.setNewId(tree.getId());

                    PersonDao personDao = new PersonDao();
                    NodeDao nodeDao = new NodeDao();

                    personDao.saveAll(tree.getPersons());
                    nodeDao.saveAll(tree.getNodes());
                    
                    if (savingResult == true) {
                        JOptionPane.showMessageDialog(null, "Данные успешно сохранены в базу данных");
                    } else {
                        JOptionPane.showMessageDialog(null, "Произошла ошибка при сохранении");
                    }    
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Произошла ошибка при сохранении");
                }
            }
            
        });

        return item;
    }

    public static JMenuItem createAddButton(FamilyTree tree, FamilyView view) {
        JMenuItem item = new JMenuItem("Добавить родственника");

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                    
                    NewRelativeView relativeView = new NewRelativeView(tree);

                    int result = relativeView.openDialog("Добавление родственника");

                    try {
                        if (result == JOptionPane.OK_OPTION) {
                            Person person = relativeView.getPerson();
                            tree.addNew(person);
                            
                            checkSpouse(person, tree);
                            tree.fixNodes();
                            view.clearComponents();
                            view.setComponents();
                        }
                    } catch (NumberFormatException exception) { } 
                }     
        });

        return item;
    }

    private static void checkSpouse(Person person, FamilyTree tree) {
        try {
            if (person.getSpouse() != Constants.EMPTY) {
                Person temp = tree.findById(person.getSpouse());
                if (temp.getSpouse() != Constants.EMPTY) {
                    int resultChange =  JOptionPane.showConfirmDialog(null, temp + " имеет другого супруга/супругу. Внести изменения?");
                    if (resultChange == JOptionPane.OK_OPTION) {
                        tree.findById(temp.getSpouse()).setSpouse(Constants.EMPTY);
                        temp.setSpouse(person.getId());
                    } else {
                        person.setSpouse(Constants.EMPTY);
                    }
                } else {
                    temp.setSpouse(person.getId());
                }
            }
        } catch (NoSuchElementException exception) {
            person.setSpouse(Constants.EMPTY);
        }
    }
}