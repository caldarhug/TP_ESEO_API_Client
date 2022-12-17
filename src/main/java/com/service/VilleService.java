package com.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.model.Coordonnee;
import com.model.Ville;

@Service
public class VilleService {

	/**
	 * @return The list of all towns
	 * @throws Exception
	 */
	public List<Ville> getVilles() throws Exception {
		List<Ville> villes = new ArrayList<>();

		// Host url
		String host = "http://localhost:8181/ville";

		// Format query for preventing encoding problems
		HttpResponse<JsonNode> response = Unirest.get(host).asJson();

		// Prettifying
		Gson gson = new Gson();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(response.getBody().toString());
		String jsonString = gson.toJson(je);

		JSONArray jsonArray = new JSONArray(jsonString);
		for (int i = 0; i < jsonArray.length(); i++) {
			Ville ville = new Ville();
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			ville.setCodeCommune(jsonObj.getString("codeCommune"));
			ville.setNomCommune(jsonObj.getString("nomCommune"));
			ville.setCodePostal(jsonObj.getString("codePostal"));
			ville.setLibelleAcheminement(jsonObj.getString("libelleAcheminement"));
			ville.setLigne(jsonObj.getString("ligne"));

			Coordonnee coord = new Coordonnee();
			String latitude = jsonObj.getJSONObject("coordonnee").getString("latitude");
			String longitude = jsonObj.getJSONObject("coordonnee").getString("longitude");
			coord.setLatitude(latitude);
			coord.setLongitude(longitude);
			ville.setCoordonnee(coord);

			villes.add(ville);
		}

		return villes;
	}
	
	/**
	 * @param codeCommune
	 * @return The ville which is linked to the codeCommune
	 * @throws Exception
	 */
	public Ville getVilleByCodeCommune(String codeCommune) throws Exception {
		String host = "http://localhost:8181/ville/";

		HttpResponse<JsonNode> response = Unirest.get(host + codeCommune).asJson();

		Gson gson = new Gson();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(response.getBody().toString());
		String jsonString = gson.toJson(je);

		JSONObject jsonObj = new JSONObject(jsonString);
		Ville ville = new Ville();
		ville.setCodeCommune(jsonObj.getString("codeCommune"));
		ville.setNomCommune(jsonObj.getString("nomCommune"));
		ville.setCodePostal(jsonObj.getString("codePostal"));
		ville.setLibelleAcheminement(jsonObj.getString("libelleAcheminement"));
		ville.setLigne(jsonObj.getString("ligne"));

		Coordonnee coord = new Coordonnee();
		String latitude = jsonObj.getJSONObject("coordonnee").getString("latitude");
		String longitude = jsonObj.getJSONObject("coordonnee").getString("longitude");
		coord.setLatitude(latitude);
		coord.setLongitude(longitude);
		ville.setCoordonnee(coord);

		return ville;
	}
	
	/**
	 * @param codeCommune
	 * @param nomCommune
	 * @param codePostal
	 * @param libelleAcheminement
	 * @param ligne
	 * @param latitude
	 * @param longitude
	 * @return A string which confirm the update
	 * @throws Exception
	 */
	public String putVille(String codeCommune, String nomCommune, String codePostal, String libelleAcheminement, String ligne, String latitude, String longitude) throws Exception {
		String host = "http://localhost:8181/ville/put";
		
		String strCodeCommune = "?codeCommune=" + codeCommune;
		String strNomCommune = "&nomCommune=" + nomCommune.replace(" ", "_");
		String strCodePostal = "&codePostal=" + codePostal;
		String strLibelleAcheminement = "&libelleAcheminement=" + libelleAcheminement.replace(" ", "_");
		String strLigne = "&ligne=" + ligne.replace(" ", "_");
		String strLatitude = "&latitude=" + latitude;
		String strLongitude = "&longitude=" + longitude;
		
		String request = host + strCodeCommune + strNomCommune + strCodePostal + strLibelleAcheminement + strLigne + strLatitude + strLongitude;

		Unirest.put(request).asString();
		
		return "La ville a été correctement modifiée !";
	}

	/**
	 * @param codeCommune
	 * @return A string which confirms the deletion of the town
	 * @throws Exception
	 */
	public String deleteVille(String codeCommune) throws Exception {
		String host = "http://localhost:8181/ville/delete";
		
		String strCodeCommune = "?codeCommune=" + codeCommune;
		
		Unirest.delete(host + strCodeCommune).asString();
		
		return "La ville a été correctement supprimée !";
	}
	
	/**
	 * @param villeA
	 * @param villeB
	 * @return The distance bewteen villeA and villeB calculed thanks to this:
	 * 	R*arcos(sina*sinb + cosa*cosb*cos(c-d))
		R = rayon de la Terre
		a = latitude villeA / b = latitude villeB
		c = longitude villeA / d = longitude villeB
	 */
	public String getDistance(Ville villeA, Ville villeB) {
		double latitudeA = Float.parseFloat(villeA.getCoordonnee().getLatitude()) * Math.PI / 180;
		double latitudeB = Float.parseFloat(villeB.getCoordonnee().getLatitude()) * Math.PI / 180;

		double longitudeA = Float.parseFloat(villeA.getCoordonnee().getLongitude()) * Math.PI / 180;
		double longitudeB = Float.parseFloat(villeB.getCoordonnee().getLongitude()) * Math.PI / 180;

		double rayonTerre = 6372.795477598;

		double distance = rayonTerre * Math.acos((Math.sin(latitudeA) * Math.sin(latitudeB))
				+ (Math.cos(latitudeA) * Math.cos(latitudeB) * Math.cos(longitudeA - longitudeB)));

		String format = "0.00";
		NumberFormat formatter = new DecimalFormat(format);
		return formatter.format(distance);
	}
}
