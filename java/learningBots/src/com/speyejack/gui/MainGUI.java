package com.speyejack.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.speyejack.bots.Arena;
import com.speyejack.bots.Fight;

public class MainGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 9222519679125563536L;
	private final Dimension dimension = new Dimension(500, 500);
	private boolean maxSpeed = false;
	private boolean render = true;
	private boolean running = false;

	private FighterPanel fpanel;
	private Arena arena;
	private Fight fight;
	private ConfigGUI configGUI;
	private Controller control;

	public MainGUI() {
		System.out.print("Initalizing...");
		DebugGUI gui = new DebugGUI();
		arena = new Arena(gui);
		new Thread(arena).start();
		fpanel = new FighterPanel(dimension);
//		configGUI = new ConfigGUI(this);
		control = new Controller(this);

		this.setTitle("Learning Bots");
		this.setIconImage(new ImageIcon("Fighter.png").getImage());
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				running = false;
				arena.stop();
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				// dimension.setSize(getSize());
			}
		});
		this.add(fpanel);
		this.setVisible(true);
		gui.pack();
		this.pack();
		System.out.println("Done");
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Get Top") {
			fight = arena.getTopFight();
			fpanel.setFight(fight);
			new Thread(fpanel).start();
		}
	}

	public static void main(String[] args) {
		MainGUI gui = new MainGUI();

		/*
		 * Bug List: Contract thingy, Fitness spikes, have total competition(every opponent fights every other opponent)
		 */

	}

	/*
	 * public void mainLoop() { running = true; long lastUpdate =
	 * System.nanoTime(); long lastRender = System.nanoTime(); long now; // try
	 * { System.out.println("Commencing Test"); while (running) {
	 * configGUI.updateText(arena); render = configGUI.isRendering(); maxSpeed =
	 * configGUI.isMaxSpeed(); now = System.nanoTime(); if (maxSpeed ||
	 * TARGET_UPDATE_TIME <= (now - lastUpdate)) { arena.update(); lastUpdate =
	 * now; }
	 * 
	 * if (render && (maxSpeed || TARGET_RENDER_TIME <= (now - lastRender))) {
	 * panel.repaint(); lastRender = now; }
	 * 
	 * if (!maxSpeed) { try { Thread.sleep((long) 0.01); } catch (Exception e) {
	 * e.printStackTrace(); } } } // System.exit(0); // } catch (Exception e) {
	 * // e.printStackTrace(); // } finally { // System.exit(0); // } }
	 */

}
