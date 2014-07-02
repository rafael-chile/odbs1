package explorer;
import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;

public class QueryEngineInstance {
	public OWLOntologyManager manager;
	public OWLOntology ontology;
	public ModelIOManager ioManager;
	public QuestPreferences preference;
	public QuestOWLFactory factory;
	public QuestOWL reasoner;
	public QuestOWLConnection conn;
	public QuestOWLStatement st;
	public OBDAModel obdaModel;
	public OBDADataFactory fac;
	
	public QueryEngineInstance() throws Exception {
		manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument(new File(Global.owlfile));
		fac = OBDADataFactoryImpl.getInstance();
		obdaModel = fac.getOBDAModel();
		ioManager = new ModelIOManager(obdaModel);
		ioManager.load(Global.obdafile);
		preference = new QuestPreferences();
		preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);
		factory = new QuestOWLFactory();
		factory.setOBDAController(obdaModel);
		factory.setPreferenceHolder(preference);
		reasoner = (QuestOWL) factory.createReasoner(ontology, new SimpleConfiguration());
		conn = reasoner.getConnection();
		st = conn.createStatement();
	}
	
	public void close() {
		try {
			if (st != null && !st.isClosed()) {
				st.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			reasoner.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public QuestOWLResultSet executeQuery(String query) {
		try {
			return st.executeTuple(query);
		} catch (OWLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public QuestOWLResultSet initialPerson() {
		String query = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n" +
				"WHERE { ?x a :Person . ?x :Person_Name ?y . }";
		if(Global.limit > 0) {
			query = query + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(query);
	}
	
	public QuestOWLResultSet initialCharacter(){
		String query = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n" +
				"WHERE { ?x a :Character . ?x :Character_Name ?y . }";
		if(Global.limit > 0) {
			query = query + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(query);
	}

	public QuestOWLResultSet initialCompany() {
		String query = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n" +
				"WHERE { ?x a :Company . ?x :Company_Name ?y . }";
		if(Global.limit > 0) {
			query = query + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(query);
 	}

	public QuestOWLResultSet initialSeries() {
		String query = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n" +
				"WHERE { ?x a :Series . ?x :Series_Title ?y . }";
		if(Global.limit > 0) {
			query = query + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(query);
	}	
	public QuestOWLResultSet initialSeason() {
		String query = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y ?z\n" +
				"WHERE { ?x a :Season . ?x :Season_SeriesTitle ?y . ?x :Season_Number ?z . }";
		if(Global.limit > 0) {
			query = query + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(query);
	}
	
	public QuestOWLResultSet initialEpisode() {
		String query = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n" +
				"WHERE { ?x a :Episode . ?x :Episode_Title ?y . }";
		if(Global.limit > 0) {
			query = query + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(query);
	}
	
	public QuestOWLResultSet initialTag() {
		String query = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n" +
				"WHERE { ?x a :Tag . ?x :Tag_Caption ?y . }";
		if(Global.limit > 0) {
			query = query + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(query);
	}
	
	public QuestOWLResultSet queryPersonToProduction(Individual person, String roleFilter, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y";
		if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?z";
		}
		querytext = querytext + "\n" + "WHERE { " + person.getUri() + " :workedAs ?j . ?j :inProduction ?x .";
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?x a :Episode . ?x :Episode_Title ?y .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?x a :Season . ?x :Season_Number ?z . ?x :Season_SeriesTitle ?y .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?x a :Series . ?x :Series_Title ?y .";
		}
		if(roleFilter.equals(Global.roleActors)) {
			querytext = querytext + " ?j a :Actor .";
		} else if(roleFilter.equals(Global.roleProducers)) {
			querytext = querytext + " ?j :Job_Role 3 .";
		} else if(roleFilter.equals(Global.roleWriters)) {
			querytext = querytext + " ?j :Job_Role 4 .";
		} else if(roleFilter.equals(Global.roleCinematographers)) {
			querytext = querytext + " ?j :Job_Role 5 .";
		} else if(roleFilter.equals(Global.roleComposers)) {
			querytext = querytext + " ?j :Job_Role 6 .";
		} else if(roleFilter.equals(Global.roleCostumeDesigners)) {
			querytext = querytext + " ?j :Job_Role 7 .";
		} else if(roleFilter.equals(Global.roleDirectors)) {
			querytext = querytext + " ?j :Job_Role 8 .";
		} else if(roleFilter.equals(Global.roleEditors)) {
			querytext = querytext + " ?j :Job_Role 9 .";
		} else if(roleFilter.equals(Global.roleMiscellaneous)) {
			querytext = querytext + " ?j :Job_Role 10 .";
		} else if(roleFilter.equals(Global.roleProductionDesigners)) {
			querytext = querytext + " ?j :Job_Role 11 .";
		} else if(roleFilter.equals(Global.roleGuests)) {
			querytext = querytext + " ?j :Job_Role 12 .";
		}
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet queryPersonToPerson(Individual person1, String roleFilter, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { ?x a :Person . ?x :Person_Name ?y ." +
				" ?x :workedAs ?jx . ?jx :inProduction ?p . " + person1.getUri() +
				" :workedAs ?juri . ?juri :inProduction ?p .";
		if(roleFilter.equals(Global.roleActors)) {
			querytext = querytext + " ?jx a :Actor .";
		} else if(roleFilter.equals(Global.roleProducers)) {
			querytext = querytext + " ?jx :Job_Role 3 .";
		} else if(roleFilter.equals(Global.roleWriters)) {
			querytext = querytext + " ?jx :Job_Role 4 .";
		} else if(roleFilter.equals(Global.roleCinematographers)) {
			querytext = querytext + " ?jx :Job_Role 5 .";
		} else if(roleFilter.equals(Global.roleComposers)) {
			querytext = querytext + " ?jx :Job_Role 6 .";
		} else if(roleFilter.equals(Global.roleCostumeDesigners)) {
			querytext = querytext + " ?jx :Job_Role 7 .";
		} else if(roleFilter.equals(Global.roleDirectors)) {
			querytext = querytext + " ?jx :Job_Role 8 .";
		} else if(roleFilter.equals(Global.roleEditors)) {
			querytext = querytext + " ?jx :Job_Role 9 .";
		} else if(roleFilter.equals(Global.roleMiscellaneous)) {
			querytext = querytext + " ?jx :Job_Role 10 .";
		} else if(roleFilter.equals(Global.roleProductionDesigners)) {
			querytext = querytext + " ?jx :Job_Role 11 .";
		} else if(roleFilter.equals(Global.roleGuests)) {
			querytext = querytext + " ?jx :Job_Role 12 .";
		}
		
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?p a :Episode .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?p a :Season .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?p a :Series .";
		}
		
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet queryPersonToCharacter(Individual person1) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?y ?z\n"+
				"WHERE {  "+ person1.getUri() +" :workedAs ?x . ?x :plays ?y. ?y :Character_Name ?z . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet queryCompanyToCompany(Individual company) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { " + company.getUri() + " :basedAt ?c . ?x :basedAt ?c . ?x :Company_Name ?y . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet queryCompanyToProduction(Individual company, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y";
		if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?z";
		}
		querytext = querytext + "\n" + "WHERE { " + company.getUri() + " :produces ?x .";
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?x a :Episode . ?x :Episode_Title ?y .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?x a :Season . ?x :Season_Number ?z . ?x :Season_SeriesTitle ?y .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?x a :Series . ?x :Series_Title ?y .";
		}
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet queryCharacterToPerson(Individual character) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { ?x :Person_Name ?y . ?x :workedAs ?j . ?j :plays " + character.getUri() + " . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet queryCharacterToProduction(Individual character, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y";
		if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?z";
		}
		querytext = querytext + "\n" + "WHERE { " + character.getUri() + " :appearsIn ?x .";
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?x a :Episode . ?x :Episode_Title ?y .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?x a :Season . ?x :Season_Number ?z . ?x :Season_SeriesTitle ?y .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?x a :Series . ?x :Series_Title ?y .";
		}
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet queryCharacterToCharacter(Individual character, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n" +
				"WHERE { " + character.getUri() + " :appearsIn ?p . ?x :appearsIn ?p . ?x :Character_Name ?y";
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?p a :Episode .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?p a :Season .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?p a :Series .";
		}
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet querySeriesToSeason(Individual series) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y ?z\n"+
				"WHERE { " + series.getUri() + " :hasSeason ?x . ?x :Season_Number ?z . ?x :Season_SeriesTitle ?y . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet querySeriesToEpisode(Individual series) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { " + series.getUri() + " :containsEpisode ?x . ?x :Series_Title ?y . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet querySeasonToSeries(Individual series) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { ?x :hasSeason " + series.getUri() + " . ?x :Series_Title ?y . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet querySeasonToEpisode(Individual season) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { " + season.getUri() + " :hasEpisode ?x . ?x :Series_Title ?y . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	public QuestOWLResultSet querySeasonToSeason(Individual season) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y ?z\n"+
				"WHERE { ?s :hasSeason " + season.getUri() + " . ?s :hasSeason ?x ." +
				" ?x :Season_Number ?z . ?x :Season_SeriesTitle ?y . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	//queryEpisodeToSeason = "Which season does it belong to?";  >
	public QuestOWLResultSet queryEpisodeToSeason(Individual episode) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?p ?st ?sn\n"
						+ "WHERE { ?p :hasEpisode " + episode.getUri() 
						+ " . ?p a :Season . ?p :Season_Number ?sn . ?p :Season_SeriesTitle ?st . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
	 
	public QuestOWLResultSet queryEpisodeToSeries(Individual series) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { ?x :containsEpisode " + series.getUri() + ". ?x :Series_Title ?y . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	 
	public QuestOWLResultSet queryEpisodeToEpisode(Individual series) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { ?x :containsEpisode ?y\n"+
						"?x :containsEpisode " + series.getUri() +" . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}	  
	
//		queryProductionToPerson  "Who worked on it?" > Role 
	public QuestOWLResultSet queryProductionToPerson(Individual production, String roleFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?p ?n\n"+
				"WHERE { ?j :inProduction " + production.getUri() + 
				" . ?p :workedAs ?j . ?p :Person_Name ?n .";		
			if(roleFilter.equals(Global.roleActors)) {
				querytext = querytext + " ?j a :Actor .";
			} else if(roleFilter.equals(Global.roleProducers)) {
				querytext = querytext + " ?j :Job_Role 3 .";
			} else if(roleFilter.equals(Global.roleWriters)) {
				querytext = querytext + " ?j :Job_Role 4 .";
			} else if(roleFilter.equals(Global.roleCinematographers)) {
				querytext = querytext + " ?j :Job_Role 5 .";
			} else if(roleFilter.equals(Global.roleComposers)) {
				querytext = querytext + " ?j :Job_Role 6 .";
			} else if(roleFilter.equals(Global.roleCostumeDesigners)) {
				querytext = querytext + " ?j :Job_Role 7 .";
			} else if(roleFilter.equals(Global.roleDirectors)) {
				querytext = querytext + " ?j :Job_Role 8 .";
			} else if(roleFilter.equals(Global.roleEditors)) {
				querytext = querytext + " ?j :Job_Role 9 .";
			} else if(roleFilter.equals(Global.roleMiscellaneous)) {
				querytext = querytext + " ?j :Job_Role 10 .";
			} else if(roleFilter.equals(Global.roleProductionDesigners)) {
				querytext = querytext + " ?j :Job_Role 11 .";
			} else if(roleFilter.equals(Global.roleGuests)) {
				querytext = querytext + " ?j :Job_Role 12 .";
			}		
			querytext = querytext + " }";
		
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}	 
	
	// queryProductionToCharacter = "What characters appeared on it?";  >
	
	public QuestOWLResultSet queryProductionToCharacter(Individual production) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?p\n"+
				"WHERE { ?x :appearsIn " + production.getUri() +
				". ?x :Character_Name ?p . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}	  
	
// queryProductionToProduction_Year = "What productions were produced in the same year?"; 	  > Production 
	
	public QuestOWLResultSet queryProductionToProduction_Year(Individual production, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?p ?st";
		if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?sn";
		}
		
		querytext = querytext + "\n" + "WHERE { " + production.getUri() + " :Production_Year ?o . ?p :Production_Year ?o .";
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?p a :Episode . ?p :Episode_SeriesTitle ?st .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?p a :Season . ?p :Season_Number ?sn . ?p :Season_SeriesTitle ?st .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?p a :Series . ?p :Series_Title ?st .";
		}
		
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}

//queryProductionToTag = "What tags are attached to it?";   > Tag 
//		public static final String tagAll = "All tags";
//		public static final String tagGenres = "Only genres";
	
	public QuestOWLResultSet queryProductionToTag(Individual production, String tagFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { " + production.getUri() + " :taggedAs ?x . ?x :Tag_Caption ?y . ";
		if(tagFilter.equals(Global.tagGenres)) {
			querytext = querytext + " ?x a :Genre .";
		}
		querytext = querytext + "}";
		
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
		
	}		
	
// queryProductionToCompany = "What companies produced it?";  >

	public QuestOWLResultSet queryProductionToCompany(Individual production) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?x ?y\n"+
				"WHERE { ?x :produces " + production.getUri() +
				". ?x :Company_Name ?y . }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}	
	
//queryProductionToProduction_Company = "What productions were made by the same company?";	 > Production 
	  
	public QuestOWLResultSet queryProductionToProduction_Company(Individual production, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?p ?st";
		if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?sn";
		}
		
		querytext = querytext + "\n" + "WHERE { ?c :produces " + production.getUri() + " . ?c :produces ?p .";
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?p a :Episode . ?p :Episode_SeriesTitle ?st .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?p a :Season . ?p :Season_Number ?sn . ?p :Season_SeriesTitle ?st .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?p a :Series . ?p :Series_Title ?st .";
		}
		
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
//queryProductionToProduction_Country = "What productions were made in the same country?";    > production
	 
	public QuestOWLResultSet queryProductionToProduction_Country(Individual production, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?p ?st";
		if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?sn";
		}
		
		querytext = querytext + "\n" + "WHERE { " + production.getUri() + " :producedIn ?o . ?p :producedIn ?o .";
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?p a :Episode . ?p :Episode_SeriesTitle ?st .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?p a :Season . ?p :Season_Number ?sn . ?p :Season_SeriesTitle ?st .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?p a :Series . ?p :Series_Title ?st .";
		}
		
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
	
// queryTagToProduction = "Which productions are tagged with this?"; > Production 
	
	public QuestOWLResultSet queryTagToProduction(Individual tag, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT DISTINCT ?p ?st";
		if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?sn";
		}
		
		querytext = querytext + "\n" + "WHERE { ?p :taggedAs " + tag.getUri() + " .";
		if(productionFilter.equals(Global.prodkindEpisodes)) {
			querytext = querytext + " ?p a :Episode . ?p :Episode_SeriesTitle ?st .";
		} else if(productionFilter.equals(Global.prodkindSeasons)) {
			querytext = querytext + " ?p a :Season . ?p :Season_Number ?sn . ?p :Season_SeriesTitle ?st .";
		} else if(productionFilter.equals(Global.prodkindSeries)) {
			querytext = querytext + " ?p a :Series . ?p :Series_Title ?st .";
		}
		
		querytext = querytext + " }";
		if(Global.limit > 0) {
			querytext = querytext + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(querytext);
	}
}
