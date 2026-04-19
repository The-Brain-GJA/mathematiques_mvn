import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import mathematiques.Fonction;
import mathematiques.Simulations;
import outils.Timer;

public class TestMainAsymetrique {

	private static final String DOSSIER = "./courbes/";

	/*
	 * plot 'fonction5.dat' w l, 'histo5.dat' w boxes
	 * set term wxt 2
	 * plot 'histor5.dat' w boxes
	 */
	
	public static void main(String[] args) throws IOException {
	
			final String NOM_FICHIER_FONCTION_DENSITE = "fonction5.dat";
			final String NOM_FICHIER_SIMULATION = "simulation5.dat";
			final String NOM_FICHIER_HISTOGRAMME_INTEGRALE = "histo5.dat";
			final String NOM_FICHIER_HISTOGRAMME_RANDOM = "histor5.dat";

			final int NB_POINTS_SIMULATION = 1_000_000;
			final int NB_POINTS_DENSITE = 1_01;
			final int NB_CLASSES_HISTOGRAMME_DENSITE = 1_000;
			final int NB_CLASSES_HISTOGRAMME_RANDOM = 10_00;

			List<Fonction> listeFonctions = List.of(
					//Fonction.getGaussienne(5, 0.5),
					Fonction.getGaussienne(8.5, 0.75),
					Fonction.getLoiNormaleAsymetrique(4, 0, 5)
					);
			
			Fonction f = Fonction.getSommeFonctions(listeFonctions);

			Simulations.ecrireFichier(DOSSIER + NOM_FICHIER_FONCTION_DENSITE, f, NB_POINTS_DENSITE);
			System.out.println("Ecriture du fichier de densité " + NOM_FICHIER_FONCTION_DENSITE);

			System.out.println("Intégrale : " + Simulations.estimerIntegrale(DOSSIER + NOM_FICHIER_HISTOGRAMME_INTEGRALE, f, NB_CLASSES_HISTOGRAMME_DENSITE));

			DecimalFormat df = new DecimalFormat("###,###,###");
			Timer timerSimulation = Timer.getTimer("Simulation de " + df.format(NB_POINTS_SIMULATION) + " tirages aléatoires dans le fichier " + NOM_FICHIER_SIMULATION);
			Simulations.ecrireFichierSimulationAleatoire(DOSSIER + NOM_FICHIER_SIMULATION, f, NB_POINTS_SIMULATION);
			timerSimulation.afficher();

			Timer timerHisto = Timer.getTimer(String.format("Calcul d'histogramme avec %s points et %s histogrammes dans le fichier %s",
					df.format(NB_POINTS_SIMULATION), df.format(NB_CLASSES_HISTOGRAMME_RANDOM), NOM_FICHIER_HISTOGRAMME_RANDOM));
			Simulations.ecrireFichierHistogrammeAleatoire(DOSSIER + NOM_FICHIER_HISTOGRAMME_RANDOM, f, NB_POINTS_SIMULATION, NB_CLASSES_HISTOGRAMME_RANDOM);
			timerHisto.afficher();

			System.out.println("Fin");

	}
}
