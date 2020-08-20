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

import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import jes.chessangels.Controller.Liberties;
import jes.chessangels.GameModel.Board;

/**
 *
 * @author jes
 */
public class BoardPanel extends JPanel {

    ChessTile tiles[] = new ChessTile[25];
    
    public BoardPanel(ActionListener al, Board boardmodel) {
        setLayout(new GridLayout(5, 5));
        setPreferredSize(new Dimension(500 + 20, 500 + 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        draw(al, boardmodel);
    }
    
    public final void draw(ActionListener al, Board boardmodel) {
        removeAll();
        for(int i = 0; i < 25; i++) {
            tiles[i] = new ChessTile(al, boardmodel, (int)(i / 5), i % 5, false);
            add(tiles[i]);
        }
        updateUI();
    }
    
    public final void drawmarked(ActionListener al, Board boardmodel, Liberties ls) {
        removeAll();
        for(int i = 0; i < 25; i++) {
            int r = (int)(i / 5);
            int c = i % 5;
            tiles[i] = new ChessTile(al, boardmodel, r, c, ls.contains(r, c));
            add(tiles[i]);
        }
        updateUI();
    }
    
}
