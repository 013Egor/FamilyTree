package com.egor.familyTree.view;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.FamilyTree;
import com.egor.familyTree.model.Person;
import com.egor.familyTree.utils.TreeListener;

import java.awt.*;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PersonComponent extends JComponent {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    private Font textFont = new Font(Font.DIALOG, Font.ROMAN_BASELINE|Font.BOLD, 12);
    
    private volatile Image image;
    private Person person;
    private FamilyTree tree;
    private TreeListener treeListener;

    public PersonComponent(Person person, FamilyTree tree, TreeListener listener) {

      this.person = person;
      this.tree = tree;
      this.treeListener = listener;

      ImageIcon imageIcon = new ImageIcon(person.getPhoto());
      image = imageIcon.getImage().getScaledInstance(Constants.ICON_WIDTH, Constants.ICON_HEIGHT, Image.SCALE_SMOOTH);
      
      setBorder(new LineBorder(new Color(12, 174, 91), 3));
      setBounds(0, 0, Constants.ICON_WIDTH, Constants.ICON_HEIGHT);
      setOpaque(true);

      setBackground(Color.WHITE);
      setName(person.getFirstName());
      setLocation(person.curX, person.curY);

      addMouseListener(createMouseListener());
      addMouseMotionListener(createMouseMotionListener());
    }
    

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      g.setColor(Color.gray);
      g.fillRect(0, 0, Constants.ICON_WIDTH, Constants.ICON_HEIGHT);
      g.setColor(Color.BLACK);
      g.drawImage(image, 0, 0, this);
      
      g.clearRect(0, 80, Constants.ICON_WIDTH, 20);
      g.drawLine(0, 80, Constants.ICON_WIDTH, 80);
      g.setFont(textFont);
      g.drawString(person.getShortName(), 5, 93);
    }

    
    private MouseListener createMouseListener() {
      PersonComponent thisComponent = this;
      MouseListener listener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
          PersonInfo window = new PersonInfo(JSplitPane.HORIZONTAL_SPLIT, thisComponent);

          JOptionPane.showMessageDialog(null, window, person.getFirstName(), JOptionPane.PLAIN_MESSAGE);
        }

        @Override
        public void mousePressed(MouseEvent e) {
          screenX = e.getXOnScreen();
          screenY = e.getYOnScreen();

          myX = getX();
          myY = getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }

      };

      return listener;
    }

    private MouseMotionListener createMouseMotionListener() {
      MouseMotionListener listener = new MouseMotionListener() {

        @Override
        public void mouseDragged(MouseEvent e) {
          int deltaX = e.getXOnScreen() - screenX;
          int deltaY = e.getYOnScreen() - screenY;

          person.curX = myX + deltaX;
          person.curY = myY + deltaY;
          setLocation(person.curX, person.curY);
        }

        @Override
        public void mouseMoved(MouseEvent e) { }

      };

      return listener;
    }

    public Image getImage() {
      return image;
    }


    public void setImage(Image image) {
      this.image = image;
    }


    public Person getPerson() {
      return person;
    }


    public void setPerson(Person person) {
      this.person = person;
    }


    public FamilyTree getTree() {
      return tree;
    }


    public void setTree(FamilyTree tree) {
      this.tree = tree;
    }


    public TreeListener getTreeListener() {
      return treeListener;
    }


    public void setTreeListener(TreeListener treeListener) {
      this.treeListener = treeListener;
    }
  }