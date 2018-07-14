package movingPointer;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.Time;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.NumberFormatter;

public class Design{

	BusinessLogic businessLogic = new BusinessLogic();
	
	JFrame jFrame = new JFrame();
	JPanel jPanelNorth = new JPanel();
	JPanel jPanelCenter = new JPanel();
	JPanel jPanelSouth = new JPanel();
	
	JTextField jTextField = new JTextField();
	JFormattedTextField jFormattedTextField = new JFormattedTextField(new NumberFormatter());
	JButton up = new BasicArrowButton(BasicArrowButton.NORTH);
	JButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
	JButton start = new JButton("실행");
	JButton stop = new JButton("중지");
	
	MakeThread makeThread = new MakeThread();
	
	public Design() {
	
		jPanelNorth.setBackground(Color.ORANGE);
		jPanelCenter.setBackground(Color.ORANGE);
		jPanelSouth.setBackground(Color.ORANGE);
		
		jFrame.setSize(250,150);
		jFrame.setLocation(800, 300);
		jTextField.setBackground(Color.ORANGE);
		jTextField.setText("마우스 움직이기");
		jTextField.setEditable(false);
		jPanelNorth.add(jTextField);
		
		jTextField = new JTextField();
		jTextField.setBackground(Color.ORANGE);
		jTextField.setText("시간 설정(분) :");
		jTextField.setEditable(false);
		
		jFormattedTextField.setText("1");
		jFormattedTextField.setPreferredSize(new Dimension(50, 20));
		
		jPanelCenter.add(jTextField);
		jPanelCenter.add(jFormattedTextField);
		
		up.addActionListener(new doActionListener());
		down.addActionListener(new doActionListener());
		jPanelCenter.add(up);
		jPanelCenter.add(down);
		
		start.addActionListener(new doActionListener());
		stop.setEnabled(false);
		stop.addActionListener(new doActionListener());
		
		jPanelSouth.add(start);
		jPanelSouth.add(stop);
		
		
		jFrame.add(jPanelNorth, "North");
		jFrame.add(jPanelCenter, "Center");
		jFrame.add(jPanelSouth, "South");
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jFrame.setVisible(true);
	}
	
	private class MakeThread extends Thread{
		private volatile boolean running = true;
		
		@Override
		public void run() {
			int timeTerm = (int)Double.parseDouble(jFormattedTextField.getText());
			jFormattedTextField.setText(String.valueOf(timeTerm));
			timeTerm = timeTerm < 0 ? 0 : timeTerm; /* 타이핑으로 바로 입력했을 경우를 대비 */
			System.out.println("러닝 상태: "+running);
			while(running){
				try {
					start.setEnabled(false);
					Robot robot = new Robot();
					Random random = new Random();
					
					System.out.println("스레드 탐");
					this.sleep(timeTerm*1000*60);
					robot.mouseMove(random.nextInt(1200)+200, random.nextInt(900)+200);
				
				} catch (AWTException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e2) {
					System.out.println("running 상태: "+running);
					System.out.println("스레드 중지 ");
					
					e2.printStackTrace();
				}
			}
			
		}
		
		public void stopThread(){
			System.out.println("stop 버튼 동작");
			start.setEnabled(true);
			stop.setEnabled(false);
			running = false;
			this.interrupt();
			this.interrupted();
			makeThread = new MakeThread();
		}
	
	}
	
	
	private class doActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource().equals(up)) {
				
				String upTime = businessLogic.up(jFormattedTextField.getText());
				jFormattedTextField.setText(upTime);
				
			}else if(e.getSource().equals(down)) {
				
				String downTime = businessLogic.down(jFormattedTextField.getText());
				jFormattedTextField.setText(downTime);
				
			}else if(e.getSource().equals(start) || e.getSource().equals(stop)){			
				
				if(e.getSource().equals(start)){
					stop.setEnabled(true);
					makeThread.start();
				}else{
					makeThread.stopThread();
				}
				
						
			}
		}
		
	}
	
}
