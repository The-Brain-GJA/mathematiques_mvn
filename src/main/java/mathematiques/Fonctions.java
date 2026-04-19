package mathematiques;

import java.util.List;
import java.util.function.DoubleUnaryOperator;

import outils.PairDouble;

public class Fonctions {

	public static double loi_normale(double x, double mu, double sigma) {
		
		double un_sur_racine_carree = 1.0 / Math.sqrt(2 * Math.PI * sigma * sigma);
		double argument_exp = - (x - mu) * (x - mu) / (2 * sigma * sigma);
		
		return un_sur_racine_carree * Math.exp(argument_exp);
	}

	public static DoubleUnaryOperator loi_normale(double mu, double sigma) {
		return x -> loi_normale(x, mu, sigma);
	}

	public static DoubleUnaryOperator sommeLoisNormales(List<PairDouble> listMuSigma) {
		final int nbFonctions = listMuSigma.size();
		return x -> {
			double somme = 0.0;
			for (PairDouble muSigma : listMuSigma) {
				somme += loi_normale(x, muSigma.getX(), muSigma.getY());
			}
			return somme / nbFonctions;
		};
	}
	
}
