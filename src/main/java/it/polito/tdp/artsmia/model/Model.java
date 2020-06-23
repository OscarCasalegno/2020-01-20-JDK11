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

}
