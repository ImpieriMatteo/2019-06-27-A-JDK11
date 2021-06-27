/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Arco> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo...\n\n");
    	
    	Integer anno = this.boxAnno.getValue();
    	if(anno==null) {
    		this.txtResult.appendText("Devi prima scegliere un ANNO!!");
    		return;
    	}
    	
    	String categoria = this.boxCategoria.getValue();
    	if(categoria==null) {
    		this.txtResult.appendText("Devi prima scegliere una CATEGORIA!!");
    		return;
    	}
    	
    	String result = this.model.creaGrafo(anno, categoria);
    	
    	this.txtResult.appendText(result);
    	this.txtResult.appendText("Elenco degli archi di peso massimo: \n");
    	for(Arco a : this.model.getBestArchi()) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    	
    	this.btnPercorso.setDisable(false);
    	this.boxArco.getItems().addAll(this.model.getAllArchi());
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n\n");
    	
    	String tipoIniziale = this.boxArco.getValue().getTipo1();
    	String tipoFinale = this.boxArco.getValue().getTipo2();
    	if(tipoFinale==null || tipoIniziale==null) {
    		this.txtResult.appendText("Devi selezionare prima un ARCO!!");
    		return;
    	}
    	
    	List<String> result = this.model.getPercorso(tipoIniziale, tipoFinale);
    	this.txtResult.appendText("PERCORSO TROVATO CON PESO "+this.model.getPesoBest()+": \n\n");
    	for(String s : result) {
    		this.txtResult.appendText("- "+s+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxAnno.getItems().addAll(this.model.listAllYears());
    	this.boxCategoria.getItems().addAll(this.model.listAllOffenseCategory());
    }
}
