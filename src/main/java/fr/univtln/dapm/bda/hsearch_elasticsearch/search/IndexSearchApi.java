package fr.univtln.dapm.bda.hsearch_elasticsearch.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import fr.univtln.dapm.bda.hsearch_elasticsearch.domain.Book;
import fr.univtln.dapm.bda.hsearch_elasticsearch.domain.BookResult;

/**
 * API pour l'indexation et la recherche de documents (des livres ici).
 * 
 * @author vincent
 *
 */
public class IndexSearchApi {
	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("bda");
	private EntityManager entityManager = entityManagerFactory.createEntityManager();
	private FullTextEntityManager fullTextSession = Search.getFullTextEntityManager(entityManager);

	public void purgeIndex() {
		fullTextSession.purgeAll(Book.class);
	}

	public boolean indexFilesInFolder(String folderPath) throws IOException {
		entityManager.getTransaction().begin();
		Files.list(Paths.get(folderPath)).filter(Files::isRegularFile).forEach(t -> {
			try {
				indexFile(t);
			} catch (IOException e) {
				System.err.println("Cannot process " + t.toString());
			}
		});
		entityManager.getTransaction().commit();
		return true;
	}

	public boolean indexFile(Path path) throws IOException {
		String fileName = path.getFileName().toString();
		String fileContent = new String(Files.readAllBytes(path));

		Book book = new Book();
		book.setTitle(fileName);
		book.setContent(fileContent);
		entityManager.persist(book); // le livre est automatiquement ajouté à la base de données et indexé!
		return true;
	}

	public List<BookResult> searchInTitle(String query) {
		List<BookResult> bookResults = new ArrayList<>();

		entityManager.getTransaction().begin();

		QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
		org.apache.lucene.search.Query luceneQuery = queryBuilder.simpleQueryString().onFields("title").matching(query)
				.createQuery();
		FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);

		// projection du score (requête, document)
		fullTextQuery.setProjection(ProjectionConstants.SCORE, ProjectionConstants.THIS);
		fullTextQuery.setMaxResults(5);
		List<Object[]> results = fullTextQuery.getResultList();

		for (Object[] result : results) {
			float score = (float) result[0];
			Book book = (Book) result[1];
			bookResults.add(new BookResult(book, score));
		}
		entityManager.getTransaction().commit();

		return bookResults;
	}
}
