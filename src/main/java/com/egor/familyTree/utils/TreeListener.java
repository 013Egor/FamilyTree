package com.egor.familyTree.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import com.egor.familyTree.view.FamilyView;



public class TreeListener implements  VetoableChangeListener {

    FamilyView view;
    
    public TreeListener(FamilyView view) {
        this.view = view;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        view.clearComponents();
        view.setComponents();
    }

    
}
