package outils;

import java.time.Duration;
import java.time.Instant;

public class Timer {
	
	private Instant tempsDebut;
	private String libelle;

	private Timer(String libelle) {
		tempsDebut = Instant.now();
		this.libelle = libelle;
	}
	
	public static Timer getTimer(String libelle) {
		return new Timer(libelle);
	}

	public void afficher() {
		Instant tempsIntermediaire = Instant.now();
		Duration duree = Duration.between(tempsDebut, tempsIntermediaire);
		long nbSecondes = duree.getSeconds();
		long nbMillisecondes = duree.toMillisPart();
		String titre = libelle != null ? libelle : "Temps";
		System.out.println(String.format("%s : %d.%ds", titre, nbSecondes, nbMillisecondes));
	}
	
}
