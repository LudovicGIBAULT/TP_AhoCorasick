package algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Main {

	/**
	 * Defini la position actuelle de la recherche dans la table de commande
	 */
	private static int eventPosition = 0;
	/**
	 * Defini le nombre de mots trouves pendant la recherche
	 */
	private static int count = 0;
	/**
	 * Defini a quel index l'on est dans le texte
	 */
	private static int index = 0;
	
	
	public static int getCount() {
		return count;
	}
	
	/**
	 * Renvoie la liste de tous les evenements disponibles (sauf le 0)
	 * 
	 * @param keyWords : la liste des mot-clefs
	 * @return
	 */
	static ArrayList<String> getListEvents (List<String> keyWords) {
		ArrayList<String> liste = new ArrayList<String>();
		
		for(int i = 0; i < getLongerWord(keyWords); i++) {
			for(int j = 0; j < keyWords.size(); j++) {
				if(i < keyWords.get(j).length() && !liste.contains(keyWords.get(j).substring(0, i+1)))
					liste.add(keyWords.get(j).substring(0, i+1).toUpperCase());
			}
		}
		return liste;
	}
	
	//TODO: REMPLACER EVENTS PAR KEYWORDS
	/***
	 * Renvoie une liste de tous les caracteres presents dans la liste des evennements passe en parametre 
	 * 
	 * @param events
	 * @return
	 */
	private static ArrayList<Character> calculNbLettres(List<String> events) {
		ArrayList<Character> listCharUsed = new ArrayList<Character>();
		
		for(int i = 0; i < events.size(); i++) {
			for(int j = 0; j < events.get(i).length(); j++) {
				if(listCharUsed.contains(events.get(i).charAt(j))) continue;
				
				listCharUsed.add(events.get(i).charAt(j));
			}	
		}
		return listCharUsed;
	}
	
		
	/**
	 * Renvoie la taille du mot le plus long de la liste des mot-clefs
	 * 
	 * @param keyWords
	 * @return
	 */
	private static int getLongerWord(List<String> keyWords) {
		
		int maxLength = 0;
		
		for(String keyWord : keyWords) {
			if(maxLength < keyWord.length())
				maxLength = keyWord.length();
		}
			
		return maxLength;
	}
	
	@SuppressWarnings("unused")
	private static void alphaTri(char[][] matrice) {
		for(int i = 0; i < matrice.length - 1; i++) {
			for(int j = 0; j < matrice[i].length; j++) {
				if((int)matrice[i][j] > (int)matrice[i+1][j]) {
					echangeLigne(matrice, i);
					break;
				}
			}
		}
	}
	
	/**
	 * echange la ligne d'une matrice avec la suivante
	 * 
	 * @param matrice
	 * @param numLigne
	 */
	private static void echangeLigne(char matrice[][], int numLigne) {
		char[] val = matrice[numLigne];
		
		matrice[numLigne] = matrice[numLigne+1];
		matrice[numLigne+1] = val;
	}
	
	/**
	 * Creer et renvoie la table des echecs
	 * 
	 * @param commandBoard
	 * @param events
	 * @return
	 */
	private static int[] createDefeatTable(int[][] commandBoard, List<String> events) {
		
		int[] defeatTable = new int[events.size()+1];
		
		for(int i = 0; i < events.size(); i++) { 
			if(events.get(i).length() > 1) {
				for(int j = i; j > 0; j--) {
					if(events.get(i).substring(1).endsWith((events.get(j)))) {
						defeatTable[i+1] = j+1;
						break;
					}
				}
			}
		}
		
		/*
		for(int i = 0; i < events.size(); i++) { 
			if(events.get(i).length() > 2) {
				for(int j = i; j >= 0; j--) {
					System.out.println("i : " + events.get(i) + " j : " + events.get(j));
					if(events.get(i).endsWith(events.get(j)) &&
							events.get(i).length() > events.get(j).length()) {
						defeatTable[i+1] = j+1;
						System.out.println("DT2 : " + (j+1));
					}
				}
			}
		}
		*/
		
		return defeatTable;
	} //createDefeatTable()
	
	/**
	 * cherche toutes les occurences d'une liste de mot-clef dans un texte et les renvoie dans une map
	 * ayant comme clef le mot-clef associe
	 * 
	 * @param texte
	 * @param keyWords
	 * @return
	 */
	public static Map<String, List<int[]>> search(String texte, List<String> keyWords) {
		

		Map<String, List<int[]>> listKeyWordsIndex = new LinkedHashMap<String, List<int[]>>();
		String text = texte.toUpperCase();
		for(int i = 0; i < keyWords.size(); i++)
			keyWords.set(i, keyWords.get(i).toUpperCase());
		System.out.println(keyWords);
		
		eventPosition = 0;
		count = 0;
		List<String> events = getListEvents(keyWords);
		List<Character> charsList = calculNbLettres(events);
		
		int[][] commandBoard = createCommandBoard(events, charsList);
		
		int[] defeatTable = createDefeatTable(commandBoard, events);
		
		for(index = 0; index < text.length(); index++) {
			if(charsList.indexOf(text.charAt(index)) > -1)
			{	
				/*eventPosition = commandBoard[charsList.indexOf(text.charAt(index))][eventPosition]; 
				int j = index+1;
				while(j < text.length() && eventPosition != 0) {
					if(charsList.indexOf(text.charAt(j)) < 0) break;
					eventPosition = commandBoard[charsList.indexOf(text.charAt(j))][eventPosition]; 
					System.out.println("" + text.charAt(j) + eventPosition);
					String currentEvent; 
					if(eventPosition > 0 && keyWords.contains(currentEvent = events.get(eventPosition - 1))) {
						if(listKeyWordsIndex.get(currentEvent) == null)
							listKeyWordsIndex.put(currentEvent, new ArrayList<int[]>());
						listKeyWordsIndex.get(currentEvent).add(new int[2]);
						listKeyWordsIndex.get(currentEvent).get(listKeyWordsIndex.get(currentEvent).size()-1)[0] = j-currentEvent.length()+1;
						listKeyWordsIndex.get(currentEvent).get(listKeyWordsIndex.get(currentEvent).size()-1)[1] = j;
						count++;
					}
					j++;
				}*/
		
				
				if(commandBoard[charsList.indexOf(text.charAt(index))][eventPosition] == 0)
					eventPosition = goToDefeat(defeatTable, events, keyWords, listKeyWordsIndex);
				eventPosition = commandBoard[charsList.indexOf(text.charAt(index))][eventPosition]; 
				System.out.println("" + text.charAt(index) + eventPosition);
				/*if(eventPosition > 0) {
				String currentEvent = events.get(eventPosition - 1); 
					for(int j = 0; j < currentEvent.length(); j++) {
						String newCurrentEvent;
						if(keyWords.contains(newCurrentEvent = currentEvent.substring(j))) {
							System.out.println(newCurrentEvent);
							if(listKeyWordsIndex.get(newCurrentEvent) == null)
								listKeyWordsIndex.put(newCurrentEvent, new ArrayList<int[]>());
							listKeyWordsIndex.get(newCurrentEvent).add(new int[2]);
							listKeyWordsIndex.get(newCurrentEvent).get(listKeyWordsIndex.get(newCurrentEvent).size()-1)[0] = index-newCurrentEvent.length()+1;
							listKeyWordsIndex.get(newCurrentEvent).get(listKeyWordsIndex.get(newCurrentEvent).size()-1)[1] = index;
							System.out.println(listKeyWordsIndex);
							count++;
							
						}
					}					
				}*/
				String currentEvent;
				if(eventPosition > 0 && keyWords.contains(currentEvent = events.get(eventPosition - 1))) {
					System.out.println(currentEvent);
					if(listKeyWordsIndex.get(currentEvent) == null)
						listKeyWordsIndex.put(currentEvent, new ArrayList<int[]>());
					listKeyWordsIndex.get(currentEvent).add(new int[2]);
					listKeyWordsIndex.get(currentEvent).get(listKeyWordsIndex.get(currentEvent).size()-1)[0] = index-currentEvent.length()+1;
					listKeyWordsIndex.get(currentEvent).get(listKeyWordsIndex.get(currentEvent).size()-1)[1] = index;
					count++;
					
				}
			}
			else
				eventPosition = goToDefeat(defeatTable, events, keyWords, listKeyWordsIndex);
		}
		
		
		
		System.out.print("\t0");
		for(String str : events)
			System.out.print("\t" + str);
		System.out.println();
		
		for(int i = 0; i < commandBoard.length; i++) {
			System.out.print(charsList.get(i));
			for(int j = 0; j < commandBoard[i].length; j++) {
				System.out.print("\t" + commandBoard[i][j]);
			}
			System.out.println();
		}
		
		for(int i = 0; i < defeatTable.length; i++)
			System.out.println(" " + i + " : " + defeatTable[i]);
		
		/*int it = 0;
		for(List<int[]> listIndex : listKeyWordsIndex.values()) {
			for(int[] i : listIndex) {
				System.out.println("i[0] = " + i[0] + " i[1] = " + i[1] + " texte = " + texte.length());
				texteToReturn = texteToReturn.substring(0, i[0]+it) + '►' + texte.substring(i[0], i[1]+1) + texte.substring(i[1]+1, texte.length());
				it++;
			}
		}*/
		

		List<String> listKeyWordsToAdd = new ArrayList<String>();
		Map<String, List<int[]>> dub = new LinkedHashMap<String, List<int[]>>();
		
		for(String keyWordToFind : keyWords) {
			if(listKeyWordsToAdd.contains(keyWordToFind)) continue;
			for(String keyWord : listKeyWordsIndex.keySet()) {
				if(keyWord.length() > keyWordToFind.length() && keyWord.contains(keyWordToFind)) {
					
					if(!dub.containsKey(keyWordToFind))
						dub.put(keyWordToFind, new ArrayList<int[]>());
					for(int[] occurence : listKeyWordsIndex.get(keyWord)) {
						System.out.println(keyWordToFind);
						int  newOccurence[] = new int[2];
						newOccurence[0] = occurence[0]+keyWord.indexOf(keyWordToFind);
						newOccurence[1] = newOccurence[0]+keyWordToFind.length();
						dub.get(keyWordToFind).add(newOccurence);
						count++;
					}
				}
			}
		}
		
		
		for(String keyWord : dub.keySet()) {
			if(!listKeyWordsIndex.containsKey(keyWord))
				listKeyWordsIndex.put(keyWord, dub.get(keyWord));
			else
				listKeyWordsIndex.get(keyWord).addAll(dub.get(keyWord));
		}
		
		return listKeyWordsIndex;
	} //Search()
	
	/**
	 * Fais les actions correspondantes en cas d'echec et renvoie la position que devrait avoir l'evenement après cela
	 * 
	 * @param defeatTable
	 * @param events
	 * @param keyWords
	 * @return
	 */
	public static int goToDefeat(int[] defeatTable, List<String> events, List<String> keyWords, Map<String, List<int[]>> listKeyWordsIndex) {
		System.out.println("Defeat");
		eventPosition = defeatTable[eventPosition];
		String currentEvent;
		if(eventPosition > 0 && keyWords.contains(currentEvent = events.get(eventPosition - 1))) {
			System.out.println(currentEvent);			
			/*
			if(eventPosition == 0) {
				eventPosition = goToDefeat(defeatTable, events, keyWords, listKeyWordsIndex);
			    if(listKeyWordsIndex.get(currentEvent) == null)
					listKeyWordsIndex.put(currentEvent, new ArrayList<int[]>());
				listKeyWordsIndex.get(currentEvent).add(new int[2]);
				listKeyWordsIndex.get(currentEvent).get(listKeyWordsIndex.get(currentEvent).size()-1)[0] = index - currentEvent.length()+1;
				listKeyWordsIndex.get(currentEvent).get(listKeyWordsIndex.get(currentEvent).size()-1)[1] = index;
				System.out.println(listKeyWordsIndex);

				index  -= currentEvent.length();
			}*/

			
			count++;
		}
		return eventPosition;
	} //goToDefeat()
	
	
	/**
	 * creer et renvoie la table de commande
	 * 
	 * @param events
	 * @param charsList
	 * @return
	 */
	private static int[][] createCommandBoard(List<String> events, List<Character> charsList) {
		
		int[][] commandBoard = new int[charsList.size()][events.size()+1];
		
		int i = 0;
		// PREMIERE COLONNE 
		while(i < events.size() && events.get(i).length() == 1) {
			for(int j = 0; j < charsList.size(); j++)
				if(events.get(i).charAt(0) == charsList.get(j)) {
					commandBoard[j][0] = i+1;
				}
			i++;
		}
		
		// LA SUITE
		for(i = 0; i < events.size(); i++) {
			for(int j = 0; j < events.size(); j++) {
				if(events.get(j).length() == events.get(i).length()+1
						&& events.get(j).startsWith(events.get(i)))
					commandBoard[charsList.indexOf(events.get(j).charAt(events.get(j).length()-1))][i+1] = j+1;
			}
		}
		
		
		return commandBoard;
	} //createCommandBoard()
	

	/**
	 * Le main avec les tests
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		ArrayList<String> keyWords = new ArrayList<String>();
		keyWords.add("Chien");
		keyWords.add("Chat");
		keyWords.add("chein");
		keyWords.add("hi");
		//keyWords.add("TABLE");
		//keyWords.add("AB");
		
		
		
		
		/*keyWords.add("ANDES");
		keyWords.add("ANE");
		keyWords.add("ARTE");
		keyWords.add("AS");
		keyWords.add("CANE");
		keyWords.add("CARTABLE");
		keyWords.add("CARTE");
		keyWords.add("RTT");
		keyWords.add("TABLEAU");
		keyWords.add("TENIR");
		
		
		ArrayList<String> listeEvents = getListEvents(keyWords);
		
		int i = 1;
		
		for(String str : listeEvents) {
			System.out.println(i + " : " + str); 
			i++;
		}
			
		ArrayList<Character> listechar = calculNbLettres(listeEvents);
		
		
		int[][] commandBoard = createCommandBoard(listeEvents, listechar);
		
		int[] defeatTable = createDefeatTable(commandBoard, listeEvents);
		
		for(i = 0; i < defeatTable.length; i++)
			System.out.println(" " + i + " : " + defeatTable[i]);
		
		*/
		String texte = "Chat ChatchatCheinChien"; //"TABLEBLABLAN TABLAN";
		
	//	search(texte, keyWords);
		
		System.out.println(search(texte, keyWords));
		System.out.println(texte);

		System.out.println("count : " + count);
	}
}