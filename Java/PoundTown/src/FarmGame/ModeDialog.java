package FarmGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class ModeDialog extends JDialog{

	public ModeDialog( ) {
		setTitle("Welcome to #Town!");
		setSize(500, 200);
		setLocation(250,200);
		setModal(true);
		setLayout(new FlowLayout());
		setUndecorated(true);
		getRootPane().setOpaque(true);
		((JPanel)getContentPane()).setBackground(Color.CYAN);
		
		JPanel main = new JPanel();
		JPanel buttons = new JPanel();
		
		main.setBackground(Color.CYAN);
		buttons.setBackground(Color.CYAN);
		Font font = new Font("Comic Sans MS", Font.PLAIN, 20);
		
		JTextArea message = new JTextArea("Would you like to play on Easy mode or Hard mode?");
		
	
		message.setBackground(Color.CYAN);
		message.setEditable(false);
		message.setFont(font);
		message.setSize(new Dimension(450,120));
		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		main.add(message);
		
		
		JButton easy = new JButton("Easy");
		easy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameEngine.easyMode=true;
				CloseFrame();
			}
		});
		
		JButton hard = new JButton("Hard");
		hard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameEngine.easyMode=false;
				CloseFrame();
			}
		});

		
		buttons.add(easy);
		buttons.add(hard);
		
		add(main);
		add(buttons);
	}
	
	public void CloseFrame(){
	    super.dispose();
	}
}
