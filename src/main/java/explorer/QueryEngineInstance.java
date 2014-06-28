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
		String query = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT ?x ?y\n" +
				"WHERE { ?x a :Person . ?x :Person_Name ?y . }";
		if(Global.limit > 0) {
			query = query + "\nLIMIT " + Global.limit.toString();
		}
		return executeQuery(query);
	}
//	
//	public QuestOWLResultSet initialCharacter() {
//
//	}
//	
//	public QuestOWLResultSet initialCompany() {
//
//	}
//	
//	public QuestOWLResultSet initialSeries() {
//
//	}
//	
//	public QuestOWLResultSet initialSeason() {
//
//	}
//	
//	public QuestOWLResultSet initialEpisode() {
//
//	}
//	
//	public QuestOWLResultSet initialTag() {
//
//	}
	
	public QuestOWLResultSet queryPersonToPerson(Individual person1, String roleFilter, String productionFilter) {
		String querytext = "PREFIX : <http://www.seriology.org/seriology#>\n" + "SELECT ?x ?y\n"+
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
}
