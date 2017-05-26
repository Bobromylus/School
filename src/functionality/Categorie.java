package functionality;

import java.io.Serializable;
import java.util.HashMap;

public class Categorie implements Serializable{
	private HashMap<String, Integer> cetnost = new HashMap<String, Integer>();
	private int pocetTextu;
	private int pravdepodobnost;
	private String categorie;
	
	public Categorie(String categorie) {
		this.pocetTextu = 0;
		this.pravdepodobnost = 0;
		this.categorie = categorie;
	}
	
	public void pridej(TextRep text){
		text.getCetnost().forEach((k,v)-> cetnost.merge(k, v, (v1, v2) -> v1 + v2));
		this.pocetTextu++;
	}
	
	public void vypocitej(Vzdalenost vz){
		cetnost.forEach((k,v) -> pravdepodobnost += vz.pravdepodobnost(this,k));
	}

	public HashMap<String, Integer> getCetnost() {
		return cetnost;
	}

	public int getPocetTextu() {
		return pocetTextu;
	}

	public int getPravdepodobnost(Vzdalenost vz) {
		if(pravdepodobnost == 0){
			vypocitej(vz);
		}
		return pravdepodobnost;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCetnost(HashMap<String, Integer> cetnost) {
		this.cetnost = cetnost;
	}

	public void setPocetTextu(int pocetTextu) {
		this.pocetTextu = pocetTextu;
	}

	public void setPravdepodobnost(int pravdepodobnost) {
		this.pravdepodobnost = pravdepodobnost;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	
	
}
