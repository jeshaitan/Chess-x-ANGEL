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

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import jes.chessangels.GameModel.Card;
import jes.chessangels.GameModel.Game;

/**
 *
 * @author jes
 */
public class HandPanel extends JPanel {
    
    public HandTile hand[];
    public boolean iswhite;
    public boolean isfar;
    
    public HandPanel(ActionListener al, Game game, boolean isw, boolean isf) {
        iswhite = isw;
        isfar = isf;
        drawcards(al, game);
    }
    
    public final void drawcards(ActionListener al, Game game) {
        this.removeAll();
        
        ArrayList<Card> handmodel;
        
        if(iswhite) {
            handmodel = game.player1.hand;
        } else {
            handmodel = game.player2.hand;
        }
        
        hand = new HandTile[handmodel.size()];
        setLayout(new GridLayout(1, handmodel.size()));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
        for(int i = 0; i < handmodel.size(); i++) {
            hand[i] = new HandTile(al, handmodel.get(i), iswhite, i, isfar);
            add(hand[i]);
        }
        
        updateUI();
    }
    
    public void togglehand() {
        iswhite = !iswhite;
    }
    
}
