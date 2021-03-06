/***********************************************
This file is part of the Calao project (https://github.com/Neonunux/calao/wiki).

Calao is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Calao is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Calao.  If not, see <http://www.gnu.org/licenses/>.

**********************************************/
package calao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class SmartBar.
 *
 * @author Neonunux
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SmartBar extends JPanel implements ActionListener, ChangeListener {
	private static final Logger logger = LogManager.getLogger(SmartBar.class
			.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4914147249638690529L;

	/** The app bundle. */
	ResourceBundle appBundle;

	Preferences appPrefs;

	/** The app font. */
	Font appFont;

	/** The home btn. */
	public RoundedButton homeBtn;

	/** The tempo container. */
	RoundPanel tempoContainer;

	/** The tempo panel. */
	public JPanel tempoPanel;

	/** The tempo slider. */
	public JSlider tempoSlider;

	/** The tempo label. */
	private JTextField tempoLabel;

	/** The metronome check box. */
	public JCheckBox metronomeCheckBox;

	/** The clef note btn. */
	public RoundedButton clefNoteBtn;

	/** The game container. */
	private RoundPanel gameContainer;

	/** The game selector. */
	public JComboBox gameSelector;

	/** The game type. */
	public JComboBox gameType;

	/** The refresh btn. */
	public RoundedButton refreshBtn;

	/** The play btn. */
	public RoundedButton playBtn;

	/** The listen btn. */
	public RoundedButton listenBtn;

	/** The button size. */
	private int buttonSize = 70;

	/** The total obj width. */
	private int totalObjWidth = 725;

	/** The upper margin. */
	private int upperMargin = 20;

	/** The is inline. */
	private boolean isInline = false;

	/** The is ear training. */
	private boolean isEarTraining = false;

	/** The is training. */
	private boolean isTraining = false;

	/** The clef notes dialog. */
	private ClefNotesOptionDialog clefNotesDialog;

	/** The comp color. */
	private Color compColor = Color.decode("0x749CC5");

	/**
	 * Instantiates a new smart bar.
	 *
	 * @param d
	 *            the d
	 * @param b
	 *            the b
	 * @param f
	 *            the f
	 * @param p
	 *            the p
	 * @param inline
	 *            the inline
	 * @param earTraining
	 *            the ear training
	 * @param training
	 *            the training
	 */
	public SmartBar(Dimension d, ResourceBundle b, Font f, Preferences p,
			boolean inline, boolean earTraining, boolean training) {
		appBundle = b;
		appFont = f;
		appPrefs = p;
		isInline = inline;
		isEarTraining = earTraining;
		isTraining = training;
		setSize(d);
		setLayout(null);

		if (inline == false) {
			totalObjWidth = 700;
			upperMargin = 15;
		}

		int posX = (d.width - totalObjWidth) / 2;

		// Create home button
		homeBtn = new RoundedButton("", appBundle);
		homeBtn.setPreferredSize(new Dimension(buttonSize, buttonSize));
		homeBtn.setBounds(posX, upperMargin, buttonSize, buttonSize);
		homeBtn.setBackground(Color.decode("0x8FC6E9"));
		// homeBtn.setBackground(compColor);
		homeBtn.setButtonImage(new ImageIcon(getClass().getResource("home.png"))
				.getImage());

		posX += buttonSize + 5;

		// create button to configure clefs and notes levels
		clefNoteBtn = new RoundedButton("RBL_NOTES", appBundle);
		clefNoteBtn.setPreferredSize(new Dimension(160, buttonSize));
		clefNoteBtn.setBounds(posX, upperMargin, 160, buttonSize);
		clefNoteBtn.setBackground(Color.decode("0x8FC6E9"));
		clefNoteBtn.setFont(f);
		clefNoteBtn.addActionListener(this);

		posX += 165;

		// create tempo container with tempo scroller and metronome check box
		tempoContainer = new RoundPanel();
		tempoContainer.setBackground(compColor);
		((FlowLayout) tempoContainer.getLayout()).setHgap(7);

		tempoPanel = new JPanel(); // new FlowLayout(FlowLayout.CENTER, 20,
									// -2));
		tempoPanel.setLayout(null);
		Border defBorder = UIManager.getBorder(tempoPanel);
		tempoPanel.setBorder(BorderFactory.createTitledBorder(defBorder,
				appBundle.getString("_speed"), TitledBorder.LEADING,
				TitledBorder.TOP));
		tempoPanel.setBackground(compColor);
		tempoPanel.setPreferredSize(new Dimension(230, 80));

		if (inline == true) {
			if (isEarTraining == false) {
				tempoSlider = new JSlider(JSlider.HORIZONTAL, 30, 200, 60);
				tempoLabel = new JTextField("60");
			} else {
				tempoSlider = new JSlider(JSlider.HORIZONTAL, 1, 12, 6);
				tempoLabel = new JTextField("6 Sec.");
			}
			tempoLabel.setBounds(85, 22, 60, 25);
		} else {
			tempoSlider = new JSlider(JSlider.HORIZONTAL, 40, 200, 80);
			tempoLabel = new JTextField("80 BPM");
			tempoLabel.setBounds(25, 22, 65, 25);
		}
		tempoSlider.setBounds(20, 45, 190, 25);
		tempoSlider.addChangeListener(this);
		tempoLabel.setBackground(Color.white);
		// tempoLabel.setPreferredSize(new Dimension(65, 25));
		tempoLabel.setAlignmentX(LEFT_ALIGNMENT);
		if (inline == false) {
			metronomeCheckBox = new JCheckBox(
					appBundle.getString("_menuMetronom"));
			metronomeCheckBox.setForeground(Color.white);
			metronomeCheckBox.setAlignmentX(RIGHT_ALIGNMENT);
			metronomeCheckBox.setBounds(100, 22, 120, 25);
			int mOn = Integer.parseInt(appPrefs.getProperty("metronome"));
			if (mOn == 1)
				metronomeCheckBox.setSelected(true);
			// metronomeCheckBox.addChangeListener(this);
			metronomeCheckBox.addActionListener(this);
		}

		tempoPanel.add(tempoLabel);
		if (inline == false)
			tempoPanel.add(metronomeCheckBox);
		tempoPanel.add(tempoSlider);

		tempoContainer.add(tempoPanel);
		tempoContainer.setBounds(posX, upperMargin - 10, 240, 90);

		posX += 245;

		if (inline == true) {
			gameContainer = new RoundPanel();
			gameContainer.setBackground(compColor);
			// ((FlowLayout)gameContainer.getLayout()).setHgap(0);
			gameContainer.setLayout(null);
			// gameContainer.setPreferredSize(new Dimension(166, buttonSize));

			Font sbf = new Font("Arial", Font.BOLD, 13);
			gameSelector = new JComboBox();
			gameSelector.setFont(sbf);

			gameContainer.add(gameSelector);

			if (isEarTraining == false) {
				gameSelector.setBounds(8, 5, 150, 28);
				gameType = new JComboBox();
				gameType.setBounds(8, 37, 150, 28);
				gameType.setFont(sbf);

				gameContainer.add(gameType);
			} else {
				gameSelector.setBounds(8, 21, 150, 28);
				clefNoteBtn.setEnabled(false);
			}
			if (isTraining == true) {
				tempoContainer.setEnabled(true);
			} else {
				tempoContainer.setEnabled(false);
			}
			gameContainer.setBounds(posX, upperMargin, 166, buttonSize);
			posX += 171;
		}

		// Create refresh button
		if (inline == false) {
			refreshBtn = new RoundedButton("", appBundle);
			refreshBtn.setPreferredSize(new Dimension(buttonSize, buttonSize));
			refreshBtn.setBounds(posX, upperMargin, buttonSize, buttonSize);
			refreshBtn.setBackground(Color.decode("0x8FC6E9"));
			refreshBtn.setButtonImage(new ImageIcon(getClass().getResource(
					"refresh.png")).getImage());

			posX += buttonSize + 5;
		}
		// Create playback button
		playBtn = new RoundedButton("", appBundle);
		playBtn.setPreferredSize(new Dimension(buttonSize, buttonSize));
		playBtn.setBounds(posX, upperMargin, buttonSize, buttonSize);
		playBtn.setBackground(Color.decode("0x8FC6E9"));
		playBtn.setButtonImage(new ImageIcon(getClass().getResource(
				"playback.png")).getImage());

		posX += buttonSize + 5;

		if (inline == false) {
			// Create playback button
			listenBtn = new RoundedButton("", appBundle);
			listenBtn.setPreferredSize(new Dimension(buttonSize, buttonSize));
			listenBtn.setBounds(posX, upperMargin, buttonSize, buttonSize);
			listenBtn.setBackground(Color.decode("0x8FC6E9"));
			listenBtn.setButtonImage(new ImageIcon(getClass().getResource(
					"listen.png")).getImage());
		}

		updateLanguage(appBundle);

		this.add(homeBtn);
		this.add(clefNoteBtn);
		this.add(tempoContainer);
		if (inline == true) {
			this.add(gameContainer);
		} else {
			this.add(refreshBtn);
		}
		this.add(playBtn);
		if (inline == false)
			this.add(listenBtn);
	}

	/**
	 * Update language.
	 *
	 * @param bundle
	 *            the bundle
	 */
	public void updateLanguage(ResourceBundle bundle) {
		appBundle = bundle;

		clefNoteBtn.setResBundle(appBundle);
		Border defBorder = UIManager.getBorder(tempoPanel);
		tempoPanel.setBorder(BorderFactory.createTitledBorder(defBorder,
				appBundle.getString("_speed"), TitledBorder.LEADING,
				TitledBorder.TOP));

		if (isInline == true) {
			gameSelector.removeAllItems();

			if (isEarTraining == true) {
				gameSelector.addItem(appBundle.getString("_levBasic"));
				gameSelector.addItem(appBundle.getString("_levMedium"));
				gameSelector.addItem(appBundle.getString("_levAdvanced"));
				gameSelector.addItem(appBundle.getString("_levCustom"));
			} else {
				gameSelector.addItem(appBundle.getString("_normalgame"));
				gameSelector.addItem(appBundle.getString("_speedgame"));
				gameSelector.addItem(appBundle.getString("_linegame"));
				gameSelector.addItem(appBundle.getString("_learninggame"));
			}

			// TODO:
			// WARNING !!!! If you change these entries you must change indexes
			// of
			// InlinePanel.setLearningInfo and InlinePanel.setGameType !!!!
			if (isEarTraining == false) {
				gameType.removeAllItems();
				gameType.addItem(appBundle.getString("_notes"));
				gameType.addItem(appBundle.getString("_alterednotes"));
				gameType.addItem(appBundle.getString("_intervals") + " - "
						+ appBundle.getString("_second"));
				gameType.addItem(appBundle.getString("_intervals") + " - "
						+ appBundle.getString("_third"));
				gameType.addItem(appBundle.getString("_intervals") + " - "
						+ appBundle.getString("_fourth"));
				gameType.addItem(appBundle.getString("_intervals") + " - "
						+ appBundle.getString("_fifth"));
				gameType.addItem(appBundle.getString("_intervals") + " - "
						+ appBundle.getString("_sixth"));
				gameType.addItem(appBundle.getString("_intervals") + " - "
						+ appBundle.getString("_seventh"));
				gameType.addItem(appBundle.getString("_intervals") + " - "
						+ appBundle.getString("_octave"));
				// gameType.addItem(appBundle.getString("_intervals") + " - " +
				// appBundle.getString("_minor"));
				// gameType.addItem(appBundle.getString("_intervals") + " - " +
				// appBundle.getString("_major"));
				gameType.addItem(appBundle.getString("_chords"));
				// gameType.addItem(appBundle.getString("_inversion"));
			}
		} else {
			metronomeCheckBox.setText(appBundle.getString("_menuMetronom"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == clefNoteBtn) {
			logger.debug("SmartBar Event received !! (" + ae.getActionCommand()
					+ ")");
			clefNotesDialog = new ClefNotesOptionDialog(appFont, appBundle,
					appPrefs);
			clefNotesDialog.setVisible(true);
			clefNotesDialog
					.addPropertyChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							if (evt.getPropertyName() == "updateParameters") {
								logger.debug("ClefNotesOptionDialog update parameters.");
								firePropertyChange("updateParameters", false,
										true);
							}
						}
					});
		} else if (ae.getSource() == metronomeCheckBox) {
			if (metronomeCheckBox.isSelected() == true) {
				appPrefs.setProperty("metronome", "1");
			} else {
				appPrefs.setProperty("metronome", "0");
			}
			appPrefs.storeProperties();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
	 * )
	 */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == tempoSlider) {
			if (isInline == false)
				tempoLabel.setText(Integer.toString(tempoSlider.getValue())
						+ " BPM");
			else {
				if (isEarTraining == true)
					tempoLabel.setText(Integer.toString(tempoSlider.getValue())
							+ " Sec.");
				else
					tempoLabel
							.setText(Integer.toString(tempoSlider.getValue()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// logger.debug("SmartBar paintComponent. w: " + getWidth() + ", h: " +
		// getHeight());
		// g.setColor(Color.decode("0xAFC6E9"));
		GradientPaint vertGrad = new GradientPaint(0, 0,
				Color.decode("0xAFC6E9"), 0, getHeight() - 50,
				Color.decode("0x4D5D8F"));
		((Graphics2D) g).setPaint(vertGrad);
		g.fillRoundRect(20, -20, getWidth() - 40, getHeight(), 25, 25);

		int posX = (getWidth() - totalObjWidth) / 2;
		homeBtn.setBounds(posX, upperMargin, buttonSize, buttonSize);
		posX += buttonSize + 5;
		clefNoteBtn.setBounds(posX, upperMargin, 160, buttonSize);
		posX += 165;
		tempoContainer.setBounds(posX, upperMargin - 10, 240, 90);
		posX += 245;
		if (isInline == true) {
			gameContainer.setBounds(posX, upperMargin, 166, buttonSize);
			posX += 171;
		} else {
			refreshBtn.setBounds(posX, upperMargin, buttonSize, buttonSize);
			posX += buttonSize + 5;
		}

		playBtn.setBounds(posX, upperMargin, buttonSize, buttonSize);
		posX += buttonSize + 5;

		if (isInline == false)
			listenBtn.setBounds(posX, upperMargin, buttonSize, buttonSize);
	}

	/**
	 * Sets the training.
	 *
	 * @param state
	 *            the new training
	 */
	public void setTraining(boolean state) {
		this.isTraining = state;
	}
}