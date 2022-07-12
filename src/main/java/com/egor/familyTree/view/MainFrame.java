package com.egor.familyTree.view;

import javax.swing.*;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.FamilyTree;
import com.egor.familyTree.utils.MenuCreator;


public class MainFrame extends JFrame {

    FamilyTree tree;
    
    FamilyView familyView;

    public JMenu createMenu(String name) {

        JMenu menu = new JMenu(name);

        menu.add(MenuCreator.createLoadButton(tree, familyView));
        menu.add(new JSeparator());

        menu.add(MenuCreator.createSaveButton(tree));
        menu.add(MenuCreator.createSaveAsButton(tree));
        menu.add(new JSeparator());

        menu.add(MenuCreator.createAddButton(tree, familyView));

        return menu;
    }


    public MainFrame(String title) {
        super(title);
        
        tree = new FamilyTree();

        familyView = new FamilyView(tree, this);
        
        JScrollPane mainPanel = new JScrollPane(familyView);
        mainPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(mainPanel);
        
        JMenuBar menu = new JMenuBar();
        menu.add(createMenu("Меню"));
        
        this.setJMenuBar(menu);
        
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}   