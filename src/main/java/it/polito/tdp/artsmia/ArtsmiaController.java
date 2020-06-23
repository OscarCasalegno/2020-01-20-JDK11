package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Couple;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnCreaGrafo;

	@FXML
	private Button btnArtistiConnessi;

	@FXML
	private Button btnCalcolaPercorso;

	@FXML
	private ComboBox<String> boxRuolo;

	@FXML
	private TextField txtArtista;

	@FXML
	private TextArea txtResult;

	@FXML
	void doArtistiConnessi(ActionEvent event) {
		txtResult.clear();
		List<Couple> co = this.model.getConnessioni();
		if (co == null) {
			this.txtResult.appendText("Genera il grafo");
			return;
		}
		if (co.isEmpty()) {
			this.txtResult.appendText("Nessuna connessione trovata");
			return;
		}
		co.sort(null);
		for (Couple c : co) {
			this.txtResult.appendText(
					String.format("%s - %s : %d \n", c.getA1().getName(), c.getA2().getName(), c.getPeso()));
		}
	}

	@FXML
	void doCalcolaPercorso(ActionEvent event) {
		txtResult.clear();
		Integer id = null;
		try {
			id = Integer.parseInt(this.txtArtista.getText());
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Inserire un id valido");
			return;
		}
		if (!this.model.isPresente(id)) {
			this.txtResult.appendText("Artista non presente");
		}

	}

	@FXML
	void doCreaGrafo(ActionEvent event) {
		txtResult.clear();
		String role = this.boxRuolo.getValue();
		if (role == null) {
			this.txtResult.appendText("Scegliere un ruolo");
			return;
		}
		this.model.creaGrafo(role);
	}

	public void setModel(Model model) {
		this.model = model;
		this.boxRuolo.getItems().addAll(this.model.getRuoli());
	}

	@FXML
	void initialize() {
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}
}
