package explorer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

public class Seriology extends MainForm implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private TheExplorer explorer;
	public boolean blockListboxAnswer;
	public boolean blockDragdownSchema;
	public boolean blockDragdownRole;
	public boolean blockDragdownProduction;
	public boolean blockDragdownTag;
	
	public Seriology() { 
		explorer = new TheExplorer(this);
		explorer.updateGuiState();
		setVisible(true);
		blockDragdownSchema = false;
		blockDragdownRole = false;
		blockDragdownProduction = false;
		blockDragdownTag = false;
		blockListboxAnswer = false;
		dragdownInitial.addActionListener(this);
		listboxAnswer.addListSelectionListener(this);
		dragdownSchema.addActionListener(this);
		dragdownRole.addActionListener(this);
		dragdownProduction.addActionListener(this);
		dragdownTag.addActionListener(this);
		buttonNext.addActionListener(this);
		buttonBack.addActionListener(this);
	}
	
	public static void main(String[] args) {
		new Seriology();
	}
	
	public void setStateCategorySelection() {
		dragdownInitial.setEnabled(true);
		textboxLog.setEditable(false);
		listboxAnswer.setEnabled(false);
		dragdownSchema.setEnabled(false);
		dragdownRole.setEnabled(false);
		dragdownProduction.setEnabled(false);
		dragdownTag.setEnabled(false);
		if(explorer.isFirstQuery()) {
			buttonBack.setEnabled(false);
		} else {
			buttonBack.setEnabled(true);
		}
		buttonNext.setEnabled(false);
	}
	public void setStateAnswerSelection() {
		dragdownInitial.setEnabled(true);
		textboxLog.setEditable(false);
		listboxAnswer.setEnabled(true);
		dragdownSchema.setEnabled(false);
		dragdownRole.setEnabled(false);
		dragdownProduction.setEnabled(false);
		dragdownTag.setEnabled(false);
		if(explorer.isFirstQuery()) {
			buttonBack.setEnabled(false);
		} else {
			buttonBack.setEnabled(true);
		}
		buttonNext.setEnabled(false);
	}
	public void setStateQueryingDatabase() {
		dragdownInitial.setEnabled(false);
		textboxLog.setEditable(false);
		listboxAnswer.setEnabled(true);
		dragdownSchema.setEnabled(false);
		dragdownRole.setEnabled(false);
		dragdownProduction.setEnabled(false);
		dragdownTag.setEnabled(false);
		if(explorer.isFirstQuery()) {
			buttonBack.setEnabled(false);
		} else {
			buttonBack.setEnabled(true);
		}
		buttonNext.setEnabled(false);
	}
	public void setStateSchemaSelection() {
		dragdownInitial.setEnabled(true);
		textboxLog.setEditable(false);
		listboxAnswer.setEnabled(true);
		dragdownSchema.setEnabled(true);
		dragdownRole.setEnabled(false);
		dragdownProduction.setEnabled(false);
		dragdownTag.setEnabled(false);
		if(explorer.isFirstQuery()) {
			buttonBack.setEnabled(false);
		} else {
			buttonBack.setEnabled(true);
		}
		buttonNext.setEnabled(false);
	}
	public void setStateFilterSelection(boolean role, boolean production, boolean tag) {
		dragdownInitial.setEnabled(true);
		textboxLog.setEditable(false);
		listboxAnswer.setEnabled(true);
		dragdownSchema.setEnabled(true);
		dragdownRole.setEnabled(role);
		dragdownProduction.setEnabled(production);
		dragdownTag.setEnabled(tag);
		if(explorer.isFirstQuery()) {
			buttonBack.setEnabled(false);
		} else {
			buttonBack.setEnabled(true);
		}
		buttonNext.setEnabled(false);
	}
	public void setStateReadyForQuerying(boolean role, boolean production, boolean tag) {
		dragdownInitial.setEnabled(true);
		textboxLog.setEditable(false);
		listboxAnswer.setEnabled(true);
		dragdownSchema.setEnabled(true);
		dragdownRole.setEnabled(role);
		dragdownProduction.setEnabled(production);
		dragdownTag.setEnabled(tag);
		if(explorer.isFirstQuery()) {
			buttonBack.setEnabled(false);
		} else {
			buttonBack.setEnabled(true);
		}
		buttonNext.setEnabled(true);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == dragdownInitial) {
			//SHOW QUERYING...
			explorer.slotCategoryChanged(dragdownInitial.getSelectedIndex());
			//HIDE QUERYING...
		}
		if(e.getSource() == dragdownSchema && !blockDragdownSchema) {
			explorer.slotSchemaChanged(dragdownSchema.getSelectedIndex());
		}
		if(e.getSource() == dragdownRole && !blockDragdownRole) {
			explorer.slotRoleFilterChanged(dragdownRole.getSelectedIndex());
		}
		if(e.getSource() == dragdownProduction && !blockDragdownProduction) {
			explorer.slotProductionFilterChanged(dragdownProduction.getSelectedIndex());
		}
		if(e.getSource() == dragdownTag && !blockDragdownTag) {
			explorer.slotTagFilterChanged(dragdownTag.getSelectedIndex());
		}
		if(e.getSource() == buttonNext) {
			explorer.slotNext();
		}
		if(e.getSource() == buttonBack) {
			explorer.slotBack();
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == listboxAnswer && !blockListboxAnswer) {
		    if (e.getValueIsAdjusting() == false) {
		        if (listboxAnswer.getSelectedIndex() != -1) {
					explorer.slotAnswerChanged(listboxAnswer.getSelectedIndex());
		        }
		    }
		}
	}
}
