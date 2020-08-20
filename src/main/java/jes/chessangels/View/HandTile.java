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
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import jes.chessangels.GameModel.Card;

/**
 *
 * @author jes
 */
public class HandTile extends JButton {
    
    public HandTile(ActionListener al, Card crd, boolean iswhite, int handind, boolean isinfarhand) {

        setBorder(new TitledBorder(new LineBorder(new Color(169, 169, 169)),
                    "Card", TitledBorder.LEADING, TitledBorder.TOP,
                    null, null));
        setBackground(Color.white);
        setOpaque(true);

        if(!isinfarhand) {
            addActionListener(al);
            setActionCommand("Card:" + handind);

            drawcard(crd, iswhite);
            setToolTipText(crd.desc + " Costs " + crd.cost + " willpower to land.");
        } else {
            addActionListener(al);
            setActionCommand("Opp:" + handind);
            
            drawcard();
            setToolTipText("A card in your opponent's hand!");
        }
    }
    
    public final void drawcard(Card c, boolean iswhite) {
        String icon;
        
        Color iconcolor = Color.BLACK;
        
        switch(c.name) {
            case "King":
                if(iswhite) icon = "♔"; 
                else icon = "♚";
                break;
            case "Pawn":
                if(iswhite) icon = "♙"; 
                else icon = "♟︎";
                setToolTipText("Activate a pawn.");
                break;
            case "Queen":
                if(iswhite) icon = "♕"; 
                else icon = "♛︎";
                break;
            case "Bishop":
                if(iswhite) icon = "♗"; 
                else icon = "♝︎";
                break;
            case "Knight":
                if(iswhite) icon = "♘"; 
                else icon = "♞";
                break;
            case "Rook":
                if(iswhite) icon = "♖"; 
                else icon = "♜";
                break;
            case "Hole":
                icon = "●";
                break;
            case "Freeze":
                icon = "❆";
                iconcolor = new Color(56,124,240);
                break;
            case "Entire Light":
                icon = "☥";
                iconcolor = new Color(240, 172, 56);
                break;
            case "ANGEL Roar":
                icon = "䨻";
                iconcolor = new Color(201, 48, 44);
                break;
            default:
                icon = "!";
                break;
        }
                        
        JLabel img = new JLabel(icon, JLabel.CENTER);
        img.setFont(new Font("Arial", 0, 25));
        img.setForeground(iconcolor);
        img.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(img);
    }

    private void drawcard() {
        JLabel img = new JLabel("?", JLabel.CENTER);
        img.setFont(new Font("Serif", 0, 25));
        img.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(img);
    }
    
}
