package ihm;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import algo.Main;

/**
 * IHM principale du programme
 * 
 * @author Ludovic GIBAULT et Jean-Baptiste BRUN
 *
 */
public class IHM extends JFrame {
	
	/**
	 * Liste des mot-clefs
	 */
	private List<String> keyWords = new ArrayList<String>();
	/**
	 * Zone de texte ou l'on va chercher les mot-clef
	 */
	private JTextArea textToSearchPanel = new JTextArea();
	/**
	 * Zone de texte affichant le resultat de la recherche (ancien texte plus un caractere pour montrer
	 * ou l'on a trouver le mot
	 */
	private JTextArea textResultPanel = new JTextArea();
	/**
	 * Map de toutes les occurences trouves par mot-clef
	 */
	private Map<String, List<int[]>> listKeyWordsIndex = new LinkedHashMap<String, List<int[]>>();
	
	/**
	 * constructeur de l'IHM
	 */
	public IHM() {
		super();
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		setContentPane(mainPanel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JButton searchButton = new JButton("Chercher");
		mainPanel.add(searchButton, BorderLayout.NORTH);
		
		searchButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				listKeyWordsIndex = Main.search(textToSearchPanel.getText(), keyWords);

				String texteToReturn = textToSearchPanel.getText();
				
				List<Integer> listOccurences = new ArrayList<Integer>();
				
				for(List<int[]> listIndex : listKeyWordsIndex.values()) {
					for(int[] i : listIndex) {
						listOccurences.add(i[0]);
					}
				}
				
				listOccurences.sort(new Comparator<Integer>() {

					public int compare(Integer o1, Integer o2) {
						return o1 < o2 ? -1 : 1;
					}
				});
				
				int it = 0;
				
				System.out.println(listOccurences);
				
				for(int occurence : listOccurences) {
						texteToReturn = texteToReturn.substring(0, occurence+it) + '►' + textToSearchPanel.getText().substring(occurence);
						it++;
				}
				
				/*
				int it = 0;
				for(List<int[]> listIndex : listKeyWordsIndex.values()) {
					for(int[] i : listIndex) {
						System.out.println("i[0] = " + i[0] + " i[1] = " + i[1] + " texte = " + textToSearchPanel.getText().length());
						texteToReturn = texteToReturn.substring(0, i[0]+it) + 'â–º' + textToSearchPanel.getText().substring(i[0], i[1]+1)
								        + textToSearchPanel.getText().substring(i[1]+1, textToSearchPanel.getText().length());
						it++;
					}
				}*/
				
				textResultPanel.setText(texteToReturn);		
			}
			
		}); //searchButton.addActionListener
		
		JPanel textPanel = new JPanel(new BorderLayout());
		
		mainPanel.add(textPanel, BorderLayout.CENTER);
		
		JScrollPane scroll = new JScrollPane(textToSearchPanel);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(400, 500));
		textPanel.add(scroll, BorderLayout.WEST);
		textToSearchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		
		JScrollPane scroll2 = new JScrollPane(textResultPanel);
		scroll2.setPreferredSize(new Dimension(400, 500));
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textPanel.add(scroll2, BorderLayout.EAST);
		textResultPanel.setBackground(Color.LIGHT_GRAY);
		textResultPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textResultPanel.setEditable(false);
		textResultPanel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		JPanel keyWordsPanel = new JPanel();
		mainPanel.add(keyWordsPanel, BorderLayout.SOUTH);
		keyWordsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
				
		JButton accessToKeyWordsButton = new JButton("Mots-clefs");
		keyWordsPanel.add(accessToKeyWordsButton);
		
		accessToKeyWordsButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				FrameKeyWords frameKeyWords = new FrameKeyWords(keyWords);
				int result = JOptionPane.showConfirmDialog(null, frameKeyWords, "Mots-clefs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(result != JOptionPane.CANCEL_OPTION) {
					keyWords = frameKeyWords.getKeyWords();
				}
			}
		});
		
		JButton fileChooserButton = new JButton("Importer fichier");
		keyWordsPanel.add(fileChooserButton);
		
        fileChooserButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
	            JFileChooser dialogue = new JFileChooser();
	            int result = dialogue.showDialog(null, "Importer");
	            
	            if(result != JFileChooser.CANCEL_OPTION) {
	            	File fileSelected = dialogue.getSelectedFile();
	            	BufferedReader br = null;

	                try {
	                	br = new BufferedReader(new InputStreamReader(new FileInputStream(fileSelected), "UTF8"));

						 String str;
						 
						 String text = "";
						 while ((str = br.readLine()) != null) {
							 	text += "\n" + str;
					         }
						 textToSearchPanel.setText(text);
						 
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, "Fichier inexistant", "ERREUR", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "erreur lors de la lecture du fichier", "ERREUR", JOptionPane.ERROR_MESSAGE);
					} finally {
							try {
								if(br !=null)
									br.close();
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "Erreur lors de la fermeture du fichier", "ERREUR", JOptionPane.ERROR_MESSAGE);
							}
	                }
				}
			}
		}); // fileChooserButton.addActionListener
        

        JButton detailsButton = new JButton("Details...");
        keyWordsPanel.add(detailsButton);
        
        detailsButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				new FrameDetails(listKeyWordsIndex);
				
			}
		});
		
		pack();
		setVisible(true);
	} //IHM()
	
	public static void main(String[] args) {
		new IHM();
	}
}