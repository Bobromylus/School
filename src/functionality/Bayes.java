package functionality;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bayes implements Klasifikator{
	private Prepravka prepravka;
	private double presnost = 0;
	private Vzdalenost vzdalenost;
	private ArrayList<Tabulka> bayes;

	public Bayes(Prepravka prepravka, Vzdalenost vzdalenost) {
		this.prepravka = prepravka;
		this.vzdalenost = vzdalenost;
		int pocetCategorii = prepravka.getTrenovaciBayes().size();

		ArrayList<Tabulka> bayes = new ArrayList<Tabulka>();

		prepravka.getTrenovaciBayes().forEach((k, v) -> {
			double pravdepodobnost = 0;
			double vyskyt = 0;
			Map<String, Double> pravdepodobnosti = new HashMap<String, Double>();
			pravdepodobnost = (double) v.getPocetTextu() / pocetCategorii;
			pravdepodobnosti.put("Categorie", pravdepodobnost);

			for (String slovo : prepravka.getTrenovaciSlova()) {
				vyskyt = 0;
				if (v.getCetnost().containsKey(slovo)) {
					vyskyt = vzdalenost.pravdepodobnost(v, slovo);
				}
				pravdepodobnost = (vyskyt + 1)
						/ (prepravka.getTrenovaciSlova().size() + v.getPravdepodobnost(vzdalenost));
				pravdepodobnosti.put(slovo, pravdepodobnost);
			}
			Tabulka tabulka = new Tabulka(v.getCategorie(), pravdepodobnosti);
			bayes.add(tabulka);
		});
		this.bayes = bayes;
	}

	public void klasifikuj() {

		prepravka.getTestovaciCat().forEach(test -> {
			double pMaximalni = -Double.MAX_VALUE;
			for(Tabulka tab : bayes){
				double pravde = 0;
				for (String k : test.getCetnost().keySet()) {
					if (tab.getTabulka().containsKey(k)) {
						pravde += Math.log(tab.getTabulka().get(k));
					}
				}
				pravde += Math.log(tab.getTabulka().get("Categorie"));
				if (pravde > pMaximalni) {
					pMaximalni = pravde;
					test.setUrceni(tab.getCategorie());
				}
			};
		});

		double correct = 0;
		double uncorrect = 0;
		double acc = 0;

		for (TextRep testing : prepravka.getTestovaciCat()) {
			if (testing.getUrceni().equals(testing.getCategorie())) {
				correct++;
				testing.setSpravne(true);
			} else {
				uncorrect++;
			}
		}

		acc = correct / (correct + uncorrect);

		this.presnost = acc * 100;
	}

	public Prepravka getPrepravka() {
		return prepravka;
	}

	public double getPresnost() {
		return presnost;
	}

	public Vzdalenost getVzdalenost() {
		return vzdalenost;
	}

	public void setPrepravka(Prepravka prepravka) {
		this.prepravka = prepravka;
	}

	public void setPresnost(double presnost) {
		this.presnost = presnost;
	}

	public void setVzdalenost(Vzdalenost vzdalenost) {
		this.vzdalenost = vzdalenost;
	}

}

class Tabulka implements Serializable {

	private Map<String, Double> tabulka;
	private String categorie;

	Tabulka(String categorie, Map<String, Double> tabulka) {
		this.tabulka = tabulka;
		this.categorie = categorie;
	}

	public Map<String, Double> getTabulka() {
		return tabulka;
	}

	public String getCategorie() {
		return categorie;
	}

}
