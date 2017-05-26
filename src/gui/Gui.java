package gui;

/********************************************************************************
 * @author Martin Formánek - A15B0029P
 * @version 1.0
 * @since 2017
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import functionality.Bayes;
import functionality.NejblizsiSoused;
import functionality.Prepravka;
import functionality.PrepravkaSaveLoad;
import functionality.TextRep;
import functionality.VzBinarniCetnost;
import functionality.VzCetnost;
import functionality.VzFiltrCetnost;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import utility.Main;

/**
 * Třída s vizuálním rozšířením aplikace.
 */
public class Gui extends Application {

	/** Hlavní náhled vstupu */
	private TextArea testTextArea;
	private TextArea trenTextArea;
	private TextArea log;
	private TextArea vstup;

	private static Prepravka prepravka = Main.getPrepravka();

	/** ListView slovníku */
	private ListView<Path> testDataView = new ListView<Path>();

	/** ListView slovníku */
	private ListView<Path> trenDataView = new ListView<Path>();

	public static final CountDownLatch latch = new CountDownLatch(1);
	public static Gui startUpTest = null;

	public static Gui waitForStartUpTest() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return startUpTest;
	}

	public static void setStartUpTest(Gui startUpTest0) {
		startUpTest = startUpTest0;
		latch.countDown();
	}

	public Gui() {
		setStartUpTest(this);
	}

	/*
	 * Spuštění grafického rozhraní aplikace.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane border = new BorderPane();
		border.setTop(menu());
		border.setCenter(loadTabPane());
		border.setRight(loadSidebar());

		testDataView.getItems().setAll(prepravka.getTestDataGui().keySet());
		prepravka.getTestDataGui().addListener((MapChangeListener<Path, TextRep>) change -> {
			testDataView.getItems().removeAll(change.getKey());
			if (change.wasAdded()) {
				testDataView.getItems().add(change.getKey());
			}
		});

		trenDataView.getItems().setAll(prepravka.getTrenDataGui().keySet());
		prepravka.getTrenDataGui().addListener((MapChangeListener<Path, TextRep>) change -> {
			trenDataView.getItems().removeAll(change.getKey());
			if (change.wasAdded()) {
				trenDataView.getItems().add(change.getKey());
			}
		});

		testDataView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (!testDataView.getItems().isEmpty()) {
					testTextArea.setText(prepravka.getTestDataGui()
							.get(testDataView.getSelectionModel().getSelectedItem()).toString());
				}

			}
		});

		trenDataView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (!trenDataView.getItems().isEmpty()) {
					trenTextArea.setText(prepravka.getTrenDataGui()
							.get(trenDataView.getSelectionModel().getSelectedItem()).toString());
				}
			}
		});

		// pridani panelu do sceny a pote do stage
		Scene scena = new Scene(border, 900, 750);
		primaryStage.setScene(scena);

		// nastaveni jmena aplikace
		primaryStage.setTitle("Semestrální práce KIV/UIR");

		// osetreni minimalni sirky
		primaryStage.setMinWidth(350);
		primaryStage.setMinHeight(350);

		// zobrazeni stage na obrazovku
		primaryStage.show();
	}

	/**
	 * Nastavení bloku menu.
	 *
	 * @return blok menu
	 */
	private Node menu() {
		MenuBar menuBar = new MenuBar();
		// polozky v menu
		Menu nacteni = new Menu("S_oubor");
		MenuItem vstupS = new MenuItem("Vstup _Testovacích dat");
		MenuItem vstupK = new MenuItem("_Vstup Trenovacích dat");
		MenuItem save = new MenuItem("_Ulož model");
		MenuItem konec = new MenuItem("_Konec");

		// nastaveni funkcnosti tlacitek
		vstupS.setOnAction(actionEvent -> {
			DirectoryChooser vyber = new DirectoryChooser();
			vyber.setTitle("Vyberte složku k načtení Testovacích dat");
			// vytvoreni dialogu
			File fileDir = vyber.showDialog(testTextArea.getScene().getWindow());
			if (fileDir != null) { // if the user chose something:
				Main.cleanTest();
				Main.nacteni(prepravka.getTestDataGui(), fileDir, 1);
			}
		});

		vstupK.setOnAction(actionEvent -> {
			DirectoryChooser vyber = new DirectoryChooser();
			vyber.setTitle("Vyberte složku k načtení Trenovacích dat");
			// vytvoreni dialogu
			File fileDir = vyber.showDialog(testTextArea.getScene().getWindow());
			if (fileDir != null) { // if the user chose something:
				Main.cleanTren();
				Main.nacteni(prepravka.getTrenDataGui(), fileDir, 0);
			}
		});

		save.setOnAction(actionEvent -> {
			FileChooser vyber = new FileChooser();
			vyber.setTitle("Ulozeni Prepravky");
			vyber.getExtensionFilters().add(new ExtensionFilter("Prepravka", "*.prep"));

			// vytvoreni dialogu
			File file = vyber.showSaveDialog(testTextArea.getScene().getWindow());
			if (file != null) { // if the user chose something:
				try {
					PrepravkaSaveLoad.save(file, new PrepravkaSaveLoad(prepravka));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		konec.setOnAction(actionEvent -> Platform.exit());

		// pridani tlacitek do menu
		nacteni.getItems().addAll(vstupS, vstupK, save, konec);
		menuBar.getMenus().addAll(nacteni);

		return menuBar;

	}

	/**
	 * Hlavní komponenta aplikace tabPane záložky text a slovník.
	 *
	 * @return blok tabPane
	 */
	private Node loadTabPane() {
		BorderPane border = new BorderPane();

		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		// slozka text
		Tab text = new Tab("Testovací");
		text.setContent(loadTabTest());
		tabPane.getTabs().add(text);

		// slozka slovnik
		Tab slovnik = new Tab("Trenovací");
		slovnik.setContent(loadTabTren());
		tabPane.getTabs().add(slovnik);

		// hlavni nahled textu
		log = new TextArea();
		log.setPrefColumnCount(70);
		log.setPrefRowCount(5);
		log.setWrapText(true);
		log.appendText("Zde se zobrazí Přesnost.\n");
		log.setPadding(new Insets(0, 0, 0, 5));

		border.setCenter(tabPane);
		border.setBottom(log);
		return border;
	}

	/**
	 * Inicializace obsahu záložky text.
	 *
	 * @return blok text
	 */
	private Node loadSidebar() {

		// postranni panel
		VBox postraniPanel = new VBox();
		Label labelAlgorit = new Label("Příznaky");

		Label labelKlasif = new Label("Klasifikátor");
		Button buttonKlasif = new Button("Výpočet");
		Button buttonVstup = new Button("Klasifikuj");

		ToggleGroup radioAlgorit = new ToggleGroup();
		// radiobuttony
		RadioButton radioA0 = new RadioButton("Četnost");
		RadioButton radioA1 = new RadioButton("Binární Četnost");
		RadioButton radioA2 = new RadioButton("Četnost s filtrem spojek");
		// nastaveni groupy
		radioA0.setToggleGroup(radioAlgorit);
		radioA1.setToggleGroup(radioAlgorit);
		radioA2.setToggleGroup(radioAlgorit);

		ToggleGroup radioKlasif = new ToggleGroup();
		// radiobuttony
		RadioButton radioB0 = new RadioButton("Nejbližší soused");
		RadioButton radioB1 = new RadioButton("Naivní Bayes");
		// nastaveni groupy
		radioB0.setToggleGroup(radioKlasif);
		radioB1.setToggleGroup(radioKlasif);

		vstup = new TextArea();
		vstup.setPrefColumnCount(15);
		vstup.setPrefRowCount(6);
		vstup.setWrapText(true);
		vstup.appendText("Zde zadejte svuj vlastní text pro klasifikaci\n");
		vstup.setPadding(new Insets(0, 0, 0, 5));

		// vzdalenost mezi prvky
		postraniPanel.setSpacing(10);
		postraniPanel.setPadding(new Insets(0, 0, 0, 5));
		postraniPanel.setMinWidth(180);
		postraniPanel.setAlignment(Pos.TOP_LEFT);

		// nastaveni funkcnosti tlacitek
		buttonKlasif.setOnAction(actionEvent -> {
			if (radioB0.isSelected() && radioA0.isSelected()) {
				prepravka.setSoused(new NejblizsiSoused(prepravka, new VzCetnost()));
				prepravka.resetTestData();
				log.appendText("Klasifikuji\n");
				prepravka.getSoused().klasifikuj();
				log.appendText("Přesnost pro nejbližšího souseda pomocí četnosti slov je: "
						+ String.format("%.2f", prepravka.getSoused().getPresnost()) + " %\n");
			} else if (radioB0.isSelected() && radioA1.isSelected()) {
				prepravka.setSoused(new NejblizsiSoused(prepravka, new VzBinarniCetnost()));
				prepravka.resetTestData();
				log.appendText("Klasifikuji\n");
				prepravka.getSoused().klasifikuj();
				log.appendText("Přesnost pro nejbližšího souseda pomocí binární četnosti slov je: "
						+ String.format("%.2f", prepravka.getSoused().getPresnost()) + " %\n");
			} else if (radioB0.isSelected() && radioA2.isSelected()) {
				prepravka.setSoused(new NejblizsiSoused(prepravka, new VzFiltrCetnost()));
				prepravka.resetTestData();
				log.appendText("Klasifikuji\n");
				prepravka.getSoused().klasifikuj();
				log.appendText("Přesnost pro nejbližšího souseda pomocí četnosti s filtrem spojek: "
						+ String.format("%.2f", prepravka.getSoused().getPresnost()) + " %\n");
			} else if (radioB1.isSelected() && radioA0.isSelected()) {
				prepravka.setBayes(new Bayes(prepravka, new VzCetnost()));
				prepravka.resetTestData();
				log.appendText("Klasifikuji\n");
				prepravka.getBayes().klasifikuj();
				log.appendText("Přesnost pro Naivní Bayes pomocí četnosti slov je: "
						+ String.format("%.2f", prepravka.getBayes().getPresnost()) + " %\n");
			} else if (radioB1.isSelected() && radioA1.isSelected()) {
				prepravka.setBayes(new Bayes(prepravka, new VzBinarniCetnost()));
				prepravka.resetTestData();
				log.appendText("Klasifikuji\n");
				prepravka.getBayes().klasifikuj();
				log.appendText("Přesnost pro Naivní Bayes pomocí binární četnosti slov je: "
						+ String.format("%.2f", prepravka.getBayes().getPresnost()) + " %\n");
			} else if (radioB1.isSelected() && radioA2.isSelected()) {
				prepravka.setBayes(new Bayes(prepravka, new VzFiltrCetnost()));
				prepravka.resetTestData();
				log.appendText("Klasifikuji\n");
				prepravka.getBayes().klasifikuj();
				log.appendText("Přesnost pro Naivní Bayes pomocí četnosti s filtrem spojek: "
						+ String.format("%.2f", prepravka.getBayes().getPresnost()) + " %\n");
			} else {
				log.appendText("Prosím vyberte klasifikátor a příznak.\n");
			}
			log.positionCaret(Integer.MAX_VALUE);
		});

		buttonVstup.setOnAction(actionEvent -> {
			Main.cleanTest();
			Main.nacteni(prepravka.getTestDataGui(), vstup.getText());
			if (radioB0.isSelected() && radioA0.isSelected()) {
				prepravka.setSoused(new NejblizsiSoused(prepravka, new VzCetnost()));
				log.appendText("Klasifikuji\n");
				prepravka.getSoused().klasifikuj();
				log.appendText("Váš článek byl klasifikován nejbližšího souseda pomocí četnosti slov a je v kategorii: "
						+ prepravka.getTestovaciData().get(Paths.get("TextArea")).getUrceni() + "\n");
			} else if (radioB0.isSelected() && radioA1.isSelected()) {
				prepravka.setSoused(new NejblizsiSoused(prepravka, new VzBinarniCetnost()));
				log.appendText("Klasifikuji\n");
				prepravka.getSoused().klasifikuj();
				log.appendText(
						"Váš článek byl klasifikován nejbližšího souseda pomocí binární četnosti slov a je v kategorii: "
								+ prepravka.getTestovaciData().get(Paths.get("TextArea")).getUrceni() + "\n");
			} else if (radioB0.isSelected() && radioA2.isSelected()) {
				prepravka.setSoused(new NejblizsiSoused(prepravka, new VzFiltrCetnost()));
				log.appendText("Klasifikuji\n");
				prepravka.getSoused().klasifikuj();
				log.appendText(
						"Váš článek byl klasifikován nejbližšího souseda pomocí četnosti s filtrem spojek a je v kategorii: "
								+ prepravka.getTestovaciData().get(Paths.get("TextArea")).getUrceni() + "\n");
			} else if (radioB1.isSelected() && radioA0.isSelected()) {
				prepravka.setBayes(new Bayes(prepravka, new VzCetnost()));
				log.appendText("Klasifikuji\n");
				prepravka.getBayes().klasifikuj();
				log.appendText("Váš článek byl klasifikován Naivní Bayes pomocí četnosti slov a je v kategorii: "
						+ prepravka.getTestovaciData().get(Paths.get("TextArea")).getUrceni() + "\n");
			} else if (radioB1.isSelected() && radioA1.isSelected()) {
				prepravka.setBayes(new Bayes(prepravka, new VzBinarniCetnost()));
				log.appendText("Klasifikuji\n");
				prepravka.getBayes().klasifikuj();
				log.appendText(
						"Váš článek byl klasifikován Naivní Bayes pomocí binární četnosti slov a je v kategorii: "
								+ prepravka.getTestovaciData().get(Paths.get("TextArea")).getUrceni() + "\n");
			} else if (radioB1.isSelected() && radioA2.isSelected()) {
				prepravka.setBayes(new Bayes(prepravka, new VzFiltrCetnost()));
				log.appendText("Klasifikuji\n");
				prepravka.getBayes().klasifikuj();
				log.appendText(
						"Váš článek byl klasifikován Naivní Bayes pomocí četnosti s filtrem spojek a je v kategorii: "
								+ prepravka.getTestovaciData().get(Paths.get("TextArea")).getUrceni() + "\n");
			} else {
				log.appendText("Prosím vyberte klasifikátor a příznak.\n");
			}
			log.positionCaret(Integer.MAX_VALUE);
		});

		// pridani prvku do panelu
		postraniPanel.getChildren().addAll(labelKlasif, radioB0, radioB1, new Separator(), labelAlgorit, radioA0,
				radioA1, radioA2, new Separator(), buttonKlasif, new Separator(), new Label("Vlastni vstup"),
				new Separator(), vstup, buttonVstup);
		postraniPanel.setPadding(new Insets(5));

		return postraniPanel;
	}

	/**
	 * Inicializace obsahu záložky text.
	 *
	 * @return blok text
	 */
	private Node loadTabTest() {
		BorderPane border = new BorderPane();

		testDataView.setCellFactory(lv -> new ListCell<Path>() {
			@Override
			protected void updateItem(Path file, boolean empty) {
				super.updateItem(file, empty);
				setText(file == null ? null : file.getFileName().toString());
			}
		});

		// hlavni nahled textu
		testTextArea = new TextArea();
		testTextArea.setPrefColumnCount(70);
		testTextArea.setPrefRowCount(70);
		testTextArea.setWrapText(true);
		testTextArea.appendText("Zde se zobrazí info o souboru po vybrání z levého menu.");

		// pridani prvku do panelu
		border.setCenter(testTextArea);
		border.setLeft(testDataView);

		return border;
	}

	/**
	 * Inicializace obsahu záložky slovník.
	 *
	 * @return blok slovník
	 */
	private Node loadTabTren() {
		BorderPane border = new BorderPane();

		trenDataView.setCellFactory(lv -> new ListCell<Path>() {
			@Override
			protected void updateItem(Path file, boolean empty) {
				super.updateItem(file, empty);
				setText(file == null ? null : file.getFileName().toString());
			}
		});

		// hlavni nahled textu
		trenTextArea = new TextArea();
		trenTextArea.setPrefColumnCount(70);
		trenTextArea.setPrefRowCount(70);
		trenTextArea.setWrapText(true);
		trenTextArea.appendText("Zde se zobrazí info o souboru po vybrání z levého menu.");

		// pridani prvku do panelu
		border.setCenter(trenTextArea);
		border.setLeft(trenDataView);

		return border;
	}

	// getter nebo setter

	/**
	 * Gets the nahled.
	 *
	 * @return the nahled
	 */
	public TextArea getNahled() {
		return testTextArea;
	}

}
