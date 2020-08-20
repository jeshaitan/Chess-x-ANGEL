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
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import jes.chessangels.GameModel.Board;
import jes.chessangels.GameModel.Piece;
import static jes.chessangels.GameModel.Piece.ischesspiece;
import jes.chessangels.GameModel.PieceStatus;
import jes.chessangels.GameModel.TileStatus;

/**
 *
 * @author jes
 */
public class ChessTile extends JButton {
    
    public ChessTile(ActionListener al, Board boardmodel, int row, int col, boolean marked) {
        
        addActionListener(al);
        setActionCommand("Tile:" + row + "," + col);
        
        setBorder(BorderFactory.createEtchedBorder(0));
        setBackground(Color.white);
        if((row - col) % 2 == 0) {
            setBackground(Color.LIGHT_GRAY);
        }
        if(marked)
            setBackground(Color.pink);

        setOpaque(true);
        drawpiece(boardmodel.piecematrix[row][col], boardmodel.statusesmatrix[row][col]);
    }
        
    public final void drawpiece(Piece p, ArrayList<TileStatus> tilestatuses) {
        String icon = "!";
        String bordertitle = "";
        Color bordercolor = Color.BLACK;
        // Special non-piece tile status visuals
        for(TileStatus ts : tilestatuses) {
            if(!bordertitle.equals(""))
                bordertitle += ", ";
            switch (ts.name) {
                case "Hole":
                    icon = "🕳●";
                    bordertitle += "HL: " + ts.getturnsleft();
                    break;
                case "Entire Light":
                    bordertitle += "EL:" + ts.getturnsleft();
                    bordercolor = new Color(240, 172, 56);
                    break;
            }   
            setBorder(new TitledBorder(new LineBorder(bordercolor),
                        bordertitle, TitledBorder.LEADING, TitledBorder.TOP,
                        null, null));
        }

        // Doesn't have a special icon
        if(icon.equals("!")) {
            boolean iswhite = p.color == 1;
            switch (p.type) {
                case 'k':
                    if(iswhite) icon = "♔"; 
                    else icon = "♚";
                    break;
                case 'p':
                    if(iswhite) icon = "♙"; 
                    else icon = "♟";
                    break;
                case 'q':
                    if(iswhite) icon = "♕"; 
                    else icon = "♛︎";
                    break;
                case 'b':
                    if(iswhite) icon = "♗"; 
                    else icon = "♝︎";
                    break;
                case 'n':
                    if(iswhite) icon = "♘"; 
                    else icon = "♞";
                    break;
                case 'r':
                    if(iswhite) icon = "♖"; 
                    else icon = "♜";
                    break;
                case 'e':
                    icon = " ";
                    break;
                default:
                    icon = "!";
                    break;
            }
        }
        
        JLabel img = new JLabel(icon, JLabel.CENTER);
        Color iconcolor = Color.BLACK;
        // Piece status other visuals
        if(ischesspiece(p.type)) {
            for (PieceStatus ps : p.piecestatuses) {
                switch (ps.name) {
                    case "Frozen":
                        iconcolor = new Color(56, 124, 240);
                        break;
                    default:
                        break;
                }
            }
        } 
        
        img.setForeground(iconcolor);
        img.setFont(new Font("Arial", 0, 30));
        img.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(img);
                
    }
    
}
