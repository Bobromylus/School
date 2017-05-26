package functionality;

public class NejblizsiSoused implements Klasifikator{
	private Prepravka prepravka;
	private double presnost = 0;
	private Vzdalenost vzdalenost;

	public NejblizsiSoused(Prepravka prepravka, Vzdalenost vzdalenost) {
		this.prepravka = prepravka;
		this.vzdalenost = vzdalenost;
	}

	public void klasifikuj() {
		double dRozdil = 0;
		double rozdil = Double.MAX_VALUE;

		for (TextRep testovaci : prepravka.getTestovaciCat()) {
			dRozdil = 0;
			rozdil = Double.MAX_VALUE;
			for (TextRep trenovaci : prepravka.getTrenovaciCat()) {
				dRozdil = vzdalenost.vzdalenost(testovaci, trenovaci);
				if (dRozdil < rozdil) {
					rozdil = dRozdil;
					testovaci.setUrceni(trenovaci.getCategorie());
				}
			}
		}

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
