package com.egor.familyTree.view;

import javax.swing.*;

import com.egor.familyTree.model.Constants;
import com.egor.familyTree.model.FamilyTree;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class RecoveryButton extends JComponent {

    private FamilyTree tree;
    private FamilyView view;

    private Image image;

    public RecoveryButton(FamilyTree tree, FamilyView view, int x, int y) {
      this.tree = tree;
      this.view = view;

      ImageIcon imageIcon = new ImageIcon(Constants.IMG_SOURCE + "/recovery.png");
      image = imageIcon.getImage().getScaledInstance(Constants.BUTTON_WIDTH, Constants.BUTTON_WIDTH, Image.SCALE_SMOOTH);
      
      setBounds(0, 0, Constants.BUTTON_WIDTH, Constants.BUTTON_WIDTH);
      setOpaque(true);

      setLocation(x, y);
      // Border raisedbevel = BorderFactory.createRaisedBevelBorder();
      // setBorder(raisedbevel);
      addMouseListener(createMouseListener());
    }
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      g.drawImage(image, 0, 0, this);
      Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(3));
      g2.setColor(Color.GRAY);
      g2.drawOval(0, 0, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);

      g2.setStroke(new BasicStroke(1));
    }

    

    private MouseListener createMouseListener() {
      MouseListener listener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            tree.fixNodes();
            view.clearComponents();
            view.setComponents();
            view.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
      };

      return listener;
    }

}
