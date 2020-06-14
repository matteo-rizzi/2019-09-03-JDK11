/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Cerco cammino peso massimo...\n\n");
    	
    	String porzione = this.boxPorzioni.getValue();
    	if(porzione == null) {
    		this.txtResult.appendText("Errore! Devi selezionare un tipo di porzione per poterne cercare il cammino con peso massimo!\n");
    		return;
    	}
    	
    	int N;
    	try {
    		N = Integer.parseInt(this.txtPassi.getText());
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Errore! Inserire un valore numerico intero per i passi!\n");
    		return;
    	}
    	
    	List<String> best = this.model.camminoMassimo(N, porzione);
    	if(best.size() == 0) {
    		this.txtResult.appendText("Non ho trovato un cammino di lunghezza N!\n");
    		return;
    	}
    	this.txtResult.appendText("Il cammino con peso massimo è composto da:\n");
    	for(String s : best) {
    		this.txtResult.appendText(s + "\n");
    	}
    	this.txtResult.appendText(String.format("Il peso totale è: %d\n", this.model.getPesoMax()));
    	
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Cerco porzioni correlate...\n\n");
    	
    	String porzione = this.boxPorzioni.getValue();
    	if(porzione == null) {
    		this.txtResult.appendText("Errore! Devi selezionare un tipo di porzione per poterne cercare i correlati!\n");
    		return;
    	}
    	
    	Map<String, Integer> correlati = this.model.getCorrelati(porzione);
    	this.txtResult.appendText("Elenco correlati:\n");
    	for(String correlato : correlati.keySet()) {
    		this.txtResult.appendText(String.format("%s (%d)\n", correlato, correlati.get(correlato)));
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...\n\n");
    	int C;
    	try {
    		C = Integer.parseInt(this.txtCalorie.getText());
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Errore! Inserire un valore numerico intero per le calorie!\n");
    		return;
    	}
    	
    	this.model.creaGrafo(C);
    	
    	this.boxPorzioni.getItems().clear();
    	this.boxPorzioni.getItems().addAll(this.model.getVertici());
    	
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("#VERTICI: " + this.model.nVertici() + "\n");
    	this.txtResult.appendText("#ARCHI: " + this.model.nArchi() + "\n");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
