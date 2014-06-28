package explorer;
import java.util.*;

public class ExplorerGuiState {
	String category;
	
	ArrayList<Individual> answerList;
	ArrayList<String> answerCaptions;
	Integer answerIndex;
	Individual answerSelection;
	
	ArrayList<ExplorerQuerySchema> schemaList;
	ArrayList<String> schemaCaptions;
	Integer schemaIndex;
	ExplorerQuerySchema schemaSelection;
	
	Boolean roleFiltering;
	ArrayList<String> roleCaptions;
	Integer roleIndex;
	String roleSelection;
	
	Boolean productionFiltering;
	ArrayList<String> productionCaptions;
	Integer productionIndex;
	String productionSelection;
	
	Boolean tagFiltering;
	ArrayList<String> tagCaptions;
	Integer tagIndex;
	String tagSelection;
	
	public ExplorerGuiState() {
		category = null;
		
		answerList = null;
		answerCaptions = null;
		answerIndex = null;
		answerSelection = null;
		
		schemaList = null;
		schemaCaptions = null;
		schemaIndex = null;
		schemaSelection = null;
		
		roleFiltering = null;
		roleCaptions = null;
		roleIndex = null;
		roleSelection = null;
		
		productionFiltering = null;
		productionCaptions = null;
		productionIndex = null;
		productionSelection = null;
		
		tagFiltering = null;
		tagCaptions  = null;;
		tagIndex = null;
		tagSelection = null;	
	}
	
	public ExplorerGuiState(ExplorerGuiState other) {
		category = other.category;
		
		answerList = other.answerList;
		answerCaptions = other.answerCaptions;
		answerIndex = other.answerIndex;
		answerSelection = other.answerSelection;
		
		schemaList = other.schemaList;
		schemaCaptions = other.schemaCaptions;
		schemaIndex = other.schemaIndex;
		schemaSelection = other.schemaSelection;
		
		roleFiltering = other.roleFiltering;
		roleCaptions = other.roleCaptions;
		roleIndex = other.roleIndex;
		roleSelection = other.roleSelection;
		
		productionFiltering = other.productionFiltering;
		productionCaptions = other.productionCaptions;
		productionIndex = other.productionIndex;
		productionSelection = other.productionSelection;
		
		tagFiltering = other.tagFiltering;
		tagCaptions  = other.tagCaptions;
		tagIndex = other.tagIndex;
		tagSelection = other.tagSelection;
	}
	
	public String getLogText() {
		String ans = "";
		if(category == null) {
			return ans;
		} else {
			ans = ans + "[[" + category + "]]" + Global.newline;
		}
		if(answerSelection == null) {
			return ans;
		} else {
			ans = ans + answerSelection.getCaption() + Global.newline;
		}
		if(schemaSelection == null) {
			return ans;
		} else {
			ans = ans + Global.newline + schemaSelection.getCaption() + Global.newline;
		}
		if(roleSelection != null) {
			ans = ans + "(" + Global.filterRole + " " + roleSelection + ".)" + Global.newline;
		}
		if(productionSelection != null) {
			ans = ans + "(" + Global.filterProduction + " " + productionSelection + ".)" + Global.newline;
		}
		if(tagSelection != null) {
			ans = ans + "(" + Global.filterTag + " " + tagSelection + ".)" + Global.newline;
		}
		return ans;
	}
	
}
