package fr.univtln.dapm.bda.hsearch_elasticsearch;

import java.io.IOException;

import fr.univtln.dapm.bda.hsearch_elasticsearch.search.IndexSearchApi;

public class Main {
	public static void main(String[] args) throws IOException {

		// Instanciation de notre classe IndexSearchApi pour indexer et rechercher
		IndexSearchApi api = new IndexSearchApi();

		// Réindexation à chaque nouvel appel de la classe Main (à commenter si besoin).
		api.purgeIndex();
		api.indexFilesInFolder(Main.class.getResource("/data/raw").getFile());

		// Recherche.
		System.out.println(api.searchInTitle("pilgrimge tale"));

		System.exit(0);
	}
}
