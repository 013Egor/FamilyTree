package com.egor.familyTree.view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.FamilyTree;
import com.egor.familyTree.model.Node;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.NoSuchElementException;

public class NodeComponent extends JComponent {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    private Node node;
    private FamilyTree tree;

    public NodeComponent(Node node, FamilyTree tree, int x, int y) {
      this.node = node;
      this.tree = tree;
      
      setBorder(createBorder());
      
      setBounds(0, 0, Constants.NODE_WIDTH, Constants.NODE_HEIGHT);
      setOpaque(true);

      setLocation(x, y);
      node.curX = x;
      node.curY = y;
      
      addMouseListener(createMouseListener());
      addMouseMotionListener(createMouseMotionListener()); 
    }
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      g.setColor(Color.BLUE);
      g.fillOval(0, 0, Constants.NODE_WIDTH, Constants.NODE_HEIGHT);  
    }

    private AbstractBorder createBorder() {
      AbstractBorder border = new AbstractBorder() {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
          g.setColor(new Color(166, 123, 81));
          g.fillOval(x, y, width, height);
          Graphics2D g2 = (Graphics2D) g;
          g2.setStroke(new BasicStroke(3));
          g.setColor(new Color(80, 40, 0));
          g2.drawOval(x, y, width, height);
          g2.setStroke(new BasicStroke(1));
        }
      };

      return border;
    }

    private MouseListener createMouseListener() {
      MouseListener listener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
          StringBuffer str = new StringBuffer();
          str.append("<html><h1>Дети:</h1><ul>");
          for (Integer id : node.getChilds()) {
            try {
              str.append("<li>" + tree.findById(id).getFullName() + "</li>");
            } catch (NoSuchElementException exc) {}
          }
          str.append("</ul><hr><h1>Родители:</h1>");
          try {
            str.append("<li>" + tree.findById(node.getParent1()).getFullName() + "</li>");
          } catch (NoSuchElementException exc) {}
          try {
            str.append("<li>" + tree.findById(node.getParent2()).getFullName() + "</li></ul>");
          } catch (NoSuchElementException exc) {}
          JOptionPane.showMessageDialog(null, str, "Информация узла", JOptionPane.PLAIN_MESSAGE);
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

          node.curX = myX + deltaX;
          node.curY = myY + deltaY;
          setLocation(node.curX, node.curY);
        }

        @Override
        public void mouseMoved(MouseEvent e) { }

      };
      
      return listener;
    }
}
