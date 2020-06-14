package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private FoodDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;

	private List<String> best;
	private Integer pesoMax;

	public Model() {
		this.dao = new FoodDao();
	}

	public void creaGrafo(int C) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// aggiungiamo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getPorzioniByCalorie(C));

		// aggiungiamo gli archi
		List<Adiacenza> adiacenze = this.dao.getAdiacenze();
		Collections.sort(adiacenze);
		for (Adiacenza a : this.dao.getAdiacenze()) {
			if (this.grafo.vertexSet().contains(a.getPrimo()) && this.grafo.vertexSet().contains(a.getSecondo()))
				Graphs.addEdge(this.grafo, a.getPrimo(), a.getSecondo(), a.getPeso());
		}
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public Set<String> getVertici() {
		return this.grafo.vertexSet();
	}

	public Map<String, Integer> getCorrelati(String porzione) {
		Map<String, Integer> correlati = new LinkedHashMap<>();
		for (String vicino : Graphs.neighborListOf(this.grafo, porzione)) {
			if (vicino != null)
				correlati.put(vicino, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(porzione, vicino)));
		}
		return correlati;
	}

	public List<String> camminoMassimo(int N, String partenza) {
		this.best = new ArrayList<>();
		this.pesoMax = 0;
		Map<String, Integer> parziale = new LinkedHashMap<>();

		parziale.put(partenza, 0);

		this.cerca(parziale, 0, N, partenza);
		return best;
	}

	private void cerca(Map<String, Integer> parziale, int L, int N, String prossimo) {
		// caso terminale
		if (L == N) {
			if (sommaPesi(parziale) > this.pesoMax) {
				this.best = new ArrayList<>(parziale.keySet());
				pesoMax = sommaPesi(parziale);
				return;
			} else
				return;
		}

		// caso intermedio
		Map<String, Integer> correlati = this.getCorrelati(prossimo);
		for (String vicino : correlati.keySet()) {
			if (!parziale.containsKey(vicino)) {
				parziale.put(vicino, correlati.get(vicino));
				this.cerca(parziale, L + 1, N, vicino);
				parziale.remove(vicino);
			}
		}

	}

	public Integer getPesoMax() {
		return pesoMax;
	}

	private Integer sommaPesi(Map<String, Integer> parziale) {
		Integer tot = 0;
		for (Integer peso : parziale.values()) {
			tot += peso;
		}
		return tot;
	}
}
