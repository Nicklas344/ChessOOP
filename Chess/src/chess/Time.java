package chess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * This is the Time Class. It contains all the required variables and functions
 * related to the timer on the Main GUI It uses a Timer Class
 */
public class Time {
	private JLabel label;
	Timer countdownTimer;
	int timeRem;

	public Time(JLabel passedLabel) {
		countdownTimer = new Timer(1000, new CountdownTimerListener());
		this.label = passedLabel;
		timeRem = Main.timeRemaining;
	}

	public void start() {
		countdownTimer.start();
	}

	public void reset() {
		timeRem = Main.timeRemaining;
	}

	class CountdownTimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int min, sec;
			if (timeRem > 0) {
				min = timeRem / 60;
				sec = timeRem % 60;
				label.setText(
						String.valueOf(min) + ":" + (sec >= 10 ? String.valueOf(sec) : "0" + String.valueOf(sec)));
				timeRem--;
			} else {
				label.setText("Time's up!");
				reset();
				start();
				Main.mainboard.changeTurn();
			}
		}
	}
}