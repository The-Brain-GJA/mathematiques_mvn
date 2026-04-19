package mathematiques;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import outils.PairDouble;
import outils.TripleDouble;

public class Simulations {

	/**
	 * Renvoie l'ensemble des valeurs de la fonction sous la forme x, f(x), pour tous les x de l'intervalle de validité de la fonction
	 */
	public static List<PairDouble> listeValeur(Fonction fonction, int nbPoints) {
		List<PairDouble> resultats = new ArrayList<PairDouble>(nbPoints);

		double pas = (fonction.getXMax() - fonction.getXMin()) / (nbPoints-1);

		double x = fonction.getXMin();
		for(int i=0; i<nbPoints; i++) {
			resultats.add(PairDouble.of(x, fonction.valeur(x)));
			x += pas;
		}

		return resultats;
	}

	public static void ecrireFichier(String nomFichier, Fonction fonction, int nbPoints) throws IOException {

		List<PairDouble> resultats = listeValeur(fonction, nbPoints);

		try (PrintWriter printer = new PrintWriter(new FileWriter(nomFichier))) {
			resultats.forEach(point -> {
				printer.println(String.format(Locale.ROOT, "%.3f\t%.5f", point.getX(), point.getY()));
			});

		}
	}

	public static void ecrireFichierSimulationAleatoire(String nomFichier, Fonction fonction, int nbPoints) throws IOException {

		double[] valeurs = generateurAleatoireFonctionDensiteDichotomique(fonction, nbPoints);

		try (PrintWriter printer = new PrintWriter(new FileWriter(nomFichier))) {
			for (int i=0; i<valeurs.length; i++) {
				printer.println(String.format(Locale.ROOT, "%.5f\t%.5f", valeurs[i],0.0));
			}

		}
	}

	public static void ecrireFichierSimulationAleatoire2D(String nomFichier, Fonction fonction, int nbPoints) throws IOException {

		double[] valeurs = generateurAleatoireFonctionDensite(fonction, nbPoints);

		try (PrintWriter printer = new PrintWriter(new FileWriter(nomFichier))) {
			for (int i=0; i<valeurs.length; i+=2) {
				printer.println(String.format(Locale.ROOT, "%.5f\t%.5f", valeurs[i], valeurs[i+1]));
			}

		}
	}

	/**
	 * Triplet renvoyé : x, f(x), largeurIntervalle où x est le milieu de l'intervalle
	 */
	private static List<TripleDouble> histogrammes(Fonction fonction, int nbClassesHistogramme) {

		List<TripleDouble> liste = new ArrayList<>(nbClassesHistogramme);

		double pas = (fonction.getXMax() - fonction.getXMin()) / nbClassesHistogramme;
		double x = fonction.getXMin();
		for(int i=0; i<nbClassesHistogramme; i++) {
			double x1 = x;
			double x2 = x + pas;
			double milieu = (x1+x2)/2;
			liste.add(TripleDouble.of(milieu, fonction.valeur(milieu), pas));
			x += pas;
		}
		return liste;
	}

	public static double estimerIntegrale(Fonction fonction, int nbHistogrammes) {
		List<TripleDouble> listePoints = histogrammes(fonction, nbHistogrammes);
		double integrale = 0.0;
		for (TripleDouble point : listePoints) {
			integrale += point.getZ() * point.getY();
		}
		return integrale;
	}

	public static double estimerIntegrale(String nomFichier, Fonction fonction, int nbHistogrammes) throws IOException {
		List<TripleDouble> listePoints = histogrammes(fonction, nbHistogrammes);
		double integrale = 0.0;
		try (PrintWriter printer = new PrintWriter(new FileWriter(nomFichier))) {
			for (TripleDouble point : listePoints) {
				integrale += point.getZ() * point.getY();
				printer.println(String.format(Locale.ROOT, "%.3f\t%.5f", point.getX(), point.getY()));
			}
		}
		return integrale;
	}

	/**
	 * Renvoie nbValeurs générées aléatoirement, en fonction de la densité de probabilité donnée
	 */
	public static double[] generateurAleatoireFonctionDensite(Fonction fonction, int nbValeursAGenerer) {

		final int NB_ECHANTILLONS = Constantes.NB_ECHANTILLONS_GENERATEUR;
		double[] densite = densiteCumulee(fonction, NB_ECHANTILLONS);

		double[] valeurs = new double[nbValeursAGenerer];
		for(int i=0; i<nbValeursAGenerer; i++) {
			double random = Math.random();
			int j = 0;
			while(densite[j] < random && j < NB_ECHANTILLONS-1) {
				j++;
			}
			valeurs[i] = fonction.getXMin() + j * (fonction.getXMax() - fonction.getXMin()) / NB_ECHANTILLONS;
		}

		return valeurs;
	}

	/**
	 * Renvoie nbValeurs générées aléatoirement, en fonction de la densité de probabilité donnée
	 */
	public static double[] generateurAleatoireFonctionDensiteDichotomique(Fonction fonction, int nbValeursAGenerer) {

		final int NB_ECHANTILLONS = Constantes.NB_ECHANTILLONS_GENERATEUR;
		double[] densite = densiteCumulee(fonction, NB_ECHANTILLONS);

		double[] valeurs = new double[nbValeursAGenerer];
		for(int i=0; i<nbValeursAGenerer; i++) {
			double random = Math.random();
			int min = 0;
			int max = NB_ECHANTILLONS-1;
			int j = (min+max) / 2;
			while(min < max-1 && j != min && j != max) {
				if(densite[j] < random) {
					min = j;
				} else {
					max = j;
				}
				j = (min+max) / 2;
			}
			valeurs[i] = fonction.getXMin() + j * (fonction.getXMax() - fonction.getXMin()) / NB_ECHANTILLONS;
		}
		return valeurs;
	}

	/**
	 * Génère des points aléatoirement, puis les regroupe dans un histogramme, et l'écrit dans un fichier
	 */
	public static void ecrireFichierHistogrammeAleatoire(String nomFichier, Fonction fonction, int nbPointsGeneres, int nbClassesHistogramme) throws IOException {

		double[] valeurs = generateurAleatoireFonctionDensiteDichotomique(fonction, nbPointsGeneres);

		// Calcul des histogrammes
		double pas = (fonction.getXMax() - fonction.getXMin()) / nbClassesHistogramme;

		PairDouble[] histogrammes = calculerHistogrammes(valeurs, fonction.getXMin(), fonction.getXMax(), nbClassesHistogramme);

		try (PrintWriter printer = new PrintWriter(new FileWriter(nomFichier))) {
			for (int i=0; i<histogrammes.length; i++) {
				printer.println(String.format(Locale.ROOT, "%.5f\t%.5f", histogrammes[i].getX(), histogrammes[i].getY()));
			}
		}
	}

	public static PairDouble[] calculerHistogrammes(double[] valeurs, double min, double max, int nbClasses) {

		double pas = (max - min) / nbClasses;

		PairDouble[] histogrammes = new PairDouble[nbClasses];

		Arrays.sort(valeurs);

		double x1 = min;
		double x2 = x1 + pas;
		for(int i=0; i<nbClasses; i++) {

			double borneInf = x1;
			double borneSup = x2;
			// Nombre de points dans la classe
			long nbPoints = nbPointsDansIntervalle(valeurs, borneInf, borneSup);

			double milieu = (x1+x2)/2;
			histogrammes[i] = PairDouble.of(milieu, nbPoints);

			x1 += pas;
			x2 += pas;
		}
		return histogrammes;
	}

	/**
	 * Compte le nombre de valeurs entre les bornes inférieures et supérieures.
	 * Le tableau doit être trié.
	 */
	public static long nbPointsDansIntervalle(double[] valeurs, double borneInf, double borneSup) {

		// 1. trouver une valeur dans l'intervalle
		int min = 0;
		int max = valeurs.length -1;
		int i = 0;
		boolean continuer = true;
		
		while(continuer) {
			i = (min + max) / 2;
			if(valeurs[i] < borneInf) {
				min = i;
			} else if(valeurs[i] > borneSup) {
				max = i;
			} else {
				continuer = false;
			}
			if(max-min <= 1) {
				continuer = false;
			}
		}

		if(valeurs[i] < borneInf || valeurs[i] > borneSup) {
			return 0;
		}

		long nbPoints = 0;
		int pivot = i;

		// 2. ajouter le nombre d'éléments avant
		while(i  >= 0 && valeurs[i] >= borneInf) {
			i--;
			nbPoints++;
		}

		// 3. ajouter le nombre d'éléments après
		i = pivot + 1;
		while(i  < valeurs.length && valeurs[i] <= borneSup) {
			i++;
			nbPoints++;
		}

		return nbPoints;
	}

	private static double[] densiteCumulee(Fonction fonction, int nbValeursEchantillonnage) {

		List<TripleDouble> listePoints = histogrammes(fonction, nbValeursEchantillonnage);
		double[] tableauDensiteCumulee = new double[nbValeursEchantillonnage];

		tableauDensiteCumulee[0] = 0.0;
		final double largeurIntervalle = listePoints.get(0).getZ();
		for (int i=1; i<listePoints.size(); i++) {
			tableauDensiteCumulee[i] = tableauDensiteCumulee[i-1] + listePoints.get(i).getY() * largeurIntervalle;
		}

		return tableauDensiteCumulee;

	}
}
