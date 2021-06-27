package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	private List<Arco> archi;
	private List<String> percorsoBest;
	private Integer pesoBest;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public String creaGrafo(Integer anno, String categoria) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.archi = new ArrayList<>();
		
		Graphs.addAllVertices(this.grafo, this.dao.getVertex(anno, categoria));
		
		this.archi = this.dao.getEdge(anno, categoria);
		for(Arco a : this.archi) {
			
			if(!this.grafo.containsEdge(a.getTipo1(), a.getTipo2())) {
				
				Graphs.addEdgeWithVertices(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
			}
		}
		
		return String.format("GRAFO CREATO!!\n\n#VERTICI: %s\n#ARCHI: %s\n\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public List<Arco> getBestArchi() {
		Collections.sort(this.archi);
		
		Integer pesoMax = this.archi.get(0).getPeso();
		List<Arco> result = new ArrayList<>();
		result.add(this.archi.get(0));
		
		for(int i=1; i<this.archi.size(); i++) {
			
			if(this.archi.get(i).getPeso()==pesoMax)
				result.add(this.archi.get(i));
			else
				break;
		}
		
		return result;
	}
	
	public List<String> listAllOffenseCategory() {
		List<String> result = this.dao.listAllOffenseCategory();
		Collections.sort(result);
		return result;
	}
	
	public List<Integer> listAllYears() {
		return this.dao.listAllYears();
	}
	
	public List<Arco> getAllArchi() {
		return this.archi;
	}

	public List<String> getPercorso(String tipoIniziale, String tipoFinale) {
		this.percorsoBest = new ArrayList<>();
		this.pesoBest = Integer.MAX_VALUE;
		
		List<String> parziale = new ArrayList<>();
		parziale.add(tipoIniziale);
		
		this.calcolaPercorso(parziale, tipoFinale);
		
		return this.percorsoBest;
	}

	private void calcolaPercorso(List<String> parziale, String tipoFinale) {

		if(parziale.size()==this.grafo.vertexSet().size()-1) {
			
			String penultimo = parziale.get(parziale.size()-1);
			for(String s : Graphs.neighborListOf(this.grafo, penultimo)) {
				
				if(s.equals(tipoFinale))
					parziale.add(tipoFinale);
			}
			
			if(parziale.size()==this.grafo.vertexSet().size()) {
				
				Integer peso = this.calcolaPeso(parziale);
				if(peso<this.pesoBest) {
					
					this.pesoBest = peso;
					this.percorsoBest = new ArrayList<>(parziale);
				}
			}
			
			return;
		}
		
		String precedente = parziale.get(parziale.size()-1);
		for(String s : Graphs.neighborListOf(this.grafo, precedente)) {
			
			if(!parziale.contains(s) && !s.equals(tipoFinale)) {
				
				parziale.add(s);
				this.calcolaPercorso(parziale, tipoFinale);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private Integer calcolaPeso(List<String> parziale) {
		String precedente = null;
		Integer peso = 0;
		
		for(int i=1; i<parziale.size(); i++) {
			
			precedente = parziale.get(i-1);
			peso += (int)this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, parziale.get(i)));
		}
		
		return peso;
	}

	public Integer getPesoBest() {
		return this.pesoBest;
	}

	
	
}
