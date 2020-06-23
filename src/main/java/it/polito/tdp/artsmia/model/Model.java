package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	Graph<Artist, DefaultWeightedEdge> graph;
	Map<Integer, Artist> idMapAutori;
	Integer bestPeso;
	Integer bestLung;
	List<Artist> bestPercorso;

	public List<String> getRuoli() {
		return ArtsmiaDAO.listAllRoles();
	}

	public void creaGrafo(String role) {

		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMapAutori = new HashMap<>();
		ArtsmiaDAO.listArtistsByRole(idMapAutori, role);

		Graphs.addAllVertices(this.graph, new ArrayList<Artist>(this.idMapAutori.values()));

		List<Couple> connec = ArtsmiaDAO.getConnectionsByRole(idMapAutori, role);

		for (Couple c : connec) {
			graph.setEdgeWeight(graph.addEdge(c.getA1(), c.getA2()), c.getPeso());
		}

	}

	public List<Couple> getConnessioni() {

		if (this.graph == null) {
			return null;
		}

		List<Couple> connec = new ArrayList<>();

		for (DefaultWeightedEdge e : graph.edgeSet()) {
			connec.add(new Couple(this.graph.getEdgeSource(e), this.graph.getEdgeTarget(e),
					(int) this.graph.getEdgeWeight(e)));
		}

		return connec;

	}

	public boolean isPresente(Integer id) {
		return this.idMapAutori.containsKey(id);

	}

	public void calcolaPercorso(Integer id) {

		Integer peso = null;
		List<Artist> parziale = new ArrayList<>();
		parziale.add(this.idMapAutori.get(id));

		bestPeso = 0;
		bestPercorso = new ArrayList<>();
		bestLung = 0;

		this.cerca(parziale, peso);

	}

	private void cerca(List<Artist> parziale, Integer peso) {

		if (parziale.size() > bestLung) {
			this.bestPeso = peso;
			this.bestPercorso = new ArrayList<>(parziale);
		}

		for (DefaultWeightedEdge e : this.graph.edgesOf(parziale.get(parziale.size() - 1))) {
			Artist a;
			if (graph.getEdgeTarget(e) == parziale.get(parziale.size() - 1)) {
				a = graph.getEdgeSource(e);
			} else {
				a = graph.getEdgeTarget(e);
			}

			if (peso == null) {
				peso = (int) graph.getEdgeWeight(e);
				parziale.add(a);

				this.cerca(parziale, peso);

				parziale.remove(parziale.size() - 1);
				peso = null;

			} else if (peso == (int) graph.getEdgeWeight(e)) {

				if (!parziale.contains(a)) {
					parziale.add(a);
					this.cerca(parziale, peso);
					parziale.remove(parziale.size() - 1);
				}

			}

		}

	}

	public Integer getBestPeso() {
		return bestPeso;
	}

	public List<Artist> getBestPercorso() {
		return bestPercorso;
	}

}
