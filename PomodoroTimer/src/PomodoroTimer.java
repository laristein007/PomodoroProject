import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
//For audio player
import javax.sound.sampled.*;


public class PomodoroTimer implements ActionListener{

	//Global variables and objects
	
	JFrame frame = new JFrame();
	
	//For audio player
	Clip clip;
	
	//Variables to store custom font: (Font location, size)
	Font uiText = loadFont(new File("src/fonts/AudioNugget.ttf"), 65);
	Font uiTime = loadFont(new File("src/fonts/moonhouse.ttf"), 47);
	
	
	//Images for buttons
	ImageIcon startImg = new ImageIcon("src/icons/play.png");
	ImageIcon pauseImg = new ImageIcon("src/icons/pause.png");
	ImageIcon resetImg = new ImageIcon("src/icons/restart.png");
	
	Image originalStartImage = startImg.getImage();  // Get the Image from the ImageIcon
	Image originalPauseImage = pauseImg.getImage();
	Image originalResetImage = resetImg.getImage();
	
	
	//Icons for buttons
	ImageIcon startIcon = new ImageIcon((originalStartImage).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
	ImageIcon pauseIcon = new ImageIcon((originalPauseImage).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
	ImageIcon resetIcon = new ImageIcon((originalResetImage).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
	//Image for HUD
	ImageIcon hud = new ImageIcon("src/icons/hud.png");
	
	//Buttons
	JButton currentStateButton = new JButton(); //Button to set play and pause
	JButton startButton = new JButton();
	JButton pauseButton = new JButton();
	JButton resetButton = new JButton();
	
	//JLabel
	JLabel timeLabel = new JLabel();
	JLabel textLabel = new JLabel("Timer");
	
	//JPanel - This is for organization; Edit to add background image
	JPanel contentPanel = new JPanel() {
		 @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);
             // Load and draw the image
             ImageIcon imageIcon = hud;
             Image image = imageIcon.getImage();
             
          // Cast Graphics to Graphics2D for advanced drawing operations
             Graphics2D g2d = (Graphics2D) g;

             // Set the opacity (range from 0.0 to 1.0, where 0.0 is fully transparent)
             float opacity = 0.2f;  // Set the opacity (50% transparent in this case)
             g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

             // Draw the image, scaling it to fill the entire panel
             g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);  
          // After drawing the background, reset the opacity and draw UI elements
             g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));  // Reset opacity
             
         }
	};
	

	//Time holders
	//int elapsedTime = 1500000;
	int elapsedTime = 3000; //For testing
	//int minutes = 25;
	int minutes = 0; //For testing
	//int seconds = 0;
	int seconds = 3; //For testing
	
	//Flags for timner state
	boolean started, reset, paused = false;
	//Strings to display time
	String minString = String.format("%02d", minutes);
	String secString = String.format("%02d", seconds);
	String currentState = "Pomodoro"; //To display what the timer is curerntly doing
	
	Color colorBack = Color.decode("#000000");//Frame background color
	// #161618
	int backgroundColor = colorBack.getRGB();
	
	Color timeColor = Color.decode("#00ff9f"); //Font custom color for time
	Color textColor = Color.decode("#ffffff"); //Font custom color for text
	
	
	//Timer
	Timer timer = new Timer(1000, new ActionListener() {
		//Action performed
		public void actionPerformed(ActionEvent e) {
			//Every second
			//Decreased elapsed time
			elapsedTime -= 1000;
			//Display time and second left
			minutes = (elapsedTime / 60000) % 60;
			seconds = (elapsedTime / 1000) % 60;
			
			minString = String.format("%02d", minutes);
			secString = String.format("%02d", seconds);
			
			timeLabel.setText(minString + ":" + secString);
			
			
			// If time runs out, stop the timer
            if (elapsedTime <= 0) {
                timer.stop();
                currentState = "Time's up!";
                textLabel.setText(currentState);
                try {
					playSound(new File("src/sound/timerEnd.wav"));
					//Toggle button
					currentStateButton.setIcon(startIcon);
					//Reset timer
					timeUp();
					
				} catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
		}
		
	});
	
	public PomodoroTimer() {
		// Constructor
		frame.setTitle("LarisPomodoro");
		//Add HUD
				contentPanel.setLayout(null);
				frame.setContentPane(contentPanel);
		
		//TextLabel
		textLabel.setFont(uiText);
		textLabel.setForeground(textColor);
		textLabel.setText(currentState);
		textLabel.setBounds(90, 90, 500, 100);
		textLabel.setHorizontalAlignment(JTextField.CENTER);
		
		
		//TimeLabel
		//Update time and display
		timeLabel.setText(minString + ":" + secString);
		timeLabel.setBounds(240, 140, 200, 100);
		//Set font
		timeLabel.setFont(uiTime);
		timeLabel.setForeground(timeColor);
		//timeLabel.setOpaque(true);
		timeLabel.setHorizontalAlignment(JTextField.CENTER);
		
		// StartButton
		startButton.setFocusable(false);
		startButton.setBounds(250, 240,startIcon.getIconWidth(), startIcon.getIconHeight());  // Adjust bounds for startButton
		startButton.addActionListener(this);
		startButton.setIcon(startIcon);
		startButton.setOpaque(false);  // Make sure the button is transparent
		startButton.setContentAreaFilled(false);  // Prevent the background from filling
		startButton.setBorderPainted(false);  // Hide the border
		
		//PauseButton
		pauseButton.setFocusable(false);
		pauseButton.setBounds(250, 240,startIcon.getIconWidth(), startIcon.getIconHeight());  // Adjust bounds for startButton
		pauseButton.addActionListener(this);
		pauseButton.setIcon(pauseIcon);
		pauseButton.setOpaque(false);  // Make sure the button is transparent
		pauseButton.setContentAreaFilled(false);  // Prevent the background from filling
		pauseButton.setBorderPainted(false);  // Hide the border
		
		//ResetButton
		resetButton.setFocusable(false);
		resetButton.setBounds(380, 240,startIcon.getIconWidth(), startIcon.getIconHeight());  // Adjust bounds for startButton
		resetButton.addActionListener(this);
		resetButton.setIcon(resetIcon);
		resetButton.setOpaque(false);  // Make sure the button is transparent
		resetButton.setContentAreaFilled(false);  // Prevent the background from filling
		resetButton.setBorderPainted(false);  // Hide the border
		
		//Add frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked
		frame.add(textLabel);
		frame.add(timeLabel);
		
		currentStateButton = startButton;
		//Add buttoms to frame
		frame.add(currentStateButton);
		frame.add(resetButton);
		
		//Set size
		frame.setSize(680, 500);
		frame.setLayout(null);
		frame.getContentPane().setBackground(new Color(backgroundColor));
		
		frame.setVisible(true);
		frame.setResizable(false);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == startButton) {
			if(!started) { //Check the flag
				started = true; //Set flag to true
				//Toggle
				//currentStateButton = pauseButton;
				start();
				//Stop playing if a button is clicked
				clip.stop();
			}else{ //If timer is running
				started = false;
				//Toggle
				currentStateButton = startButton;
				pause();
				//Stop playing if a button is clicked
				clip.stop();
			}
		}
		if(e.getSource() == pauseButton) {
			started = false; //Reset flag
			//Toggle button
			currentStateButton = startButton;
			pause();
		}
		if(e.getSource() == resetButton) {
			started = false; //Reset flag
			reset();
			//Stop playing if a button is clicked
			clip.stop();
		}
		
	}
	
	
	void start() {
		//When start button is clicked
		timer.start();
		//Toggle
			currentStateButton.setIcon(pauseIcon);
		
		currentState = "Work";
		textLabel.setText(currentState);
	}
	
	void pause() {
		timer.stop();
		//Toggle
			currentStateButton.setIcon(startIcon);
			
		currentState = "Paused";
		textLabel.setText(currentState);
	}
	
	void reset(){
		timer.stop();
		elapsedTime = 1500000;
		minutes = 25;
		seconds = 0;
		//Toggle
			currentStateButton.setIcon(startIcon);
		
		minString = String.format("%02d", minutes);
		secString = String.format("%02d", seconds);
		
		currentState = "Pomodoro";
		textLabel.setText(currentState);
		//Reset timer
		timeLabel.setText(minString + ":" + secString);
		
	}
	
	void timeUp() {
		timer.stop();
		elapsedTime = 1500000;
		minutes = 25;
		seconds = 0;
		//Toggle
			currentStateButton.setIcon(startIcon);
		
		minString = String.format("%02d", minutes);
		secString = String.format("%02d", seconds);
		
		currentState = "Time's Up!";
		textLabel.setText(currentState);
		//Reset timer
		timeLabel.setText(minString + ":" + secString);
		started = false;
	}
	
	//method to load font
	private Font loadFont(File customFile, int size) {
			Font newFont = null;
			try {
				newFont = Font.createFont(Font.TRUETYPE_FONT, customFile);
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(newFont);
			} catch (FontFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return newFont.deriveFont(Font.PLAIN, size);
	}

	//Method to load sound
	private void playSound(File soundPath) throws LineUnavailableException {
		
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundPath);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			//Play sound
			clip.start();
			
		} catch (UnsupportedAudioFileException |IOException error) {
			// TODO Auto-generated catch block
			error.printStackTrace();
		}
	}
}
