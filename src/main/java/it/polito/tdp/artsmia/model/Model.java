package it.polito.tdp.artsmia.model;

import java.util.List;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	public List<String> getRuoli() {
		return ArtsmiaDAO.listAllRoles();
	}

}
