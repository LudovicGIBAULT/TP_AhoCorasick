package ihm;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.SoftBevelBorder;

import listeners.AddKeyWordListener;

/**
 * Fenetre Permettant la gestion des mots-clefs
 * 
 * @author  Ludovic GIBAULT et Jean-Baptiste BRUN
 *
 */
public class FrameKeyWords extends JPanel {
	
	/**
	 * Liste des mot-clefs
	 */
	private List<String> keyWords;
	
	/**
	 * Champs où l'on écrit que mot-clef que l'on veut rajouter
	 */
	private JTextField keyWordsField;
	
	/**
	 * Panel où l'on va afficher les mot-clefs
	 */
	private JPanel keyWordsPanel;
	
	/**
	 * Une largeur stockee juste pour la reutiliser plus tard
	 */
	private static int WIDTH = 400;
	
	/**
	 * Constructeur
	 * @param keyWords la liste des mot-clefs a modifier/afficher	
	 */
	public FrameKeyWords(List<String> keyWords) {
		super();
		
		this.keyWords = new ArrayList<String>();
		this.keyWords.addAll(keyWords);

		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(mainPanel);
		
		keyWordsPanel = new JPanel();
		JScrollPane scroll = new JScrollPane(keyWordsPanel);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(WIDTH, 500));
		
		mainPanel.add(scroll, BorderLayout.CENTER);
		keyWordsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		keyWordsPanel.setLayout(new BoxLayout(keyWordsPanel, BoxLayout.Y_AXIS));
				
		JPanel addKeyWordsPanel = new JPanel();
		mainPanel.add(addKeyWordsPanel, BorderLayout.SOUTH);
		addKeyWordsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		keyWordsField = new JTextField();
		keyWordsField.setPreferredSize(new Dimension(150, 20));
		
		JButton addKeyWordButton = new JButton("Ajouter");
		addKeyWordButton.addActionListener(new AddKeyWordListener(this, keyWordsField, this.keyWords));

		addKeyWordsPanel.add(keyWordsField);
		addKeyWordsPanel.add(addKeyWordButton);
		

		for(String keyWord : this.keyWords)
			addKeyWordToPanel(keyWord);
		
		setVisible(true);
	}
	
	public List<String> getKeyWords() {
		return keyWords;
	}
	
	public void addKeyWordToPanel(String str) {
		final JPanel panelKeyWord = new JPanel();

		final JLabel keyWord = new JLabel(str);
		JButton remove = new JButton("Supprimer");
		remove.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				keyWordsPanel.remove(panelKeyWord);
				keyWords.remove(keyWord.getText());

				keyWordsPanel.validate();
				keyWordsPanel.repaint();
				}
		});
		panelKeyWord.setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.RAISED, Color.BLACK, Color.GRAY));
		panelKeyWord.add(keyWord);
		panelKeyWord.add(remove);
		keyWordsPanel.add(panelKeyWord);
		panelKeyWord.setMaximumSize(new Dimension(WIDTH, 50));
		keyWordsPanel.validate();
		keyWordsPanel.repaint();
	}
}