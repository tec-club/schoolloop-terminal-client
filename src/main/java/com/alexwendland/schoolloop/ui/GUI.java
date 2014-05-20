package com.alexwendland.schoolloop.ui;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Write a description of class GUI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GUI
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class GUI
     */
    public static void main(String[] args)
    {
        System.out.println("Created GUI on EDT? "+
        SwingUtilities.isEventDispatchThread());
        /*try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);
    }

    static class MyPanel extends JPanel {

        public MyPanel() {
            setBorder(BorderFactory.createLineBorder(Color.black));
        }
    
        public Dimension getPreferredSize() {
            return new Dimension(250,200);
        }
    
        public void paintComponent(Graphics g) {
            super.paintComponent(g);       
    
            // Draw Text
            g.drawString("This is my custom Panel!",10,20);
        }  
    }
}
