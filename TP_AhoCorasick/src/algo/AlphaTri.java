package algo;

public class AlphaTri {
	
	public static void tri(char matrix[][], int column, int inf, int max) {
		triBulles(matrix, column, inf, max);
		int c = 1;
		int copieInf = inf;
		int copieMax = max;
		inf = 0;
		max = 0;
		
		for(int k = copieInf; k < copieMax; k++) {
			if(matrix[k+1][column] != matrix[k][column]) {
				if(c == 1)
					inf = k+1;
				else
					max = k+1;
				c = 1;
			}
			else {
				max = k+1;
				c++;
			}
			if(max > inf){
				tri(matrix, column+1, inf, max);
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
	
	private static void triBulles(char matrix[][], int column, int inf, int max) {
			int change;
			while(true) {
				change = 0;
				int i = inf;
				while(i < max - inf) {
					if((matrix[i][column]) > (matrix[i+1][column])) {
						echangeLigne(matrix, i);
						change = 1;
					}
					i++;
				} 
				if(change == 0) break;
			}
	}
	
	public static void main(String[] args) {
		char[][] matrix = {{'A', 'B', 'C', 'D'}, {'A', 'C', 'D', 'D'}, {'A', 'C', 'D', 'C'}, {'A', 'B', 'C', 'D'}};
		triBulles(matrix, 0, 0, 3);
		for(char[] tab : matrix) {
			for(char c : tab)
				System.out.print(c);
			System.out.println();
		}
	}

}
