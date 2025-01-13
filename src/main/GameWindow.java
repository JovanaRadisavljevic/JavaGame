package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow  {
	private JFrame jframe;
	
	public GameWindow(GamePanel gamePanel) {
		jframe=new JFrame();
		//da se prekine kad zatvorim prozor
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		
		jframe.setResizable(false);
		jframe.pack(); //fit the size of window
		jframe.setLocationRelativeTo(null);//centriraj formu
		jframe.setVisible(true);
		jframe.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				//sad kad drzim D i pustim a odem na neki drugi ekran igrac staje sa kretanjem
				gamePanel.getGame().windowLostFocus();
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
}
