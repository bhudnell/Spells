import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SpellsGUI extends JFrame {

	public static void main(String[] args) {
		SpellsGUI theView = new SpellsGUI();
		theView.setVisible(true);
	}

	private DatabaseAdaptor dba = new DatabaseAdaptor();

	private JScrollPane spellListAreaScroll;
	private JTextArea spellListArea;
	private JTextField searchArea;
	private JButton searchButton = new JButton("Search");
	private JButton updateButton = new JButton("Update");
	private JMenuBar menuBar;
	private JMenu classMenu, srMenu, levelMenu;
	private MyCheckBoxMenuItem sorc, wiz, cleric, druid, ranger, bard, pala, alch, invest, summ, witch, inq, oracle,
			antipala, magus, adept, hunt, warp, blood, shaman, psy, medium, mesm, occ, spirit, skald, arc;
	private MyCheckBoxMenuItem zero, frst, scnd, thrd, frth, ffth, sxth, svnth, ghth, nnth;
	private JRadioButtonMenuItem sr;

	public SpellsGUI() {
		layoutWindow();
		setupListeners();
	}

	private void setupListeners() {
		searchButtonListener searchListener = new searchButtonListener();
		updateButtonListener updateListener = new updateButtonListener();
		searchButton.addActionListener(searchListener);
		updateButton.addActionListener(updateListener);
	}

	private void layoutWindow() {
		// Set up the JFrame TODO
		this.setSize(1000, 800);
		this.setResizable(false);
		setLocation(10, 10);
		setTitle("Pathfinder Spells");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		// Layout the spell results as a JTextArea
		spellListArea = new JTextArea();
		spellListArea.setEditable(false);
		spellListArea.setBackground(Color.white);
		// spellListArea.setFont(new Font("Courier", Font.PLAIN, 12));
		spellListAreaScroll = new JScrollPane(spellListArea);
		spellListAreaScroll.setSize(980, 625);
		spellListAreaScroll.setLocation(8, 140);
		add(spellListAreaScroll);

		// Layout the search bar as a JTestField
		searchArea = new JTextField();
		searchArea.setBackground(Color.white);
		searchArea.setSize(400, 25);
		searchArea.setLocation(10, 50);
		add(searchArea);

		// Layout the search criteria menu
		menuBar = new JMenuBar();
		menuBar.setLocation(10, 100);
		menuBar.setSize(400, 25);
		classMenu = new JMenu("Caster Class");
		menuBar.add(classMenu);
		srMenu = new JMenu("SR");
		menuBar.add(srMenu);
		levelMenu = new JMenu("Spell Level");
		menuBar.add(levelMenu);

		//class list menu
		alch = new MyCheckBoxMenuItem("Alchemist");
		classMenu.add(alch);
		antipala = new MyCheckBoxMenuItem("Antipaladin");
		classMenu.add(antipala);
		arc = new MyCheckBoxMenuItem("Arcanist");
		classMenu.add(arc);
		bard = new MyCheckBoxMenuItem("Bard");
		classMenu.add(bard);
		blood = new MyCheckBoxMenuItem("Bloodrager");
		classMenu.add(blood);
		cleric = new MyCheckBoxMenuItem("Cleric");
		classMenu.add(cleric);
		druid = new MyCheckBoxMenuItem("Druid");
		classMenu.add(druid);
		hunt = new MyCheckBoxMenuItem("Hunter");
		classMenu.add(hunt);
		inq = new MyCheckBoxMenuItem("Inquisitor");
		classMenu.add(inq);
		invest = new MyCheckBoxMenuItem("Investigator");
		classMenu.add(invest);
		magus = new MyCheckBoxMenuItem("Magus");
		classMenu.add(magus);
		medium = new MyCheckBoxMenuItem("Medium");
		classMenu.add(medium);
		mesm = new MyCheckBoxMenuItem("Mesmerist");
		classMenu.add(mesm);
		occ = new MyCheckBoxMenuItem("Occultist");
		classMenu.add(occ);
		oracle = new MyCheckBoxMenuItem("Oracle");
		classMenu.add(oracle);
		pala = new MyCheckBoxMenuItem("Paladin");
		classMenu.add(pala);
		psy = new MyCheckBoxMenuItem("Psychic");
		classMenu.add(psy);
		ranger = new MyCheckBoxMenuItem("Ranger");
		classMenu.add(ranger);
		shaman = new MyCheckBoxMenuItem("Shaman");
		classMenu.add(shaman);
		skald = new MyCheckBoxMenuItem("Skald");
		classMenu.add(skald);
		sorc = new MyCheckBoxMenuItem("Sorcerer");
		classMenu.add(sorc);
		spirit = new MyCheckBoxMenuItem("Spiritualist");
		classMenu.add(spirit);
		summ = new MyCheckBoxMenuItem("Summoner");
		classMenu.add(summ);
		warp = new MyCheckBoxMenuItem("Warpriest");
		classMenu.add(warp);
		witch = new MyCheckBoxMenuItem("Witch");
		classMenu.add(witch);
		wiz = new MyCheckBoxMenuItem("Wizard");
		classMenu.add(wiz);
		adept = new MyCheckBoxMenuItem("Adept");
		classMenu.add(adept);

		//spell level menu
		zero = new MyCheckBoxMenuItem("0th");
		levelMenu.add(zero);
		frst = new MyCheckBoxMenuItem("1st");
		levelMenu.add(frst);
		scnd = new MyCheckBoxMenuItem("2nd");
		levelMenu.add(scnd);
		thrd = new MyCheckBoxMenuItem("3rd");
		levelMenu.add(thrd);
		frth = new MyCheckBoxMenuItem("4th");
		levelMenu.add(frth);
		ffth = new MyCheckBoxMenuItem("5th");
		levelMenu.add(ffth);
		sxth = new MyCheckBoxMenuItem("6th");
		levelMenu.add(sxth);
		svnth = new MyCheckBoxMenuItem("7th");
		levelMenu.add(svnth);
		ghth = new MyCheckBoxMenuItem("8th");
		levelMenu.add(ghth);
		nnth = new MyCheckBoxMenuItem("9th");
		levelMenu.add(nnth);
		
		//SR menu
		sr = new JRadioButtonMenuItem("SR");
		srMenu.add(sr);
		
		add(menuBar);

		// Layout the search button
		searchButton.setSize(100, 25);
		searchButton.setLocation(450, 50);
		add(searchButton);
		
		// Layout the search button
		updateButton.setSize(100, 25);
		updateButton.setLocation(650, 50);
		add(updateButton);
	}

	private class MyCheckBoxMenuItem extends JCheckBoxMenuItem {
		public MyCheckBoxMenuItem(String text) {
			super(text);
		}

		@Override
		protected void processMouseEvent(MouseEvent evt) {
			if (evt.getID() == MouseEvent.MOUSE_RELEASED && contains(evt.getPoint())) {
				doClick();
				setArmed(true);
			} else
				super.processMouseEvent(evt);
		}
	}
	
	private class searchButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String search = searchArea.getText();
			ResultSet result = dba.nameSearch(search);
			String report = "";

			try {
				while (result.next()) {
					String name = result.getString("name");
					String descrip = result.getString("description");
					String level = result.getString("spell_level");
					
					report += name + "\t" + level + "\t" + descrip + "\n";
				}
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
			spellListArea.setText(report);
		}

	}

	private class updateButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unused")
			int confirmButton = JOptionPane.YES_NO_OPTION;
			int confirmResult = JOptionPane.showConfirmDialog(null, "Are you sure? Will take ~10 minutes.");
			if (confirmResult == JOptionPane.YES_OPTION)
				dba.updateTable();
		}
	}
}