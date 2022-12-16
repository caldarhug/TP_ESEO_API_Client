package com.dto;

import com.model.Coordonnee;
import com.model.Ville;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VilleDTO {

	private String codeCommune;
	private String nomCommune;
	private String codePostal;
	private String libelleAcheminement;
	private String ligne;
	private Coordonnee coordonnee;

	public static VilleDTO fromEntity(Ville ville) {
		return VilleDTO.builder()
				.codeCommune(ville.getCodeCommune())
				.nomCommune(ville.getNomCommune())
				.codePostal(ville.getCodePostal())
				.build();
	}

	public Ville toModuleEntity() {
		return Ville.builder()
				.codeCommune(getCodeCommune())
				.nomCommune(getNomCommune())
				.codePostal(getCodePostal())
				.libelleAcheminement(getLibelleAcheminement())
				.ligne(getLigne())
				.coordonnee(getCoordonnee())
				.build();
	}
}
