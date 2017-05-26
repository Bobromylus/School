package functionality;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Prepravka implements Serializable {
	private Map<Path, TextRep> testovaciData = new HashMap<Path, TextRep>();
	private Map<Path, TextRep> trenovaciData = new HashMap<Path, TextRep>();
	private HashSet<String> testovaciSlova = new HashSet<String>();
	private HashSet<String> trenovaciSlova = new HashSet<String>();
	private ArrayList<TextRep> testovaciNN = new ArrayList<TextRep>();
	private ArrayList<TextRep> trenovaciNN = new ArrayList<TextRep>();
	private Map<String,Categorie> testovaciBayes  = new HashMap<String,Categorie>();
	private Map<String,Categorie> trenovaciBayes  = new HashMap<String,Categorie>();
	private ObservableMap<Path, TextRep> testDataGui = FXCollections.observableMap(testovaciData);
	private ObservableMap<Path, TextRep> trenDataGui = FXCollections.observableMap(trenovaciData);
	private NejblizsiSoused soused;
	private Bayes bayes;
	
	
	
	
	public Prepravka(PrepravkaSaveLoad p) {
		p.testovaciData.forEach((k,v)-> this.testovaciData.put(Paths.get(k),v));
		p.trenovaciData.forEach((k,v)-> this.trenovaciData.put(Paths.get(k),v));
		this.testovaciSlova = p.testovaciSlova;
		this.trenovaciSlova = p.trenovaciSlova;
		this.testovaciNN = p.testovaciNN;
		this.trenovaciNN = p.trenovaciNN;
		this.testovaciBayes = p.testovaciBayes;
		this.trenovaciBayes = p.trenovaciBayes;
		this.testDataGui = FXCollections.observableMap(testovaciData);
		this.trenDataGui = FXCollections.observableMap(trenovaciData);
		this.soused = p.soused;
		this.bayes = p.bayes;
	}
	
	public Prepravka() {
		
	}
	public NejblizsiSoused getSoused() {
		return soused;
	}
	public Bayes getBayes() {
		return bayes;
	}
	public void setSoused(NejblizsiSoused soused) {
		this.soused = soused;
	}
	public void setBayes(Bayes bayes) {
		this.bayes = bayes;
	}
	public Map<Path, TextRep> getTestovaciData() {
		return testovaciData;
	}
	public void setTestovaciData(HashMap<Path, TextRep> testovaciData) {
		this.testovaciData = testovaciData;
	}
	public Map<Path, TextRep> getTrenovaciData() {
		return trenovaciData;
	}
	public void setTrenovaciData(HashMap<Path, TextRep> trenovaciData) {
		this.trenovaciData = trenovaciData;
	}
	public HashSet<String> getTrenovaciSlova() {
		return trenovaciSlova;
	}
	public void setTrenovaciSlova(HashSet<String> trenovaciSlova) {
		this.trenovaciSlova = trenovaciSlova;
	}
	public HashSet<String> getTestovaciSlova() {
		return testovaciSlova;
	}
	public void setTestovaciSlova(HashSet<String> testovaciSlova) {
		this.testovaciSlova = testovaciSlova;
	}
	public ArrayList<TextRep> getTestovaciCat() {
		return testovaciNN;
	}
	public ArrayList<TextRep> getTrenovaciCat() {
		return trenovaciNN;
	}
	public void setTestovaciCat(ArrayList<TextRep> testovaciCat) {
		this.testovaciNN = testovaciCat;
	}
	public void setTrenovaciCat(ArrayList<TextRep> trenovaciCat) {
		this.trenovaciNN = trenovaciCat;
	}

	
	public Map<String, Categorie> getTestovaciBayes() {
		return testovaciBayes;
	}
	public Map<String, Categorie> getTrenovaciBayes() {
		return trenovaciBayes;
	}
	public void setTestovaciBayes(Map<String, Categorie> testovaciBayes) {
		this.testovaciBayes = testovaciBayes;
	}
	public void setTrenovaciBayes(Map<String, Categorie> trenovaciBayes) {
		this.trenovaciBayes = trenovaciBayes;
	}
	public ObservableMap<Path, TextRep> getTestDataGui() {
		return testDataGui;
	}
	public ObservableMap<Path, TextRep> getTrenDataGui() {
		return trenDataGui;
	}
	public void setTestDataGui(ObservableMap<Path, TextRep> testDataGui) {
		this.testDataGui = testDataGui;
	}
	public void setTrenDataGui(ObservableMap<Path, TextRep> trenDataGui) {
		this.trenDataGui = trenDataGui;
	}
	public void resetTestData(){
		Set<Path> klice = testovaciData.keySet();
		for(Path klic : klice){
			testovaciData.get(klic).setSpravne(false);
			testovaciData.get(klic).setUrceni(null);
		}
	}


	
}
