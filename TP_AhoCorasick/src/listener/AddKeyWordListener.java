package listener;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import ihm.FrameKeyWords;

/**
 * Listener pour ajouter un mot-clef à la liste
 * 
 * @author Ludovic GIBAULT et Jean-Baptiste BRUN
 *
 */
public class AddKeyWordListener implements ActionListener {
	/**
	 * Fenetre ou l'on va rajouter le mot-clef
	 */
	private FrameKeyWords fkw;
	/**
	 * Champ ou l'on va recuperer le mot-clef
	 */
	private JTextField keyWordsField;
	/**
	 * Liste des mot-clefs a actualiser
	 */
	private List<String> keyWords;
	
	/**
	 * Constructeur 
	 * @param fkw : Fenetre ou l'on va rajouter le mot-clef
	 * @param keyWordsField : Champ ou l'on va recuperer le mot-clef
	 * @param keyWords : Liste des mot-clefs a actualiser
	 */
	public AddKeyWordListener(FrameKeyWords fkw, JTextField keyWordsField, List<String> keyWords) {
		this.fkw = fkw;
		this.keyWordsField = keyWordsField;
		this.keyWords = keyWords;
	}
	
	/**
	 * Ajoute le mot-clef
	 */
	public void actionPerformed(ActionEvent e) {
		if(keyWordsField.getText().equals("")) return;
		keyWords.add(keyWordsField.getText());
		fkw.addKeyWordToPanel(keyWordsField.getText());
		keyWordsField.setText("");
		keyWordsField.requestFocus();
	}

}