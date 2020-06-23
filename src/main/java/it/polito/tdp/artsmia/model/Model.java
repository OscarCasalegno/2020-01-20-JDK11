package it.polito.tdp.artsmia.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	Graph<Artist, DefaultWeightedEdge> graph;

	public List<String> getRuoli() {
		return ArtsmiaDAO.listAllRoles();
	}

	public void creaGrafo(String role) {

		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		Graphs.addAllVertices(this.graph, ArtsmiaDAO.listArtistsByRole(role));

		System.out.println(this.graph.vertexSet());

	}

}
