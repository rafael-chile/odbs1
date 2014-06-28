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
		ArrayList<Individual> ans = new ArrayList<Individual>();
		if(currentRecord.answerSelection == null) {
			
			
			
			
//			//BASE CASE
//			//TEST
//			String category = currentRecord.category;
//			for(int i = 0; i < 6; i++) {
//				Individual ind = new Individual("<"+ category + "/" + i + ">", category + " " + i);
//				ans.add(ind);
//			}
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		} else {
//			//INDUCTION CASE
//			//TEST
//			String category = ed.nextCategory(currentRecord.schemaSelection, currentRecord.productionSelection);
//			for(int i = 0; i < 6; i++) {
//				Individual ind = new Individual("<"+ category + "/" + i + ">", category + " " + i);
//				ans.add(ind);
//			}
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		return ans;
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
