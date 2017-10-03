package ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import algo.Main;

/**
 * Fenetre affichant les détails de la dernière recherche
 * 
 * @author Ludovic GIBAULT et Jean-Baptiste BRUN
 *
 */
public class FrameDetails extends JFrame{
	
	/**
	 * Contrusteur prenant la map des mot-clefs avec toute leurs occurences comme paramètre pour
	 * ensuite les afficher
	 * @param temporaryMapKeyWordsOccurences
	 */
	public FrameDetails( Map<String, List<int[]>> temporaryMapKeyWordsOccurences) {
		super("Details de la recherche");
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(mainPanel);
		
		mainPanel.add(new JLabel("Total d'occurences trouvees : " + Main.getCount()), BorderLayout.NORTH);
		
		
		JPanel detailsPanel = new JPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

		JScrollPane scroll = new JScrollPane(detailsPanel);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(400, 200));
		mainPanel.add(scroll, BorderLayout.CENTER);
		
		for(String keyWord : temporaryMapKeyWordsOccurences.keySet()) {
			JPanel keyWordPanel = new JPanel();
			JScrollPane scroll2 = new JScrollPane(keyWordPanel);
			scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroll2.setPreferredSize(new Dimension(400, 50));
			detailsPanel.add(scroll2);
			keyWordPanel.setBorder(BorderFactory.createTitledBorder(keyWord + " : " + temporaryMapKeyWordsOccurences.get(keyWord).size()));	
			for(int[] i : temporaryMapKeyWordsOccurences.get(keyWord)) {
					keyWordPanel.add(new JLabel("" + i[0]));
			}
		}
		
		pack();
		setVisible(true);
	}
}
