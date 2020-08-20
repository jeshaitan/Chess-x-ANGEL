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
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import jes.chessangels.GameModel.Game;

/**
 *
 * @author jes
 */
public class HistPanel extends JPanel {
    
    private ArrayList<String> futuremessages = new ArrayList<String>();
    
    public HistPanel(Game game) {
                
        setPreferredSize(new Dimension(350, 740));
        setMinimumSize(new Dimension(350, 740));
        setMaximumSize(new Dimension(350, 740));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                
        write("Game start!");
        write("You have <b>" + game.turnplayer.deck.size() + "</b> cards in your deck.");
        write("You have <b>" + game.turnplayer.wp + "</b> willpower to spend.");
        
        setBorder(new TitledBorder(new LineBorder(new Color(169, 169, 169)),
                "ANGEL History", TitledBorder.LEADING, TitledBorder.TOP,
                null, null));
        setBackground(Color.white);
        
    }
    
    public final void write(String message) {
        JLabel linelabel = new JLabel();
        linelabel.setFont(new Font("Arial", 0, 12));
        linelabel.setText("<html>" + message + "</html>");
        linelabel.setMinimumSize(new Dimension(350, linelabel.getMinimumSize().height));
        linelabel.setHorizontalAlignment(SwingConstants.LEFT);
        linelabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 0));
        add(linelabel);
        updateUI();
    }
    
    // Saves message for next turn
    public final void futurewrite(String message) {
        futuremessages.add(message);
    }
    
    public final void writefromfuture() {
        for (String fm : futuremessages) {
            write(fm);
        }
        futuremessages.clear();
    }
    
}
