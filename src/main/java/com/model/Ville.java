package com.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ville implements Serializable {

	/**
	 * Serial UID for serialization.
	 */
	private static final long serialVersionUID = -4691045975717850392L;
	
	private String codeCommune;
	private String nomCommune;
	private String codePostal;
	private String libelleAcheminement;
	private String ligne;
	private Coordonnee coordonnee;
}
