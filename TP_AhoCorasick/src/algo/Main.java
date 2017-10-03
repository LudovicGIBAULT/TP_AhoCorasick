package algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	 * @param keyWords
	 * @return
	 */
	private static ArrayList<Character> calculNbLettres(List<String> keyWords) {
		ArrayList<Character> listCharUsed = new ArrayList<Character>();
		
		for(int i = 0; i < keyWords.size(); i++) {
			for(int j = 0; j < keyWords.get(i).length(); j++) {
				if(listCharUsed.contains(keyWords.get(i).charAt(j))) continue;
				
				listCharUsed.add(keyWords.get(i).charAt(j));
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
		

		Map<String, List<int[]>> mapKeyWordsOccurences = new HashMap<String, List<int[]>>();
		String text = texte.toUpperCase();
		for(int i = 0; i < keyWords.size(); i++)
			keyWords.set(i, keyWords.get(i).toUpperCase());
		
		eventPosition = 0;
		count = 0;
		List<String> events = getListEvents(keyWords);
		List<Character> charsList = calculNbLettres(keyWords);
		
		int[][] commandBoard = createCommandBoard(events, charsList);
		
		int[] defeatTable = createDefeatTable(commandBoard, events);
		
		
		//Ici on commence le parcourt du texte tout en avançant dans la table de commande
		for(index = 0; index < text.length(); index++) {
			if(charsList.indexOf(text.charAt(index)) > -1)
			{	
				if(commandBoard[charsList.indexOf(text.charAt(index))][eventPosition] == 0)
					eventPosition = goToDefeat(defeatTable, events, keyWords, mapKeyWordsOccurences);
				eventPosition = commandBoard[charsList.indexOf(text.charAt(index))][eventPosition]; 
				String currentEvent;
				if(eventPosition > 0 && keyWords.contains(currentEvent = events.get(eventPosition - 1))) {
					wordFound(currentEvent, mapKeyWordsOccurences);
				}
			}
			else
				eventPosition = goToDefeat(defeatTable, events, keyWords, mapKeyWordsOccurences);
		}
		

		
		/*
		 * Ici on ajoute tous les mot-clefs contenus dans ceux trouves
		 */
		List<String> listKeyWordsToAdd = new ArrayList<String>();
		Map<String, List<int[]>> temporaryMapKeyWordsOccurences = new HashMap<String, List<int[]>>();
		
		for(String keyWordToFind : keyWords) {
			if(listKeyWordsToAdd.contains(keyWordToFind)) continue;
			for(String keyWord : mapKeyWordsOccurences.keySet()) {
				if(keyWord.length() > keyWordToFind.length() && keyWord.contains(keyWordToFind)) {
					
					if(!temporaryMapKeyWordsOccurences.containsKey(keyWordToFind))
						temporaryMapKeyWordsOccurences.put(keyWordToFind, new ArrayList<int[]>());
					for(int[] occurence : mapKeyWordsOccurences.get(keyWord)) {
						int  newOccurence[] = new int[2];
						newOccurence[0] = occurence[0]+keyWord.indexOf(keyWordToFind);
						newOccurence[1] = newOccurence[0]+keyWordToFind.length();
						temporaryMapKeyWordsOccurences.get(keyWordToFind).add(newOccurence);
						count++;
					}
				}
			}
		}
		
		
		for(String keyWord : temporaryMapKeyWordsOccurences.keySet()) {
			if(!mapKeyWordsOccurences.containsKey(keyWord))
				mapKeyWordsOccurences.put(keyWord, temporaryMapKeyWordsOccurences.get(keyWord));
			else
				mapKeyWordsOccurences.get(keyWord).addAll(temporaryMapKeyWordsOccurences.get(keyWord));
		}
		
		return mapKeyWordsOccurences;
	} //Search()
	
	/**
	 * Ajoute le mot trouve ainsi que l'occurence a la Map donnee
	 * 
	 * @param currentEvent
	 * @param listKeyWordsIndex
	 */
	private static void wordFound(String currentEvent, Map<String, List<int[]>> listKeyWordsIndex) {
		if(listKeyWordsIndex.get(currentEvent) == null)
			listKeyWordsIndex.put(currentEvent, new ArrayList<int[]>());
		listKeyWordsIndex.get(currentEvent).add(new int[2]);
		listKeyWordsIndex.get(currentEvent).get(listKeyWordsIndex.get(currentEvent).size()-1)[0] = index-currentEvent.length()+1;
		listKeyWordsIndex.get(currentEvent).get(listKeyWordsIndex.get(currentEvent).size()-1)[1] = index+1;
		count++;
	}
	
	/**
	 * Fais les actions correspondantes en cas d'echec et renvoie la position que devrait avoir l'evenement apres cela
	 * 
	 * @param defeatTable
	 * @param events
	 * @param keyWords
	 * @return
	 */
	public static int goToDefeat(int[] defeatTable, List<String> events, List<String> keyWords, Map<String, List<int[]>> listKeyWordsIndex) {
		eventPosition = defeatTable[eventPosition];
		String currentEvent;
		// Si apres l'echec on tombe sur un mot-clef on l'ajoute :
		if(eventPosition > 0 && keyWords.contains(currentEvent = events.get(eventPosition - 1))) {
			wordFound(currentEvent, listKeyWordsIndex);
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
			for(int j = i+1; j < events.size(); j++) {
				if(events.get(j).length() == events.get(i).length()+1
						&& events.get(j).startsWith(events.get(i)))
					commandBoard[charsList.indexOf(events.get(j).charAt(events.get(j).length()-1))][i+1] = j+1;
			}
		}
		
		
		return commandBoard;
	} //createCommandBoard()
		
}