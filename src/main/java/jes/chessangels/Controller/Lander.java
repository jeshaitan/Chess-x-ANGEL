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
package jes.chessangels.Controller;

import jes.chessangels.GameModel.Card;
import jes.chessangels.GameModel.Game;
import jes.chessangels.GameModel.TileStatus;

/**
 *
 * @author jes
 */
public class Lander {

    public static void Land(Game g, Card c, int row, int col) {
        if(c.summons > 0) {
            if(g.turnplayer.iswhite)
                g.gameboard.piecematrix[row][col].color = 1;
            else
                g.gameboard.piecematrix[row][col].color = -1;
        }
        switch(c.name) {
            case "Pawn":
                g.gameboard.piecematrix[row][col].type = 'p';
                break;
            case "Queen":
                g.gameboard.piecematrix[row][col].type = 'q';
                break;
            case "King":
                g.gameboard.piecematrix[row][col].type = 'k';
                break;
            case "Rook":
                g.gameboard.piecematrix[row][col].type = 'r';
                break;
            case "Bishop":
                g.gameboard.piecematrix[row][col].type = 'b';
                break;
            case "Knight":
                g.gameboard.piecematrix[row][col].type = 'n';
                break;
            case "Hole":
                g.gameboard.statusesmatrix[row][col].add(new TileStatus("Hole"));
                break;
            case "Freeze":
                g.gameboard.piecematrix[row][col].inflict("Frozen");
                break;
            case "Entire Light":
                g.gameboard.statusesmatrix[row][col].add(new TileStatus("Entire Light"));
                break;
            default:
                g.gameboard.piecematrix[row][col].type = '!';
                break;
        }
                
    }
    
    
}
