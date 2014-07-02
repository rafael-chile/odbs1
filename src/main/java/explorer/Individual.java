package explorer;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;

import java.util.*;

public class Individual {
	private String uri;
	private String caption;
	
	public Individual(String uri, String caption) {
		this.uri = uri;
		this.caption = caption;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public static ArrayList<Individual> parseResultSet(String category, QuestOWLResultSet rs) {
		ArrayList<Individual> ans = new ArrayList<Individual>();
		try {
			if(category.equals(Global.catSeason)) {
				while (rs.nextRow()) {
					ans.add(new Individual(rs.getOWLObject(1).toString(),
							"Season " + rs.getOWLObject(3).toString() + " [" + rs.getOWLObject(2).toString() + "]"));
				}
			} else {
				while (rs.nextRow()) {
					ans.add(new Individual(rs.getOWLObject(1).toString(), rs.getOWLObject(2).toString()));
				}
			}
			rs.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
}
	
