package explorer;
import java.util.*;

public class TheExplorer {
	private int state;
	private boolean querying;
	private ArrayList<ExplorerGuiState> recordList;
	private ExplorerGuiState currentRecord;
	private ExplorerDataInstance ed;
	private Seriology gui;
	private QueryEngineInstance qengine;
	
	public TheExplorer(Seriology gui) {
		ed = ExplorerData.GetInstance();
		qengine = QueryEngine.GetInstance();
		this.gui = gui;
		recordList = new ArrayList<ExplorerGuiState>();
		currentRecord = new ExplorerGuiState();
		querying = false;
		initInitial();
	}
	private void initInitial() {
		ArrayList<String> cat = ed.getCategoryList();
		for(String s : cat) {
			gui.dragdownInitial.addItem(s);
		}
		gui.dragdownInitial.setSelectedIndex(-1);
	}
	
	public void updateGuiState() {
		if(currentRecord.category == null) {
			setStateCategorySelection();
		} else if(currentRecord.answerSelection == null) {
			if(querying) {
				setStateQueryingDatabase();
			} else {
				setStateIndividualSelection();
			}
		} else if(currentRecord.schemaSelection == null) {
			setStateSchemaSelection();
		} else if((currentRecord.roleFiltering == true && currentRecord.roleSelection == null) ||
				(currentRecord.productionFiltering == true && currentRecord.productionSelection == null) ||
				(currentRecord.tagFiltering == true && currentRecord.tagSelection == null)) {
			setStateFilterSelection();
		} else {
			setStateReadyForQuerying();
		}
	}
	private void setStateCategorySelection() {
		if(state != Global.stateCategorySelection) {
			state = Global.stateCategorySelection;
			gui.setStateCategorySelection();
		}
	}
	private void setStateIndividualSelection() {
		if(state != Global.stateAnswerSelection) {
			state = Global.stateAnswerSelection;
			gui.setStateAnswerSelection();
		}
	}
	private void setStateQueryingDatabase() {
		if(state != Global.stateQueryingDatabase) {
			state = Global.stateQueryingDatabase;
			gui.setStateQueryingDatabase();
		}
	}
	private void setStateSchemaSelection() {
		if(state != Global.stateSchemaSelection) {
			state = Global.stateSchemaSelection;
			gui.setStateSchemaSelection();
		}
	}
	private void setStateFilterSelection() {
		if(state != Global.stateFilterSelection) {
			state = Global.stateFilterSelection;
			gui.setStateFilterSelection(currentRecord.roleFiltering, currentRecord.productionFiltering,
					currentRecord.tagFiltering);
		}
	}
	private void setStateReadyForQuerying() {
		if(state != Global.stateReadyforQuerying) {
			state = Global.stateReadyforQuerying;
			gui.setStateReadyForQuerying(currentRecord.roleFiltering, currentRecord.productionFiltering,
					currentRecord.tagFiltering);
		}
	}
	
	public void slotCategoryChanged(int categoryIndex) {
		resetExploration();
		ArrayList<String> cat = ed.getCategoryList();
		currentRecord.category = cat.get(categoryIndex);
		querying = true;
		updateGuiState();
		updateLog();
		ArrayList<Individual> queryAnswer = query();
		updateAnswers(queryAnswer);
		querying = false;
		updateGuiState();
	}
	public void slotAnswerChanged(int answerIndex) {
		resetToAnswer();
		currentRecord.answerIndex = answerIndex;
		currentRecord.answerSelection = currentRecord.answerList.get(answerIndex);
		currentRecord.schemaList = ed.getSchemas(currentRecord.category);
		currentRecord.schemaCaptions = new ArrayList<String>();
		for(ExplorerQuerySchema schema : currentRecord.schemaList) {
			gui.blockDragdownSchema = true;
			currentRecord.schemaCaptions.add(schema.getCaption());
			gui.dragdownSchema.addItem(schema.getCaption());
			gui.dragdownSchema.setSelectedIndex(-1);
			gui.blockDragdownSchema = false;
		}
		updateLog();
		updateGuiState();
	}
	public void slotSchemaChanged(int schemaIndex) {
		resetToSchema();
		currentRecord.schemaIndex = schemaIndex;
		currentRecord.schemaSelection = currentRecord.schemaList.get(schemaIndex);
		currentRecord.roleFiltering = currentRecord.schemaSelection.hasRoleFilter();
		if(currentRecord.roleFiltering) {
			gui.blockDragdownRole = true;
			currentRecord.roleCaptions = ed.getRoleList();
			for(String caption : currentRecord.roleCaptions) {
				gui.dragdownRole.addItem(caption);
			}
			gui.dragdownRole.setSelectedIndex(-1);
			gui.blockDragdownRole = false;
		}
		currentRecord.productionFiltering = currentRecord.schemaSelection.hasProductionFilter();
		if(currentRecord.productionFiltering) {
			gui.blockDragdownProduction = true;
			currentRecord.productionCaptions = ed.getProductionKindList();
			for(String caption : currentRecord.productionCaptions) {
				gui.dragdownProduction.addItem(caption);
			}
			gui.dragdownProduction.setSelectedIndex(-1);
			gui.blockDragdownProduction = false;
		}
		currentRecord.tagFiltering = currentRecord.schemaSelection.hasTagFilter();
		if(currentRecord.tagFiltering) {
			gui.blockDragdownTag = true;
			currentRecord.tagCaptions = ed.getTagList();
			for(String caption : currentRecord.tagCaptions) {
				gui.dragdownTag.addItem(caption);
			}
			gui.dragdownTag.setSelectedIndex(-1);
			gui.blockDragdownTag = false;
		}
		updateLog();
		updateGuiState();
	}
	public void slotRoleFilterChanged(int roleIndex) {
		currentRecord.roleIndex = roleIndex;
		currentRecord.roleSelection = currentRecord.roleCaptions.get(roleIndex);
		updateLog();
		updateGuiState();
	}
	public void slotProductionFilterChanged(int prodIndex) {
		currentRecord.productionIndex = prodIndex;
		currentRecord.productionSelection = currentRecord.productionCaptions.get(prodIndex);
		updateLog();
		updateGuiState();
	}
	public void slotTagFilterChanged(int tagIndex) {
		currentRecord.tagIndex = tagIndex;
		currentRecord.tagSelection = currentRecord.tagCaptions.get(tagIndex);
		updateLog();
		updateGuiState();
	}	
		
	public void slotNext() {
		ArrayList<Individual> queryAnswer = query();
		String newCategory = ed.nextCategory(currentRecord.schemaSelection, currentRecord.productionSelection);
		ExplorerGuiState newRecord = new ExplorerGuiState(currentRecord);
		recordList.add(newRecord);
		currentRecord = new ExplorerGuiState();
		currentRecord.category = newCategory;
		resetToCategory(queryAnswer);
		updateGuiState();
	}
	public void slotBack() {
		currentRecord = recordList.get(recordList.size()-1);
		recordList.remove(recordList.size()-1);
		gui.blockListboxAnswer = true;
		gui.blockDragdownSchema = true;
		gui.blockDragdownRole = true;
		gui.blockDragdownProduction = true;
		gui.blockDragdownTag = true;
//		recordList.clear();
//		currentRecord = new ExplorerGuiState();
		gui.listboxAnswer.removeAll();
		gui.dragdownSchema.removeAllItems();
		gui.dragdownProduction.removeAllItems();
		gui.dragdownRole.removeAllItems();
		gui.dragdownTag.removeAllItems();
		
		String[] listdata = new String[currentRecord.answerCaptions.size()];
		int count = 0;
		for(String ind : currentRecord.answerCaptions) {
			listdata[count] = ind;
			count++;
		}
		gui.listboxAnswer.setListData(listdata);
		gui.listboxAnswer.setSelectedIndex(currentRecord.answerIndex);
		
		for(String s : currentRecord.schemaCaptions) {
			gui.dragdownSchema.addItem(s);
		}
		gui.dragdownSchema.setSelectedIndex(currentRecord.schemaIndex);
		
		if(currentRecord.roleFiltering) {
			for(String s : currentRecord.roleCaptions) {
				gui.dragdownRole.addItem(s);
			}
			gui.dragdownRole.setSelectedIndex(currentRecord.roleIndex);
		}
		
		if(currentRecord.productionFiltering) {
			for(String s : currentRecord.productionCaptions) {
				gui.dragdownProduction.addItem(s);
			}
			gui.dragdownProduction.setSelectedIndex(currentRecord.productionIndex);
		}
		
		if(currentRecord.tagFiltering) {
			for(String s : currentRecord.tagCaptions) {
				gui.dragdownTag.addItem(s);
			}
			gui.dragdownTag.setSelectedIndex(currentRecord.tagIndex);
		}
		updateLog();
		gui.blockDragdownSchema = false;
		gui.blockDragdownRole = false;
		gui.blockDragdownProduction = false;
		gui.blockDragdownTag = false;
		gui.blockListboxAnswer = false;
		state = Global.stateWanderingBack;
		updateGuiState();
	}
	
	private void resetExploration() {
		gui.blockListboxAnswer = true;
		gui.blockDragdownSchema = true;
		gui.blockDragdownRole = true;
		gui.blockDragdownProduction = true;
		gui.blockDragdownTag = true;
		recordList.clear();
		currentRecord = new ExplorerGuiState();
		gui.listboxAnswer.removeAll();
		gui.dragdownSchema.removeAllItems();
		gui.dragdownProduction.removeAllItems();
		gui.dragdownRole.removeAllItems();
		gui.dragdownTag.removeAllItems();
		updateLog();
		gui.blockDragdownSchema = false;
		gui.blockDragdownRole = false;
		gui.blockDragdownProduction = false;
		gui.blockDragdownTag = false;
		gui.blockListboxAnswer = false;
	}
	private void resetToCategory(ArrayList<Individual> queryAnswer) {
		gui.blockListboxAnswer = true;
		gui.blockDragdownSchema = true;
		gui.blockDragdownRole = true;
		gui.blockDragdownProduction = true;
		gui.blockDragdownTag = true;
		currentRecord.answerSelection = null;
		currentRecord.answerIndex = null;
		currentRecord.schemaCaptions = null;
		currentRecord.schemaIndex = null;
		currentRecord.schemaList = null;
		currentRecord.schemaSelection = null;
		currentRecord.roleCaptions = null;
		currentRecord.roleFiltering = null;
		currentRecord.roleIndex = null;
		currentRecord.roleSelection = null;
		currentRecord.productionCaptions = null;
		currentRecord.productionFiltering = null;
		currentRecord.productionIndex = null;
		currentRecord.productionSelection = null;
		currentRecord.tagCaptions = null;
		currentRecord.tagFiltering = null;
		currentRecord.tagIndex = null;
		currentRecord.tagSelection = null;
		gui.dragdownSchema.removeAllItems();
		gui.dragdownProduction.removeAllItems();
		gui.dragdownRole.removeAllItems();
		gui.dragdownTag.removeAllItems();
		updateAnswers(queryAnswer);
		updateLog();
		gui.blockDragdownSchema = false;
		gui.blockDragdownRole = false;
		gui.blockDragdownProduction = false;
		gui.blockDragdownTag = false;
		gui.blockListboxAnswer = false;
	}
	private void resetToAnswer() {
		gui.blockDragdownSchema = true;
		gui.blockDragdownRole = true;
		gui.blockDragdownProduction = true;
		gui.blockDragdownTag = true;
		currentRecord.answerSelection = null;
		currentRecord.answerIndex = null;
		currentRecord.schemaCaptions = null;
		currentRecord.schemaIndex = null;
		currentRecord.schemaList = null;
		currentRecord.schemaSelection = null;
		currentRecord.roleCaptions = null;
		currentRecord.roleFiltering = null;
		currentRecord.roleIndex = null;
		currentRecord.roleSelection = null;
		currentRecord.productionCaptions = null;
		currentRecord.productionFiltering = null;
		currentRecord.productionIndex = null;
		currentRecord.productionSelection = null;
		currentRecord.tagCaptions = null;
		currentRecord.tagFiltering = null;
		currentRecord.tagIndex = null;
		currentRecord.tagSelection = null;
		gui.dragdownSchema.removeAllItems();
		gui.dragdownProduction.removeAllItems();
		gui.dragdownRole.removeAllItems();
		gui.dragdownTag.removeAllItems();
		updateLog();
		gui.blockDragdownSchema = false;
		gui.blockDragdownRole = false;
		gui.blockDragdownProduction = false;
		gui.blockDragdownTag = false;
	}
	private void resetToSchema() {
		gui.blockDragdownSchema = true;
		gui.blockDragdownRole = true;
		gui.blockDragdownProduction = true;
		gui.blockDragdownTag = true;
		currentRecord.schemaIndex = null;
		currentRecord.schemaSelection = null;
		currentRecord.roleCaptions = null;
		currentRecord.roleFiltering = null;
		currentRecord.roleIndex = null;
		currentRecord.roleSelection = null;
		currentRecord.productionCaptions = null;
		currentRecord.productionFiltering = null;
		currentRecord.productionIndex = null;
		currentRecord.productionSelection = null;
		currentRecord.tagCaptions = null;
		currentRecord.tagFiltering = null;
		currentRecord.tagIndex = null;
		currentRecord.tagSelection = null;
		gui.dragdownProduction.removeAllItems();
		gui.dragdownRole.removeAllItems();
		gui.dragdownTag.removeAllItems();
		updateLog();
		gui.blockDragdownSchema = false;
		gui.blockDragdownRole = false;
		gui.blockDragdownProduction = false;
		gui.blockDragdownTag = false;
	}
	
	private void updateLog() {
		String text = "";
		for(ExplorerGuiState st : recordList) {
			text = text + st.getLogText() + Global.newline + Global.logSeparator + Global.newline + Global.newline;
		}
		text = text + currentRecord.getLogText();
		gui.textboxLog.setText(text);
	}
	
	private ArrayList<Individual> query() {
		if(currentRecord.answerSelection == null) {
			if(currentRecord.category.equals(Global.catPerson)) {
				return Individual.parseResultSet(currentRecord.category, qengine.initialPerson());
			} else if(currentRecord.category.equals(Global.catCharacter)) {
				return Individual.parseResultSet(currentRecord.category, qengine.initialCharacter());
			} else if(currentRecord.category.equals(Global.catCompany)) {
				return Individual.parseResultSet(currentRecord.category, qengine.initialCompany());
			} else if(currentRecord.category.equals(Global.catSeries)) {
				return Individual.parseResultSet(currentRecord.category, qengine.initialSeries());
			} else if(currentRecord.category.equals(Global.catSeason)) {
				return Individual.parseResultSet(currentRecord.category, qengine.initialSeason());
			} else if(currentRecord.category.equals(Global.catEpisode)) {
				return Individual.parseResultSet(currentRecord.category, qengine.initialEpisode());
			} else if(currentRecord.category.equals(Global.catTag)) {
				return Individual.parseResultSet(currentRecord.category, qengine.initialTag());
			}
		} else {
			String category = ed.nextCategory(currentRecord.schemaSelection, currentRecord.productionSelection);
			int queryId = currentRecord.schemaSelection.getId();
			if(queryId == 0) {
				return Individual.parseResultSet(category,
						qengine.queryTagToProduction(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else
				if(queryId == 1) {
				return Individual.parseResultSet(category,
						qengine.queryPersonToProduction(currentRecord.answerSelection, currentRecord.roleSelection,
								currentRecord.productionSelection));
			} else if(queryId == 2) {
				return Individual.parseResultSet(category,
						qengine.queryPersonToPerson(currentRecord.answerSelection, currentRecord.roleSelection,
								currentRecord.productionSelection));
			} else if(queryId == 3) {
				return Individual.parseResultSet(category,
						qengine.queryPersonToCharacter(currentRecord.answerSelection));
			} else if(queryId == 4) {
				return Individual.parseResultSet(category,
						qengine.queryCompanyToCompany(currentRecord.answerSelection));
			} else if(queryId == 5) {
				return Individual.parseResultSet(category,
						qengine.queryCompanyToProduction(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 6) {
				return Individual.parseResultSet(category,
						qengine.queryCharacterToPerson(currentRecord.answerSelection));
			} else if(queryId == 7) {
				return Individual.parseResultSet(category,
						qengine.queryCharacterToProduction(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 8) {
				return Individual.parseResultSet(category,
						qengine.queryCharacterToCharacter(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 9) {
				return Individual.parseResultSet(category,
						qengine.querySeriesToSeason(currentRecord.answerSelection));
			} else if(queryId == 10) {
				return Individual.parseResultSet(category,
						qengine.querySeriesToEpisode(currentRecord.answerSelection));
			} else if(queryId == 11) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToPerson(currentRecord.answerSelection,
								currentRecord.roleSelection));
			} else if(queryId == 12) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToCharacter(currentRecord.answerSelection));
			} else if(queryId == 13) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Year(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 14) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToTag(currentRecord.answerSelection,
								currentRecord.tagSelection));
			} else if(queryId == 15) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToCompany(currentRecord.answerSelection));
			} else if(queryId == 16) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Company(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 17) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Country(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 18) {
				return Individual.parseResultSet(category,
						qengine.querySeasonToSeries(currentRecord.answerSelection));
			} else if(queryId == 19) {
				return Individual.parseResultSet(category,
						qengine.querySeasonToEpisode(currentRecord.answerSelection));
			} else if(queryId == 20) {
				return Individual.parseResultSet(category,
						qengine.querySeasonToSeason(currentRecord.answerSelection));
			} else if(queryId == 21) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToPerson(currentRecord.answerSelection,
								currentRecord.roleSelection));
			} else if(queryId == 22) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToCharacter(currentRecord.answerSelection));
			} else if(queryId == 23) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Year(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 24) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToTag(currentRecord.answerSelection,
								currentRecord.tagSelection));
			} else if(queryId == 25) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToCompany(currentRecord.answerSelection));
			} else if(queryId == 26) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Company(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 27) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Country(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 28) {
				return Individual.parseResultSet(category,
						qengine.queryEpisodeToSeries(currentRecord.answerSelection));
			} else if(queryId == 29) {
				return Individual.parseResultSet(category,
						qengine.queryEpisodeToSeason(currentRecord.answerSelection));
			} else if(queryId == 30) {
				return Individual.parseResultSet(category,
						qengine.queryEpisodeToEpisode(currentRecord.answerSelection));
			} else if(queryId == 31) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToPerson(currentRecord.answerSelection,
								currentRecord.roleSelection));
			} else if(queryId == 32) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToCharacter(currentRecord.answerSelection));
			} else if(queryId == 33) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Year(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 34) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToTag(currentRecord.answerSelection,
								currentRecord.tagSelection));
			} else if(queryId == 35) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToCompany(currentRecord.answerSelection));
			} else if(queryId == 36) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Company(currentRecord.answerSelection,
								currentRecord.productionSelection));
			} else if(queryId == 37) {
				return Individual.parseResultSet(category,
						qengine.queryProductionToProduction_Country(currentRecord.answerSelection,
								currentRecord.productionSelection));
			}
		}
		return null;
	}

	private void updateAnswers(ArrayList<Individual> queryAnswer) {
		currentRecord.answerList = queryAnswer;
		currentRecord.answerCaptions = new ArrayList<String>();
		String[] listdata = new String[queryAnswer.size()];
		int count = 0;
		for(Individual ind : queryAnswer) {
			listdata[count] = ind.getCaption();
			currentRecord.answerCaptions.add(ind.getCaption());
			count++;
		}
		gui.listboxAnswer.setListData(listdata);
	}

	public boolean isFirstQuery() {
		return (recordList.isEmpty());
	}
}
