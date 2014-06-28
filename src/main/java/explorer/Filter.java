package explorer;
import java.util.*;

public class Filter {
	private String mainCaption;
	private ArrayList<String> alternativesCaption;
	
	public Filter(String mainCaption) {
		this.mainCaption = mainCaption;
		alternativesCaption = new ArrayList<String>();
	}
	public String getMainCaption() {
		return mainCaption;
	}
	public void setMainCaption(String mainCaption) {
		this.mainCaption = mainCaption;
	}
	public ArrayList<String> getAlternativesCaption() {
		return alternativesCaption;
	}
	public void setAlternativesCaption(ArrayList<String> alternativesCaption) {
		this.alternativesCaption = alternativesCaption;
	}
	
	public void generateRoles() {
		mainCaption = Global.filterRole;
		alternativesCaption.add(Global.roleAll);
		alternativesCaption.add(Global.roleActors);
		alternativesCaption.add(Global.roleProducers);
		alternativesCaption.add(Global.roleWriters);
		alternativesCaption.add(Global.roleCinematographers);
		alternativesCaption.add(Global.roleComposers);
		alternativesCaption.add(Global.roleCostumeDesigners);
		alternativesCaption.add(Global.roleDirectors);
		alternativesCaption.add(Global.roleEditors);
		alternativesCaption.add(Global.roleMiscellaneous);
		alternativesCaption.add(Global.roleProductionDesigners);
		alternativesCaption.add(Global.roleGuests);
	}
	
	public void generateProductionKinds() {
		mainCaption = Global.filterProduction;
		alternativesCaption.add(Global.prodkindSeries);
		alternativesCaption.add(Global.prodkindSeasons);
		alternativesCaption.add(Global.prodkindEpisodes);
	}	
}
