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
package jes.chessangels.GameModel;

/**
 *
 * @author jes
 */
public class Card {
    
    public String name;
    public int cost;
    public String desc;
    
    // Does this card need to target a particular tile
    public boolean doestarget;
    
    // If targeting, coords of possible locations
    public int[] libertiesrows;
    public int[] libertiescols;
    
    // How many pieces does this card summon to the board
    public int summons;
    
    public Card(int id) {
        switch(id) {
            case 1:
                name = "King";
                cost = 10;
                desc = "Enter... the King!?";
                doestarget = true;
                summons = 1;
                break;
            case 2:
                name = "Pawn";
                cost = 1;
                desc = "Enter, a lowly but loyal pawn.";
                doestarget = true;
                summons = 1;
                break;
            case 3:
                name = "Queen";
                cost = 9;
                desc = "Enter, her majesty the queen!!";
                doestarget = true;
                summons = 1;
                break;
            case 4:
                name = "Bishop";
                cost = 3;
                desc = "Enter, the reticent bishop...";
                doestarget = true;
                summons = 1;
                break;
            case 5:
                name = "Knight";
                cost = 2;
                desc = "Enter, the rushing knight.";
                doestarget = true;
                summons = 1;
                break;
            case 6:
                name = "Rook";
                cost = 5;
                desc = "Enter, the towering rook!";
                doestarget = true;
                summons = 1;
                break;
            case 7:
                name = "Hole";
                cost = 3;
                desc = "Open a dark hole to... who knows where? Closes after 4 turns.";
                doestarget = true;
                summons = 0;
                break;
            case 8:
                name = "Freeze";
                cost = 2;
                desc = "Freeze a piece for its next turn (in solid ice).";
                doestarget = true;
                summons = 0;
                break;
            case 9:
                name = "Entire Light";
                cost = 1;
                desc = "Purify a square after 5 turns. Kings are immune.";
                doestarget = true;
                summons = 0;
                break;
            case 10:
                name = "ANGEL Roar";
                cost = 4;
                desc = "Blast enemies out of the center row!";
                doestarget = false;
                summons = 0;
                break;
            default:
                name = "!";
                desc = "Something went wrong...";
        }
    }
    
}
