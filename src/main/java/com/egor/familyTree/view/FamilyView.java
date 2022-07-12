package com.egor.familyTree.view;

import javax.swing.*;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.FamilyTree;
import com.egor.familyTree.model.Node;
import com.egor.familyTree.model.Person;
import com.egor.familyTree.utils.TreeListener;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Line2D;

import java.util.LinkedList;
import java.util.NoSuchElementException;



public class FamilyView extends JPanel {

  private FamilyTree tree;

  private Color green = new Color(12, 174, 91);
  private Color backgroundColor = new Color(166, 123, 81);
  private Color branchColor = new Color(97,59,22);
  private Color bloodLine = new Color(255, 87, 51);

  private LinkedList<PersonComponent> personComponents = new LinkedList<PersonComponent>();
  private LinkedList<NodeComponent> nodeComponents = new LinkedList<NodeComponent>();

  private TreeListener treeListener;

  public FamilyView(FamilyTree familyTree) {
    
    tree = familyTree;
      
    setBackground(backgroundColor);
    setLayout(null);

    setComponents();
    RecoveryButton recoveryButton = new RecoveryButton(tree, this, 10, 10);
    add(recoveryButton);
    
    treeListener = new TreeListener(this);
    
    addVetoableChangeListener(treeListener);
  }

  public void paintComponent(Graphics g) {

      super.paintComponent(g);
      
      for (Node node : tree.getNodes()) {
        drawchildsColors(g, node);
        drawParentsLines(g, node);
      }

      drawSpouseLines(g);
  }

  public void clearComponents() {
    personComponents.stream().forEach(x -> remove(x));
    personComponents.clear();
    nodeComponents.stream().forEach(x -> remove(x));
    nodeComponents.clear();
    
    repaint();
  }

  public void setComponents() {

    int[] furtherPoint = tree.getFurtherPoint();
    setPreferredSize(new Dimension(furtherPoint[0] + Constants.ICON_WIDTH, 
        furtherPoint[1] + Constants.ICON_HEIGHT));

    addPersons();
    addNodes();
  }

  private void drawLine(Graphics g, Person person, Node node, Color color) {

    int personX = person.curX + Constants.ICON_WIDTH / 2;
    int personY = person.curY + Constants.ICON_HEIGHT / 2;
    int nodeX = node.curX + Constants.NODE_WIDTH / 2;
    int nodeY = node.curY + Constants.NODE_HEIGHT / 2;
    
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(branchColor);
    g2.setStroke(new BasicStroke(Constants.BRANCH_WIDTH));

    g2.draw(new Line2D.Float(personX, personY, nodeX, nodeY));
    g2.setColor(color);
    
    g2.setStroke(new BasicStroke(Constants.LINE_WIDTH));
    g2.draw(new Line2D.Float(personX, personY, nodeX, nodeY));

    g2.setStroke(new BasicStroke(1));
  }

  private void drawchildsColors(Graphics g, Node node) {

    Person temp;
    
    if ((node.getParent1() == Constants.EMPTY && 
            node.getParent2() == Constants.EMPTY) == false) {

      for (Integer id : node.getChilds()) {
        try {
          temp = tree.findById(id);

          drawLine(g, temp, node, green);
        } catch (NoSuchElementException e) {}
      }
    }
  }

  private void drawParentsLines(Graphics g, Node node) {

    Person temp;
    try {
      temp = tree.findById(node.getParent1());
      drawLine(g, temp, node, bloodLine);
    } catch (NoSuchElementException e) {}

    try {
      temp = tree.findById(node.getParent2());
      drawLine(g, temp, node, bloodLine);
    } catch (NoSuchElementException e) {}
  }

  private void drawSpouseLines(Graphics g) {
    Person spouse;
    int personOneX = 0, personOneY = 0, personTwoX = 0, personTwoY = 0;
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.CYAN);
    g2.setStroke(new BasicStroke(5));
    

    for (Person person : tree.getPersons()) {
      if (person.getSpouse() != Constants.EMPTY) {

        spouse = tree.findById(person.getSpouse());

        personOneX = spouse.curX + Constants.ICON_WIDTH / 2;
        personOneY = spouse.curY + Constants.ICON_HEIGHT / 2;
        personTwoX = person.curX + Constants.ICON_WIDTH / 2;
        personTwoY = person.curY + Constants.ICON_HEIGHT / 2;

        g2.draw(new Line2D.Float(personOneX, personOneY, personTwoX, personTwoY));
      }
    }

    g2.setStroke(new BasicStroke(1));
  }

  private void addPersons() {
    LinkedList<Person> all = tree.getPersons();
    PersonComponent component;
    for (Person person : all) {
      component = new PersonComponent(person, tree, treeListener);
      component.addComponentListener(new ComponentAdapter() {
      
        @Override
        public void componentMoved(ComponentEvent e) {
          repaint();
        }

      });
      personComponents.add(component);
      add(component);
    }

    repaint();
  }

  private void addNodes() {
    LinkedList<Node> nodes = tree.getNodes();
    NodeComponent nodeComponent;
    
    for (Node node : nodes) {
      if ((node.getParent1() == Constants.EMPTY && 
            node.getParent2() == Constants.EMPTY) == false) {

        nodeComponent = new NodeComponent(node, tree, node.curX, node.curY);

        nodeComponent.addComponentListener(new ComponentAdapter() {
        
          @Override
          public void componentMoved(ComponentEvent e) {
            repaint();
          }
        });

        nodeComponents.add(nodeComponent);
        add(nodeComponent);
      }
    }

    repaint();
  }
};