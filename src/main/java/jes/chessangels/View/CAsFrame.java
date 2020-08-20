/*
 * Copyright (C) 2020 jes.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package jes.chessangels.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jes.chessangels.Controller.CommandHandler;
import jes.chessangels.GameModel.Game;

/**
 *
 * @author jes
 */
public class CAsFrame extends JFrame implements ActionListener {
    
    JPanel mainPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    
    HistPanel history;
    JButton endTurn = new JButton();
        
    Game g = new Game();
    
    BoardPanel bp;
    HandPanel hand1;
    HandPanel hand2;
    
    public CAsFrame(Game game) {
        super("Chess⨯ANGEL");
        
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(500 + 20, 860));
                
        history = new HistPanel(g);
        bp = new BoardPanel(this, g.gameboard);
        hand1 = new HandPanel(this, g, true, false);
        hand2 = new HandPanel(this, g, false, true);
        
        rightPanel.add(hand2);
        rightPanel.add(bp);
        rightPanel.add(hand1);
        rightPanel.setMinimumSize(new Dimension(500 + 20, 860));
        
        endTurn.setText("End Turn");
        endTurn.setBorder(BorderFactory.createEtchedBorder(0));
        endTurn.setBackground(Color.white);
        endTurn.setOpaque(true);
        endTurn.setMaximumSize(new Dimension(100, 50));
        endTurn.setToolTipText("End your turn!");
        endTurn.addActionListener(this);
        
        
        
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(history);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));        
        leftPanel.add(endTurn);
        
        fillmain(mainPanel, leftPanel, rightPanel);
        add(mainPanel);
        
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setResizable(false);
        setVisible(true);
        
        ch = new CommandHandler(this, g, bp, hand1, hand2, history, mainPanel);
    }
    
    private static void fillmain(JPanel mp, JPanel lp, JPanel rp) {
        mp.setLayout(new BoxLayout(mp, BoxLayout.X_AXIS));
        mp.add(lp);
        mp.add(rp);
        mp.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }
    
    public void fillfog() {
        mainPanel.removeAll();
        
        JButton nextTurn = new JButton();
        nextTurn.setText("<html>Start <i>" + g.turnplayer.toString() + "'s</i> Turn</html>");
        nextTurn.setBorder(BorderFactory.createEtchedBorder(0));
        nextTurn.setBackground(Color.white);
        nextTurn.setOpaque(true);
        nextTurn.setMaximumSize(new Dimension(150, 100));
        nextTurn.setPreferredSize(new Dimension(200, 100));
        nextTurn.setToolTipText("Begin the next turn!");
        nextTurn.addActionListener(this);
        nextTurn.setActionCommand("Next");
        nextTurn.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(nextTurn);
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    public void clearfog() {
        mainPanel.removeAll();

        fillmain(mainPanel, leftPanel, rightPanel);
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private final CommandHandler ch;
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        String ac = arg0.getActionCommand();
        ch.handle(ac);      
    }
}
