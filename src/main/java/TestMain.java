import java.io.IOException;
import java.util.List;

import mathematiques.Fonction;
import mathematiques.Simulations;
import outils.PairDouble;
import outils.Timer;

public class TestMain {

	private static final String DOSSIER = "./courbes/";


	public static void main(String[] args) throws IOException {
		
			List<PairDouble> listeMuSigma = List.of(
					PairDouble.of(5, 0.5),
					PairDouble.of(8.5, 0.75),
//					PairDouble.of(1.5, 3),
					PairDouble.of(-1, 1));
			
			Fonction fu = Fonction.getFonctionUniforme(1, 3);

			List<Fonction> listeFonctions = List.of(
					Fonction.getGaussienne(5, 0.5),
					Fonction.getGaussienne(8.5, 0.75),
					Fonction.getGaussienne(-1, 1),
					fu,
					Fonction.getFonctionUniforme(-3, 10),
					Fonction.getLoiExponentielle(2, 2)
					);
			
			
			Fonction fg = Fonction.getSommeGaussiennes(listeMuSigma);
			
			Fonction f = Fonction.getSommeFonctions(listeFonctions);
			
			
			final String NOM_FICHIER_FONCTION_DENSITE = "fonction2.dat";
			final String NOM_FICHIER_SIMULATION = "simulation4.dat";
			final String NOM_FICHIER_HISTOGRAMME_INTEGRALE = "histo4.dat";
			final String NOM_FICHIER_HISTOGRAMME_RANDOM = "histor4.dat";
			
			final int NB_POINTS_SIMULATION = 1_000_000;
			final int NB_POINTS_DENSITE = 1_01;
			final int NB_CLASSES_HISTOGRAMME_DENSITE = 1_000;
			final int NB_CLASSES_HISTOGRAMME_RANDOM = 10_00;

			
			System.out.println("Intégrale : " + Simulations.estimerIntegrale(DOSSIER + NOM_FICHIER_HISTOGRAMME_INTEGRALE, f, NB_CLASSES_HISTOGRAMME_DENSITE));
			
			Simulations.ecrireFichier(DOSSIER + NOM_FICHIER_FONCTION_DENSITE, f, NB_POINTS_DENSITE);

			Timer timerSimulation = Timer.getTimer("Simulation de " + NB_POINTS_SIMULATION + " tirages aléatoires");
			Simulations.ecrireFichierSimulationAleatoire(DOSSIER + NOM_FICHIER_SIMULATION, f, NB_POINTS_SIMULATION);
			timerSimulation.afficher();

			
			Timer timerHisto = Timer.getTimer(String.format("Calcul d'histogramme avec %d points et %d histogrammes", NB_POINTS_SIMULATION, NB_CLASSES_HISTOGRAMME_RANDOM));
			Simulations.ecrireFichierHistogrammeAleatoire(DOSSIER + NOM_FICHIER_HISTOGRAMME_RANDOM, f, NB_POINTS_SIMULATION, NB_CLASSES_HISTOGRAMME_RANDOM);
			timerHisto.afficher();

			System.out.println("Fin");

	}
}
