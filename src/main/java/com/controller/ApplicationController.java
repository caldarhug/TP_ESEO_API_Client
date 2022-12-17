package com.controller;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dto.VilleDTO;
import com.model.Coordonnee;
import com.model.Ville;
import com.service.VilleService;

@Controller
public class ApplicationController {

	private static final String VILLES = "villes";

	@Autowired
	public VilleService villeService;


	@GetMapping(value = "/")
	public String home(@ModelAttribute VilleDTO villeADTO, @ModelAttribute VilleDTO villeBDTO, Model model) throws Exception {
		model.addAttribute(VILLES, villeService.getVilles());

		return "home";
	}

	@PostMapping(value = "/")
	public String postDistance(Model model, @ModelAttribute("villeA") String strVilleA, @ModelAttribute("villeB") String strVilleB) throws Exception {
		model.addAttribute(VILLES, villeService.getVilles());

		try {
			String codeCommuneVilleA = StringUtils.substringBetween(strVilleA, "codeCommune=", ", nomCommune");
			String codeCommuneVilleB = StringUtils.substringBetween(strVilleB, "codeCommune=", ", nomCommune");

			Ville villeA = villeService.getVilleByCodeCommune(codeCommuneVilleA);
			Ville villeB = villeService.getVilleByCodeCommune(codeCommuneVilleB);

			String distance = villeService.getDistance(villeA, villeB);

			model.addAttribute("villeA", villeA);
			model.addAttribute("villeB", villeB);
			model.addAttribute("distance", distance);

		} catch (JSONException e) {
			if (e.getMessage() != null) {
				model.addAttribute("error", "Veillez à sélectionner deux villes de la liste pour le calcul de distance.");
				return "home";
			} else {
				System.out.println(e.getMessage());
			}
		}

		return "home";
	}

	@GetMapping(value = "/listVilles")
	public String listVilles(Model model) throws Exception {
		model.addAttribute(VILLES, villeService.getVilles());

		return "listVilles";
	}

	@GetMapping(value = "/listVilles/{codeCommune}")
	public String getVilleModify(@PathVariable(value = "codeCommune") String codeCommune, Model model) throws Exception {
		Ville ville = villeService.getVilleByCodeCommune(codeCommune);
		Coordonnee coordonnee = ville.getCoordonnee();

		model.addAttribute("ville", ville);
		model.addAttribute("coordonnee", coordonnee);

		return "villeModify";
	}

	@PostMapping(value = "/listVilles/{codeCommune}")
	public String postVilleModify(Model model, @ModelAttribute(value = "codeCommune") String codeCommune, @ModelAttribute("nomCommune") String nomCommune, @ModelAttribute("codePostal") String codePostal, @ModelAttribute("libelleAcheminement") String libelleAcheminement, @ModelAttribute("ligne") String ligne, @ModelAttribute("latitude") String latitude, @ModelAttribute("longitude") String longitude) throws Exception {
		Ville ville = villeService.getVilleByCodeCommune(codeCommune);
		Coordonnee coordonnee = ville.getCoordonnee();

		model.addAttribute("ville", ville);
		model.addAttribute("coordonnee", coordonnee);

		villeService.putVille(codeCommune, nomCommune, codePostal, libelleAcheminement, ligne, latitude, longitude);

		return "redirect:/listVilles";
	}

	@PostMapping(value = "/listVilles/{codeCommune}/delete")
	public String postVilleDelete(@PathVariable String codeCommune) throws Exception {
		villeService.deleteVille(codeCommune);

		return "redirect:/listVilles";
	}
}
