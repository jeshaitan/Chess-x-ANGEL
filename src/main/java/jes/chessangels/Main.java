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
package jes.chessangels;

import jes.chessangels.GameModel.Game;
import jes.chessangels.View.CAsFrame;

/**
 *
 * @author jes
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Game game = new Game();
        
        javax.swing.ToolTipManager.sharedInstance().setInitialDelay(0);
        CAsFrame ca = new CAsFrame(game);
                
    }
    
}
