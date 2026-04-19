package mathematiques;

import java.util.List;
import java.util.function.DoubleUnaryOperator;

import outils.PairDouble;

public class Fonction {

	private static final double NB_SIGMA = Constantes.NB_SIGMA_BORNE;

	DoubleUnaryOperator fonction;
	private double min;
	private double max;

	public Fonction(DoubleUnaryOperator fonction, double min, double max) {
		this.fonction = fonction;
		this.min = min;
		this.max = max;
	}

	/**
	 * Additionne toutes les fonctions, et divise les valeurs par le nombre de fonctions
	 */
	public static Fonction getSommeFonctions(List<Fonction> listeFonctions) {
		final int nbFonctions = listeFonctions.size();
		double min = listeFonctions.get(0).getXMin();
		double max = listeFonctions.get(0).getXMax();
		for (Fonction f : listeFonctions) {
			if(f.getXMin() < min) {
				min = f.getXMin();
			}
			if(f.getXMax() > max) {
				max = f.getXMax();
			}
		}
		DoubleUnaryOperator fonction = x -> {
			double somme = 0;
			for (Fonction f : listeFonctions) {
				somme += f.valeur(x);
			}
			return somme / nbFonctions;
		};
		return new Fonction(fonction, min, max);
	}

	public static Fonction getGaussienne(double mu, double sigma) {
		return new Fonction(Fonctions.loi_normale(mu, sigma), mu - NB_SIGMA*sigma, mu + NB_SIGMA*sigma);
	}

	public static Fonction getSommeGaussiennes(List<PairDouble> listeMuSigma) {
		double min = listeMuSigma.get(0).getX();
		double max = listeMuSigma.get(0).getX();
		for (PairDouble muSigma : listeMuSigma) {
			double muMoinsNSigma = muSigma.getX() - muSigma.getY() * NB_SIGMA;
			if(muMoinsNSigma < min) {
				min = muMoinsNSigma;
			}
			double muPlusNSigma = muSigma.getX() + muSigma.getY() * NB_SIGMA;
			if(muPlusNSigma > max) {
				max = muPlusNSigma;
			}
		}
		return new Fonction(Fonctions.sommeLoisNormales(listeMuSigma), min, max);
	}

	public double getXMin() {
		return min;
	}

	public double getXMax() {
		return max;
	}

	public double valeur(double x) {
		return fonction.applyAsDouble(x);
	}

	public static Fonction getFonctionUniforme(double min, double max) {
		double largeur = max - min;
		return new Fonction(x -> (x >= min && x <= max) ? 1 / largeur : 0, min, max);
	}

	public static Fonction getLoiExponentielle(double lambda, double decalage) {

		return new Fonction(x -> {
			double X = x - decalage;
			return X >= 0 ? lambda * Math.exp(- lambda * X) : 0;
		}, 0, 1/lambda * 8);
	}

}
